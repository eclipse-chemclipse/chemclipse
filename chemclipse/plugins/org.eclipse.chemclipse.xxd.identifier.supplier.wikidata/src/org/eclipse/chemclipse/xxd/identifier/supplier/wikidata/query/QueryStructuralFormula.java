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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.eclipse.chemclipse.logging.core.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryStructuralFormula extends AbstractQuery {

	private static final Logger logger = Logger.getLogger(QueryStructuralFormula.class);

	private QueryStructuralFormula() {

	}

	public static String fromName(String name) {

		String select = Wikidata.PROP + Wikidata.RDFS + Wikidata.SKOS + Wikidata.WIKIBASE + Wikidata.BIGDATA + Wikidata.WDT + """
				SELECT ?item ?itemLabel ?pic
				WHERE {
				  {
				    ?item rdfs:label "%s"@en.
				  }
				  UNION
				  {
				    ?item skos:altLabel "%s"@en.
				  }
				  ?item wdt:P117 ?pic
				  SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }
				}
				""".formatted(name, name);
		return query(select);
	}

	public static String fromCAS(String cas) {

		String select = Wikidata.PROP + Wikidata.STATEMENT + Wikidata.WIKIBASE + Wikidata.BIGDATA + Wikidata.WDT + """
				SELECT ?item ?itemLabel ?pic WHERE {
				  ?item p:P231 ?_cas.
				  ?_cas (ps:P231) "%s".
				  ?item wdt:P117 ?pic
				  SERVICE wikibase:label { bd:serviceParam wikibase:language "[AUTO_LANGUAGE],en" }
				}
				""".formatted(cas);
		return query(select);
	}

	public static String fromSMILES(String smiles) {

		String select = Wikidata.PROP + Wikidata.STATEMENT + Wikidata.WIKIBASE + Wikidata.BIGDATA + Wikidata.WDT + """
				SELECT ?item ?itemLabel ?pic WHERE {
				  ?item p:P233 ?_smiles.
				  ?_smiles (ps:P233) "%s".
				  ?item wdt:P117 ?pic
				  SERVICE wikibase:label { bd:serviceParam wikibase:language "[AUTO_LANGUAGE],en" }
				}
				""".formatted(smiles);
		return query(select);
	}

	public static String fromInChI(String inchi) {

		String select = Wikidata.PROP + Wikidata.STATEMENT + Wikidata.WIKIBASE + Wikidata.BIGDATA + Wikidata.WDT + """
				SELECT ?item ?itemLabel ?pic WHERE {
				  ?item p:P234 ?_inchi.
				  ?_inchi (ps:P234) "%s".
				  ?item wdt:P117 ?pic
				  SERVICE wikibase:label { bd:serviceParam wikibase:language "[AUTO_LANGUAGE],en" }
				}
				""".formatted(inchi);
		return query(select);
	}

	public static String fromInChIKey(String inchiKey) {

		String select = Wikidata.PROP + Wikidata.STATEMENT + Wikidata.WIKIBASE + Wikidata.BIGDATA + Wikidata.WDT + """
				SELECT ?item ?itemLabel ?pic WHERE {
				  ?item p:P235 ?_inchiKey.
				  ?_inchiKey (ps:P235) "%s".
				  ?item wdt:P117 ?pic
				  SERVICE wikibase:label { bd:serviceParam wikibase:language "[AUTO_LANGUAGE],en" }
				}
				""".formatted(inchiKey);
		return query(select);
	}

	private static String query(String queryString) {

		try {
			String url = Wikidata.ENDPOINT + "?query=" + java.net.URLEncoder.encode(queryString, StandardCharsets.UTF_8);
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)) //
												.header("Accept", "application/json") //
												.header("User-Agent", getUserAgent()) //
												.build();

			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.body());
			JsonNode bindings = root.path("results").path("bindings");
			for(JsonNode binding : bindings) {
				JsonNode picNode = binding.path("pic").path("value");
				if(!picNode.isMissingNode()) {
					return picNode.asText();
				}
			}
		} catch(IOException e) {
			logger.error(e);
		} catch(InterruptedException e) {
			logger.error(e);
		}
		return null;
	}
}
