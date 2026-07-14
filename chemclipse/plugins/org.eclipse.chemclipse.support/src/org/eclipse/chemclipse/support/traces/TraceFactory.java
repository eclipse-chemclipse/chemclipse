/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.support.traces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.chemclipse.support.settings.OperatingSystemUtils;

public class TraceFactory {

	public static final String DESCRIPTION = "Signal Traces";
	public static final String FILE_EXTENSION = ".str";
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION;
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION;
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")";

	private static final String LINE_DELIMITER_OS = OperatingSystemUtils.getLineDelimiter();
	private static final String LINE_DELIMITER_GENERIC = "\n";

	private static final Pattern PATTERN_GENERIC_DIGITS = Pattern.compile("(\\d+\\.?\\d+)(.*)");
	private static final Pattern PATTERN_TANDEM_MSD = Pattern.compile("(\\d+)(\\s+>\\s+)(\\d+\\.?\\d?)(\\s+@)(\\d+)(.*)");
	private static final Pattern PATTERN_DIGITS = Pattern.compile("([^x])([0-9]+)(\\.)([0-9]{2})");
	private static final Pattern PATTERN_RANGE_INTEGER = Pattern.compile("([0-9]+\\s?-\\s?[0-9]+)");

	/**
	 * Returns the list as string.
	 */
	public static String getTracesAsString(List<ITrace> traces) {

		List<String> values = new ArrayList<>();
		for(ITrace trace : traces) {
			values.add(trace.toString());
		}

		return String.join(ITrace.SEPARATOR_TRACE_ITEM, values);
	}

	/**
	 * Returns null if valid.
	 * Otherwise, the invalid characters are returned, separated by a whitespace.
	 */
	public static String validate(String line) {

		/*
		 * Remove all valid items.
		 */
		if(line != null) {
			/*
			 * Tab, ...
			 */
			if(line.contains("\t")) {
				return "Tab";
			} else if(line.contains("\n")) {
				return "New Line";
			} else if(line.contains("\r")) {
				return "Carriage Return";
			} else {
				String errors = line.replaceAll("\\s", "");
				errors = errors.replaceAll("[0-9]", "");
				List<String> replacements = new ArrayList<>();
				replacements.add(ITrace.PREFIX_SCALE_FACTOR);
				replacements.add(ITrace.POSTFIX_SCALE_FACTOR);
				replacements.add(ITrace.INFIX_RANGE_STANDARD);
				replacements.add(ITrace.INFIX_RANGE_SIMPLE);
				replacements.add(ITrace.POSTFIX_UNIT_PPM);
				replacements.add(ITrace.INFIX_TANDEM_MS_CE);
				replacements.add(ITrace.INFIX_TANDEM_MS_PD);
				replacements.add(ITrace.INFIX_DECIMALS);
				replacements.add(ITrace.SEPARATOR_TRACE_ITEM);
				replacements.add(ITrace.SEPARATOR_TRACE_RANGE);
				for(String replacement : replacements) {
					errors = errors.replace(replacement, "");
				}

				if(errors.isEmpty()) {
					return null;
				} else {
					/*
					 * Unique Characters
					 */
					Set<Character> characters = new HashSet<>();
					char[] chars = errors.toCharArray();
					for(Character character : chars) {
						characters.add(character);
					}
					/*
					 * Replace for better understanding
					 */
					List<String> invalidInput = new ArrayList<>();
					for(Character character : characters) {
						invalidInput.add(character.toString());
					}
					/*
					 * Return sorted list as String
					 */
					Collections.sort(invalidInput);
					return String.join(" ", invalidInput);
				}
			}
		} else {
			return "'null'";
		}
	}

