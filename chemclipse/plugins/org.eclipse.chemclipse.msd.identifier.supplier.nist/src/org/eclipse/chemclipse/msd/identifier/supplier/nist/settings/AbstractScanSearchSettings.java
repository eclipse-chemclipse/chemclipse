/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.identifier.supplier.nist.settings;

import org.eclipse.chemclipse.msd.identifier.settings.AbstractMassSpectrumIdentifierSettings;

public abstract class AbstractScanSearchSettings extends AbstractMassSpectrumIdentifierSettings implements ISearchSettings {

	@Override
	public int getNumberOfTargets() {

		return 15;
	}

	@Override
	public float getMinMatchFactor() {

		return 70.0f;
	}

	@Override
	public float getMinReverseMatchFactor() {

		return 70.0f;
	}

	@Override
	public int getTimeoutInMinutes() {

		return 20;
	}
}
