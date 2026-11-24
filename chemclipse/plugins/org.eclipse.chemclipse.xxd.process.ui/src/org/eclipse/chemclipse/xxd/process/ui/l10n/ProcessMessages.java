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
package org.eclipse.chemclipse.xxd.process.ui.l10n;

import org.eclipse.osgi.util.NLS;

public class ProcessMessages extends NLS {

	public static String labelCategory;
	public static String labelDataType;
	public static String labelDescription;
	public static String labelID;
	public static String labelNA;
	public static String labelName;
	public static String processorImage;
	public static String processorMenuBlacklist;
	public static String processorMenuBlacklistDescription;
	public static String remove;
	public static String searchAvailableProcessorItems;

	static {
		NLS.initializeMessages("org.eclipse.chemclipse.xxd.process.ui.l10n.messages", ProcessMessages.class); //$NON-NLS-1$
	}

	private ProcessMessages() {

	}
}