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

import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.NoiseSegmentListUI;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;

public class NoiseSegmentEditingSupport extends EditingSupport {

	private NoiseSegmentListUI tableViewer;
	private CellEditor cellEditor;
	private String column;

	public NoiseSegmentEditingSupport(NoiseSegmentListUI tableViewer, String column) {

		super(tableViewer);
		this.column = column;
		if(NoiseSegmentLabelProvider.USE.equals(column)) {
			this.cellEditor = new CheckboxCellEditor(tableViewer.getTable());
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

		if(element instanceof INoiseSegment noiseSegment) {
			if(column.equals(NoiseSegmentLabelProvider.USE)) {
				return noiseSegment.isUse();
			}
		}
		//
		return false;
	}

	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof INoiseSegment noiseSegment) {
			if(column.equals(NoiseSegmentLabelProvider.USE)) {
				noiseSegment.setUse(Boolean.valueOf(value.toString()));
			}
			//
			tableViewer.refresh();
			tableViewer.updateContent();
		}
	}
}