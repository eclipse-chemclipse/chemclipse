/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IVendorMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.support.FilterSupport;
import org.eclipse.chemclipse.msd.model.support.ScanSupport;
import org.eclipse.chemclipse.msd.swt.ui.support.DatabaseFileSupport;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageScans;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageSubtract;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class ExtendedSubtractScanUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(ExtendedSubtractScanUI.class);
	//
	private TabFolder tabFolder;
	private ScanChartUI scanChartUI;
	private ExtendedScanTableUI extendedScanTableUI;
	//
	private AtomicReference<Button> buttonSelectedScanControl = new AtomicReference<>();
	private AtomicReference<Button> buttonCombinedScanControl = new AtomicReference<>();
	//
	private IScanMSD scanMSD = null;
	private IChromatogramSelectionMSD chromatogramSelectionMSD = null;

	@Inject
	public ExtendedSubtractScanUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	@Focus
	public boolean setFocus() {

		boolean focus = super.setFocus();
		updateScanData(scanMSD);
		return focus;
	}

	public void update(Object object) {

		if(object instanceof IChromatogramSelectionMSD) {
			chromatogramSelectionMSD = (IChromatogramSelectionMSD)object;
		} else if(object instanceof IScanMSD) {
			scanMSD = (IScanMSD)object;
		} else if(object == null) {
			chromatogramSelectionMSD = null;
		}
		//
		updateScanData(scanMSD);
	}

	private void createControl() {

		setLayout(new FillLayout());
		//
		Composite composite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);
		//
		createToolbarMain(composite);
		createScanTabFolderSection(composite);
		//
		loadSessionMassSpectrum(composite.getDisplay());
		updateWidgets();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(6, false));
		//
		createAddSelectedScanButton(composite);
		createAddCombinedScanButton(composite);
		createClearSessionButton(composite);
		createButtonCopyTracesClipboard(composite);
		createSaveButton(composite);
		createSettingsButton(composite);
	}

	private void createScanTabFolderSection(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(1, true));
		//
		tabFolder = new TabFolder(composite, SWT.BOTTOM);
		tabFolder.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateScanData(scanMSD);
			}
		});
		//
		createScanChart(tabFolder);
		createScanTable(tabFolder);
	}

	private void createScanChart(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Chart");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		tabItem.setControl(composite);
		//
		scanChartUI = new ScanChartUI(composite, SWT.BORDER);
		scanChartUI.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	private void createScanTable(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Table");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite.setLayout(new GridLayout(1, true));
		tabItem.setControl(composite);
		//
		extendedScanTableUI = new ExtendedScanTableUI(composite, SWT.NONE);
		extendedScanTableUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		extendedScanTableUI.forceEnableEditModus(true);
		extendedScanTableUI.setFireUpdate(false);
		//
		extendedScanTableUI.addEditListener(new EditListener() {

			@Override
			public void modify() {

				saveSessionMassSpectrum(null, scanMSD);
			}
		});
	}

	private void createAddSelectedScanButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Add selected scan to subtract spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SUBTRACT_ADD_SELECTED_SCAN, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(chromatogramSelectionMSD != null && chromatogramSelectionMSD.getSelectedScan() != null) {
					/*
					 * Add the selected scan to the session MS.
					 */
					IScanMSD massSpectrum1 = PreferenceSupplier.getSessionSubtractMassSpectrum();
					CalculationType calculationType = PreferenceSupplier.getCalculationType();
					IVendorMassSpectrum massSpectrum2 = chromatogramSelectionMSD.getSelectedScan();
					boolean useNormalize = org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.isUseNormalizedScan();
					IScanMSD subtractMassSpectrum = FilterSupport.getCombinedMassSpectrum(massSpectrum1, massSpectrum2, null, useNormalize, calculationType);
					saveSessionMassSpectrum(e.display, subtractMassSpectrum);
				}
			}
		});
		//
		buttonSelectedScanControl.set(button);
	}

	private void createAddCombinedScanButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Add combined scan to subtract spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SUBTRACT_ADD_COMBINED_SCAN, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(chromatogramSelectionMSD != null) {
					boolean useNormalize = PreferenceSupplier.isUseNormalizedScan();
					CalculationType calculationType = PreferenceSupplier.getCalculationType();
					boolean usePeaksInsteadOfScans = PreferenceSupplier.isUsePeaksInsteadOfScans();
					IScanMSD massSpectrum1 = PreferenceSupplier.getSessionSubtractMassSpectrum();
					IScanMSD massSpectrum2 = FilterSupport.getCombinedMassSpectrum(chromatogramSelectionMSD, null, useNormalize, calculationType, usePeaksInsteadOfScans);
					IScanMSD subtractMassSpectrum = FilterSupport.getCombinedMassSpectrum(massSpectrum1, massSpectrum2, null, useNormalize, calculationType);
					saveSessionMassSpectrum(e.display, subtractMassSpectrum);
				}
			}
		});
		//
		buttonCombinedScanControl.set(button);
	}

	private void createClearSessionButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Clear the session spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SUBTRACT_CLEAR_SESSION_MASS_SPECTRUM, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				MessageBox messageBox = new MessageBox(e.display.getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messageBox.setText("Clear Session");
				messageBox.setMessage("Would you like to clear the session subtract scan?");
				if(messageBox.open() == SWT.YES) {
					//
					scanMSD = null;
					updateScanData(scanMSD);
					saveSessionMassSpectrum(e.display, null);
				}
			}
		});
	}

	private Button createButtonCopyTracesClipboard(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Copy the traces to clipboard.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_COPY_CLIPBOARD, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int maxCopyTraces = PreferenceSupplier.getCopyTracesClipboard();
				String traces = ScanSupport.extractTracesText(scanMSD, maxCopyTraces);
				TextTransfer textTransfer = TextTransfer.getInstance();
				Object[] data = new Object[]{traces};
				Transfer[] dataTypes = new Transfer[]{textTransfer};
				Clipboard clipboard = new Clipboard(e.widget.getDisplay());
				clipboard.setContents(data, dataTypes);
			}
		});
		//
		return button;
	}

	private Button createSaveButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Save the subtract scan.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SAVE_AS, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					if(scanMSD != null) {
						DatabaseFileSupport.saveMassSpectrum(e.display.getActiveShell(), scanMSD, "SubtractMS");
					}
				} catch(NoConverterAvailableException e1) {
					logger.warn(e1);
				}
			}
		});
		return button;
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePageScans.class, PreferencePageSubtract.class), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				applySettings();
			}
		});
	}

	private void applySettings() {

		updateScanData(scanMSD);
	}

	private void updateScanData(IScanMSD scanMSD) {

		if(tabFolder != null && scanChartUI != null) {
			/*
			 * Chart
			 */
			if(scanMSD == null) {
				scanChartUI.deleteSeries();
				scanChartUI.getBaseChart().redraw();
			} else {
				scanChartUI.setInput(scanMSD);
			}
			/*
			 * Table
			 */
			if(extendedScanTableUI.isVisible()) {
				extendedScanTableUI.setInput(scanMSD);
			}
		}
		//
		updateWidgets();
	}

	private void loadSessionMassSpectrum(Display display) {

		PreferenceSupplier.loadSessionSubtractMassSpectrum();
		fireUpdateEvent(display);
	}

	/**
	 * If the display is set to null, no event is fired.
	 * 
	 * @param display
	 * @param scanMSD
	 */
	private void saveSessionMassSpectrum(Display display, IScanMSD scanMSD) {

		PreferenceSupplier.setSessionSubtractMassSpectrum(scanMSD);
		PreferenceSupplier.storeSessionSubtractMassSpectrum();
		//
		if(display != null) {
			fireUpdateEvent(display);
		}
	}

	private void updateWidgets() {

		boolean enabled = chromatogramSelectionMSD != null;
		buttonSelectedScanControl.get().setEnabled(enabled);
		buttonCombinedScanControl.get().setEnabled(enabled);
	}

	private void fireUpdateEvent(Display display) {

		UpdateNotifierUI.update(display, IChemClipseEvents.TOPIC_UPDATE_SESSION_SUBTRACT_MASS_SPECTRUM, true);
	}
}
