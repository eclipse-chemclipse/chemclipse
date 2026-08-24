/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.BlastOutput2;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.Task;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.WebIdentifierSettings;
import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Version;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;

public class WebNucleotideBLAST extends AbstractNucleotideBLAST {

	private static final Logger logger = Logger.getLogger(WebNucleotideBLAST.class);

	public static int run(IChromatogramDSD chromatogram, WebIdentifierSettings settings) throws IOException, InterruptedException {

		List<NameValuePair> parameters = buildPostData(settings, getFASTA(chromatogram));
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String rid = submitSearch(client, settings, parameters);
			if(rid != null) {
				String xml = retrieveXML(client, rid, settings);
				try {
					InputSource inputSource = new InputSource(new StringReader(xml));
					BlastOutput2 blastOutput = XmlReaderVersion2.getBlastOutput(inputSource);
					transferTargets(chromatogram, blastOutput);
					return XmlReaderVersion2.getNumberResults(blastOutput);
				} catch(SAXException | IOException | JAXBException
						| ParserConfigurationException e) {
					logger.error(e);
					throw new IOException("Failed to read XML.");
				}
			}
		}

		return 0;
	}

	/**
	 * @return the response ID
	 */
	private static String submitSearch(CloseableHttpClient client, WebIdentifierSettings settings, List<NameValuePair> parameters) throws InterruptedException, IOException {

		HttpPost request = new HttpPost(settings.getEndpoint());
		request.setHeader("User-Agent", getUserAgent());
		request.setEntity(new UrlEncodedFormEntity(parameters, StandardCharsets.UTF_8));
		BasicHttpClientResponseHandler handler = new BasicHttpClientResponseHandler();
		return parseQueuedBlastInfo(client.execute(request, handler));
	}

	private static String parseQueuedBlastInfo(String response) throws InterruptedException {

		int rtoe = 10;
		String qBlastInfo = regexExtract(response, "<!--QBlastInfoBegin(.*?)QBlastInfoEnd\\s*-->", Pattern.DOTALL);
		if(qBlastInfo != null) {
			String rtoeStr = regexExtract(qBlastInfo, "RTOE = ([^\n\r]*)", 0);
			if(rtoeStr != null && !rtoeStr.trim().isEmpty()) {
				try {
					rtoe = Integer.parseInt(rtoeStr.trim());
				} catch(NumberFormatException e) {
					logger.warn(e);
				}
				Thread.sleep(rtoe * 1000L);
			}
			String rid = regexExtract(qBlastInfo, "RID = ([^\n\r]*)", 0);
			if(rid != null) {
				return rid.trim();
			}
		} else {
			logger.error("QBlastInfo not found");
		}
		return null;
	}

	private static List<NameValuePair> buildPostData(WebIdentifierSettings settings, String fasta) {

		List<NameValuePair> parameters = new ArrayList<>();

		parameters.add(new BasicNameValuePair("CMD", "Put"));
		parameters.add(new BasicNameValuePair("PROGRAM", "blastn"));
		parameters.add(new BasicNameValuePair("BLAST_PROGRAMS", getProgram(settings)));
		parameters.add(new BasicNameValuePair("DATABASE", settings.getDatabase()));
		parameters.add(new BasicNameValuePair("QUERY", fasta));

		String entryQuery = createEntrezQuery(settings);
		if(!entryQuery.isBlank()) {
			parameters.add(new BasicNameValuePair("ENTREZ_QUERY", entryQuery));
		}

		return parameters;
	}

	public static String createEntrezQuery(WebIdentifierSettings settings) {

		if(!settings.isOnlyTypeMaterial() && !settings.isExcludeModels() && !settings.isExcludeUncultured()) {
			return "";
		}

		String positive = settings.isOnlyTypeMaterial() ? "sequence_from_type[filter]" : "all [filter]";

		List<String> negative = new ArrayList<>();
		if(settings.isExcludeModels()) {
			negative.add("XM_000001:XM_9999999[pacc]");
			negative.add("XM_000000001:XM_999999999[pacc]");
			negative.add("XR_000000001:XR_999999999[pacc]");
		}
		if(settings.isExcludeUncultured()) {
			negative.add("(environmental samples[organism] OR metagenomes[orgn] OR txid32644[orgn])");
			negative.add("env [DIV]");
		}

		StringBuilder builder = new StringBuilder(positive);
		if(!negative.isEmpty()) {
			builder.append(" NOT(").append(String.join(" OR ", negative)).append(')');
		}
		return builder.toString();
	}

	private static String getProgram(WebIdentifierSettings settings) {

		switch(settings.getTask()) {
			case Task.BLASTN: {
				return "blastn";
			}
			case Task.MEGABLAST: {
				return "megaBlast";
			}
			case Task.DC_MEGABLAST: {
				return "discoMegablast";
			}
			default:
				return "blastn";
		}
	}

	private static String regexExtract(String input, String patternStr, int flags) {

		Pattern pattern = Pattern.compile(patternStr, flags);
		Matcher matcher = pattern.matcher(input);
		if(matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static String getFASTA(IChromatogramDSD chromatogram) {

		StringBuilder stringBuilder = new StringBuilder("> " + chromatogram.getSampleName() + "\n");
		stringBuilder.append(chromatogram.getNucleotideSequence());
		return stringBuilder.toString();
	}

	private static String retrieveXML(CloseableHttpClient client, String rid, WebIdentifierSettings settings) throws IOException, InterruptedException {

		while(true) {
			Thread.sleep(10000);
			String pollResponse = "";
			BasicHttpClientResponseHandler handler = new BasicHttpClientResponseHandler();
			String pollRequest = settings.getEndpoint() + "?CMD=Get&FORMAT_OBJECT=SearchInfo&RID=" + rid;
			HttpGet httpGet = new HttpGet(pollRequest);
			httpGet.setHeader("User-Agent", getUserAgent());
			pollResponse = client.execute(httpGet, handler);

			if(pollResponse.matches("(?s).*\\s+Status=WAITING.*")) {
				continue;
			}

			if(pollResponse.matches("(?s).*\\s+Status=FAILED.*")) {
				logger.error("Search " + rid + " failed.");
			}

			if(pollResponse.matches("(?s).*\\s+Status=UNKNOWN.*")) {
				logger.error("Search " + rid + " expired.");
			}

			if(pollResponse.matches("(?s).*\\s+Status=READY.*")) {
				if(pollResponse.matches("(?s).*\\s+ThereAreHits=yes.*")) {
					break;
				} else {
					logger.error("No hits found.");
				}
			}

			logger.error("Unknown error.");
		}

		BasicHttpClientResponseHandler handler = new BasicHttpClientResponseHandler();
		String getRequest = settings.getEndpoint() + "?CMD=Get&FORMAT_TYPE=XML2_S&RID=" + rid;
		HttpGet httpGet = new HttpGet(getRequest);
		httpGet.setHeader("User-Agent", getUserAgent());
		return client.execute(httpGet, handler);
	}

	private static String getUserAgent() {

		IProduct product = Platform.getProduct();
		Version version = product.getDefiningBundle().getVersion();
		return product.getName() + "/" + version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
	}
}
