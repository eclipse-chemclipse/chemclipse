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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.tsd.model.core.TraceRange1D;
import org.eclipse.jface.viewers.Viewer;

public class TraceRange1DTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof TraceRange1D traceRange1 && e2 instanceof TraceRange1D traceRange2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Integer.compare(traceRange2.getRetentionTimeColumn1Start(), traceRange1.getRetentionTimeColumn1Start());
					break;
				case 1:
					sortOrder = Integer.compare(traceRange2.getRetentionTimeColumn1Stop(), traceRange1.getRetentionTimeColumn1Stop());
					break;
				case 2:
					sortOrder = traceRange2.getName().compareTo(traceRange1.getName());
					break;
				case 3:
					sortOrder = traceRange2.getTraces().compareTo(traceRange1.getTraces());
					break;
			}

			if(getDirection() == ASCENDING) {
				sortOrder = -sortOrder;
			}
		}

		return sortOrder;
	}
}