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
@XmlType(name = "", propOrder = {"admin", "instrument", "dataProcessing"})
public class Description {

	@XmlElement(required = true)
	private AdminType admin;
	@XmlElement(required = true)
	private InstrumentDescriptionType instrument;
	@XmlElement(required = true)
	private DataProcessingType dataProcessing;

	public AdminType getAdmin() {

		return admin;
	}

	public void setAdmin(AdminType value) {

		this.admin = value;
	}

	public InstrumentDescriptionType getInstrument() {

		return instrument;
	}

	public void setInstrument(InstrumentDescriptionType value) {

		this.instrument = value;
	}

	public DataProcessingType getDataProcessing() {

		return dataProcessing;
	}

	public void setDataProcessing(DataProcessingType value) {

		this.dataProcessing = value;
	}
}