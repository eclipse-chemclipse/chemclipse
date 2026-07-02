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

import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.fsd.model.core.IScanSignalFSD;
import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.vsd.model.core.ISignalVSD;
import org.eclipse.chemclipse.wsd.model.core.IScanSignalWSD;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ScanSignalListFilter extends ViewerFilter {

	private String searchText;
	private boolean caseSensitive;

	public ScanSignalListFilter() {

	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		this.searchText = searchText;
		this.caseSensitive = caseSensitive;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		/*
		 * Pre-Condition
		 */
		if(searchText == null || searchText.equals("")) {
			return true;
		}
		/*
		 * Not needed at the moment.
		 */
		if(!caseSensitive) {
			searchText = searchText.toLowerCase();
		}

		if(element instanceof IIonMSn ion) {
			IIonTransition ionTransition = ion.getIonTransition();
			if(ionTransition != null) {
				if(Double.toString(ionTransition.getQ3Ion()).contains(searchText)) {
					return true;
				} else if(Integer.toString(ionTransition.getQ1Ion()).contains(searchText)) {
					return true;
				}
			} else if(Double.toString(ion.getIon()).contains(searchText)) {
				return true;
			}
		} else if(element instanceof IScanCSD scanCSD) {
			if(Float.toString(scanCSD.getTotalSignal()).contains(searchText)) {
				return true;
			}
		} else if(element instanceof IScanSignalWSD scanSignalWSD) {
			if(Double.toString(scanSignalWSD.getWavelength()).contains(searchText)) {
				return true;
			}
		} else if(element instanceof ISignalVSD scanSignalVSD) {
			if(Double.toString(scanSignalVSD.getWavenumber()).contains(searchText)) {
				return true;
			}
		} else if(element instanceof IScanSignalFSD scanSignalFSD) {
			if(Double.toString(scanSignalFSD.getWavelength()).contains(searchText)) {
				return true;
			}
		}

		return false;
	}
}
