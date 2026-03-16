/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.support.history.IEditInformation;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class EditHistoryComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IEditInformation info1 && e2 instanceof IEditInformation info2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = info2.getDate().compareTo(info1.getDate());
					break;
				case 1:
					sortOrder = info2.getDescription().compareTo(info1.getDescription());
					break;
				case 2:
					sortOrder = info2.getEditor().compareTo(info1.getEditor());
					break;
				default:
					sortOrder = 0;
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}