	/**
	 * Determine the trace type (TraceHighResMSD, TraceGenericDelta, TraceTandemMSD or TraceGeneric) by the given line.
	 * May return null.
	 */
	public static Class<? extends ITrace> getTraceType(String line, DetectorType detectorType) {

		Class<? extends ITrace> clazz = TraceEmpty.class;
		if(line != null) {
			/*
			 * Drill down from specific trace to generic definitions.
			 */
			String trace = line.trim();
			if(!trace.isEmpty()) {
				if(trace.contains(ITrace.INFIX_RANGE_STANDARD) || trace.contains(ITrace.INFIX_RANGE_SIMPLE)) {
					if(trace.contains(ITrace.POSTFIX_UNIT_PPM)) {
						/*
						 * 400.01627±10ppm
						 * 400.01627±10ppm (x4.7)
						 */
						clazz = TraceHighResMSD.class;
					} else {
						switch(detectorType) {
							case MSD:
								/*
								 * 400.01627±0.02
								 * 400.01627±0.02 (x2.9)
								 * 400.01627+-0.02
								 * 400.01627+-0.02 (x2.9)
								 */
								clazz = TraceHighResMSD.class;
								break;
							case WSD:
								/*
								 * 400.01627±0.02
								 * 400.01627±0.02 (x2.9)
								 * 400.01627+-0.02
								 * 400.01627+-0.02 (x2.9)
								 */
								clazz = TraceHighResWSD.class;
								break;
							default:
								clazz = TraceGenericDelta.class;
								break;
						}
					}
				} else if(trace.contains(ITrace.INFIX_TANDEM_MS_CE)) {
					/*
					 * 139 > 111.0 @12
					 * 139 > 111.0 @12 (x5.8)
					 */
					clazz = TraceTandemMSD.class;
				} else if(trace.contains(ITrace.INFIX_DECIMALS)) {
					Matcher matcher = PATTERN_DIGITS.matcher(trace);
					if(matcher.find()) {
						/*
						 * 69.25
						 * 69.276
						 * 69.25 (x1.23)
						 * 69.276 (x1.25)
						 */
						switch(detectorType) {
							case MSD:
								clazz = TraceHighResMSD.class;
								break;
							case WSD:
								clazz = TraceHighResWSD.class;
								break;
							default:
								clazz = TraceGenericDelta.class;
								break;
						}
					} else {
						/*
						 * 69.1
						 * 69.1 (x1.24)
						 */
						switch(detectorType) {
							case MSD:
								clazz = TraceNominalMSD.class;
								break;
							case VSD:
								clazz = TraceRasteredVSD.class;
								break;
							case WSD:
								clazz = TraceRasteredWSD.class;
								break;
							default:
								clazz = TraceGeneric.class;
								break;
						}
					}
				} else if(trace.contains(ITrace.SEPARATOR_TRACE_RANGE)) {
					/*
					 * Range and Legacy Support
					 * 0 - 0
					 * 55 - 120
					 * 18 28 32 55 - 65 84 207
					 * 55 - 65 84 207
					 * 18 28 32 55 - 65
					 * 18 28 32 55 - 65 84 90 - 95 207
					 * 55 - 65 84 90 - 95 207
					 * 18 28 32 55 - 65
					 * 18, 28, 32, 55 - 65, 84, 207
					 */
					switch(detectorType) {
						case MSD:
							clazz = TraceNominalMSD.class;
							break;
						case VSD:
							clazz = TraceRasteredVSD.class;
							break;
						case WSD:
							clazz = TraceRasteredWSD.class;
							break;
						default:
							clazz = TraceGeneric.class;
							break;
					}
				} else {
					/*
					 * 69
					 * 1800
					 * 400
					 */
					switch(detectorType) {
						case MSD:
							clazz = TraceNominalMSD.class;
							break;
						case VSD:
							clazz = TraceRasteredVSD.class;
							break;
						case WSD:
							clazz = TraceRasteredWSD.class;
							break;
						default:
							clazz = TraceGeneric.class;
							break;
					}
				}
			}
		}

		return clazz;
	}

