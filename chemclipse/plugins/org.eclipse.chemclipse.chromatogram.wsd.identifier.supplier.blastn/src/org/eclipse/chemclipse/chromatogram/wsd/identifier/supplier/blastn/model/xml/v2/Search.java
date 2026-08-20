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
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"queryId", "queryTitle", "queryLen", "queryMasking", "hits", "stat", "message"})
@XmlRootElement(name = "Search")
public class Search {

	@XmlElement(name = "query-id")
	protected String queryId;

	@XmlElement(name = "query-title")
	protected String queryTitle;

	@XmlElement(name = "query-len")
	protected BigInteger queryLen;

	@XmlElement(name = "query-masking")
	protected Search.QueryMasking queryMasking;

	protected Search.Hits hits;

	protected Search.Stat stat;

	protected String message;

	public String getQueryId() {

		return queryId;
	}

	public void setQueryId(String value) {

		this.queryId = value;
	}

	public String getQueryTitle() {

		return queryTitle;
	}

	public void setQueryTitle(String value) {

		this.queryTitle = value;
	}

	public BigInteger getQueryLen() {

		return queryLen;
	}

	public void setQueryLen(BigInteger value) {

		this.queryLen = value;
	}

	public Search.QueryMasking getQueryMasking() {

		return queryMasking;
	}

	public void setQueryMasking(Search.QueryMasking value) {

		this.queryMasking = value;
	}

	public Search.Hits getHits() {

		return hits;
	}

	public void setHits(Search.Hits value) {

		this.hits = value;
	}

	public Search.Stat getStat() {

		return stat;
	}

	public void setStat(Search.Stat value) {

		this.stat = value;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String value) {

		this.message = value;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"hit"})
	public static class Hits {

		@XmlElement(name = "Hit")
		protected List<Hit> hit;

		public List<Hit> getHit() {

			if(hit == null) {
				hit = new ArrayList<>();
			}
			return this.hit;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"range"})
	public static class QueryMasking {

		@XmlElement(name = "Range")
		protected List<Range> range;

		public List<Range> getRange() {

			if(range == null) {
				range = new ArrayList<>();
			}
			return this.range;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"statistics"})
	public static class Stat {

		@XmlElement(name = "Statistics", required = true)
		protected Statistics statistics;

		public Statistics getStatistics() {

			return statistics;
		}

		public void setStatistics(Statistics value) {

			this.statistics = value;
		}
	}
}
