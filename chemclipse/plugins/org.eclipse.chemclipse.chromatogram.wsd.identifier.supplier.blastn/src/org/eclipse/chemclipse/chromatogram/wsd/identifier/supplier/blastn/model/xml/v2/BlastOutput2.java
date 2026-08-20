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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"report", "error"})
@XmlRootElement(name = "BlastOutput2")
public class BlastOutput2 {

	protected BlastOutput2.Report report;
	protected BlastOutput2.Error error;

	public BlastOutput2.Report getReport() {

		return report;
	}

	public void setReport(BlastOutput2.Report value) {

		this.report = value;
	}

	public BlastOutput2.Error getError() {

		return error;
	}

	public void setError(BlastOutput2.Error value) {

		this.error = value;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"err"})
	public static class Error {

		@XmlElement(name = "Err", required = true)
		protected Err err;

		public Err getErr() {

			return err;
		}

		public void setErr(Err value) {

			this.err = value;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"report"})
	public static class Report {

		@XmlElement(name = "Report", required = true)
		protected org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report report;

		public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report getReport() {

			return report;
		}

		public void setReport(org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report value) {

			this.report = value;
		}
	}
}
