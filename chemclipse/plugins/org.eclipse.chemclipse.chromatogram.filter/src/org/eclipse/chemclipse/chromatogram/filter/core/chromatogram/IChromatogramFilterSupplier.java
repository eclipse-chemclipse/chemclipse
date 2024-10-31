/*******************************************************************************
 * Copyright (c) 2016, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.filter.core.chromatogram;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.support.literature.LiteratureReference;

public interface IChromatogramFilterSupplier {

	/**
	 * The id of the extension point: e.g.
	 * (org.eclipse.chemclipse.chromatogram.msd.filter.supplier.backgroundRemover)
	 * 
	 * @return String
	 */
	String getId();

	/**
	 * A short description of the functionality of the extension point.
	 * 
	 * @return String
	 */
	String getDescription();

	/**
	 * The filter name that can be shown in a list box dialogue.
	 * 
	 * @return String
	 */
	String getFilterName();

	/**
	 * TODO: either returns a bean-like class or with annotations ..., with a public default constructor, ... or returns <code>null</code> if no filter settings are associated
	 * 
	 * @return
	 */
	Class<? extends IChromatogramFilterSettings> getSettingsClass();

	List<LiteratureReference> getLiteratureReferences();
}
