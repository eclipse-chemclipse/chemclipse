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
package org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v110.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"link", "parameter", "values"})
@XmlRootElement(name = "altXdata")
public class AltXdata {

	protected List<Link> link;
	protected List<Parameter> parameter;
	@XmlElement(required = true)
	protected Values values;
	@XmlAttribute(name = "units", required = true)
	protected Units units;
	@XmlAttribute(name = "label")
	protected String label;
	@XmlAttribute(name = "linkid")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	protected String linkid;
	@XmlAttribute(name = "valueorder")
	protected Valueorder valueorder;

	public List<Link> getLink() {

		if(link == null) {
			link = new ArrayList<Link>();
		}
		return this.link;
	}

	public List<Parameter> getParameter() {

		if(parameter == null) {
			parameter = new ArrayList<Parameter>();
		}
		return this.parameter;
	}

	public Values getValues() {

		return values;
	}

	public void setValues(Values value) {

		this.values = value;
	}

	public Units getUnits() {

		return units;
	}

	public void setUnits(Units value) {

		this.units = value;
	}

	public String getLabel() {

		return label;
	}

	public void setLabel(String value) {

		this.label = value;
	}

	public String getLinkid() {

		return linkid;
	}

	public void setLinkid(String value) {

		this.linkid = value;
	}

	public Valueorder getValueorder() {

		return valueorder;
	}

	public void setValueorder(Valueorder value) {

		this.valueorder = value;
	}
}