	public static <T extends ITrace> List<T> parseTraces(String content, Class<T> clazz) {

		List<T> elements = new ArrayList<>();

		List<String> lines = splitLines(content);
		for(String line : lines) {
			if(clazz.equals(TraceGenericDelta.class)) {
				String[] traces = line.split(ITrace.SEPARATOR_TRACE_ITEM);
				for(String trace : traces) {
					addTraceSpecific(trace.trim(), content, elements, clazz);
				}
			} else {
				if(line.contains(ITrace.SEPARATOR_TRACE_ITEM)) {
					String[] traces = line.split(ITrace.SEPARATOR_TRACE_ITEM);
					for(String trace : traces) {
						if(trace.contains(ITrace.INFIX_RANGE_SIMPLE) || trace.contains(ITrace.INFIX_RANGE_STANDARD)) {
							addTraceSpecific(trace.trim(), content, elements, clazz);
						} else if(trace.contains(ITrace.INFIX_TANDEM_MS_CE)) {
							addTraceSpecific(trace.trim(), content, elements, clazz);
						} else {
							if(trace.contains(ITrace.SEPARATOR_TRACE_RANGE)) {
								/*
								 * Special case:
								 * 0 - 0
								 * 55 - 120
								 */
								if(isSupportGeneric(clazz)) {
									addTraceRange(trace, elements, clazz);
								}
							} else {
								String part = trace.trim();
								if(isTraceInteger(part)) {
									if(isSupportGeneric(clazz)) {
										addTraceGeneric(trace, elements, clazz);
									}
								} else {
									addTraceSpecific(part, content, elements, clazz);
								}
							}
						}
					}
				} else {
					if(isSupportGeneric(clazz)) {
						if(line.contains(ITrace.SEPARATOR_TRACE_RANGE)) {
							/*
							 * Special cases:
							 * 0 - 0
							 * 55 - 120
							 * 18 28 32 55 - 65 84 207
							 * 55 - 65 84 207
							 * 18 28 32 55 - 65
							 * 18 28 32 55 - 65 84 90 - 95 207
							 * 55 - 65 84 90 - 95 207
							 * 55 - 65 84 90 - 95
							 */
							String traces = line;
							Set<String> ranges = new HashSet<>();
							Matcher matcher = PATTERN_RANGE_INTEGER.matcher(line);
							while(matcher.find()) {
								String range = matcher.group();
								ranges.add(range);
								traces = traces.replace(range, "");
							}
							/*
							 * Parse traces and ranges
							 */
							parseSeparatedGenericTraces(traces, elements, clazz);
							for(String range : ranges) {
								addTraceRange(range, elements, clazz);
							}
							Collections.sort(elements, (e1, e2) -> Double.compare(e1.getValue(), e2.getValue()));
						} else {
							/*
							 * Special case: 18 28 32 84 207
							 */
							parseSeparatedGenericTraces(line, elements, clazz);
						}
					} else {
						String trace = line.trim();
						if(trace.contains(ITrace.INFIX_TANDEM_MS_CE)) {
							addTraceSpecific(trace, content, elements, clazz);
						} else if((trace.contains(ITrace.INFIX_RANGE_SIMPLE) || trace.contains(ITrace.INFIX_RANGE_STANDARD)) || (!trace.contains(ITrace.SEPARATOR_TRACE_RANGE) && !trace.contains(" "))) {
							addTraceSpecific(trace, content, elements, clazz);
						}
					}
				}
			}
		}

		return elements;
	}

