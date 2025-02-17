/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msl;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramExportConverter;
import org.eclipse.chemclipse.converter.chromatogram.IChromatogramExportConverter;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.converter.io.IMassSpectraWriter;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.io.MSLWriter;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.model.support.FilterSupport;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

public class ChromatogramExportConverter extends AbstractChromatogramExportConverter implements IChromatogramExportConverter {

	private static final Logger logger = Logger.getLogger(ChromatogramExportConverter.class);
	private static final String DESCRIPTION = "Chromatogram Combined Scan Converter";

	@Override
	public IProcessingInfo<File> convert(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		IProcessingInfo<File> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages() && chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			try {
				ChromatogramSelectionMSD chromatogramSelectionMSD = new ChromatogramSelectionMSD(chromatogramMSD);
				/*
				 * Create the combined scan.
				 */
				boolean useNormalize = PreferenceSupplier.isUseNormalizedScan();
				CalculationType calculationType = PreferenceSupplier.getCalculationType();
				boolean usePeaksInsteadOfScans = PreferenceSupplier.isUsePeaksInsteadOfScans();
				IScanMSD combinedMassSpectrum = FilterSupport.getCombinedMassSpectrum(chromatogramSelectionMSD, null, useNormalize, calculationType, usePeaksInsteadOfScans);
				/*
				 * Set the chromatogram name and processing comments.
				 */
				StringBuilder builder = new StringBuilder();
				builder.append("Normalize:");
				builder.append(" ");
				builder.append(useNormalize);
				builder.append(", ");
				builder.append("Calculation Type:");
				builder.append(" ");
				builder.append(calculationType.label());
				builder.append(", ");
				builder.append("Use Peaks instead of Scans:");
				builder.append(" ");
				builder.append(usePeaksInsteadOfScans);
				//
				ILibraryInformation libraryInformation = new LibraryInformation();
				libraryInformation.setName(chromatogramSelectionMSD.getChromatogram().getName());
				IIdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, ComparisonResult.COMPARISON_RESULT_BEST_MATCH);
				combinedMassSpectrum.getTargets().add(identificationTarget);
				/*
				 * Export as *.msl file.
				 */
				IMassSpectraWriter massSpectraWriter = new MSLWriter();
				massSpectraWriter.write(file, combinedMassSpectrum, false, monitor);
				processingInfo.setProcessingResult(file);
			} catch(IOException e) {
				logger.error(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.failedToWriteFile, file.getAbsolutePath()));
			} catch(FileIsNotWriteableException e) {
				logger.error(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.fileNotWritable, file.getAbsolutePath()));
			}
		}
		return processingInfo;
	}
}
