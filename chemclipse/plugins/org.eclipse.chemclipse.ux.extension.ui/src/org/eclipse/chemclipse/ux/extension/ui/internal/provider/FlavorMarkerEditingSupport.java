/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.ui.internal.provider;

import org.eclipse.chemclipse.model.identifier.IFlavorMarker;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;

public class FlavorMarkerEditingSupport extends EditingSupport {

	private CellEditor cellEditor;
	private ExtendedTableViewer tableViewer;
	private String column;

	public FlavorMarkerEditingSupport(ExtendedTableViewer tableViewer, String column) {

		super(tableViewer);
		this.column = column;
		if(column.equals(FlavorMarkerLabelProvider.VERIFIED_MANUALLY)) {
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

		if(column == FlavorMarkerLabelProvider.VERIFIED_MANUALLY) {
			return true;
		} else {
			return tableViewer.isEditEnabled();
		}
	}

	@Override
	protected Object getValue(Object element) {

		if(element instanceof IFlavorMarker flavorMarker) {
			switch(column) {
				case FlavorMarkerLabelProvider.VERIFIED_MANUALLY:
					return flavorMarker.isManuallyVerified();
			}
		}
		return false;
	}

	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof IFlavorMarker flavorMarker) {
			switch(column) {
				case FlavorMarkerLabelProvider.VERIFIED_MANUALLY:
					flavorMarker.setManuallyVerified((boolean)value);
					break;
			}
			tableViewer.refresh();
		}
	}
}