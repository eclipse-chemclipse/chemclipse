/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import org.eclipse.chemclipse.model.quantitation.IQuantitationPeak;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.PeakDataSupport;
import org.eclipse.jface.viewers.Viewer;

public class QuantPeaksTableComparator extends AbstractRecordTableComparator {

	private PeakDataSupport peakDataSupport = new PeakDataSupport();

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IQuantitationPeak entry1 && e2 instanceof IQuantitationPeak entry2) {

			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Double.compare(entry2.getConcentration(), entry1.getConcentration());
					break;
				case 1:
					sortOrder = (entry2.getConcentrationUnit().compareTo(entry1.getConcentrationUnit()));
					break;
				case 2:
					sortOrder = peakDataSupport.getType(entry2.getReferencePeak()).compareTo(peakDataSupport.getType(entry1.getReferencePeak()));
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
