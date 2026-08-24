/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.chromatogram.AbstractChromatogramIdentifier;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.chromatogram.IChromatogramIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io.LocalNucleotideBLAST;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.LocalIdentifierSettings;
import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.IChromatogramIdentificationResult;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.model.core.selection.IChromatogramSelectionWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramIdentifierLocal extends AbstractChromatogramIdentifier {

	private static final String DESCRIPTION = "Nucleotide BLAST";
	private static final Logger logger = Logger.getLogger(ChromatogramIdentifierLocal.class);

	@Override
	public IProcessingInfo<IChromatogramIdentificationResult> identify(IChromatogramSelectionWSD chromatogramSelection, IChromatogramIdentifierSettings chromatogramIdentifierSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramIdentificationResult> processingInfo = validate(chromatogramSelection, chromatogramIdentifierSettings);
		if(!processingInfo.hasErrorMessages()) {
			if(chromatogramIdentifierSettings instanceof LocalIdentifierSettings settings) {
				if(PreferenceSupplier.getDatabaseFolder().isEmpty() || !new File(PreferenceSupplier.getDatabaseFolder()).exists()) {
					processingInfo.addErrorMessage(DESCRIPTION, "Database folder does not exist.", "Set database folder in preferences.");
					return processingInfo;
				}
				try {
					int identifications = LocalNucleotideBLAST.run((IChromatogramDSD)chromatogramSelection.getChromatogram(), settings, monitor);
					processingInfo.addInfoMessage(DESCRIPTION, identifications + " targets were identified.");
				} catch(IOException e) {
					processingInfo.addErrorMessage(DESCRIPTION, e.getMessage());
					logger.error(e);
				} catch(InterruptedException e) {
					processingInfo.addInfoMessage(DESCRIPTION, "Process aborted.");
					Thread.currentThread().interrupt();
				}
			}
		}
		return processingInfo;
	}
}