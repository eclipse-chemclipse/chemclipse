/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.handlers;

import java.io.File;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.wizards.WizardCreateRetentionIndexFile;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.ux.extension.ui.provider.ISupplierEditorSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.editors.ProjectExplorerSupportFactory;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class CreateRetentionIndexFileHandler {

	private static final Logger logger = Logger.getLogger(CreateRetentionIndexFileHandler.class);

	@Execute
	public void execute(@Active Shell shell) {

		WizardCreateRetentionIndexFile wizard = new WizardCreateRetentionIndexFile();
		WizardDialog wizardDialog = new WizardDialog(shell, wizard);
		wizardDialog.setPageSize(WizardCreateRetentionIndexFile.PREFERRED_WIDTH, WizardCreateRetentionIndexFile.PREFERRED_HEIGHT);
		File calibrationFile;
		try {
			wizardDialog.open();
			calibrationFile = wizard.getCalibrationFile();
		} finally {
			wizard.dispose();
		}
		/*
		 * The wizard dialog is closed at this point, so the E4 part service can resolve the active window.
		 */
		if(calibrationFile != null) {
			ISupplierEditorSupport supplierEditorSupport = new ProjectExplorerSupportFactory(DataType.CAL).getInstanceEditorSupport();
			if(!supplierEditorSupport.openEditor(calibrationFile)) {
				logger.warn("Failed to open editor.");
			}
		}
	}
}
