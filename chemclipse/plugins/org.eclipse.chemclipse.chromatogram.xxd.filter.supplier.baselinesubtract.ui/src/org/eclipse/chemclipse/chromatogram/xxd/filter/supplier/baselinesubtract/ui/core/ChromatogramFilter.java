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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.baselinesubtract.ui.core;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.AbstractChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.result.ChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.baselinesubtract.core.ChromatogramSubtractor;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.baselinesubtract.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.baselinesubtract.settings.ChromatogramFilterSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.ux.extension.xxd.ui.dialogs.ChromatogramEditorDialog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ChromatogramFilter extends AbstractChromatogramFilter {

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramFilterResult> processingInfo = validate(chromatogramSelection, chromatogramFilterSettings);
		if(!processingInfo.hasErrorMessages()) {
			Shell shell = DisplayUtils.getShell();
			if(shell != null) {
				subtractChromatogram(shell, chromatogramSelection);
			} else {
				DisplayUtils.getDisplay().syncExec(() -> {

					/*
					 * Create a new shell and set
					 * the size to 0 cause only the wizard
					 * will be shown.
					 */
					Shell temporaryShell = new Shell();
					temporaryShell.setSize(0, 0);
					temporaryShell.open();
					//
					subtractChromatogram(temporaryShell, chromatogramSelection);
					temporaryShell.close();
				});
			}
			chromatogramSelection.getChromatogram().setDirty(true);
			processingInfo.setProcessingResult(new ChromatogramFilterResult(ResultStatus.OK, "The chromatogram was successfully subtracted."));
		}
		return processingInfo;
	}

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection chromatogramSelection, IProgressMonitor monitor) {

		ChromatogramFilterSettings filterSettings = PreferenceSupplier.getFilterSettings();
		return applyFilter(chromatogramSelection, filterSettings, monitor);
	}

	private void subtractChromatogram(Shell shell, IChromatogramSelection chromatogramSelectionMaster) {

		IChromatogram chromatogramMaster = chromatogramSelectionMaster.getChromatogram();
		ChromatogramEditorDialog dialog = new ChromatogramEditorDialog(shell, chromatogramMaster);
		//
		if(IDialogConstants.OK_ID == dialog.open()) {
			IChromatogramSelection chromatogramSelectionSubtract = dialog.getChromatogramSelection();
			if(chromatogramSelectionSubtract != null) {
				/*
				 * Check that both chromatograms are not the same
				 */
				IChromatogram chromatogramSubtract = chromatogramSelectionSubtract.getChromatogram();
				if(chromatogramMaster != chromatogramSubtract) {
					int startRetentionTime = chromatogramSelectionMaster.getStartRetentionTime();
					int stopRetentionTime = chromatogramSelectionMaster.getStopRetentionTime();
					ChromatogramSubtractor chromatogramSubtractor = new ChromatogramSubtractor();
					chromatogramSubtractor.perform(chromatogramMaster, chromatogramSubtract, startRetentionTime, stopRetentionTime);
				} else {
					MessageDialog.openWarning(shell, "Subtract Chromatogram", "The following chromatogram has been selected for subtraction.");
				}
			}
		}
	}
}
