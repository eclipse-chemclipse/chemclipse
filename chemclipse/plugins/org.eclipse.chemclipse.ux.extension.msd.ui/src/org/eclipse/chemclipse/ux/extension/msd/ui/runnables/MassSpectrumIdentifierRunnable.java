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
package org.eclipse.chemclipse.ux.extension.msd.ui.runnables;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.chemclipse.msd.identifier.MassSpectrumIdentifier;
import org.eclipse.chemclipse.msd.identifier.settings.IMassSpectrumIdentifierSettings;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.ux.extension.msd.ui.l10n.ExtensionMessages;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class MassSpectrumIdentifierRunnable implements IRunnableWithProgress {

	private List<IScanMSD> scans;
	private String identifierId;
	private IMassSpectrumIdentifierSettings identifierSettings;

	public MassSpectrumIdentifierRunnable(List<IScanMSD> scans, String identifierId, IMassSpectrumIdentifierSettings identifierSettings) {

		this.scans = scans;
		this.identifierId = identifierId;
		this.identifierSettings = identifierSettings;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		try {
			monitor.beginTask(ExtensionMessages.scanIdentification, IProgressMonitor.UNKNOWN);
			MassSpectrumIdentifier.identify(scans, identifierSettings, identifierId, monitor);
		} finally {
			monitor.done();
		}
	}
}