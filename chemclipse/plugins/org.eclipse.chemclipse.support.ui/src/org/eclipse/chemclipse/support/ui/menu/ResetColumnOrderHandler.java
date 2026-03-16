/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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

public class ResetColumnOrderHandler extends AbstractTableMenuEntry {

	@Override
	public String getCategory() {

		return SupportMessages.columns;
	}

	@Override
	public String getName() {

		return SupportMessages.resetColumnOrder;
	}

	@Override
	public void execute(ExtendedTableViewer extendedTableViewer) {

		extendedTableViewer.resetColumnOrder();
	}
}