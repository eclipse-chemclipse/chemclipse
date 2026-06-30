/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.ui.internal.provider;

import org.eclipse.chemclipse.model.identifier.IFlavorMarker;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class FlavorMarkerListFilter extends ViewerFilter {

	private String searchText;
	private boolean caseSensitive;

	public void setSearchText(String searchText, boolean caseSensitive) {

		this.searchText = searchText;
		this.caseSensitive = caseSensitive;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if(searchText == null || searchText.equals("")) {
			return true;
		}

		if(element instanceof IFlavorMarker marker) {
			searchText = caseSensitive ? searchText : searchText.toLowerCase();
			if(contains(searchText, marker.getOdor())) {
				return true;
			}

			if(contains(searchText, marker.getMatrix())) {
				return true;
			}

			if(contains(searchText, marker.getSolvent())) {
				return true;
			}
		}

		return false;
	}

	private boolean contains(String searchText, String value) {

		value = caseSensitive ? value : value.toLowerCase();
		return value.contains(searchText);
	}
}