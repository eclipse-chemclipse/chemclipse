/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.xpass.core;

import java.util.Collection;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.xpass.settings.NominalizeFilterSettings;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.filter.IPeakFilter;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.eclipse.chemclipse.processing.Processor;
import org.eclipse.chemclipse.processing.filter.Filter;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.eclipse.chemclipse.xxd.filter.peaks.AbstractPeakFilter;
import org.eclipse.core.runtime.SubMonitor;
import org.osgi.service.component.annotations.Component;

@Component(service = {IPeakFilter.class, Filter.class, Processor.class})
public class NominalizePeakFilter extends AbstractPeakFilter<NominalizeFilterSettings> {

	@Override
	public String getName() {

		return "Nominalize (Unit Mass)";
	}

	@Override
	public Class<NominalizeFilterSettings> getConfigClass() {

		return NominalizeFilterSettings.class;
	}

	@Override
	public void filterPeaks(IChromatogramSelection chromatogramSelection, NominalizeFilterSettings configuration, ProcessExecutionContext context) throws IllegalArgumentException {

		Collection<IPeak> peaks = getReadOnlyPeaks(chromatogramSelection);
		//
		if(configuration == null) {
			configuration = createConfiguration(peaks);
		}
		//
		SubMonitor subMonitor = SubMonitor.convert(context.getProgressMonitor(), peaks.size());
		for(IPeak peak : peaks) {
			if(peak instanceof IPeakMSD peakMSD) {
				IPeakModelMSD peakModelMSD = peakMSD.getPeakModel();
				XPassFilter.nominalize(peakModelMSD.getPeakMassSpectrum());
				subMonitor.worked(1);
			}
		}
	}
}
