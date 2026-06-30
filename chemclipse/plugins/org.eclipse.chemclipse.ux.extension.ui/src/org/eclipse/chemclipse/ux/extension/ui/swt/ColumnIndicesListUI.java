/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.ui.swt;

import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.ux.extension.ui.internal.provider.ColumnIndicesComparator;
import org.eclipse.chemclipse.ux.extension.ui.internal.provider.ColumnIndicesLabelProvider;
import org.eclipse.chemclipse.ux.extension.ui.internal.provider.ColumnIndicesListFilter;
import org.eclipse.swt.widgets.Composite;

public class ColumnIndicesListUI extends ExtendedTableViewer {

	private static final String[] TITLES = ColumnIndicesLabelProvider.TITLES;
	private static final int[] BOUNDS = ColumnIndicesLabelProvider.BOUNDS;

	private final ColumnIndicesLabelProvider labelProvider = new ColumnIndicesLabelProvider();
	private final ColumnIndicesComparator comparator = new ColumnIndicesComparator();
	private final ColumnIndicesListFilter listFilter = new ColumnIndicesListFilter();

	public ColumnIndicesListUI(Composite parent, int style) {

		super(parent, style);
		createColumns();
	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		listFilter.setSearchText(searchText, caseSensitive);
		refresh();
	}

	public void clear() {

		setInput(null);
	}

	private void createColumns() {

		createColumns(TITLES, BOUNDS);
		setLabelProvider(labelProvider);
		setContentProvider(new ListContentProvider());
		setComparator(comparator);
		setFilters(listFilter);
	}
}