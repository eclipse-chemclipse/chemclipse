/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - add NMR datatype, remove obsolete constants, extract init into static methods
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.preferences;

import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.model.traces.NamedTraceUtil;
import org.eclipse.chemclipse.pcr.model.core.support.LabelSetting;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.support.PartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.editors.ChromatogramEditorTSD;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.ux.extension.xxd.ui.model.TracesExportOption;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.Resources;
import org.eclipse.swtchart.extensions.linecharts.LineChart;

public class PreferenceSupplier extends AbstractPreferenceSupplier implements IPreferenceSupplier {

	public static final String[][] PART_STACKS = new String[][]{ //
			{"--", PartSupport.PARTSTACK_NONE}, //
			{ExtensionMessages.leftTop, PartSupport.PARTSTACK_LEFT_TOP}, //
			{ExtensionMessages.leftCenter, PartSupport.PARTSTACK_LEFT_CENTER}, //
			{ExtensionMessages.rightTop, PartSupport.PARTSTACK_RIGHT_TOP}, //
			{ExtensionMessages.bottomLeft, PartSupport.PARTSTACK_BOTTOM_LEFT}, //
			{ExtensionMessages.bottomCenter, PartSupport.PARTSTACK_BOTTOM_CENTER}, //
			{ExtensionMessages.bottomRight, PartSupport.PARTSTACK_BOTTOM_RIGHT}//
	};
	//
	public static final int MIN_SYMBOL_SIZE = 1;
	public static final int MAX_SYMBOL_SIZE = 72;
	public static final int DEF_SYMBOL_SIZE = 5;
	//
	public static final int MIN_FONT_SIZE = 1;
	public static final int MAX_FONT_SIZE = 72;
	public static final int DEF_FONT_SIZE = 11;
	//
	public static final int MIN_TRACES = 1;
	public static final int MAX_TRACES = Integer.MAX_VALUE;
	public static final int DEF_PEAK_TRACES = 5;
	public static final int DEF_SCAN_TRACES = 5;
	//
	public static final int MIN_TRACES_VIRTUAL_TABLE = 2500;
	public static final int MAX_TRACES_VIRTUAL_TABLE = 25000;
	//
	public static final int MIN_OFFSET_RETENTION_TIME = 0;
	public static final int MAX_OFFSET_RETENTION_TIME = 120000; // 120 Seconds (2 Minutes)
	public static final int DEF_OFFSET_RETENTION_TIME = 0;
	//
	public static final int MIN_SIM_IONS = 1;
	public static final int MAX_SIM_IONS = 50;
	//
	public static final int MIN_LENGTH_NAME_EXPORT = 1;
	public static final int MAX_LENGTH_NAME_EXPORT = 1000;
	//
	public static final int MIN_HEATMAP_SCALE_INTENSITY = 1;
	public static final int MAX_HEATMAP_SCALE_INTENSITY = 10000;
	//
	public static final float MIN_DEVIATION_RELATIVE = 0.0f;
	public static final float MAX_DEVIATION_RELATIVE = 100.0f;
	public static final int MIN_DEVIATION_RETENTION_TIME = 0;
	public static final int MAX_DEVIATION_RETENTION_TIME = Integer.MAX_VALUE;
	public static final float MIN_DEVIATION_RETENTION_INDEX = 0.0f;
	public static final float MAX_DEVIATION_RETENTION_INDEX = Float.MAX_VALUE;
	public static final float MIN_MATCH_QUALITY = 0.0f;
	public static final float MAX_MATCH_QUALITY = 100.0f;
	//
	public static final int MIN_MODULO_AUTO_MIRROR = 2;
	public static final int MAX_MODULO_AUTO_MIRROR = 100;
	public static final int DEF_MODULO_AUTO_MIRROR = 2;
	//
	public static final double MIN_RANGE = -Double.MAX_VALUE;
	public static final double MAX_RANGE = Double.MAX_VALUE;
	//
	public static final int MIN_TIME_RANGE_SELECTION_OFFSET = -1; // No Selection
	public static final int MAX_TIME_RANGE_SELECTION_OFFSET = Integer.MAX_VALUE;
	/*
	 * General / Task Quick Access
	 */
	public static final String P_STACK_POSITION_HEADER_DATA = "stackPositionMeasurementHeader";
	public static final String DEF_STACK_POSITION_MEASUREMENT_HEADER = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_CHROMATOGRAM_OVERVIEW = "stackPositionChromatogramOverview";
	public static final String DEF_STACK_POSITION_CHROMATOGRAM_OVERVIEW = PartSupport.PARTSTACK_LEFT_CENTER;
	public static final String P_STACK_POSITION_CHROMATOGRAM_STATISTICS = "stackPositionChromatogramStatistics";
	public static final String DEF_STACK_POSITION_CHROMATOGRAM_STATISTICS = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_CHROMATOGRAM_SIGNAL_NOISE = "stackPositionChromatogramSignalNoise";
	public static final String DEF_STACK_POSITION_CHROMATOGRAM_SIGNAL_NOISE = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_CHROMATOGRAM_SCAN_INFO = "stackPositionChromatogramScanInfo";
	public static final String DEF_STACK_POSITION_CHROMATOGRAM_SCAN_INFO = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_OVERLAY_CHROMATOGRAM_DEFAULT = "stackPositionOverlayChromatogramDefault";
	public static final String DEF_STACK_POSITION_OVERLAY_CHROMATOGRAM_DEFAULT = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_OVERLAY_CHROMATOGRAM_EXTRA = "stackPositionOverlayChromatogramExtra";
	public static final String DEF_STACK_POSITION_OVERLAY_CHROMATOGRAM_EXTRA = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_OVERLAY_NMR = "stackPositionOverlayNMR";
	public static final String DEF_STACK_POSITION_OVERLAY_NMR = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_OVERLAY_VSD = "stackPositionOverlayVSD";
	public static final String DEF_STACK_POSITION_OVERLAY_VSD = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_BASELINE_CHROMATOGRAM = "stackPositionBaseline";
	public static final String DEF_STACK_POSITION_BASELINE_CHROMATOGRAM = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_TARGETS = "stackPositionTargets";
	public static final String DEF_STACK_POSITION_TARGETS = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_SCAN_CHART = "stackPositionScanChart";
	public static final String DEF_STACK_POSITION_SCAN_CHART = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_SCAN_TABLE = "stackPositionScanTable";
	public static final String DEF_STACK_POSITION_SCAN_TABLE = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_SCAN_BROWSE = "stackPositionScanBrowse";
	public static final String DEF_STACK_POSITION_SCAN_BROWSE = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_SYNONYMS = "stackPositionSynonyms";
	public static final String DEF_STACK_POSITION_SYNONYMS = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_COLUMN_INDICES = "stackPositionColumnIndices";
	public static final String DEF_STACK_POSITION_COLUMN_INDICES = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_FLAVOR_MARKER = "stackPositionFlavorMarker";
	public static final String DEF_STACK_POSITION_FLAVOR_MARKER = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_LITERATURE = "stackPositionLiterature";
	public static final String DEF_STACK_POSITION_LITERATURE = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_CAS_NUMBERS = "stackPositionCasNumbers";
	public static final String DEF_STACK_POSITION_CAS_NUMBERS = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_MOLECULE = "stackPositionMolecule";
	public static final String DEF_STACK_POSITION_MOLECULE = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_PEAK_CHART = "stackPositionPeakChart";
	public static final String DEF_STACK_POSITION_PEAK_CHART = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_PEAK_DETAILS = "stackPositionPeakDetails";
	public static final String DEF_STACK_POSITION_PEAK_DETAILS = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_PEAK_DETECTOR = "stackPositionPeakDetector";
	public static final String DEF_STACK_POSITION_PEAK_DETECTOR = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_PEAK_TRACES = "stackPositionPeakTraces";
	public static final String DEF_STACK_POSITION_PEAK_TRACES = PartSupport.PARTSTACK_RIGHT_TOP;
	public static final String P_STACK_POSITION_PEAK_SCAN_LIST = "stackPositionPeakScanList";
	public static final String DEF_STACK_POSITION_PEAK_SCAN_LIST = PartSupport.PARTSTACK_LEFT_TOP;
	public static final String P_STACK_POSITION_PEAK_QUANTITATION_LIST = "stackPositionPeakQuantitationList";
	public static final String DEF_STACK_POSITION_PEAK_QUANTITATION_LIST = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_SUBTRACT_SCAN_PART = "stackPositionSubtractScanPart";
	public static final String DEF_STACK_POSITION_SUBTRACT_SCAN_PART = PartSupport.PARTSTACK_RIGHT_TOP;
	public static final String P_STACK_POSITION_COMBINED_SCAN_PART = "stackPositionCombinedScanPart";
	public static final String DEF_STACK_POSITION_COMBINED_SCAN_PART = PartSupport.PARTSTACK_RIGHT_TOP;
	public static final String P_STACK_POSITION_COMPARISON_SCAN_CHART = "stackPositionComparisonScanChart";
	public static final String DEF_STACK_POSITION_COMPARISON_SCAN_CHART = PartSupport.PARTSTACK_RIGHT_TOP;
	public static final String P_STACK_POSITION_QUANTITATION = "stackPositionQuantitation";
	public static final String DEF_STACK_POSITION_QUANTITATION = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_INTEGRATION_AREA = "stackPositionIntegrationArea";
	public static final String DEF_STACK_POSITION_INTEGRATION_AREA = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_INTERNAL_STANDARDS = "stackPositionInternalStandards";
	public static final String DEF_STACK_POSITION_INTERNAL_STANDARDS = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_MEASUREMENT_RESULTS = "stackPositionMeasurementResults";
	public static final String DEF_STACK_POSITION_MEASUREMENT_RESULTS = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_CHROMATOGRAM_INDICES = "stackPositionChromatogramIndices";
	public static final String DEF_STACK_POSITION_CHROMATOGRAM_INDICES = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_CHROMATOGRAM_HEATMAP = "stackPositionChromatogramHeatmap";
	public static final String DEF_STACK_POSITION_CHROMATOGRAM_HEATMAP = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_PEAK_QUANTITATION_REFERENCES = "stackPositionPeakQuantitationReferences";
	public static final String DEF_STACK_POSITION_PEAK_QUANTITATION_REFERENCES = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_QUANT_RESPONSE_CHART = "stackPositionQuantResponseChart";
	public static final String DEF_STACK_POSITION_QUANT_RESPONSE_CHART = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	public static final String P_STACK_POSITION_QUANT_RESPONSE_LIST = "stackPositionQuantResponseList";
	public static final String DEF_STACK_POSITION_QUANT_RESPONSE_LIST = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_QUANT_PEAKS_CHART = "stackPositionQuantPeaksChart";
	public static final String DEF_STACK_POSITION_QUANT_PEAKS_CHART = PartSupport.PARTSTACK_BOTTOM_CENTER;
	public static final String P_STACK_POSITION_QUANT_PEAKS_LIST = "stackPositionQuantPeaksList";
	public static final String DEF_STACK_POSITION_QUANT_PEAKS_LIST = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_QUANT_SIGNALS_LIST = "stackPositionQuantSignalsList";
	public static final String DEF_STACK_POSITION_QUANT_SIGNALS_LIST = PartSupport.PARTSTACK_BOTTOM_LEFT;
	public static final String P_STACK_POSITION_PENALTY_CALCULATION = "stackPositionPenaltyCalculation";
	public static final String DEF_STACK_POSITION_PENALTY_CALCULATION = PartSupport.PARTSTACK_BOTTOM_RIGHT;
	/*
	 * Overlay
	 */
	public static final String P_OVERLAY_CHART_COMPRESSION_TYPE = "overlayChartCompressionType";
	public static final String DEF_OVERLAY_CHART_COMPRESSION_TYPE = LineChart.COMPRESSION_MEDIUM;
	public static final String P_SHOW_REFERENCED_CHROMATOGRAMS = "showReferencedChromatograms";
	public static final boolean DEF_SHOW_REFERENCED_CHROMATOGRAMS = true;
	public static final String P_OVERLAY_ADD_TYPE_INFO = "overlayAddTypeInfo";
	public static final boolean DEF_OVERLAY_ADD_TYPE_INFO = true;
	public static final String P_COLOR_SCHEME_DISPLAY_OVERLAY = "colorSchemeDisplayOverlay";
	public static final String DEF_COLOR_SCHEME_DISPLAY_OVERLAY = Colors.COLOR_SCHEME_PRINT;
	public static final String P_LINE_STYLE_DISPLAY_OVERLAY = "lineStyleDisplayOverlay";
	public static final String DEF_LINE_STYLE_DISPLAY_OVERLAY = LineStyle.SOLID.toString();
	public static final String P_SHOW_OPTIMIZED_CHROMATOGRAM_XWC = "showOptimizedChromatogramXWC";
	public static final boolean DEF_SHOW_OPTIMIZED_CHROMATOGRAM_XWC = true;
	public static final String P_MODULO_AUTO_MIRROR_CHROMATOGRAMS = "moduloAutoMirrorChromatograms";
	public static final int DEF_MODULO_AUTO_MIRROR_CHROMATOGRAMS = DEF_MODULO_AUTO_MIRROR;
	/*
	 * Peak Traces
	 */
	public static final String P_COLOR_SCHEME_PEAK_TRACES = "colorSchemePeakTraces";
	public static final String DEF_COLOR_SCHEME_PEAK_TRACES = Colors.COLOR_SCHEME_PRINT;
	public static final String P_MAX_DISPLAY_PEAK_TRACES = "maxDisplayPeakTraces";
	public static final int DEF_MAX_DISPLAY_PEAK_TRACES = DEF_PEAK_TRACES;
	public static final String P_PEAK_TRACES_OFFSET_RETENTION_TIME = "peakTracesOffsetRetentionTime";
	public static final int DEF_PEAK_TRACES_OFFSET_RETENTION_TIME = DEF_OFFSET_RETENTION_TIME;
	public static final String P_CHROMATOGRAM_OVERLAY_NAMED_TRACES = "chromatogramOverlayNamedTraces";
	public static final String DEF_CHROMATOGRAM_OVERLAY_NAMED_TRACES = NamedTraceUtil.getDefaultTraces();
	//
	public static final String P_OVERLAY_SHIFT_X = "overlayShiftX";
	public static final double DEF_OVERLAY_SHIFT_X = 0.0d;
	public static final double MIN_OVERLAY_SHIFT_X = 0.0d;
	public static final double MAX_OVERLAY_SHIFT_X = Double.MAX_VALUE;
	public static final String P_INDEX_SHIFT_X = "indexShiftX";
	public static final int DEF_INDEX_SHIFT_X = 0;
	public static final int MIN_INDEX_SHIFT_X = 0;
	public static final int MAX_INDEX_SHIFT_X = 100;
	public static final String P_OVERLAY_SHIFT_Y = "overlayShiftY";
	public static final double DEF_OVERLAY_SHIFT_Y = 0.0d;
	public static final double MIN_OVERLAY_SHIFT_Y = 0.0d;
	public static final double MAX_OVERLAY_SHIFT_Y = Double.MAX_VALUE;
	public static final String P_INDEX_SHIFT_Y = "indexShiftY";
	public static final int DEF_INDEX_SHIFT_Y = 0;
	public static final int MIN_INDEX_SHIFT_Y = 0;
	public static final int MAX_INDEX_SHIFT_Y = 100;
	//
	public static final String P_OVERLAY_SHOW_AREA = "overlayShowArea";
	public static final boolean DEF_OVERLAY_SHOW_AREA = false;
	public static final String P_OVERLAY_AUTOFOCUS_PROFILE_SETTINGS = "overlayAutofocusProfileSettings";
	public static final boolean DEF_OVERLAY_AUTOFOCUS_PROFILE_SETTINGS = true;
	public static final String P_OVERLAY_AUTOFOCUS_SHIFT_SETTINGS = "overlayAutofocusShiftSettings";
	public static final boolean DEF_OVERLAY_AUTOFOCUS_SHIFT_SETTINGS = true;
	public static final String P_OVERLAY_LOCK_ZOOM = "overlayLockZoom";
	public static final boolean DEF_OVERLAY_LOCK_ZOOM = false;
	public static final String P_OVERLAY_FOCUS_SELECTION = "overlayFocusSelection";
	public static final boolean DEF_OVERLAY_FOCUS_SELECTION = false;
	/*
	 * Header Data
	 */
	public static final String P_HEADER_DATA_USE_RICH_TEXT_EDITOR = "headerDataUseRichTextEditor";
	public static final boolean DEF_HEADER_DATA_USE_RICH_TEXT_EDITOR = false;
	/*
	 * Scans
	 */
	public static final String P_SCAN_LABEL_FONT_NAME = "scanLabelFontName";
	public static final String DEF_SCAN_LABEL_FONT_NAME = Resources.DEFAULT_FONT_NAME;
	public static final String P_SCAN_LABEL_FONT_SIZE = "scanLabelFontSize";
	public static final String P_SCAN_LABEL_FONT_STYLE = "scanLabelFontStyle";
	public static final int DEF_SCAN_LABEL_FONT_STYLE = SWT.NORMAL;
	public static final String P_COLOR_SCAN_1 = "colorScan1";
	public static final String DEF_COLOR_SCAN_1 = "255,0,0";
	public static final String P_COLOR_SCAN_2 = "colorScan2";
	public static final String DEF_COLOR_SCAN_2 = "0,0,0";
	public static final String P_SCAN_LABEL_HIGHEST_INTENSITIES = "scanLabelHighestIntensities";
	public static final int MIN_SCAN_LABEL_HIGHEST_INTENSITIES = 1;
	public static final int MAX_SCAN_LABEL_HIGHEST_INTENSITIES = 32;
	public static final int DEF_SCAN_LABEL_HIGHEST_INTENSITIES = 5;
	public static final String P_SCAN_LABEL_MODULO_INTENSITIES = "scanLabelModuloIntensities";
	public static final boolean DEF_SCAN_LABEL_MODULO_INTENSITIES = false;
	public static final String P_AUTOFOCUS_SUBTRACT_SCAN_PART = "autofocusSubtractScanPart";
	public static final boolean DEF_AUTOFOCUS_SUBTRACT_SCAN_PART = true;
	public static final String P_SCAN_CHART_ENABLE_COMPRESS = "scanChartEnableCompress";
	public static final boolean DEF_SCAN_CHART_ENABLE_COMPRESS = false;
	public static final String P_MAX_COPY_SCAN_TRACES = "maxCopyScanTraces";
	public static final int DEF_MAX_COPY_SCAN_TRACES = DEF_SCAN_TRACES;
	public static final String P_SORT_COPY_TRACES = "sortCopyTraces";
	public static final boolean DEF_SORT_COPY_TRACES = true;
	public static final String P_TRACES_EXPORT_OPTION = "tracesExportOption";
	public static final String DEF_TRACES_EXPORT_OPTION = TracesExportOption.SIMPLE_TEXT.name();
	public static final String P_SCAN_IDENTIFER_MSD = "scanIdentifierMSD";
	public static final String DEF_SCAN_IDENTIFER_MSD = "org.eclipse.chemclipse.chromatogram.msd.identifier.supplier.unknown.massSpectrum"; // see -> org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file/plugin.xml
	public static final String P_SCAN_IDENTIFER_WSD = "scanIdentifierWSD";
	public static final String DEF_SCAN_IDENTIFER_WSD = "org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.unknown.waveSpectrum"; // see -> org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file/plugin.xml
	public static final String P_ENABLE_MULTI_SUBTRACT = "enableMultiSubtract";
	public static final boolean DEF_ENABLE_MULTI_SUBTRACT = false;
	public static final String P_SHOW_SUBTRACT_DIALOG = "showSubtractDialog";
	public static final boolean DEF_SHOW_SUBTRACT_DIALOG = true;
	public static final String P_TARGET_IDENTIFER = "targetIdentifier";
	public static final String DEF_TARGET_IDENTIFER = "org.eclipse.chemclipse.xxd.identifier.supplier.pubchem.identifier";
	public static final String P_SCAN_IDENTIFER = "scanIdentifier";
	public static final String DEF_SCAN_IDENTIFER = "";
	//
	public static final String P_SCAN_CHART_ENABLE_FIXED_RANGE_X = "scanChartEnableFixedRangeX";
	public static final boolean DEF_SCAN_CHART_ENABLE_FIXED_RANGE_X = false;
	public static final String P_SCAN_CHART_FIXED_RANGE_START_X = "scanChartFixedRangeStartX";
	public static final double DEF_SCAN_CHART_FIXED_RANGE_START_X = 0.0d;
	public static final String P_SCAN_CHART_FIXED_RANGE_STOP_X = "scanChartFixedRangeStopX";
	public static final double DEF_SCAN_CHART_FIXED_RANGE_STOP_X = 0.0d;
	public static final String P_SCAN_CHART_ENABLE_FIXED_RANGE_Y = "scanChartEnableFixedRangeY";
	public static final boolean DEF_SCAN_CHART_ENABLE_FIXED_RANGE_Y = false;
	public static final String P_SCAN_CHART_FIXED_RANGE_START_Y = "scanChartFixedRangeStartY";
	public static final double DEF_SCAN_CHART_FIXED_RANGE_START_Y = 0.0d;
	public static final String P_SCAN_CHART_FIXED_RANGE_STOP_Y = "scanChartFixedRangeStopY";
	public static final double DEF_SCAN_CHART_FIXED_RANGE_STOP_Y = 0.0d;
	//
	public static final String P_TITLE_X_AXIS_MZ = "titleXAxisMZ";
	public static final String DEF_TITLE_X_AXIS_MZ = "Ion [m/z]";
	public static final String P_TITLE_X_AXIS_PARENT_MZ = "titleXAxisParentMZ";
	public static final String DEF_TITLE_X_AXIS_PARENT_MZ = "Parent Ion [m/z]";
	public static final String P_TITLE_X_AXIS_PARENT_RESOLUTION = "titleXAxisParentResolution";
	public static final String DEF_TITLE_X_AXIS_PARENT_RESOLUTION = "Parent Resolution";
	public static final String P_TITLE_X_AXIS_DAUGHTER_MZ = "titleXAxisDaughterMZ";
	public static final String DEF_TITLE_X_AXIS_DAUGHTER_MZ = "Daughther Ion [m/z]";
	public static final String P_TITLE_X_AXIS_DAUGHTER_RESOLUTION = "titleXAxisDaughterResolution";
	public static final String DEF_TITLE_X_AXIS_DAUGHTER_RESOLUTION = "Daughter Resolution";
	public static final String P_TITLE_X_AXIS_COLLISION_ENERGY = "titleXAxisCollisionEnergy";
	public static final String DEF_TITLE_X_AXIS_COLLISION_ENERGY = "Collision Energy [eV]";
	public static final String P_TITLE_X_AXIS_WAVELENGTH = "titleXAxisWavelength";
	public static final String DEF_TITLE_X_AXIS_WAVELENGTH = "Wavelength [nm]";
	//
	public static final String P_TRACES_VIRTUAL_TABLE = "tracesVirtualTable";
	public static final int DEF_TRACES_VIRTUAL_TABLE = 5000;
	public static final String P_LIMIT_SIM_TRACES = "limitSimTraces";
	public static final int DEF_LIMIT_SIM_TRACES = 5;
	//
	public static final String P_HEATMAP_SCALE_INTENSITY_MIN_MSD = "heatmapScaleIntensityMinMSD";
	public static final int DEF_HEATMAP_SCALE_INTENSITY_MIN_MSD = 1;
	public static final String P_HEATMAP_SCALE_INTENSITY_MAX_MSD = "heatmapScaleIntensityMaxMSD";
	public static final int DEF_HEATMAP_SCALE_INTENSITY_MAX_MSD = 2500;
	public static final String P_HEATMAP_SCALE_INTENSITY_MIN_WSD = "heatmapScaleIntensityMinWSD";
	public static final int DEF_HEATMAP_SCALE_INTENSITY_MIN_WSD = 1;
	public static final String P_HEATMAP_SCALE_INTENSITY_MAX_WSD = "heatmapScaleIntensityMaxWSD";
	public static final int DEF_HEATMAP_SCALE_INTENSITY_MAX_WSD = 1;
	public static final String P_HEATMAP_SCALE_INTENSITY_MIN_TSD = "heatmapScaleIntensityMinTSD";
	public static final int DEF_HEATMAP_SCALE_INTENSITY_MIN_TSD = 1;
	public static final String P_HEATMAP_SCALE_INTENSITY_MAX_TSD = "heatmapScaleIntensityMaxTSD";
	public static final int DEF_HEATMAP_SCALE_INTENSITY_MAX_TSD = 250;
	public static final String P_HEATMAP_ENABLE_ZOOM = "enableZoom";
	public static final boolean DEF_HEATMAP_ENABLE_ZOOM = true;
	/*
	 * Peaks
	 */
	public static final String P_SHOW_PEAK_BASELINE = "showPeakBaseline";
	public static final boolean DEF_SHOW_PEAK_BASELINE = true;
	public static final String P_COLOR_PEAK_BASELINE = "colorPeakBaseline";
	public static final String DEF_COLOR_PEAK_BASELINE = "0,0,0";
	public static final String P_SHOW_PEAK = "showPeak";
	public static final boolean DEF_SHOW_PEAK = true;
	public static final String P_COLOR_PEAK_1 = "colorPeak1";
	public static final String DEF_COLOR_PEAK_1 = "255,0,0";
	public static final String P_COLOR_PEAK_2 = "colorPeak2";
	public static final String DEF_COLOR_PEAK_2 = "0,0,0";
	public static final String P_SHOW_PEAK_TANGENTS = "showPeakTangents";
	public static final boolean DEF_SHOW_PEAK_TANGENTS = true;
	public static final String P_COLOR_PEAK_TANGENTS = "colorPeakTangents";
	public static final String DEF_COLOR_PEAK_TANGENTS = "0,0,0";
	public static final String P_SHOW_PEAK_WIDTH_50 = "showPeakWidth50";
	public static final boolean DEF_SHOW_PEAK_WIDTH_50 = true;
	public static final String P_COLOR_PEAK_WIDTH_50 = "colorPeakWidth50";
	public static final String DEF_COLOR_PEAK_WIDTH_50 = "0,0,0";
	public static final String P_SHOW_PEAK_WIDTH_0 = "showPeakWidth0";
	public static final boolean DEF_SHOW_PEAK_WIDTH_0 = false;
	public static final String P_COLOR_PEAK_WIDTH_0 = "colorPeakWidth0";
	public static final String DEF_COLOR_PEAK_WIDTH_0 = "0,0,0";
	public static final String P_SHOW_PEAK_WIDTH_CONDAL_BOSH = "showPeakWidthCondalBosh";
	public static final boolean DEF_SHOW_PEAK_WIDTH_CONDAL_BOSH = false;
	public static final String P_COLOR_PEAK_WIDTH_CONDAL_BOSH = "colorPeakWidthCondalBosh";
	public static final String DEF_COLOR_PEAK_WIDTH_CONDAL_BOSH = "0,0,0";
	public static final String P_COLOR_PEAK_DETECTOR_CHROMATOGRAM = "colorPeakDetectorChromatogram";
	public static final String DEF_COLOR_PEAK_DETECTOR_CHROMATOGRAM = "255,0,0";
	public static final String P_SHOW_PEAK_DETECTOR_CHROMATOGRAM_AREA = "showPeakDetectorChromatogramArea";
	public static final boolean DEF_SHOW_PEAK_DETECTOR_CHROMATOGRAM_AREA = false;
	public static final String P_PEAK_DETECTOR_SCAN_MARKER_SIZE = "showPeakDetectorScanMarkerSize";
	public static final int DEF_PEAK_DETECTOR_SCAN_MARKER_SIZE = 2;
	public static final String P_PEAK_DETECTOR_SCAN_MARKER_COLOR = "showPeakDetectorScanMarkerColor";
	public static final String DEF_PEAK_DETECTOR_SCAN_MARKER_COLOR = "255,0,0";
	public static final String P_PEAK_DETECTOR_SCAN_MARKER_TYPE = "showPeakDetectorScanMarkerType";
	public static final String DEF_PEAK_DETECTOR_SCAN_MARKER_TYPE = PlotSymbolType.NONE.toString();
	/*
	 * Peak Chart
	 */
	public static final String P_COLOR_SCHEME_DISPLAY_PEAKS = "colorSchemeDisplayPeaks";
	public static final String DEF_COLOR_SCHEME_DISPLAY_PEAKS = Colors.COLOR_SCHEME_PRINT;
	public static final String P_SHOW_AREA_DISPLAY_PEAKS = "showAreaDisplayPeaks";
	public static final boolean DEF_SHOW_AREA_DISPLAY_PEAKS = false;
	//
	public static final String P_SHOW_X_AXIS_MILLISECONDS_PEAKS = "showXAxisMillisecondsPeaks";
	public static final boolean DEF_SHOW_X_AXIS_MILLISECONDS_PEAKS = false;
	public static final String P_POSITION_X_AXIS_MILLISECONDS_PEAKS = "positionXAxisMillisecondsPeaks";
	public static final String DEF_POSITION_X_AXIS_MILLISECONDS_PEAKS = Position.Secondary.toString();
	public static final String P_COLOR_X_AXIS_MILLISECONDS_PEAKS = "colorXAxisMillisecondsPeaks";
	public static final String DEF_COLOR_X_AXIS_MILLISECONDS_PEAKS = "0,0,0";
	public static final String P_GRIDLINE_STYLE_X_AXIS_MILLISECONDS_PEAKS = "gridlineStyleXAxisMillisecondsPeaks";
	public static final String DEF_GRIDLINE_STYLE_X_AXIS_MILLISECONDS_PEAKS = LineStyle.DOT.toString();
	public static final String P_GRIDLINE_COLOR_X_AXIS_MILLISECONDS_PEAKS = "gridlineColorXAxisMillisecondsPeaks";
	public static final String DEF_GRIDLINE_COLOR_X_AXIS_MILLISECONDS_PEAKS = "192,192,192";
	//
	public static final String P_SHOW_X_AXIS_MINUTES_PEAKS = "showXAxisMinutesPeaks";
	public static final boolean DEF_SHOW_X_AXIS_MINUTES_PEAKS = true;
	public static final String P_POSITION_X_AXIS_MINUTES_PEAKS = "positionXAxisMinutesPeaks";
	public static final String DEF_POSITION_X_AXIS_MINUTES_PEAKS = Position.Primary.toString();
	public static final String P_COLOR_X_AXIS_MINUTES_PEAKS = "colorXAxisMinutesPeaks";
	public static final String DEF_COLOR_X_AXIS_MINUTES_PEAKS = "0,0,0";
	public static final String P_GRIDLINE_STYLE_X_AXIS_MINUTES_PEAKS = "gridlineStyleXAxisMinutesPeaks";
	public static final String DEF_GRIDLINE_STYLE_X_AXIS_MINUTES_PEAKS = LineStyle.DOT.toString();
	public static final String P_GRIDLINE_COLOR_X_AXIS_MINUTES_PEAKS = "gridlineColorXAxisMinutesPeaks";
	public static final String DEF_GRIDLINE_COLOR_X_AXIS_MINUTES_PEAKS = "192,192,192";
	//
	public static final String P_SHOW_Y_AXIS_INTENSITY_PEAKS = "showYAxisIntensityPeaks";
	public static final boolean DEF_SHOW_Y_AXIS_INTENSITY_PEAKS = true;
	public static final String P_POSITION_Y_AXIS_INTENSITY_PEAKS = "positionYAxisIntensityPeaks";
	public static final String DEF_POSITION_Y_AXIS_INTENSITY_PEAKS = Position.Primary.toString();
	public static final String P_COLOR_Y_AXIS_INTENSITY_PEAKS = "colorYAxisIntensityPeaks";
	public static final String DEF_COLOR_Y_AXIS_INTENSITY_PEAKS = "0,0,0";
	public static final String P_GRIDLINE_STYLE_Y_AXIS_INTENSITY_PEAKS = "gridlineStyleYAxisIntensityPeaks";
	public static final String DEF_GRIDLINE_STYLE_Y_AXIS_INTENSITY_PEAKS = LineStyle.NONE.toString();
	public static final String P_GRIDLINE_COLOR_Y_AXIS_INTENSITY_PEAKS = "gridlineColorYAxisIntensityPeaks";
	public static final String DEF_GRIDLINE_COLOR_Y_AXIS_INTENSITY_PEAKS = "192,192,192";
	//
	public static final String P_SHOW_Y_AXIS_RELATIVE_INTENSITY_PEAKS = "showYAxisRelativeIntensityPeaks";
	public static final boolean DEF_SHOW_Y_AXIS_RELATIVE_INTENSITY_PEAKS = true;
	public static final String P_POSITION_Y_AXIS_RELATIVE_INTENSITY_PEAKS = "positionYAxisRelativeIntensityPeaks";
	public static final String DEF_POSITION_Y_AXIS_RELATIVE_INTENSITY_PEAKS = Position.Secondary.toString();
	public static final String P_COLOR_Y_AXIS_RELATIVE_INTENSITY_PEAKS = "colorYAxisRelativeIntensityPeaks";
	public static final String DEF_COLOR_Y_AXIS_RELATIVE_INTENSITY_PEAKS = "0,0,0";
	public static final String P_GRIDLINE_STYLE_Y_AXIS_RELATIVE_INTENSITY_PEAKS = "gridlineStyleYAxisRelativeIntensityPeaks";
	public static final String DEF_GRIDLINE_STYLE_Y_AXIS_RELATIVE_INTENSITY_PEAKS = LineStyle.DOT.toString();
	public static final String P_GRIDLINE_COLOR_Y_AXIS_RELATIVE_INTENSITY_PEAKS = "gridlineColorYAxisRelativeIntensityPeaks";
	public static final String DEF_GRIDLINE_COLOR_Y_AXIS_RELATIVE_INTENSITY_PEAKS = "192,192,192";
	/*
	 * Targets
	 */
	public static final String P_USE_TARGET_LIST = "useTargetList";
	public static final boolean DEF_USE_TARGET_LIST = true;
	public static final String P_TARGET_LIST = "targetList";
	public static final String DEF_TARGET_LIST = "";
	public static final String P_PROPAGATE_TARGET_ON_UPDATE = "propagateTargetOnUpdate";
	public static final boolean DEF_PROPAGATE_TARGET_ON_UPDATE = false;
	public static final String P_TARGETS_TABLE_SORTABLE = "targetsTableSortable";
	public static final boolean DEF_TARGETS_TABLE_SORTABLE = false;
	public static final String P_TARGETS_TABLE_SHOW_DEVIATION_RT = "targetsTableShowDeviationRetentionTime";
	public static final boolean DEF_TARGETS_TABLE_SHOW_DEVIATION_RT = false;
	public static final String P_TARGETS_TABLE_SHOW_DEVIATION_RI = "targetsTableShowDeviationRetentionIndex";
	public static final boolean DEF_TARGETS_TABLE_SHOW_DEVIATION_RI = false;
	public static final String P_TARGET_TEMPLATE_LIBRARY_IMPORT_FOLDER = "targetTemplateLibraryImportFolder";
	public static final String DEF_TARGET_TEMPLATE_LIBRARY_IMPORT_FOLDER = "";
	public static final String P_USE_ABSOLUTE_DEVIATION_RETENTION_TIME = "useAbsoluteDeviationRetentionTime";
	public static final boolean DEF_USE_ABSOLUTE_DEVIATION_RETENTION_TIME = false;
	public static final String P_RETENTION_TIME_DEVIATION_REL_OK = "retentionTimeDeviationRelativeOK";
	public static final float DEF_RETENTION_TIME_DEVIATION_REL_OK = 20.0f;
	public static final String P_RETENTION_TIME_DEVIATION_REL_WARN = "retentionTimeDeviationRelativeWARN";
	public static final float DEF_RETENTION_TIME_DEVIATION_REL_WARN = 40.0f;
	public static final String P_RETENTION_TIME_DEVIATION_ABS_OK = "retentionTimeDeviationAbsoluteOK";
	public static final int DEF_RETENTION_TIME_DEVIATION_ABS_OK = 1000;
	public static final String P_RETENTION_TIME_DEVIATION_ABS_WARN = "retentionTimeDeviationAbsoluteWARN";
	public static final int DEF_RETENTION_TIME_DEVIATION_ABS_WARN = 2000;
	public static final String P_USE_ABSOLUTE_DEVIATION_RETENTION_INDEX = "useAbsoluteDeviationRetentionIndex";
	public static final boolean DEF_USE_ABSOLUTE_DEVIATION_RETENTION_INDEX = false;
	public static final String P_RETENTION_INDEX_DEVIATION_REL_OK = "retentionIndexDeviationRelativeOK";
	public static final float DEF_RETENTION_INDEX_DEVIATION_REL_OK = 20.0f;
	public static final String P_RETENTION_INDEX_DEVIATION_REL_WARN = "retentionIndexDeviationRelativeWARN";
	public static final float DEF_RETENTION_INDEX_DEVIATION_REL_WARN = 40.0f;
	public static final String P_RETENTION_INDEX_DEVIATION_ABS_OK = "retentionIndexDeviationAbsoluteOK";
	public static final float DEF_RETENTION_INDEX_DEVIATION_ABS_OK = 20.0f;
	public static final String P_RETENTION_INDEX_DEVIATION_ABS_WARN = "retentionIndexDeviationAbsoluteWARN";
	public static final float DEF_RETENTION_INDEX_DEVIATION_ABS_WARN = 40.0f;
	public static final String P_ACTIVATE_TARGET_DND_WINDOWS = "activateTargetDragAndDropWindows";
	public static final boolean DEF_ACTIVATE_TARGET_DND_WINDOWS = false;
	//
	public static final String P_ADD_UNKNOWN_AFTER_DELETE_TARGETS_ALL = "addUnknownAfterDeleteTargetsAll";
	public static final boolean DEF_ADD_UNKNOWN_AFTER_DELETE_TARGETS_ALL = false;
	public static final String P_MATCH_QUALITY_UNKNOWN_TARGET = "matchQualityUnknownTarget";
	public static final float DEF_MATCH_QUALITY_UNKNOWN_TARGET = 80.0f;
	public static final String P_UNKNOWN_TARGET_ADD_RETENTION_INDEX = "unknownTargetAddRetentionIndex";
	public static final boolean DEF_UNKNOWN_TARGET_ADD_RETENTION_INDEX = true;
	public static final String P_VERIFY_UNKNOWN_TARGET = "verifyUnknownTarget";
	public static final boolean DEF_VERIFY_UNKNOWN_TARGET = false;
	/*
	 * Edit History
	 */
	public static final String P_EDIT_HISTORY_HIDE_PROCESS_METHOD_ENTRIES = "editHistoryHideProcessMethodEntries";
	public static final boolean DEF_EDIT_HISTORY_HIDE_PROCESS_METHOD_ENTRIES = true;
	/*
	 * Time Ranges
	 */
	public static final String P_TIME_RANGE_TEMPLATE_FOLDER = "timeRangeTemplateFolder";
	public static final String DEF_TIME_RANGE_TEMPLATE_FOLDER = "";
	public static final String P_TIME_RANGE_SELECTION_OFFSET = "timeRangeSelectionOffset";
	public static final int DEF_TIME_RANGE_SELECTION_OFFSET = 10000; // ms
	/*
	 * Named Traces
	 */
	public static final String P_NAMED_TRACES_TEMPLATE_FOLDER = "namedTracesTemplateFolder";
	public static final String DEF_NAMED_TRACES_TEMPLATE_FOLDER = "";
	/*
	 * Target Templates
	 */
	public static final String P_TARGET_TEMPLATES_FOLDER = "targetTemplatesFolder";
	public static final String DEF_TARGET_TEMPLATES_FOLDER = "";
	/*
	 * Instruments
	 */
	public static final String P_INSTRUMENTS_TEMPLATE_FOLDER = "instrumentsTemplateFolder";
	public static final String DEF_INSTRUMENTS_TEMPLATE_FOLDER = "";
	/*
	 * Chromatogram
	 */
	public static final String P_CHROMATOGRAM_CHART_COMPRESSION_TYPE = "chromatogramChartCompressionType";
	public static final String DEF_CHROMATOGRAM_CHART_COMPRESSION_TYPE = LineChart.COMPRESSION_MEDIUM;
	public static final String P_COLOR_CHROMATOGRAM = "colorChromatogram";
	public static final String DEF_COLOR_CHROMATOGRAM = "255,0,0";
	public static final String P_COLOR_CHROMATOGRAM_INACTIVE = "colorChromatogramInactive";
	public static final String DEF_COLOR_CHROMATOGRAM_INACTIVE = "125,125,125";
	public static final String P_ENABLE_CHROMATOGRAM_AREA = "enableChromatogramArea";
	public static final boolean DEF_ENABLE_CHROMATOGRAM_AREA = true;
	public static final String P_COLOR_CHROMATOGRAM_SELECTED_PEAK = "colorChromatogramSelectedPeak";
	public static final String DEF_COLOR_CHROMATOGRAM_SELECTED_PEAK = "128,0,0";
	public static final String P_CHROMATOGRAM_SELECTED_PEAK_SCAN_MARKER_SIZE = "showChromatogramSelectedPeakScanMarkerSize";
	public static final int DEF_CHROMATOGRAM_SELECTED_PEAK_SCAN_MARKER_SIZE = 2;
	public static final String P_CHROMATOGRAM_SELECTED_PEAK_SCAN_MARKER_TYPE = "showChromatogramSelectedPeakScanMarkerType";
	public static final String DEF_CHROMATOGRAM_SELECTED_PEAK_SCAN_MARKER_TYPE = PlotSymbolType.CIRCLE.toString();
	public static final String P_COLOR_CHROMATOGRAM_SELECTED_SCAN = "colorChromatogramSelectedScan";
	public static final String DEF_COLOR_CHROMATOGRAM_SELECTED_SCAN = "128,0,0";
	public static final String P_CHROMATOGRAM_SELECTED_SCAN_MARKER_SIZE = "showChromatogramSelectedScanMarkerSize";
	public static final int DEF_CHROMATOGRAM_SELECTED_SCAN_MARKER_SIZE = 5;
	public static final String P_CHROMATOGRAM_SELECTED_SCAN_MARKER_TYPE = "showChromatogramSelectedScanMarkerType";
	public static final String DEF_CHROMATOGRAM_SELECTED_SCAN_MARKER_TYPE = PlotSymbolType.CROSS.toString();
	public static final String P_CHROMATOGRAM_PEAK_LABEL_FONT_NAME = "chromatogramPeakLabelFontName";
	public static final String DEF_CHROMATOGRAM_PEAK_LABEL_FONT_NAME = Resources.DEFAULT_FONT_NAME;
	public static final String P_CHROMATOGRAM_PEAK_LABEL_FONT_SIZE = "chromatogramPeakLabelFontSize";
	public static final String P_CHROMATOGRAM_PEAK_LABEL_FONT_STYLE = "chromatogramPeakLabelFontStyle";
	public static final int DEF_CHROMATOGRAM_PEAK_LABEL_FONT_STYLE = SWT.NORMAL;
	public static final String P_SHOW_CHROMATOGRAM_BASELINE = "showChromatogramBaseline";
	public static final boolean DEF_SHOW_CHROMATOGRAM_BASELINE = true;
	public static final String P_COLOR_CHROMATOGRAM_BASELINE = "colorChromatogramBaseline";
	public static final String DEF_COLOR_CHROMATOGRAM_BASELINE = "0,0,0";
	public static final String P_ENABLE_BASELINE_AREA = "enableBaselineArea";
	public static final boolean DEF_ENABLE_BASELINE_AREA = true;
	public static final String P_CHROMATOGRAM_PEAK_LABEL_SYMBOL_SIZE = "chromatogramPeakLabelSymbolSize";
	public static final String P_CHROMATOGRAM_SELECTED_PEAK_MARKER_TYPE = "chromatogramSelectedPeakMarkerType";
	public static final String DEF_CHROMATOGRAM_SELECTED_PEAK_MARKER_TYPE = PlotSymbolType.INVERTED_TRIANGLE.toString();
	public static final String P_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL_MARKER_TYPE = "chromatogramPeaksActiveNormalMarkerType";
	public static final String DEF_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL_MARKER_TYPE = PlotSymbolType.INVERTED_TRIANGLE.toString();
	public static final String P_CHROMATOGRAM_PEAKS_INACTIVE_NORMAL_MARKER_TYPE = "chromatogramPeaksInactiveNormalMarkerType";
	public static final String DEF_CHROMATOGRAM_PEAKS_INACTIVE_NORMAL_MARKER_TYPE = PlotSymbolType.INVERTED_TRIANGLE.toString();
	public static final String P_CHROMATOGRAM_PEAKS_ACTIVE_ISTD_MARKER_TYPE = "chromatogramPeaksActiveIstdMarkerType";
	public static final String DEF_CHROMATOGRAM_PEAKS_ACTIVE_ISTD_MARKER_TYPE = PlotSymbolType.DIAMOND.toString();
	public static final String P_CHROMATOGRAM_PEAKS_INACTIVE_ISTD_MARKER_TYPE = "chromatogramPeaksInactiveIstdMarkerType";
	public static final String DEF_CHROMATOGRAM_PEAKS_INACTIVE_ISTD_MARKER_TYPE = PlotSymbolType.DIAMOND.toString();
	//
	public static final String P_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL = "colorChromatogramPeaksActiveNormal";
	public static final String DEF_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL = "128, 128, 128";
	public static final String P_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL_TARGETS_HIDDEN = "colorChromatogramPeaksActiveNormalTargetsHidden";
	public static final String DEF_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL_TARGETS_HIDDEN = "128, 0, 0";
	public static final String P_COLOR_CHROMATOGRAM_PEAKS_INACTIVE_NORMAL = "colorChromatogramPeaksInactiveNormal";
	public static final String DEF_COLOR_CHROMATOGRAM_PEAKS_INACTIVE_NORMAL = "192, 192, 192";
	public static final String P_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_ISTD = "colorChromatogramPeaksActiveIstd";
	public static final String DEF_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_ISTD = "255, 0, 0";
	public static final String P_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_ISTD_TARGETS_HIDDEN = "colorChromatogramPeaksActiveIstdTargetsHidden";
	public static final String DEF_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_ISTD_TARGETS_HIDDEN = "128,0,0";
	public static final String P_COLOR_CHROMATOGRAM_PEAKS_INACTIVE_ISTD = "colorChromatogramPeaksInactiveIstd";
	public static final String DEF_COLOR_CHROMATOGRAM_PEAKS_INACTIVE_ISTD = "192, 192, 192";
	//
	public static final String P_CHROMATOGRAM_SCAN_LABEL_SYMBOL_SIZE = "chromatogramScanLabelSymbolSize";
	public static final String P_CHROMATOGRAM_SCAN_LABEL_FONT_NAME = "chromatogramScanLabelFontName";
	public static final String DEF_CHROMATOGRAM_SCAN_LABEL_FONT_NAME = Resources.DEFAULT_FONT_NAME;
	public static final String P_CHROMATOGRAM_SCAN_LABEL_FONT_SIZE = "chromatogramScanLabelFontSize";
	public static final String P_CHROMATOGRAM_SCAN_LABEL_FONT_STYLE = "chromatogramScanLabelFontStyle";
	public static final int DEF_CHROMATOGRAM_SCAN_LABEL_FONT_STYLE = SWT.NORMAL;
	//
	public static final String P_CHROMATOGRAM_ACTIVE_TARGET_LABEL_FONT_COLOR = "chromatogramActiveTargetLabelFontColor";
	public static final String DEF_CHROMATOGRAM_ACTIVE_TARGET_LABEL_FONT_COLOR = "0, 0, 0";
	public static final String P_CHROMATOGRAM_INACTIVE_TARGET_LABEL_FONT_COLOR = "chromatogramActiveTargetLabelFontColor";
	public static final String DEF_CHROMATOGRAM_INACTIVE_TARGET_LABEL_FONT_COLOR = "109, 109, 109";
	public static final String P_CHROMATOGRAM_ID_TARGET_LABEL_FONT_COLOR = "chromatogramActiveTargetLabelFontColor";
	public static final String DEF_CHROMATOGRAM_ID_TARGET_LABEL_FONT_COLOR = "0, 0, 0";
	//
	public static final String P_CHROMATOGRAM_ACTIVE_TARGET_LABEL_FONT_DARK_COLOR = "chromatogramActiveTargetLabelFontDarkColor";
	public static final String DEF_CHROMATOGRAM_ACTIVE_TARGET_LABEL_FONT_DARK_COLOR = "255, 255, 255";
	public static final String P_CHROMATOGRAM_INACTIVE_TARGET_LABEL_FONT_DARK_COLOR = "chromatogramActiveTargetLabelFontDarkColor";
	public static final String DEF_CHROMATOGRAM_INACTIVE_TARGET_LABEL_FONT_DARK_COLOR = "109, 109, 109";
	public static final String P_CHROMATOGRAM_ID_TARGET_LABEL_FONT_DARK_COLOR = "chromatogramActiveTargetLabelFontDarkColor";
	public static final String DEF_CHROMATOGRAM_ID_TARGET_LABEL_FONT_DARK_COLOR = "255, 255, 255";
	//
	public static final String P_COLOR_CHROMATOGRAM_IDENTIFIED_SCAN = "colorChromatogramIdentifiedScan";
	public static final String DEF_COLOR_CHROMATOGRAM_IDENTIFIED_SCAN = "128,0,0";
	public static final String P_CHROMATOGRAM_SCAN_MARKER_TYPE = "chromatogramScanMarkerType";
	public static final String DEF_CHROMATOGRAM_SCAN_MARKER_TYPE = PlotSymbolType.CIRCLE.toString();
	public static final String P_CHROMATOGRAM_IDENTIFIED_SCAN_MARKER_TYPE = "chromatogramIdentifiedScanMarkerType";
	public static final String DEF_CHROMATOGRAM_IDENTIFIED_SCAN_MARKER_TYPE = PlotSymbolType.CIRCLE.toString();
	public static final String P_MOVE_RETENTION_TIME_ON_PEAK_SELECTION = "moveRetentionTimeOnPeakSelection";
	public static final boolean DEF_MOVE_RETENTION_TIME_ON_PEAK_SELECTION = true;
	public static final String P_ALTERNATE_WINDOW_MOVE_DIRECTION = "useAlternateWindowMoveDirection";
	public static final boolean DEF_ALTERNATE_WINDOW_MOVE_DIRECTION = false;
	public static final String P_CONDENSE_CYCLE_NUMBER_SCANS = "condenseCycleNumberScans";
	public static final boolean DEF_CONDENSE_CYCLE_NUMBER_SCANS = true;
	public static final String P_SET_CHROMATOGRAM_INTENSITY_RANGE = "setChromatogramIntensityRange";
	public static final boolean DEF_SET_CHROMATOGRAM_INTENSITY_RANGE = false;
	public static final String P_CHROMATOGRAM_TRANSFER_DELTA_RETENTION_TIME = "chromatogramTransferDeltaRetentionTime";
	public static final double MIN_CHROMATOGRAM_TRANSFER_DELTA_RETENTION_TIME = 0; // Minutes
	public static final double MAX_CHROMATOGRAM_TRANSFER_DELTA_RETENTION_TIME = Double.MAX_VALUE; // Minutes
	public static final double DEF_CHROMATOGRAM_TRANSFER_DELTA_RETENTION_TIME = 0.5d; // Minutes
	public static final String P_CHROMATOGRAM_TRANSFER_BEST_TARGET_ONLY = "chromatogramTransferBestTargetOnly";
	public static final boolean DEF_CHROMATOGRAM_TRANSFER_BEST_TARGET_ONLY = true;
	public static final String P_STRETCH_CHROMATOGRAM_MILLISECONDS_SCAN_DELAY = "stretchChromatogramMillisecondsScanDelay";
	public static final int MIN_STRETCH_CHROMATOGRAM_MILLISECONDS_SCAN_DELAY = 0;
	public static final int MAX_STRETCH_CHROMATOGRAM_MILLISECONDS_SCAN_DELAY = 6000000; // = 100.0 minutes
	public static final int DEF_STRETCH_CHROMATOGRAM_MILLISECONDS_SCAN_DELAY = 0;
	public static final String P_STRETCH_CHROMATOGRAM_MILLISECONDS_LENGTH = "stretchChromatogramMillisecondsLength";
	public static final int MIN_STRETCH_CHROMATOGRAM_MILLISECONDS_LENGTH = 10;
	public static final int MAX_STRETCH_CHROMATOGRAM_MILLISECONDS_LENGTH = 36000000; // = 600.0 minutes
	public static final int DEF_STRETCH_CHROMATOGRAM_MILLISECONDS_LENGTH = 6000000; // = 100.0 minutes
	public static final String P_CHROMATOGRAM_EXTEND_Y = "chromatogramExtendY";
	public static final double MIN_CHROMATOGRAM_EXTEND_Y = 0.0d;
	public static final double MAX_CHROMATOGRAM_EXTEND_Y = 10.0d;
	public static final double DEF_CHROMATOGRAM_EXTEND_Y = 0.5d; // 50%
	public static final String P_CHROMATOGRAM_RESTRICT_SELECT_X = "chromatogramRestrictSelectX";
	public static final boolean DEF_CHROMATOGRAM_RESTRICT_SELECT_X = false;
	public static final String P_CHROMATOGRAM_RESTRICT_SELECT_Y = "chromatogramRestrictSelectY";
	public static final boolean DEF_CHROMATOGRAM_RESTRICT_SELECT_Y = false;
	public static final String P_CHROMATOGRAM_FORCE_ZERO_MIN_Y_MSD = "chromatogramForceZeroMinYMSD";
	public static final boolean DEF_CHROMATOGRAM_FORCE_ZERO_MIN_Y_MSD = false;
	public static final String P_CHROMATOGRAM_REFERENCE_ZOOM_ZERO_X = "chromatogramReferenceZoomZeroX";
	public static final boolean DEF_CHROMATOGRAM_REFERENCE_ZOOM_ZERO_X = false;
	public static final String P_CHROMATOGRAM_REFERENCE_ZOOM_ZERO_Y = "chromatogramReferenceZoomZeroY";
	public static final boolean DEF_CHROMATOGRAM_REFERENCE_ZOOM_ZERO_Y = true;
	public static final String P_CHROMATOGRAM_RESTRICT_ZOOM_X = "chromatogramRestrictZoomX";
	public static final boolean DEF_CHROMATOGRAM_RESTRICT_ZOOM_X = false;
	public static final String P_CHROMATOGRAM_RESTRICT_ZOOM_Y = "chromatogramRestrictZoomY";
	public static final boolean DEF_CHROMATOGRAM_RESTRICT_ZOOM_Y = true;
	public static final String P_CHROMATOGRAM_EDITOR_LABEL = "chromatogramEditorLabel";
	public static final String DEF_CHROMATOGRAM_EDITOR_LABEL = HeaderField.NAME.name();
	public static final String P_CHROMATOGRAM_REFERENCE_LABEL = "chromatogramReferenceLabel";
	public static final String DEF_CHROMATOGRAM_REFERENCE_LABEL = HeaderField.DEFAULT.name();
	public static final String P_CHROMATOGRAM_TRANSFER_NAME_TO_REFERENCES_HEADER_FIELD = "chromatogramTransferNameToReferencesHeaderField";
	public static final String DEF_CHROMATOGRAM_TRANSFER_NAME_TO_REFERENCES_HEADER_FIELD = HeaderField.DATA_NAME.name();
	public static final String P_CHROMATOGRAM_TRANSFER_COLUMN_TYPE_TO_REFERENCES = "chromatogramTransferColumnTypeToReferences";
	public static final boolean DEF_CHROMATOGRAM_TRANSFER_COLUMN_TYPE_TO_REFERENCES = true;
	public static final String P_CHROMATOGRAM_PROCESSOR_TOOLBAR = "chromatogramProcessorToolbar";
	public static final String DEF_CHROMATOGRAM_PROCESSOR_TOOLBAR = "";
	public static final String P_CHROMATOGRAM_SHOW_METHODS_TOOLBAR = "chromatogramShowMethodsToolbar";
	public static final boolean DEF_CHROMATOGRAM_SHOW_METHODS_TOOLBAR = false;
	public static final String P_CHROMATOGRAM_SHOW_REFERENCES_COMBO = "chromatogramShowReferencesCombo";
	public static final boolean DEF_CHROMATOGRAM_SHOW_REFERENCES_COMBO = false;
	//
	public static final String P_TITLE_X_AXIS_MILLISECONDS = "titleXAxisMilliseconds";
	public static final String DEF_TITLE_X_AXIS_MILLISECONDS = "Time [ms]";
	public static final String P_FORMAT_X_AXIS_MILLISECONDS = "formatXAxisMilliseconds";
	public static final String DEF_FORMAT_X_AXIS_MILLISECONDS = "0.###";
	public static final String P_SHOW_X_AXIS_MILLISECONDS = "showXAxisMilliseconds";
	public static final boolean DEF_SHOW_X_AXIS_MILLISECONDS = false;
	public static final String P_POSITION_X_AXIS_MILLISECONDS = "positionXAxisMilliseconds";
	public static final String DEF_POSITION_X_AXIS_MILLISECONDS = Position.Secondary.toString();
	public static final String P_COLOR_X_AXIS_MILLISECONDS = "colorXAxisMilliseconds";
	public static final String DEF_COLOR_X_AXIS_MILLISECONDS = "0,0,0";
	public static final String P_COLOR_X_AXIS_MILLISECONDS_DARKTHEME = "colorXAxisMillisecondsDarkTheme";
	public static final String DEF_COLOR_X_AXIS_MILLISECONDS_DARKTHEME = "252,252,247";
	public static final String P_FONT_NAME_X_AXIS_MILLISECONDS = "fontNameXAxisMilliseconds";
	public static final String DEF_FONT_NAME_X_AXIS_MILLISECONDS = Resources.DEFAULT_FONT_NAME;
	public static final String P_FONT_SIZE_X_AXIS_MILLISECONDS = "fontSizeXAxisMilliseconds";
	public static final String P_FONT_STYLE_X_AXIS_MILLISECONDS = "fontStyleXAxisMilliseconds";
	public static final int DEF_FONT_STYLE_X_AXIS_MILLISECONDS = SWT.BOLD;
	public static final String P_GRIDLINE_STYLE_X_AXIS_MILLISECONDS = "gridlineStyleXAxisMilliseconds";
	public static final String DEF_GRIDLINE_STYLE_X_AXIS_MILLISECONDS = LineStyle.NONE.toString();
	public static final String P_GRIDLINE_COLOR_X_AXIS_MILLISECONDS = "gridlineColorXAxisMilliseconds";
	public static final String DEF_GRIDLINE_COLOR_X_AXIS_MILLISECONDS = "192,192,192";
	public static final String P_SHOW_X_AXIS_TITLE_MILLISECONDS = "showXAxisTitleMilliseconds";
	public static final boolean DEF_SHOW_X_AXIS_TITLE_MILLISECONDS = true;
	//
	public static final String P_TITLE_X_AXIS_RETENTION_INDEX = "titleXAxisRetentionIndex";
	public static final String DEF_TITLE_X_AXIS_RETENTION_INDEX = "Retention Index";
	public static final String P_FORMAT_X_AXIS_RETENTION_INDEX = "formatXAxisRetentionIndex";
	public static final String DEF_FORMAT_X_AXIS_RETENTION_INDEX = "0.0";
	public static final String P_SHOW_X_AXIS_RETENTION_INDEX = "showXAxisRetentionIndex";
	public static final boolean DEF_SHOW_X_AXIS_RETENTION_INDEX = false;
	public static final String P_POSITION_X_AXIS_RETENTION_INDEX = "positionXAxisRetentionIndex";
	public static final String DEF_POSITION_X_AXIS_RETENTION_INDEX = Position.Primary.toString();
	public static final String P_COLOR_X_AXIS_RETENTION_INDEX = "colorXAxisRetentionIndex";
	public static final String DEF_COLOR_X_AXIS_RETENTION_INDEX = "0,0,0";
	public static final String P_COLOR_X_AXIS_RETENTION_INDEX_DARKTHEME = "colorXAxisRetentionIndexDarkTheme";
	public static final String DEF_COLOR_X_AXIS_RETENTION_INDEX_DARKTHEME = "252,252,247";
	public static final String P_FONT_NAME_X_AXIS_RETENTION_INDEX = "fontNameXAxisRetentionIndex";
	public static final String DEF_FONT_NAME_X_AXIS_RETENTION_INDEX = Resources.DEFAULT_FONT_NAME;
	public static final String P_FONT_SIZE_X_AXIS_RETENTION_INDEX = "fontSizeXAxisRetentionIndex";
	public static final String P_FONT_STYLE_X_AXIS_RETENTION_INDEX = "fontStyleXAxisRetentionIndex";
	public static final int DEF_FONT_STYLE_X_AXIS_RETENTION_INDEX = SWT.BOLD;
	public static final String P_GRIDLINE_STYLE_X_AXIS_RETENTION_INDEX = "gridlineStyleXAxisRetentionIndex";
	public static final String DEF_GRIDLINE_STYLE_X_AXIS_RETENTION_INDEX = LineStyle.DOT.toString();
	public static final String P_GRIDLINE_COLOR_X_AXIS_RETENTION_INDEX = "gridlineColorXAxisRetentionIndex";
	public static final String DEF_GRIDLINE_COLOR_X_AXIS_RETENTION_INDEX = "192,192,192";
	public static final String P_SHOW_X_AXIS_TITLE_RETENTION_INDEX = "showXAxisTitleRetentionIndex";
	public static final boolean DEF_SHOW_X_AXIS_TITLE_RETENTION_INDEX = true;
	//
	public static final String P_TITLE_X_AXIS_SECONDS = "titleXAxisSeconds";
	public static final String DEF_TITLE_X_AXIS_SECONDS = "Time [s]";
	public static final String P_FORMAT_X_AXIS_SECONDS = "formatXAxisSeconds";
	public static final String DEF_FORMAT_X_AXIS_SECONDS = "0.00#";
	public static final String P_SHOW_X_AXIS_SECONDS = "showXAxisSeconds";
	public static final boolean DEF_SHOW_X_AXIS_SECONDS = false;
	public static final String P_POSITION_X_AXIS_SECONDS = "positionXAxisSeconds";
	public static final String DEF_POSITION_X_AXIS_SECONDS = Position.Primary.toString();
	public static final String P_COLOR_X_AXIS_SECONDS = "colorXAxisSeconds";
	public static final String DEF_COLOR_X_AXIS_SECONDS = "0,0,0";
	public static final String P_COLOR_X_AXIS_SECONDS_DARKTHEME = "colorXAxisSecondsDarkTheme";
	public static final String DEF_COLOR_X_AXIS_SECONDS_DARKTHEME = "252,252,247";
	public static final String P_FONT_NAME_X_AXIS_SECONDS = "fontNameXAxisSeconds";
	public static final String DEF_FONT_NAME_X_AXIS_SECONDS = Resources.DEFAULT_FONT_NAME;
	public static final String P_FONT_SIZE_X_AXIS_SECONDS = "fontSizeXAxisSeconds";
	public static final String P_FONT_STYLE_X_AXIS_SECONDS = "fontStyleXAxisSeconds";
	public static final int DEF_FONT_STYLE_X_AXIS_SECONDS = SWT.BOLD;
	public static final String P_GRIDLINE_STYLE_X_AXIS_SECONDS = "gridlineStyleXAxisSeconds";
	public static final String DEF_GRIDLINE_STYLE_X_AXIS_SECONDS = LineStyle.NONE.toString();
	public static final String P_GRIDLINE_COLOR_X_AXIS_SECONDS = "gridlineColorXAxisSeconds";
	public static final String DEF_GRIDLINE_COLOR_X_AXIS_SECONDS = "192,192,192";
	public static final String P_SHOW_X_AXIS_TITLE_SECONDS = "showXAxisTitleSeconds";
	public static final boolean DEF_SHOW_X_AXIS_TITLE_SECONDS = true;
	//
	public static final String P_TITLE_X_AXIS_MINUTES = "titleXAxisMinutes";
	public static final String DEF_TITLE_X_AXIS_MINUTES = "Time [min]";
	public static final String P_FORMAT_X_AXIS_MINUTES = "formatXAxisMinutes";
	public static final String DEF_FORMAT_X_AXIS_MINUTES = "0.00#";
	public static final String P_SHOW_X_AXIS_MINUTES = "showXAxisMinutes";
	public static final boolean DEF_SHOW_X_AXIS_MINUTES = true;
	public static final String P_POSITION_X_AXIS_MINUTES = "positionXAxisMinutes";
	public static final String DEF_POSITION_X_AXIS_MINUTES = Position.Primary.toString();
	public static final String P_COLOR_X_AXIS_MINUTES = "colorXAxisMinutes";
	public static final String DEF_COLOR_X_AXIS_MINUTES = "0,0,0";
	public static final String P_COLOR_X_AXIS_MINUTES_DARKTHEME = "colorXAxisMinutesDarkTheme";
	public static final String DEF_COLOR_X_AXIS_MINUTES_DARKTHEME = "252,252,247";
	public static final String P_FONT_NAME_X_AXIS_MINUTES = "fontNameXAxisMinutes";
	public static final String DEF_FONT_NAME_X_AXIS_MINUTES = Resources.DEFAULT_FONT_NAME;
	public static final String P_FONT_SIZE_X_AXIS_MINUTES = "fontSizeXAxisMinutes";
	public static final String P_FONT_STYLE_X_AXIS_MINUTES = "fontStyleXAxisMinutes";
	public static final int DEF_FONT_STYLE_X_AXIS_MINUTES = SWT.BOLD;
	public static final String P_GRIDLINE_STYLE_X_AXIS_MINUTES = "gridlineStyleXAxisMinutes";
	public static final String DEF_GRIDLINE_STYLE_X_AXIS_MINUTES = LineStyle.DOT.toString();
	public static final String P_GRIDLINE_COLOR_X_AXIS_MINUTES = "gridlineColorXAxisMinutes";
	public static final String DEF_GRIDLINE_COLOR_X_AXIS_MINUTES = "192,192,192";
	public static final String P_SHOW_X_AXIS_TITLE_MINUTES = "showXAxisTitleMinutes";
	public static final boolean DEF_SHOW_X_AXIS_TITLE_MINUTES = true;
	public static final String P_SHOW_X_AXIS_LINE_MINUTES = "showXAxisLineMinutes";
	public static final boolean DEF_SHOW_X_AXIS_LINE_MINUTES = true;
	public static final String P_SHOW_X_AXIS_POSITION_MARKER_MINUTES = "showXAxisPositionMarkerMinutes";
	public static final boolean DEF_SHOW_X_AXIS_POSITION_MARKER_MINUTES = true;
	//
	public static final String P_TITLE_X_AXIS_SCANS = "titleXAxisScans";
	public static final String DEF_TITLE_X_AXIS_SCANS = "Scan";
	public static final String P_FORMAT_X_AXIS_SCANS = "formatXAxisScans";
	public static final String DEF_FORMAT_X_AXIS_SCANS = "0.###";
	public static final String P_SHOW_X_AXIS_SCANS = "showXAxisScans";
	public static final boolean DEF_SHOW_X_AXIS_SCANS = false;
	public static final String P_POSITION_X_AXIS_SCANS = "positionXAxisScans";
	public static final String DEF_POSITION_X_AXIS_SCANS = Position.Primary.toString();
	public static final String P_COLOR_X_AXIS_SCANS = "colorXAxisScans";
	public static final String DEF_COLOR_X_AXIS_SCANS = "0,0,0";
	public static final String P_COLOR_X_AXIS_SCANS_DARKTHEME = "colorXAxisScansDarkTheme";
	public static final String DEF_COLOR_X_AXIS_SCANS_DARKTHEME = "252,252,247";
	public static final String P_FONT_NAME_X_AXIS_SCANS = "fontNameXAxisScans";
	public static final String DEF_FONT_NAME_X_AXIS_SCANS = Resources.DEFAULT_FONT_NAME;
	public static final String P_FONT_SIZE_X_AXIS_SCANS = "fontSizeXAxisScans";
	public static final String P_FONT_STYLE_X_AXIS_SCANS = "fontStyleXAxisScans";
	public static final int DEF_FONT_STYLE_X_AXIS_SCANS = SWT.BOLD;
	public static final String P_GRIDLINE_STYLE_X_AXIS_SCANS = "gridlineStyleXAxisScans";
	public static final String DEF_GRIDLINE_STYLE_X_AXIS_SCANS = LineStyle.NONE.toString();
	public static final String P_GRIDLINE_COLOR_X_AXIS_SCANS = "gridlineColorXAxisScans";
	public static final String DEF_GRIDLINE_COLOR_X_AXIS_SCANS = "192,192,192";
	public static final String P_SHOW_X_AXIS_TITLE_SCANS = "showXAxisTitleScans";
	public static final boolean DEF_SHOW_X_AXIS_TITLE_SCANS = true;
	//
	public static final String P_TITLE_Y_AXIS_INTENSITY = "titleYAxisIntensity";
	public static final String DEF_TITLE_Y_AXIS_INTENSITY = "Intensity";
	public static final String P_FORMAT_Y_AXIS_INTENSITY = "formatYAxisIntensity";
	public static final String DEF_FORMAT_Y_AXIS_INTENSITY = "0.0#E0";
	public static final String P_SHOW_Y_AXIS_INTENSITY = "showYAxisIntensity";
	public static final boolean DEF_SHOW_Y_AXIS_INTENSITY = true;
	public static final String P_POSITION_Y_AXIS_INTENSITY = "positionYAxisIntensity";
	public static final String DEF_POSITION_Y_AXIS_INTENSITY = Position.Primary.toString();
	public static final String P_COLOR_Y_AXIS_INTENSITY = "colorYAxisIntensity";
	public static final String DEF_COLOR_Y_AXIS_INTENSITY = "0,0,0";
	public static final String P_COLOR_Y_AXIS_INTENSITY_DARKTHEME = "colorYAxisIntensityDarkTheme";
	public static final String DEF_COLOR_Y_AXIS_INTENSITY_DARKTHEME = "252,252,247";
	public static final String P_FONT_NAME_Y_AXIS_INTENSITY = "fontNameYAxisIntensity";
	public static final String DEF_FONT_NAME_Y_AXIS_INTENSITY = Resources.DEFAULT_FONT_NAME;
	public static final String P_FONT_SIZE_Y_AXIS_INTENSITY = "fontSizeYAxisIntensity";
	public static final String P_FONT_STYLE_Y_AXIS_INTENSITY = "fontStyleYAxisIntensity";
	public static final int DEF_FONT_STYLE_Y_AXIS_INTENSITY = SWT.BOLD;
	public static final String P_GRIDLINE_STYLE_Y_AXIS_INTENSITY = "gridlineStyleYAxisIntensity";
	public static final String DEF_GRIDLINE_STYLE_Y_AXIS_INTENSITY = LineStyle.NONE.toString();
	public static final String P_GRIDLINE_COLOR_Y_AXIS_INTENSITY = "gridlineColorYAxisIntensity";
	public static final String DEF_GRIDLINE_COLOR_Y_AXIS_INTENSITY = "192,192,192";
	public static final String P_SHOW_Y_AXIS_TITLE_INTENSITY = "showYAxisTitleIntensity";
	public static final boolean DEF_SHOW_Y_AXIS_TITLE_INTENSITY = true;
	//
	public static final String P_TITLE_Y_AXIS_RELATIVE_INTENSITY = "titleYAxisRelativeIntensity";
	public static final String DEF_TITLE_Y_AXIS_RELATIVE_INTENSITY = "Intensity [%]";
	public static final String P_FORMAT_Y_AXIS_RELATIVE_INTENSITY = "formatYAxisRelativeIntensity";
	public static final String DEF_FORMAT_Y_AXIS_RELATIVE_INTENSITY = "0.00#";
	public static final String P_SHOW_Y_AXIS_RELATIVE_INTENSITY = "showYAxisRelativeIntensity";
	public static final boolean DEF_SHOW_Y_AXIS_RELATIVE_INTENSITY = true;
	public static final String P_POSITION_Y_AXIS_RELATIVE_INTENSITY = "positionYAxisRelativeIntensity";
	public static final String DEF_POSITION_Y_AXIS_RELATIVE_INTENSITY = Position.Secondary.toString();
	public static final String P_COLOR_Y_AXIS_RELATIVE_INTENSITY = "colorYAxisRelativeIntensity";
	public static final String DEF_COLOR_Y_AXIS_RELATIVE_INTENSITY = "0,0,0";
	public static final String P_COLOR_Y_AXIS_RELATIVE_INTENSITY_DARKTHEME = "colorYAxisRelativeIntensityDarkTheme";
	public static final String DEF_COLOR_Y_AXIS_RELATIVE_INTENSITY_DARKTHEME = "252,252,247";
	public static final String P_FONT_NAME_Y_AXIS_RELATIVE_INTENSITY = "fontNameYAxisRelativeIntensity";
	public static final String DEF_FONT_NAME_Y_AXIS_RELATIVE_INTENSITY = Resources.DEFAULT_FONT_NAME;
	public static final String P_FONT_SIZE_Y_AXIS_RELATIVE_INTENSITY = "fontSizeYAxisRelativeIntensity";
	public static final String P_FONT_STYLE_Y_AXIS_RELATIVE_INTENSITY = "fontStyleYAxisRelativeIntensity";
	public static final int DEF_FONT_STYLE_Y_AXIS_RELATIVE_INTENSITY = SWT.BOLD;
	public static final String P_GRIDLINE_STYLE_Y_AXIS_RELATIVE_INTENSITY = "gridlineStyleYAxisRelativeIntensity";
	public static final String DEF_GRIDLINE_STYLE_Y_AXIS_RELATIVE_INTENSITY = LineStyle.DOT.toString();
	public static final String P_GRIDLINE_COLOR_Y_AXIS_RELATIVE_INTENSITY = "gridlineColorYAxisRelativeIntensity";
	public static final String DEF_GRIDLINE_COLOR_Y_AXIS_RELATIVE_INTENSITY = "192,192,192";
	public static final String P_SHOW_Y_AXIS_TITLE_RELATIVE_INTENSITY = "showYAxisTitleRelativeIntensity";
	public static final boolean DEF_SHOW_Y_AXIS_TITLE_RELATIVE_INTENSITY = true;
	//
	public static final String P_CHROMATOGRAM_SELECTED_ACTION_ID = "chromatogramSelectedActionId";
	public static final String DEF_CHROMATOGRAM_SELECTED_ACTION_ID = "";
	public static final String P_CHROMATOGRAM_SAVE_AS_FOLDER = "chromatogramSaveAsFolder";
	public static final String DEF_CHROMATOGRAM_SAVE_AS_FOLDER = "";
	public static final String P_CHROMATOGRAM_LOAD_PROCESS_METHOD = "chromatogramLoadProcessMethod";
	public static final String DEF_CHROMATOGRAM_LOAD_PROCESS_METHOD = "";
	public static final int MIN_DELTA_MILLISECONDS_PEAK_SELECTION = 0;
	public static final int MAX_DELTA_MILLISECONDS_PEAK_SELECTION = 120000; // = 2.0 minutes
	public static final String P_DELTA_MILLISECONDS_PEAK_SELECTION = "deltaMillisecondsPeakSelection";
	public static final int DEF_DELTA_MILLISECONDS_PEAK_SELECTION = 2000; // 2 seconds
	public static final String P_CHROMATOGRAM_MARK_ANALYSIS_SEGMENTS = "chromatogramMarkAnalysisSegments";
	public static final boolean DEF_CHROMATOGRAM_MARK_ANALYSIS_SEGMENTS = false;
	public static final String P_SHOW_RESUME_METHOD_DIALOG = "showResumeMethodDialog";
	public static final boolean DEF_SHOW_RESUME_METHOD_DIALOG = true;
	public static final String P_SHOW_RETENTION_INDEX_MARKER = "showRetenetionIndexMarker";
	public static final boolean DEF_SHOW_RETENTION_INDEX_MARKER = false;
	/*
	 * Calibration Chart
	 */
	public static final String P_COLOR_SCHEME_DISPLAY_CALIBRATION = "colorSchemeDisplayCalibration";
	public static final String DEF_COLOR_SCHEME_DISPLAY_CALIBRATION = Colors.COLOR_SCHEME_PRINT;
	//
	public static final String P_SHOW_X_AXIS_CONCENTRATION_CALIBRATION = "showXAxisConcentrationCalibration";
	public static final boolean DEF_SHOW_X_AXIS_CONCENTRATION_CALIBRATION = true;
	public static final String P_POSITION_X_AXIS_CONCENTRATION_CALIBRATION = "positionXAxisConcentrationCalibration";
	public static final String DEF_POSITION_X_AXIS_CONCENTRATION_CALIBRATION = Position.Primary.toString();
	public static final String P_COLOR_X_AXIS_CONCENTRATION_CALIBRATION = "colorXAxisConcentrationCalibration";
	public static final String DEF_COLOR_X_AXIS_CONCENTRATION_CALIBRATION = "0,0,0";
	public static final String P_GRIDLINE_STYLE_X_AXIS_CONCENTRATION_CALIBRATION = "gridlineStyleXAxisConcentrationCalibration";
	public static final String DEF_GRIDLINE_STYLE_X_AXIS_CONCENTRATION_CALIBRATION = LineStyle.DOT.toString();
	public static final String P_GRIDLINE_COLOR_X_AXIS_CONCENTRATION_CALIBRATION = "gridlineColorXAxisConcentrationCalibration";
	public static final String DEF_GRIDLINE_COLOR_X_AXIS_CONCENTRATION_CALIBRATION = "192,192,192";
	//
	public static final String P_SHOW_Y_AXIS_RESPONSE_CALIBRATION = "showYAxisResponseCalibration";
	public static final boolean DEF_SHOW_Y_AXIS_RESPONSE_CALIBRATION = true;
	public static final String P_POSITION_Y_AXIS_RESPONSE_CALIBRATION = "positionYAxisResponseCalibration";
	public static final String DEF_POSITION_Y_AXIS_RESPONSE_CALIBRATION = Position.Primary.toString();
	public static final String P_COLOR_Y_AXIS_RESPONSE_CALIBRATION = "colorYAxisResponseCalibration";
	public static final String DEF_COLOR_Y_AXIS_RESPONSE_CALIBRATION = "0,0,0";
	public static final String P_GRIDLINE_STYLE_Y_AXIS_RESPONSE_CALIBRATION = "gridlineStyleYAxisResponseCalibration";
	public static final String DEF_GRIDLINE_STYLE_Y_AXIS_RESPONSE_CALIBRATION = LineStyle.NONE.toString();
	public static final String P_GRIDLINE_COLOR_Y_AXIS_RESPONSE_CALIBRATION = "gridlineColorYAxisResponseCalibration";
	public static final String DEF_GRIDLINE_COLOR_Y_AXIS_RESPONSE_CALIBRATION = "192,192,192";
	//
	public static final String P_SHOW_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION = "showYAxisRelativeResponseCalibration";
	public static final boolean DEF_SHOW_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION = true;
	public static final String P_POSITION_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION = "positionYAxisRelativeResponseCalibration";
	public static final String DEF_POSITION_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION = Position.Secondary.toString();
	public static final String P_COLOR_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION = "colorYAxisRelativeResponseCalibration";
	public static final String DEF_COLOR_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION = "0,0,0";
	public static final String P_GRIDLINE_STYLE_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION = "gridlineStyleYAxisRelativeResponseCalibration";
	public static final String DEF_GRIDLINE_STYLE_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION = LineStyle.DOT.toString();
	public static final String P_GRIDLINE_COLOR_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION = "gridlineColorYAxisRelativeResponseCalibration";
	public static final String DEF_GRIDLINE_COLOR_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION = "192,192,192";
	/*
	 * File Explorer
	 */
	public static final String P_SHOW_DATA_MSD = "showDataMSD";
	public static final boolean DEF_SHOW_DATA_MSD = true;
	public static final String P_SHOW_DATA_CSD = "showDataCSD";
	public static final boolean DEF_SHOW_DATA_CSD = true;
	public static final String P_SHOW_DATA_WSD = "showDataWSD";
	public static final boolean DEF_SHOW_DATA_WSD = true;
	public static final String P_SHOW_DATA_VSD = "showDataVSD";
	public static final boolean DEF_SHOW_DATA_VSD = true;
	public static final String P_SHOW_DATA_TSD = "showDataTSD";
	public static final boolean DEF_SHOW_DATA_TSD = true;
	public static final String P_SHOW_LIBRARY_MSD = "showLibraryMSD";
	public static final boolean DEF_SHOW_LIBRARY_MSD = true;
	public static final String P_SHOW_SCANS_MSD = "showScansMSD";
	public static final boolean DEF_SHOW_SCANS_MSD = true;
	public static final String P_SHOW_DATA_SCAN_VSD = "showDataScanVSD";
	public static final boolean DEF_SHOW_DATA_SCAN_VSD = true;
	public static final String P_SHOW_DATA_SCAN_WSD = "showDataScanWSD";
	public static final boolean DEF_SHOW_DATA_SCAN_WSD = true;
	public static final String P_SHOW_DATA_NMR = "showDataNMR";
	public static final boolean DEF_SHOW_DATA_NMR = true;
	public static final String P_SHOW_DATA_CAL = "showDataCAL";
	public static final boolean DEF_SHOW_DATA_CAL = true;
	public static final String P_SHOW_DATA_PCR = "showDataPCR";
	public static final boolean DEF_SHOW_DATA_PCR = true;
	public static final String P_SHOW_DATA_SEQUENCE = "showDataSequence";
	public static final boolean DEF_SHOW_DATA_SEQUENCE = true;
	public static final String P_SHOW_DATA_METHOD = "showDataMethod";
	public static final boolean DEF_SHOW_DATA_METHOD = true;
	public static final String P_SHOW_DATA_QUANT_DB = "showDataQuantDB";
	public static final boolean DEF_SHOW_DATA_QUANT_DB = true;
	/*
	 * Lists
	 */
	public static final String P_SHOW_PEAKS_IN_LIST = "showPeaksInList";
	public static final boolean DEF_SHOW_PEAKS_IN_LIST = true;
	public static final String P_SHOW_PEAKS_IN_SELECTED_RANGE = "showPeaksInSelectedRange";
	public static final boolean DEF_SHOW_PEAKS_IN_SELECTED_RANGE = true;
	public static final String P_SHOW_SCANS_IN_LIST = "showScansInList";
	public static final boolean DEF_SHOW_SCANS_IN_LIST = true;
	public static final String P_SHOW_SCANS_IN_SELECTED_RANGE = "showScansInSelectedRange";
	public static final boolean DEF_SHOW_SCANS_IN_SELECTED_RANGE = true;
	public static final String P_SHOW_PEAK_PROFILES_SELECTION_ALL = "showPeakProfilesSelectionAll";
	public static final boolean DEF_SHOW_PEAK_PROFILES_SELECTION_ALL = true;
	/*
	 * Baseline
	 */
	public static final String P_BASELINE_CHART_COMPRESSION_TYPE = "baselineChartCompressionType";
	public static final String DEF_BASELINE_CHART_COMPRESSION_TYPE = LineChart.COMPRESSION_MEDIUM;
	public static final String P_COLOR_SCHEME_DISPLAY_BASELINE = "colorSchemeDisplayBaseline";
	public static final String DEF_COLOR_SCHEME_DISPLAY_BASELINE = Colors.COLOR_SCHEME_RED;
	/*
	 * Sequences
	 */
	public static final String P_SEQUENCE_EXPLORER_USE_SUBFOLDER = "sequenceExplorerUseSubfolder";
	public static final boolean DEF_SEQUENCE_EXPLORER_USE_SUBFOLDER = true;
	public static final String P_SEQUENCE_EXPLORER_SORT_DATA = "sequenceExplorerSortData";
	public static final boolean DEF_SEQUENCE_EXPLORER_SORT_DATA = false;
	public static final String P_SEQUENCE_EXPLORER_PATH_ROOT_FOLDER = "sequenceExplorerPathRootFolder";
	public static final String DEF_SEQUENCE_EXPLORER_PATH_ROOT_FOLDER = "";
	public static final String P_SEQUENCE_EXPLORER_PATH_PARENT_FOLDER = "sequenceExplorerPathParentFolder";
	public static final String DEF_SEQUENCE_EXPLORER_PATH_PARENT_FOLDER = "";
	public static final String P_SEQUENCE_EXPLORER_PATH_SUB_FOLDER = "sequenceExplorerPathSubFolder";
	public static final String DEF_SEQUENCE_EXPLORER_PATH_SUB_FOLDER = "";
	public static final String P_SEQUENCE_EXPLORER_PATH_DIALOG_FOLDER = "sequenceExplorerPathDialogFolder";
	public static final String DEF_SEQUENCE_EXPLORER_PATH_DIALOG_FOLDER = "";
	/*
	 * Quanititation
	 */
	public static final String P_USE_QUANTITATION_REFERENCE_LIST = "useQuantitationReferenceList";
	public static final boolean DEF_USE_QUANTITATION_REFERENCE_LIST = true;
	public static final String P_QUANTITATION_REFERENCE_LIST = "quantitationReferenceList";
	public static final String DEF_QUANTITATION_REFERENCE_LIST = "";
	/*
	 * PCR
	 */
	public static final String P_PCR_DEFAULT_COLOR = "pcrDefaultColor";
	public static final String DEF_PCR_DEFAULT_COLOR = "192,192,192";
	public static final String P_PCR_PLATE_COLOR_CODES = "pcrColorCodes";
	public static final String DEF_PCR_PLATE_COLOR_CODES = "";
	public static final String P_PCR_WELL_COLOR_CODES = "pcrWellColorCodes";
	public static final String DEF_PCR_WELL_COLOR_CODES = "";
	public static final String P_PCR_SAVE_AS_FOLDER = "pcrSaveAsFolder";
	public static final String DEF_PCR_SAVE_AS_FOLDER = "";
	public static final String P_PCR_REFERENCE_LABEL = "pcrReferenceLabel";
	public static final String DEF_PCR_REFERENCE_LABEL = LabelSetting.COORDINATE_SAMPLENAME.name();
	/*
	 * Processor
	 */
	public static final String P_PROCESSOR_SELECTION_DATA_CATEGORY = "processorSelectionDataCategory";
	public static final boolean DEF_PROCESSOR_SELECTION_DATA_CATEGORY = true;
	/*
	 * Molecules
	 */
	public static final String P_MOLECULE_PATH_EXPORT = "moleculePathExport";
	public static final String DEF_MOLECULE_PATH_EXPORT = "";
	public static final String P_LENGTH_MOLECULE_NAME_EXPORT = "lengthMoleculeNameExport";
	public static final int DEF_LENGTH_MOLECULE_NAME_EXPORT = 40;
	public static final String P_MOLECULE_IMAGE_SERVICE_ID = "moleculeImageServiceId";
	public static final String DEF_MOLECULE_IMAGE_SERVICE_ID = "";
	/*
	 * Peaks Merge
	 */
	public static final String P_MERGE_PEAKS_CALCULATION_TYPE = "mergePeaksCalculationType";
	public static final String DEF_MERGE_PEAKS_CALCULATION_TYPE = CalculationType.SUM.name();
	public static final String P_MERGE_PEAKS_IDENTIFICATION_TARGETS = "mergePeaksIdentificationTargets";
	public static final boolean DEF_MERGE_PEAKS_IDENTIFICATION_TARGETS = true;
	public static final String P_MERGE_PEAKS_DELETE_ORIGINS = "mergePeaksDeleteOrigins";
	public static final boolean DEF_MERGE_PEAKS_DELETE_ORIGINS = true;
	/*
	 * Editors
	 */
	public static final String P_EDITOR_TSD = "editorTSD";
	public static final String DEF_EDITOR_TSD = ChromatogramEditorTSD.CONTRIBUTION_URI;
	/*
	 * Methods
	 */
	public static final String P_CREATE_METHOD_ENABLE_RESUME = "createMethodEnableResume";
	public static final boolean DEF_CREATE_METHOD_ENABLE_RESUME = true;
	/*
	 * Quick-Access Processors
	 * The basic setting is initialized with DataCategory elements
	 */
	public static final String P_QUICK_ACCESS_PROCESSORS = "quickAccessProcessors";
	public static final String DEF_QUICK_ACCESS_PROCESSORS = "";
	//
	public static final String P_FILTER_PATH_CHROMATOGRAM_MSD = "filterPathChromatogramMSD";
	public static final String DEF_FILTER_PATH_CHROMATOGRAM_MSD = "";
	public static final String P_FILTER_PATH_CHROMATOGRAM_CSD = "filterPathChromatogramCSD";
	public static final String DEF_FILTER_PATH_CHROMATOGRAM_CSD = "";
	public static final String P_FILTER_PATH_CHROMATOGRAM_WSD = "filterPathChromatogramWSD";
	public static final String DEF_FILTER_PATH_CHROMATOGRAM_WSD = "";
	public static final String P_FILTER_PATH_RETENTION_INDICES = "filterPathRetentionIndices";
	public static final String DEF_FILTER_PATH_RETENTION_INDICES = "";
	//
	public static final String P_LIST_PATH_IMPORT = "listPathImport";
	public static final String DEF_LIST_PATH_IMPORT = "";
	public static final String P_LIST_PATH_EXPORT = "listPathExport";
	public static final String DEF_LIST_PATH_EXPORT = "";

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	public static IPreferenceStore getPreferenceStore() {

		return Activator.getDefault().getPreferenceStore();
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getDefault().getBundle().getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_STACK_POSITION_HEADER_DATA, DEF_STACK_POSITION_MEASUREMENT_HEADER);
		putDefault(P_STACK_POSITION_CHROMATOGRAM_OVERVIEW, DEF_STACK_POSITION_CHROMATOGRAM_OVERVIEW);
		putDefault(P_STACK_POSITION_CHROMATOGRAM_STATISTICS, DEF_STACK_POSITION_CHROMATOGRAM_STATISTICS);
		putDefault(P_STACK_POSITION_CHROMATOGRAM_SIGNAL_NOISE, DEF_STACK_POSITION_CHROMATOGRAM_SIGNAL_NOISE);
		putDefault(P_STACK_POSITION_CHROMATOGRAM_SCAN_INFO, DEF_STACK_POSITION_CHROMATOGRAM_SCAN_INFO);
		putDefault(P_STACK_POSITION_OVERLAY_CHROMATOGRAM_DEFAULT, DEF_STACK_POSITION_OVERLAY_CHROMATOGRAM_DEFAULT);
		putDefault(P_STACK_POSITION_OVERLAY_CHROMATOGRAM_EXTRA, DEF_STACK_POSITION_OVERLAY_CHROMATOGRAM_EXTRA);
		putDefault(P_STACK_POSITION_OVERLAY_NMR, DEF_STACK_POSITION_OVERLAY_NMR);
		putDefault(P_STACK_POSITION_OVERLAY_VSD, DEF_STACK_POSITION_OVERLAY_VSD);
		putDefault(P_STACK_POSITION_BASELINE_CHROMATOGRAM, DEF_STACK_POSITION_BASELINE_CHROMATOGRAM);
		putDefault(P_STACK_POSITION_TARGETS, DEF_STACK_POSITION_TARGETS);
		putDefault(P_STACK_POSITION_SCAN_CHART, DEF_STACK_POSITION_SCAN_CHART);
		putDefault(P_STACK_POSITION_SCAN_TABLE, DEF_STACK_POSITION_SCAN_TABLE);
		putDefault(P_STACK_POSITION_SCAN_BROWSE, DEF_STACK_POSITION_SCAN_BROWSE);
		putDefault(P_STACK_POSITION_SYNONYMS, DEF_STACK_POSITION_SYNONYMS);
		putDefault(P_STACK_POSITION_COLUMN_INDICES, DEF_STACK_POSITION_COLUMN_INDICES);
		putDefault(P_STACK_POSITION_FLAVOR_MARKER, DEF_STACK_POSITION_FLAVOR_MARKER);
		putDefault(P_STACK_POSITION_LITERATURE, DEF_STACK_POSITION_LITERATURE);
		putDefault(P_STACK_POSITION_CAS_NUMBERS, DEF_STACK_POSITION_CAS_NUMBERS);
		putDefault(P_STACK_POSITION_MOLECULE, DEF_STACK_POSITION_MOLECULE);
		putDefault(P_STACK_POSITION_PEAK_CHART, DEF_STACK_POSITION_PEAK_CHART);
		putDefault(P_STACK_POSITION_PEAK_DETAILS, DEF_STACK_POSITION_PEAK_DETAILS);
		putDefault(P_STACK_POSITION_PEAK_DETECTOR, DEF_STACK_POSITION_PEAK_DETECTOR);
		putDefault(P_STACK_POSITION_PEAK_TRACES, DEF_STACK_POSITION_PEAK_TRACES);
		putDefault(P_STACK_POSITION_PEAK_SCAN_LIST, DEF_STACK_POSITION_PEAK_SCAN_LIST);
		putDefault(P_STACK_POSITION_PEAK_QUANTITATION_LIST, DEF_STACK_POSITION_PEAK_QUANTITATION_LIST);
		putDefault(P_STACK_POSITION_SUBTRACT_SCAN_PART, DEF_STACK_POSITION_SUBTRACT_SCAN_PART);
		putDefault(P_STACK_POSITION_COMBINED_SCAN_PART, DEF_STACK_POSITION_COMBINED_SCAN_PART);
		putDefault(P_STACK_POSITION_COMPARISON_SCAN_CHART, DEF_STACK_POSITION_COMPARISON_SCAN_CHART);
		putDefault(P_STACK_POSITION_QUANTITATION, DEF_STACK_POSITION_QUANTITATION);
		putDefault(P_STACK_POSITION_INTEGRATION_AREA, DEF_STACK_POSITION_INTEGRATION_AREA);
		putDefault(P_STACK_POSITION_INTERNAL_STANDARDS, DEF_STACK_POSITION_INTERNAL_STANDARDS);
		putDefault(P_STACK_POSITION_MEASUREMENT_RESULTS, DEF_STACK_POSITION_MEASUREMENT_RESULTS);
		putDefault(P_STACK_POSITION_CHROMATOGRAM_INDICES, DEF_STACK_POSITION_CHROMATOGRAM_INDICES);
		putDefault(P_STACK_POSITION_CHROMATOGRAM_HEATMAP, DEF_STACK_POSITION_CHROMATOGRAM_HEATMAP);
		putDefault(P_STACK_POSITION_PEAK_QUANTITATION_REFERENCES, DEF_STACK_POSITION_PEAK_QUANTITATION_REFERENCES);
		putDefault(P_STACK_POSITION_QUANT_RESPONSE_CHART, DEF_STACK_POSITION_QUANT_RESPONSE_CHART);
		putDefault(P_STACK_POSITION_QUANT_RESPONSE_LIST, DEF_STACK_POSITION_QUANT_RESPONSE_LIST);
		putDefault(P_STACK_POSITION_QUANT_PEAKS_CHART, DEF_STACK_POSITION_QUANT_PEAKS_CHART);
		putDefault(P_STACK_POSITION_QUANT_PEAKS_LIST, DEF_STACK_POSITION_QUANT_PEAKS_LIST);
		putDefault(P_STACK_POSITION_QUANT_SIGNALS_LIST, DEF_STACK_POSITION_QUANT_SIGNALS_LIST);
		putDefault(P_STACK_POSITION_PENALTY_CALCULATION, DEF_STACK_POSITION_PENALTY_CALCULATION);
		/*
		 * Overlay
		 */
		initializeOverlayDefaults();
		/*
		 * Peak Traces
		 */
		putDefault(P_COLOR_SCHEME_PEAK_TRACES, DEF_COLOR_SCHEME_PEAK_TRACES);
		putDefault(P_MAX_DISPLAY_PEAK_TRACES, DEF_MAX_DISPLAY_PEAK_TRACES);
		putDefault(P_PEAK_TRACES_OFFSET_RETENTION_TIME, DEF_PEAK_TRACES_OFFSET_RETENTION_TIME);
		/*
		 * Header Data
		 */
		putDefault(P_HEADER_DATA_USE_RICH_TEXT_EDITOR, DEF_HEADER_DATA_USE_RICH_TEXT_EDITOR);
		/*
		 * Scans
		 */
		putDefault(P_SCAN_LABEL_FONT_NAME, DEF_SCAN_LABEL_FONT_NAME);
		putDefault(P_SCAN_LABEL_FONT_SIZE, DEF_FONT_SIZE);
		putDefault(P_SCAN_LABEL_FONT_STYLE, DEF_SCAN_LABEL_FONT_STYLE);
		putDefault(P_COLOR_SCAN_1, DEF_COLOR_SCAN_1);
		putDefault(P_COLOR_SCAN_2, DEF_COLOR_SCAN_2);
		putDefault(P_SCAN_LABEL_HIGHEST_INTENSITIES, DEF_SCAN_LABEL_HIGHEST_INTENSITIES);
		putDefault(P_SCAN_LABEL_MODULO_INTENSITIES, DEF_SCAN_LABEL_MODULO_INTENSITIES);
		putDefault(P_AUTOFOCUS_SUBTRACT_SCAN_PART, DEF_AUTOFOCUS_SUBTRACT_SCAN_PART);
		putDefault(P_SCAN_CHART_ENABLE_COMPRESS, DEF_SCAN_CHART_ENABLE_COMPRESS);
		putDefault(P_MAX_COPY_SCAN_TRACES, DEF_MAX_COPY_SCAN_TRACES);
		putDefault(P_SORT_COPY_TRACES, DEF_SORT_COPY_TRACES);
		putDefault(P_TRACES_EXPORT_OPTION, DEF_TRACES_EXPORT_OPTION);
		putDefault(P_SCAN_IDENTIFER_MSD, DEF_SCAN_IDENTIFER_MSD);
		putDefault(P_SCAN_IDENTIFER_WSD, DEF_SCAN_IDENTIFER_WSD);
		putDefault(P_SHOW_SUBTRACT_DIALOG, DEF_SHOW_SUBTRACT_DIALOG);
		putDefault(P_ENABLE_MULTI_SUBTRACT, DEF_ENABLE_MULTI_SUBTRACT);
		putDefault(P_TARGET_IDENTIFER, DEF_TARGET_IDENTIFER);
		putDefault(P_SCAN_IDENTIFER, DEF_SCAN_IDENTIFER);
		//
		putDefault(P_SCAN_CHART_ENABLE_FIXED_RANGE_X, DEF_SCAN_CHART_ENABLE_FIXED_RANGE_X);
		putDefault(P_SCAN_CHART_FIXED_RANGE_START_X, DEF_SCAN_CHART_FIXED_RANGE_START_X);
		putDefault(P_SCAN_CHART_FIXED_RANGE_STOP_X, DEF_SCAN_CHART_FIXED_RANGE_STOP_X);
		putDefault(P_SCAN_CHART_ENABLE_FIXED_RANGE_Y, DEF_SCAN_CHART_ENABLE_FIXED_RANGE_Y);
		putDefault(P_SCAN_CHART_FIXED_RANGE_START_Y, DEF_SCAN_CHART_FIXED_RANGE_START_Y);
		putDefault(P_SCAN_CHART_FIXED_RANGE_STOP_Y, DEF_SCAN_CHART_FIXED_RANGE_STOP_Y);
		//
		putDefault(P_TITLE_X_AXIS_MZ, DEF_TITLE_X_AXIS_MZ);
		putDefault(P_TITLE_X_AXIS_PARENT_MZ, DEF_TITLE_X_AXIS_PARENT_MZ);
		putDefault(P_TITLE_X_AXIS_PARENT_RESOLUTION, DEF_TITLE_X_AXIS_PARENT_RESOLUTION);
		putDefault(P_TITLE_X_AXIS_DAUGHTER_MZ, DEF_TITLE_X_AXIS_DAUGHTER_MZ);
		putDefault(P_TITLE_X_AXIS_DAUGHTER_RESOLUTION, DEF_TITLE_X_AXIS_DAUGHTER_RESOLUTION);
		putDefault(P_TITLE_X_AXIS_COLLISION_ENERGY, DEF_TITLE_X_AXIS_COLLISION_ENERGY);
		putDefault(P_TITLE_X_AXIS_WAVELENGTH, DEF_TITLE_X_AXIS_WAVELENGTH);
		//
		putDefault(P_TRACES_VIRTUAL_TABLE, DEF_TRACES_VIRTUAL_TABLE);
		putDefault(P_LIMIT_SIM_TRACES, DEF_LIMIT_SIM_TRACES);
		//
		putDefault(P_HEATMAP_SCALE_INTENSITY_MIN_MSD, DEF_HEATMAP_SCALE_INTENSITY_MIN_MSD);
		putDefault(P_HEATMAP_SCALE_INTENSITY_MAX_MSD, DEF_HEATMAP_SCALE_INTENSITY_MAX_MSD);
		putDefault(P_HEATMAP_SCALE_INTENSITY_MIN_WSD, DEF_HEATMAP_SCALE_INTENSITY_MIN_WSD);
		putDefault(P_HEATMAP_SCALE_INTENSITY_MAX_WSD, DEF_HEATMAP_SCALE_INTENSITY_MAX_WSD);
		putDefault(P_HEATMAP_SCALE_INTENSITY_MIN_TSD, DEF_HEATMAP_SCALE_INTENSITY_MIN_TSD);
		putDefault(P_HEATMAP_SCALE_INTENSITY_MAX_TSD, DEF_HEATMAP_SCALE_INTENSITY_MAX_TSD);
		putDefault(P_HEATMAP_ENABLE_ZOOM, DEF_HEATMAP_ENABLE_ZOOM);
		/*
		 * Peaks
		 */
		putDefault(P_SHOW_PEAK_BASELINE, DEF_SHOW_PEAK_BASELINE);
		putDefault(P_COLOR_PEAK_BASELINE, DEF_COLOR_PEAK_BASELINE);
		putDefault(P_SHOW_PEAK, DEF_SHOW_PEAK);
		putDefault(P_COLOR_PEAK_1, DEF_COLOR_PEAK_1);
		putDefault(P_COLOR_PEAK_2, DEF_COLOR_PEAK_2);
		putDefault(P_SHOW_PEAK_TANGENTS, DEF_SHOW_PEAK_TANGENTS);
		putDefault(P_COLOR_PEAK_TANGENTS, DEF_COLOR_PEAK_TANGENTS);
		putDefault(P_SHOW_PEAK_WIDTH_0, DEF_SHOW_PEAK_WIDTH_0);
		putDefault(P_COLOR_PEAK_WIDTH_0, DEF_COLOR_PEAK_WIDTH_0);
		putDefault(P_SHOW_PEAK_WIDTH_50, DEF_SHOW_PEAK_WIDTH_50);
		putDefault(P_COLOR_PEAK_WIDTH_50, DEF_COLOR_PEAK_WIDTH_50);
		putDefault(P_SHOW_PEAK_WIDTH_CONDAL_BOSH, DEF_SHOW_PEAK_WIDTH_CONDAL_BOSH);
		putDefault(P_COLOR_PEAK_WIDTH_CONDAL_BOSH, DEF_COLOR_PEAK_WIDTH_CONDAL_BOSH);
		putDefault(P_COLOR_PEAK_DETECTOR_CHROMATOGRAM, DEF_COLOR_PEAK_DETECTOR_CHROMATOGRAM);
		putDefault(P_SHOW_PEAK_DETECTOR_CHROMATOGRAM_AREA, DEF_SHOW_PEAK_DETECTOR_CHROMATOGRAM_AREA);
		putDefault(P_PEAK_DETECTOR_SCAN_MARKER_SIZE, DEF_PEAK_DETECTOR_SCAN_MARKER_SIZE);
		putDefault(P_PEAK_DETECTOR_SCAN_MARKER_COLOR, DEF_PEAK_DETECTOR_SCAN_MARKER_COLOR);
		putDefault(P_PEAK_DETECTOR_SCAN_MARKER_TYPE, DEF_PEAK_DETECTOR_SCAN_MARKER_TYPE);
		/*
		 * Peak Chart
		 */
		putDefault(P_COLOR_SCHEME_DISPLAY_PEAKS, DEF_COLOR_SCHEME_DISPLAY_PEAKS);
		putDefault(P_SHOW_AREA_DISPLAY_PEAKS, DEF_SHOW_AREA_DISPLAY_PEAKS);
		//
		putDefault(P_SHOW_X_AXIS_MILLISECONDS_PEAKS, DEF_SHOW_X_AXIS_MILLISECONDS_PEAKS);
		putDefault(P_POSITION_X_AXIS_MILLISECONDS_PEAKS, DEF_POSITION_X_AXIS_MILLISECONDS_PEAKS);
		putDefault(P_COLOR_X_AXIS_MILLISECONDS_PEAKS, DEF_COLOR_X_AXIS_MILLISECONDS_PEAKS);
		putDefault(P_GRIDLINE_STYLE_X_AXIS_MILLISECONDS_PEAKS, DEF_GRIDLINE_STYLE_X_AXIS_MILLISECONDS_PEAKS);
		putDefault(P_GRIDLINE_COLOR_X_AXIS_MILLISECONDS_PEAKS, DEF_GRIDLINE_COLOR_X_AXIS_MILLISECONDS_PEAKS);
		//
		putDefault(P_SHOW_X_AXIS_MINUTES_PEAKS, DEF_SHOW_X_AXIS_MINUTES_PEAKS);
		putDefault(P_POSITION_X_AXIS_MINUTES_PEAKS, DEF_POSITION_X_AXIS_MINUTES_PEAKS);
		putDefault(P_COLOR_X_AXIS_MINUTES_PEAKS, DEF_COLOR_X_AXIS_MINUTES_PEAKS);
		putDefault(P_GRIDLINE_STYLE_X_AXIS_MINUTES_PEAKS, DEF_GRIDLINE_STYLE_X_AXIS_MINUTES_PEAKS);
		putDefault(P_GRIDLINE_COLOR_X_AXIS_MINUTES_PEAKS, DEF_GRIDLINE_COLOR_X_AXIS_MINUTES_PEAKS);
		//
		putDefault(P_SHOW_Y_AXIS_INTENSITY_PEAKS, DEF_SHOW_Y_AXIS_INTENSITY_PEAKS);
		putDefault(P_POSITION_Y_AXIS_INTENSITY_PEAKS, DEF_POSITION_Y_AXIS_INTENSITY_PEAKS);
		putDefault(P_COLOR_Y_AXIS_INTENSITY_PEAKS, DEF_COLOR_Y_AXIS_INTENSITY_PEAKS);
		putDefault(P_GRIDLINE_STYLE_Y_AXIS_INTENSITY_PEAKS, DEF_GRIDLINE_STYLE_Y_AXIS_INTENSITY_PEAKS);
		putDefault(P_GRIDLINE_COLOR_Y_AXIS_INTENSITY_PEAKS, DEF_GRIDLINE_COLOR_Y_AXIS_INTENSITY_PEAKS);
		//
		putDefault(P_SHOW_Y_AXIS_RELATIVE_INTENSITY_PEAKS, DEF_SHOW_Y_AXIS_RELATIVE_INTENSITY_PEAKS);
		putDefault(P_POSITION_Y_AXIS_RELATIVE_INTENSITY_PEAKS, DEF_POSITION_Y_AXIS_RELATIVE_INTENSITY_PEAKS);
		putDefault(P_COLOR_Y_AXIS_RELATIVE_INTENSITY_PEAKS, DEF_COLOR_Y_AXIS_RELATIVE_INTENSITY_PEAKS);
		putDefault(P_GRIDLINE_STYLE_Y_AXIS_RELATIVE_INTENSITY_PEAKS, DEF_GRIDLINE_STYLE_Y_AXIS_RELATIVE_INTENSITY_PEAKS);
		putDefault(P_GRIDLINE_COLOR_Y_AXIS_RELATIVE_INTENSITY_PEAKS, DEF_GRIDLINE_COLOR_Y_AXIS_RELATIVE_INTENSITY_PEAKS);
		/*
		 * Targets
		 */
		putDefault(P_USE_TARGET_LIST, DEF_USE_TARGET_LIST);
		putDefault(P_TARGET_LIST, DEF_TARGET_LIST);
		putDefault(P_PROPAGATE_TARGET_ON_UPDATE, DEF_PROPAGATE_TARGET_ON_UPDATE);
		putDefault(P_TARGETS_TABLE_SORTABLE, DEF_TARGETS_TABLE_SORTABLE);
		putDefault(P_TARGETS_TABLE_SHOW_DEVIATION_RT, DEF_TARGETS_TABLE_SHOW_DEVIATION_RT);
		putDefault(P_TARGETS_TABLE_SHOW_DEVIATION_RI, DEF_TARGETS_TABLE_SHOW_DEVIATION_RI);
		putDefault(P_TARGET_TEMPLATE_LIBRARY_IMPORT_FOLDER, DEF_TARGET_TEMPLATE_LIBRARY_IMPORT_FOLDER);
		putDefault(P_USE_ABSOLUTE_DEVIATION_RETENTION_TIME, DEF_USE_ABSOLUTE_DEVIATION_RETENTION_TIME);
		putDefault(P_RETENTION_TIME_DEVIATION_REL_OK, DEF_RETENTION_TIME_DEVIATION_REL_OK);
		putDefault(P_RETENTION_TIME_DEVIATION_REL_WARN, DEF_RETENTION_TIME_DEVIATION_REL_WARN);
		putDefault(P_RETENTION_TIME_DEVIATION_ABS_OK, DEF_RETENTION_TIME_DEVIATION_ABS_OK);
		putDefault(P_RETENTION_TIME_DEVIATION_ABS_WARN, DEF_RETENTION_TIME_DEVIATION_ABS_WARN);
		putDefault(P_USE_ABSOLUTE_DEVIATION_RETENTION_INDEX, DEF_USE_ABSOLUTE_DEVIATION_RETENTION_INDEX);
		putDefault(P_RETENTION_INDEX_DEVIATION_REL_OK, DEF_RETENTION_INDEX_DEVIATION_REL_OK);
		putDefault(P_RETENTION_INDEX_DEVIATION_REL_WARN, DEF_RETENTION_INDEX_DEVIATION_REL_WARN);
		putDefault(P_RETENTION_INDEX_DEVIATION_ABS_OK, DEF_RETENTION_INDEX_DEVIATION_ABS_OK);
		putDefault(P_RETENTION_INDEX_DEVIATION_ABS_WARN, DEF_RETENTION_INDEX_DEVIATION_ABS_WARN);
		putDefault(P_ADD_UNKNOWN_AFTER_DELETE_TARGETS_ALL, DEF_ADD_UNKNOWN_AFTER_DELETE_TARGETS_ALL);
		putDefault(P_MATCH_QUALITY_UNKNOWN_TARGET, DEF_MATCH_QUALITY_UNKNOWN_TARGET);
		putDefault(P_UNKNOWN_TARGET_ADD_RETENTION_INDEX, DEF_UNKNOWN_TARGET_ADD_RETENTION_INDEX);
		putDefault(P_VERIFY_UNKNOWN_TARGET, DEF_VERIFY_UNKNOWN_TARGET);
		putDefault(P_ACTIVATE_TARGET_DND_WINDOWS, DEF_ACTIVATE_TARGET_DND_WINDOWS);
		/*
		 * Edit History
		 */
		putDefault(P_EDIT_HISTORY_HIDE_PROCESS_METHOD_ENTRIES, DEF_EDIT_HISTORY_HIDE_PROCESS_METHOD_ENTRIES);
		//
		initializeChromatogramDefaults();
		/*
		 * Calibration Chart
		 */
		putDefault(P_COLOR_SCHEME_DISPLAY_CALIBRATION, DEF_COLOR_SCHEME_DISPLAY_CALIBRATION);
		//
		putDefault(P_SHOW_X_AXIS_CONCENTRATION_CALIBRATION, DEF_SHOW_X_AXIS_CONCENTRATION_CALIBRATION);
		putDefault(P_POSITION_X_AXIS_CONCENTRATION_CALIBRATION, DEF_POSITION_X_AXIS_CONCENTRATION_CALIBRATION);
		putDefault(P_COLOR_X_AXIS_CONCENTRATION_CALIBRATION, DEF_COLOR_X_AXIS_CONCENTRATION_CALIBRATION);
		putDefault(P_GRIDLINE_STYLE_X_AXIS_CONCENTRATION_CALIBRATION, DEF_GRIDLINE_STYLE_X_AXIS_CONCENTRATION_CALIBRATION);
		putDefault(P_GRIDLINE_COLOR_X_AXIS_CONCENTRATION_CALIBRATION, DEF_GRIDLINE_COLOR_X_AXIS_CONCENTRATION_CALIBRATION);
		//
		putDefault(P_SHOW_Y_AXIS_RESPONSE_CALIBRATION, DEF_SHOW_Y_AXIS_RESPONSE_CALIBRATION);
		putDefault(P_POSITION_Y_AXIS_RESPONSE_CALIBRATION, DEF_POSITION_Y_AXIS_RESPONSE_CALIBRATION);
		putDefault(P_COLOR_Y_AXIS_RESPONSE_CALIBRATION, DEF_COLOR_Y_AXIS_RESPONSE_CALIBRATION);
		putDefault(P_GRIDLINE_STYLE_Y_AXIS_RESPONSE_CALIBRATION, DEF_GRIDLINE_STYLE_Y_AXIS_RESPONSE_CALIBRATION);
		putDefault(P_GRIDLINE_COLOR_Y_AXIS_RESPONSE_CALIBRATION, DEF_GRIDLINE_COLOR_Y_AXIS_RESPONSE_CALIBRATION);
		//
		putDefault(P_SHOW_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION, DEF_SHOW_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION);
		putDefault(P_POSITION_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION, DEF_POSITION_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION);
		putDefault(P_COLOR_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION, DEF_COLOR_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION);
		putDefault(P_GRIDLINE_STYLE_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION, DEF_GRIDLINE_STYLE_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION);
		putDefault(P_GRIDLINE_COLOR_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION, DEF_GRIDLINE_COLOR_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION);
		/*
		 * File Explorer
		 */
		putDefault(P_SHOW_DATA_MSD, DEF_SHOW_DATA_MSD);
		putDefault(P_SHOW_DATA_CSD, DEF_SHOW_DATA_CSD);
		putDefault(P_SHOW_DATA_WSD, DEF_SHOW_DATA_WSD);
		putDefault(P_SHOW_DATA_VSD, DEF_SHOW_DATA_VSD);
		putDefault(P_SHOW_DATA_TSD, DEF_SHOW_DATA_TSD);
		putDefault(P_SHOW_LIBRARY_MSD, DEF_SHOW_LIBRARY_MSD);
		putDefault(P_SHOW_SCANS_MSD, DEF_SHOW_SCANS_MSD);
		putDefault(P_SHOW_DATA_SCAN_VSD, DEF_SHOW_DATA_SCAN_VSD);
		putDefault(P_SHOW_DATA_SCAN_WSD, DEF_SHOW_DATA_SCAN_WSD);
		putDefault(P_SHOW_DATA_NMR, DEF_SHOW_DATA_NMR);
		putDefault(P_SHOW_DATA_CAL, DEF_SHOW_DATA_CAL);
		putDefault(P_SHOW_DATA_PCR, DEF_SHOW_DATA_PCR);
		putDefault(P_SHOW_DATA_SEQUENCE, DEF_SHOW_DATA_SEQUENCE);
		putDefault(P_SHOW_DATA_METHOD, DEF_SHOW_DATA_METHOD);
		putDefault(P_SHOW_DATA_QUANT_DB, DEF_SHOW_DATA_QUANT_DB);
		/*
		 * Lists
		 */
		putDefault(P_SHOW_PEAKS_IN_LIST, DEF_SHOW_PEAKS_IN_LIST);
		putDefault(P_SHOW_PEAKS_IN_SELECTED_RANGE, DEF_SHOW_PEAKS_IN_SELECTED_RANGE);
		putDefault(P_SHOW_SCANS_IN_LIST, DEF_SHOW_SCANS_IN_LIST);
		putDefault(P_SHOW_SCANS_IN_SELECTED_RANGE, DEF_SHOW_SCANS_IN_SELECTED_RANGE);
		putDefault(P_SHOW_PEAK_PROFILES_SELECTION_ALL, DEF_SHOW_PEAK_PROFILES_SELECTION_ALL);
		/*
		 * Baseline
		 */
		putDefault(P_BASELINE_CHART_COMPRESSION_TYPE, DEF_BASELINE_CHART_COMPRESSION_TYPE);
		putDefault(P_COLOR_SCHEME_DISPLAY_BASELINE, DEF_COLOR_SCHEME_DISPLAY_BASELINE);
		/*
		 * Sequences
		 */
		putDefault(P_SEQUENCE_EXPLORER_USE_SUBFOLDER, DEF_SEQUENCE_EXPLORER_USE_SUBFOLDER);
		putDefault(P_SEQUENCE_EXPLORER_SORT_DATA, DEF_SEQUENCE_EXPLORER_SORT_DATA);
		putDefault(P_SEQUENCE_EXPLORER_PATH_ROOT_FOLDER, DEF_SEQUENCE_EXPLORER_PATH_ROOT_FOLDER);
		putDefault(P_SEQUENCE_EXPLORER_PATH_PARENT_FOLDER, DEF_SEQUENCE_EXPLORER_PATH_PARENT_FOLDER);
		putDefault(P_SEQUENCE_EXPLORER_PATH_SUB_FOLDER, DEF_SEQUENCE_EXPLORER_PATH_SUB_FOLDER);
		putDefault(P_SEQUENCE_EXPLORER_PATH_DIALOG_FOLDER, DEF_SEQUENCE_EXPLORER_PATH_DIALOG_FOLDER);
		/*
		 * Quantitation
		 */
		putDefault(P_USE_QUANTITATION_REFERENCE_LIST, DEF_USE_QUANTITATION_REFERENCE_LIST);
		putDefault(P_QUANTITATION_REFERENCE_LIST, DEF_QUANTITATION_REFERENCE_LIST);
		/*
		 * PCR
		 */
		putDefault(P_PCR_DEFAULT_COLOR, DEF_PCR_DEFAULT_COLOR);
		putDefault(P_PCR_PLATE_COLOR_CODES, DEF_PCR_PLATE_COLOR_CODES);
		putDefault(P_PCR_WELL_COLOR_CODES, DEF_PCR_WELL_COLOR_CODES);
		putDefault(P_PCR_SAVE_AS_FOLDER, DEF_PCR_SAVE_AS_FOLDER);
		putDefault(P_PCR_REFERENCE_LABEL, DEF_PCR_REFERENCE_LABEL);
		/*
		 * Molecules
		 */
		putDefault(P_MOLECULE_PATH_EXPORT, DEF_MOLECULE_PATH_EXPORT);
		putDefault(P_LENGTH_MOLECULE_NAME_EXPORT, DEF_LENGTH_MOLECULE_NAME_EXPORT);
		putDefault(P_MOLECULE_IMAGE_SERVICE_ID, DEF_MOLECULE_IMAGE_SERVICE_ID);
		/*
		 * Merge Peaks
		 */
		putDefault(P_MERGE_PEAKS_CALCULATION_TYPE, DEF_MERGE_PEAKS_CALCULATION_TYPE);
		putDefault(P_MERGE_PEAKS_IDENTIFICATION_TARGETS, DEF_MERGE_PEAKS_IDENTIFICATION_TARGETS);
		putDefault(P_MERGE_PEAKS_DELETE_ORIGINS, DEF_MERGE_PEAKS_DELETE_ORIGINS);
		/*
		 * Methods
		 */
		putDefault(P_CREATE_METHOD_ENABLE_RESUME, DEF_CREATE_METHOD_ENABLE_RESUME);
		//
		putDefault(P_FILTER_PATH_CHROMATOGRAM_MSD, DEF_FILTER_PATH_CHROMATOGRAM_MSD);
		putDefault(P_FILTER_PATH_CHROMATOGRAM_CSD, DEF_FILTER_PATH_CHROMATOGRAM_CSD);
		putDefault(P_FILTER_PATH_CHROMATOGRAM_WSD, DEF_FILTER_PATH_CHROMATOGRAM_WSD);
		putDefault(P_FILTER_PATH_RETENTION_INDICES, DEF_FILTER_PATH_RETENTION_INDICES);
		//
		putDefault(P_LIST_PATH_IMPORT, DEF_LIST_PATH_IMPORT);
		putDefault(P_LIST_PATH_EXPORT, DEF_LIST_PATH_EXPORT);
	}

	private void initializeChromatogramDefaults() {

		/*
		 * Chromatogram
		 */
		putDefault(P_CHROMATOGRAM_CHART_COMPRESSION_TYPE, DEF_CHROMATOGRAM_CHART_COMPRESSION_TYPE);
		putDefault(P_COLOR_CHROMATOGRAM, DEF_COLOR_CHROMATOGRAM);
		putDefault(P_COLOR_CHROMATOGRAM_INACTIVE, DEF_COLOR_CHROMATOGRAM_INACTIVE);
		putDefault(P_ENABLE_CHROMATOGRAM_AREA, DEF_ENABLE_CHROMATOGRAM_AREA);
		putDefault(P_COLOR_CHROMATOGRAM_SELECTED_PEAK, DEF_COLOR_CHROMATOGRAM_SELECTED_PEAK);
		putDefault(P_CHROMATOGRAM_SELECTED_PEAK_SCAN_MARKER_SIZE, DEF_CHROMATOGRAM_SELECTED_PEAK_SCAN_MARKER_SIZE);
		putDefault(P_CHROMATOGRAM_SELECTED_PEAK_SCAN_MARKER_TYPE, DEF_CHROMATOGRAM_SELECTED_PEAK_SCAN_MARKER_TYPE);
		putDefault(P_COLOR_CHROMATOGRAM_SELECTED_SCAN, DEF_COLOR_CHROMATOGRAM_SELECTED_SCAN);
		putDefault(P_CHROMATOGRAM_SELECTED_SCAN_MARKER_SIZE, DEF_CHROMATOGRAM_SELECTED_SCAN_MARKER_SIZE);
		putDefault(P_CHROMATOGRAM_SELECTED_SCAN_MARKER_TYPE, DEF_CHROMATOGRAM_SELECTED_SCAN_MARKER_TYPE);
		putDefault(P_CHROMATOGRAM_PEAK_LABEL_FONT_NAME, DEF_CHROMATOGRAM_PEAK_LABEL_FONT_NAME);
		putDefault(P_CHROMATOGRAM_PEAK_LABEL_FONT_SIZE, DEF_FONT_SIZE);
		putDefault(P_CHROMATOGRAM_PEAK_LABEL_FONT_STYLE, DEF_CHROMATOGRAM_PEAK_LABEL_FONT_STYLE);
		putDefault(P_SHOW_CHROMATOGRAM_BASELINE, DEF_SHOW_CHROMATOGRAM_BASELINE);
		putDefault(P_COLOR_CHROMATOGRAM_BASELINE, DEF_COLOR_CHROMATOGRAM_BASELINE);
		putDefault(P_ENABLE_BASELINE_AREA, DEF_ENABLE_BASELINE_AREA);
		putDefault(P_CHROMATOGRAM_PEAK_LABEL_SYMBOL_SIZE, DEF_SYMBOL_SIZE);
		putDefault(P_CHROMATOGRAM_SELECTED_PEAK_MARKER_TYPE, DEF_CHROMATOGRAM_SELECTED_PEAK_MARKER_TYPE);
		putDefault(P_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL_MARKER_TYPE, DEF_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL_MARKER_TYPE);
		putDefault(P_CHROMATOGRAM_PEAKS_INACTIVE_NORMAL_MARKER_TYPE, DEF_CHROMATOGRAM_PEAKS_INACTIVE_NORMAL_MARKER_TYPE);
		putDefault(P_CHROMATOGRAM_PEAKS_ACTIVE_ISTD_MARKER_TYPE, DEF_CHROMATOGRAM_PEAKS_ACTIVE_ISTD_MARKER_TYPE);
		putDefault(P_CHROMATOGRAM_PEAKS_INACTIVE_ISTD_MARKER_TYPE, DEF_CHROMATOGRAM_PEAKS_INACTIVE_ISTD_MARKER_TYPE);
		//
		putDefault(P_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL, DEF_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL);
		putDefault(P_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL_TARGETS_HIDDEN, DEF_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_NORMAL_TARGETS_HIDDEN);
		putDefault(P_COLOR_CHROMATOGRAM_PEAKS_INACTIVE_NORMAL, DEF_COLOR_CHROMATOGRAM_PEAKS_INACTIVE_NORMAL);
		putDefault(P_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_ISTD, DEF_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_ISTD);
		putDefault(P_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_ISTD_TARGETS_HIDDEN, DEF_COLOR_CHROMATOGRAM_PEAKS_ACTIVE_ISTD_TARGETS_HIDDEN);
		putDefault(P_COLOR_CHROMATOGRAM_PEAKS_INACTIVE_ISTD, DEF_COLOR_CHROMATOGRAM_PEAKS_INACTIVE_ISTD);
		//
		putDefault(P_CHROMATOGRAM_ACTIVE_TARGET_LABEL_FONT_COLOR, DEF_CHROMATOGRAM_ACTIVE_TARGET_LABEL_FONT_COLOR);
		putDefault(P_CHROMATOGRAM_INACTIVE_TARGET_LABEL_FONT_COLOR, DEF_CHROMATOGRAM_INACTIVE_TARGET_LABEL_FONT_COLOR);
		putDefault(P_CHROMATOGRAM_ID_TARGET_LABEL_FONT_COLOR, DEF_CHROMATOGRAM_ID_TARGET_LABEL_FONT_COLOR);
		//
		putDefault(P_CHROMATOGRAM_ACTIVE_TARGET_LABEL_FONT_DARK_COLOR, DEF_CHROMATOGRAM_ACTIVE_TARGET_LABEL_FONT_DARK_COLOR);
		putDefault(P_CHROMATOGRAM_INACTIVE_TARGET_LABEL_FONT_DARK_COLOR, DEF_CHROMATOGRAM_INACTIVE_TARGET_LABEL_FONT_DARK_COLOR);
		putDefault(P_CHROMATOGRAM_ID_TARGET_LABEL_FONT_DARK_COLOR, DEF_CHROMATOGRAM_ID_TARGET_LABEL_FONT_DARK_COLOR);
		//
		putDefault(P_CHROMATOGRAM_SCAN_LABEL_SYMBOL_SIZE, DEF_SYMBOL_SIZE);
		putDefault(P_CHROMATOGRAM_SCAN_LABEL_FONT_NAME, DEF_CHROMATOGRAM_SCAN_LABEL_FONT_NAME);
		putDefault(P_CHROMATOGRAM_SCAN_LABEL_FONT_SIZE, DEF_FONT_SIZE);
		putDefault(P_CHROMATOGRAM_SCAN_LABEL_FONT_STYLE, DEF_CHROMATOGRAM_SCAN_LABEL_FONT_STYLE);
		putDefault(P_COLOR_CHROMATOGRAM_IDENTIFIED_SCAN, DEF_COLOR_CHROMATOGRAM_IDENTIFIED_SCAN);
		putDefault(P_CHROMATOGRAM_SCAN_MARKER_TYPE, DEF_CHROMATOGRAM_SCAN_MARKER_TYPE);
		putDefault(P_CHROMATOGRAM_IDENTIFIED_SCAN_MARKER_TYPE, DEF_CHROMATOGRAM_IDENTIFIED_SCAN_MARKER_TYPE);
		putDefault(P_MOVE_RETENTION_TIME_ON_PEAK_SELECTION, DEF_MOVE_RETENTION_TIME_ON_PEAK_SELECTION);
		putDefault(P_ALTERNATE_WINDOW_MOVE_DIRECTION, DEF_ALTERNATE_WINDOW_MOVE_DIRECTION);
		putDefault(P_CONDENSE_CYCLE_NUMBER_SCANS, DEF_CONDENSE_CYCLE_NUMBER_SCANS);
		putDefault(P_SET_CHROMATOGRAM_INTENSITY_RANGE, DEF_SET_CHROMATOGRAM_INTENSITY_RANGE);
		putDefault(P_CHROMATOGRAM_TRANSFER_DELTA_RETENTION_TIME, DEF_CHROMATOGRAM_TRANSFER_DELTA_RETENTION_TIME);
		putDefault(P_CHROMATOGRAM_TRANSFER_BEST_TARGET_ONLY, DEF_CHROMATOGRAM_TRANSFER_BEST_TARGET_ONLY);
		putDefault(P_STRETCH_CHROMATOGRAM_MILLISECONDS_SCAN_DELAY, DEF_STRETCH_CHROMATOGRAM_MILLISECONDS_SCAN_DELAY);
		putDefault(P_STRETCH_CHROMATOGRAM_MILLISECONDS_LENGTH, DEF_STRETCH_CHROMATOGRAM_MILLISECONDS_LENGTH);
		putDefault(P_CHROMATOGRAM_EXTEND_Y, DEF_CHROMATOGRAM_EXTEND_Y);
		putDefault(P_CHROMATOGRAM_RESTRICT_SELECT_X, DEF_CHROMATOGRAM_RESTRICT_SELECT_X);
		putDefault(P_CHROMATOGRAM_RESTRICT_SELECT_Y, DEF_CHROMATOGRAM_RESTRICT_SELECT_Y);
		putDefault(P_CHROMATOGRAM_FORCE_ZERO_MIN_Y_MSD, DEF_CHROMATOGRAM_FORCE_ZERO_MIN_Y_MSD);
		putDefault(P_CHROMATOGRAM_REFERENCE_ZOOM_ZERO_X, DEF_CHROMATOGRAM_REFERENCE_ZOOM_ZERO_X);
		putDefault(P_CHROMATOGRAM_REFERENCE_ZOOM_ZERO_Y, DEF_CHROMATOGRAM_REFERENCE_ZOOM_ZERO_Y);
		putDefault(P_CHROMATOGRAM_RESTRICT_ZOOM_X, DEF_CHROMATOGRAM_RESTRICT_ZOOM_X);
		putDefault(P_CHROMATOGRAM_RESTRICT_ZOOM_Y, DEF_CHROMATOGRAM_RESTRICT_ZOOM_Y);
		putDefault(P_CHROMATOGRAM_EDITOR_LABEL, DEF_CHROMATOGRAM_EDITOR_LABEL);
		putDefault(P_CHROMATOGRAM_REFERENCE_LABEL, DEF_CHROMATOGRAM_REFERENCE_LABEL);
		putDefault(P_CHROMATOGRAM_TRANSFER_NAME_TO_REFERENCES_HEADER_FIELD, DEF_CHROMATOGRAM_TRANSFER_NAME_TO_REFERENCES_HEADER_FIELD);
		putDefault(P_CHROMATOGRAM_TRANSFER_COLUMN_TYPE_TO_REFERENCES, DEF_CHROMATOGRAM_TRANSFER_COLUMN_TYPE_TO_REFERENCES);
		putDefault(P_CHROMATOGRAM_PROCESSOR_TOOLBAR, DEF_CHROMATOGRAM_PROCESSOR_TOOLBAR);
		putDefault(P_CHROMATOGRAM_SHOW_METHODS_TOOLBAR, DEF_CHROMATOGRAM_SHOW_METHODS_TOOLBAR);
		putDefault(P_CHROMATOGRAM_SHOW_REFERENCES_COMBO, DEF_CHROMATOGRAM_SHOW_REFERENCES_COMBO);
		//
		putDefault(P_TITLE_X_AXIS_MILLISECONDS, DEF_TITLE_X_AXIS_MILLISECONDS);
		putDefault(P_FORMAT_X_AXIS_MILLISECONDS, DEF_FORMAT_X_AXIS_MILLISECONDS);
		putDefault(P_SHOW_X_AXIS_MILLISECONDS, DEF_SHOW_X_AXIS_MILLISECONDS);
		putDefault(P_POSITION_X_AXIS_MILLISECONDS, DEF_POSITION_X_AXIS_MILLISECONDS);
		putDefault(P_COLOR_X_AXIS_MILLISECONDS, DEF_COLOR_X_AXIS_MILLISECONDS);
		putDefault(P_COLOR_X_AXIS_MILLISECONDS_DARKTHEME, DEF_COLOR_X_AXIS_MILLISECONDS_DARKTHEME);
		putDefault(P_FONT_NAME_X_AXIS_MILLISECONDS, DEF_FONT_NAME_X_AXIS_MILLISECONDS);
		putDefault(P_FONT_SIZE_X_AXIS_MILLISECONDS, DEF_FONT_SIZE);
		putDefault(P_FONT_STYLE_X_AXIS_MILLISECONDS, DEF_FONT_STYLE_X_AXIS_MILLISECONDS);
		putDefault(P_GRIDLINE_STYLE_X_AXIS_MILLISECONDS, DEF_GRIDLINE_STYLE_X_AXIS_MILLISECONDS);
		putDefault(P_GRIDLINE_COLOR_X_AXIS_MILLISECONDS, DEF_GRIDLINE_COLOR_X_AXIS_MILLISECONDS);
		putDefault(P_SHOW_X_AXIS_TITLE_MILLISECONDS, DEF_SHOW_X_AXIS_TITLE_MILLISECONDS);
		//
		putDefault(P_TITLE_X_AXIS_RETENTION_INDEX, DEF_TITLE_X_AXIS_RETENTION_INDEX);
		putDefault(P_FORMAT_X_AXIS_RETENTION_INDEX, DEF_FORMAT_X_AXIS_RETENTION_INDEX);
		putDefault(P_SHOW_X_AXIS_RETENTION_INDEX, DEF_SHOW_X_AXIS_RETENTION_INDEX);
		putDefault(P_POSITION_X_AXIS_RETENTION_INDEX, DEF_POSITION_X_AXIS_RETENTION_INDEX);
		putDefault(P_COLOR_X_AXIS_RETENTION_INDEX, DEF_COLOR_X_AXIS_RETENTION_INDEX);
		putDefault(P_COLOR_X_AXIS_RETENTION_INDEX_DARKTHEME, DEF_COLOR_X_AXIS_RETENTION_INDEX_DARKTHEME);
		putDefault(P_FONT_NAME_X_AXIS_RETENTION_INDEX, DEF_FONT_NAME_X_AXIS_RETENTION_INDEX);
		putDefault(P_FONT_SIZE_X_AXIS_RETENTION_INDEX, DEF_FONT_SIZE);
		putDefault(P_FONT_STYLE_X_AXIS_RETENTION_INDEX, DEF_FONT_STYLE_X_AXIS_RETENTION_INDEX);
		putDefault(P_GRIDLINE_STYLE_X_AXIS_RETENTION_INDEX, DEF_GRIDLINE_STYLE_X_AXIS_RETENTION_INDEX);
		putDefault(P_GRIDLINE_COLOR_X_AXIS_RETENTION_INDEX, DEF_GRIDLINE_COLOR_X_AXIS_RETENTION_INDEX);
		putDefault(P_SHOW_X_AXIS_TITLE_RETENTION_INDEX, DEF_SHOW_X_AXIS_TITLE_RETENTION_INDEX);
		//
		putDefault(P_TITLE_X_AXIS_SECONDS, DEF_TITLE_X_AXIS_SECONDS);
		putDefault(P_FORMAT_X_AXIS_SECONDS, DEF_FORMAT_X_AXIS_SECONDS);
		putDefault(P_SHOW_X_AXIS_SECONDS, DEF_SHOW_X_AXIS_SECONDS);
		putDefault(P_POSITION_X_AXIS_SECONDS, DEF_POSITION_X_AXIS_SECONDS);
		putDefault(P_COLOR_X_AXIS_SECONDS, DEF_COLOR_X_AXIS_SECONDS);
		putDefault(P_COLOR_X_AXIS_SECONDS_DARKTHEME, DEF_COLOR_X_AXIS_SECONDS_DARKTHEME);
		putDefault(P_FONT_NAME_X_AXIS_SECONDS, DEF_FONT_NAME_X_AXIS_SECONDS);
		putDefault(P_FONT_SIZE_X_AXIS_SECONDS, DEF_FONT_SIZE);
		putDefault(P_FONT_STYLE_X_AXIS_SECONDS, DEF_FONT_STYLE_X_AXIS_SECONDS);
		putDefault(P_GRIDLINE_STYLE_X_AXIS_SECONDS, DEF_GRIDLINE_STYLE_X_AXIS_SECONDS);
		putDefault(P_GRIDLINE_COLOR_X_AXIS_SECONDS, DEF_GRIDLINE_COLOR_X_AXIS_SECONDS);
		putDefault(P_SHOW_X_AXIS_TITLE_SECONDS, DEF_SHOW_X_AXIS_TITLE_SECONDS);
		//
		putDefault(P_TITLE_X_AXIS_MINUTES, DEF_TITLE_X_AXIS_MINUTES);
		putDefault(P_FORMAT_X_AXIS_MINUTES, DEF_FORMAT_X_AXIS_MINUTES);
		putDefault(P_SHOW_X_AXIS_MINUTES, DEF_SHOW_X_AXIS_MINUTES);
		putDefault(P_POSITION_X_AXIS_MINUTES, DEF_POSITION_X_AXIS_MINUTES);
		putDefault(P_COLOR_X_AXIS_MINUTES, DEF_COLOR_X_AXIS_MINUTES);
		putDefault(P_COLOR_X_AXIS_MINUTES_DARKTHEME, DEF_COLOR_X_AXIS_MINUTES_DARKTHEME);
		putDefault(P_FONT_NAME_X_AXIS_MINUTES, DEF_FONT_NAME_X_AXIS_MINUTES);
		putDefault(P_FONT_SIZE_X_AXIS_MINUTES, DEF_FONT_SIZE);
		putDefault(P_FONT_STYLE_X_AXIS_MINUTES, DEF_FONT_STYLE_X_AXIS_MINUTES);
		putDefault(P_GRIDLINE_STYLE_X_AXIS_MINUTES, DEF_GRIDLINE_STYLE_X_AXIS_MINUTES);
		putDefault(P_GRIDLINE_COLOR_X_AXIS_MINUTES, DEF_GRIDLINE_COLOR_X_AXIS_MINUTES);
		putDefault(P_SHOW_X_AXIS_TITLE_MINUTES, DEF_SHOW_X_AXIS_TITLE_MINUTES);
		putDefault(P_SHOW_X_AXIS_LINE_MINUTES, DEF_SHOW_X_AXIS_LINE_MINUTES);
		putDefault(P_SHOW_X_AXIS_POSITION_MARKER_MINUTES, DEF_SHOW_X_AXIS_POSITION_MARKER_MINUTES);
		//
		putDefault(P_TITLE_X_AXIS_SCANS, DEF_TITLE_X_AXIS_SCANS);
		putDefault(P_FORMAT_X_AXIS_SCANS, DEF_FORMAT_X_AXIS_SCANS);
		putDefault(P_SHOW_X_AXIS_SCANS, DEF_SHOW_X_AXIS_SCANS);
		putDefault(P_POSITION_X_AXIS_SCANS, DEF_POSITION_X_AXIS_SCANS);
		putDefault(P_COLOR_X_AXIS_SCANS, DEF_COLOR_X_AXIS_SCANS);
		putDefault(P_COLOR_X_AXIS_SCANS_DARKTHEME, DEF_COLOR_X_AXIS_SCANS_DARKTHEME);
		putDefault(P_FONT_NAME_X_AXIS_SCANS, DEF_FONT_NAME_X_AXIS_SCANS);
		putDefault(P_FONT_SIZE_X_AXIS_SCANS, DEF_FONT_SIZE);
		putDefault(P_FONT_STYLE_X_AXIS_SCANS, DEF_FONT_STYLE_X_AXIS_SCANS);
		putDefault(P_GRIDLINE_STYLE_X_AXIS_SCANS, DEF_GRIDLINE_STYLE_X_AXIS_SCANS);
		putDefault(P_GRIDLINE_COLOR_X_AXIS_SCANS, DEF_GRIDLINE_COLOR_X_AXIS_SCANS);
		putDefault(P_SHOW_X_AXIS_TITLE_SCANS, DEF_SHOW_X_AXIS_TITLE_SCANS);
		//
		putDefault(P_TITLE_Y_AXIS_INTENSITY, DEF_TITLE_Y_AXIS_INTENSITY);
		putDefault(P_FORMAT_Y_AXIS_INTENSITY, DEF_FORMAT_Y_AXIS_INTENSITY);
		putDefault(P_SHOW_Y_AXIS_INTENSITY, DEF_SHOW_Y_AXIS_INTENSITY);
		putDefault(P_POSITION_Y_AXIS_INTENSITY, DEF_POSITION_Y_AXIS_INTENSITY);
		putDefault(P_COLOR_Y_AXIS_INTENSITY, DEF_COLOR_Y_AXIS_INTENSITY);
		putDefault(P_COLOR_Y_AXIS_INTENSITY_DARKTHEME, DEF_COLOR_Y_AXIS_INTENSITY_DARKTHEME);
		putDefault(P_FONT_NAME_Y_AXIS_INTENSITY, DEF_FONT_NAME_Y_AXIS_INTENSITY);
		putDefault(P_FONT_SIZE_Y_AXIS_INTENSITY, DEF_FONT_SIZE);
		putDefault(P_FONT_STYLE_Y_AXIS_INTENSITY, DEF_FONT_STYLE_Y_AXIS_INTENSITY);
		putDefault(P_GRIDLINE_STYLE_Y_AXIS_INTENSITY, DEF_GRIDLINE_STYLE_Y_AXIS_INTENSITY);
		putDefault(P_GRIDLINE_COLOR_Y_AXIS_INTENSITY, DEF_GRIDLINE_COLOR_Y_AXIS_INTENSITY);
		putDefault(P_SHOW_Y_AXIS_TITLE_INTENSITY, DEF_SHOW_Y_AXIS_TITLE_INTENSITY);
		//
		putDefault(P_TITLE_Y_AXIS_RELATIVE_INTENSITY, DEF_TITLE_Y_AXIS_RELATIVE_INTENSITY);
		putDefault(P_FORMAT_Y_AXIS_RELATIVE_INTENSITY, DEF_FORMAT_Y_AXIS_RELATIVE_INTENSITY);
		putDefault(P_SHOW_Y_AXIS_RELATIVE_INTENSITY, DEF_SHOW_Y_AXIS_RELATIVE_INTENSITY);
		putDefault(P_POSITION_Y_AXIS_RELATIVE_INTENSITY, DEF_POSITION_Y_AXIS_RELATIVE_INTENSITY);
		putDefault(P_COLOR_Y_AXIS_RELATIVE_INTENSITY, DEF_COLOR_Y_AXIS_RELATIVE_INTENSITY);
		putDefault(P_COLOR_Y_AXIS_RELATIVE_INTENSITY_DARKTHEME, DEF_COLOR_Y_AXIS_RELATIVE_INTENSITY_DARKTHEME);
		putDefault(P_FONT_NAME_Y_AXIS_RELATIVE_INTENSITY, DEF_FONT_NAME_Y_AXIS_RELATIVE_INTENSITY);
		putDefault(P_FONT_SIZE_Y_AXIS_RELATIVE_INTENSITY, DEF_FONT_SIZE);
		putDefault(P_FONT_STYLE_Y_AXIS_RELATIVE_INTENSITY, DEF_FONT_STYLE_Y_AXIS_RELATIVE_INTENSITY);
		putDefault(P_GRIDLINE_STYLE_Y_AXIS_RELATIVE_INTENSITY, DEF_GRIDLINE_STYLE_Y_AXIS_RELATIVE_INTENSITY);
		putDefault(P_GRIDLINE_COLOR_Y_AXIS_RELATIVE_INTENSITY, DEF_GRIDLINE_COLOR_Y_AXIS_RELATIVE_INTENSITY);
		putDefault(P_SHOW_Y_AXIS_TITLE_RELATIVE_INTENSITY, DEF_SHOW_Y_AXIS_TITLE_RELATIVE_INTENSITY);
		//
		putDefault(P_CHROMATOGRAM_SELECTED_ACTION_ID, DEF_CHROMATOGRAM_SELECTED_ACTION_ID);
		putDefault(P_CHROMATOGRAM_SAVE_AS_FOLDER, DEF_CHROMATOGRAM_SAVE_AS_FOLDER);
		putDefault(P_CHROMATOGRAM_LOAD_PROCESS_METHOD, DEF_CHROMATOGRAM_LOAD_PROCESS_METHOD);
		putDefault(P_DELTA_MILLISECONDS_PEAK_SELECTION, DEF_DELTA_MILLISECONDS_PEAK_SELECTION);
		putDefault(P_CHROMATOGRAM_MARK_ANALYSIS_SEGMENTS, DEF_CHROMATOGRAM_MARK_ANALYSIS_SEGMENTS);
		putDefault(P_SHOW_RESUME_METHOD_DIALOG, DEF_SHOW_RESUME_METHOD_DIALOG);
		putDefault(P_SHOW_RETENTION_INDEX_MARKER, DEF_SHOW_RETENTION_INDEX_MARKER);
		/*
		 * Time Ranges
		 */
		putDefault(P_TIME_RANGE_TEMPLATE_FOLDER, DEF_TIME_RANGE_TEMPLATE_FOLDER);
		putDefault(P_TIME_RANGE_SELECTION_OFFSET, DEF_TIME_RANGE_SELECTION_OFFSET);
		/*
		 * Named Traces
		 */
		putDefault(P_NAMED_TRACES_TEMPLATE_FOLDER, DEF_NAMED_TRACES_TEMPLATE_FOLDER);
		/*
		 * Target Templates
		 */
		putDefault(P_TARGET_TEMPLATES_FOLDER, DEF_TARGET_TEMPLATES_FOLDER);
		/*
		 * Instruments
		 */
		putDefault(P_INSTRUMENTS_TEMPLATE_FOLDER, DEF_INSTRUMENTS_TEMPLATE_FOLDER);
		/*
		 * Processor
		 */
		putDefault(P_PROCESSOR_SELECTION_DATA_CATEGORY, DEF_PROCESSOR_SELECTION_DATA_CATEGORY);
		/*
		 * Editors
		 */
		putDefault(P_EDITOR_TSD, DEF_EDITOR_TSD);
		/*
		 * Quick-Access Processors
		 */
		for(DataCategory dataCategory : DataCategory.values()) {
			putDefault(P_QUICK_ACCESS_PROCESSORS + dataCategory.name(), DEF_QUICK_ACCESS_PROCESSORS);
		}
	}

	private void initializeOverlayDefaults() {

		putDefault(P_OVERLAY_CHART_COMPRESSION_TYPE, DEF_OVERLAY_CHART_COMPRESSION_TYPE);
		putDefault(P_SHOW_REFERENCED_CHROMATOGRAMS, DEF_SHOW_REFERENCED_CHROMATOGRAMS);
		putDefault(P_OVERLAY_ADD_TYPE_INFO, DEF_OVERLAY_ADD_TYPE_INFO);
		putDefault(P_COLOR_SCHEME_DISPLAY_OVERLAY, DEF_COLOR_SCHEME_DISPLAY_OVERLAY);
		putDefault(P_LINE_STYLE_DISPLAY_OVERLAY, DEF_LINE_STYLE_DISPLAY_OVERLAY);
		putDefault(P_SHOW_OPTIMIZED_CHROMATOGRAM_XWC, DEF_SHOW_OPTIMIZED_CHROMATOGRAM_XWC);
		putDefault(P_MODULO_AUTO_MIRROR_CHROMATOGRAMS, DEF_MODULO_AUTO_MIRROR_CHROMATOGRAMS);
		//
		putDefault(P_CHROMATOGRAM_OVERLAY_NAMED_TRACES, DEF_CHROMATOGRAM_OVERLAY_NAMED_TRACES);
		//
		putDefault(P_OVERLAY_SHIFT_X, DEF_OVERLAY_SHIFT_X);
		putDefault(P_INDEX_SHIFT_X, DEF_INDEX_SHIFT_X);
		putDefault(P_OVERLAY_SHIFT_Y, DEF_OVERLAY_SHIFT_Y);
		putDefault(P_INDEX_SHIFT_Y, DEF_INDEX_SHIFT_Y);
		//
		putDefault(P_OVERLAY_SHOW_AREA, DEF_OVERLAY_SHOW_AREA);
		putDefault(P_OVERLAY_AUTOFOCUS_PROFILE_SETTINGS, DEF_OVERLAY_AUTOFOCUS_PROFILE_SETTINGS);
		putDefault(P_OVERLAY_AUTOFOCUS_SHIFT_SETTINGS, DEF_OVERLAY_AUTOFOCUS_SHIFT_SETTINGS);
		putDefault(P_OVERLAY_LOCK_ZOOM, DEF_OVERLAY_LOCK_ZOOM);
		putDefault(P_OVERLAY_FOCUS_SELECTION, DEF_OVERLAY_FOCUS_SELECTION);
	}

	public static boolean isActivateTargetDragAndDropWindows() {

		return INSTANCE().getBoolean(P_ACTIVATE_TARGET_DND_WINDOWS, DEF_ACTIVATE_TARGET_DND_WINDOWS);
	}

	public static boolean isCreateMethodEnableResume() {

		return INSTANCE().getBoolean(P_CREATE_METHOD_ENABLE_RESUME, DEF_CREATE_METHOD_ENABLE_RESUME);
	}

	public static String getFilterPathRetentionIndices() {

		return INSTANCE().get(P_FILTER_PATH_RETENTION_INDICES);
	}

	public static void setFilterPathRetentionIndices(String path) {

		INSTANCE().put(P_FILTER_PATH_RETENTION_INDICES, path);
	}

	public static String getMoleculeImageServiceId() {

		return INSTANCE().get(P_MOLECULE_IMAGE_SERVICE_ID);
	}

	public static void setMoleculeImageServiceId(String selection) {

		INSTANCE().put(P_MOLECULE_IMAGE_SERVICE_ID, selection);
	}

	public static boolean isHideProcessMethodEntries() {

		return INSTANCE().getBoolean(P_EDIT_HISTORY_HIDE_PROCESS_METHOD_ENTRIES, DEF_EDIT_HISTORY_HIDE_PROCESS_METHOD_ENTRIES);
	}

	public static boolean isHeaderDataUseRichTextEditor() {

		return INSTANCE().getBoolean(P_HEADER_DATA_USE_RICH_TEXT_EDITOR, DEF_HEADER_DATA_USE_RICH_TEXT_EDITOR);
	}

	public static HeaderField getChromatogramEditorLabel() {

		try {
			return HeaderField.valueOf(INSTANCE().get(P_CHROMATOGRAM_EDITOR_LABEL, DEF_CHROMATOGRAM_EDITOR_LABEL));
		} catch(Exception e) {
			return HeaderField.NAME;
		}
	}

	public static void setChromatogramEditorLabel(HeaderField headerField) {

		INSTANCE().put(P_CHROMATOGRAM_EDITOR_LABEL, headerField.name());
	}

	public static String getListPathImport() {

		return INSTANCE().get(P_LIST_PATH_IMPORT, DEF_LIST_PATH_IMPORT);
	}

	public static void setListPathImport(String filterPath) {

		INSTANCE().put(P_LIST_PATH_IMPORT, filterPath);
	}

	public static String getListPathExport() {

		return INSTANCE().get(P_LIST_PATH_EXPORT, DEF_LIST_PATH_EXPORT);
	}

	public static void setListPathExport(String filterPath) {

		INSTANCE().put(P_LIST_PATH_EXPORT, filterPath);
	}
}