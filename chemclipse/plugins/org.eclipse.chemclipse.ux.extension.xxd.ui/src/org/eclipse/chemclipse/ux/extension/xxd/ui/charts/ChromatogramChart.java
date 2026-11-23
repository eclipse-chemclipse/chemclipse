/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - support usage of custom preference store
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.charts;

import org.eclipse.chemclipse.ux.extension.xxd.ui.custom.IRangeSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.axisconverter.MillisecondsToMinuteConverter;
import org.eclipse.swtchart.extensions.axisconverter.MillisecondsToSecondsConverter;
import org.eclipse.swtchart.extensions.axisconverter.PercentageConverter;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;

public class ChromatogramChart extends LineChart implements IRangeSupport {

	private String titleSeconds = "";
	private String titleMinutes = "";
	private String titleRelativeIntensity = "";
	private String intensityUnit = "";

	private Range selectedRangeX = null;
	private Range selectedRangeY = null;

	public ChromatogramChart() {

		super();
		initialize();
	}

	public ChromatogramChart(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	@Override
	public void clearSelectedRange() {

		selectedRangeX = null;
		selectedRangeY = null;
	}

	@Override
	public void assignCurrentRangeSelection() {

		BaseChart baseChart = getBaseChart();
		selectedRangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
		selectedRangeY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();
	}

	@Override
	public Range getCurrentRangeX() {

		BaseChart baseChart = getBaseChart();
		IAxisSet axisSet = baseChart.getAxisSet();
		IAxis xAxis = axisSet.getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		return new Range(xAxis.getRange().lower, xAxis.getRange().upper);
	}

	@Override
	public void updateRangeX(Range selectedRangeX) {

		updateRange(selectedRangeX, selectedRangeY);
	}

	@Override
	public Range getCurrentRangeY() {

		BaseChart baseChart = getBaseChart();
		IAxisSet axisSet = baseChart.getAxisSet();
		IAxis yAxis = axisSet.getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		return new Range(yAxis.getRange().lower, yAxis.getRange().upper);
	}

	@Override
	public void updateRangeY(Range selectedRangeY) {

		updateRange(selectedRangeX, selectedRangeY);
	}

	@Override
	public void updateRange(Range selectedRangeX, Range selectedRangeY) {

		this.selectedRangeX = selectedRangeX;
		this.selectedRangeY = selectedRangeY;
		adjustChartRange();
	}

	@Override
	public void adjustChartRange() {

		setRange(IExtendedChart.X_AXIS, selectedRangeX);
		setRange(IExtendedChart.Y_AXIS, selectedRangeY);
		redrawChart();
	}

	/**
	 * Modifies the x and y axis set given in accordance to the given settings.
	 */
	public void modifyAxes(boolean applySettings) {

		/*
		 * Adjust Title, Format, ...
		 */
		adjustAxisMilliseconds();
		adjustAxisIntensity();
		adjustAxisSeconds();
		adjustAxisMinutes();
		adjustAxisRelativeIntensity();

		if(applySettings) {
			IChartSettings chartSettings = getChartSettings();
			applySettings(chartSettings);
		}
	}

	/**
	 * Sets the unit shown in brackets after the intensity axis title.
	 * Call modifyAxes afterwards to apply it.
	 */
	public void setIntensityUnit(String intensityUnit) {

		this.intensityUnit = intensityUnit != null ? intensityUnit : "";
	}

	public String getIntensityUnit() {

		return intensityUnit;
	}

	public String getTitleAxisRelativeIntensity() {

		return titleRelativeIntensity;
	}

	public ISecondaryAxisSettings getAxisSettingsSeconds(IChartSettings chartSettings) {

		return ChartSupport.getSecondaryAxisSettingsX(titleSeconds, chartSettings);
	}

	public ISecondaryAxisSettings getAxisSettingsMinutes(IChartSettings chartSettings) {

		return ChartSupport.getSecondaryAxisSettingsX(titleMinutes, chartSettings);
	}

	private void initialize() {

		/*
		 * Initialize secondary axis titles.
		 */
		titleSeconds = ExtensionMessages.seconds;
		titleMinutes = ExtensionMessages.minutes;
		titleRelativeIntensity = ExtensionMessages.relativeIntensity;

		IChartSettings chartSettings = getChartSettings();
		chartSettings.setTitle("");
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(false);
		// Use background from StyleSheet.
		chartSettings.setBackground(null);
		chartSettings.setBackgroundChart(null);
		chartSettings.setBackgroundPlotArea(null);

		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setZeroX(true);
		rangeRestriction.setZeroY(true);
		rangeRestriction.setReferenceZoomZeroX(false);
		rangeRestriction.setReferenceZoomZeroY(true);
		rangeRestriction.setRestrictZoomX(false);
		rangeRestriction.setRestrictZoomY(true);

		modifyAxes(true);

		setData("org.eclipse.e4.ui.css.CssClassName", "ChromatogramChart");
	}

	private void adjustAxisMilliseconds() {

		IChartSettings chartSettings = getChartSettings();
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle(ExtensionMessages.miliseconds);

		String positionNode = PreferenceSupplier.P_POSITION_X_AXIS_MILLISECONDS;
		String patternNode = PreferenceSupplier.P_FORMAT_X_AXIS_MILLISECONDS;
		String gridLineStyleNode = PreferenceSupplier.P_GRIDLINE_STYLE_X_AXIS_MILLISECONDS;

		ChartSupport.setAxisSettingsExtended(primaryAxisSettingsX, positionNode, patternNode, gridLineStyleNode);
		ChartSupport.themeAxis(primaryAxisSettingsX, ChromatogramChart.class.getName() + ".AxisMilliseconds");
		primaryAxisSettingsX.setVisible(ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_X_AXIS_MILLISECONDS));
		primaryAxisSettingsX.setTitleVisible(ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_X_AXIS_TITLE_MILLISECONDS));
	}

	private void adjustAxisIntensity() {

		IChartSettings chartSettings = getChartSettings();
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle(AxisTitleSupport.getAxisTitle(ExtensionMessages.intensity, intensityUnit));

		String positionNode = PreferenceSupplier.P_POSITION_Y_AXIS_INTENSITY;
		String patternNode = PreferenceSupplier.P_FORMAT_Y_AXIS_INTENSITY;
		String gridLineStyleNode = PreferenceSupplier.P_GRIDLINE_STYLE_Y_AXIS_INTENSITY;

		ChartSupport.setAxisSettingsExtended(primaryAxisSettingsY, positionNode, patternNode, gridLineStyleNode);
		ChartSupport.themeAxis(primaryAxisSettingsY, ChromatogramChart.class.getName() + ".AxisIntensity");
		primaryAxisSettingsY.setVisible(ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_Y_AXIS_INTENSITY));
		primaryAxisSettingsY.setTitleVisible(ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_Y_AXIS_TITLE_INTENSITY));
	}

	private void adjustAxisRelativeIntensity() {

		IChartSettings chartSettings = getChartSettings();
		ISecondaryAxisSettings axisSettings = ChartSupport.getSecondaryAxisSettingsY(titleRelativeIntensity, chartSettings);

		String positionNode = PreferenceSupplier.P_POSITION_Y_AXIS_RELATIVE_INTENSITY;
		String patternNode = PreferenceSupplier.P_FORMAT_Y_AXIS_RELATIVE_INTENSITY;
		String gridLineStyleNode = PreferenceSupplier.P_GRIDLINE_STYLE_Y_AXIS_RELATIVE_INTENSITY;

		boolean isShowAxis = ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_Y_AXIS_RELATIVE_INTENSITY);
		boolean isShowAxisTitle = ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_Y_AXIS_TITLE_RELATIVE_INTENSITY);

		String title = ExtensionMessages.relativeIntensity;

		if(isShowAxis) {
			if(axisSettings == null) {
				ISecondaryAxisSettings secondaryAxisSettingsY = new SecondaryAxisSettings(title, new PercentageConverter(SWT.VERTICAL, true));
				ChartSupport.setAxisSettingsExtended(secondaryAxisSettingsY, positionNode, patternNode, gridLineStyleNode);
				ChartSupport.themeAxis(secondaryAxisSettingsY, ChromatogramChart.class.getName() + ".AxisRelativeIntensity");
				secondaryAxisSettingsY.setTitleVisible(isShowAxisTitle);
				chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY);
			} else {
				ChartSupport.setAxisSettingsExtended(axisSettings, positionNode, patternNode, gridLineStyleNode);
				ChartSupport.themeAxis(axisSettings, ChromatogramChart.class.getName() + ".AxisRelativeIntensity");
				axisSettings.setTitle(title);
				axisSettings.setVisible(true);
				axisSettings.setTitleVisible(isShowAxisTitle);
			}
		} else {
			if(axisSettings != null) {
				axisSettings.setTitle(title);
				axisSettings.setVisible(false);
				axisSettings.setTitleVisible(isShowAxisTitle);
			}
		}
		/*
		 * Update the title to retrieve the correct axis.
		 */
		titleRelativeIntensity = title;
	}

	private void adjustAxisSeconds() {

		IChartSettings chartSettings = getChartSettings();
		ISecondaryAxisSettings axisSettings = getAxisSettingsSeconds(chartSettings);

		String positionNode = PreferenceSupplier.P_POSITION_X_AXIS_SECONDS;
		String patternNode = PreferenceSupplier.P_FORMAT_X_AXIS_SECONDS;
		String gridLineStyleNode = PreferenceSupplier.P_GRIDLINE_STYLE_X_AXIS_SECONDS;
		boolean isShowAxis = ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_X_AXIS_SECONDS);
		boolean isShowAxisTitle = ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_X_AXIS_TITLE_SECONDS);

		String title = ExtensionMessages.seconds;

		if(isShowAxis) {
			if(axisSettings == null) {
				ISecondaryAxisSettings secondaryAxisSettingsX = new SecondaryAxisSettings(title, new MillisecondsToSecondsConverter());
				ChartSupport.setAxisSettingsExtended(secondaryAxisSettingsX, positionNode, patternNode, gridLineStyleNode);
				ChartSupport.themeAxis(secondaryAxisSettingsX, ChromatogramChart.class.getName() + ".AxisSeconds");
				secondaryAxisSettingsX.setTitleVisible(isShowAxisTitle);
				chartSettings.getSecondaryAxisSettingsListX().add(secondaryAxisSettingsX);
			} else {
				ChartSupport.setAxisSettingsExtended(axisSettings, positionNode, patternNode, gridLineStyleNode);
				ChartSupport.themeAxis(axisSettings, ChromatogramChart.class.getName() + ".AxisSeconds");
				axisSettings.setTitle(title);
				axisSettings.setVisible(true);
				axisSettings.setTitleVisible(isShowAxisTitle);
			}
		} else {
			if(axisSettings != null) {
				axisSettings.setTitle(title);
				axisSettings.setVisible(false);
				axisSettings.setTitleVisible(isShowAxisTitle);
			}
		}
		/*
		 * Update the title to retrieve the correct axis.
		 */
		titleSeconds = title;
	}

	private void adjustAxisMinutes() {

		IChartSettings chartSettings = getChartSettings();
		ISecondaryAxisSettings axisSettings = getAxisSettingsMinutes(chartSettings);

		String positionNode = PreferenceSupplier.P_POSITION_X_AXIS_MINUTES;
		String patternNode = PreferenceSupplier.P_FORMAT_X_AXIS_MINUTES;
		String gridLineStyleNode = PreferenceSupplier.P_GRIDLINE_STYLE_X_AXIS_MINUTES;
		boolean isShowAxis = ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_X_AXIS_MINUTES);
		boolean isShowAxisTitle = ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_X_AXIS_TITLE_MINUTES);

		String title = ExtensionMessages.minutes;
		boolean drawAxisLine = ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_X_AXIS_LINE_MINUTES);
		boolean drawPositionMarker = ChartSupport.getBoolean(PreferenceSupplier.P_SHOW_X_AXIS_POSITION_MARKER_MINUTES);

		if(isShowAxis) {
			if(axisSettings == null) {
				ISecondaryAxisSettings secondaryAxisSettingsX = new SecondaryAxisSettings(title, new MillisecondsToMinuteConverter());
				ChartSupport.setAxisSettingsExtended(secondaryAxisSettingsX, positionNode, patternNode, gridLineStyleNode);
				ChartSupport.themeAxis(secondaryAxisSettingsX, ChromatogramChart.class.getName() + ".AxisMinutes");
				secondaryAxisSettingsX.setTitleVisible(isShowAxisTitle);
				secondaryAxisSettingsX.setDrawAxisLine(drawAxisLine);
				secondaryAxisSettingsX.setDrawPositionMarker(drawPositionMarker);
				chartSettings.getSecondaryAxisSettingsListX().add(secondaryAxisSettingsX);
			} else {
				ChartSupport.setAxisSettingsExtended(axisSettings, positionNode, patternNode, gridLineStyleNode);
				ChartSupport.themeAxis(axisSettings, ChromatogramChart.class.getName() + ".AxisMinutes");
				axisSettings.setTitle(title);
				axisSettings.setVisible(true);
				axisSettings.setTitleVisible(isShowAxisTitle);
				axisSettings.setDrawAxisLine(drawAxisLine);
				axisSettings.setDrawPositionMarker(drawPositionMarker);
			}
		} else {
			if(axisSettings != null) {
				axisSettings.setTitle(title);
				axisSettings.setVisible(false);
				axisSettings.setTitleVisible(isShowAxisTitle);
				axisSettings.setDrawAxisLine(drawAxisLine);
				axisSettings.setDrawPositionMarker(drawPositionMarker);
			}
		}
		/*
		 * Update the title to retrieve the correct axis.
		 */
		titleMinutes = title;
	}

	private void redrawChart() {

		getBaseChart().redraw();
	}
}