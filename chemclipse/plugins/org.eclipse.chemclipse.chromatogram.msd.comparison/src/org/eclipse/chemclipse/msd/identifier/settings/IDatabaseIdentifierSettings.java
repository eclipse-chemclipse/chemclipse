/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.identifier.settings;

import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;

public interface IDatabaseIdentifierSettings extends IIdentifierSettings {

	// Database
	String getForceMatchFactorPenaltyCalculationForDatabase();

	void setForceMatchFactorPenaltyCalculationForDatabase(String forceMatchFactorPenaltyCalculation);

	int getRetentionTimeWindowForDatabase();

	void setRetentionTimeWindowForDatabase(int retentionTimeWindow);

	int getRetentionIndexWindowForDatabase();

	void setRetentionIndexWindowForDatabase(int retentionIndexWindow);
}
