/*******************************************************************************
 * Copyright (c) 2016 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.wizards;

import java.io.File;
import java.util.Date;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.ui.wizards.AbstractFileWizard;
import org.eclipse.chemclipse.ux.extension.msd.ui.wizards.ChromatogramInputEntriesWizardPage;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class WizardCreateRetentionIndexFile extends AbstractFileWizard {

	/**
	 * Preferred size of the wizard.
	 */
	public static final int PREFERRED_WIDTH = 300;
	public static final int PREFERRED_HEIGHT = 540;
	//
	private static final Logger logger = Logger.getLogger(WizardCreateRetentionIndexFile.class);
	//
	private static final String CALIBRATION_FILE_EXTENSION = ".cal";
	//
	private IRetentionIndexWizardElements wizardElements = new RetentionIndexWizardElements();
	//
	private PageCalibrationSettings pageCalibrationSettings;
	private ChromatogramInputEntriesWizardPage pageChromatogramInputEntries;
	private PagePeakSelection pagePeakSelection;
	private PagePeakRange pagePeakRange;
	private PagePeakAssignment pagePeakAssignment;
	private PageCalibrationTable pageCalibrationTable;

	public WizardCreateRetentionIndexFile() {
		super("RetentionIndices_" + new Date().getTime(), CALIBRATION_FILE_EXTENSION);
	}

	@Override
	public void addPages() {

		super.addPages();
		/*
		 * Pages must implement IExtendedWizardPage / extend AbstractExtendedWizardPage
		 */
		pageCalibrationSettings = new PageCalibrationSettings(wizardElements);
		pageChromatogramInputEntries = new ChromatogramInputEntriesWizardPage(wizardElements);
		pagePeakSelection = new PagePeakSelection(wizardElements);
		pagePeakRange = new PagePeakRange(wizardElements);
		pagePeakAssignment = new PagePeakAssignment(wizardElements);
		pageCalibrationTable = new PageCalibrationTable(wizardElements);
		//
		addPage(pageCalibrationSettings);
		addPage(pageChromatogramInputEntries);
		addPage(pagePeakSelection);
		addPage(pagePeakRange);
		addPage(pagePeakAssignment);
		addPage(pageCalibrationTable);
	}

	@Override
	public boolean canFinish() {

		boolean canFinish = pageCalibrationSettings.canFinish();
		if(canFinish) {
			canFinish = (wizardElements.getSelectedChromatograms().size() > 0) ? true : false;
		}
		if(canFinish) {
			canFinish = pagePeakSelection.canFinish();
		}
		if(canFinish) {
			canFinish = pagePeakRange.canFinish();
		}
		if(canFinish) {
			canFinish = pagePeakAssignment.canFinish();
		}
		if(canFinish) {
			canFinish = pageCalibrationTable.canFinish();
		}
		return canFinish;
	}

	@Override
	public void doFinish(IProgressMonitor monitor) throws CoreException {

		monitor.beginTask("Create Chromatogram Evaluation", IProgressMonitor.UNKNOWN);
		final IFile file = super.prepareProject(monitor);
		try {
			File calibrationFile = file.getLocation().toFile();
			System.out.println(calibrationFile);
		} catch(Exception e) {
			logger.warn(e);
		}
		/*
		 * Refresh
		 */
		super.refreshWorkspace(monitor);
		super.runOpenEditor(file, monitor);
	}
}
