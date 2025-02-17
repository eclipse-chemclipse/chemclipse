/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Matthias Mailänder - remove noise enums
 * Philip Wenig - refactoring classifier
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.core.noise;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.settings.NoiseChromatogramClassifierSettings;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.core.AbstractChromatogramClassifier;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.result.IChromatogramClassifierResult;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.settings.IChromatogramClassifierSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.results.NoiseSegmentMeasurementResult;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class NoiseChromatogramClassifier extends AbstractChromatogramClassifier {

	private static final String NAME = "Noise Calculator (Chromatogram)";

	public NoiseChromatogramClassifier() {

		super(DataType.MSD, DataType.WSD, DataType.CSD);
	}

	@Override
	public IProcessingInfo<IChromatogramClassifierResult> applyClassifier(IChromatogramSelection chromatogramSelection, IChromatogramClassifierSettings chromatogramClassifierSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramClassifierResult> processingInfo = new ProcessingInfo<>();
		//
		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		NoiseChromatogramClassifierSettings settings = getNoiseChromatogramClassifierSettings(chromatogramClassifierSettings);
		//
		NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = NoiseChromatogramSupport.applyNoiseSettings(chromatogram, settings, monitor);
		if(noiseSegmentMeasurementResult == null) {
			processingInfo.addErrorMessage(NAME, "Can't find any noise segments in the given chromatogram.");
		} else if(noiseSegmentMeasurementResult.getChromatogramSegmentation().getSegmentWidth() != settings.getSegmentWidth()) {
			StringBuilder builder = new StringBuilder();
			builder.append("No noise segments found with segmentation width ");
			builder.append(settings.getSegmentWidth());
			builder.append(" using value ");
			builder.append(noiseSegmentMeasurementResult.getChromatogramSegmentation().getSegmentWidth());
			builder.append(" instead, you might want to adjust settings to get better results.");
			processingInfo.addWarnMessage(NAME, builder.toString());
		}
		//
		return processingInfo;
	}

	private NoiseChromatogramClassifierSettings getNoiseChromatogramClassifierSettings(IChromatogramClassifierSettings chromatogramClassifierSettings) {

		if(chromatogramClassifierSettings instanceof NoiseChromatogramClassifierSettings noiseChromatogramClassifierSettings) {
			return noiseChromatogramClassifierSettings;
		} else {
			return new NoiseChromatogramClassifierSettings();
		}
	}
}
