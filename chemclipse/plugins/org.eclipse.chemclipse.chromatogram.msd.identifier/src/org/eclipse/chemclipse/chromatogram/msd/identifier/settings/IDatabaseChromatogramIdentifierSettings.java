/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.identifier.settings;

import org.eclipse.chemclipse.msd.identifier.settings.IDatabaseIdentifierSettings;

public interface IDatabaseChromatogramIdentifierSettings extends IDatabaseIdentifierSettings, IChromatogramIdentifierSettings {

	/*
	 * Identification
	 */
	float getMinSignalToNoiseRatioForIdentification();

	void setMinSignalToNoiseRatioForIdentification(float minSignalToNoiseRatioForIdentification);

	float getMinTailingForIdentification();

	void setMinTailingForIdentification(float minTailingForIdentification);

	float getMaxTailingForIdentification();

	void setMaxTailingForIdentification(float maxTailingForIdentification);

	float getMinMatchFactorForIdentification();

	void setMinMatchFactorForIdentification(float minMatchFactorForIdentification);

	float getMinReverseMatchFactorForIdentification();

	void setMinReverseMatchFactorForIdentification(float minReverseMatchFactorForIdentification);

	boolean isStoreTargetsInChromatogram();

	void setStoreTargetsInChromatogram(boolean storeTargetsInChromatogram);

	float getMatchFactorThresholdForIdentification();

	void setMatchFactorThresholdForIdentification(float matchFactorThresholdForIdentification);

	/*
	 * Database
	 */
	float getMinSignalToNoiseRatioForDatabase();

	void setMinSignalToNoiseRatioForDatabase(float minSignalToNoiseRatioForDatabase);

	float getMinTailingForDatabase();

	void setMinTailingForDatabase(float minTailingForDatabase);

	float getMaxTailingForDatabase();

	void setMaxTailingForDatabase(float maxTailingForDatabase);

	float getMinMatchFactorForDatabase();

	void setMinMatchFactorForDatabase(float minMatchFactorForDatabase);

	float getMinReverseMatchFactorForDatabase();

	void setMinReverseMatchFactorForDatabase(float minReverseMatchFactorForDatabase);
}
