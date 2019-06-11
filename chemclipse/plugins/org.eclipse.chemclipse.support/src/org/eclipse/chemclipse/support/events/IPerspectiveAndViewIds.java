/*******************************************************************************
 * Copyright (c) 2013, 2019 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.events;

/**
 * This interface contains the perspective and view
 * ids stored in the fragment.e4xmi file.
 * It neccessary to keep both ids in sync.
 */
public interface IPerspectiveAndViewIds {

	/*
	 * Is used to show 3.x editors too:
	 * "org.eclipse.e4.primaryDataStack"
	 * Is an own area to show only 4.x parts (editors)
	 * "org.eclipse.chemclipse.rcp.app.ui.partstack.editor"
	 * Definition in Application.e4xmi
	 * org.eclipse.chemclipse.rcp.app.ui
	 */
	String EDITOR_PART_STACK_ID = "org.eclipse.e4.primaryDataStack";
	String STACK_PERSPECTIVES = "org.eclipse.chemclipse.rcp.app.ui.perspectivestack.main";
	/*
	 * Perspectives
	 */
	String PERSPECTIVE_WELCOME = "org.eclipse.chemclipse.ux.extension.ui.perspective.welcome";
	String PERSPECTIVE_LOGGING = "org.eclipse.chemclipse.logging.ui.perspective.main";
	/*
	 * Views
	 */
	String VIEW_PEAK_TARGETS_MSD = "org.eclipse.chemclipse.ux.extension.msd.ui.part.peakTargetsView";
	String VIEW_PEAK_TARGETS_CSD = "org.eclipse.chemclipse.ux.extension.csd.ui.part.peakTargetsView";
	String VIEW_PROCESSING_INFO = "org.eclipse.chemclipse.processing.ui.parts.ProcessingInfoPart";
	String VIEW_CHROMATOGRAM_TARGETS = "org.eclipse.chemclipse.ux.extension.msd.ui.part.chromatogramTargetsView";
	String VIEW_SCAN_ACCURATE = "org.eclipse.chemclipse.ux.extension.msd.ui.part.scanAccurate";
	String VIEW_SELECTED_ION_CHROMATOGRAM_ACCURATE = "org.eclipse.chemclipse.ux.extension.msd.ui.part.selectedAccurateIonChromtogramView";
	String VIEW_SELECTED_PEAK_AND_CHROMATOGRAM = "org.eclipse.chemclipse.ux.extension.msd.ui.part.selectedPeakChromtogramView";
	String VIEW_INTEGRATION_RESULTS = "org.eclipse.chemclipse.chromatogram.xxd.integrator.ui.views.integrationResultView";
	String VIEW_MASS_SPECTRUM_IDENTIFICATION_RESULTS = "org.eclipse.chemclipse.ux.extension.msd.ui.part.massSpectrumTargetsView";
	String VIEW_MASS_SPECTRUM_TARGETS = "org.eclipse.chemclipse.ux.extension.msd.ui.part.massSpectrumTargetsView";
	/*
	 * Legacy
	 */
	String VIEW_CONSOLE = "org.eclipse.ui.console.ConsoleView";
}
