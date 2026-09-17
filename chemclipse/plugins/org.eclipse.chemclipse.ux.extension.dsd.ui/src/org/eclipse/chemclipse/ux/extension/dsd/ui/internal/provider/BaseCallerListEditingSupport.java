/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.dsd.ui.internal.provider;

import org.eclipse.chemclipse.dsd.model.core.Nucleobase;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.targets.TargetSupport;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.SWT;

public class BaseCallerListEditingSupport extends EditingSupport {

	private final Nucleobase[] values = Nucleobase.values();
	private final CellEditor cellEditor;
	private final ExtendedTableViewer tableViewer;
	private final String column;

	public BaseCallerListEditingSupport(ExtendedTableViewer tableViewer, String column) {

		super(tableViewer);
		this.column = column;
		this.cellEditor = new ComboBoxCellEditor(tableViewer.getTable(), getLabels(), SWT.READ_ONLY);
		this.tableViewer = tableViewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {

		return cellEditor;
	}

	@Override
	protected boolean canEdit(Object element) {

		return element instanceof IScan && column.equals(BaseCallerListLabelProvider.BEST_TARGET);
	}

	@Override
	protected Object getValue(Object element) {

		if(element instanceof IScan scan && column.equals(BaseCallerListLabelProvider.BEST_TARGET)) {
			char letter = TargetSupport.getBestTargetLibraryField(scan).charAt(0);
			for(int i = 0; i < values.length; i++) {
				if(values[i].equals(Nucleobase.of(letter))) {
					return i;
				}
			}
		}

		return -1;
	}

	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof IScan scan && value instanceof Integer index && index >= 0 && index < values.length && column.equals(BaseCallerListLabelProvider.BEST_TARGET)) {
			Nucleobase selectedValue = values[index];

			ILibraryInformation libraryInformation = new LibraryInformation();
			libraryInformation.setName(String.valueOf(selectedValue.letter()));

			IComparisonResult comparisonResult = new ComparisonResult(100);
			IIdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, comparisonResult);

			scan.getTargets().add(identificationTarget);
			tableViewer.refresh(element);
		}
	}

	private String[] getLabels() {

		String[] labels = new String[values.length];
		for(int i = 0; i < values.length; i++) {
			labels[i] = values[i].toString();
		}
		return labels;
	}
}
