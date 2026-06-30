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

import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ColumnIndicesListFilter extends ViewerFilter {

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

		if(element instanceof IColumnIndexMarker marker) {
			searchText = caseSensitive ? searchText : searchText.toLowerCase();
			ISeparationColumn column = marker.getSeparationColumn();
			if(contains(searchText, Float.toString(marker.getRetentionIndex()))) {
				return true;
			}

			if(contains(searchText, column.getName())) {
				return true;
			}

			if(contains(searchText, column.getSeparationColumnType().label())) {
				return true;
			}

			if(contains(searchText, column.getSeparationColumnPackaging().label())) {
				return true;
			}

			if(contains(searchText, column.getCalculationType())) {
				return true;
			}

			if(contains(searchText, column.getLength())) {
				return true;
			}

			if(contains(searchText, column.getDiameter())) {
				return true;
			}

			if(contains(searchText, column.getPhase())) {
				return true;
			}

			if(contains(searchText, column.getThickness())) {
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