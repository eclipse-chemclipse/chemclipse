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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.tsd.model.core.TraceRange;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.TraceRangesListUI;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;

public class TraceRangeEditingSupport extends EditingSupport {

	private String column;
	private CellEditor cellEditor;
	private TraceRangesListUI tableViewer;

	public TraceRangeEditingSupport(TraceRangesListUI tableViewer, String column) {

		super(tableViewer);
		this.column = column;
		this.cellEditor = new TextCellEditor(tableViewer.getTable());
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

		if(element instanceof TraceRange stackRange) {
			switch(column) {
				case TraceRangeLabelProvider.RETENTION_TIME_COLUMN1_START:
					return stackRange.getRetentionTimeColumn1Start() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR;
				case TraceRangeLabelProvider.RETENTION_TIME_COLUMN1_STOP:
					return stackRange.getRetentionTimeColumn1Stop() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR;
				case TraceRangeLabelProvider.RETENTION_TIME_COLUMN2_START:
					return stackRange.getRetentionTimeColumn2Start() / IChromatogramOverview.SECOND_CORRELATION_FACTOR;
				case TraceRangeLabelProvider.RETENTION_TIME_COLUMN2_STOP:
					return stackRange.getRetentionTimeColumn2Stop() / IChromatogramOverview.SECOND_CORRELATION_FACTOR;
				case TraceRangeLabelProvider.SCAN_INDICES_COLUMN2:
					return stackRange.getScanIndicesColumn2();
				case TraceRangeLabelProvider.NAME:
					return stackRange.getName();
				case TraceRangeLabelProvider.TRACES:
					return stackRange.getTraces();
			}
		}
		return false;
	}

	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof TraceRange stackRange) {
			switch(column) {
				case TraceRangeLabelProvider.RETENTION_TIME_COLUMN1_START:
					stackRange.setRetentionTimeColumn1Start(getRetentionTime(value.toString()));
					break;
				case TraceRangeLabelProvider.RETENTION_TIME_COLUMN1_STOP:
					stackRange.setRetentionTimeColumn1Stop(getRetentionTime(value.toString()));
					break;
				case TraceRangeLabelProvider.RETENTION_TIME_COLUMN2_START:
					stackRange.setRetentionTimeColumn2Start(getRetentionTime(value.toString()));
					break;
				case TraceRangeLabelProvider.RETENTION_TIME_COLUMN2_STOP:
					stackRange.setRetentionTimeColumn2Stop(getRetentionTime(value.toString()));
					break;
				case TraceRangeLabelProvider.SCAN_INDICES_COLUMN2:
					stackRange.setScanIndicesColumn2(value.toString());
					break;
				case TraceRangeLabelProvider.NAME:
					stackRange.setName(value.toString());
					break;
				case TraceRangeLabelProvider.TRACES:
					stackRange.setTraces(value.toString());
					break;
			}
			//
			tableViewer.refresh();
			tableViewer.updateContent();
		}
	}

	private int getRetentionTime(String value) {

		try {
			return (int)(Double.parseDouble(value) * IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
		} catch(NumberFormatException e) {
			return 0;
		}
	}
}