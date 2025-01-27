/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.identifier.IFlavorMarker;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.library.LibrarySearchSettings;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.support.ui.wizards.AbstractExtendedWizardPage;
import org.eclipse.chemclipse.swt.ui.components.ISearchListener;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedColumnIndicesUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedFlavorMarkerUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedLiteratureUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedMoleculeUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedSynonymsUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.TargetsListUI;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class PageLibrarySearch extends AbstractExtendedWizardPage implements IExtendedPartUI {

	private AtomicReference<Button> buttonToolbarInfoControl = new AtomicReference<>();
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarSearchControl = new AtomicReference<>();
	private AtomicReference<TargetsListUI> targetListControl = new AtomicReference<>();
	private AtomicReference<ExtendedFlavorMarkerUI> flavorMarkerControl = new AtomicReference<>();
	private AtomicReference<ExtendedColumnIndicesUI> columnIndicesControl = new AtomicReference<>();
	private AtomicReference<ExtendedSynonymsUI> synonymsControl = new AtomicReference<>();
	private AtomicReference<ExtendedMoleculeUI> moleculeControl = new AtomicReference<>();
	private AtomicReference<ExtendedLiteratureUI> literatureControl = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfoTop = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfoBottom = new AtomicReference<>();
	//
	private LibrarySearchSettings librarySearchSettings;
	private Button buttonDefault;

	public PageLibrarySearch(LibrarySearchSettings librarySearchSettings) {

		super(PageLibrarySearch.class.getName());
		//
		this.librarySearchSettings = librarySearchSettings;
		//
		setTitle("Library Search Review");
		setMessage("Review the retrieved library entries.");
	}

	@Override
	public boolean canFinish() {

		return true;
	}

	@Override
	public void setDefaultValues() {

	}

	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		//
		createToolbarMain(composite);
		createToolbarInfoTop(composite);
		createToolbarSearch(composite);
		createDataSection(composite);
		createToolbarInfoBottom(composite);
		//
		initialize();
		setControl(composite);
	}

	private void initialize() {

		/*
		 * Default button of the wizard.
		 */
		buttonDefault = getShell().getDefaultButton();
		//
		enableToolbar(toolbarInfoTop, buttonToolbarInfoControl.get(), IApplicationImage.IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarInfoBottom, buttonToolbarInfoControl.get(), IApplicationImage.IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarSearch, buttonToolbarSearchControl.get(), IMAGE_SEARCH, TOOLTIP_SEARCH, false);
		//
		TargetsListUI targetsListUI = targetListControl.get();
		targetsListUI.setInput(librarySearchSettings.getIdentificationTargets());
		targetListControl.get().updateSourceRange(librarySearchSettings.getRetentionTime(), librarySearchSettings.getRetentionIndex());
		//
		updateInfoToolbars();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));
		//
		createButtonToggleInfo(composite);
		createButtonToggleSearch(composite);
	}

	private void createButtonToggleInfo(Composite parent) {

		Button button = createButtonToggleToolbar(parent, Arrays.asList(toolbarInfoTop, toolbarInfoBottom), IMAGE_INFO, TOOLTIP_INFO);
		buttonToolbarInfoControl.set(button);
	}

	private void createButtonToggleSearch(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Button button;
				if(toolbarSearch.get().isVisible()) {
					button = null;
				} else {
					button = buttonDefault;
				}
				//
				Shell shell = getShell();
				shell.getDisplay().asyncExec(() -> shell.setDefaultButton(button));
			}
		});
		//
		buttonToolbarSearchControl.set(button);
	}

	private void createToolbarSearch(Composite parent) {

		SearchSupportUI searchSupportUI = new SearchSupportUI(parent, SWT.NONE);
		searchSupportUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		searchSupportUI.setSearchListener(new ISearchListener() {

			@Override
			public void performSearch(String searchText, boolean caseSensitive) {

				targetListControl.get().setSearchText(searchText, caseSensitive);
				updateInfoToolbarBottom();
			}
		});
		//
		toolbarSearch.set(searchSupportUI);
	}

	private void createDataSection(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		SashForm sashForm = new SashForm(composite, SWT.VERTICAL);
		createTargetList(sashForm);
		createDetailsSection(sashForm);
		sashForm.setWeights(new int[]{1, 1});
	}

	private void createToolbarInfoTop(Composite parent) {

		InformationUI informationUI = new InformationUI(parent, SWT.NONE);
		informationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		toolbarInfoTop.set(informationUI);
	}

	private void createToolbarInfoBottom(Composite parent) {

		InformationUI informationUI = new InformationUI(parent, SWT.NONE);
		informationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		toolbarInfoBottom.set(informationUI);
	}

	private void createTargetList(Composite parent) {

		TargetsListUI targetListUI = new TargetsListUI(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		targetListUI.setComparator(true);
		Table table = targetListUI.getTable();
		//
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				toggleManuallyVerfied(getIdentificationTarget());
			}

			@Override
			public void mouseDown(MouseEvent e) {

				toggleManuallyVerfied(getSelectedCell(table, e));
			}
		});
		//
		targetListUI.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IIdentificationTarget identificationTarget = getIdentificationTarget();
				ILibraryInformation libraryInformation = (identificationTarget != null) ? identificationTarget.getLibraryInformation() : null;
				flavorMarkerControl.get().setInput(libraryInformation);
				columnIndicesControl.get().setInput(libraryInformation);
				synonymsControl.get().setInput(libraryInformation);
				moleculeControl.get().setInput(libraryInformation);
				updateLiterature(libraryInformation);
			}
		});
		//
		targetListControl.set(targetListUI);
	}

	private void updateLiterature(ILibraryInformation libraryInformation) {

		Set<String> literature = new HashSet<>();
		if(libraryInformation != null) {
			List<IFlavorMarker> flavorMarkers = libraryInformation.getFlavorMarkers();
			for(IFlavorMarker flavorMarker : flavorMarkers) {
				literature.add(flavorMarker.getLiteratureReference());
			}
		}
		//
		literatureControl.get().setInput(new ArrayList<>(literature));
	}

	private IIdentificationTarget getSelectedCell(Table table, MouseEvent event) {

		Rectangle clientArea = table.getClientArea();
		Point point = new Point(event.x, event.y);
		int index = table.getTopIndex();
		while(index < table.getItemCount()) {
			boolean visible = false;
			TableItem item = table.getItem(index);
			Rectangle rectangle = item.getBounds(0);
			if(rectangle.contains(point)) {
				TableItem[] tableItems = table.getSelection();
				TableItem tableItem = tableItems[0];
				Object object = tableItem.getData();
				if(object instanceof IIdentificationTarget identificationTarget) {
					return identificationTarget;
				}
			}
			//
			if(!visible && rectangle.intersects(clientArea)) {
				visible = true;
			}
			//
			if(!visible) {
				return null;
			}
			index++;
		}
		//
		return null;
	}

	private void toggleManuallyVerfied(IIdentificationTarget identificationTarget) {

		if(identificationTarget != null) {
			boolean manuallyVerified = !identificationTarget.isVerified();
			identificationTarget.setVerified(manuallyVerified);
			/*
			 * Also select/deselect the flavor marker.
			 */
			ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
			for(IFlavorMarker flavorMarker : libraryInformation.getFlavorMarkers()) {
				flavorMarker.setManuallyVerified(manuallyVerified);
			}
			//
			targetListControl.get().refresh();
			flavorMarkerControl.get().updateInput();
		}
	}

	private IIdentificationTarget getIdentificationTarget() {

		Object object = targetListControl.get().getStructuredSelection().getFirstElement();
		if(object instanceof IIdentificationTarget identificationTarget) {
			return identificationTarget;
		}
		//
		return null;
	}

	private void createDetailsSection(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		//
		SashForm sashForm = new SashForm(composite, SWT.HORIZONTAL);
		createFlavorMarkerSection(sashForm);
		createColumnSection(sashForm);
		createMoleculeSection(sashForm);
		createLiteratureSection(sashForm);
		sashForm.setWeights(new int[]{1, 1, 1, 1});
	}

	private void createFlavorMarkerSection(Composite parent) {

		ExtendedFlavorMarkerUI extendedFlavorMarkerUI = new ExtendedFlavorMarkerUI(parent, SWT.BORDER);
		//
		flavorMarkerControl.set(extendedFlavorMarkerUI);
	}

	private void createColumnSection(Composite parent) {

		TabFolder tabFolder = new TabFolder(parent, SWT.BOTTOM);
		//
		createColumnIndicesSection(tabFolder);
		createSynonymsSection(tabFolder);
	}

	private void createColumnIndicesSection(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Column Indices");
		//
		ExtendedColumnIndicesUI extendedColumnIndicesUI = new ExtendedColumnIndicesUI(tabFolder, SWT.BORDER);
		tabItem.setControl(extendedColumnIndicesUI);
		//
		columnIndicesControl.set(extendedColumnIndicesUI);
	}

	private void createSynonymsSection(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Synonyms");
		//
		ExtendedSynonymsUI extendedSynonymsUI = new ExtendedSynonymsUI(tabFolder, SWT.BORDER);
		tabItem.setControl(extendedSynonymsUI);
		//
		synonymsControl.set(extendedSynonymsUI);
	}

	private void createMoleculeSection(Composite parent) {

		moleculeControl.set(new ExtendedMoleculeUI(parent, SWT.BORDER));
	}

	private void createLiteratureSection(Composite parent) {

		literatureControl.set(new ExtendedLiteratureUI(parent, SWT.BORDER));
	}

	private void updateInfoToolbars() {

		updateInfoToolbarTop();
		updateInfoToolbarBottom();
	}

	private void updateInfoToolbarTop() {

		StringBuilder builder = new StringBuilder();
		builder.append("Column: ");
		builder.append(librarySearchSettings.getSearchColumn());
		builder.append(" | ");
		builder.append("Retention Time: ");
		builder.append(ValueFormat.getDecimalFormatEnglish("0.000").format(librarySearchSettings.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
		builder.append(" | ");
		builder.append("Retention Index: ");
		builder.append(librarySearchSettings.getRetentionIndex());
		//
		toolbarInfoTop.get().setText(builder.toString());
	}

	private void updateInfoToolbarBottom() {

		StringBuilder builder = new StringBuilder();
		builder.append("Results: ");
		builder.append(librarySearchSettings.getIdentificationTargets().size());
		builder.append(" | ");
		builder.append("Filtered: ");
		builder.append(targetListControl.get().getTable().getItemCount());
		//
		toolbarInfoBottom.get().setText(builder.toString());
	}
}