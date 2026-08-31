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
package org.eclipse.chemclipse.msd.swt.ui.support;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.notifier.MassSpectrumUpdateNotifier;
import org.eclipse.swt.widgets.Display;

public class MassSpectrumUpdateNotifierUI {

	public static void update(Display display, IScanMSD scan) {

		if(display != null) {
			display.asyncExec(() -> MassSpectrumUpdateNotifier.update(scan));
		}
	}
}
