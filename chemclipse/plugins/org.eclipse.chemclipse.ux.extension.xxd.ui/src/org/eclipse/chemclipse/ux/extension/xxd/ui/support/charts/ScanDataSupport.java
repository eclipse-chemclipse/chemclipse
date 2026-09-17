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
 * Lorenz Gerber - fix integer overflow for signal label
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.model.types.SignalType;
import org.eclipse.chemclipse.msd.model.core.ILibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.ux.extension.xxd.ui.charts.ChartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.swtchart.extensions.core.IChartSettings;

public class ScanDataSupport {

	public static final String[] DATA_TYPES_DEFAULT = new String[]{DataType.AUTO_DETECT.toString()};
	public static final String[] DATA_TYPES_MSD = new String[]{DataType.AUTO_DETECT.toString(), DataType.MSD_NOMINAL.toString(), DataType.MSD_TANDEM.toString(), DataType.MSD_HIGHRES.toString()};
	public static final String[] DATA_TYPES_CSD = new String[]{DataType.AUTO_DETECT.toString(), DataType.CSD.toString()};
	public static final String[] DATA_TYPES_WSD = new String[]{DataType.AUTO_DETECT.toString(), DataType.WSD.toString()};
	public static final String[] DATA_TYPES_VSD = new String[]{DataType.AUTO_DETECT.toString(), DataType.VSD.toString()};

	public static final String[] SIGNAL_TYPES_DEFAULT = new String[]{SignalType.AUTO_DETECT.toString()};
	public static final String[] SIGNAL_TYPES_MSD = new String[]{SignalType.AUTO_DETECT.toString(), SignalType.CENTROID.toString(), SignalType.PROFILE.toString()};
	public static final String[] SIGNAL_TYPES_CSD = new String[]{SignalType.AUTO_DETECT.toString(), SignalType.CENTROID.toString()};
	public static final String[] SIGNAL_TYPES_WSD = new String[]{SignalType.AUTO_DETECT.toString(), SignalType.CENTROID.toString(), SignalType.PROFILE.toString()};
	public static final String[] SIGNAL_TYPES_VSD = new String[]{SignalType.AUTO_DETECT.toString(), SignalType.CENTROID.toString(), SignalType.PROFILE.toString()};

	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.0##");

