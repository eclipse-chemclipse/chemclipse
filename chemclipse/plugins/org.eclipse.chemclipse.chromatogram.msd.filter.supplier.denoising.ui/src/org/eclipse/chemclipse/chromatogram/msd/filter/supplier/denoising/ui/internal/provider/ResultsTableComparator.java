/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.ui.internal.provider;

import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class ResultsTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof ICombinedMassSpectrum combinedMassSpectrum1 && e2 instanceof ICombinedMassSpectrum combinedMassSpectrum2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Integer.compare(combinedMassSpectrum2.getStartScan(), combinedMassSpectrum1.getStartScan());
					break;
				case 1:
					sortOrder = Integer.compare(combinedMassSpectrum2.getStopScan(), combinedMassSpectrum1.getStopScan());
					break;
				case 2:
					sortOrder = Integer.compare(combinedMassSpectrum2.getStartRetentionTime(), combinedMassSpectrum1.getStartRetentionTime());
					break;
				case 3:
					sortOrder = Integer.compare(combinedMassSpectrum2.getStopRetentionTime(), combinedMassSpectrum1.getStopRetentionTime());
					break;
				case 4:
					sortOrder = Float.compare(combinedMassSpectrum2.getStartRetentionIndex(), combinedMassSpectrum1.getStartRetentionIndex());
					break;
				case 5:
					sortOrder = Float.compare(combinedMassSpectrum2.getStopRetentionIndex(), combinedMassSpectrum1.getStopRetentionIndex());
					break;
			}
			if(getDirection() == ASCENDING) {
				sortOrder = -sortOrder;
			}
		}
		return sortOrder;
	}
}
