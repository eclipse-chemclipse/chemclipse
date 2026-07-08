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
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider.IonComparator;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider.IonLabelProvider;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider.IonListFilter;
import org.eclipse.swt.widgets.Composite;

public class MassSpectrumIonListUI extends ExtendedTableViewer {

	private static final String[] TITLES = IonLabelProvider.TITLES;
	private static final int[] BOUNDS = IonLabelProvider.BOUNDS;

	private final IonLabelProvider labelProvider = new IonLabelProvider();
	private final IonComparator comparator = new IonComparator();
	private final IonListFilter listFilter = new IonListFilter();

	public MassSpectrumIonListUI(Composite parent, int style) {

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