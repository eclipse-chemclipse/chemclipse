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
package org.eclipse.chemclipse.msd.model.core.support;

import java.util.Comparator;

import org.eclipse.chemclipse.support.comparator.SortOrder;

public class MarkedIonTransitionComparator implements Comparator<IMarkedIonTransition> {

	private SortOrder sortOrder = SortOrder.ASC;

	public MarkedIonTransitionComparator(SortOrder sortOrder) {

		if(sortOrder != null) {
			this.sortOrder = sortOrder;
		}
	}

	@Override
	public int compare(IMarkedIonTransition transition1, IMarkedIonTransition transition2) {

		int result = 0;
		if(transition1 == null || transition2 == null) {
			return 0;
		}
		if(transition1.getIonTransition() == transition2.getIonTransition()) {
			return 0;
		}
		switch(sortOrder) {
			case ASC:
				result = Double.compare(transition2.getIonTransition().getQ1StartIon(), transition1.getIonTransition().getQ1StartIon());
				break;
			case DESC:
				result = Double.compare(transition1.getIonTransition().getQ1StartIon(), transition2.getIonTransition().getQ1StartIon());
				break;
			default:
				result = Double.compare(transition2.getIonTransition().getQ1StartIon(), transition1.getIonTransition().getQ1StartIon());
		}
		return result;
	}
}
