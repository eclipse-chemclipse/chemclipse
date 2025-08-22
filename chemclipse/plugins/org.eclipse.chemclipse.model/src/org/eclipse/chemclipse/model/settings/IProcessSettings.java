/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.settings;

import java.util.List;

import org.eclipse.chemclipse.support.literature.LiteratureReference;

public interface IProcessSettings {

	/*
	 * File name placeholder
	 */
	public static final String VARIABLE_CHROMATOGRAM_NAME = "{chromatogram_name}";
	public static final String VARIABLE_CHROMATOGRAM_DATANAME = "{chromatogram_dataname}";
	public static final String VARIABLE_CHROMATOGRAM_SAMPLEGROUP = "{chromatogram_samplegroup}";
	public static final String VARIABLE_CHROMATOGRAM_SHORTINFO = "{chromatogram_shortinfo}";
	public static final String VARIABLE_EXTENSION = "{extension}";
	/*
	 * Directory placeholder
	 */
	public static final String VARIABLE_CURRENT_DIRECTORY = "{current_directory}";

	/**
	 * Use this method to set specific system settings.
	 * 
	 * @deprecated this method will be removed soon
	 */
	@Deprecated
	default void setSystemSettings() {

		throw new UnsupportedOperationException();
	}

	List<LiteratureReference> getLiteratureReferences();
}