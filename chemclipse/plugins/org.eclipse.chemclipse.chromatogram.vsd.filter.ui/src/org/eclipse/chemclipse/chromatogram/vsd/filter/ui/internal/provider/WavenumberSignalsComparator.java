/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.vsd.filter.ui.internal.provider;

import org.eclipse.chemclipse.chromatogram.vsd.filter.model.WavenumberSignal;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class WavenumberSignalsComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof WavenumberSignal signal1 && e2 instanceof WavenumberSignal signal2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Double.compare(signal2.getWavenumber(), signal1.getWavenumber());
					break;
				case 1:
					sortOrder = Double.compare(signal2.getIntensity(), signal1.getIntensity());
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