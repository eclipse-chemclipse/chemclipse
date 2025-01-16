/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.vsd.converter.ui.l10n;

import org.eclipse.osgi.util.NLS;

public class VibrationalSpectroscopyMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.chemclipse.vsd.converter.ui.l10n.messages"; //$NON-NLS-1$
	//
	public static String saveIR;
	public static String saveRaman;
	//
	static {
		NLS.initializeMessages(BUNDLE_NAME, VibrationalSpectroscopyMessages.class);
	}

	private VibrationalSpectroscopyMessages() {

	}
}