/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.locations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.locations.UserLocation;
import org.eclipse.chemclipse.model.locations.UserLocations;
import org.eclipse.chemclipse.processing.supplier.IProcessorPreferences;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.ui.events.IKeyEventProcessor;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.swt.ui.components.ISearchListener;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.ux.extension.ui.methods.SettingsUIProvider;
import org.eclipse.chemclipse.ux.extension.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class UserLocationsSettingsEditor implements SettingsUIProvider.SettingsUIControl, IExtendedPartUI {

	private static final String MESSAGE_REMOVE = "Do you want to delete the selected user locations?";
	private static final String CATEGORY = "User Locations";
	private static final String DELETE = "Delete";
	//
	private AtomicReference<Button> buttonToolbarSearch = new AtomicReference<>();
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private AtomicReference<UserLocationsListUI> listControl = new AtomicReference<>();
	//
	private List<Listener> listeners = new ArrayList<>();
	private List<Button> buttons = new ArrayList<>();
	//
	private Composite control;
	private IProcessorPreferences<UserLocations> preferences = null;
	private UserLocations settings = new UserLocations();

	public UserLocationsSettingsEditor(Composite parent, IProcessorPreferences<UserLocations> preferences, UserLocations userLocations) {

		/*
		 * Populate the settings on demand.
		 */
		this.preferences = preferences;
		if(userLocations != null) {
			this.settings.load(userLocations.save());
		}
		//
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);
		//
		createToolbarMain(composite);
		createToolbarSearch(composite);
		createTableSection(composite);
		//
		setTableViewerInput();
		initialize();
		setControl(composite);
	}

	@Override
	public void setEnabled(boolean enabled) {

		listControl.get().getControl().setEnabled(enabled);
		for(Button button : buttons) {
			button.setEnabled(enabled);
		}
		toolbarSearch.get().setEnabled(enabled);
	}

	@Override
	public IStatus validate() {

		return ValidationStatus.ok();
	}

	@Override
	public String getSettings() throws IOException {

		if(preferences != null) {
			UserLocations settingz = new UserLocations();
			settingz.load(settings.save());
			return preferences.getSerialization().toString(settingz);
		}
		return "";
	}

	@Override
	public void addChangeListener(Listener listener) {

		listeners.add(listener);
	}

	@Override
	public Control getControl() {

		return control;
	}

	@Override
	public void restoreDefaults() {

		settings.clear();
	}

	public void load(String entries) {

		settings.load(entries);
		setTableViewerInput();
	}

	public String getValues() {

		return settings.save();
	}

	private void initialize() {

		enableToolbar(toolbarSearch, buttonToolbarSearch.get(), IMAGE_SEARCH, TOOLTIP_SEARCH, false);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(7, false));
		//
		createButtonToggleToolbar(composite);
		add(createButtonAdd(composite));
		add(createButtonEdit(composite));
		add(createButtonRemove(composite));
		add(createButtonRemoveAll(composite));
		add(createButtonImport(composite));
		add(createButtonExport(composite));
	}

	private void createButtonToggleToolbar(Composite parent) {

		buttonToolbarSearch.set(createButtonToggleToolbar(parent, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH));
	}

	private void add(Button button) {

		buttons.add(button);
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

		UserLocationsListUI userLocationsListUI = new UserLocationsListUI(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = userLocationsListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		userLocationsListUI.setEditEnabled(true);
		//
		Shell shell = table.getShell();
		ITableSettings tableSettings = userLocationsListUI.getTableSettings();
		addDeleteMenuEntry(shell, tableSettings);
		addKeyEventProcessors(shell, tableSettings);
		userLocationsListUI.applySettings(tableSettings);
		//
		listControl.set(userLocationsListUI);
	}

	private Button createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Add a user location.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				InputDialog dialog = new InputDialog(e.display.getActiveShell(), "User Location", "Create a new user location.", "Project | path", new UserLocationInputValidator(settings.keySet()));
				if(IDialogConstants.OK_ID == dialog.open()) {
					String item = dialog.getValue();
					UserLocation userLocation = settings.extractUserLocation(item);
					if(userLocation != null) {
						settings.add(userLocation);
						setTableViewerInput();
					}
				}
			}
		});
		//
		return button;
	}

	private Button createButtonEdit(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Edit the selected user location.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = listControl.get().getStructuredSelection().getFirstElement();
				if(object instanceof UserLocation userLocation) {
					Set<String> keySetEdit = new HashSet<>();
					keySetEdit.addAll(settings.keySet());
					keySetEdit.remove(userLocation.getName());
					InputDialog dialog = new InputDialog(e.display.getActiveShell(), "User Location", "Edit the selected user location.", settings.extractUserLocation(userLocation), new UserLocationInputValidator(keySetEdit));
					if(IDialogConstants.OK_ID == dialog.open()) {
						String item = dialog.getValue();
						UserLocation userLocationNew = settings.extractUserLocation(item);
						if(userLocationNew != null) {
							settings.remove(userLocation);
							settings.add(userLocationNew);
							setTableViewerInput();
						}
					}
				}
			}
		});
		//
		return button;
	}

	private Button createButtonRemove(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Remove the selected user locations.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openQuestion(e.display.getActiveShell(), "User Locations", "Do you want to delete the selected user locations?")) {
					for(Object object : listControl.get().getStructuredSelection().toArray()) {
						if(object instanceof UserLocation userLocation) {
							settings.remove(userLocation.getName());
						}
					}
					setTableViewerInput();
				}
			}
		});
		//
		return button;
	}

	private Button createButtonRemoveAll(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Remove all user locations.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE_ALL, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openQuestion(e.display.getActiveShell(), "User Locations", "Do you want to delete all user locations?")) {
					settings.clear();
					setTableViewerInput();
				}
			}
		});
		//
		return button;
	}

	private Button createButtonImport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Import user locations.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IMPORT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.READ_ONLY);
				fileDialog.setText("User Locations");
				fileDialog.setFilterExtensions(new String[]{UserLocations.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{UserLocations.FILTER_NAME});
				fileDialog.setFilterPath(PreferenceSupplier.getUserLocationsTemplateFolder());
				String pathname = fileDialog.open();
				if(pathname != null) {
					PreferenceSupplier.setUserLocationsTemplateFolder(fileDialog.getFilterPath());
					File file = new File(pathname);
					settings.importItems(file);
					setTableViewerInput();
				}
			}
		});
		//
		return button;
	}

	private Button createButtonExport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Export the user locations.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXPORT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
				fileDialog.setOverwrite(true);
				fileDialog.setText("User Locations");
				fileDialog.setFilterExtensions(new String[]{UserLocations.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{UserLocations.FILTER_NAME});
				fileDialog.setFileName(UserLocations.FILE_NAME);
				fileDialog.setFilterPath(PreferenceSupplier.getUserLocationsTemplateFolder());
				String pathname = fileDialog.open();
				if(pathname != null) {
					PreferenceSupplier.setUserLocationsTemplateFolder(fileDialog.getFilterPath());
					File file = new File(pathname);
					if(settings.exportItems(file)) {
						MessageDialog.openInformation(button.getShell(), "User Location List", "The user locations have been exported successfully.");
					} else {
						MessageDialog.openWarning(button.getShell(), "User Location List", "Something went wrong to export the user locations.");
					}
				}
			}
		});
		//
		return button;
	}

	private void setTableViewerInput() {

		listControl.get().setInput(settings.values());
	}

	private void addDeleteMenuEntry(Shell shell, ITableSettings tableSettings) {

		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return DELETE;
			}

			@Override
			public String getCategory() {

				return CATEGORY;
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				deleteItems(shell);
			}
		});
	}

	private void addKeyEventProcessors(Shell shell, ITableSettings tableSettings) {

		tableSettings.addKeyEventProcessor(new IKeyEventProcessor() {

			@Override
			public void handleEvent(ExtendedTableViewer extendedTableViewer, KeyEvent e) {

				if(e.keyCode == SWT.DEL) {
					deleteItems(shell);
				}
			}
		});
	}

	private void deleteItems(Shell shell) {

		if(MessageDialog.openQuestion(shell, UserLocations.DESCRIPTION, MESSAGE_REMOVE)) {
			for(Object object : listControl.get().getStructuredSelection().toArray()) {
				if(object instanceof UserLocation userLocation) {
					settings.remove(userLocation);
				}
			}
			setTableViewerInput();
		}
	}

	private void setControl(Composite composite) {

		this.control = composite;
	}
}