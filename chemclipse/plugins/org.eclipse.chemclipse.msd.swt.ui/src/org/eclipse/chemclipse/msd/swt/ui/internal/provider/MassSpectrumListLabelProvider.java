/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.swt.ui.internal.provider;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Set;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.model.core.DuplicateDetection;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class MassSpectrumListLabelProvider extends AbstractChemClipseLabelProvider {

	private DuplicateDetection duplicateDetection = DuplicateDetection.NONE;
	private Set<String> duplicateGroups = Collections.emptySet();

	public void updateDuplicateHints(DuplicateDetection duplicateDetection, Set<String> duplicateGroups) {

		this.duplicateDetection = duplicateDetection;
		this.duplicateGroups = duplicateGroups != null ? duplicateGroups : Collections.emptySet();
	}

	@Override
	public Color getBackground(Object element) {

		if(element instanceof IRegularLibraryMassSpectrum scan && isDuplicate(scan)) {
			return Colors.LIGHT_YELLOW;
		}
		return super.getBackground(element);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			if(element instanceof IRegularLibraryMassSpectrum scan && isDuplicate(scan)) {
				return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_STATUS_WARN, IApplicationImageProvider.SIZE_16x16);
			}
			return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_STATUS_OK, IApplicationImageProvider.SIZE_16x16);
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		if(element instanceof IRegularLibraryMassSpectrum libraryMassSpectrum) {
			/*
			 * Library Entry
			 */
			ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
			return getText(libraryMassSpectrum, libraryInformation, columnIndex);
		} else if(element instanceof IScanMSD massSpectrum) {
			/*
			 * Scan
			 */
			ILibraryInformation libraryInformation = IIdentificationTarget.getLibraryInformation(massSpectrum);
			if(massSpectrum.getOptimizedMassSpectrum() != null) {
				massSpectrum = massSpectrum.getOptimizedMassSpectrum();
			}
			return getText(massSpectrum, libraryInformation, columnIndex);
		} else {
			return "n.a.";
		}
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_MASS_SPECTRUM, IApplicationImageProvider.SIZE_16x16);
	}

	private String getText(IScanMSD massSpectrum, ILibraryInformation libraryInformation, int columnIndex) {

		DecimalFormat decimalFormat = getDecimalFormat();
		String text = "";
		switch(columnIndex) {
			case 0: // Name
				if(libraryInformation != null) {
					text = libraryInformation.getName();
				}
				break;
			case 1: // RT
				if(massSpectrum.getRetentionTime() == 0) {
					text = "0";
				} else {
					text = decimalFormat.format(massSpectrum.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
				}
				break;
			case 2: // RI
				int retentionIndexNoPrecision = (int)massSpectrum.getRetentionIndex();
				if(retentionIndexNoPrecision == massSpectrum.getRetentionIndex()) {
					text = Integer.toString(retentionIndexNoPrecision);
				} else {
					if(PreferenceSupplier.showRetentionIndexWithoutDecimals()) {
						text = Integer.toString(retentionIndexNoPrecision);
					} else {
						text = decimalFormat.format(massSpectrum.getRetentionIndex());
					}
				}
				break;
			case 3: // Base Peak
				int basePeakNoPrecision = (int)massSpectrum.getBasePeak();
				if(basePeakNoPrecision == massSpectrum.getBasePeak()) {
					text = Integer.toString(basePeakNoPrecision);
				} else {
					text = decimalFormat.format(massSpectrum.getBasePeak());
				}
				break;
			case 4: // Base Peak Abundance
				int basePeakAbundanceNoPrecision = (int)massSpectrum.getBasePeakAbundance();
				if(basePeakAbundanceNoPrecision == massSpectrum.getBasePeakAbundance()) {
					text = Integer.toString(basePeakAbundanceNoPrecision);
				} else {
					text = decimalFormat.format(massSpectrum.getBasePeakAbundance());
				}
				break;
			case 5: // Number of Ions
				text = Integer.toString(massSpectrum.getNumberOfIons());
				break;
			case 6: // CAS
				if(libraryInformation != null) {
					text = libraryInformation.getCasNumber();
				}
				break;
			case 7: // MW
				if(libraryInformation != null) {
					int molWeightNoPrecision = (int)libraryInformation.getMolWeight();
					if(molWeightNoPrecision == libraryInformation.getMolWeight()) {
						text = Integer.toString(molWeightNoPrecision);
					} else {
						text = decimalFormat.format(libraryInformation.getMolWeight());
					}
				}
				break;
			case 8: // Formula
				if(libraryInformation != null) {
					text = libraryInformation.getFormula();
				}
				break;
			case 9:
				if(libraryInformation != null) {
					text = libraryInformation.getSmiles();
				}
				break;
			case 10:
				if(libraryInformation != null) {
					text = libraryInformation.getInChI();
				}
				break;
			case 11: // Reference Identifier
				if(libraryInformation != null) {
					text = libraryInformation.getReferenceIdentifier();
				}
				break;
			case 12:
				if(libraryInformation != null) {
					text = libraryInformation.getComments();
				}
				break;
			default:
				text = "n.v.";
		}
		return text;
	}

	private boolean isDuplicate(IRegularLibraryMassSpectrum scanMSD) {

		if(duplicateGroups.isEmpty()) {
			return false;
		}

		switch(duplicateDetection) {
			case NAME:
				return duplicateGroups.contains(scanMSD.getLibraryInformation().getName());
			case CAS:
				return duplicateGroups.contains(scanMSD.getLibraryInformation().getCasNumber());
			case FORMULA:
				return duplicateGroups.contains(scanMSD.getLibraryInformation().getFormula());
			case SMILES:
				return duplicateGroups.contains(scanMSD.getLibraryInformation().getSmiles());
			case INCHI:
				return duplicateGroups.contains(scanMSD.getLibraryInformation().getInChI());
			case REFERENCE_IDENTIFIER:
				return duplicateGroups.contains(scanMSD.getLibraryInformation().getReferenceIdentifier());
			default:
				return false;
		}
	}
}