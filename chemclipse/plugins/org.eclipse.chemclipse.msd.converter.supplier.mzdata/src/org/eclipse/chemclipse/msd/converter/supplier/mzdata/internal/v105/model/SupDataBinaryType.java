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
 * Matthias Mailänder - read precision as integer
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "supDataBinaryType", propOrder = {"arrayName", "data"})
public class SupDataBinaryType {

	@XmlElement(required = true)
	private String arrayName;
	@XmlElement(required = true)
	private Data data;
	@XmlAttribute(name = "id", required = true)
	private int id;

	public String getArrayName() {

		return arrayName;
	}

	public void setArrayName(String value) {

		this.arrayName = value;
	}

	public Data getData() {

		return data;
	}

	public void setData(Data value) {

		this.data = value;
	}

	public int getId() {

		return id;
	}

	public void setId(int value) {

		this.id = value;
	}
}
