/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.internal.provider;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.model.IndexNameMarker;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class IndexAssignerTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IndexNameMarker marker1 && e2 instanceof IndexNameMarker marker2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Float.compare(marker2.getRetentionIndex(), marker1.getRetentionIndex());
					break;
				case 1:
					sortOrder = marker2.getName().compareTo(marker1.getName());
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