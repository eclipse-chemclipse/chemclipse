/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.ui.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.ui.internal.provider.LibraryInformationComparator;
import org.eclipse.chemclipse.model.ui.internal.provider.LibraryInformationContentProvider;
import org.eclipse.chemclipse.model.ui.internal.provider.LibraryInformationFilter;
import org.eclipse.chemclipse.model.ui.internal.provider.LibraryInformationLabelProvider;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class LibraryInformationListUI extends ExtendedTableViewer {

	private static final String[] TITLES = LibraryInformationLabelProvider.TITLES;
	private static final int[] BOUNDS = LibraryInformationLabelProvider.BOUNDS;

	private LabelProvider labelProvider = new LibraryInformationLabelProvider();
	private IContentProvider contentProviderNormal = new ListContentProvider();
	private IContentProvider contentProviderVirtual = new LibraryInformationContentProvider(this);
	private ViewerComparator comparator = new LibraryInformationComparator();
	private LibraryInformationFilter listFilter = new LibraryInformationFilter();

	private boolean virtualFlagIsSet = false;
	private List<ILibraryInformation> libraryInformationsFull = null;
	private List<ILibraryInformation> libraryInformationsUse = new ArrayList<>();

	public LibraryInformationListUI(Composite parent, int style) {

		super(parent, style);
		virtualFlagIsSet = ((style & SWT.VIRTUAL) == SWT.VIRTUAL);
		initialize();
	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		/*
		 * Switch virtual or normal to search items.
		 */
		listFilter.setSearchText(searchText, caseSensitive);
		boolean reloadData = (searchText == null || searchText.isBlank());
		if(getComparator() == null) {
			/*
			 * Virtual Search
			 */
			updateLibraryInformationUseList(reloadData);
		} else {
			if(reloadData) {
				updateLibraryInformationUseList(reloadData);
			} else {
				refresh();
			}
		}
	}

	public void setInput(List<ILibraryInformation> libraryInformations) {

		libraryInformationsFull = libraryInformations;
		libraryInformationsUse.clear();
		if(libraryInformations != null) {
			libraryInformationsUse.addAll(libraryInformations);
		}
		setInput();
	}

	private void updateLibraryInformationUseList(boolean reloadData) {

		libraryInformationsUse.clear();
		if(libraryInformationsFull != null) {
			if(reloadData) {
				libraryInformationsUse.addAll(libraryInformationsFull);
			} else {
				for(ILibraryInformation libraryInformation : libraryInformationsFull) {
					if(listFilter.isMatch(libraryInformation)) {
						libraryInformationsUse.add(libraryInformation);
					}
				}
			}
		}
		/*
		 * Update the list.
		 */
		setInput();
	}

	private void setInput() {

		/*
		 * Reset Content Provider
		 */
		setComparator(null);
		setContentProvider(contentProviderNormal);
		super.setInput(null);
		setUseHashlookup(false);
		/*
		 * Normal or Virtual
		 */
		int size = libraryInformationsUse.size();
		if(virtualFlagIsSet && (size > 1000)) {
			setUseHashlookup(true);
			setContentProvider(contentProviderVirtual);
		} else {
			setComparator(comparator);
		}
		/*
		 * Input
		 */
		super.setInput(libraryInformationsUse);
		setItemCount(size);
	}

	private void initialize() {

		createColumns(TITLES, BOUNDS);
		setLabelProvider(labelProvider);
		setContentProvider(contentProviderNormal);
		setComparator(comparator);
		setUseHashlookup(true);
		setFilters(listFilter);
	}
}
