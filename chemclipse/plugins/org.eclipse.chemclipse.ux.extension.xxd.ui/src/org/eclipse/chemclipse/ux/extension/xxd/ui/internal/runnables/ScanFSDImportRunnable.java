/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.runnables;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.chemclipse.fsd.converter.core.ScanConverterFSD;
import org.eclipse.chemclipse.fsd.model.core.ISpectrumFSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoPartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class ScanFSDImportRunnable implements IRunnableWithProgress {

	private static final Logger logger = Logger.getLogger(ScanFSDImportRunnable.class);
	//
	private File file;
	private ISpectrumFSD spectrumFSD = null;

	public ScanFSDImportRunnable(File file) {

		this.file = file;
	}

	public ISpectrumFSD getSpectrumFSD() {

		return spectrumFSD;
	}

	@Override
	public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		try {
			monitor.beginTask(ExtensionMessages.importScan, IProgressMonitor.UNKNOWN);
			IProcessingInfo<?> processingInfo = ScanConverterFSD.convert(file, monitor);
			ProcessingInfoPartSupport.getInstance().update(processingInfo);
			spectrumFSD = (ISpectrumFSD)processingInfo.getProcessingResult();
		} catch(Exception e) {
			logger.error(e);
		} finally {
			monitor.done();
		}
	}
}
