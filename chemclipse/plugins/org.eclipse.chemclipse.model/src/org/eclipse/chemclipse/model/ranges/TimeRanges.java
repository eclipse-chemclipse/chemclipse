/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.ranges;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.support.util.TimeRangeListUtil;
import org.eclipse.chemclipse.support.util.ValueParserSupport;

public class TimeRanges {

	private static final Logger logger = Logger.getLogger(TimeRanges.class);

	public static final String DESCRIPTION = "Time Ranges"; //$NON-NLS-1$
	public static final String FILE_EXTENSION = ".tra"; //$NON-NLS-1$
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION; //$NON-NLS-1$ //$NON-NLS-2$
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION; //$NON-NLS-1$
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")"; //$NON-NLS-1$ //$NON-NLS-2$

	private TimeRangeListUtil timeRangeListUtil = new TimeRangeListUtil();
	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.000"); //$NON-NLS-1$
	/*
	 * The map is used to have an easier access to the stored time ranges.
	 * To keep the map and time range instances in sync, this put method
	 * is used.
	 */
	private final Map<String, TimeRange> timeRangeMap = new HashMap<>();

	public TimeRanges() {

	}

	/**
	 * Initializes this ranges from the given settings.
	 * 
	 * @param timeRanges
	 */
	public TimeRanges(String timeRanges) {

		load(timeRanges);
	}

	public void addAll(Collection<TimeRange> timeRanges) {

		for(TimeRange timeRange : timeRanges) {
			add(timeRange);
		}
	}

	public void add(TimeRange timeRange) {

		timeRangeMap.put(timeRange.getIdentifier(), timeRange);
	}

	public void remove(String identifier) {

		timeRangeMap.remove(identifier);
	}

	public void remove(List<TimeRange> timeRanges) {

		for(TimeRange timeRange : timeRanges) {
			remove(timeRange);
		}
	}

	public void remove(TimeRange timeRange) {

		if(timeRange != null) {
			timeRangeMap.remove(timeRange.getIdentifier());
		}
	}

	public TimeRange get(String identifier) {

		return timeRangeMap.get(identifier);
	}

	public Set<String> keySet() {

		return timeRangeMap.keySet();
	}

	public Collection<TimeRange> values() {

		return timeRangeMap.values();
	}

	public void clear() {

		timeRangeMap.clear();
	}

	public String extractTimeRange(TimeRange timeRange) {

		StringBuilder builder = new StringBuilder();
		extractTimeRange(timeRange, builder);
		return builder.toString();
	}

	public TimeRange extractTimeRange(String item) {

		TimeRange timeRange = null;

		if(item != null && !item.isBlank()) {
			ValueParserSupport valueParserSupport = new ValueParserSupport();
			String[] values = item.split("\\" + TimeRangeListUtil.SEPARATOR_ENTRY); //$NON-NLS-1$
			String identifier = valueParserSupport.parseString(values, 0, ""); //$NON-NLS-1$
			int start = calculateRetentionTime(valueParserSupport.parseDouble(values, 1, 0));
			int maximum = calculateRetentionTime(valueParserSupport.parseDouble(values, 2, 0));
			int stop = calculateRetentionTime(valueParserSupport.parseDouble(values, 3, 0));
			String traces = valueParserSupport.parseString(values, 4, ""); //$NON-NLS-1$
			timeRange = new TimeRange(identifier, start, maximum, stop);
			timeRange.setTraces(traces);
		}

		return timeRange;
	}

	public void load(String timeRanges) {

		loadSettings(timeRanges);
	}

	public void loadDefault(String timeRanges) {

		loadSettings(timeRanges);
	}

	public String save() {

		StringBuilder builder = new StringBuilder();
		Iterator<TimeRange> iterator = values().iterator();
		while(iterator.hasNext()) {
			TimeRange timeRange = iterator.next();
			extractTimeRange(timeRange, builder);
			if(iterator.hasNext()) {
				builder.append(TimeRangeListUtil.SEPARATOR_TOKEN);
			}
		}
		return builder.toString().trim();
	}

	public void importItems(File file) {

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line;
			while((line = bufferedReader.readLine()) != null) {
				TimeRange template = extractTimeRange(line);
				if(template != null) {
					add(template);
				}
			}
		} catch(FileNotFoundException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}
	}

	public boolean exportItems(File file) {

		try (PrintWriter printWriter = new PrintWriter(file)) {
			/*
			 * Sort the items.
			 */
			List<TimeRange> timeRanges = new ArrayList<>(values());
			Collections.sort(timeRanges, (r1, r2) -> Integer.compare(r1.getStart(), r2.getStart()));

			Iterator<TimeRange> iterator = timeRanges.iterator();
			while(iterator.hasNext()) {
				StringBuilder builder = new StringBuilder();
				TimeRange template = iterator.next();
				extractTimeRange(template, builder);
				printWriter.println(builder.toString());
			}
			printWriter.flush();
			return true;
		} catch(FileNotFoundException e) {
			logger.warn(e);
			return false;
		}
	}

	private void loadSettings(String timeRanges) {

		if(timeRanges != null && !timeRanges.isBlank()) {
			String[] items = timeRangeListUtil.parseString(timeRanges);
			if(items.length > 0) {
				for(String item : items) {
					TimeRange timeRange = extractTimeRange(item);
					if(timeRange != null) {
						add(timeRange);
					}
				}
			}
		}
	}

	private int calculateRetentionTime(double minutes) {

		return (int)(minutes * TimeRange.MINUTE_FACTOR);
	}

	private String calculateRetentionTimeMinutes(int retentionTime) {

		return decimalFormat.format(retentionTime / TimeRange.MINUTE_FACTOR);
	}

	private void extractTimeRange(TimeRange timeRange, StringBuilder builder) {

		List<String> values = new ArrayList<>();
		values.add(timeRange.getIdentifier());
		values.add(calculateRetentionTimeMinutes(timeRange.getStart()));
		values.add(calculateRetentionTimeMinutes(timeRange.getMaximum()));
		values.add(calculateRetentionTimeMinutes(timeRange.getStop()));
		values.add(timeRange.getTraces());
		Iterator<String> iterator = values.iterator();

		while(iterator.hasNext()) {
			builder.append(iterator.next());
			if(iterator.hasNext()) {
				builder.append(" "); //$NON-NLS-1$
				builder.append(TimeRangeListUtil.SEPARATOR_ENTRY);
				builder.append(" "); //$NON-NLS-1$
			}
		}
	}
}