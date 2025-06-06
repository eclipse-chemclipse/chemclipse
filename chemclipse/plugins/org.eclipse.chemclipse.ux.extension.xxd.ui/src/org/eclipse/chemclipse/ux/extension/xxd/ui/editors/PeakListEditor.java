/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.editors;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.chemclipse.msd.converter.peak.PeakConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoPartSupport;
import org.eclipse.chemclipse.progress.core.InfoType;
import org.eclipse.chemclipse.progress.core.StatusLineLogger;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.PeakScanListUI;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import jakarta.annotation.PostConstruct;

public class PeakListEditor {

	@PostConstruct
	public void construct(Composite parent, ISupplier supplier, File file) {

		PeakScanListUI scanListUI = new PeakScanListUI(parent, SWT.NONE);
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(parent.getShell());
		try {
			dialog.run(true, true, monitor -> {

				IProcessingInfo<IPeaksMSD> convert = PeakConverterMSD.convert(file, supplier.getId(), monitor);
				Display.getDefault().asyncExec(() -> {

					IPeaksMSD result = convert.getProcessingResult();
					if(convert.hasErrorMessages() || result == null) {
						ProcessingInfoPartSupport.getInstance().update(convert);
					} else {
						scanListUI.setInput(result);
					}
				});
			});
		} catch(InvocationTargetException e) {
			IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
			processingInfo.addErrorMessage("PeakListEditor", "Open file " + file.getAbsolutePath() + " failed", e);
			StatusLineLogger.setInfo(InfoType.ERROR_MESSAGE, "Failed to open " + file.getName() + ". See Feedback for details.");
			ProcessingInfoPartSupport.getInstance().update(processingInfo);
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
