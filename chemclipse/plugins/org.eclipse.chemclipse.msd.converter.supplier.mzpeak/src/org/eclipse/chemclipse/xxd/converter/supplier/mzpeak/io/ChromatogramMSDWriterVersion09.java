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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetWriter;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramWriter;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDWriter;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.ArrayIndex;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.ArrayIndexEntry;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.ColumnMapping;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.Cv;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.DataProcessingMethod;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.FileDescription;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.InstrumentConfiguration;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.Metadata;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.MsRun;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.MzPeakIndex;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.Param;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.ProcessingMethod;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.Sample;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.Software;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.SourceFile;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Writes an mzPeak archive in the point layout: centroid spectra go to spectra_peaks.parquet,
 * profile spectra to spectra_data.parquet, and the total ion current to the chromatogram files.
 */
public class ChromatogramMSDWriterVersion09 extends AbstractChromatogramWriter implements IChromatogramMSDWriter {

	private static final Logger logger = Logger.getLogger(ChromatogramMSDWriterVersion09.class);

	public static final String VERSION = "0.9.0";

	private static final String MZPEAK_INDEX = "mzpeak_index.json";
	private static final String SPECTRA_METADATA = "spectra_metadata.parquet";
	private static final String SPECTRA_METADATA_SCANS = "spectra_metadata_scans.parquet";
	private static final String SPECTRA_PEAKS = "spectra_peaks.parquet";
	private static final String SPECTRA_DATA = "spectra_data.parquet";
	private static final String CHROMATOGRAMS_METADATA = "chromatograms_metadata.parquet";
	private static final String CHROMATOGRAMS_DATA = "chromatograms_data.parquet";

	private static final String SPECTRUM = "spectrum";
	private static final String CHROMATOGRAM = "chromatogram";
	private static final String POINT = "point";

	private static final String PARAMETERS = "parameters";
	private static final String AUXILIARY_ARRAYS = "auxiliary_arrays";
	private static final String SCAN_WINDOWS = "scan_windows";

	private static final String SOURCE_FILE_ID = "source_file";
	private static final int INSTRUMENT_CONFIGURATION_ID = 0;
	private static final long TIC_INDEX = 0;

	private static final String MS1_SPECTRUM = "MS:1000579";
	private static final String MSN_SPECTRUM = "MS:1000580";
	private static final String CENTROID_SPECTRUM = "MS:1000127";
	private static final String PROFILE_SPECTRUM = "MS:1000128";
	private static final String TOTAL_ION_CURRENT_CHROMATOGRAM = "MS:1000235";

	private static final String INDEX_ARRAY = "MS:1003870";
	private static final String MZ_ARRAY = "MS:1000514";
	private static final String INTENSITY_ARRAY = "MS:1000515";
	private static final String TIME_ARRAY = "MS:1000595";
	private static final String NON_STANDARD_DATA_ARRAY = "MS:1000786";

	private static final String FLOAT_32_BIT = "MS:1000521";
	private static final String INTEGER_64_BIT = "MS:1000522";
	private static final String FLOAT_64_BIT = "MS:1000523";

	private static final String MZ = "MS:1000040";
	private static final String NUMBER_OF_DETECTOR_COUNTS = "MS:1000131";
	private static final String MINUTE = "UO:0000031";
	private static final String DIMENSIONLESS = "UO:0000186";