	public static <T extends ITrace> T parseTrace(String content, Class<T> clazz) {

		T traceSpecific = null;
		try {
			/*
			 * Try to parse the content.
			 */
			traceSpecific = clazz.getDeclaredConstructor().newInstance();
			boolean success = false;

			if(traceSpecific instanceof TraceNominalMSD traceNominalMSD) {
				success = parseTraceNominalMSD(content, traceNominalMSD);
			} else if(traceSpecific instanceof TraceTandemMSD traceTandemMSD) {
				success = parseTraceTandemMSD(content, traceTandemMSD);
			} else if(traceSpecific instanceof TraceHighResMSD traceHighResMSD) {
				success = parseTraceHighResMSD(content, traceHighResMSD);
			} else if(traceSpecific instanceof TraceRasteredWSD traceRasteredWSD) {
				success = parseTraceRasteredWSD(content, traceRasteredWSD);
			} else if(traceSpecific instanceof TraceHighResWSD traceHighResWSD) {
				success = parseTraceHighResWSD(content, traceHighResWSD);
			} else if(traceSpecific instanceof TraceRasteredVSD traceRasteredVSD) {
				success = parseTraceRasteredVSD(content, traceRasteredVSD);
			} else if(traceSpecific instanceof TraceGeneric traceGeneric) {
				success = parseTraceGeneric(content, traceGeneric);
			} else if(traceSpecific instanceof TraceGenericDelta traceGenericDelta) {
				success = parseTraceGenericDelta(content, traceGenericDelta);
			}
			/*
			 * In case that the content can't be parsed.
			 */
			if(!success) {
				traceSpecific = null;
			}
		} catch(Exception e) {
		}

		return traceSpecific;
	}

	/*
	 * 18 28 32 84 207
	 */
	private static <T extends ITrace> void parseSeparatedGenericTraces(String line, List<T> elements, Class<T> clazz) {

		String[] parts = line.split("\\s+");
		for(String part : parts) {
			addTraceGeneric(part, elements, clazz);
		}
	}

	private static boolean parseTraceNominalMSD(String content, TraceNominalMSD traceSpecific) {

		/*
		 * 69
		 * 69.4
		 * 69.5
		 * 79 (x10.5)
		 * 79 (x8)
		 * 79.4 (x8)
		 * 79.5 (x8)
		 * 79 (x0.27)
		 */
		Matcher matcher = PATTERN_GENERIC_DIGITS.matcher(content);
		if(matcher.matches()) {
			double mz = parseDouble(matcher.group(1)); // 79
			if(mz > 0) {
				traceSpecific.setMZ(mz);
				assignScaleFactor(matcher.group(2), traceSpecific); // 10.5
				return true;
			}
		}
		/*
		 * Content couldn't be matched.
		 */
		return false;
	}

	private static boolean parseTraceTandemMSD(String content, TraceTandemMSD traceSpecific) {

		/*
		 * 139 > 111.0 @12
		 * 139 > 111.0 @12 (x5.8)
		 */
		Matcher matcher = PATTERN_TANDEM_MSD.matcher(content);
		if(matcher.matches()) {
			double parentMZ = parseDouble(matcher.group(1)); // 139
			if(parentMZ > 0) {
				double daughterMZ = parseDouble(matcher.group(3)); // 111.0
				if(daughterMZ > 0) {
					double collisionEnergy = parseDouble(matcher.group(5)); // 12
					if(collisionEnergy > 0) {
						traceSpecific.setParentMZ(parentMZ);
						traceSpecific.setDaughterMZ(daughterMZ);
						traceSpecific.setCollisionEnergy(collisionEnergy);
						assignScaleFactor(matcher.group(6), traceSpecific); // 5.8
						return true;
					}
				}
			}
		}
		/*
		 * Content couldn't be matched.
		 */
		return false;
	}

