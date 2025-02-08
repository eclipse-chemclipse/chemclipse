/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - adjust to simplified API, add generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.identifier.massspectrum.AbstractMassSpectrumIdentifier;
import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IMassSpectrumIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.identifier.FileIdentifier;
import org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.identifier.UnknownIdentifier;
import org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.model.UnknownSettingsSupport;
import org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.settings.MassSpectrumUnknownSettings;
import org.eclipse.chemclipse.model.targets.TargetUnknownSettings;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class MassSpectrumIdentifierUnknown extends AbstractMassSpectrumIdentifier {

	@Override
	public IProcessingInfo<IMassSpectra> identify(List<IScanMSD> massSpectraList, IMassSpectrumIdentifierSettings identifierSettings, IProgressMonitor monitor) {

		MassSpectrumUnknownSettings unknownSettings;
		if(identifierSettings instanceof MassSpectrumUnknownSettings settings) {
			unknownSettings = settings;
		} else {
			unknownSettings = PreferenceSupplier.getMassSpectrumUnknownSettings();
		}
		//
		IProcessingInfo<IMassSpectra> processingInfo = new ProcessingInfo<>();
		TargetUnknownSettings targetUnknownSettings = UnknownSettingsSupport.getTargetUnknownSettings(unknownSettings);
		float limitMatchFactor = unknownSettings.getLimitMatchFactor();
		UnknownIdentifier unknownIdentifier = new UnknownIdentifier();
		unknownIdentifier.runIdentificationScan(massSpectraList, limitMatchFactor, targetUnknownSettings);
		processingInfo.addInfoMessage(FileIdentifier.IDENTIFIER, "Mass spectra have been identified.");
		return processingInfo;
	}
}
