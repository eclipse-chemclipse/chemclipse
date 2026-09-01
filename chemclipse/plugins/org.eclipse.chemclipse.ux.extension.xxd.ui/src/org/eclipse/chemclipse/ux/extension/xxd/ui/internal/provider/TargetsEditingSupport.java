/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.ui.provider.TargetColumn;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.TargetsListUI;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;

public class TargetsEditingSupport extends EditingSupport {

	private CellEditor cellEditor;
	private TargetsListUI tableViewer;
	private TargetColumn column;

	public TargetsEditingSupport(TargetsListUI tableViewer, TargetColumn column) {

		super(tableViewer);
		this.column = column;
		if(column == TargetColumn.VERIFIED) {
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

		if(column == TargetColumn.VERIFIED) {
			return true;
		} else {
			return tableViewer.isEditEnabled();
		}
	}

	@Override
	protected Object getValue(Object element) {

		if(element instanceof IIdentificationTarget identificationTarget) {
			return switch(column) {
				case VERIFIED -> identificationTarget.isVerified();
				case NAME -> identificationTarget.getLibraryInformation().getName();
				case CAS -> identificationTarget.getLibraryInformation().getCasNumber();
				case COMMENTS -> identificationTarget.getLibraryInformation().getComments();
				case FORMULA -> identificationTarget.getLibraryInformation().getFormula();
				case SMILES -> identificationTarget.getLibraryInformation().getSmiles();
				case INCHI -> identificationTarget.getLibraryInformation().getInChI();
				case INCHI_KEY -> identificationTarget.getLibraryInformation().getInChIKey();
				case CONTRIBUTOR -> identificationTarget.getLibraryInformation().getContributor();
				case REFERENCE_ID -> identificationTarget.getLibraryInformation().getReferenceIdentifier();
				default -> false;
			};
		}

		return false;
	}

	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof IIdentificationTarget identificationTarget) {
			switch(column) {
				case VERIFIED -> identificationTarget.setVerified((boolean)value);
				case NAME -> identificationTarget.getLibraryInformation().setName((String)value);
				case CAS -> identificationTarget.getLibraryInformation().setCasNumber((String)value);
				case COMMENTS -> identificationTarget.getLibraryInformation().setComments((String)value);
				case FORMULA -> identificationTarget.getLibraryInformation().setFormula((String)value);
				case SMILES -> identificationTarget.getLibraryInformation().setSmiles((String)value);
				case INCHI -> identificationTarget.getLibraryInformation().setInChI((String)value);
				case INCHI_KEY -> identificationTarget.getLibraryInformation().setInChIKey((String)value);
				case CONTRIBUTOR -> identificationTarget.getLibraryInformation().setContributor((String)value);
				case REFERENCE_ID -> identificationTarget.getLibraryInformation().setReferenceIdentifier((String)value);
				default -> {
					// The column is not editable.
				}
			}
		}

		tableViewer.refresh();
		tableViewer.updateContent();
		UpdateNotifierUI.update(tableViewer.getTable().getDisplay(), IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_UPDATE, "The target has been edited.");
	}
}
