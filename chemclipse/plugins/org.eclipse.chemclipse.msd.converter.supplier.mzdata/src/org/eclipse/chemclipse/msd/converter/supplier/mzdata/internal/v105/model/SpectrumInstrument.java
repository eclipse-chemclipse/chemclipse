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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class SpectrumInstrument extends ParamType {

	@XmlAttribute(name = "msLevel", required = true)
	private int msLevel;
	@XmlAttribute(name = "mzRangeStart")
	private Float mzRangeStart;
	@XmlAttribute(name = "mzRangeStop")
	private Float mzRangeStop;

	public int getMsLevel() {

		return msLevel;
	}

	public void setMsLevel(int value) {

		this.msLevel = value;
	}

	public Float getMzRangeStart() {

		return mzRangeStart;
	}

	public void setMzRangeStart(Float value) {

		this.mzRangeStart = value;
	}

	public Float getMzRangeStop() {

		return mzRangeStop;
	}

	public void setMzRangeStop(Float value) {

		this.mzRangeStop = value;
	}
}