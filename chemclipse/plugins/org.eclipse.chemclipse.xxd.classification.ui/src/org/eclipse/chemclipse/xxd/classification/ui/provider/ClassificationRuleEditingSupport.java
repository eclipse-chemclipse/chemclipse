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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.classification.ui.provider;

import org.eclipse.chemclipse.support.text.ILabel;
import org.eclipse.chemclipse.xxd.classification.model.ClassificationRule;
import org.eclipse.chemclipse.xxd.classification.model.Reference;
import org.eclipse.chemclipse.xxd.classification.ui.swt.ClassificationDictionaryListUI;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;

public class ClassificationRuleEditingSupport extends EditingSupport {

	private String column;
	private CellEditor cellEditor;
	private ClassificationDictionaryListUI tableViewer;

	private Reference[] references = Reference.values();

	public ClassificationRuleEditingSupport(ClassificationDictionaryListUI tableViewer, String column) {

		super(tableViewer);
		this.column = column;
		if(ClassificationRuleLabelProvider.REFERENCE.equals(column)) {
			this.cellEditor = new ComboBoxCellEditor(tableViewer.getTable(), ILabel.getLabels(references), SWT.READ_ONLY);
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

		if(element instanceof ClassificationRule classificationRule) {
			switch(column) {
				case ClassificationRuleLabelProvider.SEARCH_EXPRESSION:
					return classificationRule.getSearchExpression();
				case ClassificationRuleLabelProvider.CLASSIFICATION:
					return classificationRule.getClassification();
				case ClassificationRuleLabelProvider.REFERENCE:
					return getComboIndex(classificationRule.getReference(), references);
			}
		}
		return false;
	}

	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof ClassificationRule classificationRule) {
			switch(column) {
				case ClassificationRuleLabelProvider.SEARCH_EXPRESSION:
					classificationRule.setSearchExpression((String)value);
					break;
				case ClassificationRuleLabelProvider.CLASSIFICATION:
					classificationRule.setClassification((String)value);
					break;
				case ClassificationRuleLabelProvider.REFERENCE:
					classificationRule.setReference(getReference(value));
					break;
			}

			tableViewer.refresh();
			tableViewer.updateContent();
		}
	}

	private int getComboIndex(Reference item, Reference[] items) {

		int index = 0;
		exitloop:
		for(int i = 0; i < items.length; i++) {
			if(items[i].equals(item)) {
				index = i;
				break exitloop;
			}
		}
		return index;
	}

	private Reference getReference(Object value) {

		try {
			return references[(int)value];
		} catch(Exception e) {
			return Reference.NAME;
		}
	}
}
