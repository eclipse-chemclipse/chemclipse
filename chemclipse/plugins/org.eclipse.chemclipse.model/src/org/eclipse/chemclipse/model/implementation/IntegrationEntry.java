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
 *******************************************************************************/
package org.eclipse.chemclipse.model.implementation;

import org.eclipse.chemclipse.model.core.IIntegrationEntry;
import org.eclipse.chemclipse.model.core.IntegrationType;
import org.eclipse.chemclipse.support.traces.ITrace;

public class IntegrationEntry implements IIntegrationEntry {

	private static final long serialVersionUID = -4615194962387454532L;

	private final double signal;
	private final double integratedArea;
	private IntegrationType integrationType = IntegrationType.NONE;

	public IntegrationEntry(double integratedArea) {

		this(ITrace.TOTAL_INTENSITY, integratedArea);
	}

	public IntegrationEntry(double signal, double integratedArea) {

		this.signal = signal;
		this.integratedArea = integratedArea;
	}

	@Override
	public double getSignal() {

		return signal;
	}

	@Override
	public double getIntegratedArea() {

		return integratedArea;
	}

	@Override
	public IntegrationType getIntegrationType() {

		return integrationType;
	}

	@Override
	public void setIntegrationType(IntegrationType integrationType) {

		this.integrationType = integrationType;
	}
}