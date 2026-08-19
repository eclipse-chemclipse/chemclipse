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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.BlastOutput;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.Task;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.WebIdentifierSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Version;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;

public class WebNucleotideBLAST extends AbstractNucleotideBLAST {

	private static final Logger logger = Logger.getLogger(WebNucleotideBLAST.class);

	public static int run(IChromatogramWSD chromatogram, WebIdentifierSettings settings) throws IOException, InterruptedException {

		String postData = buildPostData(settings, getFASTA(chromatogram)).toString();
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String rid = submitSearch(client, settings, postData);
			if(rid != null) {
				String xml = retrieveXML(client, rid, settings);
				try {
					InputSource inputSource = new InputSource(new StringReader(xml));
					BlastOutput blastOutput = XmlReaderVersion1.getBlastOutput(inputSource);
					transferTargets(chromatogram, blastOutput);
					return blastOutput.getIterations().getIteration().stream() //
										.mapToInt(iteration -> iteration.getHits().getHit().size()) //
										.sum();
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
	private static String submitSearch(CloseableHttpClient client, WebIdentifierSettings settings, String postData) throws InterruptedException, IOException {

		HttpPost request = new HttpPost(settings.getEndpoint());
		request.setHeader("User-Agent", getUserAgent());
		request.setEntity(new StringEntity(postData, ContentType.APPLICATION_FORM_URLENCODED));
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

	private static StringBuilder buildPostData(WebIdentifierSettings settings, String fasta) {

		StringBuilder stringBuilder = new StringBuilder("CMD=Put");
		stringBuilder.append("&PROGRAM=");
		stringBuilder.append(getProgram(settings));
		stringBuilder.append("&DATABASE=");
		stringBuilder.append(settings.getDatabase());
		stringBuilder.append("&QUERY=");
		stringBuilder.append(fasta);
		return stringBuilder;
	}

	private static String getProgram(WebIdentifierSettings settings) {

		if(settings.getTask() == Task.MEGABLAST) {
			return "blastn&MEGABLAST=on";
		}
		return settings.getTask().value();
	}

	private static String regexExtract(String input, String patternStr, int flags) {

		Pattern pattern = Pattern.compile(patternStr, flags);
		Matcher matcher = pattern.matcher(input);
		if(matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static String getFASTA(IChromatogramWSD chromatogram) {

		StringBuilder stringBuilder = new StringBuilder("> " + chromatogram.getSampleName() + "\n");
		stringBuilder.append(chromatogram.getMiscInfo());
		return URLEncoder.encode(stringBuilder.toString(), StandardCharsets.UTF_8);
	}

	private static String retrieveXML(CloseableHttpClient client, String rid, WebIdentifierSettings settings) throws IOException, InterruptedException {

		while(true) {
			Thread.sleep(5000);
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
		String getRequest = settings.getEndpoint() + "?CMD=Get&FORMAT_TYPE=XML&RID=" + rid;
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
