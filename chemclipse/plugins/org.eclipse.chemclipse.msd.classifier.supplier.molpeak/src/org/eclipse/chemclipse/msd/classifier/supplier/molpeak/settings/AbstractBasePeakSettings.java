/*******************************************************************************
 * Copyright (c) 2016, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Lorenz Gerber - initial API and implementation
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.classifier.supplier.molpeak.settings;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;
import org.eclipse.chemclipse.model.settings.IProcessSettings;
import org.eclipse.chemclipse.msd.classifier.supplier.molpeak.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.support.literature.LiteratureReference;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public abstract class AbstractBasePeakSettings implements IBasePeakSettings, IProcessSettings {

	private static final Logger logger = Logger.getLogger(AbstractBasePeakSettings.class);
	//
	@JsonProperty(value = "Limit Match Factor", defaultValue = "80.0")
	@JsonPropertyDescription(value = "Run an identification if no target exists with a Match Factor >= the given limit.")
	@FloatSettingsProperty(minValue = IIdentifierSettings.MIN_LIMIT_MATCH_FACTOR, maxValue = IIdentifierSettings.MAX_LIMIT_MATCH_FACTOR)
	private float limitMatchFactor = 80.0f;
	@JsonProperty(value = "Match Quality", defaultValue = "80.0")
	@JsonPropertyDescription(value = "The match quality is set as the Match Factor.")
	@FloatSettingsProperty(minValue = PreferenceSupplier.MIN_FACTOR, maxValue = PreferenceSupplier.MAX_FACTOR)
	private float matchQuality = 80.0f;

	@Override
	public float getLimitMatchFactor() {

		return limitMatchFactor;
	}

	@Override
	public void setLimitMatchFactor(float limitMatchFactor) {

		this.limitMatchFactor = limitMatchFactor;
	}

	@Override
	public float getMatchQuality() {

		return matchQuality;
	}

	@Override
	public void setMatchQuality(float matchQuality) {

		this.matchQuality = matchQuality;
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		return Collections.singletonList(createLiteratureReference("S0165237012000137.ris", "10.1016/j.jaap.2012.01.011"));
	}

	private static LiteratureReference createLiteratureReference(String file, String doi) {

		String content;
		try {
			content = new String(MassSpectrumIdentifierSettings.class.getResourceAsStream(file).readAllBytes());
		} catch(IOException | NullPointerException e) {
			content = doi;
			logger.warn(e);
		}
		return new LiteratureReference(content);
	}
}
