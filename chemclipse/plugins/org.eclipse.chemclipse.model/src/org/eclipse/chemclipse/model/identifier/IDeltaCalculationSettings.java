/*******************************************************************************
 * Copyright (c) 2018, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import org.eclipse.chemclipse.model.settings.IProcessSettings;

public interface IDeltaCalculationSettings extends IProcessSettings {

	float MIN_DELTA_WINDOW = 0.0f;
	float MAX_DELTA_WINDOW = Float.MAX_VALUE;

	/**
	 * Retention Time / Index Delta Calculation
	 */
	DeltaCalculation getDeltaCalculation();

	void setDeltaCalculation(DeltaCalculation deltaCalculation);

	float getDeltaWindow();

	void setDeltaWindow(float deltaWindow);
}
