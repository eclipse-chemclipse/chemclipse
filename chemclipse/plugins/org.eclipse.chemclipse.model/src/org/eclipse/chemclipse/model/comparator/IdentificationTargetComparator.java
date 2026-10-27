/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - optionally ignore the manual verification
 *******************************************************************************/
package org.eclipse.chemclipse.model.comparator;

import java.util.Comparator;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.support.comparator.SortOrder;

public class IdentificationTargetComparator implements Comparator<IIdentificationTarget> {

	private SortOrder sortOrder;
	private float retentionIndexSource = 0.0f;
	private boolean verifiedFirst = true;

	public IdentificationTargetComparator() {

		this(0.0f);
	}

	public IdentificationTargetComparator(SortOrder sortOrder) {

		this(sortOrder, 0.0f);
	}

	public IdentificationTargetComparator(float retentionIndexSource) {

		this(SortOrder.DESC, retentionIndexSource);
	}

	public IdentificationTargetComparator(SortOrder sortOrder, float retentionIndexSource) {

		this(sortOrder, retentionIndexSource, true);
	}

	public IdentificationTargetComparator(SortOrder sortOrder, float retentionIndexSource, boolean verifiedFirst) {

		this.sortOrder = sortOrder;
		this.retentionIndexSource = retentionIndexSource;
		this.verifiedFirst = verifiedFirst;
	}

	@Override
	public int compare(IIdentificationTarget identificationTarget1, IIdentificationTarget identificationTarget2) {

		/*
		 * WATCH OUT!
		 * OPPOSITE DIRECTION - DELTA RETENTION INDEX
		 * ---
		 * The value needs to be sorted in the opposite direction, as the lowest deviation is the best.
		 * Hence, it behaves completely different than the other values.
		 */
		int sortIndex = 0;
		if(identificationTarget1 != null && identificationTarget2 != null) {
			/*
			 * Manually verified
			 */
			if(verifiedFirst) {
				sortIndex = Boolean.compare(identificationTarget1.isVerified(), identificationTarget2.isVerified());
			}
			if(sortIndex == 0) {
				/*
				 * Delta Retention Index
				 */
				ILibraryInformation libraryInformation1 = identificationTarget1.getLibraryInformation();
				ILibraryInformation libraryInformation2 = identificationTarget2.getLibraryInformation();
				float deltaRetentionIndex1 = calculateDeltaRetentionIndex(libraryInformation1);
				float deltaRetentionIndex2 = calculateDeltaRetentionIndex(libraryInformation2);
				sortIndex = Float.compare(deltaRetentionIndex2, deltaRetentionIndex1); // <- see Info
				if(sortIndex == 0) {
					/*
					 * Match Factor
					 */
					IComparisonResult comparisonResult1 = identificationTarget1.getComparisonResult();
					IComparisonResult comparisonResult2 = identificationTarget2.getComparisonResult();
					sortIndex = Float.compare(comparisonResult1.getMatchFactor(), comparisonResult2.getMatchFactor());
					if(sortIndex == 0) {
						/*
						 * Reverse Match Factor
						 */
						sortIndex = Float.compare(comparisonResult1.getReverseMatchFactor(), comparisonResult2.getReverseMatchFactor());
						if(sortIndex == 0) {
							/*
							 * Match Factor Direct
							 */
							sortIndex = Float.compare(comparisonResult1.getMatchFactorDirect(), comparisonResult2.getMatchFactorDirect());
							if(sortIndex == 0) {
								/*
								 * Reverse Match Factor Direct
								 */
								sortIndex = Float.compare(comparisonResult1.getReverseMatchFactorDirect(), comparisonResult2.getReverseMatchFactorDirect());
								if(sortIndex == 0) {
									/*
									 * Probability
									 */
									sortIndex = Float.compare(comparisonResult1.getProbability(), comparisonResult2.getProbability());
								}
							}
						}
					}
				}
			}
		}
		/*
		 * Sort
		 */
		if(SortOrder.DESC.equals(sortOrder) && sortIndex != 0) {
			sortIndex *= -1;
		}

		return sortIndex;
	}

	private float calculateDeltaRetentionIndex(ILibraryInformation libraryInformation) {

		float deltaRI = 0.0f;

		if(PreferenceSupplier.isUseRetentionIndexQC()) {
			if(retentionIndexSource != 0.0f) {
				deltaRI = Math.abs(retentionIndexSource - libraryInformation.getRetentionIndex());
			}
		}

		return deltaRI;
	}
}
