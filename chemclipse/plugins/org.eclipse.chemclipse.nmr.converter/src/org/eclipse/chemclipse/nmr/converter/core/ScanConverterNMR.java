/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics, Logging
 * Christoph Läubrich - Adjust to new API
 *******************************************************************************/
package org.eclipse.chemclipse.nmr.converter.core;

import java.io.File;
import java.util.List;

import org.eclipse.chemclipse.converter.core.Converter;
import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.converter.scan.IScanConverterSupport;
import org.eclipse.chemclipse.converter.scan.ScanConverterSupport;
import org.eclipse.chemclipse.converter.scan.ScanSupplier;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.nmr.model.core.ISpectrumNMR;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubMonitor;

public class ScanConverterNMR {

	private static final Logger logger = Logger.getLogger(ScanConverterNMR.class);
	private static final String EXTENSION_POINT = "org.eclipse.chemclipse.nmr.converter.scanSupplier";

	/**
	 * This class has only static methods.
	 */
	private ScanConverterNMR() {

	}

	public static IProcessingInfo<ISpectrumNMR> convert(final File file, final String converterId, final IProgressMonitor monitor) {

		IProcessingInfo<ISpectrumNMR> processingInfo;
		/*
		 * Do not use a safe runnable here, because an object must
		 * be returned or null.
		 */
		IScanImportConverter importConverter = getScanImportConverter(converterId);
		if(importConverter != null) {
			processingInfo = importConverter.convert(file, monitor);
		} else {
			processingInfo = getProcessingError(file);
		}
		return processingInfo;
	}

	public static IProcessingInfo<ISpectrumNMR> convert(final File file, final IProgressMonitor monitor) {

		IScanConverterSupport converterSupport = getScanConverterSupport();
		try {
			List<String> availableConverterIds = converterSupport.getAvailableConverterIds(file);
			SubMonitor subMonitor = SubMonitor.convert(monitor, availableConverterIds.size());
			for(String converterId : availableConverterIds) {
				IScanImportConverter importConverter = getScanImportConverter(converterId);
				if(importConverter != null) {
					IProcessingInfo<ISpectrumNMR> processingInfo = importConverter.convert(file, subMonitor.split(1));
					if(processingInfo != null) {
						if(!processingInfo.hasErrorMessages()) {
							return processingInfo;
						}
					}
				}
			}
		} catch(NoConverterAvailableException e) {
			logger.info(e);
		}

		return getProcessingError(file);
	}

	public static IProcessingInfo<?> export(final File file, final IComplexSignalMeasurement<?> measurement, final String converterId, final IProgressMonitor monitor) {

		try {
			IScanExportConverter exportConverter = getScanExportConverter(converterId);
			if(exportConverter != null) {
				ProcessingInfo<Void> result = new ProcessingInfo<>();
				exportConverter.convert(file, measurement, result, monitor);
				return result;
			}
			return getProcessingError(file);
		} catch(Exception e) {
			ProcessingInfo<Void> processingInfo = new ProcessingInfo<>();
			processingInfo.addErrorMessage(converterId, "Failed to export");
			logger.error(e);
			return processingInfo;
		}
	}

	private static IScanImportConverter getScanImportConverter(final String converterId) {

		IConfigurationElement element;
		element = getConfigurationElement(converterId);
		IScanImportConverter instance = null;
		if(element != null) {
			try {
				instance = (IScanImportConverter)element.createExecutableExtension(Converter.IMPORT_CONVERTER);
			} catch(CoreException e) {
				logger.error(e);
			}
		}
		return instance;
	}

	private static IScanExportConverter getScanExportConverter(final String converterId) throws CoreException {

		IConfigurationElement element = getConfigurationElement(converterId);
		if(element != null) {
			return (IScanExportConverter)element.createExecutableExtension(Converter.EXPORT_CONVERTER);
		}
		return null;
	}

	private static IConfigurationElement getConfigurationElement(final String converterId) {

		if("".equals(converterId)) {
			return null;
		}
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : elements) {
			if(element.getAttribute(Converter.ID).equals(converterId)) {
				return element;
			}
		}
		return null;
	}

	public static IScanConverterSupport getScanConverterSupport() {

		ScanSupplier supplier;
		ScanConverterSupport converterSupport = new ScanConverterSupport(DataCategory.NMR);
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : extensions) {
			/*
			 * Set the values to the ChromatogramSupplier instance first before
			 * validating them.<br/> Why? Because the return value of
			 * element.getAttribute(...) could be null. If the element is null
			 * and stored in a ChromatogramSupplier instance it will be at least
			 * "".
			 */
			supplier = new ScanSupplier();
			supplier.setFileExtension(element.getAttribute(Converter.FILE_EXTENSION));
			supplier.setFileName(element.getAttribute(Converter.FILE_NAME));
			supplier.setDirectoryExtension(element.getAttribute(Converter.DIRECTORY_EXTENSION));
			/*
			 * Check if these values contain not allowed characters. If yes than
			 * do not add the supplier to the supported converter list.
			 */
			if(Converter.isValid(supplier.getFileExtension()) && Converter.isValid(supplier.getFileName()) && Converter.isValid(supplier.getDirectoryExtension())) {
				supplier.setId(element.getAttribute(Converter.ID));
				supplier.setDescription(element.getAttribute(Converter.DESCRIPTION));
				supplier.setFilterName(element.getAttribute(Converter.FILTER_NAME));
				supplier.setExportable(Boolean.valueOf(element.getAttribute(Converter.IS_EXPORTABLE)));
				supplier.setImportable(Boolean.valueOf(element.getAttribute(Converter.IS_IMPORTABLE)));
				supplier.setMagicNumberMatcher(Converter.getMagicNumberMatcher(element));
				supplier.setFileContentMatcher(Converter.getFileContentMatcher(element));
				converterSupport.add(supplier);
			}
		}
		return converterSupport;
	}

	private static IProcessingInfo<ISpectrumNMR> getProcessingError(File file) {

		IProcessingInfo<ISpectrumNMR> processingInfo = new ProcessingInfo<>();
		processingInfo.addErrorMessage("Scan Converter", "No suitable converter was found for: " + file);
		return processingInfo;
	}
}
