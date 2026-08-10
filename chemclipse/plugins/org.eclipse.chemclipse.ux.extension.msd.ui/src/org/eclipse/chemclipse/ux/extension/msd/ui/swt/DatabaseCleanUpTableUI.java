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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider.DatabaseCleanUpLabelProvider;
import org.eclipse.swt.widgets.Composite;

public class DatabaseCleanUpTableUI extends ExtendedTableViewer {

	private static final String[] TITLES = DatabaseCleanUpLabelProvider.TITLES;
	private static final int[] BOUNDS = DatabaseCleanUpLabelProvider.BOUNDS;

	public DatabaseCleanUpTableUI(Composite parent, int style) {

		super(parent, style);
		createColumns();
	}

	private void createColumns() {

		createColumns(TITLES, BOUNDS);
		setLabelProvider(new DatabaseCleanUpLabelProvider());
		setContentProvider(ListContentProvider.getInstance());
	}
}
