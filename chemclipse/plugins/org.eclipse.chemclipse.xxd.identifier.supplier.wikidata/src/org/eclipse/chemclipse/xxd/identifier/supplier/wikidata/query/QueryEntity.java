/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.identifier.supplier.wikidata.query;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class QueryEntity {

	public static String fromCAS(String cas) {

		String select = Wikidata.PROP + Wikidata.STATEMENT + Wikidata.WIKIBASE + Wikidata.BIGDATA + Wikidata.WDT + //
				"SELECT DISTINCT ?item WHERE {\n" //
				+ "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\". }\n" //
				+ "  {\n" //
				+ "    SELECT DISTINCT ?item WHERE {\n" //
				+ "      ?item p:P231 ?cas.\n" //
				+ "      ?cas (ps:P231) \"" + cas + "\".\n" //
				+ "    }\n" //
				+ "  }\n" //
				+ "}"; //
		return query(select);
	}

	// TODO: case insensitive query that is not super slow
	public static String fromName(String name) {

		String select = Wikidata.PROP + Wikidata.BIGDATA + Wikidata.SCHEMA + Wikidata.WIKIBASE + Wikidata.WD + Wikidata.WDT + //
				"SELECT distinct ?item WHERE { \n" + //
				"  ?item ?label \"" + name.toLowerCase() + "\"@en . \n" + //
				"  ?item wdt:P31 wd:Q113145171 . \n" + //
				"  ?article schema:about ?item . \n" + //
				"  ?article schema:inLanguage \"en\" . \n" + //
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\". }\n" + //
				"}";
		return query(select);
	}

	private static String query(String queryString) {

		Query query = QueryFactory.create(queryString);
		try (QueryExecution qexec = QueryExecutionFactory.sparqlService(Wikidata.ENDPOINT, query)) {
			ResultSet results = qexec.execSelect();
			while(results.hasNext()) {
				QuerySolution solution = results.next();
				RDFNode entity = solution.get("item");
				return entity.toString();
			}
		}
		return null;
	}
}
