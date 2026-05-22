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
package org.eclipse.chemclipse.chromatogram.msd.filter.core.peak;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.settings.IPeakFilterSettings;
import org.eclipse.chemclipse.support.literature.LiteratureReference;

public interface IPeakFilterSupplier {

	/**
	 * The id of the extension point: e.g.
	 * (org.eclipse.chemclipse.chromatogram.msd.filter.supplier.backgroundRemover)
	 */
	String getId();

	/**
	 * A short description of the functionality of the extension point.
	 */
	String getDescription();

	/**
	 * The filter name that can be shown in a list box dialogue.
	 */
	String getFilterName();

	/**
	 * TODO: either returns a bean-like class or with annotations ..., with a public default constructor, ... or returns <code>null</code> if no filter settings are associated
	 */
	Class<? extends IPeakFilterSettings> getSettingsClass();

	List<LiteratureReference> getLiteratureReferences();
}
