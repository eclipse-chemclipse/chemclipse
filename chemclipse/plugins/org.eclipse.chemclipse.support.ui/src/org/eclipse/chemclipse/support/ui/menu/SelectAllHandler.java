/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.support.ui.menu;

import org.eclipse.chemclipse.support.ui.l10n.SupportMessages;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;

public class SelectAllHandler extends AbstractTableMenuEntry {

	@Override
	public String getCategory() {

		return ""; // Must be empty to be placed on the main menu level. //$NON-NLS-1$
	}

	@Override
	public String getName() {

		return SupportMessages.selectAll;
	}

	@Override
	public void execute(ExtendedTableViewer extendedTableViewer) {

		extendedTableViewer.getTable().selectAll();
	}
}
