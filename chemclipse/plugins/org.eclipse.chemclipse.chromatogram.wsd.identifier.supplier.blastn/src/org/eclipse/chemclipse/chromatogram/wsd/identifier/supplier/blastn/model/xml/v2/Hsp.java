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
@XmlType(name = "", propOrder = {"num", "bitScore", "score", "evalue", "identity", "positive", "density", "patternFrom", "patternTo", "queryFrom", "queryTo", "queryStrand", "queryFrame", "hitFrom", "hitTo", "hitStrand", "hitFrame", "alignLen", "gaps", "qseq", "hseq", "midline"})
@XmlRootElement(name = "Hsp")
public class Hsp {

	@XmlElement(required = true)
	protected BigInteger num;

	@XmlElement(name = "bit-score")
	protected double bitScore;

	protected double score;

	protected double evalue;

	protected BigInteger identity;

	protected BigInteger positive;

	protected BigInteger density;

	@XmlElement(name = "pattern-from")
	protected BigInteger patternFrom;

	@XmlElement(name = "pattern-to")
	protected BigInteger patternTo;

	@XmlElement(name = "query-from", required = true)
	protected BigInteger queryFrom;

	@XmlElement(name = "query-to", required = true)
	protected BigInteger queryTo;

	@XmlElement(name = "query-strand")
	protected String queryStrand;

	@XmlElement(name = "query-frame")
	protected BigInteger queryFrame;

	@XmlElement(name = "hit-from", required = true)
	protected BigInteger hitFrom;

	@XmlElement(name = "hit-to", required = true)
	protected BigInteger hitTo;

	@XmlElement(name = "hit-strand")
	protected String hitStrand;

	@XmlElement(name = "hit-frame")
	protected BigInteger hitFrame;

	@XmlElement(name = "align-len")
	protected BigInteger alignLen;

	protected BigInteger gaps;

	@XmlElement(required = true)
	protected String qseq;

	@XmlElement(required = true)
	protected String hseq;

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

	public BigInteger getDensity() {

		return density;
	}

	public void setDensity(BigInteger value) {

		this.density = value;
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

	public String getQueryStrand() {

		return queryStrand;
	}

	public void setQueryStrand(String value) {

		this.queryStrand = value;
	}

	public BigInteger getQueryFrame() {

		return queryFrame;
	}

	public void setQueryFrame(BigInteger value) {

		this.queryFrame = value;
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

	public String getHitStrand() {

		return hitStrand;
	}

	public void setHitStrand(String value) {

		this.hitStrand = value;
	}

	public BigInteger getHitFrame() {

		return hitFrame;
	}

	public void setHitFrame(BigInteger value) {

		this.hitFrame = value;
	}

	public BigInteger getAlignLen() {

		return alignLen;
	}

	public void setAlignLen(BigInteger value) {

		this.alignLen = value;
	}

	public BigInteger getGaps() {

		return gaps;
	}

	public void setGaps(BigInteger value) {

		this.gaps = value;
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
