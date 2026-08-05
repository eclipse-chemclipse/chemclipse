/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.filter.peaks;

import java.util.Collection;
import java.util.Set;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.filter.IPeakFilter;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.PenaltyCalculationSupport;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.Processor;
import org.eclipse.chemclipse.processing.filter.Filter;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.eclipse.chemclipse.xxd.filter.peaks.settings.PenaltyCalculatorFilterSettings;
import org.eclipse.core.runtime.SubMonitor;
import org.osgi.service.component.annotations.Component;

@Component(service = {IPeakFilter.class, Filter.class, Processor.class})
public class CalculatePenaltyFilter extends AbstractPeakFilter<PenaltyCalculatorFilterSettings> {

	@Override
	public String getName() {

		return "Penalty Calculator";
	}

	@Override
	public String getDescription() {

		return "Calculate match penalties based on RT or RI deviations of unknown and library hit.";
	}

	@Override
	public Class<PenaltyCalculatorFilterSettings> getConfigClass() {

		return PenaltyCalculatorFilterSettings.class;
	}

	@Override
	public void filterPeaks(IChromatogramSelection chromatogramSelection, PenaltyCalculatorFilterSettings penaltyCalculationFilterSettings, ProcessExecutionContext context) throws IllegalArgumentException {

		Collection<IPeak> peaks = getReadOnlyPeaks(chromatogramSelection);
		if(penaltyCalculationFilterSettings == null) {
			penaltyCalculationFilterSettings = createConfiguration(peaks);
		}

		SubMonitor subMonitor = SubMonitor.convert(context.getProgressMonitor(), peaks.size());
		for(IPeak peak : peaks) {
			Set<IIdentificationTarget> targets = peak.getTargets();
			for(IIdentificationTarget identificationTarget : targets) {
				IComparisonResult comparisonResult = identificationTarget.getComparisonResult();
				ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
				IPeakModel peakModel = peak.getPeakModel();
				IScan unknown = peakModel.getPeakMaximum();
				IScan reference = getReference(libraryInformation);
				PenaltyCalculationSupport.applyPenalty(unknown, reference, comparisonResult, penaltyCalculationFilterSettings);
			}
			subMonitor.worked(1);
		}
	}

	private IScan getReference(ILibraryInformation libraryInformation) {

		IScan reference = new Scan(1000.0f);
		reference.setRetentionTime(libraryInformation.getRetentionTime());
		reference.setRetentionIndex(libraryInformation.getRetentionIndex());

		return reference;
	}
}