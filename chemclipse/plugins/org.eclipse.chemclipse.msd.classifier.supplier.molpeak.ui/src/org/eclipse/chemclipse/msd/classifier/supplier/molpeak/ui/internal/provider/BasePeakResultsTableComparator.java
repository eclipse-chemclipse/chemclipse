/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Dr. Lorenz Gerber - initial API and implementation
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.classifier.supplier.molpeak.ui.internal.provider;

import java.util.Map;

import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class BasePeakResultsTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof Map.Entry<?, ?> ligninEntry1 && e2 instanceof Map.Entry<?, ?> ligninEntry2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = ((String)ligninEntry2.getKey()).compareTo((String)ligninEntry1.getKey());
					break;
				case 1:
					sortOrder = Double.compare((double)ligninEntry2.getValue(), (double)ligninEntry1.getValue());
					break;
			}
			if(getDirection() == ASCENDING) {
				sortOrder = -sortOrder;
			}
		}
		return sortOrder;
	}
}
