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
package org.eclipse.chemclipse.swt.ui.comparator;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class PeakTargetsTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IIdentificationTarget identificationTarget1 && e2 instanceof IIdentificationTarget identificationTarget2) {
			ILibraryInformation libraryInformation1 = identificationTarget1.getLibraryInformation();
			IComparisonResult comparisonResult1 = identificationTarget1.getComparisonResult();

			ILibraryInformation libraryInformation2 = identificationTarget2.getLibraryInformation();
			IComparisonResult comparisonResult2 = identificationTarget2.getComparisonResult();

			switch(getPropertyIndex()) {
				case 0:
					sortOrder = libraryInformation2.getName().compareTo(libraryInformation1.getName());
					break;
				case 1:
					sortOrder = Float.compare(comparisonResult2.getMatchFactor(), comparisonResult1.getMatchFactor());
					break;
				case 2:
					sortOrder = Float.compare(comparisonResult2.getReverseMatchFactor(), comparisonResult1.getReverseMatchFactor());
					break;
				case 3:
					sortOrder = Float.compare(comparisonResult2.getMatchFactorDirect(), comparisonResult1.getMatchFactorDirect());
					break;
				case 4:
					sortOrder = Float.compare(comparisonResult2.getReverseMatchFactorDirect(), comparisonResult1.getReverseMatchFactorDirect());
					break;
				default:
					sortOrder = 0;
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}
