/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.core.noise.INoiseCalculatorSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.core.noise.NoiseCalculator;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.core.noise.NoiseChromatogramSupport;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.settings.NoiseChromatogramClassifierSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.INoiseCalculator;
import org.eclipse.chemclipse.model.results.NoiseSegmentMeasurementResult;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.ISettingsHandler;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ChromatogramDataSupport;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.preferences.PreferencePage;

public class ChromatogramSignalNoiseUI extends Composite implements IExtendedPartUI {

	private AtomicReference<Button> buttonToolbarInfo = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfoTop = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfoBottom = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboViewerNoiseCalculatorControl = new AtomicReference<>();
	private AtomicReference<NoiseSegmentListUI> noiseSegmentListControl = new AtomicReference<>();
	//
	private IChromatogramSelection chromatogramSelection = null;
	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.0000");

	public ChromatogramSignalNoiseUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setInput(IChromatogramSelection chromatogramSelection) {

		this.chromatogramSelection = chromatogramSelection;
		updateInput();
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		createToolbarInfoTop(this);
		createDataSection(this);
		createToolbarInfoBottom(this);
		//
		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarInfoTop, buttonToolbarInfo.get(), IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarInfoBottom, buttonToolbarInfo.get(), IMAGE_INFO, TOOLTIP_INFO, true);
		comboViewerNoiseCalculatorControl.get().setInput(null);
	}

	private Composite createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(6, false));
		//
		createButtonToggleToolbar(composite);
		createComboViewerNoiseCalculator(composite);
		createButtonAdd(composite);
		createButtonDelete(composite);
		createButtonReset(composite);
		createSettingsButton(composite);
		//
		return composite;
	}

	private void createButtonToggleToolbar(Composite parent) {

		buttonToolbarInfo.set(createButtonToggleToolbar(parent, Arrays.asList(toolbarInfoTop, toolbarInfoBottom), IMAGE_INFO, TOOLTIP_INFO));
	}

	private void createToolbarInfoTop(Composite parent) {

		toolbarInfoTop.set(createToolbarInfo(parent));
	}

	private void createToolbarInfoBottom(Composite parent) {

		toolbarInfoBottom.set(createToolbarInfo(parent));
	}

	private InformationUI createToolbarInfo(Composite parent) {

		InformationUI informationUI = new InformationUI(parent, SWT.NONE);
		informationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		return informationUI;
	}

	private void createDataSection(Composite parent) {

		createNoiseSegmentList(parent);
	}

	private void createNoiseSegmentList(Composite parent) {

		NoiseSegmentListUI noiseSegmentListUI = new NoiseSegmentListUI(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		noiseSegmentListUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		noiseSegmentListUI.setUpdateListener(new IUpdateListener() {

			@Override
			public void update() {

				if(chromatogramSelection != null) {
					IChromatogram chromatogram = chromatogramSelection.getChromatogram();
					if(chromatogram != null) {
						chromatogram.recalculateTheNoiseFactor();
						chromatogram.setDirty(true);
						updateInput();
					}
				}
			}
		});
		//
		noiseSegmentListControl.set(noiseSegmentListUI);
	}

	private void createComboViewerNoiseCalculator(Composite parent) {

		ComboViewer comboViewer = new EnhancedComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ListContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof INoiseCalculatorSupplier noiseCalculatorSupplier) {
					return noiseCalculatorSupplier.getCalculatorName();
				}
				return null;
			}
		});
		//
		combo.setToolTipText("Select a noise calculator.");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = comboViewer.getStructuredSelection().getFirstElement();
				if(object instanceof INoiseCalculatorSupplier noiseCalculatorSupplier) {
					if(chromatogramSelection != null) {
						IChromatogram chromatogram = chromatogramSelection.getChromatogram();
						if(chromatogram != null) {
							NoiseChromatogramClassifierSettings settings = new NoiseChromatogramClassifierSettings();
							settings.setNoiseCalculatorId(noiseCalculatorSupplier.getId());
							NoiseChromatogramSupport.applyNoiseSettings(chromatogram, settings, new NullProgressMonitor());
							chromatogram.setDirty(true);
							updateInput();
						}
					}
				}
			}
		});
		//
		comboViewerNoiseCalculatorControl.set(comboViewer);
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Add a specific noise segment.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String message = NoiseChromatogramSupport.addNoiseSegment(chromatogramSelection, false);
				if(message != null) {
					MessageDialog.openInformation(e.display.getActiveShell(), "Noise Segment", message);
				} else {
					updateInput();
					UpdateNotifierUI.update(e.display, chromatogramSelection);
				}
			}
		});
	}

	private void createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete the selected noise segments.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(chromatogramSelection != null) {
					IChromatogram chromatogram = chromatogramSelection.getChromatogram();
					if(chromatogram != null) {
						NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = chromatogram.getMeasurementResult(NoiseSegmentMeasurementResult.class);
						if(noiseSegmentMeasurementResult != null) {
							List<INoiseSegment> noiseSegments = getNoiseSegmentSelection();
							noiseSegmentMeasurementResult.getResult().removeAll(noiseSegments);
						}
						chromatogram.recalculateTheNoiseFactor();
						chromatogram.setDirty(true);
						updateInput();
					}
				}
			}
		});
	}

	private List<INoiseSegment> getNoiseSegmentSelection() {

		List<INoiseSegment> noiseSegments = new ArrayList<>();
		for(Object object : noiseSegmentListControl.get().getStructuredSelection().toArray()) {
			if(object instanceof INoiseSegment noiseSegment) {
				noiseSegments.add(noiseSegment);
			}
		}
		//
		return noiseSegments;
	}

	private void createButtonReset(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Reset the noise factor calculation.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RESET, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(chromatogramSelection != null) {
					IChromatogram chromatogram = chromatogramSelection.getChromatogram();
					if(chromatogram != null) {
						if(MessageDialog.openQuestion(e.display.getActiveShell(), "Noise Factor", "Would you like to reset the noise segments?")) {
							NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = chromatogram.getMeasurementResult(NoiseSegmentMeasurementResult.class);
							if(noiseSegmentMeasurementResult != null) {
								noiseSegmentMeasurementResult.getResult().clear();
							}
							chromatogram.recalculateTheNoiseFactor();
							chromatogram.setDirty(true);
							updateInput();
						}
					}
				}
			}
		});
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePage.class), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				applySettings();
			}
		});
	}

	private void applySettings() {

		updateInput();
	}

	private void updateInput() {

		toolbarInfoTop.get().setText("");
		comboViewerNoiseCalculatorControl.get().setInput(null);
		noiseSegmentListControl.get().clear();
		toolbarInfoBottom.get().setText("");
		//
		if(chromatogramSelection != null) {
			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			if(chromatogram != null) {
				/*
				 * Trigger the NoiseCalculator setup if not done already.
				 */
				getSignalToNoiseRatio(chromatogram, 1000);
				/*
				 * Update the details.
				 */
				toolbarInfoTop.get().setText(getScanRangeInfo(chromatogramSelection));
				updateComboViewerNoiseCalculator(chromatogram);
				updateNoiseSegmentList(chromatogram);
				toolbarInfoBottom.get().setText(getNoiseFactorInfo(chromatogram));
			}
		}
	}

	private void updateComboViewerNoiseCalculator(IChromatogram chromatogram) {

		Collection<INoiseCalculatorSupplier> noiseCalculatorSuppliers = NoiseCalculator.getNoiseCalculatorSupport().getCalculatorSupplier();
		comboViewerNoiseCalculatorControl.get().setInput(noiseCalculatorSuppliers);
		//
		INoiseCalculator noiseCalculator = chromatogram.getNoiseCalculator();
		if(noiseCalculator != null) {
			exitloop:
			for(INoiseCalculatorSupplier noiseCalculatorSupplier : noiseCalculatorSuppliers) {
				INoiseCalculator reference = NoiseCalculator.getNoiseCalculator(noiseCalculatorSupplier.getId());
				if(noiseCalculator.getClass().equals(reference.getClass())) {
					comboViewerNoiseCalculatorControl.get().setSelection(new StructuredSelection(noiseCalculatorSupplier));
					break exitloop;
				}
			}
		}
	}

	private void updateNoiseSegmentList(IChromatogram chromatogram) {

		NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = chromatogram.getMeasurementResult(NoiseSegmentMeasurementResult.class);
		if(noiseSegmentMeasurementResult != null) {
			noiseSegmentListControl.get().setInput(noiseSegmentMeasurementResult.getResult());
		}
	}

	private String getScanRangeInfo(IChromatogramSelection chromatogramSelection) {

		StringBuilder builder = new StringBuilder();
		if(chromatogramSelection != null) {
			int startRetentionTime = chromatogramSelection.getStartRetentionTime();
			int stopRetentionTime = chromatogramSelection.getStopRetentionTime();
			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			builder.append(ChromatogramDataSupport.getChromatogramLabel(chromatogram));
			builder.append(" | Scan range: ");
			builder.append(chromatogram.getScanNumber(startRetentionTime));
			builder.append("–");
			builder.append(chromatogram.getScanNumber(stopRetentionTime));
			builder.append(" | RT range: ");
			builder.append(decimalFormat.format(startRetentionTime / IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
			builder.append("–");
			builder.append(decimalFormat.format(stopRetentionTime / IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
		} else {
			builder.append("No chromatogram selected.");
		}
		//
		return builder.toString();
	}

	private String getNoiseFactorInfo(IChromatogram chromatogram) {

		INoiseCalculator noiseCalculator = chromatogram.getNoiseCalculator();
		if(noiseCalculator != null) {
			int intensity = 10000;
			float signalToNoiseRatio = getSignalToNoiseRatio(chromatogram, intensity);
			StringBuilder builder = new StringBuilder();
			/*
			 * Details
			 */
			builder.append("Name: ");
			builder.append(noiseCalculator.getName());
			builder.append(" | ");
			builder.append("Noise Factor: ");
			builder.append(decimalFormat.format(noiseCalculator.getNoiseFactor()));
			builder.append(" | ");
			builder.append("S/N (");
			builder.append(Integer.toString(intensity));
			builder.append("): ");
			builder.append(decimalFormat.format(signalToNoiseRatio));
			return builder.toString();
		} else {
			return "The chromatogram offers no noise calculation yet.";
		}
	}

	private float getSignalToNoiseRatio(IChromatogram chromatogram, float intensity) {

		return chromatogram.getSignalToNoiseRatio(intensity);
	}
}
