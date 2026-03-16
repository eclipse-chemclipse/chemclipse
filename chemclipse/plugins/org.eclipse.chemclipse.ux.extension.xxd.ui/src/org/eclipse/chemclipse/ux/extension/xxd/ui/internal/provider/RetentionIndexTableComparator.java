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

import org.eclipse.chemclipse.model.columns.IRetentionIndexEntry;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class RetentionIndexTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IRetentionIndexEntry retentionIndexEntry1 && e2 instanceof IRetentionIndexEntry retentionIndexEntry2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Integer.compare(retentionIndexEntry2.getRetentionTime(), retentionIndexEntry1.getRetentionTime());
					break;
				case 1:
					sortOrder = Float.compare(retentionIndexEntry2.getRetentionIndex(), retentionIndexEntry1.getRetentionIndex());
					break;
				case 2:
					sortOrder = retentionIndexEntry2.getName().compareTo(retentionIndexEntry1.getName());
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
