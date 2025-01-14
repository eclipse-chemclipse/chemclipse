/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.columns.SeparationColumnMapping;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.chemclipse.support.ui.events.IKeyEventProcessor;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.swt.ui.components.ISearchListener;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.methods.IChangeListener;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ColumnMappingListUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.IExtendedPartUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class ColumnMappingEditorUI extends Composite implements IChangeListener, IExtendedPartUI {

	private static final String ADD_TOOLTIP = "Add a new column mapping";
	private static final String REMOVE_TOOLTIP = "Remove the selected column mappings";
	private static final String REMOVE_ALL_TOOLTIP = "Remove All Mappings";
	//
	private static final String IMPORT_TITLE = "Import Mappings";
	private static final String EXPORT_TITLE = "Export Mappings";
	private static final String MESSAGE_ADD = "You can create a new column mapping here.";
	private static final String MESSAGE_REMOVE = "Do you want to delete the selected column mappings?";
	private static final String MESSAGE_REMOVE_ALL = "Do you want to delete all column mappings?";
	private static final String MESSAGE_EXPORT_SUCCESSFUL = "Column mappings have been exported successfully.";
	private static final String MESSAGE_EXPORT_FAILED = "Failed to export the column mappings.";
	//
	private static final String CATEGORY = "Column Mapping";
	private static final String DELETE = "Delete";
	//
	private List<Button> buttons = new ArrayList<>();
	private AtomicReference<Button> buttonAdd = new AtomicReference<>();
	private AtomicReference<Button> buttonRemove = new AtomicReference<>();
	private AtomicReference<Button> buttonRemoveAll = new AtomicReference<>();
	private AtomicReference<Button> buttonImport = new AtomicReference<>();
	private AtomicReference<Button> buttonExport = new AtomicReference<>();
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarSearchControl = new AtomicReference<>();
	private AtomicReference<ColumnMappingListUI> listControl = new AtomicReference<>();
	//
	private SeparationColumnMapping separationColumnMapping = new SeparationColumnMapping();
	//
	private Listener listener;

	public ColumnMappingEditorUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void load(String entries) {

		separationColumnMapping.load(entries);
		updateInput();
	}

	public void setInput(SeparationColumnMapping separationColumnMapping) {

		this.separationColumnMapping = separationColumnMapping;
		updateInput();
	}

	public SeparationColumnMapping getSeparationColumnMapping() {

		return separationColumnMapping;
	}

	@Override
	public void setEnabled(boolean enabled) {

		for(Button button : buttons) {
			button.setEnabled(enabled);
		}
		listControl.get().getControl().setEnabled(enabled);
	}

	@Override
	public void addChangeListener(Listener listener) {

		this.listener = listener;
		//
		Table table = listControl.get().getTable();
		table.addListener(SWT.Selection, listener);
		table.addListener(SWT.KeyUp, listener);
		table.addListener(SWT.MouseUp, listener);
		table.addListener(SWT.MouseDoubleClick, listener);
		//
		buttonAdd.get().addListener(SWT.KeyUp, listener);
		buttonRemove.get().addListener(SWT.KeyUp, listener);
		buttonRemoveAll.get().addListener(SWT.KeyUp, listener);
		buttonImport.get().addListener(SWT.KeyUp, listener);
		buttonExport.get().addListener(SWT.KeyUp, listener);
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		//
		createToolbarMain(this);
		createSearchSection(this);
		createTableSection(this);
		//
		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarSearch, buttonToolbarSearchControl.get(), IMAGE_SEARCH, TOOLTIP_SEARCH, false);
		updateInput();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(6, false));
		//
		createButtonToggleSearch(composite);
		createButtonAdd(composite);
		createButtonRemove(composite);
		createButtonRemoveAll(composite);
		createButtonImport(composite);
		createButtonExport(composite);
	}

	private void add(Button button) {

		buttons.add(button);
	}

	private void createButtonToggleSearch(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH);
		buttons.add(button);
		buttonToolbarSearchControl.set(button);
	}

	private void createSearchSection(Composite parent) {

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

		ColumnMappingListUI columnMappingListUI = new ColumnMappingListUI(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = columnMappingListUI.getTable();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 350;
		table.setLayoutData(gridData);
		//
		Shell shell = table.getShell();
		ITableSettings tableSettings = columnMappingListUI.getTableSettings();
		addDeleteMenuEntry(shell, tableSettings);
		addKeyEventProcessors(shell, tableSettings);
		columnMappingListUI.applySettings(tableSettings);
		//
		listControl.set(columnMappingListUI);
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(ADD_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				InputDialog dialog = new InputDialog(button.getShell(), SeparationColumnMapping.DESCRIPTION, MESSAGE_ADD, "DB5", new IInputValidator() {

					@Override
					public String isValid(String newText) {

						if("".equals(newText)) {
							return "Please type in a keyword.";
						} else if(newText.contains(SeparationColumnMapping.SEPARATOR_ENTRY)) {
							return "The keyword must not contain the following character: '" + SeparationColumnMapping.SEPARATOR_ENTRY + "'";
						} else if(newText.contains(SeparationColumnMapping.SEPARATOR_TOKEN)) {
							return "The keyword must not contain the following character: '" + SeparationColumnMapping.SEPARATOR_TOKEN + "'";
						} else {
							if(separationColumnMapping.containsKey(newText)) {
								return "The keyword already exists.";
							}
						}
						return null;
					}
				});
				//
				if(IDialogConstants.OK_ID == dialog.open()) {
					String item = dialog.getValue().trim();
					if(!"".equals(item)) {
						if(!separationColumnMapping.keySet().contains(item)) {
							separationColumnMapping.put(item, SeparationColumnType.DEFAULT);
							updateInput();
						}
					}
				}
			}
		});
		//
		add(button);
		buttonAdd.set(button);
	}

	private void createButtonRemove(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(REMOVE_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteItems(e.display.getActiveShell());
			}
		});
		//
		add(button);
		buttonRemove.set(button);
	}

	private void createButtonRemoveAll(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(REMOVE_ALL_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE_ALL, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openQuestion(button.getShell(), SeparationColumnMapping.DESCRIPTION, MESSAGE_REMOVE_ALL)) {
					separationColumnMapping.clear();
					updateInput();
				}
			}
		});
		//
		add(button);
		buttonRemoveAll.set(button);
	}

	private void createButtonImport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(IMPORT_TITLE);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IMPORT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.READ_ONLY);
				fileDialog.setText(IMPORT_TITLE);
				fileDialog.setFilterExtensions(new String[]{SeparationColumnMapping.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{SeparationColumnMapping.FILTER_NAME});
				fileDialog.setFilterPath(PreferenceSupplier.getListPathImport());
				String path = fileDialog.open();
				if(path != null) {
					PreferenceSupplier.setListPathImport(fileDialog.getFilterPath());
					File file = new File(path);
					separationColumnMapping.importItems(file);
					updateInput();
				}
			}
		});
		//
		add(button);
		buttonImport.set(button);
	}

	private void createButtonExport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(EXPORT_TITLE);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXPORT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
				fileDialog.setOverwrite(true);
				fileDialog.setText(EXPORT_TITLE);
				fileDialog.setFilterExtensions(new String[]{SeparationColumnMapping.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{SeparationColumnMapping.FILTER_NAME});
				fileDialog.setFileName(SeparationColumnMapping.FILE_NAME);
				fileDialog.setFilterPath(PreferenceSupplier.getListPathExport());
				String path = fileDialog.open();
				if(path != null) {
					PreferenceSupplier.setListPathExport(fileDialog.getFilterPath());
					File file = new File(path);
					if(separationColumnMapping.exportItems(file)) {
						MessageDialog.openInformation(button.getShell(), EXPORT_TITLE, MESSAGE_EXPORT_SUCCESSFUL);
					} else {
						MessageDialog.openWarning(button.getShell(), EXPORT_TITLE, MESSAGE_EXPORT_FAILED);
					}
				}
			}
		});
		//
		add(button);
		buttonExport.set(button);
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

		if(MessageDialog.openQuestion(shell, SeparationColumnMapping.DESCRIPTION, MESSAGE_REMOVE)) {
			IStructuredSelection structuredSelection = listControl.get().getStructuredSelection();
			for(Object object : structuredSelection.toArray()) {
				if(object instanceof Map.Entry<?, ?> setting) {
					separationColumnMapping.remove(setting.getKey());
				}
			}
			updateInput();
		}
	}

	private void updateInput() {

		listControl.get().setInput(separationColumnMapping);
		if(listener != null) {
			listener.handleEvent(new Event());
		}
	}
}