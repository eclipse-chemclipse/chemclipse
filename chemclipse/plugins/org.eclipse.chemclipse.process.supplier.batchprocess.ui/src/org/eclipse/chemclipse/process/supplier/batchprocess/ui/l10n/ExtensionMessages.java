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
package org.eclipse.chemclipse.process.supplier.batchprocess.ui.l10n;

import org.eclipse.osgi.util.NLS;

public class ExtensionMessages extends NLS {

	public static String batchProcess;
	public static String batchProcessJob;
	public static String pathImportRecords;
	public static String saveBatchJob;

	static {
		NLS.initializeMessages("org.eclipse.chemclipse.process.supplier.batchprocess.ui.l10n.messages", ExtensionMessages.class); //$NON-NLS-1$
	}

	private ExtensionMessages() {

	}
}
