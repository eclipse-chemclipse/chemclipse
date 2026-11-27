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
 *******************************************************************************/
package org.eclipse.chemclipse.converter.quantitation;

import java.io.File;

import org.eclipse.chemclipse.converter.core.Converter;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.quantitation.IQuantitationDatabase;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

public class QuantDBConverter {

	/*
	 * Keep in sync with:
	 * org.eclipse.chemclipse.xxd.converter.supplier.chemclipse
	 */
	public static final String DEFAULT_QUANT_DB_CONVERTER_ID = "org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.quantitationDatabaseSupplier";
	public static final String DEFAULT_QUANT_DB_FILE_NAME = "QuantitationDatabase.ocq";
	public static final String[] DEFAULT_QUANT_DB_FILE_EXTENSIONS = new String[]{"*.ocq"};
	public static final String[] DEFAULT_QUANT_DB_FILE_NAMES = new String[]{"Quantitation Database (*.ocq)"};

	private static final Logger logger = Logger.getLogger(QuantDBConverter.class);
	private static final String EXTENSION_POINT = "org.eclipse.chemclipse.converter.quantitationDatabaseSupplier";
	/*
	 * These are the attributes of the extension point elements.
	 */
	private static final String ID = "id"; //$NON-NLS-1$
	private static final String DESCRIPTION = "description"; //$NON-NLS-1$
	private static final String FILTER_NAME = "filterName"; //$NON-NLS-1$
	private static final String FILE_EXTENSION = "fileExtension"; //$NON-NLS-1$
	private static final String FILE_NAME = "fileName"; //$NON-NLS-1$
	private static final String IMPORT_CONVERTER = "importConverter"; //$NON-NLS-1$
	private static final String EXPORT_CONVERTER = "exportConverter"; //$NON-NLS-1$
	private static final String IS_EXPORTABLE = "isExportable"; //$NON-NLS-1$
	private static final String IS_IMPORTABLE = "isImportable"; //$NON-NLS-1$

	/**
	 * This class has only static methods.
	 */
	private QuantDBConverter() {

	}

	public static IProcessingInfo<IQuantitationDatabase> convert(final File file, IProgressMonitor monitor) {

		QuantDBConverterSupport converterSupport = getQuantDBConverterSupport();
		for(ISupplier supplier : converterSupport.getSupplier()) {

			IProcessingInfo<IQuantitationDatabase> processinInfo = convert(file, supplier.getId(), monitor);
			IQuantitationDatabase quantitationDatabase = processinInfo.getProcessingResult();
			if(quantitationDatabase != null) {
				return processinInfo;
			}
		}

		return getNoImportConverterAvailableProcessingInfo(file);
	}

	public static IProcessingInfo<IQuantitationDatabase> convert(final File file, final String converterId, IProgressMonitor monitor) {

		IProcessingInfo<IQuantitationDatabase> processingInfo;
		IQuantDBImportConverter importConverter = getImportConverter(converterId);
		if(importConverter != null) {
			processingInfo = importConverter.convert(file, monitor);
		} else {
			processingInfo = getNoImportConverterAvailableProcessingInfo(file);
		}
		return processingInfo;
	}

	public static IProcessingInfo<File> convert(File file, IQuantitationDatabase quantitationDatabase, String converterId, IProgressMonitor monitor) {

		IProcessingInfo<File> processingInfo = null;
		QuantDBConverterSupport converterSupport = getQuantDBConverterSupport();
		exitloop:
		for(ISupplier supplier : converterSupport.getSupplier()) {
			if(supplier.isExportable() && supplier.getId().equals(converterId)) {
				IQuantDBExportConverter exportConverter = getExportConverter(converterId);
				processingInfo = exportConverter.convert(file, quantitationDatabase, monitor);
				break exitloop;
			}
		}

		if(processingInfo == null) {
			processingInfo = getNoExportConverterAvailableProcessingInfo(file);
		}

		return processingInfo;
	}

	private static IQuantDBImportConverter getImportConverter(final String converterId) {

		IConfigurationElement element;
		element = getConfigurationElement(converterId);
		IQuantDBImportConverter instance = null;
		if(element != null) {
			try {
				instance = (IQuantDBImportConverter)element.createExecutableExtension(IMPORT_CONVERTER);
			} catch(CoreException e) {
				logger.error(e);
			}
		}
		return instance;
	}

	private static IQuantDBExportConverter getExportConverter(final String converterId) {

		IConfigurationElement element;
		element = getConfigurationElement(converterId);
		IQuantDBExportConverter instance = null;
		if(element != null) {
			try {
				instance = (IQuantDBExportConverter)element.createExecutableExtension(EXPORT_CONVERTER);
			} catch(CoreException e) {
				logger.error(e);
			}
		}
		return instance;
	}

	private static IConfigurationElement getConfigurationElement(final String converterId) {

		if("".equals(converterId)) {
			return null;
		}
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : elements) {
			if(element.getAttribute(ID).equals(converterId)) {
				return element;
			}
		}
		return null;
	}

	public static QuantDBConverterSupport getQuantDBConverterSupport() {

		QuantDBSupplier supplier;
		QuantDBConverterSupport converterSupport = new QuantDBConverterSupport();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : extensions) {

			supplier = new QuantDBSupplier();
			supplier.setFileExtension(element.getAttribute(FILE_EXTENSION));
			supplier.setFileName(element.getAttribute(FILE_NAME));
			supplier.setId(element.getAttribute(ID));
			supplier.setDescription(element.getAttribute(DESCRIPTION));
			supplier.setFilterName(element.getAttribute(FILTER_NAME));
			supplier.setExportable(Boolean.valueOf(element.getAttribute(IS_EXPORTABLE)));
			supplier.setImportable(Boolean.valueOf(element.getAttribute(IS_IMPORTABLE)));
			supplier.setMagicNumberMatcher(Converter.getMagicNumberMatcher(element));
			supplier.setFileContentMatcher(Converter.getFileContentMatcher(element));
			converterSupport.add(supplier);
		}
		return converterSupport;
	}

	private static <R> IProcessingInfo<R> getNoImportConverterAvailableProcessingInfo(File file) {

		IProcessingInfo<R> processingInfo = new ProcessingInfo<>();
		processingInfo.addErrorMessage("QuantDB Import Converter", "There is no suitable converter available to load the database from the file: " + file.getAbsolutePath());
		return processingInfo;
	}

	private static <R> IProcessingInfo<R> getNoExportConverterAvailableProcessingInfo(File file) {

		IProcessingInfo<R> processingInfo = new ProcessingInfo<>();
		processingInfo.addErrorMessage("QuantDB Export Converter", "There is no suitable converter available to write the database to the file: " + file.getAbsolutePath());
		return processingInfo;
	}
}
