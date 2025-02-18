/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeakList", propOrder = {"peak", "peakGroup"})
public class PeakList {

	@XmlElement(name = "peak")
	protected List<Peak> peak;
	@XmlElement(name = "peakGroup")
	protected List<PeakGroup> peakGroup;

	public List<Peak> getPeak() {

		return peak;
	}

	public void setPeak(List<Peak> peak) {

		this.peak = peak;
	}

	public List<PeakGroup> getPeakGroup() {

		return peakGroup;
	}

	public void setPeakGroup(List<PeakGroup> peakGroup) {

		this.peakGroup = peakGroup;
	}
}
