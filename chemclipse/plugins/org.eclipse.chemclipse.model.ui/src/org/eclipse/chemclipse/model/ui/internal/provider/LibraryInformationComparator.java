/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.ui.internal.provider;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class LibraryInformationComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof ILibraryInformation libraryInformation1 && e2 instanceof ILibraryInformation libraryInformation2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = libraryInformation2.getName().compareTo(libraryInformation1.getName());
					break;
				case 1:
					sortOrder = Integer.compare(libraryInformation2.getSynonyms().size(), libraryInformation1.getSynonyms().size());
					break;
				case 2:
					sortOrder = Integer.compare(libraryInformation2.getRetentionTime(), libraryInformation1.getRetentionTime());
					break;
				case 3:
					sortOrder = libraryInformation2.getContributor().compareTo(libraryInformation1.getContributor());
					break;
				case 4:
					sortOrder = libraryInformation2.getCasNumber().compareTo(libraryInformation1.getCasNumber());
					break;
				case 5:
					sortOrder = libraryInformation2.getFormula().compareTo(libraryInformation1.getFormula());
					break;
				case 6:
					sortOrder = libraryInformation2.getSmiles().compareTo(libraryInformation1.getSmiles());
					break;
				case 7:
					sortOrder = libraryInformation2.getInChI().compareTo(libraryInformation1.getInChI());
					break;
				case 8:
					sortOrder = libraryInformation2.getDatabase().compareTo(libraryInformation1.getDatabase());
					break;
				case 9:
					sortOrder = Integer.compare(libraryInformation2.getDatabaseIndex(), libraryInformation1.getDatabaseIndex());
					break;
				case 10:
					sortOrder = libraryInformation2.getComments().compareTo(libraryInformation1.getComments());
					break;
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}