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
@XmlType(name = "", propOrder = {"matrix", "expect", "include", "scMatch", "scMismatch", "gapOpen", "gapExtend", "filter", "pattern", "entrezQuery", "cbs", "queryGencode", "dbGencode", "bl2SeqMode"})
@XmlRootElement(name = "Parameters")
public class Parameters {

	protected String matrix;

	protected double expect;

	protected Double include;

	@XmlElement(name = "sc-match")
	protected BigInteger scMatch;

	@XmlElement(name = "sc-mismatch")
	protected BigInteger scMismatch;

	@XmlElement(name = "gap-open")
	protected BigInteger gapOpen;

	@XmlElement(name = "gap-extend")
	protected BigInteger gapExtend;

	protected String filter;

	protected String pattern;

	@XmlElement(name = "entrez-query")
	protected String entrezQuery;

	protected BigInteger cbs;

	@XmlElement(name = "query-gencode")
	protected BigInteger queryGencode;

	@XmlElement(name = "db-gencode")
	protected BigInteger dbGencode;

	@XmlElement(name = "bl2seq-mode")
	protected String bl2SeqMode;

	public String getMatrix() {

		return matrix;
	}

	public void setMatrix(String value) {

		this.matrix = value;
	}

	public double getExpect() {

		return expect;
	}

	public void setExpect(double value) {

		this.expect = value;
	}

	public Double getInclude() {

		return include;
	}

	public void setInclude(Double value) {

		this.include = value;
	}

	public BigInteger getScMatch() {

		return scMatch;
	}

	public void setScMatch(BigInteger value) {

		this.scMatch = value;
	}

	public BigInteger getScMismatch() {

		return scMismatch;
	}

	public void setScMismatch(BigInteger value) {

		this.scMismatch = value;
	}

	public BigInteger getGapOpen() {

		return gapOpen;
	}

	public void setGapOpen(BigInteger value) {

		this.gapOpen = value;
	}

	public BigInteger getGapExtend() {

		return gapExtend;
	}

	public void setGapExtend(BigInteger value) {

		this.gapExtend = value;
	}

	public String getFilter() {

		return filter;
	}

	public void setFilter(String value) {

		this.filter = value;
	}

	public String getPattern() {

		return pattern;
	}

	public void setPattern(String value) {

		this.pattern = value;
	}

	public String getEntrezQuery() {

		return entrezQuery;
	}

	public void setEntrezQuery(String value) {

		this.entrezQuery = value;
	}

	public BigInteger getCbs() {

		return cbs;
	}

	public void setCbs(BigInteger value) {

		this.cbs = value;
	}

	public BigInteger getQueryGencode() {

		return queryGencode;
	}

	public void setQueryGencode(BigInteger value) {

		this.queryGencode = value;
	}

	public BigInteger getDbGencode() {

		return dbGencode;
	}

	public void setDbGencode(BigInteger value) {

		this.dbGencode = value;
	}

	public String getBl2SeqMode() {

		return bl2SeqMode;
	}

	public void setBl2SeqMode(String value) {

		this.bl2SeqMode = value;
	}
}
