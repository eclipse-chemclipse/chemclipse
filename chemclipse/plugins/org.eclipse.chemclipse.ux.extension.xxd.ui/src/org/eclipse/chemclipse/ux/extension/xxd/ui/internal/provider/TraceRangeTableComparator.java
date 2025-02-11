/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.support.ui.swt.IRecordTableComparator;
import org.eclipse.chemclipse.tsd.model.core.TraceRange;
import org.eclipse.jface.viewers.Viewer;

public class TraceRangeTableComparator extends AbstractRecordTableComparator implements IRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof TraceRange traceRange1 && e2 instanceof TraceRange traceRange2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Integer.compare(traceRange2.getRetentionTimeColumn1Start(), traceRange1.getRetentionTimeColumn1Start());
					break;
				case 1:
					sortOrder = Integer.compare(traceRange2.getRetentionTimeColumn1Stop(), traceRange1.getRetentionTimeColumn1Stop());
					break;
				case 2:
					sortOrder = Integer.compare(traceRange2.getRetentionTimeColumn2Start(), traceRange1.getRetentionTimeColumn2Start());
					break;
				case 3:
					sortOrder = Integer.compare(traceRange2.getRetentionTimeColumn2Stop(), traceRange1.getRetentionTimeColumn2Stop());
					break;
				case 4:
					sortOrder = traceRange2.getScanIndicesColumn2().compareTo(traceRange1.getScanIndicesColumn2());
					break;
				case 5:
					sortOrder = traceRange2.getName().compareTo(traceRange1.getName());
					break;
				case 6:
					sortOrder = traceRange2.getTraces().compareTo(traceRange1.getTraces());
					break;
				case 7:
					sortOrder = traceRange2.getSecondDimensionHint().compareTo(traceRange1.getSecondDimensionHint());
					break;
			}
			//
			if(getDirection() == ASCENDING) {
				sortOrder = -sortOrder;
			}
		}
		//
		return sortOrder;
	}
}