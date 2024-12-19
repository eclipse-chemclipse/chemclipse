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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.settings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.classifier.settings.IChromatogramClassifierSettings;
import org.eclipse.chemclipse.support.literature.LiteratureReference;
import org.eclipse.chemclipse.support.settings.SystemSettings;
import org.eclipse.chemclipse.support.settings.SystemSettingsStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@SystemSettings(SystemSettingsStrategy.NEW_INSTANCE)
public class NoiseSegmentSetterSettings implements IChromatogramClassifierSettings {

	@JsonProperty(value = "Use Only New Segment", defaultValue = "false")
	@JsonPropertyDescription(value = "If set to true, only the new segment is set to be used.")
	private boolean useOnlyNewSegment = false;

	public boolean isUseOnlyNewSegment() {

		return useOnlyNewSegment;
	}

	public void setUseOnlyNewSegment(boolean useOnlyNewSegment) {

		this.useOnlyNewSegment = useOnlyNewSegment;
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		return new ArrayList<>();
	}
}