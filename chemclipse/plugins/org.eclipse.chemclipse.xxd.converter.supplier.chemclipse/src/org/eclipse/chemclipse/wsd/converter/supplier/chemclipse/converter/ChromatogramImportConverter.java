/*******************************************************************************
 * Copyright (c) 2014, 2015 Dr. Philip Wenig.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.chemclipse.converter;

import java.io.File;

import org.eclipse.chemclipse.converter.processing.chromatogram.ChromatogramOverviewImportConverterProcessingInfo;
import org.eclipse.chemclipse.converter.processing.chromatogram.IChromatogramOverviewImportConverterProcessingInfo;
import org.eclipse.chemclipse.csd.converter.io.IChromatogramCSDReader;
import org.eclipse.chemclipse.csd.converter.supplier.chemclipse.io.ChromatogramReaderFID;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.chromatogram.AbstractChromatogramWSDImportConverter;
import org.eclipse.chemclipse.wsd.converter.io.IChromatogramWSDReader;
import org.eclipse.chemclipse.wsd.converter.processing.chromatogram.ChromatogramWSDImportConverterProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.processing.chromatogram.IChromatogramWSDImportConverterProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.supplier.chemclipse.io.ChromatogramReaderWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.internal.support.IConstants;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.internal.support.SpecificationValidator;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramImportConverter extends AbstractChromatogramWSDImportConverter {

	private static final Logger logger = Logger.getLogger(ChromatogramImportConverter.class);

	@Override
	public IChromatogramWSDImportConverterProcessingInfo convert(File file, IProgressMonitor monitor) {

		IChromatogramWSDImportConverterProcessingInfo processingInfo = new ChromatogramWSDImportConverterProcessingInfo();
		/*
		 * Validate the file.
		 */
		IProcessingInfo processingInfoValidate = super.validate(file);
		if(processingInfoValidate.hasErrorMessages()) {
			processingInfo.addMessages(processingInfoValidate);
		} else {
			/*
			 * Read the chromatogram.
			 */
			file = SpecificationValidator.validateSpecification(file);
			IChromatogramWSDReader reader = new ChromatogramReaderWSD();
			monitor.subTask(IConstants.IMPORT_CHROMATOGRAM);
			try {
				IChromatogramWSD chromatogram = reader.read(file, monitor);
				processingInfo.setChromatogram(chromatogram);
			} catch(Exception e) {
				logger.warn(e);
				processingInfo.addErrorMessage(IConstants.DESCRIPTION_IMPORT, "Something has definitely gone wrong with the file: " + file.getAbsolutePath());
			}
		}
		return processingInfo;
	}

	@Override
	public IChromatogramOverviewImportConverterProcessingInfo convertOverview(File file, IProgressMonitor monitor) {

		IChromatogramOverviewImportConverterProcessingInfo processingInfo = new ChromatogramOverviewImportConverterProcessingInfo();
		/*
		 * Validate the file.
		 */
		IProcessingInfo processingInfoValidate = super.validate(file);
		if(processingInfoValidate.hasErrorMessages()) {
			processingInfo.addMessages(processingInfoValidate);
		} else {
			/*
			 * Read the chromatogram overview.
			 */
			file = SpecificationValidator.validateSpecification(file);
			IChromatogramCSDReader reader = new ChromatogramReaderFID();
			monitor.subTask(IConstants.IMPORT_CHROMATOGRAM_OVERVIEW);
			try {
				IChromatogramOverview chromatogramOverview = reader.readOverview(file, monitor);
				processingInfo.setChromatogramOverview(chromatogramOverview);
			} catch(Exception e) {
				logger.warn(e);
				processingInfo.addErrorMessage(IConstants.DESCRIPTION_IMPORT, "Something has definitely gone wrong with the file: " + file.getAbsolutePath());
			}
		}
		return processingInfo;
	}
}
