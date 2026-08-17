/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.model.PenaltyCalculationModel;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePagePeakTraces;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.PeakDataSupport;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.extensions.preferences.PreferencePage;

public class ExtendedPenaltyCalculationUI extends Composite implements IExtendedPartUI {

	private static final String TOOLTIP_CALCULATION = "calculation details.";
	private static final String IMAGE_CALCULATION = IApplicationImage.IMAGE_CALCULATE;

	private AtomicReference<Button> buttonToolbarInfo = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfo = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarCalculation = new AtomicReference<>();
	private AtomicReference<PenaltyCalculationUI> toolbarCalculation = new AtomicReference<>();
	private AtomicReference<PenaltyCalculationChart> chartControl = new AtomicReference<>();

	private IPeak peak = null;

	public ExtendedPenaltyCalculationUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	@Focus
	public boolean setFocus() {

		update(peak);
		return true;
	}

	public void update(IPeak peak) {

		this.peak = peak;
		updateLabel();
		updatePeak();
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		setLayout(gridLayout);

		createToolbarMain(this);
		createToolbarInfo(this);
		createToolbarCalculation(this);
		createChart(this);

		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarInfo, buttonToolbarInfo.get(), IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarCalculation, buttonToolbarCalculation.get(), IMAGE_CALCULATION, TOOLTIP_CALCULATION, true);
		applySettings();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(3, false));

		createButtonInfo(composite);
		createButtonCalculation(composite);
		createButtonSettings(composite);
	}

	private void createButtonInfo(Composite parent) {

		buttonToolbarInfo.set(createButtonToggleToolbar(parent, toolbarInfo, IMAGE_INFO, TOOLTIP_INFO));
	}

	private void createButtonCalculation(Composite parent) {

		buttonToolbarCalculation.set(createButtonToggleToolbar(parent, toolbarCalculation, IMAGE_CALCULATION, TOOLTIP_CALCULATION));
	}

	private void createToolbarInfo(Composite parent) {

		InformationUI informationUI = new InformationUI(parent, SWT.NONE);
		informationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		toolbarInfo.set(informationUI);
	}

	private void createToolbarCalculation(Composite parent) {

		PenaltyCalculationUI penaltyCalculationUI = new PenaltyCalculationUI(parent, SWT.NONE);
		penaltyCalculationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		penaltyCalculationUI.setUpdateListener(() -> {
			if(peak != null) {
				PenaltyCalculationModel penaltyCalculationModel = penaltyCalculationUI.getPenaltyCalculationModel();
				if(penaltyCalculationModel != null) {
					chartControl.get().setInput(penaltyCalculationModel);
				}
			}
		});

		toolbarCalculation.set(penaltyCalculationUI);
	}

	private void createChart(Composite parent) {

		PenaltyCalculationChart penaltyCalculationChart = new PenaltyCalculationChart(parent, SWT.BORDER);
		penaltyCalculationChart.setLayoutData(new GridData(GridData.FILL_BOTH));

		chartControl.set(penaltyCalculationChart);
	}

	private void createButtonSettings(Composite parent) {

		createSettingsButton(parent, Arrays.asList( //
				PreferencePagePeakTraces.class, //
				PreferencePage.class //
		), _ -> applySettings());
	}

	private void applySettings() {

		updatePeak();
	}

	private void updateLabel() {

		PeakDataSupport peakDataSupport = new PeakDataSupport();
		toolbarInfo.get().setText(peakDataSupport.getPeakLabel(peak));
	}

	private void updatePeak() {

		chartControl.get().setInput(peak);
	}
}