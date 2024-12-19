/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.core.noise;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.settings.NoiseSegmentSetterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.core.AbstractChromatogramClassifier;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.result.IChromatogramClassifierResult;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.settings.IChromatogramClassifierSettings;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class NoiseSegmentSetter extends AbstractChromatogramClassifier {

	public NoiseSegmentSetter() {

		super(DataType.MSD, DataType.WSD, DataType.CSD);
	}

	@Override
	public IProcessingInfo<IChromatogramClassifierResult> applyClassifier(IChromatogramSelection<?, ?> chromatogramSelection, IChromatogramClassifierSettings chromatogramClassifierSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramClassifierResult> processingInfo = new ProcessingInfo<>();
		//
		NoiseSegmentSetterSettings settings = getNoiseSegmentSetterSettings(chromatogramClassifierSettings);
		NoiseChromatogramSupport.addNoiseSegment(chromatogramSelection, settings.isUseOnlyNewSegment());
		//
		return processingInfo;
	}

	private NoiseSegmentSetterSettings getNoiseSegmentSetterSettings(IChromatogramClassifierSettings chromatogramClassifierSettings) {

		if(chromatogramClassifierSettings instanceof NoiseSegmentSetterSettings noiseSegmentSetterSettings) {
			return noiseSegmentSetterSettings;
		} else {
			return new NoiseSegmentSetterSettings();
		}
	}
}