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
@XmlType(name = "", propOrder = {"iterNum", "search"})
@XmlRootElement(name = "Iteration")
public class Iteration {

	@XmlElement(name = "iter-num", required = true)
	protected BigInteger iterNum;

	@XmlElement(required = true)
	protected Iteration.Search search;

	public BigInteger getIterNum() {

		return iterNum;
	}

	public void setIterNum(BigInteger value) {

		this.iterNum = value;
	}

	public Iteration.Search getSearch() {

		return search;
	}

	public void setSearch(Iteration.Search value) {

		this.search = value;
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
