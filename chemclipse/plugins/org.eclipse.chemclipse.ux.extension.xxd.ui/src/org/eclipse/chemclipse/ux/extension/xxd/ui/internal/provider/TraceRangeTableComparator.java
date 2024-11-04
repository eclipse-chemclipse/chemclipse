/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
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
		if(e1 instanceof TraceRange stackRange1 && e2 instanceof TraceRange stackRange2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Integer.compare(stackRange2.getRetentionTimeColumn1Start(), stackRange1.getRetentionTimeColumn1Start());
					break;
				case 1:
					sortOrder = Integer.compare(stackRange2.getRetentionTimeColumn1Stop(), stackRange1.getRetentionTimeColumn1Stop());
					break;
				case 2:
					sortOrder = Integer.compare(stackRange2.getRetentionTimeColumn2Start(), stackRange1.getRetentionTimeColumn2Start());
					break;
				case 3:
					sortOrder = Integer.compare(stackRange2.getRetentionTimeColumn2Stop(), stackRange1.getRetentionTimeColumn2Stop());
					break;
				case 4:
					sortOrder = stackRange2.getScanIndicesColumn2().compareTo(stackRange1.getScanIndicesColumn2());
					break;
				case 5:
					sortOrder = stackRange2.getName().compareTo(stackRange1.getName());
					break;
				case 6:
					sortOrder = stackRange2.getTraces().compareTo(stackRange1.getTraces());
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