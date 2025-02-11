/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.support.text.ILabel;
import org.eclipse.chemclipse.tsd.model.core.SecondDimensionHint;
import org.eclipse.chemclipse.tsd.model.core.TraceRange;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.TraceRangesListUI;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;

public class TraceRangeEditingSupport extends EditingSupport {

	private String column;
	private CellEditor cellEditor;
	private TraceRangesListUI tableViewer;
	private SecondDimensionHint[] secondDimensionValues = SecondDimensionHint.values();

	public TraceRangeEditingSupport(TraceRangesListUI tableViewer, String column) {

		super(tableViewer);
		this.column = column;
		if(TraceRangeLabelProvider.SECOND_DIMENSION_HINT.equals(column)) {
			this.cellEditor = new ComboBoxCellEditor(tableViewer.getTable(), getEnumLabels(secondDimensionValues), SWT.READ_ONLY);
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
			canEdit = column.equals(TraceRangeLabelProvider.SCAN_INDICES_COLUMN2) || //
					column.equals(TraceRangeLabelProvider.NAME) || //
					column.equals(TraceRangeLabelProvider.TRACES) || //
					column.equals(TraceRangeLabelProvider.SECOND_DIMENSION_HINT); //
		}
		//
		return canEdit;
	}

	@Override
	protected Object getValue(Object element) {

		if(element instanceof TraceRange traceRange) {
			switch(column) {
				case TraceRangeLabelProvider.SCAN_INDICES_COLUMN2:
					return traceRange.getScanIndicesColumn2();
				case TraceRangeLabelProvider.NAME:
					return traceRange.getName();
				case TraceRangeLabelProvider.TRACES:
					return traceRange.getTraces();
				case TraceRangeLabelProvider.SECOND_DIMENSION_HINT:
					return getComboIndexType(traceRange.getSecondDimensionHint(), secondDimensionValues);
			}
		}
		return false;
	}

	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof TraceRange traceRange) {
			switch(column) {
				case TraceRangeLabelProvider.SCAN_INDICES_COLUMN2:
					traceRange.setScanIndicesColumn2(value.toString());
					break;
				case TraceRangeLabelProvider.NAME:
					traceRange.setName(value.toString());
					break;
				case TraceRangeLabelProvider.TRACES:
					traceRange.setTraces(value.toString());
					break;
				case TraceRangeLabelProvider.SECOND_DIMENSION_HINT:
					traceRange.setSecondDimensionHint(secondDimensionValues[(int)value]);
					break;
			}
			//
			tableViewer.refresh();
			tableViewer.updateContent();
		}
	}

	private static String[] getEnumLabels(ILabel[] collection) {

		return Arrays.stream(collection).map(ILabel::label).collect(Collectors.toList()).toArray(new String[collection.length]);
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
		//
		return index;
	}
}