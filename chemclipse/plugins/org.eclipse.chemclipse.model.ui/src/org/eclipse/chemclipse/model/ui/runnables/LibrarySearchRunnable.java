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
package org.eclipse.chemclipse.model.ui.runnables;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.services.IRetentionIndexLibraryService;
import org.eclipse.chemclipse.model.services.RetentionIndexLibrarySettings;
import org.eclipse.chemclipse.model.support.ColumnIndexSupport;
import org.eclipse.chemclipse.model.support.LibraryInformationSupport;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class LibrarySearchRunnable implements IRunnableWithProgress {

	private static final int MAX_RESULTS = 100;
	//
	private int retentionIndex = 0;
	private RetentionIndexLibrarySettings retentionIndexLibrarySettings = new RetentionIndexLibrarySettings();
	private List<IIdentificationTarget> identificationTargets = new ArrayList<>();

	public LibrarySearchRunnable(int retentionIndex, RetentionIndexLibrarySettings retentionIndexLibrarySettings) {

		this.retentionIndex = retentionIndex;
		this.retentionIndexLibrarySettings = retentionIndexLibrarySettings;
	}

	public List<IIdentificationTarget> getIdentificationTargets() {

		return identificationTargets;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		List<ILibraryInformation> libraryInformations = new ArrayList<>();
		for(IRetentionIndexLibraryService retentionIndexLibraryService : retentionIndexLibrarySettings.getRetentionIndexLibraryServices()) {
			libraryInformations.addAll(retentionIndexLibraryService.getLibraryInformation(retentionIndex, retentionIndexLibrarySettings, monitor));
		}
		/*
		 * Map the library entries to identification targets.
		 */
		String searchColumn = retentionIndexLibrarySettings.getSearchColumn();
		boolean caseSensitive = retentionIndexLibrarySettings.isCaseSensitive();
		boolean removeWhiteSpace = retentionIndexLibrarySettings.isRemoveWhiteSpace();
		//
		for(ILibraryInformation libraryInformation : libraryInformations) {
			float retentionIndexColumn = ColumnIndexSupport.getRetentionIndex(retentionIndex, libraryInformation.getColumnIndexMarkers(), searchColumn, caseSensitive, removeWhiteSpace);
			libraryInformation.setRetentionIndex(retentionIndexColumn);
		}
		/*
		 * Filter Entries
		 */
		libraryInformations = LibraryInformationSupport.filterByRetentionIndexDelta(libraryInformations, retentionIndex, MAX_RESULTS);
		for(ILibraryInformation libraryInformation : libraryInformations) {
			identificationTargets.add(new IdentificationTarget(libraryInformation, ComparisonResult.COMPARISON_RESULT_BEST_MATCH));
		}
	}
}