	private static boolean parseTraceHighResMSD(String content, TraceHighResMSD traceSpecific) {

		/*
		 * 427.240
		 * 279.092 (x5.3)
		 * 400.01627±10ppm
		 * 400.01627±10ppm (x4.7)
		 * 400.01627±0.02
		 * 400.01627±0.02 (x2.9)
		 * 400.01627+-0.02
		 * 400.01627+-0.02 (x2.9)
		 * 400.01627+-10ppm
		 * 400.01627+-10ppm (x4.7)
		 */
		Matcher matcher = PATTERN_GENERIC_DIGITS.matcher(content);
		if(matcher.matches()) {
			double mz = parseDouble(matcher.group(1)); // 400.01627
			if(mz > 0) {
				/*
				 * Set m/z and match range.
				 */
				traceSpecific.setMZ(mz);
				String part = matcher.group(2).trim();
				if(containsRangeMarker(part)) {
					String[] parts = part.split("\\s");
					String range = parts[0].replace(ITrace.INFIX_RANGE_STANDARD, "").replace(ITrace.INFIX_RANGE_SIMPLE, "").trim();
					double delta = -1;
					if(containsUnitMarkerPPM(range)) {
						double ppm = parseDouble(range.replace(ITrace.POSTFIX_UNIT_PPM, ""));
						if(ppm >= 0) {
							delta = mz * ppm / ITrace.MILLION;
						}
					} else {
						delta = parseDouble(range);
					}

					if(delta >= 0) {
						traceSpecific.setDelta(delta);
						if(parts.length > 1) {
							assignScaleFactor(parts[1], traceSpecific); // 5.3
						}
						return true;
					}
				} else {
					assignScaleFactor(part, traceSpecific); // 5.3
					return true;
				}
			}
		}
		/*
		 * Content couldn't be matched.
		 */
		return false;
	}

	private static boolean parseTraceRasteredWSD(String content, TraceRasteredWSD traceSpecific) {

		/*
		 * 200
		 * 200.4
		 * 200.5
		 * 201 (x1.6)
		 */
		Matcher matcher = PATTERN_GENERIC_DIGITS.matcher(content);
		if(matcher.matches()) {
			double wavelength = parseDouble(matcher.group(1)); // 200
			if(wavelength > 0) {
				traceSpecific.setWavelength(wavelength);
				assignScaleFactor(matcher.group(2), traceSpecific); // 1.6
				return true;
			}
		}
		/*
		 * Content couldn't be matched.
		 */
		return false;
	}

	private static boolean parseTraceHighResWSD(String content, TraceHighResWSD traceSpecific) {

		/*
		 * 427.240
		 * 279.092 (x5.3)
		 * 400.01627±0.02
		 * 400.01627±0.02 (x2.9)
		 * 400.01627+-0.02
		 * 400.01627+-0.02 (x2.9)
		 */
		Matcher matcher = PATTERN_GENERIC_DIGITS.matcher(content);
		if(matcher.matches()) {
			double wavelength = parseDouble(matcher.group(1)); // 400.01627
			if(wavelength > 0) {
				/*
				 * Set wavelength and match range.
				 */
				traceSpecific.setWavelength(wavelength);
				String part = matcher.group(2).trim();
				if(containsRangeMarker(content)) {
					String[] parts = part.split("\\s");
					String range = parts[0].replace(ITrace.INFIX_RANGE_STANDARD, "").replace(ITrace.INFIX_RANGE_SIMPLE, "").trim();
					double delta = parseDouble(range);
					if(delta >= 0) {
						traceSpecific.setDelta(delta);
						if(parts.length > 1) {
							assignScaleFactor(parts[1], traceSpecific); // 5.3
						}
						return true;
					}
				} else {
					assignScaleFactor(part, traceSpecific); // 5.3
					return true;
				}
			}
		}
		/*
		 * Content couldn't be matched.
		 */
		return false;
	}

	private static boolean parseTraceRasteredVSD(String content, TraceRasteredVSD traceSpecific) {

		/*
		 * 1800
		 * 1800.4
		 * 1800.5
		 * 1801 (x1.6)
		 */
		Matcher matcher = PATTERN_GENERIC_DIGITS.matcher(content);
		if(matcher.matches()) {
			double wavenumber = parseDouble(matcher.group(1)); // 200
			if(wavenumber > 0) {
				traceSpecific.setWavenumber(wavenumber);
				assignScaleFactor(matcher.group(2), traceSpecific); // 1.6
				return true;
			}
		}
		/*
		 * Content couldn't be matched.
		 */
		return false;
	}

