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
@XmlType(name = "", propOrder = {"iterNum", "queryID", "queryDef", "queryLen", "hits", "stat", "message"})
@XmlRootElement(name = "Iteration")
public class Iteration {

	@XmlElement(name = "Iteration_iter-num", required = true)
	protected BigInteger iterNum;
	@XmlElement(name = "Iteration_query-ID")
	protected String queryID;
	@XmlElement(name = "Iteration_query-def")
	protected String queryDef;
	@XmlElement(name = "Iteration_query-len")
	protected BigInteger queryLen;
	@XmlElement(name = "Iteration_hits")
	protected Hits hits;
	@XmlElement(name = "Iteration_stat")
	protected Stat stat;
	@XmlElement(name = "Iteration_message")
	protected String message;

	public BigInteger getIterNum() {

		return iterNum;
	}

	public void setIterNum(BigInteger value) {

		this.iterNum = value;
	}

	public String getQueryID() {

		return queryID;
	}

	public void setQueryID(String value) {

		this.queryID = value;
	}

	public String getQueryDef() {

		return queryDef;
	}

	public void setQueryDef(String value) {

		this.queryDef = value;
	}

	public BigInteger getQueryLen() {

		return queryLen;
	}

	public void setQueryLen(BigInteger value) {

		this.queryLen = value;
	}

	public Hits getHits() {

		return hits;
	}

	public void setHits(Hits value) {

		this.hits = value;
	}

	public Stat getStat() {

		return stat;
	}

	public void setStat(Stat value) {

		this.stat = value;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String value) {

		this.message = value;
	}
}
