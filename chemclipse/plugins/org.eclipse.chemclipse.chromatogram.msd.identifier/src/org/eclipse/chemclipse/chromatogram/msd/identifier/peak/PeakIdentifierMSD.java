/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Jan Holy - implementation
 * Alexander Kerner - Generics
 * Christoph Läubrich - add delegation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.identifier.peak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IPeakIdentifierSettingsMSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.exceptions.NoIdentifierAvailableException;
import org.eclipse.chemclipse.model.identifier.IPeakIdentificationResults;
import org.eclipse.chemclipse.model.identifier.core.Identifier;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.IProcessingMessage;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.chemclipse.support.literature.LiteratureReference;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

/**
 * Use the methods of this class to identify the mass spectrum of a peak.<br/>
 */
public class PeakIdentifierMSD {

	private static final String EXTENSION_POINT = "org.eclipse.chemclipse.chromatogram.msd.identifier.peakIdentifier";
	private static final Logger logger = Logger.getLogger(PeakIdentifierMSD.class);
	private static final String NO_IDENTIFIER_AVAILABLE = "There is no suitable peak identifier available";

	/**
	 * This class should have only static methods.
	 */
	private PeakIdentifierMSD() {

	}

	/**
	 * Returns an {@link IConfigurationElement} instance or null if none is
	 * available.
	 *
	 * @param filterId
	 * @return IConfigurationElement
	 */
	private static IConfigurationElement getConfigurationElement(final String filterId) {

		if("".equals(filterId)) {
			return null;
		}
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : elements) {
			if(element.getAttribute(Identifier.ID).equals(filterId)) {
				return element;
			}
		}
		return null;
	}

	private static IProcessingInfo<IPeakIdentificationResults> getNoIdentifierAvailableProcessingInfo() {

		IProcessingInfo<IPeakIdentificationResults> processingInfo = new ProcessingInfo<>();
		IProcessingMessage processingMessage = new ProcessingMessage(MessageType.ERROR, "Peak Identifier", NO_IDENTIFIER_AVAILABLE);
		processingInfo.addMessage(processingMessage);
		return processingInfo;
	}

	/**
	 * Returns a {@link IPeakIdentifierMSD} instance given by the identifierId or
	 * null, if none is available.
	 */
	private static IPeakIdentifierMSD getPeakIdentifier(final String identifierId) {

		IConfigurationElement element;
		element = getConfigurationElement(identifierId);
		IPeakIdentifierMSD instance = null;
		if(element != null) {
			try {
				instance = (IPeakIdentifierMSD)element.createExecutableExtension(Identifier.IDENTIFIER);
			} catch(CoreException e) {
				logger.warn(e);
			}
		}
		return instance;
	}

	/**
	 * Returns an {@link IPeakIdentifierSupportMSD} instance.
	 *
	 * @return {@link IPeakIdentifierSupportMSD}
	 */
	public static IPeakIdentifierSupportMSD getPeakIdentifierSupport() {

		PeakIdentifierSupplierMSD supplier;
		PeakIdentifierSupportMSD identifierSupport = new PeakIdentifierSupportMSD();
		/*
		 * Search in the extension registry and fill the comparison support
		 * object with supplier information.
		 */
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : extensions) {
			supplier = new PeakIdentifierSupplierMSD();
			supplier.setId(element.getAttribute(Identifier.ID));
			supplier.setDescription(element.getAttribute(Identifier.DESCRIPTION));
			supplier.setIdentifierName(element.getAttribute(Identifier.IDENTIFIER_NAME));
			if(element.getAttribute(Identifier.IDENTIFIER_SETTINGS) != null) {
				try {
					IPeakIdentifierSettingsMSD instance = (IPeakIdentifierSettingsMSD)element.createExecutableExtension(Identifier.IDENTIFIER_SETTINGS);
					supplier.setIdentifierSettingsClass(instance.getClass());
					List<LiteratureReference> literatureReferences = instance.getLiteratureReferences();
					if(literatureReferences != null) {
						supplier.getLiteratureReferences().addAll(literatureReferences);
					}
				} catch(CoreException e) {
					logger.warn(e);
					// settings class is optional, set null instead
					supplier.setIdentifierSettingsClass(null);
				}
			}
			identifierSupport.add(supplier);
		}
		return identifierSupport;
	}

	/**
	 * Runs the given identifier
	 *
	 * @param peak
	 * @param identifierSettings
	 * @param identifierId
	 * @param monitor
	 * @return {@link IProcessingInfo}
	 * @throws NoIdentifierAvailableException
	 */
	public static IProcessingInfo<IPeakIdentificationResults> identify(IPeakMSD peak, IPeakIdentifierSettingsMSD identifierSettings, String identifierId, IProgressMonitor monitor) {

		List<IPeakMSD> peaks = new ArrayList<>();
		peaks.add(peak);
		return identify(peaks, identifierSettings, identifierId, monitor);
	}

	/**
	 * Runs the given identifier
	 *
	 * @param peak
	 * @param identifierId
	 * @param monitor
	 * @return {@link IProcessingInfo}
	 * @throws NoIdentifierAvailableException
	 */
	public static IProcessingInfo<IPeakIdentificationResults> identify(IPeakMSD peak, String identifierId, IProgressMonitor monitor) {

		return identify(Collections.singletonList(peak), identifierId, monitor);
	}

	/**
	 * Runs the given identifier
	 *
	 * @param peaks
	 * @param identifierSettings
	 * @param identifierId
	 * @param monitor
	 * @return {@link IProcessingInfo}
	 * @throws NoIdentifierAvailableException
	 */
	public static IProcessingInfo<IPeakIdentificationResults> identify(List<? extends IPeakMSD> peaks, IPeakIdentifierSettingsMSD identifierSettings, String identifierId, IProgressMonitor monitor) {

		IPeakIdentifierMSD peakIdentifier = getPeakIdentifier(identifierId);
		if(peakIdentifier != null) {
			return peakIdentifier.identify(peaks, identifierSettings, monitor);
		} else {
			return getNoIdentifierAvailableProcessingInfo();
		}
	}

	/**
	 * Runs the given identifier
	 *
	 * @param peaks
	 * @param identifierId
	 * @param monitor
	 * @return {@link IProcessingInfo}
	 * @throws NoIdentifierAvailableException
	 */
	public static IProcessingInfo<IPeakIdentificationResults> identify(List<? extends IPeakMSD> peaks, String identifierId, IProgressMonitor monitor) {

		return identify(peaks, null, identifierId, monitor);
	}

	public static IProcessingInfo<IPeakIdentificationResults> identify(IChromatogramSelectionMSD chromatogramSelectionMSD, String identifierId, IProgressMonitor monitor) {

		chromatogramSelectionMSD.getChromatogram().setDirty(true);
		return identify(chromatogramSelectionMSD, null, identifierId, monitor);
	}

	public static IProcessingInfo<IPeakIdentificationResults> identify(IChromatogramSelectionMSD chromatogramSelectionMSD, IPeakIdentifierSettingsMSD peakIdentifierSettingsMSD, String identifierId, IProgressMonitor monitor) {

		chromatogramSelectionMSD.getChromatogram().setDirty(true);
		return identify(chromatogramSelectionMSD.getChromatogram().getPeaks(chromatogramSelectionMSD), peakIdentifierSettingsMSD, identifierId, monitor);
	}
}
