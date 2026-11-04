/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - improvements classifier
 * Lorenz Gerber - label area/concentration
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.extraction;

import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.xxd.process.supplier.pca.core.ValueOption;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.DescriptionOption;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IDataInputEntry;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Samples;

public class PeakExtractionSupport {

	public Samples extractPeakData(Map<IDataInputEntry, List<IPeak>> peaks, ExtractionSettings extractionSettings) {

		Samples samples;
		DescriptionOption descriptionOption = extractionSettings.getDescriptionOption();
		ValueOption valueOption = extractionSettings.getValueOption();

		switch(extractionSettings.getExtractionOption()) {
			case RETENTION_INDEX:
				PeakRetentionIndexExtractor peakRetentionIndexExtractor = new PeakRetentionIndexExtractor();
				int retentionIndexWindow = extractionSettings.getGroupValueWindow();
				samples = peakRetentionIndexExtractor.extractPeakData(peaks, retentionIndexWindow, descriptionOption, valueOption);
				break;
			case PEAK_TARGETS:
				PeakTargetExtractor peakTargetExtractor = new PeakTargetExtractor();
				samples = peakTargetExtractor.extractPeakData(peaks, descriptionOption, valueOption);
				break;
			default:
				PeakRetentionTimeExtractor peakRetentionTimeExtractor = new PeakRetentionTimeExtractor();
				int retentionTimeWindow = extractionSettings.getGroupValueWindow();
				samples = peakRetentionTimeExtractor.extractPeakData(peaks, retentionTimeWindow, descriptionOption, valueOption);
				break;
		}

		if(samples != null && ValueOption.CONCENTRATION.equals(valueOption)) {
			samples.setValueType(Samples.VALUE_TYPE_CONCENTRATION);
		}

		return samples;
	}
}
