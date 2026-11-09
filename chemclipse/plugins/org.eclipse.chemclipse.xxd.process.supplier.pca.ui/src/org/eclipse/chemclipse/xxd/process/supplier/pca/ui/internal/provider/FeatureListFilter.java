/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider;

import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class FeatureListFilter extends ViewerFilter {

	private String searchText;
	private boolean caseSensitive;

	public void setSearchText(String searchText, boolean caseSensitive) {

		this.searchText = searchText;
		this.caseSensitive = caseSensitive;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if(searchText == null || searchText.equals("")) { //$NON-NLS-1$
			return true;
		}

		if(element instanceof Feature feature) {
			IVariable variable = feature.getVariable();

			String value = variable.getValue();
			String classification = variable.getClassification() == null ? "" : variable.getClassification(); //$NON-NLS-1$
			String description = variable.getDescription() == null ? "" : variable.getDescription(); //$NON-NLS-1$

			if(!caseSensitive) {
				searchText = searchText.toLowerCase();
				value = value.toLowerCase();
				classification = classification.toLowerCase();
				description = description.toLowerCase();
			}

			if(value.contains(searchText)) {
				return true;
			}

			if(classification.contains(searchText)) {
				return true;
			}

			if(description.contains(searchText)) {
				return true;
			}
		}

		return false;
	}
}
