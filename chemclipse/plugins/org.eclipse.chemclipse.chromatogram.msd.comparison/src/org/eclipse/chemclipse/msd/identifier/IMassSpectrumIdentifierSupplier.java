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

import org.eclipse.chemclipse.model.identifier.core.ISupplier;
import org.eclipse.chemclipse.msd.identifier.settings.IMassSpectrumIdentifierSettings;

public interface IMassSpectrumIdentifierSupplier extends ISupplier {

	@Override
	Class<? extends IMassSpectrumIdentifierSettings> getSettingsClass();
}
