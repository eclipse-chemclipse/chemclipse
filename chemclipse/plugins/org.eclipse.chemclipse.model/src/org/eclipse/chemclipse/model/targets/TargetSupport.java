/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - adjust to new API
 *******************************************************************************/
package org.eclipse.chemclipse.model.targets;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.ITargetSupplier;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;

public class TargetSupport {

	/**
	 * Returns the best target string representation or "" if none is available.
	 */
	public static String getBestTargetLibraryField(ITargetSupplier targetSupplier) {

		IIdentificationTarget identificationTarget = getBestIdentificationTarget(targetSupplier);
		if(identificationTarget != null) {
			LibraryField libraryField = PreferenceSupplier.getBestTargetLibraryField();
			String name = libraryField.getTransformer().apply(identificationTarget);
			if(name != null) {
				return name;
			}
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * Return the best identification target. May return null.
	 */
	public static IIdentificationTarget getBestIdentificationTarget(ITargetSupplier targetSupplier) {

		/*
		 * Is Retention Index used for QC?
		 */
		float retentionIndex = 0;
		if(PreferenceSupplier.isUseRetentionIndexQC()) {
			if(targetSupplier instanceof IPeak peak) {
				retentionIndex = peak.getPeakModel().getPeakMaximum().getRetentionIndex();
			} else if(targetSupplier instanceof IScan scan) {
				retentionIndex = scan.getRetentionIndex();
			}
		}
		/*
		 * Best Match
		 */
		return IIdentificationTarget.getIdentificationTarget(targetSupplier.getTargets(), retentionIndex);
	}
}
