/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.scf.io;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotReadableException;
import org.eclipse.chemclipse.converter.io.support.DataArrayReader;
import org.eclipse.chemclipse.converter.io.support.IDataArrayReader;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.wsd.converter.io.AbstractChromatogramWSDReader;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.internal.support.HeaderArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.internal.support.IHeaderArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.internal.support.ISamplePointsByteArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.internal.support.ISamplePointsShortArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.internal.support.ISequenceInformationArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.internal.support.SamplePointsByteArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.internal.support.SamplePointsShortArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.internal.support.SequenceInformationArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.model.IVendorChromatogram;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.model.IVendorScan;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.model.VendorChromatogram;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.model.VendorScan;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.model.VendorScanSignalDAD;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.model.Version;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.core.runtime.IProgressMonitor;

/*
 * Simon Dear, Rodger Staden (1992).
 * A standard file format for data from DNA sequencing instruments.
 * DNA Sequence, 3(2), 107–110.
 * https://doi.org/10.3109/10425179209034003
 */
public class ChromatogramReader extends AbstractChromatogramWSDReader {

	private static final Logger logger = Logger.getLogger(ChromatogramReader.class);
	//
	private int numberSamples;
	private int offsetSamples;
	private int sampleSize;
	private int numberBases;
	private int offsetBases;
	private int offsetComments;
	private int sizeComments;
	private int offsetPrivate;
	private int sizePrivate;

