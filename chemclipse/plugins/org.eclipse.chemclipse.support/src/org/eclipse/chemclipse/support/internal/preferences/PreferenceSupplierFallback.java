/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.support.internal.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;

/**
 * NOTE: This class is only used for fallback purposes.
 */
public final class PreferenceSupplierFallback extends AbstractPreferenceSupplier {

	@Override
	public String getPreferenceNode() {

		return "org.eclipse.chemclipse.support.internal.preferences.fallback";
	}

	@Override
	public void initializeDefaults() {

	}
}