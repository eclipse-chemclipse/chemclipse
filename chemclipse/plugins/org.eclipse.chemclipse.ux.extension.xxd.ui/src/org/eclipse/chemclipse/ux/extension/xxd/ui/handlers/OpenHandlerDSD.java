/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.handlers;

import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;

public class OpenHandlerDSD extends AbstractOpenHandler {

	@Override
	protected DataType getDataType() {

		return DataType.DSD;
	}

	@Override
	protected String getPreferenceKey() {

		return PreferenceSupplier.P_FILTER_PATH_CHROMATOGRAM_DSD;
	}
}
