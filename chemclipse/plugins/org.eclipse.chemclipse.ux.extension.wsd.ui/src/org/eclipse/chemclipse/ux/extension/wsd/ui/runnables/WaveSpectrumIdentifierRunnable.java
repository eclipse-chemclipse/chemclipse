/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.wsd.ui.runnables;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.settings.IWaveSpectrumIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.wavespectrum.WaveSpectrumIdentifier;
import org.eclipse.chemclipse.ux.extension.wsd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class WaveSpectrumIdentifierRunnable implements IRunnableWithProgress {

	private List<IScanWSD> scans;
	private String identifierId;
	private IWaveSpectrumIdentifierSettings identifierSettings;

	public WaveSpectrumIdentifierRunnable(List<IScanWSD> scans, String identifierId, IWaveSpectrumIdentifierSettings identifierSettings) {

		this.scans = scans;
		this.identifierId = identifierId;
		this.identifierSettings = identifierSettings;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		try {
			monitor.beginTask(ExtensionMessages.scanIdentification, IProgressMonitor.UNKNOWN);
			WaveSpectrumIdentifier.identify(scans, identifierSettings, identifierId, monitor);
		} finally {
			monitor.done();
		}
	}
}