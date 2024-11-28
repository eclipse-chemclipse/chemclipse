/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.library.LibrarySearchSettings;
import org.eclipse.chemclipse.model.services.RetentionIndexLibrarySettings;
import org.eclipse.chemclipse.model.ui.dialogs.RetentionIndexLibrarySettingsDialog;
import org.eclipse.chemclipse.model.ui.runnables.LibrarySearchRunnable;
import org.eclipse.chemclipse.ux.extension.xxd.ui.wizards.WizardLibrarySearch;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

public class RetentionIndexLibraryDialog {

	private static final Logger logger = Logger.getLogger(RetentionIndexLibraryDialog.class);
	//
	private Display display = null;
	private int retentionTime = 0;
	private int retentionIndex = 0;
	//
	private RetentionIndexLibrarySettingsDialog retentionIndexLibrarySettingsDialog = null;
	private List<IIdentificationTarget> identificationTargets = new ArrayList<>();

	public RetentionIndexLibraryDialog(Display display, int retentionTime, int retentionIndex) {

		this.display = display;
		this.retentionTime = retentionTime;
		this.retentionIndex = retentionIndex;
		//
		retentionIndexLibrarySettingsDialog = new RetentionIndexLibrarySettingsDialog(display.getActiveShell());
	}

	public RetentionIndexLibrarySettings getRetentionIndexLibrarySettings() {

		return retentionIndexLibrarySettingsDialog.getRetentionIndexLibrarySettings();
	}

	public List<IIdentificationTarget> getIdentificationTargets() {

		return identificationTargets;
	}

	public boolean open() {

		boolean success = false;
		//
		identificationTargets.clear();
		if(retentionIndex > 0) {
			retentionIndexLibrarySettingsDialog.create();
			if(retentionIndexLibrarySettingsDialog.open() == Window.OK) {
				RetentionIndexLibrarySettings retentionIndexLibrarySettings = retentionIndexLibrarySettingsDialog.getRetentionIndexLibrarySettings();
				try {
					/*
					 * Fetch the library results.
					 */
					LibrarySearchRunnable librarySearchRunnable = new LibrarySearchRunnable(retentionIndex, retentionIndexLibrarySettings);
					ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(display.getActiveShell());
					progressMonitorDialog.run(true, false, librarySearchRunnable);
					identificationTargets.addAll(librarySearchRunnable.getIdentificationTargets());
				} catch(InvocationTargetException e) {
					logger.warn(e);
				} catch(InterruptedException e) {
					logger.warn(e);
				}
				/*
				 * Let the user review and select the results.
				 */
				if(!identificationTargets.isEmpty()) {
					/*
					 * Settings
					 */
					LibrarySearchSettings librarySearchSettings = new LibrarySearchSettings();
					librarySearchSettings.setSearchColumn(retentionIndexLibrarySettings.getSearchColumn());
					librarySearchSettings.setRetentionTime(retentionTime);
					librarySearchSettings.setRetentionIndex(retentionIndex);
					librarySearchSettings.getIdentificationTargets().addAll(identificationTargets);
					/*
					 * Wizard
					 */
					WizardLibrarySearch wizard = new WizardLibrarySearch(librarySearchSettings);
					WizardDialog wizardDialog = new WizardDialog(display.getActiveShell(), wizard);
					wizardDialog.setMinimumPageSize(WizardLibrarySearch.DEFAULT_WIDTH, WizardLibrarySearch.DEFAULT_HEIGHT);
					wizardDialog.create();
					if(wizardDialog.open() == WizardDialog.OK) {
						success = true;
					} else {
						identificationTargets.clear();
					}
				} else {
					MessageDialog.openInformation(display.getActiveShell(), RetentionIndexLibrarySettingsDialog.TITLE, RetentionIndexLibrarySettingsDialog.ERROR_NO_LIBRARY);
				}
			}
		} else {
			MessageDialog.openInformation(display.getActiveShell(), RetentionIndexLibrarySettingsDialog.TITLE, RetentionIndexLibrarySettingsDialog.ERROR_RETENTION_INDEX);
		}
		//
		return success;
	}
}