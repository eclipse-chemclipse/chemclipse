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
package org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v100.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "technique")
@XmlEnum
public enum Technique {
	ATOMIC, CHROM, FLUOR, IR, MS, NIR, NMR, PDA, PARTICLE, POLAR, RAMAN, THERMAL, UNKNOWN, UVVIS, XRAY;

	public String value() {

		return name();
	}

	public static Technique fromValue(String v) {

		return valueOf(v);
	}
}
