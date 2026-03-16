/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.ui.internal.provider;

import org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.model.IdentifierFile;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class IdentifierListTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IdentifierFile identifierFile1 && e2 instanceof IdentifierFile identifierFile2) {

			String column1 = identifierFile1.getSeparationColumn().getName();
			String column2 = identifierFile2.getSeparationColumn().getName();
			String type1 = identifierFile1.getSeparationColumn().getSeparationColumnType().label();
			String type2 = identifierFile2.getSeparationColumn().getSeparationColumnType().label();

			switch(getPropertyIndex()) {
				case 0:
					sortOrder = column2.compareTo(column1);
					break;
				case 1:
					sortOrder = type2.compareTo(type1);
					break;
				case 2:
					sortOrder = identifierFile2.getFile().getName().compareTo(identifierFile1.getFile().getName());
					break;
				case 3:
					sortOrder = identifierFile2.getFile().getAbsolutePath().compareTo(identifierFile1.getFile().getAbsolutePath());
					break;
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}