	@Override
	public void writeChromatogram(File file, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws IOException {

		List<IScanMSD> scans = getScans(chromatogram);
		monitor.beginTask(ConverterMessages.exportChromatogram, scans.size());
		Path directory = Files.createTempDirectory("mzpeak");
		try {
			MzPeakIndex mzPeakIndex = new MzPeakIndex();
			writeSpectra(directory, scans, mzPeakIndex, monitor);
			writeChromatograms(directory, scans, mzPeakIndex);
			mzPeakIndex.setMetadata(createMetadata(chromatogram, scans));
			writeArchive(file, directory, mzPeakIndex);
		} finally {
			delete(directory);
			monitor.done();
		}
	}

	private static List<IScanMSD> getScans(IChromatogramMSD chromatogram) {

		List<IScanMSD> scans = new ArrayList<>();
		for(IScan scan : chromatogram.getScans()) {
			if(scan instanceof IScanMSD scanMSD) {
				scans.add(scanMSD);
			}
		}
		return scans;
	}

	private static void writeSpectra(Path directory, List<IScanMSD> scans, MzPeakIndex mzPeakIndex, IProgressMonitor monitor) throws IOException {

		boolean hasCentroids = scans.stream().anyMatch(scan -> !isProfile(scan));
		boolean hasProfiles = scans.stream().anyMatch(ChromatogramMSDWriterVersion09::isProfile);
		Map<String, String> arrayIndex = Map.of(SPECTRUM + "_array_index", serialize(createSpectrumArrayIndex()));

		try (ParquetWriter<Group> metadataWriter = ParquetWriterSupport.create(directory.resolve(SPECTRA_METADATA), ParquetWriterSupport.SPECTRUM_METADATA, Map.of());
				ParquetWriter<Group> scansWriter = ParquetWriterSupport.create(directory.resolve(SPECTRA_METADATA_SCANS), ParquetWriterSupport.SPECTRUM_SCANS, Map.of());
				ParquetWriter<Group> peaksWriter = hasCentroids ? ParquetWriterSupport.create(directory.resolve(SPECTRA_PEAKS), ParquetWriterSupport.SPECTRUM_POINTS, arrayIndex) : null;
				ParquetWriter<Group> dataWriter = hasProfiles ? ParquetWriterSupport.create(directory.resolve(SPECTRA_DATA), ParquetWriterSupport.SPECTRUM_POINTS, arrayIndex) : null) {
			SimpleGroupFactory metadataFactory = new SimpleGroupFactory(ParquetWriterSupport.SPECTRUM_METADATA);
			SimpleGroupFactory scansFactory = new SimpleGroupFactory(ParquetWriterSupport.SPECTRUM_SCANS);
			SimpleGroupFactory pointsFactory = new SimpleGroupFactory(ParquetWriterSupport.SPECTRUM_POINTS);
			for(int index = 0; index < scans.size(); index++) {
				IScanMSD scan = scans.get(index);
				List<IIon> ions = new ArrayList<>(scan.getIons());
				ions.sort(Comparator.comparingDouble(IIon::getIon));
				writePoints(isProfile(scan) ? dataWriter : peaksWriter, pointsFactory, index, ions);
				metadataWriter.write(createSpectrumMetadata(metadataFactory, index, scan, ions));
				scansWriter.write(createScan(scansFactory, index, scan));
				monitor.worked(1);
			}
		}

		mzPeakIndex.getFiles().add(createFile(SPECTRA_METADATA, SPECTRUM, "metadata", createSpectrumColumnMapping()));
		mzPeakIndex.getFiles().add(createFile(SPECTRA_METADATA_SCANS, SPECTRUM, "scans", List.of(createColumnMapping("scan start time", "scan_start_time", "MS:1000016", MINUTE, false))));
		if(hasCentroids) {
			mzPeakIndex.getFiles().add(createFile(SPECTRA_PEAKS, SPECTRUM, "peaks", List.of()));
		}
		if(hasProfiles) {
			mzPeakIndex.getFiles().add(createFile(SPECTRA_DATA, SPECTRUM, "data_arrays", List.of()));
		}
	}

	private static void writePoints(ParquetWriter<Group> writer, SimpleGroupFactory factory, long index, List<IIon> ions) throws IOException {

		for(IIon ion : ions) {
			Group row = factory.newGroup();
			row.addGroup(POINT) //
				.append(SPECTRUM + "_index", index) //
				.append("mz", ion.getIon()) //
				.append("intensity", ion.getAbundance());
			writer.write(row);
		}
	}

	private static Group createSpectrumMetadata(SimpleGroupFactory factory, long index, IScanMSD scan, List<IIon> ions) {

		Group row = factory.newGroup();
		row.append("index", index);
		row.append("id", "scan=" + (index + 1));
		short msLevel = getMsLevel(scan);
		row.append("ms_level", msLevel);
		row.append("time", getMinutes(scan));
		Integer polarity = getPolarity(scan);
		if(polarity != null) {
			row.append("scan_polarity", polarity);
		}
		boolean isProfile = isProfile(scan);
		row.append("spectrum_representation", isProfile ? PROFILE_SPECTRUM : CENTROID_SPECTRUM);
		row.append("spectrum_type", msLevel > 1 ? MSN_SPECTRUM : MS1_SPECTRUM);
		if(!ions.isEmpty()) {
			row.append("lowest_observed_mz", scan.getLowestIon().getIon());
			row.append("highest_observed_mz", scan.getHighestIon().getIon());
		}
		row.append(isProfile ? "number_of_data_points" : "number_of_peaks", (long)ions.size());
		IIon basePeak = ions.stream().max(Comparator.comparingDouble(IIon::getAbundance)).orElse(null);
		if(basePeak != null) {
			row.append("base_peak_mz", basePeak.getIon());
			row.append("base_peak_intensity", basePeak.getAbundance());
		}
		row.append("total_ion_current", scan.getTotalSignal());
		ParquetWriterSupport.addEmptyLists(row, PARAMETERS, AUXILIARY_ARRAYS);
		row.append("number_of_auxiliary_arrays", 0);
		return row;
	}

	private static Group createScan(SimpleGroupFactory factory, long index, IScanMSD scan) {

		Group row = factory.newGroup();
		row.append("source_index", index);
		row.append("scan_index", index);
		row.append("scan_start_time", getMinutes(scan));
		row.append("instrument_configuration_id", INSTRUMENT_CONFIGURATION_ID);
		ParquetWriterSupport.addEmptyLists(row, PARAMETERS, SCAN_WINDOWS);
		return row;
	}

	private static void writeChromatograms(Path directory, List<IScanMSD> scans, MzPeakIndex mzPeakIndex) throws IOException {

		try (ParquetWriter<Group> metadataWriter = ParquetWriterSupport.create(directory.resolve(CHROMATOGRAMS_METADATA), ParquetWriterSupport.CHROMATOGRAM_METADATA, Map.of())) {
			Group row = new SimpleGroupFactory(ParquetWriterSupport.CHROMATOGRAM_METADATA).newGroup();
			row.append("index", TIC_INDEX);
			row.append("id", "TIC");
			row.append("chromatogram_type", TOTAL_ION_CURRENT_CHROMATOGRAM);
			row.append("number_of_data_points", (long)scans.size());
			ParquetWriterSupport.addEmptyLists(row, PARAMETERS, AUXILIARY_ARRAYS);
			row.append("number_of_auxiliary_arrays", 0);
			metadataWriter.write(row);
		}

		Map<String, String> arrayIndex = Map.of(CHROMATOGRAM + "_array_index", serialize(createChromatogramArrayIndex()));
		try (ParquetWriter<Group> dataWriter = ParquetWriterSupport.create(directory.resolve(CHROMATOGRAMS_DATA), ParquetWriterSupport.CHROMATOGRAM_POINTS, arrayIndex)) {
			SimpleGroupFactory factory = new SimpleGroupFactory(ParquetWriterSupport.CHROMATOGRAM_POINTS);
			for(IScanMSD scan : scans) {
				Group row = factory.newGroup();
				row.addGroup(POINT) //
					.append(CHROMATOGRAM + "_index", TIC_INDEX) //
					.append("time", getMinutes(scan)) //
					.append("intensity", scan.getTotalSignal()) //
					.append("ms_level", (long)getMsLevel(scan));
				dataWriter.write(row);
			}
		}

		mzPeakIndex.getFiles().add(createFile(CHROMATOGRAMS_METADATA, CHROMATOGRAM, "metadata", List.of( //
				createColumnMapping("chromatogram type", "chromatogram_type", "MS:1000626", null, true), //
				createColumnMapping("number of data points", "number_of_data_points", "MS:1003060", null, false))));
		mzPeakIndex.getFiles().add(createFile(CHROMATOGRAMS_DATA, CHROMATOGRAM, "data_arrays", List.of()));
	}

	private static ArrayIndex createSpectrumArrayIndex() {

		ArrayIndex arrayIndex = new ArrayIndex();
		arrayIndex.setPrefix(POINT);
		arrayIndex.getEntries().add(createArrayIndexEntry(SPECTRUM, POINT + "." + SPECTRUM + "_index", INTEGER_64_BIT, INDEX_ARRAY, "index array", null, "index", 0));
		arrayIndex.getEntries().add(createArrayIndexEntry(SPECTRUM, POINT + ".mz", FLOAT_64_BIT, MZ_ARRAY, "m/z array", MZ, POINT, 1));
		arrayIndex.getEntries().add(createArrayIndexEntry(SPECTRUM, POINT + ".intensity", FLOAT_32_BIT, INTENSITY_ARRAY, "intensity array", NUMBER_OF_DETECTOR_COUNTS, POINT, null));
		return arrayIndex;
	}

	private static ArrayIndex createChromatogramArrayIndex() {

		ArrayIndex arrayIndex = new ArrayIndex();
		arrayIndex.setPrefix(POINT);
		arrayIndex.getEntries().add(createArrayIndexEntry(CHROMATOGRAM, POINT + "." + CHROMATOGRAM + "_index", INTEGER_64_BIT, INDEX_ARRAY, "index array", null, "index", 0));
		arrayIndex.getEntries().add(createArrayIndexEntry(CHROMATOGRAM, POINT + ".time", FLOAT_64_BIT, TIME_ARRAY, "time array", MINUTE, POINT, 1));
		arrayIndex.getEntries().add(createArrayIndexEntry(CHROMATOGRAM, POINT + ".intensity", FLOAT_32_BIT, INTENSITY_ARRAY, "intensity array", NUMBER_OF_DETECTOR_COUNTS, POINT, null));
		arrayIndex.getEntries().add(createArrayIndexEntry(CHROMATOGRAM, POINT + ".ms_level", INTEGER_64_BIT, NON_STANDARD_DATA_ARRAY, "ms level", DIMENSIONLESS, POINT, null));
		return arrayIndex;
	}

	private static ArrayIndexEntry createArrayIndexEntry(String context, String path, String dataType, String arrayType, String arrayName, String unit, String bufferFormat, Integer sortingRank) {

		ArrayIndexEntry entry = new ArrayIndexEntry();
		entry.setContext(context);
		entry.setPath(path);
		entry.setDataType(dataType);
		entry.setArrayType(arrayType);
		entry.setArrayName(arrayName);
		entry.setUnit(unit);
		entry.setBufferFormat(bufferFormat);
		entry.setBufferPriority("primary");
		entry.setSortingRank(sortingRank);
		return entry;
	}

	private static List<ColumnMapping> createSpectrumColumnMapping() {

		return List.of( //
				createColumnMapping("ms level", "ms_level", "MS:1000511", null, false), //
				createColumnMapping("scan polarity", "scan_polarity", "MS:1000465", null, false), //
				createColumnMapping("spectrum representation", "spectrum_representation", "MS:1000525", null, true), //
				createColumnMapping("spectrum type", "spectrum_type", "MS:1000559", null, true), //
				createColumnMapping("lowest observed m/z", "lowest_observed_mz", "MS:1000528", MZ, false), //
				createColumnMapping("highest observed m/z", "highest_observed_mz", "MS:1000527", MZ, false), //
				createColumnMapping("number of data points", "number_of_data_points", "MS:1003060", null, false), //
				createColumnMapping("number of peaks", "number_of_peaks", "MS:1003059", null, false), //
				createColumnMapping("base peak m/z", "base_peak_mz", "MS:1000504", MZ, false), //
				createColumnMapping("base peak intensity", "base_peak_intensity", "MS:1000505", NUMBER_OF_DETECTOR_COUNTS, false), //
				createColumnMapping("total ion current", "total_ion_current", "MS:1000285", NUMBER_OF_DETECTOR_COUNTS, false));
	}

	private static ColumnMapping createColumnMapping(String name, String path, String accession, String unit, boolean termMarker) {

		ColumnMapping columnMapping = new ColumnMapping();
		columnMapping.setName(name);
		columnMapping.setPath(path);
		columnMapping.setAccession(accession);
		columnMapping.setUnit(unit);
		columnMapping.setTermMarker(termMarker);
		return columnMapping;
	}

	private static org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.File createFile(String name, String entityType, String dataKind, List<ColumnMapping> columnMapping) {

		org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.File file = new org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.File();
		file.setName(name);
		file.setEntityType(entityType);
		file.setDataKind(dataKind);
		file.setColumnMapping(new ArrayList<>(columnMapping));
		return file;
	}

	private Metadata createMetadata(IChromatogramMSD chromatogram, List<IScanMSD> scans) {

		Metadata metadata = new Metadata();
		metadata.setVersion(VERSION);
		metadata.setFileDescription(createFileDescription(chromatogram, scans));
		metadata.getInstrumentConfigurationList().add(createInstrumentConfiguration(chromatogram));
		metadata.getDataProcessingMethodList().add(createDataProcessingMethod());
		metadata.getSoftwareList().add(createSoftware());
		String sampleName = chromatogram.getSampleName();
		if(sampleName != null && !sampleName.isBlank()) {
			Sample sample = new Sample();
			sample.setId("sample_1");
			sample.setName(sampleName);
			sample.getParameters().add(createParam("sample name", "MS:1000002", sampleName));
			metadata.getSampleList().add(sample);
		}
		metadata.getCvList().add(createCv("MS", "4.1.249", "Proteomics Standards Initiative Mass Spectrometry Ontology", "http://purl.obolibrary.org/obo/ms/4.1.249/psi-ms.obo"));
		metadata.getCvList().add(createCv("UO", "2026-01-16", "Units of measurement ontology", "http://purl.obolibrary.org/obo/uo/releases/2026-01-16/uo.obo"));
		metadata.setRun(createRun(chromatogram, metadata.getFileDescription()));
		return metadata;
	}

	private static FileDescription createFileDescription(IChromatogramMSD chromatogram, List<IScanMSD> scans) {

		FileDescription fileDescription = new FileDescription();
		if(scans.stream().anyMatch(scan -> getMsLevel(scan) <= 1)) {
			fileDescription.getContents().add(createParam("MS1 spectrum", MS1_SPECTRUM, null));
		}
		if(scans.stream().anyMatch(scan -> getMsLevel(scan) > 1)) {
			fileDescription.getContents().add(createParam("MSn spectrum", MSN_SPECTRUM, null));
		}
		File file = chromatogram.getFile();
		if(file != null) {
			SourceFile sourceFile = new SourceFile();
			sourceFile.setId(SOURCE_FILE_ID);
			sourceFile.setName(file.getName());
			File parent = file.getAbsoluteFile().getParentFile();
			sourceFile.setLocation(parent != null ? parent.toPath().toUri().toString() : "");
			sourceFile.getParameters().add(createParam("scan number only nativeID format", "MS:1000776", null));
			fileDescription.getSourceFiles().add(sourceFile);
		}
		return fileDescription;
	}

	private static InstrumentConfiguration createInstrumentConfiguration(IChromatogramMSD chromatogram) {

		InstrumentConfiguration instrumentConfiguration = new InstrumentConfiguration();
		instrumentConfiguration.setId(INSTRUMENT_CONFIGURATION_ID);
		instrumentConfiguration.setSoftwareReference(getSoftwareName());
		instrumentConfiguration.getParameters().add(createParam("instrument model", "MS:1000031", null));
		String instrument = chromatogram.getInstrument();
		if(instrument != null && !instrument.isBlank()) {
			/*
			 * Like mzML, a model the vocabulary does not know is named by a user parameter.
			 */
			instrumentConfiguration.getParameters().add(createParam(instrument, null, null));
		}
		return instrumentConfiguration;
	}

	private static DataProcessingMethod createDataProcessingMethod() {

		ProcessingMethod processingMethod = new ProcessingMethod();
		processingMethod.setOrder(0);
		processingMethod.setSoftwareReference(getSoftwareName());
		processingMethod.getParameters().add(createParam("file format conversion", "MS:1000530", null));
		DataProcessingMethod dataProcessingMethod = new DataProcessingMethod();
		dataProcessingMethod.setId(getDataProcessingId());
		dataProcessingMethod.getMethods().add(processingMethod);
		return dataProcessingMethod;
	}

	private Software createSoftware() {

		Software software = new Software();
		String productName = getSoftwareName();
		software.setId(getSoftwareName());
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		if(bundle != null) {
			Version version = bundle.getVersion();
			software.setVersion(version.getMajor() + "." + version.getMinor() + "." + version.getMicro());
		}
		software.getParameters().add(createParam(productName, "MS:1003376", null));
		return software;
	}

	private static MsRun createRun(IChromatogramMSD chromatogram, FileDescription fileDescription) {

		MsRun run = new MsRun();
		String name = chromatogram.getName();
		run.setId(name != null && !name.isBlank() ? name : "run");
		run.setDefaultDataProcessingId(getDataProcessingId());
		run.setDefaultInstrumentId(INSTRUMENT_CONFIGURATION_ID);
		if(!fileDescription.getSourceFiles().isEmpty()) {
			run.setDefaultSourceFileId(SOURCE_FILE_ID);
		}
		run.setStartTime(chromatogram.getDate());
		return run;
	}

	private static Param createParam(String name, String accession, String value) {

		Param param = new Param();
		param.setName(name);
		param.setAccession(accession);
		param.setValue(value);
		return param;
	}

	private static Cv createCv(String id, String version, String fullName, String uri) {

		Cv cv = new Cv();
		cv.setId(id);
		cv.setVersion(version);
		cv.setFullName(fullName);
		cv.setUri(uri);
		return cv;
	}

	/**
	 * mzPeak requires the members of the ZIP archive to be stored uncompressed, so they can be read
	 * in place. A stored entry has to announce its size and checksum before its content.
	 */
	private static void writeArchive(File file, Path directory, MzPeakIndex mzPeakIndex) throws IOException {

		try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(file.toPath())))) {
			zipOutputStream.setMethod(ZipOutputStream.STORED);
			for(org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.File member : mzPeakIndex.getFiles()) {
				Path path = directory.resolve(member.getName());
				zipOutputStream.putNextEntry(createStoredEntry(member.getName(), Files.size(path), getChecksum(path)));
				Files.copy(path, zipOutputStream);
				zipOutputStream.closeEntry();
			}
			byte[] index = createObjectMapper().writeValueAsBytes(mzPeakIndex);
			CRC32 crc = new CRC32();
			crc.update(index);
			zipOutputStream.putNextEntry(createStoredEntry(MZPEAK_INDEX, index.length, crc.getValue()));
			zipOutputStream.write(index);
			zipOutputStream.closeEntry();
		}
	}

	private static ZipEntry createStoredEntry(String name, long size, long crc) {

		ZipEntry zipEntry = new ZipEntry(name);
		zipEntry.setMethod(ZipEntry.STORED);
		zipEntry.setSize(size);
		zipEntry.setCompressedSize(size);
		zipEntry.setCrc(crc);
		return zipEntry;
	}

	private static long getChecksum(Path path) throws IOException {

		try (InputStream inputStream = new CheckedInputStream(Files.newInputStream(path), new CRC32())) {
			inputStream.transferTo(OutputStream.nullOutputStream());
			return ((CheckedInputStream)inputStream).getChecksum().getValue();
		}
	}

	private static ObjectMapper createObjectMapper() {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // RFC 3339
		return objectMapper;
	}

	private static String serialize(Object object) throws IOException {

		return createObjectMapper().writeValueAsString(object);
	}

	private static boolean isProfile(IScanMSD scan) {

		return scan instanceof IRegularMassSpectrum massSpectrum && massSpectrum.getMassSpectrumType() == MassSpectrumType.PROFILE;
	}

	private static short getMsLevel(IScanMSD scan) {

		if(scan instanceof IRegularMassSpectrum massSpectrum && massSpectrum.getMassSpectrometer() > 1) {
			return massSpectrum.getMassSpectrometer();
		}
		return 1;
	}

	private static Integer getPolarity(IScanMSD scan) {

		if(scan instanceof IRegularMassSpectrum massSpectrum) {
			if(massSpectrum.getPolarity() == Polarity.POSITIVE) {
				return 1;
			} else if(massSpectrum.getPolarity() == Polarity.NEGATIVE) {
				return -1;
			}
		}
		return null;
	}

	private static double getMinutes(IScan scan) {

		return scan.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR;
	}

	private static void delete(Path directory) {

		try (Stream<Path> paths = Files.walk(directory)) {
			paths.sorted(Comparator.reverseOrder()).forEach(path -> path.toFile().delete());
		} catch(IOException e) {
			logger.warn(e);
		}
	}

	private static String getSoftwareName() {

		if(Platform.getProduct() != null) {
			return Platform.getProduct().getName().toLowerCase();
		} else {
			return "";
		}
	}

	private static String getDataProcessingId() {

		if(Platform.getProduct() != null) {
			return Platform.getProduct().getName().toLowerCase() + "_conversion";
		} else {
			return "";
		}
	}
}
