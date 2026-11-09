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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.ui.l10n;

import org.eclipse.osgi.util.NLS;

public class ExtensionMessages extends NLS {

	public static String inputMustBeInteger;
	public static String startRI;
	public static String startRT;
	public static String startScan;
	public static String stopRI;
	public static String stopRT;
	public static String stopScan;

	static {
		NLS.initializeMessages("org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.ui.l10n.messages", ExtensionMessages.class); //$NON-NLS-1$
	}

	private ExtensionMessages() {

	}
}
