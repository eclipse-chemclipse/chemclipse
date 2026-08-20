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

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"iterations", "search", "bl2Seq"})
@XmlRootElement(name = "Results")
public class Results {

	protected Results.Iterations iterations;

	protected Results.Search search;

	@XmlElement(name = "bl2seq")
	protected Results.Bl2Seq bl2Seq;

	public Results.Iterations getIterations() {

		return iterations;
	}

	public void setIterations(Results.Iterations value) {

		this.iterations = value;
	}

	public Results.Search getSearch() {

		return search;
	}

	public void setSearch(Results.Search value) {

		this.search = value;
	}

	public Results.Bl2Seq getBl2Seq() {

		return bl2Seq;
	}

	public void setBl2Seq(Results.Bl2Seq value) {

		this.bl2Seq = value;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"search"})
	public static class Bl2Seq {

		@XmlElement(name = "Search")
		protected List<org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search> search;

		public List<org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search> getSearch() {

			if(search == null) {
				search = new ArrayList<>();
			}
			return this.search;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"iteration"})
	public static class Iterations {

		@XmlElement(name = "Iteration")
		protected List<Iteration> iteration;

		public List<Iteration> getIteration() {

			if(iteration == null) {
				iteration = new ArrayList<>();
			}
			return this.iteration;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"search"})
	public static class Search {

		@XmlElement(name = "Search", required = true)
		protected org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search search;

		public org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search getSearch() {

			return search;
		}

		public void setSearch(org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search value) {

			this.search = value;
		}
	}
}
