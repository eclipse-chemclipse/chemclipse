/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.internal.provider;

import org.eclipse.chemclipse.model.locations.UserLocation;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.support.ui.swt.IRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class UserLocationsComparator extends AbstractRecordTableComparator implements IRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof UserLocation userLocation1 && e2 instanceof UserLocation userLocation2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = userLocation2.getName().compareTo(userLocation1.getName());
					break;
				case 1:
					sortOrder = userLocation2.getPath().compareTo(userLocation1.getPath());
					break;
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