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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.swt.ui.components.identification;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.msd.swt.ui.internal.provider.CasNumbersListContentProvider;
import org.eclipse.chemclipse.msd.swt.ui.internal.provider.CasNumbersListLabelProvider;
import org.eclipse.chemclipse.msd.swt.ui.internal.provider.CasNumbersListTableComparator;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.swt.widgets.Composite;

public class CasNumbersListUI extends ExtendedTableViewer {

	private String[] titles = {"CAS#"};
	private int[] bounds = {300};

	public CasNumbersListUI(Composite parent) {

		super(parent);
		createColumns();
	}

	public CasNumbersListUI(Composite parent, int style) {

		super(parent, style);
		createColumns();
	}

	private void createColumns() {

		createColumns(titles, bounds);

		setLabelProvider(new CasNumbersListLabelProvider());
		setContentProvider(new CasNumbersListContentProvider());
		setComparator(new CasNumbersListTableComparator());
	}

	public void update(ILibraryInformation libraryInformation) {

		if(libraryInformation != null) {
			setInput(libraryInformation.getCasNumbers());
		}
	}
}