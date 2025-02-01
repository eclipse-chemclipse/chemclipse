/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring package name
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.csv.io.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IChromatogramPeak;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IPeakIntensityValues;
import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.implementation.IntegrationEntry;
import org.eclipse.chemclipse.model.implementation.PeakIntensityValues;
import org.eclipse.chemclipse.msd.converter.peak.IPeakExportConverter;
import org.eclipse.chemclipse.msd.converter.peak.IPeakImportConverter;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonProvider;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.msd.model.core.PeaksMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.PeakMSD;
import org.eclipse.chemclipse.msd.model.implementation.PeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.PeakModelMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class CSVPeakConverter implements IPeakExportConverter, IPeakImportConverter {

	public static final Charset CHARSET = StandardCharsets.UTF_8;
	//
	private static final String HEADER_NAME = "Name";
	private static final String HEADER_AREA = "Area";
	private static final String HEADER_RRT = "RRT [min]";
	private static final String HEADER_RI = "RI";
	private static final String HEADER_INTENSITIES = "intensities";
	private static final String HEADER_MZ = "m/z";
	private static final String HEADER_RT = "RT [min]";
	private static final char SEPERATOR_VALUE = ':';
	private static final char SEPERATOR_RECORD = ' ';
	private static final Pattern SEPERATOR_VALUE_PATTERN = Pattern.compile(String.valueOf(SEPERATOR_VALUE), Pattern.LITERAL);
	private static final Pattern SEPERATOR_RECORD_PATTERN = Pattern.compile(String.valueOf(SEPERATOR_RECORD), Pattern.LITERAL);
	private static final String NAME = "CSV Peak Export";
	//
	private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	//
	public static final String[] HEADERS = {HEADER_NAME, HEADER_RT, HEADER_RRT, HEADER_RI, HEADER_AREA, "S/N", "CAS", HEADER_MZ, HEADER_INTENSITIES};

	// export
	@Override
	public IProcessingInfo<File> convert(File file, IPeaksMSD peaks, boolean append, IProgressMonitor monitor) {

		try {
			try (FileOutputStream stream = new FileOutputStream(file, append)) {
				writePeaks(peaks, new OutputStreamWriter(stream, CHARSET), !append);
			}
		} catch(IOException e) {
			ProcessingInfo<File> error = new ProcessingInfo<>();
			error.addErrorMessage(NAME, "Export to CSV failed", e);
			return error;
		}
		return new ProcessingInfo<>(file);
	}

	private static StringBuilder writeIntensities(IPeakModelMSD peakModel) {

		StringBuilder stringBuilder = new StringBuilder();
		if(peakModel != null) {
			List<Integer> retentionTimes = peakModel.getRetentionTimes();
			for(Integer rt : retentionTimes) {
				if(stringBuilder.length() > 0) {
					stringBuilder.append(SEPERATOR_RECORD);
				}
				stringBuilder.append(rt);
				stringBuilder.append(SEPERATOR_VALUE);
				stringBuilder.append(peakModel.getPeakAbundance(rt));
			}
		}
		return stringBuilder;
	}

	private static StringBuilder writeMassSpectrum(IIonProvider provider) {

		StringBuilder sb = new StringBuilder();
		if(provider != null) {
			for(final IIon ion : provider.getIons()) {
				if(sb.length() > 0) {
					sb.append(SEPERATOR_RECORD);
				}
				sb.append(ion.getIon());
				sb.append(':');
				sb.append(ion.getAbundance());
			}
		}
		return sb;
	}

	private static <R> R getLibInfo(IIdentificationTarget target, Function<ILibraryInformation, R> fkt) {

		if(target != null) {
			ILibraryInformation information = target.getLibraryInformation();
			return fkt.apply(information);
		}
		return null;
	}

	// import
	@Override
	public IProcessingInfo<IPeaksMSD> convert(File file, IProgressMonitor monitor) {

		try {
			try (FileInputStream stream = new FileInputStream(file)) {
				return new ProcessingInfo<>(readPeaks(new InputStreamReader(stream, CHARSET)));
			}
		} catch(ParseException | IOException e) {
			ProcessingInfo<IPeaksMSD> error = new ProcessingInfo<>();
			error.addErrorMessage(NAME, "Import failed", e);
			return error;
		}
	}

	public static void writePeaks(IPeaksMSD peaks, Writer writer, boolean writeHeader) throws IOException {

		CSVFormat csvFormat = CSVFormat.EXCEL.builder().setNullString("").setQuoteMode(QuoteMode.ALL).build();
		try (CSVPrinter csv = new CSVPrinter(writer, csvFormat)) {
			if(writeHeader) {
				csv.printRecord(Arrays.asList(HEADERS));
			}
			NumberFormat nf;
			synchronized(NUMBER_FORMAT) {
				nf = (NumberFormat)NUMBER_FORMAT.clone();
			}
			for(IPeak peak : peaks.getPeaks()) {
				/*
				 * Sort
				 */
				IPeakModel peakModel = peak.getPeakModel();
				IIdentificationTarget target = IIdentificationTarget.getIdentificationTarget(peak);
				// Name
				csv.print(getName(peak));
				// RT
				csv.print(nf.format(peakModel.getRetentionTimeAtPeakMaximum() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
				// RRT
				csv.print(nf.format(peakModel.getPeakMaximum().getRelativeRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
				// RI
				csv.print(nf.format(peakModel.getPeakMaximum().getRetentionIndex()));
				// Area
				csv.print(nf.format(peak.getIntegratedArea()));
				// S/N
				if(peak instanceof IChromatogramPeak chromatogramPeak) {
					csv.print(nf.format((chromatogramPeak).getSignalToNoiseRatio()));
				} else {
					csv.print("-");
				}
				// CAS
				csv.print(getLibInfo(target, ILibraryInformation::getCasNumber));
				if(peak instanceof IPeakMSD msd) {
					// mass spectrum
					csv.print(writeMassSpectrum(msd.getPeakModel().getPeakMassSpectrum()));
					// intensities
					csv.print(writeIntensities(msd.getPeakModel()));
				}
				csv.println();
			}
		}
	}

	public static IPeaksMSD readPeaks(Reader reader) throws IOException, ParseException {

		PeaksMSD result = new PeaksMSD();
		CSVFormat csvFormat = CSVFormat.EXCEL.builder().setHeader(HEADERS).setSkipHeaderRecord(true).build();
		try (CSVParser parser = new CSVParser(reader, csvFormat)) {
			NumberFormat nf;
			synchronized(NUMBER_FORMAT) {
				nf = (NumberFormat)NUMBER_FORMAT.clone();
			}

			for(CSVRecord csvRecord : parser) {
				PeakModelMSD peakModel = new PeakModelMSD(parseMassSpectrum(csvRecord.get(HEADER_MZ)), parseIntensityValues(csvRecord.get(HEADER_INTENSITIES)));
				IScan maximum = peakModel.getPeakMaximum();
				maximum.setRetentionTime((int)(nf.parse(csvRecord.get(HEADER_RT)).doubleValue() * IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
				maximum.setRelativeRetentionTime((int)(nf.parse(csvRecord.get(HEADER_RRT)).doubleValue() * IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
				maximum.setRetentionIndex(nf.parse(csvRecord.get(HEADER_RI)).floatValue());
				PeakMSD peakMSD = new PeakMSD(peakModel);
				addTarget(peakMSD, csvRecord.get(HEADER_NAME));
				peakMSD.addAllIntegrationEntries(new IntegrationEntry(nf.parse(csvRecord.get(HEADER_AREA)).doubleValue()));
				result.addPeak(peakMSD);
			}
		}
		return result;
	}

	private static void addTarget(IPeak peak, String name) {

		ILibraryInformation libraryInformation = new LibraryInformation();
		libraryInformation.setName(name);
		IComparisonResult comparisonResult = ComparisonResult.COMPARISON_RESULT_BEST_MATCH;
		IIdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, comparisonResult);
		peak.getTargets().add(identificationTarget);
	}

	private static IPeakMassSpectrum parseMassSpectrum(String headerMz) {

		PeakMassSpectrum massSpectrum = new PeakMassSpectrum();
		SEPERATOR_RECORD_PATTERN.splitAsStream(headerMz).spliterator().forEachRemaining(csvRecord -> {
			String[] values = SEPERATOR_VALUE_PATTERN.split(csvRecord, 2);
			double ion = Double.parseDouble(values[0]);
			float intensity = Float.parseFloat(values[1]);
			massSpectrum.addIon(new Ion(ion, intensity));
		});
		return massSpectrum;
	}

	private static IPeakIntensityValues parseIntensityValues(String headerIntensity) {

		PeakIntensityValues intensityValues = new PeakIntensityValues(Float.MAX_VALUE);
		SEPERATOR_RECORD_PATTERN.splitAsStream(headerIntensity).spliterator().forEachRemaining(csvRecord -> {
			String[] values = SEPERATOR_VALUE_PATTERN.split(csvRecord, 2);
			int rt = Integer.parseInt(values[0]);
			float abundance = Float.parseFloat(values[1]);
			intensityValues.addIntensityValue(rt, abundance);
		});
		intensityValues.normalize();
		return intensityValues;
	}

	/**
	 * Returns the best hit or "".
	 * 
	 * @param peak
	 * @return
	 */
	public static String getName(IPeak peak) {

		ILibraryInformation libraryInformation = IIdentificationTarget.getLibraryInformation(peak);
		if(libraryInformation != null) {
			return libraryInformation.getName();
		}

		return "";
	}
}
