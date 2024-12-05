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

import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.support.ui.swt.IRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class NoiseSegmentTableComparator extends AbstractRecordTableComparator implements IRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof INoiseSegment noiseSegment1 && e2 instanceof INoiseSegment noiseSegment2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Integer.compare(noiseSegment2.getStartRetentionTime(), noiseSegment1.getStartRetentionTime());
					break;
				case 1:
					sortOrder = Integer.compare(noiseSegment2.getStopRetentionTime(), noiseSegment1.getStopRetentionTime());
					break;
				case 2:
					sortOrder = Boolean.compare(noiseSegment2.isUserSelection(), noiseSegment1.isUserSelection());
					break;
				case 3:
					sortOrder = Integer.compare(noiseSegment2.getStartScan(), noiseSegment1.getStartScan());
					break;
				case 4:
					sortOrder = Integer.compare(noiseSegment2.getStopScan(), noiseSegment1.getStopScan());
					break;
				case 5:
					sortOrder = Integer.compare(noiseSegment2.getWidth(), noiseSegment1.getWidth());
					break;
				case 6:
					sortOrder = Boolean.compare(noiseSegment2.isUse(), noiseSegment1.isUse());
					break;
				case 7:
					sortOrder = Double.compare(noiseSegment2.getNoiseFactor(), noiseSegment1.getNoiseFactor());
					break;
				default:
					sortOrder = 0;
			}
		}
		//
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		//
		return sortOrder;
	}
}