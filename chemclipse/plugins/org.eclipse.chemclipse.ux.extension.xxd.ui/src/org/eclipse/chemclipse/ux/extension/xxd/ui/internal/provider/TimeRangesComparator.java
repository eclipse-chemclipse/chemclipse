/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.ranges.TimeRange;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class TimeRangesComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof TimeRange timeRange1 && e2 instanceof TimeRange timeRange2) {

			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Integer.compare(timeRange2.getStart(), timeRange1.getStart());
					break;
				case 1:
					sortOrder = Integer.compare(timeRange2.getStop(), timeRange1.getStop());
					break;
				case 2:
					sortOrder = timeRange2.getIdentifier().compareTo(timeRange1.getIdentifier());
					break;
				case 3:
					sortOrder = timeRange2.getTraces().compareTo(timeRange1.getTraces());
					break;
			}
		}

		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}