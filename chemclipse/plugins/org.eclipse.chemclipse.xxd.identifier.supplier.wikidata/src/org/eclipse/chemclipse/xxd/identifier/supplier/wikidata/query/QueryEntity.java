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

public class QueryEntity extends AbstractQuery {

	private static final Logger logger = Logger.getLogger(QueryEntity.class);

	public static String fromCAS(String cas) {

		String select = Wikidata.PROP + Wikidata.STATEMENT + Wikidata.WIKIBASE + Wikidata.BIGDATA + Wikidata.WDT + """
				SELECT DISTINCT ?item WHERE {
				  SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }
				  {
				    SELECT DISTINCT ?item WHERE {
				      ?item p:P231 ?cas.
				      ?cas (ps:P231) "%s".
				    }
				  }
				}
				""".formatted(cas);
		return query(select);
	}

	// TODO: case insensitive query that is not super slow
	public static String fromName(String name) {

		String select = Wikidata.PROP + Wikidata.BIGDATA + Wikidata.SCHEMA + Wikidata.WIKIBASE + Wikidata.WD + Wikidata.WDT + """
				SELECT distinct ?item WHERE {
				  ?item ?label "%s"@en .
				  ?item wdt:P31 wd:Q113145171 .
				  ?article schema:about ?item .
				  ?article schema:inLanguage "en" .
				  SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }
				}
				""".formatted(name.toLowerCase());
		return query(select);
	}

	private static String query(String queryString) {

		String url;
		try {
			url = Wikidata.ENDPOINT + "?query=" + java.net.URLEncoder.encode(queryString, StandardCharsets.UTF_8);
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
				JsonNode picNode = binding.path("item").path("value");
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
