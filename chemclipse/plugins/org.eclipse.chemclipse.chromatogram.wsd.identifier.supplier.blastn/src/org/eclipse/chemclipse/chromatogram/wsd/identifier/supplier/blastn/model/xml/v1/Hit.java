/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1;

import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"num", "id", "def", "accession", "len", "hsps"})
@XmlRootElement(name = "Hit")
public class Hit {

	@XmlElement(name = "Hit_num", required = true)
	protected BigInteger num;
	@XmlElement(name = "Hit_id", required = true)
	protected String id;
	@XmlElement(name = "Hit_def", required = true)
	protected String def;
	@XmlElement(name = "Hit_accession", required = true)
	protected String accession;
	@XmlElement(name = "Hit_len", required = true)
	protected BigInteger len;
	@XmlElement(name = "Hit_hsps", required = true)
	protected Hsps hsps;

	public BigInteger getNum() {

		return num;
	}

	public void setNum(BigInteger value) {

		this.num = value;
	}

	public String getId() {

		return id;
	}

	public void setId(String value) {

		this.id = value;
	}

	public String getDef() {

		return def;
	}

	public void setDef(String value) {

		this.def = value;
	}

	public String getAccession() {

		return accession;
	}

	public void setAccession(String value) {

		this.accession = value;
	}

	public BigInteger getLen() {

		return len;
	}

	public void setLen(BigInteger value) {

		this.len = value;
	}

	public Hsps getHsps() {

		return hsps;
	}

	public void setHsps(Hsps value) {

		this.hsps = value;
	}
}
