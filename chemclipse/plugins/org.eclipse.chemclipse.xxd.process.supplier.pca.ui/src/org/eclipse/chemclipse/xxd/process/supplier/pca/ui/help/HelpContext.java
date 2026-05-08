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
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.help;

public class HelpContext {

	HelpContext() {

		// static only
	}

	public static final String PREFIX = "org.eclipse.chemclipse.xxd.process.supplier.pca.ui."; //$NON-NLS-1$

	public static final String LOADINGS_PLOT = PREFIX + "loadings_plot"; //$NON-NLS-1$
	public static final String SCORE_PLOT = PREFIX + "score_plot"; //$NON-NLS-1$
	public static final String FOLD_CHANGE_PLOT = PREFIX + "fold_change_plot"; //$NON-NLS-1$
	public static final String S_PLOT = PREFIX + "s_plot"; //$NON-NLS-1$
}
