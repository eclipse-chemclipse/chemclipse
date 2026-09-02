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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mace.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.SeparationColumnFactory;
import org.eclipse.chemclipse.model.identifier.ColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.msd.converter.io.AbstractMassSpectraReader;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.MassSpectra;
import org.eclipse.chemclipse.msd.model.implementation.RegularLibraryMassSpectrum;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.core.runtime.IProgressMonitor;

public class MaceReader extends AbstractMassSpectraReader {

	private static final Logger logger = Logger.getLogger(MaceReader.class);
	private static final String CONVERTER_ID = "org.eclipse.chemclipse.msd.converter.supplier.mace";
	private static final String LINE_END = "\n";

	private static final Pattern namePattern = Pattern.compile("(NAME:)(.*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern formulaPattern = Pattern.compile("(FORMULA:)(.*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern molweightPattern = Pattern.compile("(MW:)(.*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern exactMassPattern = Pattern.compile("(ExactMass:)(.*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern synonymPattern = Pattern.compile("(Synon:)(.*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern commentsPattern = Pattern.compile("(COMMENTS?:)(.*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern smilesInCommentsPattern = Pattern.compile("\\bSmiles=(\\S+)");
	private static final Pattern contributorInCommentsPattern = Pattern.compile("\\bContributor=(\\S+)");
	private static final Pattern casNumberPattern = Pattern.compile("(CAS(NO|#)?:[ ]+)([0-9-]*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern databaseNamePattern = Pattern.compile("(DB(NO|#)?:)(.*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern inchiKeyPattern = Pattern.compile("(InChIKey:)(.*)", Pattern.CASE_INSENSITIVE);
	/*
	 * Retention_index: SemiStdNP=2279 StdNP=2283 StdPolar=2353 User=1857
	 * Capture the full line, then find all key=value pairs.
	 */
	private static final Pattern retentionIndexLinePattern = Pattern.compile("Retention_index:(.*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern retentionIndexPairPattern = Pattern.compile("(\\w+)=([0-9.]+)");
	private static final Pattern dataPattern = Pattern.compile("(.*)(Num Peaks:)(\\s*)(\\d*)(.*)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern ionPattern = Pattern.compile("([+]?\\d+\\.?\\d*)([\t ,;:]+)([+-]?\\d+\\.?\\d*([eE][+-]?\\d+)?)");

	private File file;

	@Override
	public IMassSpectra read(File file, IProgressMonitor monitor) throws IOException {

		this.file = file;
		ConcurrentHashMap<Integer, String> massSpectraData = getMassSpectraData(file);
		IMassSpectra massSpectra = extractMassSpectra(massSpectraData, monitor);
		massSpectra.setConverterId(CONVERTER_ID);
		massSpectra.setName(file.getName());
		return massSpectra;
	}

	private ConcurrentHashMap<Integer, String> getMassSpectraData(File file) throws IOException {

		ConcurrentHashMap<Integer, String> massSpectraData = new ConcurrentHashMap<>();
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
			StringBuilder builder = new StringBuilder();
			String line;
			int entryNumber = 0;
			while((line = bufferedReader.readLine()) != null) {
				if(line.isEmpty()) {
					addMassSpectrumData(entryNumber, builder, massSpectraData);
					builder = new StringBuilder();
				} else {
					builder.append(line);
					builder.append(LINE_END);
				}
				entryNumber++;
			}
			addMassSpectrumData(entryNumber, builder, massSpectraData);
		}
		return massSpectraData;
	}

	private void addMassSpectrumData(int entryNumber, StringBuilder builder, ConcurrentHashMap<Integer, String> massSpectraData) {

		if(!builder.isEmpty()) {
			massSpectraData.put(entryNumber, builder.toString());
		}
	}

	private IMassSpectra extractMassSpectra(ConcurrentHashMap<Integer, String> indexedMassSpectraData, IProgressMonitor monitor) {

		String referenceIdentifierMarker = org.eclipse.chemclipse.msd.converter.preferences.PreferenceSupplier.getReferenceIdentifierMarker();
		String referenceIdentifierPrefix = org.eclipse.chemclipse.msd.converter.preferences.PreferenceSupplier.getReferenceIdentifierPrefix();
		ConcurrentHashMap<Integer, IRegularLibraryMassSpectrum> indexedMassSpectra = new ConcurrentHashMap<>();
		if(indexedMassSpectraData.size() > 1) {
			monitor.beginTask("Extract mass spectra", IProgressMonitor.UNKNOWN);
			indexedMassSpectraData.entrySet().parallelStream().forEach(entry -> {
				addMassSpectrum(indexedMassSpectra, entry, referenceIdentifierMarker, referenceIdentifierPrefix);
			});
			monitor.done();
		} else if(indexedMassSpectraData.size() == 1) {
			String[] splittedMassSpectra = indexedMassSpectraData.entrySet().iterator().next().getValue().split("(NAME:|name:)");
			int index = 0;
			for(String split : splittedMassSpectra) {
				if(!split.equals("")) {
					Entry<Integer, String> entry;
					if(splittedMassSpectra.length == 1) {
						entry = new AbstractMap.SimpleEntry<>(index, split);
					} else {
						entry = new AbstractMap.SimpleEntry<>(index, "NAME:" + split);
					}
					addMassSpectrum(indexedMassSpectra, entry, referenceIdentifierMarker, referenceIdentifierPrefix);
					index++;
				}
			}
		}
		IMassSpectra massSpectra = new MassSpectra();
		TreeMap<Integer, IRegularLibraryMassSpectrum> sortedMassSpectra = new TreeMap<>(indexedMassSpectra);
		massSpectra.addMassSpectra(sortedMassSpectra.values());

		return massSpectra;
	}

	private void addMassSpectrum(ConcurrentHashMap<Integer, IRegularLibraryMassSpectrum> indexedMassSpectra, Entry<Integer, String> massSpectrumEntry, String referenceIdentifierMarker, String referenceIdentifierPrefix) {

		IRegularLibraryMassSpectrum massSpectrum = new RegularLibraryMassSpectrum();
		ILibraryInformation libraryInformation = massSpectrum.getLibraryInformation();
		libraryInformation.setDatabase(FilenameUtils.removeExtension(file.getName()));
		String massSpectrumData = massSpectrumEntry.getValue();
		String name = extractString(massSpectrumData, namePattern, 2);
		extractNameAndReferenceIdentifier(massSpectrum, name, referenceIdentifierMarker, referenceIdentifierPrefix);
		libraryInformation.setFormula(extractString(massSpectrumData, formulaPattern, 2));
		libraryInformation.setMolWeight(extractDouble(massSpectrumData, molweightPattern));
		libraryInformation.setExactMass(extractDouble(massSpectrumData, exactMassPattern));
		libraryInformation.setSynonyms(extractSynonyms(massSpectrumData));
		String rawComments = extractString(massSpectrumData, commentsPattern, 2);
		libraryInformation.setSmiles(extractCommentField(rawComments, smilesInCommentsPattern));
		libraryInformation.setContributor(extractCommentField(rawComments, contributorInCommentsPattern));
		libraryInformation.setComments(rawComments);
		libraryInformation.setCasNumber(extractString(massSpectrumData, casNumberPattern, 3));
		libraryInformation.setDatabaseIndex(extractInteger(massSpectrumData, databaseNamePattern, 3, -1));
		libraryInformation.setInChIKey(extractString(massSpectrumData, inchiKeyPattern, 2));
		massSpectrum.setRetentionIndex(extractRetentionIndex(libraryInformation, massSpectrumData));
		extractIons(massSpectrum, massSpectrumData);
		if(!massSpectrum.isEmpty()) {
			indexedMassSpectra.put(massSpectrumEntry.getKey(), massSpectrum);
		}
	}

	private float extractRetentionIndex(ILibraryInformation libraryInformation, String data) {

		Matcher lineMatcher = retentionIndexLinePattern.matcher(data);
		if(lineMatcher.find()) {
			Matcher pairMatcher = retentionIndexPairPattern.matcher(lineMatcher.group(1));
			while(pairMatcher.find()) {
				SeparationColumnType columnType = mapRetentionIndexKey(pairMatcher.group(1));
				if(columnType != null) {
					try {
						float retentionIndex = Float.parseFloat(pairMatcher.group(2));
						ISeparationColumn separationColumn = SeparationColumnFactory.getSeparationColumn(columnType);
						IColumnIndexMarker columnIndexMarker = new ColumnIndexMarker(separationColumn, retentionIndex);
						libraryInformation.add(columnIndexMarker);
					} catch(NumberFormatException e) {
						logger.warn(e);
					}
				}
			}
		}

		return 0.0f;
	}

	private SeparationColumnType mapRetentionIndexKey(String key) {

		return switch(key) {
			case "SemiStdNP" -> SeparationColumnType.SEMI_POLAR;
			case "StdNP" -> SeparationColumnType.NON_POLAR;
			case "StdPolar" -> SeparationColumnType.POLAR;
			case "User" -> SeparationColumnType.DEFAULT;
			default -> null;
		};
	}

	private String extractCommentField(String comments, Pattern pattern) {

		Matcher matcher = pattern.matcher(comments);
		return matcher.find() ? matcher.group(1) : "";
	}

	private void extractIons(IRegularLibraryMassSpectrum massSpectrum, String massSpectrumData) {

		String ionData = "";
		Matcher data = dataPattern.matcher(massSpectrumData);
		data.find();
		if(data.matches()) {
			ionData = data.group(5);
		}

		Matcher ions = ionPattern.matcher(ionData);
		while(ions.find()) {
			double ion = Double.parseDouble(ions.group(1));
			float abundance = Float.parseFloat(ions.group(3));
			if(abundance > 0) {
				massSpectrum.addIon(new Ion(ion, abundance));
			}
		}
	}

	private int extractInteger(String data, Pattern pattern, int group, int defaultValue) {

		try {
			return Integer.parseInt(extractString(data, pattern, group));
		} catch(NumberFormatException e) {
			return defaultValue;
		}
	}

	private String extractString(String data, Pattern pattern, int group) {

		Matcher matcher = pattern.matcher(data);
		if(matcher.find()) {
			return matcher.group(group).trim();
		}
		return "";
	}

	private double extractDouble(String data, Pattern pattern) {

		try {
			Matcher matcher = pattern.matcher(data);
			if(matcher.find() && matcher.groupCount() > 1 && !matcher.group(2).isBlank()) {
				return Double.parseDouble(matcher.group(2).trim());
			}
		} catch(NumberFormatException e) {
			logger.warn(e);
		}
		return 0.0;
	}

	private Set<String> extractSynonyms(String data) {

		Set<String> synonyms = new HashSet<>();
		Matcher matcher = synonymPattern.matcher(data);
		while(matcher.find()) {
			String synonym = matcher.group(2).trim();
			synonyms.add(synonym);
		}
		return synonyms;
	}
}