	public String getRetentionTime(IScan scan) {

		if(scan != null) {
			return decimalFormat.format(scan.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
		} else {
			return "";
		}
	}

	public String getScanLabel(IScan scan) {

		StringBuilder builder = new StringBuilder();
		if(scan != null) {
			if(scan instanceof IPeakMassSpectrum) {
				builder.append("Peak Scan");
			} else if(scan instanceof ILibraryMassSpectrum) {
				builder.append("Library Spectrum");
			} else {
				builder.append("Scan: ");
				builder.append(scan.getScanNumber());
			}

			if(!(scan instanceof ILibraryMassSpectrum)) {
				builder.append(" | ");
				builder.append("RT: ");
				builder.append(decimalFormat.format(scan.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
				builder.append(" | ");
				builder.append("RI: ");
				if(org.eclipse.chemclipse.model.preferences.PreferenceSupplier.showRetentionIndexWithoutDecimals()) {
					builder.append(Integer.toString((int)scan.getRetentionIndex()));
				} else {
					builder.append(decimalFormat.format(scan.getRetentionIndex()));
				}
			}

			if(scan instanceof IRegularMassSpectrum massSpectrum) {
				builder.append(" | ");
				builder.append("Detector: MS");
				builder.append(massSpectrum.getMassSpectrometer());
				builder.append(" | ");
				builder.append("Type: ");
				builder.append(massSpectrum.getMassSpectrumType().label());
				builder.append(" | ");
				if(massSpectrum.getPolarity() != Polarity.NONE) {
					builder.append("Polarity: ");
					builder.append(massSpectrum.getPolarity().label());
				}
			}

			if(!(scan instanceof ILibraryMassSpectrum)) {
				builder.append(" | ");
				builder.append("Signal: ");
				builder.append(BigDecimal.valueOf(scan.getTotalSignal()).toBigInteger());
			}

			if(scan instanceof IScanMSD scanMSD) {
				IScanMSD optimizedMassSpectrum = scanMSD.getOptimizedMassSpectrum();
				if(optimizedMassSpectrum != null) {
					builder.append(" | ");
					builder.append("optimized");
				}
			}
		} else {
			builder.append("No scan has been selected yet.");
		}
		return builder.toString();
	}

	public String getMassSpectrumLabel(IScanMSD scanMSD, String prefix, String title, String postfix) {

		StringBuilder builder = new StringBuilder();
		builder.append(prefix);
		builder.append(" ");
		builder.append(title);
		builder.append(" = ");

		if(scanMSD != null) {
			if(scanMSD instanceof IRegularLibraryMassSpectrum libraryMassSpectrum) {
				ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
				builder.append("NAME: ");
				builder.append(libraryInformation.getName());
				builder.append(" | ");
				builder.append("CAS: ");
				builder.append(libraryInformation.getCasNumber());
				builder.append(" | ");
				builder.append("MW: ");
				builder.append(libraryInformation.getMolWeight());
				builder.append(" | ");
			}
			builder.append("RT: ");
			builder.append(decimalFormat.format(scanMSD.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
			builder.append(" | ");
			builder.append("RI: ");
			if(org.eclipse.chemclipse.model.preferences.PreferenceSupplier.showRetentionIndexWithoutDecimals()) {
				builder.append(Integer.toString((int)scanMSD.getRetentionIndex()));
			} else {
				builder.append(decimalFormat.format(scanMSD.getRetentionIndex()));
			}

			IScanMSD optimizedMassSpectrum = scanMSD.getOptimizedMassSpectrum();
			if(optimizedMassSpectrum != null) {
				builder.append(" | ");
				builder.append("optimized");
			}

			if(!"".equals(postfix)) {
				builder.append(" | ");
				builder.append(postfix);
				builder.append("*");
			}
		} else {
			builder.append("No mass spectrum has been selected yet.");
		}

		return builder.toString();
	}

	public boolean containsOptimizedScan(IScan scan) {

		boolean containsOptimizedScan = false;

		if(scan instanceof IScanMSD scanMSD) {
			IScanMSD optimizedMassSpectrum = scanMSD.getOptimizedMassSpectrum();
			if(optimizedMassSpectrum != null) {
				containsOptimizedScan = true;
			}
		}

		return containsOptimizedScan;
	}

	public void setDataTypeMSD(IChartSettings chartSettings) {

		String titleX = ExtensionMessages.ion;
		String titleY = ExtensionMessages.intensity;
		String titleY1 = ExtensionMessages.relativeIntensity;

		ChartSupport.setPrimaryAxisSet(chartSettings, titleX, true, titleY);
		ChartSupport.clearSecondaryAxes(chartSettings);
		ChartSupport.addSecondaryAxisY(chartSettings, titleY1);
	}

	public void setDataTypeCSD(IChartSettings chartSettings) {

		String titleX = ExtensionMessages.miliseconds;
		String titleX1 = ExtensionMessages.minutes;
		String titleY = ExtensionMessages.intensity;
		String titleY1 = ExtensionMessages.relativeIntensity;

		ChartSupport.setPrimaryAxisSet(chartSettings, titleX, false, titleY);
		ChartSupport.clearSecondaryAxes(chartSettings);
		ChartSupport.addSecondaryAxisX(chartSettings, titleX1);
		ChartSupport.addSecondaryAxisY(chartSettings, titleY1);
	}

	public void setDataTypeWSD(IChartSettings chartSettings) {

		String titleX = ExtensionMessages.wavelength;
		String titleY = ExtensionMessages.intensity;
		String titleY1 = ExtensionMessages.relativeIntensity;

		ChartSupport.setPrimaryAxisSet(chartSettings, titleX, true, titleY);
		ChartSupport.clearSecondaryAxes(chartSettings);
		ChartSupport.addSecondaryAxisY(chartSettings, titleY1);
	}

	public void setDataTypeVSD(IChartSettings chartSettings) {

		String titleX = ExtensionMessages.wavenumber;
		String titleY = ExtensionMessages.intensity;
		String titleY1 = ExtensionMessages.relativeIntensity;

		ChartSupport.setPrimaryAxisSet(chartSettings, titleX, true, titleY);
		ChartSupport.clearSecondaryAxes(chartSettings);
		ChartSupport.addSecondaryAxisY(chartSettings, titleY1);
	}
}
