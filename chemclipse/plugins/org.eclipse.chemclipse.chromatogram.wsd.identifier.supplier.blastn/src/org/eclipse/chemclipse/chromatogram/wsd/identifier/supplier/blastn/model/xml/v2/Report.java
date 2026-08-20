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
@XmlType(name = "", propOrder = {"program", "version", "reference", "searchTarget", "params", "results"})
@XmlRootElement(name = "Report")
public class Report {

	@XmlElement(required = true)
	protected String program;

	@XmlElement(required = true)
	protected String version;

	@XmlElement(required = true)
	protected String reference;

	@XmlElement(name = "search-target", required = true)
	protected Report.SearchTarget searchTarget;

	@XmlElement(required = true)
	protected Report.Params params;

	@XmlElement(required = true)
	protected Report.Results results;

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

	public Report.SearchTarget getSearchTarget() {

		return searchTarget;
	}

	public void setSearchTarget(Report.SearchTarget value) {

		this.searchTarget = value;
	}

	public Report.Params getParams() {

		return params;
	}

	public void setParams(Report.Params value) {

		this.params = value;
	}

	public Report.Results getResults() {

		return results;
	}

	public void setResults(Report.Results value) {

		this.results = value;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"parameters"})
	public static class Params {

		@XmlElement(name = "Parameters", required = true)
		protected Parameters parameters;

		public Parameters getParameters() {

			return parameters;
		}

		public void setParameters(Parameters value) {

			this.parameters = value;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"results"})
	public static class Results {

		@XmlElement(name = "Results", required = true)
		protected org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results results;

		public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results getResults() {

			return results;
		}

		public void setResults(org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Results value) {

			this.results = value;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"target"})
	public static class SearchTarget {

		@XmlElement(name = "Target", required = true)
		protected Target target;

		public Target getTarget() {

			return target;
		}

		public void setTarget(Target value) {

			this.target = value;
		}
	}
}
