/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - prediction
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.support.ui.swt.IRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class SamplesComparator extends AbstractRecordTableComparator implements IRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof ISample sample1 && e2 instanceof ISample sample2) {
			//
			switch(getPropertyIndex()) {
				case 0:
					String name1 = sample1.getSampleName() != null ? sample1.getSampleName() : "";
					String name2 = sample2.getSampleName() != null ? sample2.getSampleName() : "";
					sortOrder = name2.compareTo(name1);
					break;
				case 1:
					sortOrder = Boolean.compare(sample2.isSelected(), sample1.isSelected());
					break;
				case 2:
					sortOrder = Boolean.compare(sample2.isPredicted(), sample1.isPredicted());
					break;
				case 3: // Color is defined by the group name
				case 4:
					String groupName1 = sample1.getGroupName() != null ? sample1.getGroupName() : "";
					String groupName2 = sample2.getGroupName() != null ? sample2.getGroupName() : "";
					sortOrder = groupName2.compareTo(groupName1);
					break;
				case 5:
					String classification1 = sample1.getClassification() != null ? sample1.getClassification() : "";
					String classification2 = sample2.getClassification() != null ? sample2.getClassification() : "";
					sortOrder = classification2.compareTo(classification1);
					break;
				case 6:
					String description1 = sample1.getDescription() != null ? sample1.getDescription() : "";
					String description2 = sample2.getDescription() != null ? sample2.getDescription() : "";
					sortOrder = description2.compareTo(description1);
					break;
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}
