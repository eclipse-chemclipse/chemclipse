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

public abstract class AbstractDatabaseIdentifierSettings extends AbstractIdentifierSettingsMSD implements IDatabaseIdentifierSettings {

	private int retentionTimeWindowForDatabase; // milliseconds
	private int retentionIndexWindowForDatabase;
	private String forceMatchFactorPenaltyCalculationForDatabase;

	@Override
	public String getForceMatchFactorPenaltyCalculationForDatabase() {

		return forceMatchFactorPenaltyCalculationForDatabase;
	}

	@Override
	public void setForceMatchFactorPenaltyCalculationForDatabase(String forceMatchFactorPenaltyCalculationForDatabase) {

		this.forceMatchFactorPenaltyCalculationForDatabase = forceMatchFactorPenaltyCalculationForDatabase;
	}

	@Override
	public int getRetentionTimeWindowForDatabase() {

		return retentionTimeWindowForDatabase;
	}

	@Override
	public void setRetentionTimeWindowForDatabase(int retentionTimeWindowForDatabase) {

		this.retentionTimeWindowForDatabase = retentionTimeWindowForDatabase;
	}

	@Override
	public int getRetentionIndexWindowForDatabase() {

		return retentionIndexWindowForDatabase;
	}

	@Override
	public void setRetentionIndexWindowForDatabase(int retentionIndexWindowForDatabase) {

		this.retentionIndexWindowForDatabase = retentionIndexWindowForDatabase;
	}
}
