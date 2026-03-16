/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - adapted for MALDI
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class TargetsComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IIdentificationTarget entry1 && e2 instanceof IIdentificationTarget entry2) {

			ILibraryInformation libraryInformation1 = entry1.getLibraryInformation();
			IComparisonResult comparisonResult1 = entry1.getComparisonResult();
			ILibraryInformation libraryInformation2 = entry2.getLibraryInformation();
			IComparisonResult comparisonResult2 = entry2.getComparisonResult();

			switch(getPropertyIndex()) {
				case 0: // Verified
					sortOrder = Boolean.compare(entry2.isVerified(), entry1.isVerified());
					if(sortOrder == 0) {
						sortOrder = getAdditionalSortOrder(comparisonResult1, comparisonResult2);
					}
					break;
				case 1: // Rating
					sortOrder = Float.compare(comparisonResult2.getRatingSupplier().getScore(), comparisonResult1.getRatingSupplier().getScore());
					if(sortOrder == 0) {
						sortOrder = getAdditionalSortOrder(comparisonResult1, comparisonResult2);
					}
					break;
				case 2: // Name
					sortOrder = libraryInformation2.getName().compareTo(libraryInformation1.getName());
					break;
				case 3: // Match Factor
					sortOrder = Float.compare(comparisonResult2.getMatchFactor(), comparisonResult1.getMatchFactor());
					break;
				case 4: // Reverse Match Factor
					sortOrder = Float.compare(comparisonResult2.getReverseMatchFactor(), comparisonResult1.getReverseMatchFactor());
					break;
				case 5: // Match Factor Direct
					sortOrder = Float.compare(comparisonResult2.getMatchFactorDirect(), comparisonResult1.getMatchFactorDirect());
					break;
				case 6: // Reverse Match Factor Direct
					sortOrder = Float.compare(comparisonResult2.getReverseMatchFactorDirect(), comparisonResult1.getReverseMatchFactorDirect());
					break;
				case 7: // Probability
					sortOrder = Float.compare(comparisonResult2.getProbability(), comparisonResult1.getProbability());
					break;
				case 8: // Advise
					String advise2 = comparisonResult2.getRatingSupplier().getAdvise();
					String advise1 = comparisonResult1.getRatingSupplier().getAdvise();
					if(advise2 != null && advise1 != null) {
						sortOrder = advise2.compareTo(advise1);
					}
					break;
				case 9: // Identifier
					sortOrder = entry2.getIdentifier().compareTo(entry1.getIdentifier());
					break;
				case 10: // Miscellaneous
					sortOrder = libraryInformation2.getMiscellaneous().compareTo(libraryInformation1.getMiscellaneous());
					break;
				case 11: // Comments
					sortOrder = libraryInformation2.getComments().compareTo(libraryInformation1.getComments());
					break;
				case 12: // DB
					sortOrder = libraryInformation2.getDatabase().compareTo(libraryInformation1.getDatabase());
					break;
				case 13: // DB Index
					sortOrder = Integer.compare(libraryInformation2.getDatabaseIndex(), libraryInformation1.getDatabaseIndex());
					break;
				case 14: // Contributor
					sortOrder = libraryInformation2.getContributor().compareTo(libraryInformation1.getContributor());
					break;
				case 15: // Reference Identifier
					sortOrder = libraryInformation2.getReferenceIdentifier().compareTo(libraryInformation1.getReferenceIdentifier());
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

	/**
	 * Calculates the additional sort order by MF, RMF, MFD, RMFD and Probability
	 *
	 * @param comparisonResult1
	 * @param comparisonResult2
	 * @return int
	 */
	private int getAdditionalSortOrder(IComparisonResult comparisonResult1, IComparisonResult comparisonResult2) {

		/*
		 * Match Factor
		 */
		int sortOrder = Float.compare(comparisonResult2.getMatchFactor(), comparisonResult1.getMatchFactor());
		if(sortOrder == 0) {
			/*
			 * Reverse Match Factor
			 */
			sortOrder = Float.compare(comparisonResult2.getReverseMatchFactor(), comparisonResult1.getReverseMatchFactor());
			if(sortOrder == 0) {
				/*
				 * Match Factor Direct
				 */
				sortOrder = Float.compare(comparisonResult2.getMatchFactorDirect(), comparisonResult1.getMatchFactorDirect());
				if(sortOrder == 0) {
					/*
					 * Reverse Match Factor
					 */
					sortOrder = Float.compare(comparisonResult2.getReverseMatchFactorDirect(), comparisonResult1.getReverseMatchFactorDirect());
					if(sortOrder == 0) {
						/*
						 * Probability
						 */
						sortOrder = Float.compare(comparisonResult2.getProbability(), comparisonResult1.getProbability());
					}
				}
			}
		}
		return sortOrder;
	}
}