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
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"value"})
public class Data {

	@XmlValue
	private byte[] value;
	@XmlAttribute(name = "precision", required = true)
	private int precision;
	@XmlAttribute(name = "endian", required = true)
	private String endian;
	@XmlAttribute(name = "length", required = true)
	private int length;

	public byte[] getValue() {

		return value;
	}

	public void setValue(byte[] value) {

		this.value = value;
	}

	public int getPrecision() {

		return precision;
	}

	public void setPrecision(int value) {

		this.precision = value;
	}

	public String getEndian() {

		return endian;
	}

	public void setEndian(String value) {

		this.endian = value;
	}

	public int getLength() {

		return length;
	}

	public void setLength(int value) {

		this.length = value;
	}
}