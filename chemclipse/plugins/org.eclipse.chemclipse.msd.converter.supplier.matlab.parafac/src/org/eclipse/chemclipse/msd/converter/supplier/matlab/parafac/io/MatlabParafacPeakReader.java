/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.msd.converter.io.IPeakReader;
import org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.internal.converter.MatlabParafac;
import org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.internal.converter.ParseStatus;
import org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.internal.converter.PeakSupport;
import org.eclipse.chemclipse.msd.model.core.IPeakIon;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.msd.model.core.PeaksMSD;
import org.eclipse.chemclipse.msd.model.implementation.PeakIon;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.IProcessingMessage;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.core.runtime.IProgressMonitor;

public class MatlabParafacPeakReader implements IPeakReader {

	private static final Logger logger = Logger.getLogger(MatlabParafacPeakReader.class);
	private ParseStatus parseStatus;

	@Override
	public IProcessingInfo<IPeaksMSD> read(File file, IProgressMonitor monitor) throws IOException {

		IProcessingInfo<IPeaksMSD> processingInfo = new ProcessingInfo<>();
		validateContent(file, processingInfo);
		readPeaks(file, processingInfo);
		return processingInfo;
	}

	private void validateContent(File file, IProcessingInfo<?> processingInfo) throws IOException {

		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String firstLine = bufferedReader.readLine();
		bufferedReader.close();
		fileReader.close();
		if(!firstLine.equals(MatlabParafac.PEAK_IDENTIFIER)) {
			IProcessingMessage processingMessage = new ProcessingMessage(MessageType.ERROR, "Import Peak", "The given file contains no valid *.mpl peak list: " + file);
			processingInfo.addMessage(processingMessage);
		}
	}

	private void readPeaks(File file, IProcessingInfo<IPeaksMSD> processingInfo) throws IOException, IllegalArgumentException {

		try (FileReader fileReader = new FileReader(file)) {
			try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
				String line;
				IPeaksMSD peaks = new PeaksMSD();
				PeakSupport peakSupport = null;
				/*
				 * Parse each line the file
				 */
				while((line = bufferedReader.readLine()) != null) {
					/*
					 * If the line is empty, continue
					 */
					if(line.equals("")) {
						continue;
					}
					/*
					 * Start a peak start.
					 */
					if(line.equals(MatlabParafac.PEAK_IDENTIFIER)) {
						/*
						 * Add an existing peak and switch to the next.
						 */
						if(peakSupport != null) {
							addPeak(peaks, peakSupport, processingInfo, file);
						}
						peakSupport = new PeakSupport();
						parseStatus = ParseStatus.DESCRIPTION;
					}
					parseLine(line, peakSupport, processingInfo);
				}
				/*
				 * Don't forget to add the last peak.
				 */
				addPeak(peaks, peakSupport, processingInfo, file);
				processingInfo.setProcessingResult(peaks);
				/*
				 * Close the streams
				 */
			}
		}
	}

	private void addPeak(IPeaksMSD peaks, PeakSupport peakSupport, IProcessingInfo<?> processingInfo, File file) {

		IProcessingMessage processingMessage;
		try {
			peaks.addPeak(peakSupport.getPeak());
			processingMessage = new ProcessingMessage(MessageType.INFO, "Import Peak", getMessage("Successfully imported the peak", peakSupport.getModelDescription(), file.getAbsolutePath()));
			processingInfo.addMessage(processingMessage);
		} catch(PeakException e) {
			processingMessage = new ProcessingMessage(MessageType.WARN, "Import Peak", getMessage("The peak couldn't be created", peakSupport.getModelDescription(), file.getAbsolutePath()));
			processingInfo.addMessage(processingMessage);
			logger.warn(e);
		}
	}

	private String getMessage(String message, String peakDescription, String file) {

		StringBuilder builder = new StringBuilder();
		builder.append(message);
		builder.append(" :'");
		builder.append(peakDescription);
		builder.append("' from file: '");
		builder.append(file);
		builder.append("'");
		return builder.toString();
	}

	/**
	 * Parses each line and tries to set the description, m/z and elution profile values.
	 * 
	 * @param line
	 * @param peakSupport
	 */
	private void parseLine(String line, PeakSupport peakSupport, IProcessingInfo<?> processingInfo) {

		/*
		 * 
		 */
		if(line.startsWith(MatlabParafac.COMMENT)) {
			/*
			 * Determine the values to be parsed.
			 */
			if(line.startsWith(MatlabParafac.MASS_SPECTRUM)) {
				parseStatus = ParseStatus.MASS_SPECTRUM;
			} else if(line.startsWith(MatlabParafac.ELUTION_PROFILE)) {
				parseStatus = ParseStatus.ELUTION_PROFILE;
			}
		} else {
			/*
			 * Parse the values.
			 */
			String[] values = line.split(MatlabParafac.VALUE_DELIMITER);
			switch(parseStatus) {
				case DESCRIPTION:
					if(values[0] != null && values[0].equals(MatlabParafac.DESCRIPTION)) {
						peakSupport.setModelDescription(values[1]);
					}
					break;
				case MASS_SPECTRUM:
					int ion = Integer.parseInt(values[0]);
					float abundance = Float.parseFloat(values[1]);
					IPeakIon peakIon = new PeakIon(ion, abundance);
					peakSupport.getPeakMaximum().addIon(peakIon);
					break;
				case ELUTION_PROFILE:
					int retentionTime = Integer.parseInt(values[0]);
					float relativeIntensity = Float.parseFloat(values[1]);
					peakSupport.getPeakIntensityValues().addIntensityValue(retentionTime, relativeIntensity);
					break;
			}
		}
	}
}
