/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - extend IIdentifierSettingsMSD
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.settings;

import org.eclipse.chemclipse.model.identifier.IDeltaCalculationSettings;
import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;
import org.eclipse.chemclipse.model.identifier.IPenaltyCalculationSettings;
import org.eclipse.chemclipse.msd.identifier.comparison.IMassSpectrumComparator;

public interface IFileIdentifierSettings extends IIdentifierSettings, IPenaltyCalculationSettings, IDeltaCalculationSettings {

	String getMassSpectraFiles();

	void setMassSpectraFiles(String massSpectraFiles);

	boolean isUsePreOptimization();

	void setUsePreOptimization(boolean usePreOptimization);

	double getThresholdPreOptimization();

	void setThresholdPreOptimization(double thresholdPreOptimization);

	int getNumberOfTargets();

	void setNumberOfTargets(int numberOfTargets);

	float getMinMatchFactor();

	void setMinMatchFactor(float minMatchFactor);

	float getMinReverseMatchFactor();

	void setMinReverseMatchFactor(float minReverseMatchFactor);

	String getAlternateIdentifierId();

	void setAlternateIdentifierId(String alternateIdentifierId);

	IMassSpectrumComparator getMassSpectrumComparator();
}
