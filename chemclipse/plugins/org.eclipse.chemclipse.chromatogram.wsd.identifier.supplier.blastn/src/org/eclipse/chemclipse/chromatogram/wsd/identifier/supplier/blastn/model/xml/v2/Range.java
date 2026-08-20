/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2;

import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"from", "to"})
@XmlRootElement(name = "Range")
public class Range {

	@XmlElement(required = true)
	protected BigInteger from;

	@XmlElement(required = true)
	protected BigInteger to;

	public BigInteger getFrom() {

		return from;
	}

	public void setFrom(BigInteger value) {

		this.from = value;
	}

	public BigInteger getTo() {

		return to;
	}

	public void setTo(BigInteger value) {

		this.to = value;
	}
}
