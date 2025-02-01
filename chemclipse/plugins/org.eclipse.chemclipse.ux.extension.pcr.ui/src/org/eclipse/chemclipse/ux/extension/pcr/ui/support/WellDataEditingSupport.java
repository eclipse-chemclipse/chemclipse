/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui.support;

import java.util.Map;

import org.eclipse.chemclipse.pcr.model.core.IWell;
import org.eclipse.chemclipse.ux.extension.pcr.ui.provider.WellDataLabelProvider;
import org.eclipse.chemclipse.ux.extension.pcr.ui.swt.WellDataListUI;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;

public class WellDataEditingSupport extends EditingSupport {

	private CellEditor cellEditor;
	private WellDataListUI tableViewer;
	private String column;

	public WellDataEditingSupport(WellDataListUI tableViewer, String column) {

		super(tableViewer);
		this.column = column;
		if(column.equals(WellDataLabelProvider.VALUE)) {
			this.cellEditor = new TextCellEditor(tableViewer.getTable());
		}
		this.tableViewer = tableViewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {

		return cellEditor;
	}

	@Override
	protected boolean canEdit(Object element) {

		if(column.equals(WellDataLabelProvider.VALUE)) {
			return tableViewer.isEditEnabled();
		} else {
			return false;
		}
	}

	@Override
	protected Object getValue(Object element) {

		if(element instanceof Map.Entry<?, ?> entry) {
			if(column.equals(WellDataLabelProvider.VALUE)) {
				return entry.getValue();
			}
		}
		return false;
	}

	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof Map.Entry<?, ?> entry) {
			IWell well = tableViewer.getWell();
			if(well != null) {
				if(column.equals(WellDataLabelProvider.VALUE)) {
					well.putHeaderData((String)entry.getKey(), (String)value);
				}
				tableViewer.refresh();
			}
		}
	}
}
