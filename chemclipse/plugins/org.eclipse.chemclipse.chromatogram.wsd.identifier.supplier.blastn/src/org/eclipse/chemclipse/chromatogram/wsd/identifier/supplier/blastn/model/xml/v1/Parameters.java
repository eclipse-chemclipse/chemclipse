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
@XmlType(name = "", propOrder = {"matrix", "expect", "include", "scMatch", "scMismatch", "gapOpen", "gapExtend", "filter", "pattern", "entrezQuery"})
@XmlRootElement(name = "Parameters")
public class Parameters {

	@XmlElement(name = "Parameters_matrix", required = true)
	protected String matrix;
	@XmlElement(name = "Parameters_expect", required = true)
	protected double expect;
	@XmlElement(name = "Parameters_include")
	protected Double include;
	@XmlElement(name = "Parameters_sc-match", required = true)
	protected BigInteger scMatch;
	@XmlElement(name = "Parameters_sc-mismatch", required = true)
	protected BigInteger scMismatch;
	@XmlElement(name = "Parameters_gap-open", required = true)
	protected BigInteger gapOpen;
	@XmlElement(name = "Parameters_gap-extend", required = true)
	protected BigInteger gapExtend;
	@XmlElement(name = "Parameters_filter", required = true)
	protected String filter;
	@XmlElement(name = "Parameters_pattern")
	protected String pattern;
	@XmlElement(name = "Parameters_entrez-query")
	protected String entrezQuery;

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
}