	private static boolean parseTraceGeneric(String content, TraceGeneric traceGeneric) {

		if(PATTERN_TANDEM_MSD.matcher(content).matches()) {
			TraceTandemMSD traceTandemMSD = new TraceTandemMSD();
			if(parseTraceTandemMSD(content, traceTandemMSD)) {
				traceGeneric.setValue(traceTandemMSD.getDaughterMZ());
				traceGeneric.setScaleFactor(traceTandemMSD.getScaleFactor());
				return true;
			}
		} else if(containsRangeMarker(content)) {
			if(containsUnitMarkerPPM(content)) {
				TraceHighResMSD traceHighResMSD = new TraceHighResMSD();
				if(parseTraceHighResMSD(content, traceHighResMSD)) {
					traceGeneric.setValue(traceHighResMSD.getMZ());
					traceGeneric.setScaleFactor(traceHighResMSD.getScaleFactor());
					return true;
				}
			} else {
				TraceHighResWSD traceHighResWSD = new TraceHighResWSD();
				if(parseTraceHighResWSD(content, traceHighResWSD)) {
					traceGeneric.setValue(traceHighResWSD.getWavelength());
					traceGeneric.setScaleFactor(traceHighResWSD.getScaleFactor());
					return true;
				}
			}
		} else {
			Matcher matcher = PATTERN_GENERIC_DIGITS.matcher(content);
			if(matcher.matches()) {
				double value = parseDouble(matcher.group(1)); // 79
				if(value > 0) {
					traceGeneric.setValue(value);
					assignScaleFactor(matcher.group(2), traceGeneric); // 10.5
					return true;
				}
			}
		}
		/*
		 * Content couldn't be matched.
		 */
		return false;
	}

	private static boolean parseTraceGenericDelta(String content, TraceGenericDelta traceSpecific) {

		/*
		 * 427.240
		 * 279.092 (x5.3)
		 * 400.01627±0.02
		 * 400.01627±0.02 (x2.9)
		 * 400.01627+-0.02
		 * 400.01627+-0.02 (x2.9)
		 */
		Matcher matcher = PATTERN_GENERIC_DIGITS.matcher(content);
		if(matcher.matches()) {
			double value = parseDouble(matcher.group(1)); // 400.01627
			if(value > 0) {
				/*
				 * Set value and match range.
				 */
				traceSpecific.setValue(value);
				String part = matcher.group(2).trim();
				if(containsRangeMarker(content)) {
					String[] parts = part.split("\\s");
					String range = parts[0].replace(ITrace.INFIX_RANGE_STANDARD, "").replace(ITrace.INFIX_RANGE_SIMPLE, "").trim();
					double delta = parseDouble(range);
					if(delta >= 0) {
						traceSpecific.setDelta(delta);
						if(parts.length > 1) {
							assignScaleFactor(parts[1], traceSpecific); // 5.3
						}
						return true;
					}
				} else {
					assignScaleFactor(part, traceSpecific); // 5.3
					return true;
				}
			}
		}
		/*
		 * Content couldn't be matched.
		 */
		return false;
	}

	private static boolean containsRangeMarker(String content) {

		return content.contains(ITrace.INFIX_RANGE_STANDARD) || content.contains(ITrace.INFIX_RANGE_SIMPLE);
	}

	private static boolean containsUnitMarkerPPM(String content) {

		return content.contains(ITrace.POSTFIX_UNIT_PPM);
	}

	private static void assignScaleFactor(String value, ITrace trace) {

		if(!value.isEmpty()) {
			value = value.replace(ITrace.PREFIX_SCALE_FACTOR, "");
			value = value.replace(ITrace.POSTFIX_SCALE_FACTOR, "");
			double scaleFactor = parseDouble(value);
			if(scaleFactor > 0) {
				trace.setScaleFactor(scaleFactor);
			}
		}
	}

	private static List<String> splitLines(String content) {

		List<String> lines = new ArrayList<>();

		if(content.contains(LINE_DELIMITER_OS)) {
			lines.addAll(Arrays.asList(content.split(LINE_DELIMITER_OS)));
		} else if(content.contains(LINE_DELIMITER_GENERIC)) {
			lines.addAll(Arrays.asList(content.split(LINE_DELIMITER_GENERIC)));
		} else {
			lines.add(content);
		}

		return lines;
	}

