/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - menu category
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.settings;

import java.util.List;

import org.eclipse.chemclipse.model.settings.IProcessSettings;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.processing.core.ICategories;

public interface IMassSpectrumFilterSettings extends IProcessSettings {

	List<MassSpectrumType> appliesToMassSpectrumTypes();

	/**
	 * The menu category the filter shall be listed in, see {@link ICategories}.
	 *
	 * @return String
	 */
	default String getCategory() {

		return ICategories.MASS_SPECTRUM_FILTER;
	}
}
