/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.List;

import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.TraceRangeEditingSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.TraceRangeFilter;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.TraceRangeLabelProvider;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.TraceRangeTableComparator;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

public class TraceRangesListUI extends ExtendedTableViewer {

	private static final String[] TITLES = TraceRangeLabelProvider.TITLES;
	private static final int[] BOUNDS = TraceRangeLabelProvider.BOUNDS;
	//
	private TraceRangeLabelProvider labelProvider = new TraceRangeLabelProvider();
	private TraceRangeTableComparator tableComparator = new TraceRangeTableComparator();
	private TraceRangeFilter listFilter = new TraceRangeFilter();
	//
	private IUpdateListener updateListener;

	public TraceRangesListUI(Composite parent, int style) {

		super(parent, style);
		createColumns();
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public void updateContent() {

		if(updateListener != null) {
			updateListener.update();
		}
	}

	public void clear() {

		setInput(null);
	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		listFilter.setSearchText(searchText, caseSensitive);
		refresh();
	}

	private void createColumns() {

		createColumns(TITLES, BOUNDS);
		setLabelProvider(labelProvider);
		setContentProvider(new ListContentProvider());
		setComparator(tableComparator);
		setEditingSupport();
	}

	private void setEditingSupport() {

		List<TableViewerColumn> tableViewerColumns = getTableViewerColumns();
		for(int i = 0; i < tableViewerColumns.size(); i++) {
			TableViewerColumn tableViewerColumn = tableViewerColumns.get(i);
			String label = tableViewerColumn.getColumn().getText();
			tableViewerColumn.setEditingSupport(new TraceRangeEditingSupport(this, label));
		}
	}
}