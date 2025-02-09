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
@XmlType(name = "", propOrder = {"program", "version", "reference", "db", "queryID", "queryDef", "queryLen", "querySeq", "param", "iterations", "mbstat"})
@XmlRootElement(name = "BlastOutput")
public class BlastOutput {

	@XmlElement(name = "BlastOutput_program", required = true)
	protected String program;
	@XmlElement(name = "BlastOutput_version", required = true)
	protected String version;
	@XmlElement(name = "BlastOutput_reference", required = true)
	protected String reference;
	@XmlElement(name = "BlastOutput_db", required = true)
	protected String db;
	@XmlElement(name = "BlastOutput_query-ID", required = true)
	protected String queryID;
	@XmlElement(name = "BlastOutput_query-def", required = true)
	protected String queryDef;
	@XmlElement(name = "BlastOutput_query-len", required = true)
	protected BigInteger queryLen;
	@XmlElement(name = "BlastOutput_query-seq")
	protected String querySeq;
	@XmlElement(name = "BlastOutput_param", required = true)
	protected Param param;
	@XmlElement(name = "BlastOutput_iterations", required = true)
	protected Iterations iterations;
	@XmlElement(name = "BlastOutput_mbstat")
	protected Mbstat mbstat;

	public String getProgram() {

		return program;
	}

	public void setProgram(String value) {

		this.program = value;
	}

	public String getVersion() {

		return version;
	}

	public void setVersion(String value) {

		this.version = value;
	}

	public String getReference() {

		return reference;
	}

	public void setReference(String value) {

		this.reference = value;
	}

	public String getDb() {

		return db;
	}

	public void setDb(String value) {

		this.db = value;
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

	public String getQuerySeq() {

		return querySeq;
	}

	public void setQuerySeq(String value) {

		this.querySeq = value;
	}

	public Param getParam() {

		return param;
	}

	public void setParam(Param value) {

		this.param = value;
	}

	public Iterations getIterations() {

		return iterations;
	}

	public void setIterations(Iterations value) {

		this.iterations = value;
	}

	public Mbstat getMbstat() {

		return mbstat;
	}

	public void setMbstat(Mbstat value) {

		this.mbstat = value;
	}
}
