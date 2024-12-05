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
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.NoiseSegmentEditingSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.NoiseSegmentLabelProvider;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.NoiseSegmentTableComparator;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Composite;

public class NoiseSegmentListUI extends ExtendedTableViewer {

	private static final String[] TITLES = NoiseSegmentLabelProvider.TITLES;
	private static final int[] BOUNDS = NoiseSegmentLabelProvider.BOUNDS;
	//
	private IBaseLabelProvider labelProvider = new NoiseSegmentLabelProvider();
	private ViewerComparator tableComparator = new NoiseSegmentTableComparator();
	private IUpdateListener updateListener = null;

	public NoiseSegmentListUI(Composite parent, int style) {

		super(parent, style);
		createColumns();
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public void clear() {

		setInput(null);
	}

	public void updateContent() {

		if(updateListener != null) {
			updateListener.update();
		}
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
			if(label.equals(NoiseSegmentLabelProvider.USE)) {
				tableViewerColumn.setEditingSupport(new NoiseSegmentEditingSupport(this, label));
			}
		}
	}
}
