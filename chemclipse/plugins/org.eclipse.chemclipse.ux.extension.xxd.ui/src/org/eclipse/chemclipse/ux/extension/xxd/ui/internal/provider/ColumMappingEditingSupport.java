/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import java.util.Map;

import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.chemclipse.support.text.ILabel;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;

public class ColumMappingEditingSupport extends EditingSupport {

	private CellEditor cellEditor;
	private ExtendedTableViewer tableViewer;
	private String column;
	private SeparationColumnType[] columnTypes = SeparationColumnType.values();

	public ColumMappingEditingSupport(ExtendedTableViewer tableViewer, String column) {

		super(tableViewer);
		this.column = column;
		if(column.equals(ColumMappingLabelProvider.SEPRATION_COLUMN)) {
			this.cellEditor = new ComboBoxCellEditor(tableViewer.getTable(), ILabel.getLabels(columnTypes), SWT.READ_ONLY);
		} else {
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

		return tableViewer.isEditEnabled();
	}

	@Override
	protected Object getValue(Object element) {

		if(element instanceof Map.Entry<?, ?> setting) {
			switch(column) {
				case ColumMappingLabelProvider.SEPRATION_COLUMN:
					return getIndexColumnType(setting.getValue().toString());
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof Map.Entry<?, ?>) {
			Map.Entry<String, SeparationColumnType> setting = (Map.Entry<String, SeparationColumnType>)element;
			switch(column) {
				case ColumMappingLabelProvider.SEPRATION_COLUMN:
					try {
						setting.setValue(columnTypes[(int)value]);
					} catch(Exception e) {
						setting.setValue(SeparationColumnType.DEFAULT);
					}
					break;
			}
			tableViewer.refresh();
		}
	}

	private int getIndexColumnType(String value) {

		for(int i = 0; i < columnTypes.length; i++) {
			if(columnTypes[i].name().equals(value)) {
				return i;
			}
		}

		return 0; // Default
	}
}