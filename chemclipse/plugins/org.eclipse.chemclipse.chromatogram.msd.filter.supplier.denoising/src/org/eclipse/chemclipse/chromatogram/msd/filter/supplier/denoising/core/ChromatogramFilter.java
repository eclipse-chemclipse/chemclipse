/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - use {@link DenoisingFilterResult} instead of plain {@link List}
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.chromatogram.AbstractChromatogramFilterMSD;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.internal.core.support.Denoising;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.result.DenoisingFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.settings.FilterSettings;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.model.implementation.MeasurementResult;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.exceptions.FilterException;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.eclipse.chemclipse.support.util.TraceSettingUtil;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramFilter extends AbstractChromatogramFilterMSD {

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelectionMSD chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramFilterResult> processingInfo = new ProcessingInfo<>();
		processingInfo.addMessages(validate(chromatogramSelection, chromatogramFilterSettings));

		if(!processingInfo.hasErrorMessages()) {
			if(chromatogramFilterSettings instanceof FilterSettings filterSettings) {
				try {
					TraceSettingUtil ionSettingsUtil = new TraceSettingUtil();
					IMarkedTraces<ITrace> ionsToRemove = getMarkedTraces(ionSettingsUtil.extractTraces(ionSettingsUtil.deserialize(filterSettings.getIonsToRemove())));
					IMarkedTraces<ITrace> ionsToPreserve = getMarkedTraces(ionSettingsUtil.extractTraces(ionSettingsUtil.deserialize(filterSettings.getIonsToPreserve())));
					boolean adjustThresholdTransitions = filterSettings.isAdjustThresholdTransitions();
					int numberOfUsedIonsForCoefficient = filterSettings.getNumberOfUsedIonsForCoefficient();
					int segmentWidth = filterSettings.getSegmentWidth();

					List<ICombinedMassSpectrum> noiseMassSpectra = Denoising.applyDenoisingFilter(chromatogramSelection, ionsToRemove, ionsToPreserve, adjustThresholdTransitions, numberOfUsedIonsForCoefficient, segmentWidth, monitor);
					DenoisingFilterResult filterResult = new DenoisingFilterResult(ResultStatus.OK, "The chromatogram selection has been denoised successfully.", noiseMassSpectra);
					IMeasurementResult<?> measurementResult = new MeasurementResult("MS Denoising Filter", "org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising", "This list contains the calculated noise mass spectra.", filterResult);
					chromatogramSelection.getChromatogram().addMeasurementResult(measurementResult);
					processingInfo.setProcessingResult(filterResult);
					chromatogramSelection.getChromatogram().setDirty(true);
				} catch(FilterException e) {
					processingInfo.setProcessingResult(new DenoisingFilterResult(ResultStatus.EXCEPTION, e.getMessage()));
				}
			}
		}
		return processingInfo;
	}

	private IMarkedTraces<ITrace> getMarkedTraces(int[] traces) {

		IMarkedTraces<ITrace> markedTraces = new MarkedTraces(MarkedTraceModus.INCLUDE);
		for(int trace : traces) {
			markedTraces.add(new TraceNominalMSD(trace));
		}

		return markedTraces;
	}
}
