/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.model.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.support.settings.OperatingSystemUtils;
import org.eclipse.chemclipse.tsd.model.support.TraceRangeSupport;
import org.eclipse.chemclipse.tsd.model.validators.TraceRangeValidator;
import org.eclipse.core.runtime.IStatus;

public class TraceRanges extends ArrayList<TraceRange> {

	private static final long serialVersionUID = 8589998690167624026L;
	//
	public static final String DESCRIPTION = "Trace Range Definitions";
	public static final String FILE_EXTENSION = ".trd";
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION;
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION;
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")";
	//
	private static final String WHITE_SPACE = " ";
	private static final String SEPARATOR_TOKEN = ";";
	private static final String SEPARATOR_ENTRY = "|";
	//
	private static final Logger logger = Logger.getLogger(TraceRanges.class);

	public void load(String items) {

		loadRules(items);
	}

	public String save() {

		return extractRanges(SEPARATOR_TOKEN);
	}

	public TraceRange extractTraceRange(String item) {

		return extract(item);
	}

	public String extractTraceRange(TraceRange traceRange) {

		return getTraceRangeAsString(traceRange);
	}

	public void importRules(File file) {

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line;
			while((line = bufferedReader.readLine()) != null) {
				TraceRange stackRange = extract(line);
				if(stackRange != null) {
					add(stackRange);
				}
			}
		} catch(FileNotFoundException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}
	}

	public boolean exportRules(File file) {

		boolean success = false;
		try (PrintWriter printWriter = new PrintWriter(file)) {
			String content = extractRanges(OperatingSystemUtils.getLineDelimiter());
			printWriter.println(content);
			printWriter.flush();
			success = true;
		} catch(FileNotFoundException e) {
			logger.warn(e);
		}
		//
		return success;
	}

	private String extractRanges(String rangeSeparator) {

		StringBuilder builder = new StringBuilder();
		Iterator<TraceRange> iterator = iterator();
		//
		while(iterator.hasNext()) {
			TraceRange traceRange = iterator.next();
			builder.append(getTraceRangeAsString(traceRange));
			if(iterator.hasNext()) {
				builder.append(rangeSeparator);
			}
		}
		//
		return builder.toString().trim();
	}

	private String getTraceRangeAsString(TraceRange traceRange) {

		StringBuilder builder = new StringBuilder();
		//
		if(traceRange != null) {
			builder.append(TraceRangeSupport.DF_COLUMN_1_MINUTES.format(traceRange.getRetentionTimeColumn1Start() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
			addSeparator(builder);
			builder.append(TraceRangeSupport.DF_COLUMN_1_MINUTES.format(traceRange.getRetentionTimeColumn1Stop() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
			addSeparator(builder);
			builder.append(TraceRangeSupport.DF_COLUMN_2_SECONDS.format(traceRange.getRetentionTimeColumn2Start() / IChromatogramOverview.SECOND_CORRELATION_FACTOR));
			addSeparator(builder);
			builder.append(TraceRangeSupport.DF_COLUMN_2_SECONDS.format(traceRange.getRetentionTimeColumn2Stop() / IChromatogramOverview.SECOND_CORRELATION_FACTOR));
			addSeparator(builder);
			builder.append(traceRange.getScanIndicesColumn2());
			addSeparator(builder);
			builder.append(traceRange.getName());
			addSeparator(builder);
			builder.append(traceRange.getTraces());
		}
		//
		return builder.toString();
	}

	private TraceRange extract(String text) {

		TraceRange stackRange = null;
		TraceRangeValidator validator = new TraceRangeValidator();
		//
		IStatus status = validator.validate(text);
		if(status.isOK()) {
			stackRange = validator.getSetting();
		} else {
			logger.warn(status.getMessage());
		}
		//
		return stackRange;
	}

	private void loadRules(String input) {

		String[] lines;
		if(input.contains(SEPARATOR_TOKEN)) {
			lines = input.split(SEPARATOR_TOKEN);
		} else {
			lines = new String[1];
			lines[0] = input;
		}
		//
		for(String line : lines) {
			TraceRange stackRange = extract(line);
			if(stackRange != null && !contains(stackRange)) {
				add(stackRange);
			}
		}
	}

	private void addSeparator(StringBuilder builder) {

		builder.append(WHITE_SPACE);
		builder.append(SEPARATOR_ENTRY);
		builder.append(WHITE_SPACE);
	}
}