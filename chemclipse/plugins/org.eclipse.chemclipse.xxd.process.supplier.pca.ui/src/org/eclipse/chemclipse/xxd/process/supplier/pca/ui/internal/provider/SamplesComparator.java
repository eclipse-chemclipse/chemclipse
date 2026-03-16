/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - prediction, alphnumeric sort
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.AlphanumericStringComparator;
import org.eclipse.jface.viewers.Viewer;

public class SamplesComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		AlphanumericStringComparator stringComp = new AlphanumericStringComparator();
		if(e1 instanceof ISample sample1 && e2 instanceof ISample sample2) {

			switch(getPropertyIndex()) {
				case 0:
					String name1 = sample1.getSampleName() != null ? sample1.getSampleName() : "";
					String name2 = sample2.getSampleName() != null ? sample2.getSampleName() : "";
					sortOrder = stringComp.compare(name1, name2);
					break;
				case 1:
					String detail1 = sample1.getSampleDetails() != null ? sample1.getSampleDetails() : "";
					String detail2 = sample2.getSampleDetails() != null ? sample2.getSampleDetails() : "";
					sortOrder = stringComp.compare(detail1, detail2);
					break;
				case 2:
					sortOrder = Boolean.compare(sample2.isSelected(), sample1.isSelected());
					break;
				case 3:
					sortOrder = Boolean.compare(sample2.isPredicted(), sample1.isPredicted());
					break;
				case 4: // Color is defined by the group name
				case 5:

					String groupName1 = sample1.getGroupName() != null ? sample1.getGroupName() : "";
					String groupName2 = sample2.getGroupName() != null ? sample2.getGroupName() : "";
					sortOrder = stringComp.compare(groupName1, groupName2);
					break;
				case 6:
					String classification1 = sample1.getClassification() != null ? sample1.getClassification() : "";
					String classification2 = sample2.getClassification() != null ? sample2.getClassification() : "";
					sortOrder = stringComp.compare(classification1, classification2);
					break;
				case 7:
					String description1 = sample1.getDescription() != null ? sample1.getDescription() : "";
					String description2 = sample2.getDescription() != null ? sample2.getDescription() : "";
					sortOrder = stringComp.compare(description1, description2);
					break;
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}
