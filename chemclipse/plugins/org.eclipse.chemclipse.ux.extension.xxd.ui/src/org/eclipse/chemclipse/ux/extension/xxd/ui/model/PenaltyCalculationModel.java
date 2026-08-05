/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.model;

import org.eclipse.chemclipse.model.identifier.IPenaltyCalculationSettings;
import org.eclipse.chemclipse.model.identifier.PenaltyCalculationSettings;

public class PenaltyCalculationModel {

	private double referenceValue = 0.0d;
	private IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();

	public double getReferenceValue() {

		return referenceValue;
	}

	public void setReferenceValue(double referenceValue) {

		this.referenceValue = referenceValue;
	}

	public IPenaltyCalculationSettings getPenaltyCalculationSettings() {

		return penaltyCalculationSettings;
	}
}