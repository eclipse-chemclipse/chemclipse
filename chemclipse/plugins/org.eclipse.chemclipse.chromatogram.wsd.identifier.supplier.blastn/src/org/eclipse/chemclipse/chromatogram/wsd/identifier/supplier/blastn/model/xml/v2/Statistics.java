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
@XmlType(name = "", propOrder = {"dbNum", "dbLen", "hspLen", "effSpace", "kappa", "lambda", "entropy"})
@XmlRootElement(name = "Statistics")
public class Statistics {

	@XmlElement(name = "db-num")
	protected Long dbNum;

	@XmlElement(name = "db-len")
	protected Long dbLen;

	@XmlElement(name = "hsp-len", required = true)
	protected BigInteger hspLen;

	@XmlElement(name = "eff-space")
	protected long effSpace;

	protected double kappa;

	protected double lambda;

	protected double entropy;

	public Long getDbNum() {

		return dbNum;
	}

	public void setDbNum(Long value) {

		this.dbNum = value;
	}

	public Long getDbLen() {

		return dbLen;
	}

	public void setDbLen(Long value) {

		this.dbLen = value;
	}

	public BigInteger getHspLen() {

		return hspLen;
	}

	public void setHspLen(BigInteger value) {

		this.hspLen = value;
	}

	public long getEffSpace() {

		return effSpace;
	}

	public void setEffSpace(long value) {

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
