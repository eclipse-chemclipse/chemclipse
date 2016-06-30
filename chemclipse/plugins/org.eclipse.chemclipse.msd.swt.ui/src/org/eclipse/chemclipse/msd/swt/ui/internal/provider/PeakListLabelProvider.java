/*******************************************************************************
 * Copyright (c) 2008, 2016 Philip (eselmeister) Wenig.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip (eselmeister) Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.swt.ui.internal.provider;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.model.comparator.SortOrder;
import org.eclipse.chemclipse.model.comparator.TargetExtendedComparator;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.targets.IPeakTarget;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.swt.ui.preferences.PreferenceSupplier;
import org.eclipse.swt.graphics.Image;

public class PeakListLabelProvider extends AbstractChemClipseLabelProvider {

	private TargetExtendedComparator targetExtendedComparator;

	public PeakListLabelProvider() {
		targetExtendedComparator = new TargetExtendedComparator(SortOrder.DESC);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			if(element instanceof IPeak) {
				IPeak peak = (IPeak)element;
				if(peak.isActiveForAnalysis()) {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SELECTED, IApplicationImage.SIZE_16x16);
				} else {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DESELECTED, IApplicationImage.SIZE_16x16);
				}
			}
		} else if(columnIndex == 1) {
			return getImage(element);
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		/*
		 * SYNCHRONIZE: PeakListLabelProvider PeakListLabelComparator PeakListView
		 */
		DecimalFormat decimalFormat = getDecimalFormat();
		String text = "";
		if(element instanceof IPeakMSD) {
			IPeakMSD peak = (IPeakMSD)element;
			IPeakModelMSD peakModel = peak.getPeakModel();
			ILibraryInformation libraryInformation = getLibraryInformation(peak.getTargets());
			//
			switch(columnIndex) {
				case 0:
					text = "";
					break;
				case 1: // RT
					text = decimalFormat.format(peakModel.getRetentionTimeAtPeakMaximum() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
					break;
				case 2: // RI
					if(PreferenceSupplier.showRetentionIndexWithoutDecimals()) {
						text = Integer.toString((int)peakModel.getPeakMassSpectrum().getRetentionIndex());
					} else {
						text = decimalFormat.format(peakModel.getPeakMassSpectrum().getRetentionIndex());
					}
					break;
				case 3: // Area
					if(PreferenceSupplier.showAreaWithoutDecimals()) {
						text = Integer.toString((int)peak.getIntegratedArea());
					} else {
						text = decimalFormat.format(peak.getIntegratedArea());
					}
					break;
				case 4: // Start RT
					text = decimalFormat.format(peakModel.getStartRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
					break;
				case 5: // Stop RT
					text = decimalFormat.format(peakModel.getStopRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
					break;
				case 6: // Width
					text = decimalFormat.format(peakModel.getWidthByInflectionPoints() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
					break;
				case 7:
				case 8:
					if(element instanceof IChromatogramPeakMSD) {
						IChromatogramPeakMSD chromatogramPeak = (IChromatogramPeakMSD)element;
						switch(columnIndex) {
							case 7: // Scan# at Peak Maximum
								text = Integer.toString(chromatogramPeak.getScanMax());
								break;
							case 8: // S/N
								text = decimalFormat.format(chromatogramPeak.getSignalToNoiseRatio());
								break;
						}
					}
					break;
				case 9: // Leading
					text = decimalFormat.format(peakModel.getLeading());
					break;
				case 10: // Tailing
					text = decimalFormat.format(peakModel.getTailing());
					break;
				case 11: // Model Description
					text = peak.getModelDescription();
					break;
				case 12: // Suggested Components
					text = Integer.toString(peak.getSuggestedNumberOfComponents());
					break;
				case 13: // Name
					if(libraryInformation != null) {
						text = libraryInformation.getName();
					}
					break;
				default:
					text = "n.v.";
			}
		}
		return text;
	}

	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_PEAK, IApplicationImage.SIZE_16x16);
	}

	private ILibraryInformation getLibraryInformation(List<IPeakTarget> targets) {

		ILibraryInformation libraryInformation = null;
		Collections.sort(targets, targetExtendedComparator);
		if(targets.size() >= 1) {
			libraryInformation = targets.get(0).getLibraryInformation();
		}
		return libraryInformation;
	}
}
