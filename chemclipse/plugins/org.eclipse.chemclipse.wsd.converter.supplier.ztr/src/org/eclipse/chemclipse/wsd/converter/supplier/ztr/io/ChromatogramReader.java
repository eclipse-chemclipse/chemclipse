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
package org.eclipse.chemclipse.wsd.converter.supplier.ztr.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.chemclipse.dsd.converter.io.AbstractChromatogramDSDReader;
import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.chemclipse.dsd.model.core.Nucleobase;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.model.Chunk;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.BasePositionArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.ChunkArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.HeaderArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.IBasePositionArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.IChunkArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.IHeaderArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.IQualityClipArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.ITraceArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.QualityClipArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.TraceArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.model.IVendorChromatogram;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.model.IVendorScan;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.model.VendorChromatogram;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.model.VendorScan;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.model.VendorScanSignalWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.core.runtime.IProgressMonitor;

/*
 * James Bonfield, Rodger Staden (2002).
 * ZTR: a new format for DNA sequence trace data.
 * Bioinformatics, 18(1), 3–10.
 * https://doi.org/10.1093/bioinformatics/18.1.3
 */
public class ChromatogramReader extends AbstractChromatogramDSDReader {

	private static final Logger logger = Logger.getLogger(ChromatogramReader.class);

	@Override
	public IChromatogramDSD read(File file, IProgressMonitor monitor) throws IOException {

		return readChromatogram(file);
	}

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		return readChromatogram(file);
	}

	private IChromatogramDSD readChromatogram(File file) throws IOException {

		IVendorChromatogram chromatogram = new VendorChromatogram();
		chromatogram.setConverterId("ZTR"); // to be exportable
		chromatogram.setFile(file);

		readFile(file, chromatogram);
		readChunks(file, chromatogram);

		return chromatogram;
	}

	private void readFile(File file, IVendorChromatogram chromatogram) throws IOException {

		IHeaderArrayReader headerArrayReader = new HeaderArrayReader(file);
		String magicNumber = headerArrayReader.readMagicNumber(); // "\256ZTR\r\n\032\n"
		if(!magicNumber.contains("ZTR")) {
			return;
		}

		chromatogram.setVersion(headerArrayReader.readVersion());
	}

	private void readChunks(File file, IVendorChromatogram chromatogram) throws IOException {

		IChunkArrayReader chunkArrayReader = new ChunkArrayReader(file);
		chunkArrayReader.skipBytes(10); // header

		IBasePositionArrayReader basePositionArrayReader = null;
		char[] nucleotides = new char[0];

		while(chunkArrayReader.getPosition() < chunkArrayReader.getLength()) {
			Chunk chunk = new Chunk(chunkArrayReader);
			switch(chunk.getType()) {
				case "SAMP": {
					break;
				}
				case "SMP4": {
					byte[] paddedData = removePadding(chunk.getData(), 1); // reserved byte
					ITraceArrayReader traceArrayReader = new TraceArrayReader(paddedData);
					readTraceSignals(traceArrayReader, chromatogram);
					break;
				}
				case "BASE": {
					String baseCalls = new String(chunk.getData(), StandardCharsets.US_ASCII);
					nucleotides = baseCalls.toCharArray();
					break;
				}
				case "BPOS": {
					byte[] paddedData = removePadding(chunk.getData(), 3); // aligns the positions to 4 bytes
					basePositionArrayReader = new BasePositionArrayReader(paddedData);
					break;
				}
				case "CNF4": {
					break;
				}
				case "TEXT": {
					readText(chunk.getData(), chromatogram);
					break;
				}
				case "CLIP": {
					IQualityClipArrayReader qualityClipArrayReader = new QualityClipArrayReader(chunk.getData());
					qualityClipArrayReader.readLeftClip();
					qualityClipArrayReader.readRightClip();
					break;
				}
				case "CR32": {
					break;
				}
				case "COMM": {
					break;
				}
				default:
					logger.warn("Unexpected chunk: " + chunk.getType());
					break;
			}
		}

		if(basePositionArrayReader == null) {
			return;
		}

		for(char letter : nucleotides) {

			int scanNumber = basePositionArrayReader.readBasePosition() + 1;
			IScanWSD scan = chromatogram.getScan(scanNumber);

			ILibraryInformation libraryInformation = new LibraryInformation();
			libraryInformation.setName(String.valueOf(letter));

			IComparisonResult comparisonResult = new ComparisonResult(0); // TODO
			IIdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, comparisonResult);
			identificationTarget.setIdentifier(String.valueOf(letter));
			scan.getTargets().add(identificationTarget); // TODO add to scan signal rather than total signal
		}
	}

	private byte[] removePadding(byte[] data, int padding) {

		if(data.length < padding) {
			return new byte[0];
		}
		return Arrays.copyOfRange(data, padding, data.length);
	}

	private void readText(byte[] data, IVendorChromatogram chromatogram) {

		String[] fields = new String(data, StandardCharsets.US_ASCII).split("\0");
		for(int i = 0; i + 1 < fields.length; i += 2) {
			String identifier = fields[i];
			String value = fields[i + 1];
			switch(identifier) {
				case "NAME": {
					chromatogram.setSampleName(value);
					break;
				}
				case "OPER": {
					chromatogram.setOperator(value);
					break;
				}
				case "MACH": {
					chromatogram.setInstrument(value);
					break;
				}
				case "DATE": {
					chromatogram.setDate(parseDate(value));
					break;
				}
				default:
					break;
			}
		}
	}

	private void readTraceSignals(ITraceArrayReader traceArrayReader, IVendorChromatogram chromatogram) {

		for(int i = 0; i < traceArrayReader.getSamples(); i++) {
			IVendorScan scan = new VendorScan();
			addSignal(scan, 1, traceArrayReader.getAdenine().get(i));
			addSignal(scan, 2, traceArrayReader.getCytosine().get(i));
			addSignal(scan, 3, traceArrayReader.getGuanine().get(i));
			addSignal(scan, 4, traceArrayReader.getThymine().get(i));
			chromatogram.addScan(scan);
		}

		chromatogram.getWavelengthMapping().put(1f, Nucleobase.ADENINE);
		chromatogram.getWavelengthMapping().put(2f, Nucleobase.CYTOSINE);
		chromatogram.getWavelengthMapping().put(3f, Nucleobase.GUANINE);
		chromatogram.getWavelengthMapping().put(4f, Nucleobase.THYMINE);

		chromatogram.recalculateRetentionTimes();

	}

	private void addSignal(IVendorScan scan, float wavelength, int value) {

		VendorScanSignalWSD scanSignal = new VendorScanSignalWSD();
		scanSignal.setWavelength(wavelength);
		scanSignal.setAbsorbance(value);
		scan.addScanSignal(scanSignal);
	}

	// sadly not standardized
	private Date parseDate(String value) {

		String[] formats = {"EEE dd MMM HH:mm:ss yyyy", "MMM dd yyyy HH:mm:ss"};
		try {
			String[] dateParts = value.split(" to ");
			return DateUtils.parseDate(dateParts[0], Locale.ENGLISH, formats);
		} catch(ParseException e) {
			logger.warn(e);
		}
		return null;
	}
}