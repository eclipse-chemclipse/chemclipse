/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "spectrumDescType", propOrder = {"spectrumSettings", "precursorList", "comments"})
public class SpectrumDescType {

	@XmlElement(required = true)
	private SpectrumSettingsType spectrumSettings;
	private PrecursorList precursorList;
	private List<String> comments;

	public SpectrumSettingsType getSpectrumSettings() {

		return spectrumSettings;
	}

	public void setSpectrumSettings(SpectrumSettingsType value) {

		this.spectrumSettings = value;
	}

	public PrecursorList getPrecursorList() {

		return precursorList;
	}

	public void setPrecursorList(PrecursorList value) {

		this.precursorList = value;
	}

	public List<String> getComments() {

		if(comments == null) {
			comments = new ArrayList<String>();
		}
		return this.comments;
	}
}
