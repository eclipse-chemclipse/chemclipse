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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import org.eclipse.chemclipse.model.core.IIntegrationEntry;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class IntegrationAreaTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IIntegrationEntry integrationEntry1 && e2 instanceof IIntegrationEntry integrationEntry2) {
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Double.compare(integrationEntry2.getIntegratedArea(), integrationEntry1.getIntegratedArea());
					break;
				case 1:
					sortOrder = Double.compare(integrationEntry2.getSignal(), integrationEntry1.getSignal());
					break;
				case 2:
					sortOrder = integrationEntry2.getIntegrationType().compareTo(integrationEntry1.getIntegrationType());
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