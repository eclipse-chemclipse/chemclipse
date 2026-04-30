/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.filter.IPeakFilter;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.Processor;
import org.eclipse.chemclipse.processing.filter.Filter;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.eclipse.chemclipse.xxd.filter.peaks.settings.HighPassPeaksFilterSettings;
import org.eclipse.chemclipse.xxd.filter.peaks.settings.PeakFilterOption;
import org.osgi.service.component.annotations.Component;

@Component(service = {IPeakFilter.class, Filter.class, Processor.class})
public class HighPassPeaksFilter extends AbstractPeakFilter<HighPassPeaksFilterSettings> {

	@Override
	public String getName() {

		return "High Pass Peaks";
	}

	@Override
	public String getDescription() {

		return "Keep the n-highest peaks";
	}

	@Override
	public Class<HighPassPeaksFilterSettings> getConfigClass() {

		return HighPassPeaksFilterSettings.class;
	}

	@Override
	public void filterPeaks(IChromatogramSelection chromatogramSelection, HighPassPeaksFilterSettings configuration, ProcessExecutionContext context) throws IllegalArgumentException {

		Collection<IPeak> peaks = getReadOnlyPeaks(chromatogramSelection);

		if(configuration == null) {
			configuration = createConfiguration(peaks);
		}

		/*
		 * 0 is the conditional option to skip this processor.
		 */
		int numberHighest = configuration.getNumberHighest();
		if(numberHighest > 0) {
			PeakFilterOption peakFilterOption = configuration.getPeakFilterOption();
			List<IPeak> peaksToDelete = XPassPeaksFilter.filterPeaks(peaks, context, peakFilterOption, numberHighest, true);
			deletePeaks(peaksToDelete, chromatogramSelection);
			resetPeakSelection(chromatogramSelection);
		}
	}

	@Override
	public List<String> getLegacyIDs() {

		List<String> legacyIDs = new ArrayList<>();
		legacyIDs.add("PeakFilter:filter:processor:class:org.eclipse.chemclipse.xxd.model.filter.peaks.HighPassPeaksFilter");
		return legacyIDs;
	}
}