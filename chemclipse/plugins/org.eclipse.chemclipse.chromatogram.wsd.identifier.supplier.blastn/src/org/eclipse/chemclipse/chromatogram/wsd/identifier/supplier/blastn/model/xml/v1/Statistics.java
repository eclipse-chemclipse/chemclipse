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
@XmlType(name = "", propOrder = {"dbNum", "dbLen", "hspLen", "effSpace", "kappa", "lambda", "entropy"})
@XmlRootElement(name = "Statistics")
public class Statistics {

	@XmlElement(name = "Statistics_db-num", required = true)
	protected BigInteger dbNum;
	@XmlElement(name = "Statistics_db-len", required = true)
	protected long dbLen;
	@XmlElement(name = "Statistics_hsp-len", required = true)
	protected BigInteger hspLen;
	@XmlElement(name = "Statistics_eff-space", required = true)
	protected double effSpace;
	@XmlElement(name = "Statistics_kappa", required = true)
	protected double kappa;
	@XmlElement(name = "Statistics_lambda", required = true)
	protected double lambda;
	@XmlElement(name = "Statistics_entropy", required = true)
	protected double entropy;

	public BigInteger getDbNum() {

		return dbNum;
	}

	public void setDbNum(BigInteger value) {

		this.dbNum = value;
	}

	public long getDbLen() {

		return dbLen;
	}

	public void setDbLen(long value) {

		this.dbLen = value;
	}

	public BigInteger getHspLen() {

		return hspLen;
	}

	public void setHspLen(BigInteger value) {

		this.hspLen = value;
	}

	public double getEffSpace() {

		return effSpace;
	}

	public void setEffSpace(double value) {

		this.effSpace = value;
	}

	public double getKappa() {

		return kappa;
	}

	public void setKappa(double value) {

		this.kappa = value;
	}

	public double getLambda() {

		return lambda;
	}

	public void setLambda(double value) {

		this.lambda = value;
	}

	public double getEntropy() {

		return entropy;
	}

	public void setEntropy(double value) {

		this.entropy = value;
	}
}
