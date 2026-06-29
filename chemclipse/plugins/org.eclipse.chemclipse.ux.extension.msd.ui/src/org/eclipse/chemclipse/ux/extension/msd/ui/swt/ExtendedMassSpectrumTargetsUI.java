/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - adapted for MALDI
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.ITargetSupplier;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.msd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swtchart.extensions.core.IKeyboardSupport;

import jakarta.inject.Inject;

public class ExtendedMassSpectrumTargetsUI extends Composite implements IExtendedPartUI {

	private static final String MENU_CATEGORY_TARGETS = "Targets";

	private AtomicReference<Button> buttonToolbarSearch = new AtomicReference<>();
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private AtomicReference<Button> buttonDeleteTargetsControl = new AtomicReference<>();
	private AtomicReference<MassSpectrumTargetsListUI> targetList = new AtomicReference<>();

	private Object cachedScan = null;

	private IUpdateListener updateListener = null;

	@Inject
	public ExtendedMassSpectrumTargetsUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	public boolean setFocus() {

		updateOnFocus();
		return true;
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public void clear() {

		cachedScan = null;
		updateTargets();
	}

	public void updatePart() {

		targetList.get().refresh();
	}

	public void update(Object object) {

		if(object != null) {
			if(cachedScan != object) {
				this.cachedScan = object;
			}
			updateTargets();
		}
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		setLayout(gridLayout);

		createToolbarMain(this);
		createToolbarSearch(this);
		createTableSection(this);

		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarSearch, buttonToolbarSearch.get(), IMAGE_SEARCH, TOOLTIP_SEARCH, false);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));

		createButtonToggleToolbarSearch(composite);
		createButtonDeleteAll(composite);
	}

	private void createButtonToggleToolbarSearch(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH);
		buttonToolbarSearch.set(button);
	}

	private void createButtonDeleteAll(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Delete all targets");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE_ALL, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteTargetsAll(e.display);
			}
		});

		buttonDeleteTargetsControl.set(button);
	}

	private void createToolbarSearch(Composite parent) {

		SearchSupportUI searchSupportUI = new SearchSupportUI(parent, SWT.NONE);
		searchSupportUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		searchSupportUI.setSearchListener((String searchText, boolean caseSensitive) -> {
			targetList.get().setSearchText(searchText, caseSensitive);
		});

		toolbarSearch.set(searchSupportUI);
	}

	private void createTableSection(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(1, true));
		createTargetTable(composite, targetList);
	}

	private void createTargetTable(Composite parent, AtomicReference<MassSpectrumTargetsListUI> listControl) {

		MassSpectrumTargetsListUI targetListUI = new MassSpectrumTargetsListUI(parent, SWT.BORDER);
		Table table = targetListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateWidgets();
			}
		});

		targetListUI.getControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				propagateTarget(e.display);
			}
		});

		targetListUI.setUpdateListener(this::fireUpdate);
		/*
		 * Sort the table
		 */
		targetListUI.setComparator(true);
		/*
		 * Add the delete targets support.
		 */
		Display display = targetListUI.getTable().getDisplay();
		ITableSettings tableSettings = targetListUI.getTableSettings();
		addDeleteMenuEntry(display, tableSettings);
		addVerifyTargetsMenuEntry(display, tableSettings);
		addUnverifyTargetsMenuEntry(display, tableSettings);
		addKeyEventProcessors(display, tableSettings);
		targetListUI.applySettings(tableSettings);

		listControl.set(targetListUI);
	}

	private void addDeleteMenuEntry(Display display, ITableSettings tableSettings) {

		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return "Delete Targets";
			}

			@Override
			public String getCategory() {

				return MENU_CATEGORY_TARGETS;
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				deleteTargetsSelected(display);
			}
		});
	}

	private void addVerifyTargetsMenuEntry(Display display, ITableSettings tableSettings) {

		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return "Verify Targets Check";
			}

			@Override
			public String getCategory() {

				return MENU_CATEGORY_TARGETS;
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				verifyTargets(true, display);
			}
		});
	}

	private void addUnverifyTargetsMenuEntry(Display display, ITableSettings tableSettings) {

		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return "Verify Targets Uncheck";
			}

			@Override
			public String getCategory() {

				return MENU_CATEGORY_TARGETS;
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				verifyTargets(false, display);
			}
		});
	}

	private void addKeyEventProcessors(Display display, ITableSettings tableSettings) {

		tableSettings.addKeyEventProcessor((ExtendedTableViewer extendedTableViewer, KeyEvent e) -> {
			if(e.keyCode == SWT.DEL) {
				deleteTargetsSelected(display);
			} else if((e.stateMask & SWT.MOD1) == SWT.MOD1) {
				/*
				 * CTRL
				 */
				if(e.keyCode == IKeyboardSupport.KEY_CODE_LC_I) {
					if((e.stateMask & SWT.MOD3) == SWT.MOD3) {
						verifyTargets(false, display); // CTRL + ALT + i
					} else {
						verifyTargets(true, display); // CTRL + i
					}
				} else if(e.keyCode == IKeyboardSupport.KEY_CODE_LC_D) {
					deleteTargetsAll(e.display); // CTRL + d
				}
			} else {
				propagateTarget(display);
			}
		});
	}

	private void verifyTargets(boolean verified, Display display) {

		Iterator<?> iterator = targetList.get().getStructuredSelection().iterator();
		while(iterator.hasNext()) {
			Object object = iterator.next();
			if(object instanceof IIdentificationTarget identificationTarget) {
				identificationTarget.setVerified(verified);
				UpdateNotifierUI.update(display, IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_UPDATE, "Target has been manually verified.");
			}
		}
		updateTargets();
	}

	private void updateWidgets() {

		if(getObject() instanceof ITargetSupplier targetSupplier) {
			buttonDeleteTargetsControl.get().setEnabled(!targetSupplier.getTargets().isEmpty());
		} else {
			buttonDeleteTargetsControl.get().setEnabled(false);
		}
	}

	private void updateTargets() {

		updateWidgets();
		updateInput();

		MassSpectrumTargetsListUI targetListUI = targetList.get();
		targetListUI.sortTable();
		Table table = targetListUI.getTable();
		if(table.getItemCount() > 0) {
			table.setSelection(0);
			propagateTarget(getDisplay());
		}
	}

	private void updateInput() {

		if(getObject() instanceof ITargetSupplier targetSupplier) {
			targetList.get().setInput(targetSupplier.getTargets());
		} else {
			targetList.get().setInput(null);
		}
	}

	private void propagateTarget(Display display) {

		Table table = targetList.get().getTable();
		int index = table.getSelectionIndex();
		if(index >= 0) {
			TableItem tableItem = table.getItem(index);
			Object data = tableItem.getData();
			if(data instanceof IIdentificationTarget identificationTarget) {
				/*
				 * First update the mass spectrum.
				 */
				IScanMSD massSpectrum = getMassSpectrum();
				if(massSpectrum != null) {
					UpdateNotifierUI.update(display, massSpectrum, identificationTarget);
				}
				/*
				 * Update the target and weblink button.
				 */
				UpdateNotifierUI.update(display, identificationTarget);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void deleteTargetsSelected(Display display) {

		if(getObject() instanceof ITargetSupplier targetSupplier) {
			List<IIdentificationTarget> selection = targetList.get().getStructuredSelection().toList();
			targetSupplier.getTargets().removeAll(selection);
			UpdateNotifierUI.update(display, IChemClipseEvents.TOPIC_IDENTIFICATION_TARGETS_UPDATE_SELECTION, "Target deleted.");
			if(targetSupplier instanceof IScan scan) {
				UpdateNotifierUI.update(display, scan);
			}
		}
	}

	private void deleteTargetsAll(Display display) {

		if(getObject() instanceof ITargetSupplier targetSupplier) {
			Set<IIdentificationTarget> targetsToDelete = targetSupplier.getTargets();
			targetSupplier.getTargets().removeAll(targetsToDelete);
			UpdateNotifierUI.update(display, IChemClipseEvents.TOPIC_IDENTIFICATION_TARGETS_UPDATE_SELECTION, "Targets deleted.");
			if(targetSupplier instanceof IScan scan) {
				UpdateNotifierUI.update(display, scan);
			}
		}
	}

	private void updateOnFocus() {

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		List<Object> objects = dataUpdateSupport.getUpdates(getLastTopic(dataUpdateSupport.getTopics()));

		if(!objects.isEmpty()) {
			Object object = objects.get(0);
			update(object);
		}
	}

	private String getLastTopic(List<String> topics) {

		Collections.reverse(topics);
		for(String topic : topics) {
			if(topic.equals(IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION)) {
				return topic;
			}
			if(topic.equals(IChemClipseEvents.TOPIC_IDENTIFICATION_TARGETS_UPDATE_SELECTION)) {
				return topic;
			}
		}

		return "";
	}

	/**
	 * May return null.
	 * 
	 * @return IScanMSD
	 */
	private IScanMSD getMassSpectrum() {

		Object object = getObject();
		if(object instanceof IScanMSD scanMSD) {
			return scanMSD;
		} else {
			return null;
		}
	}

	private Object getObject() {

		return cachedScan;
	}

	private void fireUpdate() {

		if(updateListener != null) {
			updateListener.update();
		}
	}
}
