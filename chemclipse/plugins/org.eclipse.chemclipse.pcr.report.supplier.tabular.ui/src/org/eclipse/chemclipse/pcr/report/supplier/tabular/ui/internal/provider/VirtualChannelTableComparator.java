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
package org.eclipse.chemclipse.pcr.report.supplier.tabular.ui.internal.provider;

import org.eclipse.chemclipse.pcr.report.supplier.tabular.model.VirtualChannel;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class VirtualChannelTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof VirtualChannel mapping1 && e2 instanceof VirtualChannel mapping2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = mapping1.getSubset().compareTo(mapping2.getSubset());
					break;
				case 1:
					sortOrder = mapping1.getSample().compareTo(mapping2.getSample());
					break;
				case 2:
					sortOrder = mapping1.getSourceChannelString().compareTo(mapping2.getSourceChannelString());
					break;
				case 3:
					sortOrder = Integer.compare(mapping1.getTargetChannel(), mapping2.getTargetChannel());
					break;
			}
		}
		if(getDirection() == DESCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}
