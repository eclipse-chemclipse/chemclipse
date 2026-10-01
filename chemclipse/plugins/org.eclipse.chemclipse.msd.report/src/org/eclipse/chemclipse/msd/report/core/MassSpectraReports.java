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
package org.eclipse.chemclipse.msd.report.core;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.report.massspectra.IMassSpectraReportGenerator;
import org.eclipse.chemclipse.msd.report.settings.IMassSpectraReportSettings;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

public class MassSpectraReports {

	private static final Logger logger = Logger.getLogger(MassSpectraReports.class);
	private static final String EXTENSION_POINT = "org.eclipse.chemclipse.msd.report.massSpectraReportSupplier";
	/*
	 * These are the attributes of the extension point elements.
	 */
	public static final String ID = "id";
	public static final String DESCRIPTION = "description";
	public static final String FILTER_NAME = "reportName";
	public static final String FILE_EXTENSION = "fileExtension";
	public static final String FILE_NAME = "fileName";
	public static final String REPORT_GENERATOR = "reportGenerator";
	public static final String REPORT_SETTINGS = "reportSettings";

	/**
	 * This class has only static methods.
	 */
	private MassSpectraReports() {

	}

	public static IProcessingInfo<?> generate(File file, boolean append, IScanMSD massSpectrum, IMassSpectraReportSettings massSpectraReportSettings, String reportSupplierId, IProgressMonitor monitor) {

		IProcessingInfo<?> processingInfo;
		IMassSpectraReportGenerator reportGenerator = getMassSpectraReportGenerator(reportSupplierId);
		if(reportGenerator != null) {
			processingInfo = reportGenerator.generate(file, append, massSpectrum, massSpectraReportSettings, monitor);
		} else {
			processingInfo = getNoMassSpectraReportAvailableProcessingInfo(file);
		}
		return processingInfo;
	}

	public static IProcessingInfo<?> generate(File file, boolean append, IScanMSD massSpectrum, String reportSupplierId, IProgressMonitor monitor) {

		IProcessingInfo<?> processingInfo;
		IMassSpectraReportGenerator reportGenerator = getMassSpectraReportGenerator(reportSupplierId);
		if(reportGenerator != null) {
			processingInfo = reportGenerator.generate(file, append, massSpectrum, monitor);
		} else {
			processingInfo = getNoMassSpectraReportAvailableProcessingInfo(file);
		}
		return processingInfo;
	}

	public static IProcessingInfo<?> generate(File file, boolean append, IMassSpectra massSpectra, IMassSpectraReportSettings massSpectraReportSettings, String reportSupplierId, IProgressMonitor monitor) {

		IProcessingInfo<?> processingInfo;
		IMassSpectraReportGenerator reportGenerator = getMassSpectraReportGenerator(reportSupplierId);
		if(reportGenerator != null) {
			processingInfo = reportGenerator.generate(file, append, massSpectra, massSpectraReportSettings, monitor);
		} else {
			processingInfo = getNoMassSpectraReportAvailableProcessingInfo(file);
		}
		return processingInfo;
	}

	public static IProcessingInfo<?> generate(File file, boolean append, IMassSpectra massSpectra, String reportSupplierId, IProgressMonitor monitor) {

		IProcessingInfo<?> processingInfo;
		IMassSpectraReportGenerator reportGenerator = getMassSpectraReportGenerator(reportSupplierId);
		if(reportGenerator != null) {
			processingInfo = reportGenerator.generate(file, append, massSpectra, monitor);
		} else {
			processingInfo = getNoMassSpectraReportAvailableProcessingInfo(file);
		}
		return processingInfo;
	}

	/**
	 * This method returns an {@link MassSpectraReportSupport} instance.<br/>
	 * The {@link MassSpectraReportSupport} instance stores descriptions
	 * about all valid and registered mass spectra report suppliers.
	 *
	 * @return {@link IMassSpectraReportSupport}
	 */
	public static IMassSpectraReportSupport getMassSpectraReportSupplierSupport() {

		MassSpectraReportSupport massSpectraReportSupport = new MassSpectraReportSupport();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : extensions) {
			/*
			 * Set the values to the MassSpectraReportSupplier instance first before
			 * validating them, because the return value of element.getAttribute(...)
			 * could be null. If the element is null and stored in a
			 * MassSpectraReportSupplier instance it will be at least "".
			 */
			MassSpectraReportSupplier supplier = new MassSpectraReportSupplier();
			supplier.setFileExtension(element.getAttribute(FILE_EXTENSION));
			supplier.setFileName(element.getAttribute(FILE_NAME));
			/*
			 * Check if these values contain not allowed characters. If yes than
			 * do not add the supplier to the supported report list.
			 */
			if(isValid(supplier.getFileExtension()) && isValid(supplier.getFileName())) {
				supplier.setId(element.getAttribute(ID));
				supplier.setDescription(element.getAttribute(DESCRIPTION));
				supplier.setFilterName(element.getAttribute(FILTER_NAME));
				if(element.getAttribute(REPORT_SETTINGS) != null) {
					try {
						IMassSpectraReportSettings instance = (IMassSpectraReportSettings)element.createExecutableExtension(REPORT_SETTINGS);
						supplier.setSettingsClass(instance.getClass());
					} catch(CoreException e) {
						logger.warn(e);
						// settings class is optional, set null instead
						supplier.setSettingsClass(null);
					}
				}
				massSpectraReportSupport.add(supplier);
			}
		}
		return massSpectraReportSupport;
	}

	/**
	 * Returns an {@link IMassSpectraReportGenerator} instance or null if none is
	 * available.
	 */
	private static IMassSpectraReportGenerator getMassSpectraReportGenerator(final String reportGeneratorId) {

		IConfigurationElement element = getConfigurationElement(reportGeneratorId);
		IMassSpectraReportGenerator instance = null;
		if(element != null) {
			try {
				instance = (IMassSpectraReportGenerator)element.createExecutableExtension(REPORT_GENERATOR);
			} catch(CoreException e) {
				logger.warn(e);
			}
		}
		return instance;
	}

	/**
	 * Returns the configuration element of the given report generator id or null
	 * if none is available.
	 */
	private static IConfigurationElement getConfigurationElement(final String reportGeneratorId) {

		if(reportGeneratorId == null || reportGeneratorId.isEmpty()) {
			return null;
		}
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : elements) {
			if(reportGeneratorId.equals(element.getAttribute(ID))) {
				return element;
			}
		}
		return null;
	}

	private static IProcessingInfo<?> getNoMassSpectraReportAvailableProcessingInfo(File file) {

		IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
		processingInfo.addErrorMessage("Mass Spectra Report Generator", "There is no suitable mass spectra report generator available for: " + file.getAbsolutePath());
		return processingInfo;
	}

	/**
	 * This method returns true if the input string contains no not allowed
	 * character like \/:*?"&lt;&gt;| and false if it does.<br/>
	 * If the input string is null it returns false.
	 *
	 * @return boolean
	 */
	public static boolean isValid(final String input) {

		if(input == null) {
			return false;
		}
		/*
		 * Use four times backslash to search after a normal backslash. See
		 * "Mastering Regular Expressions" from Jeffrey Friedl, ISBN:
		 * 0596528124.
		 */
		String regex = "[\\\\/:*?\"<>|]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		return !matcher.find();
	}
}
