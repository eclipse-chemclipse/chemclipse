/*******************************************************************************
 * Copyright (c) 2011, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.backfolding.settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.settings.AbstractChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.backfolding.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.literature.LiteratureReference;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChromatogramFilterSettings extends AbstractChromatogramFilterSettings {

	private static final Logger logger = Logger.getLogger(ChromatogramFilterSettings.class);
	//
	@JsonProperty(value = "Backfolding Runs", defaultValue = "3")
	@IntSettingsProperty(minValue = PreferenceSupplier.MIN_BACKFOLDING_RUNS, maxValue = PreferenceSupplier.MAX_BACKFOLDING_RUNS)
	private int numberOfBackfoldingRuns = 3;
	@JsonProperty(value = "Max Retention Time Shift (Milliseconds)", defaultValue = "5000")
	@IntSettingsProperty(minValue = PreferenceSupplier.MIN_RETENTION_TIME_SHIFT, maxValue = PreferenceSupplier.MAX_RETENTION_TIME_SHIFT)
	private int maximumRetentionTimeShift = 5000;

	public int getNumberOfBackfoldingRuns() {

		return numberOfBackfoldingRuns;
	}

	public void setNumberOfBackfoldingRuns(int numberOfBackfoldingRuns) {

		this.numberOfBackfoldingRuns = numberOfBackfoldingRuns;
	}

	public int getMaximumRetentionTimeShift() {

		return maximumRetentionTimeShift;
	}

	public void setMaximumRetentionTimeShift(int maximumRetentionTimeShift) {

		this.maximumRetentionTimeShift = maximumRetentionTimeShift;
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		List<LiteratureReference> literatureReferences = new ArrayList<>();
		literatureReferences.add(createLiteratureReference("achs_ancham61_73.ris", "10.1021/ac00176a015"));
		literatureReferences.add(createLiteratureReference("pericles_10969888c31.ris", "10.1002/(SICI)1096-9888(199605)31:5<509::AID-JMS323>3.0.CO;2-B"));
		literatureReferences.add(createLiteratureReference("pericles_10969888c32.ris", "10.1002/(SICI)1096-9888(199704)32:4<438::AID-JMS499>3.0.CO;2-N"));
		literatureReferences.add(createLiteratureReference("pericles_10969888c33.ris", "10.1002/(SICI)1096-9888(199711)32:11<1253::AID-JMS593>3.0.CO;2-T"));
		return literatureReferences;
	}

	private static LiteratureReference createLiteratureReference(String file, String doi) {

		String content;
		try {
			content = new String(ChromatogramFilterSettings.class.getResourceAsStream(file).readAllBytes());
		} catch(IOException | NullPointerException e) {
			content = doi;
			logger.warn(e);
		}
		return new LiteratureReference(content);
	}
}
