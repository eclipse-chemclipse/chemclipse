/*******************************************************************************
 * Copyright (c) 2023, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.ui.processors;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;

import org.eclipse.chemclipse.csd.converter.supplier.ocx.io.ChromatogramWriterCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.supplier.IChromatogramSelectionProcessSupplier;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.io.ChromatogramWriterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.supplier.AbstractProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessTypeSupplier;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.io.ChromatogramWriterWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.settings.ChromatogramExportSettings;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.ChromatogramVersion;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.VersionConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.component.annotations.Component;

@Component(service = {IProcessTypeSupplier.class})
public class ChromatogramFileExportProcessSupplier implements IProcessTypeSupplier {

	private static final Logger logger = Logger.getLogger(ChromatogramFileExportProcessSupplier.class);
	//
	private static final String ID = "org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.ui.processors.chromatogramFileExportProcessSupplier";
	private static final String NAME = "Open Chromatography Binary [Specific Version] (*.ocb)";
	private static final String DESCRIPTION = "Exports the chromatogram to a file.";

	@Override
	public String getCategory() {

		return ICategories.CHROMATOGRAM_EXPORT;
	}

	@Override
	public Collection<IProcessSupplier<?>> getProcessorSuppliers() {

		return Collections.singleton(new ProcessSupplier(this));
	}

	private static final class ProcessSupplier extends AbstractProcessSupplier<ChromatogramExportSettings> implements IChromatogramSelectionProcessSupplier<ChromatogramExportSettings> {

		public ProcessSupplier(IProcessTypeSupplier parent) {

			super(ID, NAME, DESCRIPTION, ChromatogramExportSettings.class, parent, DataCategory.chromatographyCategories());
		}

		@Override
		public IChromatogramSelection<?, ?> apply(IChromatogramSelection<?, ?> chromatogramSelection, ChromatogramExportSettings processSettings, ProcessExecutionContext context) throws InterruptedException {

			try {
				DisplayUtils.executeBusy(new Callable<Void>() {

					@Override
					public Void call() throws Exception {

						Display display = Display.getDefault();
						if(display != null) {
							display.syncExec(new Runnable() {

								@Override
								public void run() {

									IChromatogram<?> chromatogram = chromatogramSelection.getChromatogram();
									String fileName = chromatogram.getName().isEmpty() ? VersionConstants.FILE_NAME_CHROMATOGRAM : chromatogram.getName() + VersionConstants.FILE_EXTENSION_CHROMATOGRAM;
									/*
									 * Sometimes the shell is null.
									 */
									boolean disposeShell = false;
									Shell shell = display.getActiveShell();
									if(shell == null) {
										shell = new Shell(display);
										disposeShell = true;
									}
									//
									FileDialog fileDialog = new FileDialog(display.getActiveShell(), SWT.SAVE);
									fileDialog.setOverwrite(true);
									fileDialog.setText(VersionConstants.DESCRIPTION_CHROMATOGRAM);
									fileDialog.setFilterExtensions(new String[]{VersionConstants.FILTER_EXTENSION_CHROMATOGRAM});
									fileDialog.setFilterNames(new String[]{VersionConstants.FILTER_NAME_CHROMATOGRAM});
									fileDialog.setFileName(fileName);
									fileDialog.setFilterPath(PreferenceSupplier.getListPathExport());
									String path = fileDialog.open();
									if(path != null) {
										try {
											/*
											 * Save the chromatogram in a specific version.
											 */
											PreferenceSupplier.setListPathExport(fileDialog.getFilterPath());
											ChromatogramVersion chromatogramVersion = processSettings.getChromatogramVersion();
											String version = chromatogramVersion.getVersion();
											//
											File file = new File(path);
											if(chromatogram instanceof IChromatogramCSD chromatogramCSD) {
												/*
												 * CSD
												 */
												ChromatogramWriterCSD writer = new ChromatogramWriterCSD();
												writer.writeChromatogram(file, version, chromatogramCSD, context.getProgressMonitor());
											} else if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
												/*
												 * MSD
												 */
												ChromatogramWriterMSD writer = new ChromatogramWriterMSD();
												writer.writeChromatogram(file, version, chromatogramMSD, context.getProgressMonitor());
											} else if(chromatogram instanceof IChromatogramWSD chromatogramWSD) {
												/*
												 * WSD
												 */
												ChromatogramWriterWSD writer = new ChromatogramWriterWSD();
												writer.writeChromatogram(file, version, chromatogramWSD, context.getProgressMonitor());
											}
										} catch(Exception e) {
											logger.warn(e);
										}
									}
									/*
									 * Dispose the shell on demand.
									 */
									if(disposeShell) {
										if(!shell.isDisposed()) {
											shell.dispose();
										}
									}
								}
							});
						}
						//
						return null;
					}
				});
			} catch(Exception e) {
				logger.warn(e);
				Thread.currentThread().interrupt();
			}
			//
			return chromatogramSelection;
		}
	}
}