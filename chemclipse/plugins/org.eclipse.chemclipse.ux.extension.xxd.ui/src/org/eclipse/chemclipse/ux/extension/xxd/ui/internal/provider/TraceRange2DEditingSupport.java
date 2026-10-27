/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.support.text.ILabel;
import org.eclipse.chemclipse.tsd.model.core.SecondDimensionHint;
import org.eclipse.chemclipse.tsd.model.core.TraceRange2D;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.TraceRanges2DListUI;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;

public class TraceRange2DEditingSupport extends EditingSupport {

	private String column;
	private CellEditor cellEditor;
	private TraceRanges2DListUI tableViewer;
	private SecondDimensionHint[] secondDimensionValues = SecondDimensionHint.values();

	public TraceRange2DEditingSupport(TraceRanges2DListUI tableViewer, String column) {

		super(tableViewer);
		this.column = column;
		if(TraceRange2DLabelProvider.SECOND_DIMENSION_HINT.equals(column)) {
			this.cellEditor = new ComboBoxCellEditor(tableViewer.getTable(), ILabel.getLabels(secondDimensionValues), SWT.READ_ONLY);
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

		boolean canEdit = false;
		if(tableViewer.isEditEnabled()) {
			canEdit = column.equals(TraceRange2DLabelProvider.SCAN_INDICES_COLUMN2) || //
					column.equals(TraceRange2DLabelProvider.NAME) || //
					column.equals(TraceRange2DLabelProvider.TRACES) || //
					column.equals(TraceRange2DLabelProvider.SECOND_DIMENSION_HINT); //
		}

		return canEdit;
	}

	@Override
	protected Object getValue(Object element) {

		if(element instanceof TraceRange2D traceRange) {
			switch(column) {
				case TraceRange2DLabelProvider.SCAN_INDICES_COLUMN2:
					return traceRange.getScanIndicesColumn2();
				case TraceRange2DLabelProvider.NAME:
					return traceRange.getName();
				case TraceRange2DLabelProvider.TRACES:
					return traceRange.getTraces();
				case TraceRange2DLabelProvider.SECOND_DIMENSION_HINT:
					return getComboIndexType(traceRange.getSecondDimensionHint(), secondDimensionValues);
			}
		}
		return "";
	}

	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof TraceRange2D traceRange) {
			switch(column) {
				case TraceRange2DLabelProvider.SCAN_INDICES_COLUMN2:
					traceRange.setScanIndicesColumn2(value.toString());
					break;
				case TraceRange2DLabelProvider.NAME:
					traceRange.setName(value.toString());
					break;
				case TraceRange2DLabelProvider.TRACES:
					traceRange.setTraces(value.toString());
					break;
				case TraceRange2DLabelProvider.SECOND_DIMENSION_HINT:
					traceRange.setSecondDimensionHint(secondDimensionValues[(int)value]);
					break;
			}

			tableViewer.refresh();
			tableViewer.updateContent();
		}
	}

	private int getComboIndexType(Enum<?> item, Enum<?>[] collection) {

		int index = 0;
		exitloop:
		for(int i = 0; i < collection.length; i++) {
			if(collection[i].name().equals(item.name())) {
				index = i;
				break exitloop;
			}
		}

		return index;
	}
}