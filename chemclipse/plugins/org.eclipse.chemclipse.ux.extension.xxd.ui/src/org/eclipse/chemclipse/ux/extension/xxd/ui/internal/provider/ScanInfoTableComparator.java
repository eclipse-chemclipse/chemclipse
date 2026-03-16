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

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class ScanInfoTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IScanMSD ion1 && e2 instanceof IScanMSD ion2) {

			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Integer.compare(ion2.getScanNumber(), ion1.getScanNumber());
					break;
				case 1:
					sortOrder = Integer.compare(ion2.getRetentionTime(), ion1.getRetentionTime());
					break;
				case 2:
					sortOrder = Integer.compare(ion2.getNumberOfIons(), ion1.getNumberOfIons());
					break;
				case 3:
					sortOrder = Integer.compare(ion2.getNumberOfIons(), ion1.getNumberOfIons());
					break;
				case 4:
					sortOrder = Integer.compare(ion2.getNumberOfIons(), ion1.getNumberOfIons());
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
