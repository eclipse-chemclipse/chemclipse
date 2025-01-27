/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.locations;

import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.ux.extension.ui.internal.provider.UserLocationsComparator;
import org.eclipse.chemclipse.ux.extension.ui.internal.provider.UserLocationsFilter;
import org.eclipse.chemclipse.ux.extension.ui.internal.provider.UserLocationsLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;

public class UserLocationsListUI extends ExtendedTableViewer {

	private static final String[] TITLES = UserLocationsLabelProvider.TITLES;
	private static final int[] BOUNDS = UserLocationsLabelProvider.BOUNDS;
	//
	private ITableLabelProvider labelProvider = new UserLocationsLabelProvider();
	private ViewerComparator tableComparator = new UserLocationsComparator();
	private UserLocationsFilter listFilter = new UserLocationsFilter();

	public UserLocationsListUI(Composite parent, int style) {

		super(parent, style);
		createColumns();
	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		listFilter.setSearchText(searchText, caseSensitive);
		refresh();
	}

	public void setSortEnabled(boolean sortEnabled) {

		setComparator((sortEnabled) ? tableComparator : null);
	}

	public void clear() {

		setInput(null);
	}

	private void createColumns() {

		createColumns(TITLES, BOUNDS);
		setLabelProvider(labelProvider);
		setContentProvider(new ListContentProvider());
		setComparator(tableComparator);
		setFilters(new ViewerFilter[]{listFilter});
	}
}