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
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"analyzer"})
public class AnalyzerList {

	@XmlElement(required = true)
	private List<ParamType> analyzer;
	@XmlAttribute(name = "count", required = true)
	private int count;

	public List<ParamType> getAnalyzer() {

		if(analyzer == null) {
			analyzer = new ArrayList<ParamType>();
		}
		return this.analyzer;
	}

	public int getCount() {

		return count;
	}

	public void setCount(int value) {

		this.count = value;
	}
}