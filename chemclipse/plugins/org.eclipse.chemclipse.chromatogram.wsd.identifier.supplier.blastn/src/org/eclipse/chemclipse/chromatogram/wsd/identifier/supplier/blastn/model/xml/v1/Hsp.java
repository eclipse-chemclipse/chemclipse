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
@XmlType(name = "", propOrder = {"num", "bitScore", "score", "evalue", "queryFrom", "queryTo", "hitFrom", "hitTo", "patternFrom", "patternTo", "queryFrame", "hitFrame", "identity", "positive", "gaps", "alignLen", "density", "qseq", "hseq", "midline"})
@XmlRootElement(name = "Hsp")
public class Hsp {

	@XmlElement(name = "Hsp_num", required = true)
	protected BigInteger num;
	@XmlElement(name = "Hsp_bit-score", required = true)
	protected double bitScore;
	@XmlElement(name = "Hsp_score", required = true)
	protected double score;
	@XmlElement(name = "Hsp_evalue", required = true)
	protected double evalue;
	@XmlElement(name = "Hsp_query-from", required = true)
	protected BigInteger queryFrom;
	@XmlElement(name = "Hsp_query-to", required = true)
	protected BigInteger queryTo;
	@XmlElement(name = "Hsp_hit-from", required = true)
	protected BigInteger hitFrom;
	@XmlElement(name = "Hsp_hit-to", required = true)
	protected BigInteger hitTo;
	@XmlElement(name = "Hsp_pattern-from")
	protected BigInteger patternFrom;
	@XmlElement(name = "Hsp_pattern-to")
	protected BigInteger patternTo;
	@XmlElement(name = "Hsp_query-frame")
	protected BigInteger queryFrame;
	@XmlElement(name = "Hsp_hit-frame")
	protected BigInteger hitFrame;
	@XmlElement(name = "Hsp_identity", required = true)
	protected BigInteger identity;
	@XmlElement(name = "Hsp_positive", required = true)
	protected BigInteger positive;
	@XmlElement(name = "Hsp_gaps", required = true)
	protected BigInteger gaps;
	@XmlElement(name = "Hsp_align-len", required = true)
	protected BigInteger alignLen;
	@XmlElement(name = "Hsp_density")
	protected BigInteger density;
	@XmlElement(name = "Hsp_qseq", required = true)
	protected String qseq;
	@XmlElement(name = "Hsp_hseq", required = true)
	protected String hseq;
	@XmlElement(name = "Hsp_midline")
	protected String midline;

	public BigInteger getNum() {

		return num;
	}

	public void setNum(BigInteger value) {

		this.num = value;
	}

	public double getBitScore() {

		return bitScore;
	}

	public void setBitScore(double value) {

		this.bitScore = value;
	}

	public double getScore() {

		return score;
	}

	public void setScore(double value) {

		this.score = value;
	}

	public double getEvalue() {

		return evalue;
	}

	public void setEvalue(double value) {

		this.evalue = value;
	}

	public BigInteger getQueryFrom() {

		return queryFrom;
	}

	public void setQueryFrom(BigInteger value) {

		this.queryFrom = value;
	}

	public BigInteger getQueryTo() {

		return queryTo;
	}

	public void setQueryTo(BigInteger value) {

		this.queryTo = value;
	}

	public BigInteger getHitFrom() {

		return hitFrom;
	}

	public void setHitFrom(BigInteger value) {

		this.hitFrom = value;
	}

	public BigInteger getHitTo() {

		return hitTo;
	}

	public void setHitTo(BigInteger value) {

		this.hitTo = value;
	}

	public BigInteger getPatternFrom() {

		return patternFrom;
	}

	public void setPatternFrom(BigInteger value) {

		this.patternFrom = value;
	}

	public BigInteger getPatternTo() {

		return patternTo;
	}

	public void setPatternTo(BigInteger value) {

		this.patternTo = value;
	}

	public BigInteger getQueryFrame() {

		return queryFrame;
	}

	public void setQueryFrame(BigInteger value) {

		this.queryFrame = value;
	}

	public BigInteger getHitFrame() {

		return hitFrame;
	}

	public void setHitFrame(BigInteger value) {

		this.hitFrame = value;
	}

	public BigInteger getIdentity() {

		return identity;
	}

	public void setIdentity(BigInteger value) {

		this.identity = value;
	}

	public BigInteger getPositive() {

		return positive;
	}

	public void setPositive(BigInteger value) {

		this.positive = value;
	}

	public BigInteger getGaps() {

		return gaps;
	}

	public void setGaps(BigInteger value) {

		this.gaps = value;
	}

	public BigInteger getAlignLen() {

		return alignLen;
	}

	public void setAlignLen(BigInteger value) {

		this.alignLen = value;
	}

	public BigInteger getDensity() {

		return density;
	}

	public void setDensity(BigInteger value) {

		this.density = value;
	}

	public String getQseq() {

		return qseq;
	}

	public void setQseq(String value) {

		this.qseq = value;
	}

	public String getHseq() {

		return hseq;
	}

	public void setHseq(String value) {

		this.hseq = value;
	}

	public String getMidline() {

		return midline;
	}

	public void setMidline(String value) {

		this.midline = value;
	}
}
