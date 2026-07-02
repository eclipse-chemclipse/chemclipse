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
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import java.text.DecimalFormat;

import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.fsd.model.core.IScanSignalFSD;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.vsd.model.core.ISignalVSD;
import org.eclipse.chemclipse.wsd.model.core.IScanSignalWSD;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class ScanLabelProvider extends ColumnLabelProvider implements ITableLabelProvider {

	public static final String NO_VALUE = "n.a.";

	public static final String INTENSITY = ExtensionMessages.intensity;
	public static final String RELATIVE_INTENSITY = ExtensionMessages.relativeIntensity;
	public static final String ION = ExtensionMessages.ion;
	public static final String WAVELENGTH = ExtensionMessages.wavelength;
	public static final String MINUTES = ExtensionMessages.minutes;
	public static final String PARENT_ION = ExtensionMessages.parentIon;
	public static final String PARENT_RESOLUTION = ExtensionMessages.parentResolution;
	public static final String DAUGHTER_ION = ExtensionMessages.daughterIon;
	public static final String DAUGHTER_RESOLUTION = ExtensionMessages.daughterResolution;
	public static final String COLLISION_ENERGY = ExtensionMessages.collisionEnergy;
	public static final String WAVENUMBER = ExtensionMessages.wavenumber;

	public static final String[] TITLES_MSD_NOMINAL = {ION, INTENSITY, RELATIVE_INTENSITY};
	public static final int[] BOUNDS_MSD_NOMINAL = {150, 150, 150};
	public static final String[] TITLES_MSD_TANDEM = {ION, INTENSITY, RELATIVE_INTENSITY, PARENT_ION, PARENT_RESOLUTION, DAUGHTER_ION, DAUGHTER_RESOLUTION, COLLISION_ENERGY};
	public static final int[] BOUNDS_MSD_TANDEM = {100, 100, 100, 120, 120, 120, 120, 120};
	public static final String[] TITLES_MSD_HIGHRES = {ION, INTENSITY, RELATIVE_INTENSITY};
	public static final int[] BOUNDS_MSD_HIGHRES = {150, 150, 150};
	public static final String[] TITLES_CSD = {MINUTES, INTENSITY, RELATIVE_INTENSITY};
	public static final int[] BOUNDS_CSD = {150, 150, 150};
	public static final String[] TITLES_WSD = {WAVELENGTH, INTENSITY, RELATIVE_INTENSITY};
	public static final int[] BOUNDS_WSD = {150, 150, 150};
	public static final String[] TITLES_VSD = {WAVENUMBER, INTENSITY, RELATIVE_INTENSITY};
	public static final int[] BOUNDS_VSD = {150, 150, 150};
	public static final String[] TITLES_FSD = {WAVELENGTH, INTENSITY, RELATIVE_INTENSITY};
	public static final int[] BOUNDS_FSD = {150, 150, 150};
	public static final String[] TITLES_EMPTY = {NO_VALUE};
	public static final int[] BOUNDS_EMPTY = {150};

	private DataType dataType;

	private DecimalFormat decimalFormatNominalMSD;
	private DecimalFormat decimalFormatPrecursorMSD;
	private DecimalFormat decimalFormatTandemMSD;
	private DecimalFormat decimalFormatHighResMSD;
	private DecimalFormat decimalFormatCSD;
	private DecimalFormat decimalFormatWSD;
	private DecimalFormat decimalFormatFSD;

	private DecimalFormat decimalFormatIntensity;
	private DecimalFormat decimalFormatRelativeIntensity;

	private double relativeIntensityFactorPositive = 0.0d;
	private double relativeIntensityFactorNegative = 0.0d;

	public ScanLabelProvider(DataType dataType) {

		this.dataType = dataType;
		decimalFormatNominalMSD = ValueFormat.getDecimalFormatEnglish("0.0");
		decimalFormatPrecursorMSD = ValueFormat.getDecimalFormatEnglish("0.######"); // Transition 128 > 78.4 but can also be high-res
		decimalFormatTandemMSD = ValueFormat.getDecimalFormatEnglish("0.0#####"); // m/z: normal 28.3 or with but can also be high-res
		decimalFormatHighResMSD = ValueFormat.getDecimalFormatEnglish("0.000###");
		decimalFormatCSD = ValueFormat.getDecimalFormatEnglish("0.0000");
		decimalFormatWSD = ValueFormat.getDecimalFormatEnglish("0.0");
		decimalFormatFSD = ValueFormat.getDecimalFormatEnglish("0.0");
		decimalFormatIntensity = ValueFormat.getDecimalFormatEnglish("0.0###");
		decimalFormatRelativeIntensity = ValueFormat.getDecimalFormatEnglish("0.0000");
	}

	public void setTotalIntensity(double minIntensity, double maxIntensity) {

		relativeIntensityFactorNegative = (minIntensity < 0) ? 100.0d / minIntensity : 0.0d;
		relativeIntensityFactorPositive = (maxIntensity > 0) ? 100.0d / maxIntensity : 0.0d;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ION, IApplicationImageProvider.SIZE_16x16);
		} else {
			return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		switch(dataType) {
			case MSD_NOMINAL:
				text = getNominalMSD(element, columnIndex);
				break;
			case MSD_TANDEM:
				text = getTandemMSD(element, columnIndex);
				break;
			case MSD_HIGHRES:
				text = getHighResolutionMSD(element, columnIndex);
				break;
			case CSD:
				text = getCSD(element, columnIndex);
				break;
			case WSD:
				text = getWSD(element, columnIndex);
				break;
			case VSD:
				text = getVSD(element, columnIndex);
				break;
			case FSD:
				text = getFSD(element, columnIndex);
				break;
			default:
				text = NO_VALUE;
		}
		return text;
	}

	private String getNominalMSD(Object element, int columnIndex) {

		String text = "";
		if(element instanceof IIon ion) {
			switch(columnIndex) {
				case 0:
					text = decimalFormatNominalMSD.format(ion.getIon());
					break;
				case 1:
					text = decimalFormatIntensity.format(ion.getAbundance());
					break;
				case 2:
					text = decimalFormatRelativeIntensity.format(relativeIntensityFactorPositive * ion.getAbundance());
					break;
				default:
					text = NO_VALUE;
			}
		}
		return text;
	}

	private String getTandemMSD(Object element, int columnIndex) {

		String text = "";
		if(element instanceof IIonMSn ion) {
			IIonTransition ionTransition = ion.getIonTransition();
			switch(columnIndex) {
				case 0:
					String mz = decimalFormatTandemMSD.format(ion.getIon());
					if(ionTransition != null) {
						String q1 = decimalFormatPrecursorMSD.format(ionTransition.getQ1StartIon());
						text = q1 + " > " + mz;
					} else {
						text = mz;
					}
					break;
				case 1:
					text = decimalFormatIntensity.format(ion.getAbundance());
					break;
				case 2:
					text = decimalFormatRelativeIntensity.format(relativeIntensityFactorPositive * ion.getAbundance());
					break;
				case 3: // parent m/z
					text = (ionTransition == null) ? "" : decimalFormatTandemMSD.format(ionTransition.getQ1Ion());
					break;
				case 4: // parent resolution
					text = (ionTransition == null) ? "" : decimalFormatTandemMSD.format(ionTransition.getQ1Resolution());
					break;
				case 5: // daughter m/z
					text = (ionTransition == null) ? "" : decimalFormatTandemMSD.format(ionTransition.getQ3Ion());
					break;
				case 6: // daughter resolution
					text = (ionTransition == null) ? "" : decimalFormatTandemMSD.format(ionTransition.getQ3Resolution());
					break;
				case 7: // collision energy
					text = (ionTransition == null) ? "" : decimalFormatTandemMSD.format(ionTransition.getCollisionEnergy());
					break;
				default:
					text = NO_VALUE;
			}
		}
		return text;
	}

	private String getHighResolutionMSD(Object element, int columnIndex) {

		String text = "";
		if(element instanceof IIon ion) {
			switch(columnIndex) {
				case 0:
					text = decimalFormatHighResMSD.format(ion.getIon());
					break;
				case 1:
					text = decimalFormatIntensity.format(ion.getAbundance());
					break;
				case 2:
					text = decimalFormatRelativeIntensity.format(relativeIntensityFactorPositive * ion.getAbundance());
					break;
				default:
					text = NO_VALUE;
			}
		}
		return text;
	}

	private String getCSD(Object element, int columnIndex) {

		String text = "";
		if(element instanceof IScanCSD scanCSD) {
			switch(columnIndex) {
				case 0:
					text = decimalFormatCSD.format(scanCSD.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
					break;
				case 1:
					text = decimalFormatIntensity.format(scanCSD.getTotalSignal());
					break;
				case 2:
					float signal = scanCSD.getTotalSignal();
					if(signal < 0) {
						text = "-" + decimalFormatRelativeIntensity.format(relativeIntensityFactorNegative * signal);
					} else {
						text = decimalFormatRelativeIntensity.format(relativeIntensityFactorPositive * signal);
					}
					break;
				default:
					text = NO_VALUE;
			}
		}
		return text;
	}

	private String getWSD(Object element, int columnIndex) {

		String text = "";
		if(element instanceof IScanSignalWSD scanSignal) {
			switch(columnIndex) {
				case 0:
					text = decimalFormatWSD.format(scanSignal.getWavelength());
					break;
				case 1:
					text = decimalFormatIntensity.format(scanSignal.getAbsorbance());
					break;
				case 2:
					float signal = scanSignal.getAbsorbance();
					if(signal < 0) {
						text = "-" + decimalFormatRelativeIntensity.format(relativeIntensityFactorNegative * signal);
					} else {
						text = decimalFormatRelativeIntensity.format(relativeIntensityFactorPositive * signal);
					}
					break;
				default:
					text = NO_VALUE;
			}
		}
		return text;
	}

	private String getVSD(Object element, int columnIndex) {

		String text = "";
		if(element instanceof ISignalVSD scanSignal) {
			switch(columnIndex) {
				case 0:
					text = Double.toString(scanSignal.getWavenumber());
					break;
				case 1:
					text = decimalFormatIntensity.format(scanSignal.getIntensity());
					break;
				case 2:
					double signal = scanSignal.getIntensity();
					if(signal < 0) {
						text = "-" + decimalFormatRelativeIntensity.format(relativeIntensityFactorNegative * signal);
					} else {
						text = decimalFormatRelativeIntensity.format(relativeIntensityFactorPositive * signal);
					}
					break;
				default:
					text = NO_VALUE;
			}
		}
		return text;
	}

	private String getFSD(Object element, int columnIndex) {

		String text = "";
		if(element instanceof IScanSignalFSD scanSignal) {
			switch(columnIndex) {
				case 0:
					text = decimalFormatFSD.format(scanSignal.getWavelength());
					break;
				case 1:
					text = decimalFormatIntensity.format(scanSignal.getFluorescence());
					break;
				case 2:
					float signal = scanSignal.getFluorescence();
					if(signal < 0) {
						text = "-" + decimalFormatRelativeIntensity.format(relativeIntensityFactorNegative * signal);
					} else {
						text = decimalFormatRelativeIntensity.format(relativeIntensityFactorPositive * signal);
					}
					break;
				default:
					text = NO_VALUE;
			}
		}
		return text;
	}
}
