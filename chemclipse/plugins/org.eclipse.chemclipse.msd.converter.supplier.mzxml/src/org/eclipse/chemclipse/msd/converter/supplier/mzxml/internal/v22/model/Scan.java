/*******************************************************************************
 * Copyright (c) 2015 Dr. Philip Wenig.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"scanOrigin", "precursorMz", "maldi", "peaks", "nameValueAndComment", "scan"})
@XmlRootElement(name = "scan")
public class Scan implements Serializable {

	private final static long serialVersionUID = 220L;
	protected List<ScanOrigin> scanOrigin;
	protected List<PrecursorMz> precursorMz;
	protected Maldi maldi;
	@XmlElement(required = true, nillable = true)
	protected Peaks peaks;
	@XmlElements({@XmlElement(name = "nameValue", type = NameValue.class), @XmlElement(name = "comment", type = String.class)})
	protected List<Serializable> nameValueAndComment;
	protected List<Scan> scan;
	@XmlAttribute(name = "num", required = true)
	@XmlSchemaType(name = "positiveInteger")
	protected BigInteger num;
	@XmlAttribute(name = "msLevel", required = true)
	@XmlSchemaType(name = "positiveInteger")
	protected BigInteger msLevel;
	@XmlAttribute(name = "peaksCount", required = true)
	@XmlSchemaType(name = "nonNegativeInteger")
	protected BigInteger peaksCount;
	@XmlAttribute(name = "polarity")
	protected String polarity;
	@XmlAttribute(name = "scanType")
	protected String scanType;
	@XmlAttribute(name = "centroided")
	protected Boolean centroided;
	@XmlAttribute(name = "deisotoped")
	protected Boolean deisotoped;
	@XmlAttribute(name = "chargeDeconvoluted")
	protected Boolean chargeDeconvoluted;
	@XmlAttribute(name = "retentionTime")
	protected Duration retentionTime;
	@XmlAttribute(name = "ionisationEnergy")
	protected Float ionisationEnergy;
	@XmlAttribute(name = "collisionEnergy")
	protected Float collisionEnergy;
	@XmlAttribute(name = "cidGasPressure")
	protected Float cidGasPressure;
	@XmlAttribute(name = "startMz")
	protected Float startMz;
	@XmlAttribute(name = "endMz")
	protected Float endMz;
	@XmlAttribute(name = "lowMz")
	protected Float lowMz;
	@XmlAttribute(name = "highMz")
	protected Float highMz;
	@XmlAttribute(name = "basePeakMz")
	protected Float basePeakMz;
	@XmlAttribute(name = "basePeakIntensity")
	protected Float basePeakIntensity;
	@XmlAttribute(name = "totIonCurrent")
	protected Float totIonCurrent;

	public List<ScanOrigin> getScanOrigin() {

		if(scanOrigin == null) {
			scanOrigin = new ArrayList<ScanOrigin>();
		}
		return this.scanOrigin;
	}

	public List<PrecursorMz> getPrecursorMz() {

		if(precursorMz == null) {
			precursorMz = new ArrayList<PrecursorMz>();
		}
		return this.precursorMz;
	}

	public Maldi getMaldi() {

		return maldi;
	}

	public void setMaldi(Maldi value) {

		this.maldi = value;
	}

	public Peaks getPeaks() {

		return peaks;
	}

	public void setPeaks(Peaks value) {

		this.peaks = value;
	}

	public List<Serializable> getNameValueAndComment() {

		if(nameValueAndComment == null) {
			nameValueAndComment = new ArrayList<Serializable>();
		}
		return this.nameValueAndComment;
	}

	public List<Scan> getScan() {

		if(scan == null) {
			scan = new ArrayList<Scan>();
		}
		return this.scan;
	}

	public BigInteger getNum() {

		return num;
	}

	public void setNum(BigInteger value) {

		this.num = value;
	}

	public BigInteger getMsLevel() {

		return msLevel;
	}

	public void setMsLevel(BigInteger value) {

		this.msLevel = value;
	}

	public BigInteger getPeaksCount() {

		return peaksCount;
	}

	public void setPeaksCount(BigInteger value) {

		this.peaksCount = value;
	}

	public String getPolarity() {

		return polarity;
	}

	public void setPolarity(String value) {

		this.polarity = value;
	}

	public String getScanType() {

		return scanType;
	}

	public void setScanType(String value) {

		this.scanType = value;
	}

	public Boolean isCentroided() {

		return centroided;
	}

	public void setCentroided(Boolean value) {

		this.centroided = value;
	}

	public Boolean isDeisotoped() {

		return deisotoped;
	}

	public void setDeisotoped(Boolean value) {

		this.deisotoped = value;
	}

	public boolean isChargeDeconvoluted() {

		if(chargeDeconvoluted == null) {
			return false;
		} else {
			return chargeDeconvoluted;
		}
	}

	public void setChargeDeconvoluted(Boolean value) {

		this.chargeDeconvoluted = value;
	}

	public Duration getRetentionTime() {

		return retentionTime;
	}

	public void setRetentionTime(Duration value) {

		this.retentionTime = value;
	}

	public Float getIonisationEnergy() {

		return ionisationEnergy;
	}

	public void setIonisationEnergy(Float value) {

		this.ionisationEnergy = value;
	}

	public Float getCollisionEnergy() {

		return collisionEnergy;
	}

	public void setCollisionEnergy(Float value) {

		this.collisionEnergy = value;
	}

	public Float getCidGasPressure() {

		return cidGasPressure;
	}

	public void setCidGasPressure(Float value) {

		this.cidGasPressure = value;
	}

	public Float getStartMz() {

		return startMz;
	}

	public void setStartMz(Float value) {

		this.startMz = value;
	}

	public Float getEndMz() {

		return endMz;
	}

	public void setEndMz(Float value) {

		this.endMz = value;
	}

	public Float getLowMz() {

		return lowMz;
	}

	public void setLowMz(Float value) {

		this.lowMz = value;
	}

	public Float getHighMz() {

		return highMz;
	}

	public void setHighMz(Float value) {

		this.highMz = value;
	}

	public Float getBasePeakMz() {

		return basePeakMz;
	}

	public void setBasePeakMz(Float value) {

		this.basePeakMz = value;
	}

	public Float getBasePeakIntensity() {

		return basePeakIntensity;
	}

	public void setBasePeakIntensity(Float value) {

		this.basePeakIntensity = value;
	}

	public Float getTotIonCurrent() {

		return totIonCurrent;
	}

	public void setTotIonCurrent(Float value) {

		this.totIonCurrent = value;
	}
}
