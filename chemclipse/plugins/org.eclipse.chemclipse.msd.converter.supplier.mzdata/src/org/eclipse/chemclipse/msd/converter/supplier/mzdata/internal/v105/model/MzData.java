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
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"cvLookup", "description", "spectrumList"})
@XmlRootElement(name = "mzData")
public class MzData {

	private List<CvLookupType> cvLookup;
	@XmlElement(required = true)
	private Description description;
	@XmlElement(required = true)
	private SpectrumList spectrumList;
	@XmlAttribute(name = "version", required = true)
	private String version;
	@XmlAttribute(name = "accessionNumber", required = true)
	private String accessionNumber;

	public List<CvLookupType> getCvLookup() {

		if(cvLookup == null) {
			cvLookup = new ArrayList<>();
		}
		return this.cvLookup;
	}

	public Description getDescription() {

		return description;
	}

	public void setDescription(Description value) {

		this.description = value;
	}

	public SpectrumList getSpectrumList() {

		return spectrumList;
	}

	public void setSpectrumList(SpectrumList value) {

		this.spectrumList = value;
	}

	public String getVersion() {

		if(version == null) {
			return "1.05";
		} else {
			return version;
		}
	}

	public void setVersion(String value) {

		this.version = value;
	}

	public String getAccessionNumber() {

		return accessionNumber;
	}

	public void setAccessionNumber(String value) {

		this.accessionNumber = value;
	}
}
