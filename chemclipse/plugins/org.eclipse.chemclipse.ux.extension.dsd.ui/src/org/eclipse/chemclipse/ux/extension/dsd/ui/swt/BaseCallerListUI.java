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
package org.eclipse.chemclipse.ux.extension.dsd.ui.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.ITargetSupplier;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.ux.extension.dsd.ui.internal.provider.BaseCallerListEditingSupport;
import org.eclipse.chemclipse.ux.extension.dsd.ui.internal.provider.BaseCallerListFilter;
import org.eclipse.chemclipse.ux.extension.dsd.ui.internal.provider.BaseCallerListLabelProvider;
import org.eclipse.chemclipse.ux.extension.dsd.ui.internal.provider.BaseCallerListTableComparator;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

public class BaseCallerListUI extends ExtendedTableViewer {

	private static final String[] titles = BaseCallerListLabelProvider.TITLES;
	private static final int[] bounds = BaseCallerListLabelProvider.BOUNDS;

	private final BaseCallerListLabelProvider labelProvider = new BaseCallerListLabelProvider();
	private final BaseCallerListTableComparator tableComparator = new BaseCallerListTableComparator();
	private final BaseCallerListFilter listFilter = new BaseCallerListFilter();

	private List<ITargetSupplier> targetSuppliers = new ArrayList<>();

	public BaseCallerListUI(Composite parent, int style) {

		super(parent, style);
		createColumns();
	}

	public void setInput(IChromatogramSelection chromatogramSelection) {

		if(chromatogramSelection != null) {
			targetSuppliers.clear();
			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			targetSuppliers.addAll(getIdentifiedScans(chromatogram));
			super.setInput(targetSuppliers);
		} else {
			clear();
		}
	}

	private static List<IScan> getIdentifiedScans(IChromatogram chromatogram) {

		List<IScan> scans = new ArrayList<>();
		if(chromatogram != null) {
			for(IScan scan : chromatogram.getScans()) {
				if(!scan.getTargets().isEmpty()) {
					scans.add(scan);
				}
			}
		}
		return scans;
	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		listFilter.setSearchText(searchText, caseSensitive);
		refresh();
	}

	public void clear() {

		super.setInput(null);
	}

	private void createColumns() {

		createColumns(titles, bounds);
		setLabelProvider(labelProvider);
		setContentProvider(new ListContentProvider());
		setComparator(tableComparator);
		setFilters(listFilter);
		setEditingSupport();
	}

	private void setEditingSupport() {

		List<TableViewerColumn> tableViewerColumns = getTableViewerColumns();
		for(int i = 0; i < tableViewerColumns.size(); i++) {
			TableViewerColumn tableViewerColumn = tableViewerColumns.get(i);
			String label = tableViewerColumn.getColumn().getText();
			if(label.equals(BaseCallerListLabelProvider.BEST_TARGET)) {
				tableViewerColumn.setEditingSupport(new BaseCallerListEditingSupport(this, label));
			}
		}
	}
}
