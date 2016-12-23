/*******************************************************************************
 * Copyright (c) 2011, 2016 Dr. Philip Wenig.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IPeaks;
import org.eclipse.chemclipse.model.implementation.Peaks;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.internal.report.PeakReport;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.model.IPeakIdentificationBatchJob;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.model.IPeakInputEntry;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.model.IPeakOutputEntry;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.processing.IPeakIdentificationProcessingInfo;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.processing.PeakIdentificationProcessingInfo;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.report.IPeakIdentificationBatchProcessReport;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.report.PeakIdentificationBatchProcessReport;
import org.eclipse.chemclipse.msd.converter.peak.PeakConverterMSD;
import org.eclipse.chemclipse.msd.converter.processing.peak.IPeakImportConverterProcessingInfo;
import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.PeakIdentifier;
import org.eclipse.chemclipse.chromatogram.msd.identifier.processing.IPeakIdentifierProcessingInfo;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.peaks.PeakIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.processing.IPeakIntegratorProcessingInfo;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.IProcessingMessage;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;

public class PeakIdentificationBatchProcess implements IPeakIdentificationBatchProcess {

	private static final Logger logger = Logger.getLogger(PeakIdentificationBatchProcess.class);

	@Override
	public IPeakIdentificationProcessingInfo execute(IPeakIdentificationBatchJob peakIdentificationBatchJob, IProgressMonitor monitor) {

		IPeakIdentificationProcessingInfo processingInfo = new PeakIdentificationProcessingInfo();
		IPeakIdentificationProcessingInfo peakIdentificationProcessingInfo;
		IProcessingMessage processingMessage;
		File peakInputFile;
		IPeaks peakImports;
		List<IPeakMSD> peaks = new ArrayList<IPeakMSD>();
		IPeakIdentificationBatchProcessReport batchProcessReport = new PeakIdentificationBatchProcessReport();
		/*
		 * Validate
		 */
		if(peakIdentificationBatchJob == null) {
			return null;
		}
		/*
		 * Read the peak entries.
		 */
		for(IPeakInputEntry inputEntry : peakIdentificationBatchJob.getPeakInputEntries()) {
			try {
				peakInputFile = new File(inputEntry.getInputFile());
				IPeakImportConverterProcessingInfo processingPeakImportConverterInfo = loadPeaksFromFile(peakInputFile, monitor);
				processingInfo.addMessages(processingPeakImportConverterInfo);
				try {
					peakImports = processingPeakImportConverterInfo.getPeaks();
					for(IPeak peak : peakImports.getPeaks()) {
						if(peak instanceof IPeakMSD) {
							peaks.add((IPeakMSD)peak);
						}
					}
				} catch(Exception e) {
					logger.warn(e);
				}
			} catch(Exception e) {
				logger.warn(e);
				processingMessage = new ProcessingMessage(MessageType.ERROR, "Batch Process Peaks", "Something has completely gone wrong with the file: " + inputEntry.getInputFile());
				processingInfo.addMessage(processingMessage);
			}
		}
		/*
		 * Integrate and Identify the peaks
		 */
		peakIdentificationProcessingInfo = processPeaks(peaks, peakIdentificationBatchJob, batchProcessReport, monitor);
		processingInfo.addMessages(peakIdentificationProcessingInfo);
		/*
		 * Export the peaks
		 */
		peakIdentificationProcessingInfo = exportPeaks(peaks, peakIdentificationBatchJob, monitor);
		processingInfo.addMessages(peakIdentificationProcessingInfo);
		/*
		 * Report the results.
		 */
		reportTheResults(peakIdentificationBatchJob, batchProcessReport, monitor);
		processingInfo.setPeakIdentificationBatchProcessReport(batchProcessReport);
		return processingInfo;
	}

	private IPeakImportConverterProcessingInfo loadPeaksFromFile(File peakInputFile, IProgressMonitor monitor) {

		return PeakConverterMSD.convert(peakInputFile, monitor);
	}

	private IPeakIdentificationProcessingInfo processPeaks(List<IPeakMSD> peaks, IPeakIdentificationBatchJob peakIdentificationBatchJob, IPeakIdentificationBatchProcessReport batchProcessReport, IProgressMonitor monitor) {

		IPeakIdentificationProcessingInfo peakIdentificationProcessingInfo = new PeakIdentificationProcessingInfo();
		/*
		 * Integrator
		 */
		String integratorId = peakIdentificationBatchJob.getPeakIntegrationEntry().getProcessorId();
		IPeakIntegratorProcessingInfo processingInfoIntegrator = PeakIntegrator.integrate(peaks, integratorId, monitor);
		peakIdentificationProcessingInfo.addMessages(processingInfoIntegrator);
		/*
		 * Identifier
		 */
		String identifierId = peakIdentificationBatchJob.getPeakIdentificationEntry().getProcessorId();
		IPeakIdentifierProcessingInfo processingInfoIdentifier = PeakIdentifier.identify(peaks, identifierId, monitor);
		peakIdentificationProcessingInfo.addMessages(processingInfoIdentifier);
		/*
		 * Add the peaks to the report.
		 */
		IPeaks reportPeaks = batchProcessReport.getPeaks();
		for(IPeakMSD peak : peaks) {
			reportPeaks.addPeak(peak);
		}
		return peakIdentificationProcessingInfo;
	}

	private IPeakIdentificationProcessingInfo exportPeaks(List<IPeakMSD> peakList, IPeakIdentificationBatchJob peakIdentificationBatchJob, IProgressMonitor monitor) {

		IPeakIdentificationProcessingInfo peakIdentificationProcessingInfo = new PeakIdentificationProcessingInfo();
		List<IPeakOutputEntry> outputEntries = peakIdentificationBatchJob.getPeakOutputEntries();
		for(IPeakOutputEntry outputEntry : outputEntries) {
			String converterId = outputEntry.getConverterId();
			/*
			 * Append the "/" or "\" to the end of the folder if not exists.
			 */
			String outputFolder = outputEntry.getOutputFolder();
			if(!outputFolder.endsWith(File.separator)) {
				outputFolder += File.separator;
			}
			/*
			 * Write the mass spectra.
			 */
			File peakOutputFile = new File(outputFolder + peakIdentificationBatchJob.getName());
			IPeaks peaks = getPeaksInstance(peakList);
			IProcessingInfo processingInfo = PeakConverterMSD.convert(peakOutputFile, peaks, false, converterId, monitor);
			peakIdentificationProcessingInfo.addMessages(processingInfo);
		}
		return peakIdentificationProcessingInfo;
	}

	private IPeaks getPeaksInstance(List<IPeakMSD> peakList) {

		IPeaks peaks = new Peaks();
		for(IPeakMSD peak : peakList) {
			peaks.addPeak(peak);
		}
		return peaks;
	}

	private void reportTheResults(IPeakIdentificationBatchJob peakIdentificationBatchJob, IPeakIdentificationBatchProcessReport batchProcessReport, IProgressMonitor monitor) {

		String folder = peakIdentificationBatchJob.getReportFolder();
		if(!folder.endsWith(File.separator)) {
			folder += File.separator;
		}
		/*
		 * Delete the report file?
		 * If yes, delete, otherwise create a timestamp.
		 */
		String file = folder + peakIdentificationBatchJob.getName();
		File results;
		if(peakIdentificationBatchJob.isOverrideReport()) {
			results = new File(file);
			if(results.exists()) {
				results.delete();
			}
		} else {
			Date now = new Date();
			/*
			 * It's not possible to write files with a colon using windows.
			 */
			file += " - " + now.toString().replace(":", "");
			results = new File(file);
		}
		/*
		 * Process the chromatogram and report the results.
		 */
		try {
			PrintWriter printWriter = new PrintWriter(results, "UTF-8");
			/*
			 * Integrator Report
			 */
			IPeaks peaks = batchProcessReport.getPeaks();
			String integrator = peakIdentificationBatchJob.getPeakIntegrationEntry().getProcessorId();
			String identifier = peakIdentificationBatchJob.getPeakIdentificationEntry().getProcessorId();
			PeakReport.writeResults(peaks, printWriter, integrator, identifier);
			/*
			 * Flush and close.
			 */
			printWriter.flush();
			printWriter.close();
		} catch(FileNotFoundException e) {
			logger.warn(e);
		} catch(UnsupportedEncodingException e) {
			logger.warn(e);
		}
	}
}
