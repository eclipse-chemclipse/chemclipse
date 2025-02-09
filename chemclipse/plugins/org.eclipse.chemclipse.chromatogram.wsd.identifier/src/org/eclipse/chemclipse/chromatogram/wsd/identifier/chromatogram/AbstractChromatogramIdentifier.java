/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.chromatogram;

import org.eclipse.chemclipse.model.exceptions.ValueMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.IChromatogramIdentificationResult;
import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.wsd.model.core.selection.IChromatogramSelectionWSD;

public abstract class AbstractChromatogramIdentifier implements IChromatogramIdentifier {

	private static final String DESCRIPTION = "Chromatogram Identifier";

	public IProcessingInfo<IChromatogramIdentificationResult> validate(IChromatogramSelectionWSD chromatogramSelection, IIdentifierSettings identifierSettings) {

		IProcessingInfo<IChromatogramIdentificationResult> processingInfo = new ProcessingInfo<>();
		try {
			validateChromatogramSelection(chromatogramSelection);
			validateSettings(identifierSettings);
		} catch(ValueMustNotBeNullException e) {
			processingInfo.addErrorMessage(DESCRIPTION, e.getMessage());
		}
		return processingInfo;
	}

	/**
	 * Tests if the chromatogram selection is a valid instance.
	 * 
	 * @param chromatogramSelection
	 * @throws ValueMustNotBeNullException
	 */
	public void validateChromatogramSelection(IChromatogramSelectionWSD chromatogramSelection) throws ValueMustNotBeNullException {

		if(chromatogramSelection == null) {
			throw new ValueMustNotBeNullException("The chromatogram selection must not be null.");
		}
		if(chromatogramSelection.getChromatogram() == null) {
			throw new ValueMustNotBeNullException("The chromatogram must not be null.");
		}
	}

	/**
	 * Throws an exception if the settings are null.
	 * 
	 * @param identifierSettings
	 * @throws ValueMustNotBeNullException
	 */
	public void validateSettings(IIdentifierSettings identifierSettings) throws ValueMustNotBeNullException {

		if(identifierSettings == null) {
			throw new ValueMustNotBeNullException("The identifier settings must not be null.");
		}
	}
}
