/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - field for area/concentration label
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeatureDataMatrix {

	private List<String> sampleNames = new ArrayList<>();
	private List<Feature> features = new ArrayList<>();
	private List<String> groupNames = new ArrayList<>();
	private String valueLabel = "";

	public FeatureDataMatrix(List<String> sampleNames, List<String> groupNames, List<Feature> features) {

		this.sampleNames = sampleNames;
		this.groupNames = groupNames;
		this.features = features;
	}

	public String getVariableName() {

		for(Feature feature : features) {
			String name = feature.getVariable().getType();
			if(!"".equals(name)) {
				return name;
			}
		}
		return "--";
	}

	public List<String> getSampleNames() {

		return Collections.unmodifiableList(sampleNames);
	}

	public List<String> getGroupNames() {

		return groupNames;
	}

	/**
	 * @return what the sample values represent, e.g. "Area" or "Concentration [mg/L]", or "" if unknown
	 */
	public String getValueLabel() {

		return valueLabel;
	}

	public void setValueLabel(String valueLabel) {

		this.valueLabel = valueLabel == null ? "" : valueLabel;
	}

	public List<Feature> getFeatures() {

		return Collections.unmodifiableList(features);
	}
}