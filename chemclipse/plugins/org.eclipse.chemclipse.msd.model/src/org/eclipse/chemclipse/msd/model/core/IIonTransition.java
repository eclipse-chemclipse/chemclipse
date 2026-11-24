/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.core;

import java.io.Serializable;

public interface IIonTransition extends Serializable {

	String getCompoundName();

	void setCompoundName(String compoundName);

	double getQ1StartIon();

	double getQ1StopIon();

	double getQ1Ion();

	double getDeltaQ1Ion();

	double getQ3StartIon();

	double getQ3StopIon();

	double getQ3Ion();

	/*
	 * Handle with care. Only update this
	 * value if you are sure that this
	 * action is performed intentionally.
	 */
	void updateQ3Ion(double q3Ion);

	double getDeltaQ3Ion();

	double getCollisionEnergy();

	int getTransitionGroup();

	double getQ1Resolution();

	double getQ3Resolution();

	int getDwell();

	void setDwell(int dwell);
}