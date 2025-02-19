/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - improvements
 * Lorenz Gerber - allow duplicate sample names
 *******************************************************************************/
package org.eclipse.chemclipse.model.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractSample<D extends ISampleData<?>> implements ISample {

	private String sampleName = "";
	private String groupName = "";
	private String classification = "";
	private String description = "";
	private List<D> sampleData = new ArrayList<>();
	private boolean selected = true;
	private boolean predicted = false;
	private String rgb = "255,0,0";

	public AbstractSample(String sampleName) {

		this.sampleName = sampleName;
		sampleData = new ArrayList<>();
		selected = true;
	}

	@Override
	public String getSampleName() {

		return sampleName;
	}

	@Override
	public void setSampleName(String sampleName) {

		this.sampleName = sampleName;
	}

	@Override
	public String getGroupName() {

		return groupName;
	}

	@Override
	public void setGroupName(String groupName) {

		this.groupName = groupName;
	}

	@Override
	public String getClassification() {

		return classification;
	}

	@Override
	public void setClassification(String classification) {

		this.classification = classification;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public void setDescription(String description) {

		this.description = description;
	}

	@Override
	public List<D> getSampleData() {

		return sampleData;
	}

	@Override
	public boolean isSelected() {

		return selected;
	}

	@Override
	public void setSelected(boolean selected) {

		this.selected = selected;
	}

	@Override
	public boolean isPredicted() {

		return predicted;
	}

	@Override
	public void setPredicted(boolean predicted) {

		this.predicted = predicted;
	}

	@Override
	public String getRGB() {

		return rgb;
	}

	@Override
	public void setRGB(String rgb) {

		this.rgb = rgb;
	}

	@Override
	public int hashCode() {

		return Objects.hash(sampleName);
	}
}