	private static <T extends ITrace> void addTraceSpecific(String part, String content, List<T> elements, Class<T> clazz) {

		if(!part.isBlank()) {
			if(containsRangeMarker(part)) {
				if(containsUnitMarkerPPM(content)) {
					if(isSupportHighResMSD(clazz)) {
						addTraceSpecific(part, elements, clazz);
					}
				} else {
					if(isSupportHighRes(clazz)) {
						addTraceSpecific(part, elements, clazz);
					}
				}
			} else {
				addTraceSpecific(part, elements, clazz);
			}
		}
	}

	private static <T extends ITrace> void addTraceSpecific(String part, List<T> elements, Class<T> clazz) {

		T specificTrace = parseTrace(part, clazz);
		if(specificTrace != null) {
			elements.add(specificTrace);
		}
	}

	private static <T extends ITrace> void addTraceGeneric(String part, List<T> elements, Class<T> clazz) {

		double trace = parseDouble(part);
		if(trace > 0) {
			try {
				T genericTrace = clazz.getDeclaredConstructor().newInstance();
				genericTrace.setValue(trace);
				elements.add(genericTrace);
			} catch(Exception e) {
			}
		}
	}

	private static <T extends ITrace> void addTraceRange(String trace, List<T> elements, Class<T> clazz) {

		String[] parts = trace.trim().split(ITrace.SEPARATOR_TRACE_RANGE);
		if(parts.length == 2) {
			String part1 = parts[0].trim();
			String part2 = parts[1].trim();
			int start = parseInteger(part1);
			int stop = parseInteger(part2);
			if(start == stop) {
				/*
				 * 0 - 0 (valid)
				 * 18 28 32 55 - 65 84 207 (invalid)
				 */
				if(!part1.contains(" ") && !part2.contains(" ")) {
					addGenericTrace(elements, clazz, start);
				}
			} else if(start > 0 && start < stop) {
				/*
				 * 55 - 120
				 */
				for(int i = start; i <= stop; i++) {
					addGenericTrace(elements, clazz, i);
				}
			}
		}
	}

	private static <T extends ITrace> void addGenericTrace(List<T> elements, Class<T> clazz, int trace) {

		try {
			T genericTrace = clazz.getDeclaredConstructor().newInstance();
			genericTrace.setValue(trace);
			elements.add(genericTrace);
		} catch(Exception e) {
		}
	}

	private static boolean isTraceInteger(String content) {

		int valueInteger = parseInteger(content);
		if(valueInteger > 0) {
			return Integer.toString(valueInteger).equals(content.trim());
		}

		return false;
	}

	private static <T extends ITrace> boolean isSupportGeneric(Class<T> clazz) {

		return clazz == TraceGeneric.class || //
				clazz == TraceNominalMSD.class || //
				clazz == TraceRasteredVSD.class || //
				clazz == TraceRasteredWSD.class;
	}

	private static <T extends ITrace> boolean isSupportHighResMSD(Class<T> clazz) {

		return clazz == TraceGeneric.class || //
				clazz == TraceGenericDelta.class || //
				clazz == TraceHighResMSD.class;
	}

	private static <T extends ITrace> boolean isSupportHighRes(Class<T> clazz) {

		return clazz == TraceGeneric.class || //
				clazz == TraceGenericDelta.class || //
				clazz == TraceHighResMSD.class || //
				clazz == TraceHighResWSD.class;
	}

	private static int parseInteger(String value) {

		try {
			return Integer.parseInt(value.trim());
		} catch(NumberFormatException e) {
			return -1;
		}
	}

	private static double parseDouble(String value) {

		try {
			return Double.parseDouble(value.trim());
		} catch(NumberFormatException e) {
			return -1;
		}
	}
}