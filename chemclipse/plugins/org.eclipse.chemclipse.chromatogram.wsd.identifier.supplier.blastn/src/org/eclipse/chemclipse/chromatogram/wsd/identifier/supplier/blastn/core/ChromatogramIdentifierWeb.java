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

import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.chromatogram.AbstractChromatogramIdentifier;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.chromatogram.IChromatogramIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io.WebNucleotideBLAST;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.WebIdentifierSettings;
import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.IChromatogramIdentificationResult;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.model.core.selection.IChromatogramSelectionWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramIdentifierWeb extends AbstractChromatogramIdentifier {

	private static final String DESCRIPTION = "Nucleotide BLAST";
	private static final Logger logger = Logger.getLogger(ChromatogramIdentifierWeb.class);

	@Override
	public IProcessingInfo<IChromatogramIdentificationResult> identify(IChromatogramSelectionWSD chromatogramSelection, IChromatogramIdentifierSettings chromatogramIdentifierSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramIdentificationResult> processingInfo = validate(chromatogramSelection, chromatogramIdentifierSettings);
		if(!processingInfo.hasErrorMessages()) {
			if(chromatogramIdentifierSettings instanceof WebIdentifierSettings settings) {
				try {
					int identifications = WebNucleotideBLAST.run((IChromatogramDSD)chromatogramSelection.getChromatogram(), settings);
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