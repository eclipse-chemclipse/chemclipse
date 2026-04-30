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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.xpass.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum.AbstractMassSpectrumFilter;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.MassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.xpass.operations.DeleteMassSpectrumPeaksOperation;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.xpass.settings.HighPassMassSpectrumPeaksFilterSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.chemclipse.rcp.app.undo.UndoContextFactory;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class HighPassMassSpectrumPeaksFilter extends AbstractMassSpectrumFilter {

	private static final Logger logger = Logger.getLogger(HighPassMassSpectrumPeaksFilter.class);

	private static final String DESCRIPTION = "High Pass Peaks Filter";

	@Override
	public IProcessingInfo<IMassSpectrumFilterResult> applyFilter(List<IScanMSD> massSpectra, IMassSpectrumFilterSettings filterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(massSpectra, filterSettings);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}

		if(filterSettings instanceof HighPassMassSpectrumPeaksFilterSettings settings) {
			for(IScanMSD massSpectrum : massSpectra) {
				if(massSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
					filterPeaks(standaloneMassSpectrum, settings, monitor);
					processingInfo.addMessage(new ProcessingMessage(MessageType.INFO, DESCRIPTION, "The mass spectrum has filtered successfully."));
					IMassSpectrumFilterResult massSpectrumFilterResult = new MassSpectrumFilterResult(ResultStatus.OK, "The mass spectrum has filtered successfully.");
					processingInfo.setProcessingResult(massSpectrumFilterResult);
				}
			}
		} else {
			processingInfo.addErrorMessage(DESCRIPTION, "The filter settings instance is not a type of: " + HighPassMassSpectrumPeaksFilterSettings.class);
		}

		return processingInfo;
	}

	private void filterPeaks(IStandaloneMassSpectrum massSpectrum, HighPassMassSpectrumPeaksFilterSettings settings, IProgressMonitor monitor) {

		List<IMassSpectrumPeak> peaksToDelete = filteredPeaks(massSpectrum.getPeaks(), settings, monitor);

		deletePeaks(peaksToDelete, massSpectrum);
	}

	private static List<IMassSpectrumPeak> filteredPeaks(List<IMassSpectrumPeak> peaks, HighPassMassSpectrumPeaksFilterSettings settings, IProgressMonitor monitor) {

		List<IMassSpectrumPeak> peaksToDelete = new ArrayList<>();
		int numberHighest = settings.getNumberHighest();
		if(numberHighest > 0) {
			/*
			 * Data
			 */
			List<IMassSpectrumPeak> sortedPeaks = peaks.stream() //
					.sorted(Comparator.comparingDouble(IMassSpectrumPeak::getAbundance).reversed()) //
					.toList();
			/*
			 * Filter
			 */
			SubMonitor subMonitor = SubMonitor.convert(monitor, peaks.size());
			for(int i = 0; i < peaks.size(); i++) {
				if(i >= numberHighest) {
					IMassSpectrumPeak peak = sortedPeaks.get(i);
					peaksToDelete.add(peak);
				}
				subMonitor.worked(1);
			}
		}

		return peaksToDelete;
	}

	private void deletePeaks(List<IMassSpectrumPeak> peaksToDelete, IStandaloneMassSpectrum standaloneMassSpectrum) {

		if(!peaksToDelete.isEmpty()) {
			DeleteMassSpectrumPeaksOperation deletePeaks = new DeleteMassSpectrumPeaksOperation(standaloneMassSpectrum, peaksToDelete);
			deletePeaks.addContext(UndoContextFactory.getUndoContext());
			try {
				OperationHistoryFactory.getOperationHistory().execute(deletePeaks, null, null);
			} catch(ExecutionException e) {
				logger.warn(e);
			}
		}
	}
}