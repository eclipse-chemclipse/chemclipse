/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier.settings;

import java.util.List;

import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;

public interface IMassSpectrumIdentifierSettings extends IIdentifierSettings {

	/**
	 * The mass spectrum types the identifier is able to handle, see {@link MassSpectrumType}.
	 * Identifiers that evaluate profile data need to override this method.
	 *
	 * @return List
	 */
	default List<MassSpectrumType> appliesToMassSpectrumTypes() {

		return List.of(MassSpectrumType.CENTROID);
	}
}
