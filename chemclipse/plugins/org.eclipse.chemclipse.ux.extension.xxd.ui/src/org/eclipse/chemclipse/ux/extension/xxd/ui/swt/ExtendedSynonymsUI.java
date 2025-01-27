/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.swt.ui.components.ISearchListener;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.ISettingsHandler;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePage;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

public class ExtendedSynonymsUI extends LibraryInformationComposite implements IExtendedPartUI {

	private Button buttonToolbarSearch;
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private AtomicReference<SynonymsListUI> listControl = new AtomicReference<>();

	public ExtendedSynonymsUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	public void updateInput() {

		ILibraryInformation libraryInformation = getLibraryInformation();
		if(libraryInformation != null) {
			listControl.get().setInput(libraryInformation.getSynonyms());
		} else {
			listControl.get().clear();
		}
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		setLayout(gridLayout);
		//
		createToolbarMain(this);
		createToolbarInfo(this);
		createToolbarSearch(this);
		createTableSection(this);
		//
		initialize();
	}

	private void initialize() {

		initializeToolbarInfo();
		enableToolbar(toolbarSearch, buttonToolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH, false);
		//
		applySettings();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(3, false));
		//
		createButtonToolbarInfo(composite);
		buttonToolbarSearch = createButtonToggleToolbar(composite, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH);
		createButtonSettings(composite);
	}

	private void createToolbarSearch(Composite parent) {

		SearchSupportUI searchSupportUI = new SearchSupportUI(parent, SWT.NONE);
		searchSupportUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		searchSupportUI.setSearchListener(new ISearchListener() {

			@Override
			public void performSearch(String searchText, boolean caseSensitive) {

				listControl.get().setSearchText(searchText, caseSensitive);
			}
		});
		//
		toolbarSearch.set(searchSupportUI);
	}

	private void createTableSection(Composite parent) {

		SynonymsListUI synonymsListUI = new SynonymsListUI(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		Table table = synonymsListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		ITableSettings tableSettings = synonymsListUI.getTableSettings();
		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return "Swap Synonym <-> Name";
			}

			@Override
			public String getCategory() {

				return "Targets";
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				ILibraryInformation libraryInformation = getLibraryInformation();
				if(libraryInformation != null) {
					Object object = extendedTableViewer.getStructuredSelection().getFirstElement();
					if(object instanceof String synonym) {
						/*
						 * Exchange synonym and name
						 */
						String name = libraryInformation.getName();
						libraryInformation.setName(synonym);
						libraryInformation.getSynonyms().remove(synonym);
						libraryInformation.getSynonyms().add(name);
						/*
						 * Update
						 */
						updateInput();
						extendedTableViewer.setSelection(new StructuredSelection(name));
						UpdateNotifierUI.updateParts(extendedTableViewer.getControl().getDisplay());
					}
				}
			}
		});
		//
		listControl.set(synonymsListUI);
	}

	private void createButtonSettings(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePage.class), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				applySettings();
			}
		});
	}

	private void applySettings() {

		updateInput();
	}
}