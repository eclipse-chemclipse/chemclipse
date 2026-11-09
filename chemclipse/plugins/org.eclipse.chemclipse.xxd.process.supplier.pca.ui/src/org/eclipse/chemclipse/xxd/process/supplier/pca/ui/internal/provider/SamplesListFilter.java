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

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class SamplesListFilter extends ViewerFilter {

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

		if(element instanceof ISample sample) {
			String name = sample.getSampleName();
			String groupName = (sample.getGroupName() != null) ? sample.getGroupName() : ""; //$NON-NLS-1$
			String classification = (sample.getClassification() != null) ? sample.getClassification() : ""; //$NON-NLS-1$
			String description = (sample.getDescription() != null) ? sample.getDescription() : ""; //$NON-NLS-1$

			if(!caseSensitive) {
				searchText = searchText.toLowerCase();
				name = name.toLowerCase();
				groupName = groupName.toLowerCase();
				classification = classification.toLowerCase();
				description = description.toLowerCase();
			}

			if(name.contains(searchText)) {
				return true;
			}

			if(groupName.contains(searchText)) {
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
