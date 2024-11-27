/*******************************************************************************
 * Copyright (c) 2018, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import org.eclipse.chemclipse.model.settings.AbstractProcessSettings;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class AbstractIdentifierSettings extends AbstractProcessSettings implements IIdentifierSettings {

	/**
	 * Limit Match Factor
	 */
	@JsonProperty(value = "Limit Match Factor", defaultValue = "80.0")
	@JsonPropertyDescription(value = "Run an identification if no target exists with a Match Factor >= the given limit.")
	@FloatSettingsProperty(minValue = IIdentifierSettings.MIN_LIMIT_MATCH_FACTOR, maxValue = IIdentifierSettings.MAX_LIMIT_MATCH_FACTOR)
	private float limitMatchFactor = IIdentifierSettings.DEF_LIMIT_MATCH_FACTOR;

	@Override
	public float getLimitMatchFactor() {

		return limitMatchFactor;
	}

	@Override
	public void setLimitMatchFactor(float limitMatchFactor) {

		this.limitMatchFactor = limitMatchFactor;
	}
}