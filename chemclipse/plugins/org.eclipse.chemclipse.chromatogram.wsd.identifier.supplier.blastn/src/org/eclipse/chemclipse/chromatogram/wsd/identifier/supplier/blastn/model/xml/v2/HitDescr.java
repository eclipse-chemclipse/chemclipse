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
@XmlType(name = "", propOrder = {"id", "accession", "title", "taxid", "sciname"})
@XmlRootElement(name = "HitDescr")
public class HitDescr {

	@XmlElement(required = true)
	protected String id;

	protected String accession;

	protected String title;

	protected BigInteger taxid;

	protected String sciname;

	public String getId() {

		return id;
	}

	public void setId(String value) {

		this.id = value;
	}

	public String getAccession() {

		return accession;
	}

	public void setAccession(String value) {

		this.accession = value;
	}

	public String getTitle() {

		return title;
	}

	public void setTitle(String value) {

		this.title = value;
	}

	public BigInteger getTaxid() {

		return taxid;
	}

	public void setTaxid(BigInteger value) {

		this.taxid = value;
	}

	public String getSciname() {

		return sciname;
	}

	public void setSciname(String value) {

		this.sciname = value;
	}
}
