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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrumentDescriptionType", propOrder = {"instrumentName", "source", "analyzerList", "detector", "additional"})
public class InstrumentDescriptionType {

	@XmlElement(required = true)
	private String instrumentName;
	@XmlElement(required = true)
	private ParamType source;
	@XmlElement(required = true)
	private AnalyzerList analyzerList;
	@XmlElement(required = true)
	private ParamType detector;
	private ParamType additional;

	public String getInstrumentName() {

		return instrumentName;
	}

	public void setInstrumentName(String value) {

		this.instrumentName = value;
	}

	public ParamType getSource() {

		return source;
	}

	public void setSource(ParamType value) {

		this.source = value;
	}

	public AnalyzerList getAnalyzerList() {

		return analyzerList;
	}

	public void setAnalyzerList(AnalyzerList value) {

		this.analyzerList = value;
	}

	public ParamType getDetector() {

		return detector;
	}

	public void setDetector(ParamType value) {

		this.detector = value;
	}

	public ParamType getAdditional() {

		return additional;
	}

	public void setAdditional(ParamType value) {

		this.additional = value;
	}
}
