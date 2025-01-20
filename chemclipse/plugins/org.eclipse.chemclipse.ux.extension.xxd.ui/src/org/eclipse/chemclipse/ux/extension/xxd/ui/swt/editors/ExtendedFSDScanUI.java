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
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.chemclipse.fsd.model.core.ISignalFSD;
import org.eclipse.chemclipse.fsd.model.core.ISpectrumFSD;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.swt.ui.preferences.PreferencePageSystem;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.xxd.ui.charts.ChartFSD;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageChromatogram;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ISettingsHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class ExtendedFSDScanUI extends Composite implements IExtendedPartUI {

	private ChartFSD chartFSD;
	private ISpectrumFSD spectrumFSD;

	public ExtendedFSDScanUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void update(ISpectrumFSD spectrumFSD) {

		this.spectrumFSD = spectrumFSD;
		updateScan();
	}

	private void updateScan() {

		chartFSD.deleteSeries();
		//
		if(spectrumFSD != null) {
			List<ILineSeriesData> lineSeriesDataList = new ArrayList<>();
			lineSeriesDataList.add(getExcitationLineSeriesData());
			lineSeriesDataList.add(getEmissionLineSeriesData());
			chartFSD.addSeriesData(lineSeriesDataList);
		}
	}

	private LineSeriesData getExcitationLineSeriesData() {

		LineSeriesData lineSeriesData = new LineSeriesData(getSeriesData(spectrumFSD.getExcitation(), "Excitation"));
		ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setLineColor(Colors.BLACK);
		lineSeriesSettings.setEnableArea(false);
		return lineSeriesData;
	}

	private LineSeriesData getEmissionLineSeriesData() {

		LineSeriesData lineSeriesData = new LineSeriesData(getSeriesData(spectrumFSD.getEmission(), "Emission"));
		ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setLineColor(Colors.RED);
		lineSeriesSettings.setEnableArea(false);
		return lineSeriesData;
	}

	private ISeriesData getSeriesData(TreeSet<ISignalFSD> signalsFSD, String id) {

		double[] xSeries;
		double[] ySeries;
		//
		if(signalsFSD != null) {
			int size = signalsFSD.size();
			xSeries = new double[size];
			ySeries = new double[size];
			int index = 0;
			for(ISignalFSD scanSignal : signalsFSD) {
				xSeries[index] = scanSignal.getWavelength();
				ySeries[index] = scanSignal.getIntensity();
				index++;
			}
		} else {
			xSeries = new double[0];
			ySeries = new double[0];
		}
		return new SeriesData(xSeries, ySeries, id);
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		createScanChart(this);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(8, false));
		//
		createToggleChartSeriesLegendButton(composite);
		createToggleLegendMarkerButton(composite);
		createToggleRangeSelectorButton(composite);
		createResetButton(composite);
		createSettingsButton(composite);
	}

	private void createToggleChartSeriesLegendButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle the chart series legend.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_TAG, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				chartFSD.toggleSeriesLegendVisibility();
			}
		});
	}

	private void createToggleLegendMarkerButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle the chart legend marker.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CHART_LEGEND_MARKER, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				chartFSD.togglePositionLegendVisibility();
				chartFSD.redraw();
			}
		});
	}

	private void createToggleRangeSelectorButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle the chart range selector.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CHART_RANGE_SELECTOR, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				chartFSD.toggleRangeSelectorVisibility();
			}
		});
	}

	private void createResetButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the scan");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RESET, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				reset();
			}
		});
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePageChromatogram.class, PreferencePageSystem.class), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				applySettings();
			}
		});
	}

	private void applySettings() {

		updateScan();
	}

	private void reset() {

		updateScan();
	}

	private void createScanChart(Composite parent) {

		chartFSD = new ChartFSD(parent, SWT.BORDER);
		chartFSD.setLayoutData(new GridData(GridData.FILL_BOTH));
		/*
		 * Chart Settings
		 */
		IChartSettings chartSettings = chartFSD.getChartSettings();
		chartSettings.setCreateMenu(true);
		chartSettings.setEnableRangeSelector(true);
		chartSettings.setShowRangeSelectorInitially(false);
		//
		chartFSD.applySettings(chartSettings);
	}
}
