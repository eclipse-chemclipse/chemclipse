/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"parameter", "coordinates", "xdata"})
@XmlRootElement(name = "trace")
public class Trace {

	protected List<Parameter> parameter;
	protected List<Coordinates> coordinates;
	@XmlElement(name = "Xdata")
	protected List<Xdata> xdata;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "technique", required = true)
	protected Technique technique;

	public List<Parameter> getParameter() {

		if(parameter == null) {
			parameter = new ArrayList<Parameter>();
		}
		return this.parameter;
	}

	public List<Coordinates> getCoordinates() {

		if(coordinates == null) {
			coordinates = new ArrayList<Coordinates>();
		}
		return this.coordinates;
	}

	public List<Xdata> getXdata() {

		if(xdata == null) {
			xdata = new ArrayList<Xdata>();
		}
		return this.xdata;
	}

	public String getName() {

		return name;
	}

	public void setName(String value) {

		this.name = value;
	}

	public Technique getTechnique() {

		return technique;
	}

	public void setTechnique(Technique value) {

		this.technique = value;
	}
}
