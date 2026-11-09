/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.model.comparator.PeakRetentionTimeComparator;
import org.eclipse.chemclipse.model.core.IClassifier;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.quantitation.IQuantitationEntry;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.comparator.SortOrder;

public class PeakQuantitationsExtractor {

	private PeakRetentionTimeComparator peakRetentionTimeComparator = new PeakRetentionTimeComparator(SortOrder.ASC);

	public PeakQuantitations extract(List<? extends IPeak> peaks) {

		return extract(peaks, null);
	}

	public PeakQuantitations extract(List<? extends IPeak> peaks, IChromatogramSelection chromatogramSelection) {

		PeakQuantitations peakQuantitations = new PeakQuantitations();

		if(peaks != null) {
			/*
			 * Sort peak list by retention time.
			 */
			peaks = new ArrayList<>(peaks);
			Collections.sort(peaks, peakRetentionTimeComparator);

			Set<String> quantitationColumns = new HashSet<>();
			for(IPeak peak : peaks) {
				for(IQuantitationEntry quantitationEntry : peak.getQuantitationEntries()) {
					quantitationColumns.add(getIdentifier(quantitationEntry.getName(), quantitationEntry.getConcentrationUnit()));
				}
			}
			/*
			 * Add the titles.
			 */
			peakQuantitations.getTitles().add("Time [min]"); //$NON-NLS-1$
			peakQuantitations.getTitles().add("Name"); //$NON-NLS-1$
			peakQuantitations.getTitles().add("CAS#"); //$NON-NLS-1$
			peakQuantitations.getTitles().add("Reference Identifier"); //$NON-NLS-1$
			peakQuantitations.getTitles().add("Area"); //$NON-NLS-1$
			peakQuantitations.getTitles().add("Classifier"); //$NON-NLS-1$
			peakQuantitations.getTitles().add("Quantifier"); //$NON-NLS-1$
			for(String quantitationColumn : quantitationColumns) {
				peakQuantitations.getTitles().add(quantitationColumn);
			}
			/*
			 * Add the concentrations.
			 */
			for(IPeak peak : peaks) {
				IIdentificationTarget identificationTarget = IIdentificationTarget.getIdentificationTarget(peak);
				PeakQuantitation peakQuantitation = new PeakQuantitation();
				peakQuantitation.setRetentionTime(peak.getPeakModel().getRetentionTimeAtPeakMaximum());
				peakQuantitation.setName(getName(identificationTarget));
				peakQuantitation.setCasNumber(getCasNumber(identificationTarget));
				peakQuantitation.setReferenceIdentifier(getReferenceIdentifier(identificationTarget));
				peakQuantitation.setIntegratedArea(peak.getIntegratedArea());
				peakQuantitation.setClassifier(IClassifier.asString(peak));
				peakQuantitation.setQuantifier(getQuantifier(peak));

				for(String quantitationColumn : quantitationColumns) {
					peakQuantitation.getConcentrations().add(getConcentration(peak, quantitationColumn));
				}

				peakQuantitation.setChromatogramSelection(chromatogramSelection);
				peakQuantitation.setPeak(peak);
				peakQuantitations.getQuantitationEntries().add(peakQuantitation);
			}
		}
		return peakQuantitations;
	}

	private String getName(IIdentificationTarget identificationTarget) {

		String name = ""; //$NON-NLS-1$
		if(identificationTarget != null) {
			name = identificationTarget.getLibraryInformation().getName();
		}
		return name;
	}

	private String getCasNumber(IIdentificationTarget identificationTarget) {

		String casNumber = ""; //$NON-NLS-1$
		if(identificationTarget != null) {
			casNumber = identificationTarget.getLibraryInformation().getCasNumber();
		}
		return casNumber;
	}

	private String getReferenceIdentifier(IIdentificationTarget identificationTarget) {

		String referenceIdentifier = ""; //$NON-NLS-1$
		if(identificationTarget != null) {
			referenceIdentifier = identificationTarget.getLibraryInformation().getReferenceIdentifier();
		}
		return referenceIdentifier;
	}

	private String getQuantifier(IPeak peak) {

		String quantifier = ""; //$NON-NLS-1$
		if(peak != null) {
			quantifier = !peak.getInternalStandards().isEmpty() ? "ISTD" : ""; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return quantifier;
	}

	private Double getConcentration(IPeak peak, String quantitationColumn) {

		Double concentration = Double.NaN;
		exitloop:
		for(IQuantitationEntry quantitationEntry : peak.getQuantitationEntries()) {
			if(quantitationColumn.equals(getIdentifier(quantitationEntry.getName(), quantitationEntry.getConcentrationUnit()))) {
				concentration = quantitationEntry.getConcentration();
				break exitloop;
			}
		}
		return concentration;
	}

	private String getIdentifier(final String name, String concentrationUnit) {

		return name + " [" + concentrationUnit + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
