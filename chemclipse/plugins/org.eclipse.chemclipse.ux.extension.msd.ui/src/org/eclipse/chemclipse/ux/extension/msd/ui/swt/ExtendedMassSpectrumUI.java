/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.swt.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.ux.extension.msd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.ui.swt.ChartGridSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.ISettingsHandler;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public class ExtendedMassSpectrumUI extends Composite implements IExtendedPartUI {

	private AtomicReference<Composite> toolbarMainControl = new AtomicReference<>();
	private AtomicReference<MassSpectraSelectionUI> toolbarSelectionControl = new AtomicReference<>();
	private IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	private ChartGridSupport chartGridSupport = new ChartGridSupport();

	private IMassSpectrumChart massSpectrumChart;

	private IMassSpectra massSpectra;
	private IScanMSD massSpectrum;

	public ExtendedMassSpectrumUI(Composite parent, int style) {

		super(parent, style);
	}

	public void update(IMassSpectra massSpectra) {

		this.massSpectra = massSpectra;
		createControl();
		toolbarSelectionControl.get().update(massSpectra);
		massSpectrum = massSpectra.getMassSpectrum(1);
		massSpectrumChart.update(massSpectrum);
	}

	public void update(IScanMSD massSpectrum) {

		this.massSpectrum = massSpectrum;
		massSpectrumChart.update(massSpectrum);
	}

	private void createControl() {

		setLayout(new FillLayout());
		createPages(this);
	}

	private void createPages(Composite parent) {

		if(massSpectra != null && massSpectra.getMassSpectrum(1) != null) {
			massSpectrum = massSpectra.getMassSpectrum(1);
			createMassSpectrumPage(parent);
		} else {
			createErrorMessagePage(parent);
		}
	}

	private void createMassSpectrumPage(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));

		createToolbarMain(composite);
		createToolbarSelection(composite);
		createMassSpectrumChart(composite);
	}

	private void createMassSpectrumChart(Composite composite) {

		if(isProfile()) {
			massSpectrumChart = new MassSpectrumChartProfile(composite, SWT.BORDER);
		} else {
			massSpectrumChart = new MassSpectrumChartCentroid(composite, SWT.BORDER);
		}
		massSpectrumChart.update(massSpectrum);
	}

	private boolean isProfile() {

		if(massSpectrum instanceof IRegularMassSpectrum regularMassSpectrum) {
			return regularMassSpectrum.getMassSpectrumType() == MassSpectrumType.PROFILE;
		} else {
			return PreferenceSupplier.useProfileMassSpectrumView();
		}
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = GridData.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(4, false));
		//
		createButtonToggleSelection(composite);
		createButtonToggleChartGrid(composite);
		createToggleChartSeriesLegendButton(composite);
		createButtonSettings(composite);
		//
		toolbarMainControl.set(composite);
	}

	private void createToolbarSelection(Composite parent) {

		MassSpectraSelectionUI massSpectraSelectionUI = new MassSpectraSelectionUI(parent, SWT.NONE);
		massSpectraSelectionUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		massSpectraSelectionUI.setVisible(PreferenceSupplier.isSelectionComboVisible());
		toolbarSelectionControl.set(massSpectraSelectionUI);
		massSpectraSelectionUI.update(massSpectra);
		massSpectraSelectionUI.addSelectionChangeListener(createSelectionChangedListener());
	}

	private void createErrorMessagePage(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		Label label = new Label(composite, SWT.NONE);
		label.setText("The mass spectrum couldn't be loaded.");
	}

	private Button createButtonToggleSelection(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarSelectionControl, IApplicationImage.IMAGE_EXPAND_ALL, "Selection toolbar.");
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				preferenceStore.setValue(PreferenceSupplier.P_SHOW_MASS_SPECTRUM_SELECTION_COMBO, toolbarSelectionControl.get().isVisible());
			}
		});
		return button;
	}

	private ISelectionChangedListener createSelectionChangedListener() {

		return new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				if(event.getSelection() instanceof IStructuredSelection selection) {
					if(selection.getFirstElement() instanceof IScanMSD scanMSD) {
						massSpectrum = scanMSD;
						massSpectrumChart.update(massSpectrum);
						UpdateNotifier.update(massSpectrum);
					}
				}
			}
		};
	}

	private Button createButtonToggleChartGrid(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setText("");
		setButtonImage(button, IApplicationImage.IMAGE_GRID, "Show", "Hide", "Chart Grid", false);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(massSpectrumChart instanceof ScrollableChart scrollableChart) {
					IChartSettings chartSettings = scrollableChart.getChartSettings();
					boolean isGridDisplayed = !chartGridSupport.isGridDisplayed(chartSettings);
					chartGridSupport.showGrid(scrollableChart.getChartSettings(), isGridDisplayed);
					scrollableChart.applySettings(chartSettings);
					setButtonImage(button, IApplicationImage.IMAGE_GRID, "Show", "Hide", "Chart Grid", isGridDisplayed);
				}
			}
		});
		return button;
	}

	private void createToggleChartSeriesLegendButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle the chart series legend.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_TAG, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(massSpectrumChart instanceof ScrollableChart scrollableChart) {
					scrollableChart.toggleSeriesLegendVisibility();
				}
			}
		});
	}

	private void createButtonSettings(Composite parent) {

		createSettingsButton(parent, getPreferencePagesSupplier(), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				massSpectrumChart.update();
			}

		}, false);
	}

	private Supplier<List<Class<? extends IPreferencePage>>> getPreferencePagesSupplier() {

		return new Supplier<List<Class<? extends IPreferencePage>>>() {

			@Override
			public List<Class<? extends IPreferencePage>> get() {

				List<Class<? extends IPreferencePage>> preferencePages = new ArrayList<>();
				preferencePages.add(org.eclipse.chemclipse.msd.swt.ui.preferences.PreferencePage.class);
				return preferencePages;
			}
		};
	}

}