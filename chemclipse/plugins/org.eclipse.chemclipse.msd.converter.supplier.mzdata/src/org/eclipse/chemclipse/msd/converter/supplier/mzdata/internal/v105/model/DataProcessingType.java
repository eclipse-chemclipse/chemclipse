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
@XmlType(name = "dataProcessingType", propOrder = {"software", "processingMethod"})
public class DataProcessingType {

	@XmlElement(required = true)
	private Software software;
	private ParamType processingMethod;

	public Software getSoftware() {

		return software;
	}

	public void setSoftware(Software value) {

		this.software = value;
	}

	public ParamType getProcessingMethod() {

		return processingMethod;
	}

	public void setProcessingMethod(ParamType value) {

		this.processingMethod = value;
	}
}