	@Override
	public IChromatogramWSD read(File file, IProgressMonitor monitor) throws IOException {

		return readChromatogram(file);
	}

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		return readChromatogram(file);
	}

	private IChromatogramWSD readChromatogram(File file) throws IOException {

		IVendorChromatogram chromatogram = new VendorChromatogram();
		chromatogram.setConverterId("SCF"); // to be exportable
		chromatogram.setFile(file);
		readHeader(file, chromatogram);
		if(chromatogram.getVersion().getMajor() != 3) {
			throw new NotImplementedException("Only version 3 is supported.");
		}
		if(numberSamples > 0) {
			readSamples(file, chromatogram);
		}
		if(numberBases > 0) {
			readSequenceInformation(file, chromatogram);
		}
		if(sizeComments > 0) {
			readComments(file, chromatogram);
		}
		if(sizePrivate > 0) {
			readPrivateData(file);
		}
		return chromatogram;
	}

	@SuppressWarnings("deprecation")
	private void readHeader(File file, IVendorChromatogram chromatogram) throws IOException {

		IHeaderArrayReader headerArrayReader = new HeaderArrayReader(file);
		headerArrayReader.resetPosition();
		String magicNumber = headerArrayReader.readMagicNumber();
		if(!magicNumber.equals(".scf")) {
			throw new FileIsNotReadableException("Not an SCF trace file.");
		}
		numberSamples = headerArrayReader.readSampleNumber();
		offsetSamples = headerArrayReader.readSampleOffset();
		numberBases = headerArrayReader.readBaseNumber();
		headerArrayReader.readBasesLeftClip();
		headerArrayReader.readBasesRightClip();
		offsetBases = headerArrayReader.readBasesOffset();
		sizeComments = headerArrayReader.readCommentsSize();
		offsetComments = headerArrayReader.readCommentsOffset();
		chromatogram.setVersion(new Version(headerArrayReader.readVersion()));
		sampleSize = headerArrayReader.readSampleSize();
		headerArrayReader.readCodeSet();
		sizePrivate = headerArrayReader.readPrivateSize();
		offsetPrivate = headerArrayReader.readPrivateOffset();
		headerArrayReader.skipSpare();
	}

	private void readSamples(File file, IVendorChromatogram chromatogram) throws IOException {

		if(sampleSize == 1) {
			ISamplePointsByteArrayReader samplesByteArrayReader = new SamplePointsByteArrayReader(file);
			samplesByteArrayReader.resetPosition();
			samplesByteArrayReader.seek(offsetSamples);
			samplesByteArrayReader.readCytosine(numberSamples);
			samplesByteArrayReader.readGuanine(numberSamples);
			samplesByteArrayReader.readThymine(numberSamples);
			throw new NotImplementedException("Sample points with precision 1 are not yet supported.");
		} else if(sampleSize == 2) {
			ISamplePointsShortArrayReader samplesShortArrayReader = new SamplePointsShortArrayReader(file);
			samplesShortArrayReader.resetPosition();
			samplesShortArrayReader.seek(offsetSamples);
			//
			addShortSignals(samplesShortArrayReader.readAdenine(numberSamples), chromatogram);
			chromatogram.setDataName("Adenine");
			//
			IVendorChromatogram referencedChromatogram1 = new VendorChromatogram();
			addShortSignals(samplesShortArrayReader.readCytosine(numberSamples), referencedChromatogram1);
			referencedChromatogram1.setDataName("Cytosine");
			chromatogram.addReferencedChromatogram(referencedChromatogram1);
			//
			IVendorChromatogram referencedChromatogram2 = new VendorChromatogram();
			addShortSignals(samplesShortArrayReader.readGuanine(numberSamples), referencedChromatogram2);
			referencedChromatogram2.setDataName("Guanine");
			chromatogram.addReferencedChromatogram(referencedChromatogram2);
			//
			IVendorChromatogram referencedChromatogram3 = new VendorChromatogram();
			addShortSignals(samplesShortArrayReader.readThymine(numberSamples), referencedChromatogram3);
			referencedChromatogram3.setDataName("Thymine");
			chromatogram.addReferencedChromatogram(referencedChromatogram3);
		}
	}

	private void readSequenceInformation(File file, IVendorChromatogram chromatogram) throws IOException {

		ISequenceInformationArrayReader sequenceInformationArrayReader = new SequenceInformationArrayReader(file);
		sequenceInformationArrayReader.resetPosition();
		sequenceInformationArrayReader.seek(offsetBases);
		sequenceInformationArrayReader.readPeakIndices(numberBases);
		sequenceInformationArrayReader.readProbabilities(numberBases);
		chromatogram.setMiscInfo(new String(sequenceInformationArrayReader.readBaseCalls(numberBases)));
		sequenceInformationArrayReader.readSpares(numberBases);
	}

	private void readComments(File file, IVendorChromatogram chromatogram) throws IOException {

		IDataArrayReader dataArrayReader = new DataArrayReader(file);
		dataArrayReader.resetPosition();
		dataArrayReader.seek(offsetComments);
		String comments = dataArrayReader.readString(sizeComments);
		Matcher fieldMatcher = Pattern.compile("([A-Z0-9]{4})=(.+)").matcher(comments);
		while(fieldMatcher.find()) {
			if(fieldMatcher.groupCount() == 2) {
				String fieldId = fieldMatcher.group(1);
				String value = fieldMatcher.group(2);
				if(fieldId.equals("DATE")) {
					chromatogram.setDate(parseDate(value));
				}
				if(fieldId.equals("NAME")) {
					chromatogram.setSampleName(value);
				}
				if(fieldId.equals("OPER")) {
					chromatogram.setOperator(value);
				}
				if(fieldId.equals("MACH")) {
					chromatogram.setInstrument(value);
				}
			}
		}
	}

	// vendor specific fields
	private void readPrivateData(File file) throws IOException {

		IDataArrayReader dataArrayReader = new DataArrayReader(file);
		dataArrayReader.resetPosition();
		dataArrayReader.seek(offsetPrivate);
		dataArrayReader.readString(sizePrivate);
	}

	// sadly not standardized
	private Date parseDate(String value) {

		String[] formats = {"MMM dd yyyy HH:mm:ss", "EEE dd MMM HH:mm:ss yyyy"};
		try {
			String[] dateParts = value.split(" to ");
			if(dateParts.length > 1) {
				return DateUtils.parseDate(dateParts[1], Locale.ENGLISH, formats);
			} else {
				return DateUtils.parseDate(value, Locale.ENGLISH, formats);
			}
		} catch(ParseException e) {
			logger.warn(e);
		}
		return null;
	}

	private void addShortSignals(short[] samples, IVendorChromatogram chromatogram) {

		short sample = 0;
		sample = 0;
		for(int i = 0; i < numberSamples; i++) {
			samples[i] = (short)(samples[i] + sample);
			sample = samples[i];
		}
		sample = 0;
		for(int i = 0; i < numberSamples; i++) {
			samples[i] = (short)(samples[i] + sample);
			sample = samples[i];
		}
		for(short base : samples) {
			VendorScanSignalDAD scanSignal = new VendorScanSignalDAD();
			scanSignal.setAbsorbance(base);
			IVendorScan scan = new VendorScan();
			scan.addScanSignal(scanSignal);
			chromatogram.addScan(scan);
		}
		chromatogram.recalculateRetentionTimes();
	}
}
