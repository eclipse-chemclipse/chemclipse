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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.swt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.model.IndexNameMarker;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.model.RetentionIndexAssigner;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.internal.provider.IndexAssignerInputValidator;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.validators.RetentionIndexAssignerValidator;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.ui.events.IKeyEventProcessor;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.swt.ui.components.ISearchListener;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.methods.IChangeListener;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class RetentionIndexAssignerEditor extends Composite implements IChangeListener, IExtendedPartUI {

	private static final String ADD_TOOLTIP = "Add a new index name marker.";
	private static final String EDIT_TOOLTIP = "Edit the selected index name marker.";
	private static final String REMOVE_TOOLTIP = "Remove Selected Marker";
	private static final String REMOVE_ALL_TOOLTIP = "Remove All Marker";
	//
	private static final String IMPORT_TITLE = "Import Template";
	private static final String EXPORT_TITLE = "Export Template";
	private static final String DIALOG_TITLE = "Index Name Marker";
	private static final String MESSAGE_ADD = "You can create a new index name marker here.";
	private static final String MESSAGE_EDIT = "Edit the selected marker.";
	private static final String MESSAGE_REMOVE = "Do you want to delete the selected marker?";
	private static final String MESSAGE_REMOVE_ALL = "Do you want to delete all marker?";
	private static final String MESSAGE_EXPORT_SUCCESSFUL = "Marker have been exported successfully.";
	private static final String MESSAGE_EXPORT_FAILED = "Failed to export the marker.";
	//
	private static final String CATEGORY = "Retention Index Marker";
	private static final String DELETE = "Delete";
	//
	private AtomicReference<Button> buttonSearchControl = new AtomicReference<Button>();
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private AtomicReference<IndexAssignerListUI> tableViewer = new AtomicReference<>();
	//
	private List<Button> buttons = new ArrayList<>();
	private List<Listener> listeners = new ArrayList<>();
	//
	private RetentionIndexAssigner retentionIndexAssigner = new RetentionIndexAssigner();

	public RetentionIndexAssignerEditor(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void updateInput() {

		tableViewer.get().setInput(retentionIndexAssigner);
		for(Listener listener : listeners) {
			listener.handleEvent(new Event());
		}
	}

	public RetentionIndexAssigner getRetentionIndexAssigner() {

		return retentionIndexAssigner;
	}

	public void setRetentionIndexMarker(RetentionIndexAssigner retentionIndexAssigner) {

		this.retentionIndexAssigner = retentionIndexAssigner;
		updateInput();
	}

	public void load(String entries) {

		retentionIndexAssigner.load(entries);
		updateInput();
	}

	public String save() {

		return retentionIndexAssigner.save();
	}

	@Override
	public void addChangeListener(Listener listener) {

		/*
		 * Listen to changes in the table.
		 */
		Control control = tableViewer.get().getControl();
		control.addListener(SWT.Selection, listener);
		control.addListener(SWT.KeyUp, listener);
		control.addListener(SWT.MouseUp, listener);
		control.addListener(SWT.MouseDoubleClick, listener);
		/*
		 * Listen to selection of the buttons.
		 */
		for(Button button : buttons) {
			button.addListener(SWT.Selection, listener);
			button.addListener(SWT.KeyUp, listener);
			button.addListener(SWT.MouseUp, listener);
			button.addListener(SWT.MouseDoubleClick, listener);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {

		toolbarSearch.get().setEnabled(enabled);
		tableViewer.get().getControl().setEnabled(enabled);
		for(Button button : buttons) {
			button.setEnabled(enabled);
		}
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		//
		createToolbarMain(this);
		createToolbarSearch(this);
		createTableSection(this);
		//
		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarSearch, buttonSearchControl.get(), IMAGE_SEARCH, TOOLTIP_SEARCH, false);
		updateInput();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(7, false));
		//
		createButtonToggleSearch(composite);
		add(createButtonAdd(composite));
		add(createButtonEdit(composite));
		add(createButtonRemove(composite));
		add(createButtonRemoveAll(composite));
		add(createButtonImport(composite));
		add(createButtonExport(composite));
	}

	private void createButtonToggleSearch(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH);
		buttonSearchControl.set(button);
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

				tableViewer.get().setSearchText(searchText, caseSensitive);
			}
		});
		//
		toolbarSearch.set(searchSupportUI);
	}

	private void createTableSection(Composite parent) {

		IndexAssignerListUI indexAssignerListUI = new IndexAssignerListUI(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = indexAssignerListUI.getTable();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 600;
		gridData.heightHint = 400;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);
		//
		indexAssignerListUI.setUpdateListener(new IUpdateListener() {

			@Override
			public void update() {

				updateInput();
			}
		});
		//
		Shell shell = indexAssignerListUI.getTable().getShell();
		ITableSettings tableSettings = indexAssignerListUI.getTableSettings();
		addDeleteMenuEntry(shell, tableSettings);
		addKeyEventProcessors(shell, tableSettings);
		indexAssignerListUI.applySettings(tableSettings);
		//
		tableViewer.set(indexAssignerListUI);
	}

	private Button createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(ADD_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				InputDialog dialog = new InputDialog(e.display.getActiveShell(), DIALOG_TITLE, MESSAGE_ADD, RetentionIndexAssignerValidator.EXAMPLE_SINGLE, new IndexAssignerInputValidator(retentionIndexAssigner.keySet()));
				if(IDialogConstants.OK_ID == dialog.open()) {
					String item = dialog.getValue();
					IndexNameMarker setting = retentionIndexAssigner.extractSettingInstance(item);
					if(setting != null) {
						retentionIndexAssigner.add(setting);
						updateInput();
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
		button.setToolTipText(EDIT_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IStructuredSelection structuredSelection = (IStructuredSelection)tableViewer.get().getSelection();
				Object object = structuredSelection.getFirstElement();
				if(object instanceof IndexNameMarker setting) {
					RetentionIndexAssigner retentionIndexAssignerModified = new RetentionIndexAssigner();
					retentionIndexAssignerModified.addAll(retentionIndexAssigner);
					retentionIndexAssignerModified.remove(setting);
					InputDialog dialog = new InputDialog(e.display.getActiveShell(), DIALOG_TITLE, MESSAGE_EDIT, retentionIndexAssigner.extractSetting(setting), new IndexAssignerInputValidator(retentionIndexAssignerModified.keySet()));
					if(IDialogConstants.OK_ID == dialog.open()) {
						String item = dialog.getValue();
						IndexNameMarker settingEdited = retentionIndexAssigner.extractSettingInstance(item);
						setting.copyFrom(settingEdited);
						updateInput();
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
		button.setToolTipText(REMOVE_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteItems(e.display.getActiveShell());
			}
		});
		//
		return button;
	}

	private Button createButtonRemoveAll(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(REMOVE_ALL_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE_ALL, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openQuestion(e.display.getActiveShell(), DIALOG_TITLE, MESSAGE_REMOVE_ALL)) {
					retentionIndexAssigner.clear();
					updateInput();
				}
			}
		});
		//
		return button;
	}

	private Button createButtonImport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(IMPORT_TITLE);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IMPORT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.READ_ONLY);
				fileDialog.setText(IMPORT_TITLE);
				fileDialog.setFilterExtensions(new String[]{RetentionIndexAssigner.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{RetentionIndexAssigner.FILTER_NAME});
				fileDialog.setFilterPath(PreferenceSupplier.getListPathImportTemplate());
				String path = fileDialog.open();
				if(path != null) {
					PreferenceSupplier.setListPathImportTemplate(fileDialog.getFilterPath());
					File file = new File(path);
					retentionIndexAssigner.importItems(file);
					updateInput();
				}
			}
		});
		//
		return button;
	}

	private Button createButtonExport(Composite parent) {

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
				fileDialog.setFilterExtensions(new String[]{RetentionIndexAssigner.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{RetentionIndexAssigner.FILTER_NAME});
				fileDialog.setFileName(RetentionIndexAssigner.FILE_NAME);
				fileDialog.setFilterPath(PreferenceSupplier.getListPathExportTemplate());
				String path = fileDialog.open();
				if(path != null) {
					PreferenceSupplier.setListPathExportTemplate(fileDialog.getFilterPath());
					File file = new File(path);
					if(retentionIndexAssigner.exportItems(file)) {
						MessageDialog.openInformation(e.display.getActiveShell(), EXPORT_TITLE, MESSAGE_EXPORT_SUCCESSFUL);
					} else {
						MessageDialog.openWarning(e.display.getActiveShell(), EXPORT_TITLE, MESSAGE_EXPORT_FAILED);
					}
				}
			}
		});
		//
		return button;
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

		if(MessageDialog.openQuestion(shell, DIALOG_TITLE, MESSAGE_REMOVE)) {
			IStructuredSelection structuredSelection = (IStructuredSelection)tableViewer.get().getSelection();
			for(Object object : structuredSelection.toArray()) {
				if(object instanceof IndexNameMarker indexNameMarker) {
					retentionIndexAssigner.remove(indexNameMarker);
				}
			}
			updateInput();
		}
	}
}