/*******************************************************************************
 * Copyright (c) 2010, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Jan Holy - implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier;

import org.eclipse.chemclipse.model.identifier.core.AbstractSupplier;
import org.eclipse.chemclipse.msd.identifier.settings.IMassSpectrumIdentifierSettings;

public class MassSpectrumIdentifierSupplier extends AbstractSupplier<IMassSpectrumIdentifierSettings> implements IMassSpectrumIdentifierSupplier {

	@Override
	public Class<? extends IMassSpectrumIdentifierSettings> getSettingsClass() {

		return getSpecificIdentifierSettingsClass();
	}
}
