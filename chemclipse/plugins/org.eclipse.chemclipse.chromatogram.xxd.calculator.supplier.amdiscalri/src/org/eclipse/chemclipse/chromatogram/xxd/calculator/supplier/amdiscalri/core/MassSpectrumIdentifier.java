/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - adjust to simplified API, add generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.AlkaneIdentifier;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.settings.MassSpectrumIdentifierAlkaneSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.identifier.AbstractMassSpectrumIdentifier;
import org.eclipse.chemclipse.msd.identifier.settings.IMassSpectrumIdentifierSettings;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class MassSpectrumIdentifier extends AbstractMassSpectrumIdentifier {

	private static final Logger logger = Logger.getLogger(MassSpectrumIdentifier.class);

	@Override
	public IProcessingInfo<IMassSpectra> identify(List<IScanMSD> massSpectraList, IMassSpectrumIdentifierSettings massSpectrumIdentifierSettings, IProgressMonitor monitor) {

		IProcessingInfo<IMassSpectra> processingInfo = new ProcessingInfo<>();

		try {
			AlkaneIdentifier alkaneIdentifier = new AlkaneIdentifier();
			MassSpectrumIdentifierAlkaneSettings alkaneSettings;
			if(massSpectrumIdentifierSettings instanceof MassSpectrumIdentifierAlkaneSettings settings) {
				alkaneSettings = settings;
			} else {
				alkaneSettings = PreferenceSupplier.getMassSpectrumIdentifierAlkaneSettings();
			}
			/*
			 * Run the identifier.
			 */
			processingInfo.addMessages(alkaneIdentifier.runIdentification(massSpectraList, alkaneSettings, monitor));
		} catch(Exception e) {
			logger.warn(e);
			processingInfo.addErrorMessage("Alkanes Identifier", "Some has gone wrong.");
		}

		return processingInfo;
	}
}
