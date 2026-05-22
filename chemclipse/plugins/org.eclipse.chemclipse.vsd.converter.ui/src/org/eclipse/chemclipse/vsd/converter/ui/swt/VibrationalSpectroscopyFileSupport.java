/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.vsd.converter.ui.swt;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.chemclipse.converter.core.IConverterSupport;
import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.converter.scan.IScanConverterSupport;
import org.eclipse.chemclipse.converter.ui.l10n.ConverterMessagesUI;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoPartSupport;
import org.eclipse.chemclipse.vsd.converter.core.ScanConverterVSD;
import org.eclipse.chemclipse.vsd.converter.ui.l10n.VibrationalSpectroscopyMessages;
import org.eclipse.chemclipse.vsd.model.core.ISpectrumVSD;
import org.eclipse.chemclipse.vsd.model.core.SignalType;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class VibrationalSpectroscopyFileSupport {

	private static final Logger logger = Logger.getLogger(VibrationalSpectroscopyFileSupport.class);

	/**
	 * Use only static methods.
	 */
	private VibrationalSpectroscopyFileSupport() {

	}

	public static boolean saveSpectrum(ISpectrumVSD spectrum) throws NoConverterAvailableException {

		Shell shell = Display.getDefault().getActiveShell();
		saveSpectrum(shell, spectrum, ConverterMessagesUI.spectrum);
		return true;
	}

	/**
	 * Opens a file dialog and tries to save the mass spectra
	 */
	public static void saveSpectrum(Shell shell, ISpectrumVSD spectrum, String fileName) {

		if(spectrum == null) {
			return;
		}
		/*
		 * Create the dialogue.
		 */
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFileName(fileName);
		dialog.setText(ConverterMessagesUI.saveSpectrumAs);
		dialog.setOverwrite(true);
		IScanConverterSupport converterSupport = ScanConverterVSD.getScanConverterSupport();
		/*
		 * Set the filters that allow an export of IR data.
		 */
		String[] filterExtensions = converterSupport.getFilterExtensions(IConverterSupport.EXPORT_SUPPLIER);
		dialog.setFilterExtensions(filterExtensions);
		String[] filterNames = converterSupport.getFilterNames(IConverterSupport.EXPORT_SUPPLIER);
		dialog.setFilterNames(filterNames);
		String filename = dialog.open();
		if(filename != null) {
			Collection<ISupplier> suppliers = converterSupport.getSupplier(IConverterSupport.EXPORT_SUPPLIER);
			validateFile(dialog, new ArrayList<>(suppliers), shell, spectrum);
		}
	}

	/**
	 * Validates the selected file to save the spectrum. This method checks
	 * if the spectrum is stored in a directory or not and prepares the file
	 * system in a convenient way.
	 */
	private static void validateFile(FileDialog dialog, List<ISupplier> supplier, Shell shell, ISpectrumVSD spectrum) {

		boolean overwrite = dialog.getOverwrite();
		ISupplier selectedSupplier = supplier.get(dialog.getFilterIndex());
		if(selectedSupplier == null) {
			MessageDialog.openInformation(shell, ConverterMessagesUI.spectrumConverter, ConverterMessagesUI.converterDoesNotExist);
			return;
		}
		String filename = dialog.getFilterPath() + File.separator + dialog.getFileName();
		filename = FilenameUtils.removeExtension(filename);
		filename = filename.concat(selectedSupplier.getFileExtension());
		String filenameDialog = dialog.getFilterPath() + File.separator + dialog.getFileName();
		if(!filename.equals(filenameDialog)) {
			/*
			 * The file name has been modified. Ask for override if it
			 * still exists.
			 */
			File file = new File(filename);
			if(file.exists()) {
				if(MessageDialog.openQuestion(shell, ConverterMessagesUI.overwrite, NLS.bind(ConverterMessagesUI.overwriteFile, file.toString()))) {
					overwrite = true;
				} else {
					overwrite = false;
				}
			}
		}
		if(overwrite) {
			String fileExtension = selectedSupplier.getFileExtension();
			if(!filename.endsWith(fileExtension)) {
				filename = filename + fileExtension;
			}
		}
		writeFile(shell, new File(filename), spectrum, selectedSupplier);
	}

	public static void writeFile(Shell shell, final File file, final ISpectrumVSD spectrum, final ISupplier supplier) {

		if(file == null || spectrum == null || supplier == null) {
			return;
		}
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		IRunnableWithProgress runnable = monitor -> {

			if(spectrum.getScanVSD().getSignalType() == SignalType.FTIR) {
				monitor.beginTask(VibrationalSpectroscopyMessages.saveIR, IProgressMonitor.UNKNOWN);
			} else if(spectrum.getScanVSD().getSignalType() == SignalType.RAMAN) {
				monitor.beginTask(VibrationalSpectroscopyMessages.saveRaman, IProgressMonitor.UNKNOWN);
			}
			IProcessingInfo<File> processingInfo = ScanConverterVSD.convert(file, spectrum, supplier.getId(), monitor);
			ProcessingInfoPartSupport.getInstance().update(processingInfo);
			processingInfo.getProcessingResult();
			monitor.done();
		};
		try {
			dialog.run(true, false, runnable);
		} catch(InvocationTargetException e) {
			logger.warn(e);
		} catch(InterruptedException e) {
			logger.warn(e);
			Thread.currentThread().interrupt();
		}
	}
}
