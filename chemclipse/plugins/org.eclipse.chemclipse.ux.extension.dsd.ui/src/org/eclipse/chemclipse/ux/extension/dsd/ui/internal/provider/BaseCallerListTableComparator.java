/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.dsd.ui.internal.provider;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.ITargetSupplier;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.targets.TargetSupport;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class BaseCallerListTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;

		Object object1 = null;
		Object object2 = null;

		if(e1 instanceof IScan) {
			object1 = e1;
		}

		if(e2 instanceof IScan) {
			object2 = e2;
		}

		if(object1 != null && object2 != null) {
			sortOrder = getSortOrder(object1, object2);
		}

		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}

	private int getSortOrder(Object object1, Object object2) {

		int sortOrder = 0;

		switch(getPropertyIndex()) {
			case 0:
				if(object1 instanceof IScan scan1 && object2 instanceof IScan scan2) {
					sortOrder = Float.compare(scan1.getScanNumber(), scan2.getScanNumber());
					sortOrder = -sortOrder;
				}
				break;
			case 1:
				if(object1 instanceof ITargetSupplier targetSupplier1 && object2 instanceof ITargetSupplier targetSupplier2) {
					IIdentificationTarget target1 = TargetSupport.getBestIdentificationTarget(targetSupplier1);
					IIdentificationTarget target2 = TargetSupport.getBestIdentificationTarget(targetSupplier2);
					float score1 = target1 != null ? target1.getComparisonResult().getRatingSupplier().getScore() : 0;
					float score2 = target2 != null ? target2.getComparisonResult().getRatingSupplier().getScore() : 0;
					sortOrder = Float.compare(score2, score1);
				}
				break;
			case 2:
				if(object1 instanceof ITargetSupplier targetSupplier1 && object2 instanceof ITargetSupplier targetSupplier2) {
					String name1 = TargetSupport.getBestTargetLibraryField(targetSupplier1);
					String name2 = TargetSupport.getBestTargetLibraryField(targetSupplier2);
					sortOrder = org.eclipse.chemclipse.model.preferences.PreferenceSupplier.isSortCaseSensitive() ? name2.compareTo(name1) : name2.compareToIgnoreCase(name1);
				}
				break;
		}

		return sortOrder;
	}
}