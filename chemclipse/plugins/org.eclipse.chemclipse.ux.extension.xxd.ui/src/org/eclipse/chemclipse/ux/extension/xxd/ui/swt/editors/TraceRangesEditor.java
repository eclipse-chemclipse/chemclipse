/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.swt.ui.components.ISearchListener;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.tsd.model.core.TraceRange;
import org.eclipse.chemclipse.tsd.model.core.TraceRanges;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.TraceRangeInputValidator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.methods.IChangeListener;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.TraceRangesListUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

public class TraceRangesEditor extends Composite implements IChangeListener, IExtendedPartUI {

	public static final String ADD = "Add";
	public static final String ADD_TOOLTIP = "Add a new stack range.";
	public static final String EDIT = "Edit";
	public static final String EDIT_TOOLTIP = "Edit the selected stack range.";
	public static final String REMOVE = "Remove";
	public static final String REMOVE_TOOLTIP = "Remove selected stack ranges.";
	public static final String REMOVE_ALL = "Remove all";
	public static final String REMOVE_ALL_TOOLTIP = "Remove all stack ranges.";
	public static final String IMPORT = "Import";
	public static final String EXPORT = "Export";
	//
	public static final String IMPORT_TITLE = "Import";
	public static final String EXPORT_TITLE = "Export";
	public static final String DIALOG_TITLE = "Stack Ranges";
	public static final String MESSAGE_ADD = "You can create a new stack range here.";
	public static final String MESSAGE_EDIT = "Edit the selected stack ranges.";
	public static final String MESSAGE_REMOVE = "Do you want to delete the selected stack ranges?";
	public static final String MESSAGE_REMOVE_ALL = "Do you want to delete all stack ranges?";
	public static final String MESSAGE_EXPORT_SUCCESSFUL = "Stack ranges have been exported successfully.";
	public static final String MESSAGE_EXPORT_FAILED = "Failed to export the stack ranges.";
	//
	public static final String EXAMPLE_ENTRY = "1.0 | 3.0 | 0 | 0 | 150 - 160";
	//
	private AtomicReference<TraceRangesListUI> listControl = new AtomicReference<>();
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarSearch = new AtomicReference<>();
	private AtomicReference<Button> buttonAdd = new AtomicReference<>();
	private AtomicReference<Button> buttonEdit = new AtomicReference<>();
	private AtomicReference<Button> buttonRemove = new AtomicReference<>();
	private AtomicReference<Button> buttonRemoveAll = new AtomicReference<>();
	private AtomicReference<Button> buttonImport = new AtomicReference<>();
	private AtomicReference<Button> buttonExport = new AtomicReference<>();
	//
	private TraceRanges traceRanges = new TraceRanges();
	private Listener listener;

	public TraceRangesEditor(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setInput(TraceRanges traceRanges) {

		this.traceRanges = (traceRanges == null) ? new TraceRanges() : traceRanges;
		setInput();
	}

	public void clear() {

		traceRanges.clear();
		setInput();
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
		buttonEdit.get().addListener(SWT.KeyUp, listener);
		buttonRemove.get().addListener(SWT.KeyUp, listener);
		buttonRemoveAll.get().addListener(SWT.KeyUp, listener);
		buttonImport.get().addListener(SWT.KeyUp, listener);
		buttonExport.get().addListener(SWT.KeyUp, listener);
	}

	@Override
	public void setEnabled(boolean enabled) {

		toolbarSearch.get().setEnabled(enabled);
		buttonAdd.get().setEnabled(enabled);
		buttonEdit.get().setEnabled(enabled);
		buttonRemove.get().setEnabled(enabled);
		buttonRemoveAll.get().setEnabled(enabled);
		buttonImport.get().setEnabled(enabled);
		buttonExport.get().setEnabled(enabled);
		listControl.get().getControl().setEnabled(enabled);
	}

	public TraceRanges getTraceRanges() {

		return traceRanges;
	}

	public void load(String entries) {

		traceRanges.load(entries);
		setInput();
	}

	public String save() {

		return traceRanges.save();
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

		enableToolbar(toolbarSearch, buttonToolbarSearch.get(), IMAGE_SEARCH, TOOLTIP_SEARCH, false);
		setInput();
	}

	private void createTableSection(Composite parent) {

		TraceRangesListUI stackRangesListUI = new TraceRangesListUI(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = stackRangesListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		stackRangesListUI.setEditEnabled(true);
		stackRangesListUI.setUpdateListener(new IUpdateListener() {

			@Override
			public void update() {

				setInput();
			}
		});
		//
		ITableSettings tableSettings = stackRangesListUI.getTableSettings();
		stackRangesListUI.applySettings(tableSettings);
		//
		listControl.set(stackRangesListUI);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(7, false));
		//
		createButtonToggleToolbar(composite);
		createButtonAdd(composite);
		createButtonEdit(composite);
		createButtonRemove(composite);
		createButtonRemoveAll(composite);
		createButtonImport(composite);
		createButtonExport(composite);
	}

	private void createButtonToggleToolbar(Composite parent) {

		buttonToolbarSearch.set(createButtonToggleToolbar(parent, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH));
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

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(ADD_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				InputDialog dialog = new InputDialog(button.getShell(), DIALOG_TITLE, MESSAGE_ADD, EXAMPLE_ENTRY, new TraceRangeInputValidator(traceRanges));
				if(IDialogConstants.OK_ID == dialog.open()) {
					String item = dialog.getValue();
					TraceRange setting = traceRanges.extractTraceRange(item);
					if(setting != null) {
						traceRanges.add(setting);
						setInput();
					}
				}
			}
		});
		//
		buttonAdd.set(button);
	}

	private void createButtonEdit(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(EDIT_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				TraceRange traceRange = getTraceRangeSelected();
				if(traceRange != null) {
					InputDialog dialog = new InputDialog(button.getShell(), DIALOG_TITLE, MESSAGE_ADD, traceRanges.extractTraceRange(traceRange), new TraceRangeInputValidator(traceRanges));
					if(IDialogConstants.OK_ID == dialog.open()) {
						String item = dialog.getValue();
						TraceRange setting = traceRanges.extractTraceRange(item);
						if(setting != null) {
							traceRanges.remove(traceRange);
							traceRanges.add(setting);
							setInput();
						}
					}
				}
			}
		});
		//
		buttonEdit.set(button);
	}

	private void createButtonRemove(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(REMOVE_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int index = listControl.get().getTable().getSelectionIndex();
				traceRanges.remove(index);
				setInput();
			}
		});
		//
		buttonRemove.set(button);
	}

	private void createButtonRemoveAll(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(REMOVE_ALL_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE_ALL, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openQuestion(e.display.getActiveShell(), DIALOG_TITLE, MESSAGE_REMOVE_ALL)) {
					traceRanges.clear();
					setInput();
				}
			}
		});
		//
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

				FileDialog fileDialog = new FileDialog(e.widget.getDisplay().getActiveShell(), SWT.READ_ONLY);
				fileDialog.setText(IMPORT_TITLE);
				fileDialog.setFilterExtensions(new String[]{TraceRanges.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{TraceRanges.FILTER_NAME});
				fileDialog.setFilterPath(PreferenceSupplier.getListPathImport());
				String path = fileDialog.open();
				if(path != null) {
					PreferenceSupplier.setListPathImport(fileDialog.getFilterPath());
					File file = new File(path);
					traceRanges.importRules(file);
					setInput();
				}
			}
		});
		//
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

				FileDialog fileDialog = new FileDialog(e.widget.getDisplay().getActiveShell(), SWT.SAVE);
				fileDialog.setOverwrite(true);
				fileDialog.setText(EXPORT_TITLE);
				fileDialog.setFilterExtensions(new String[]{TraceRanges.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{TraceRanges.FILTER_NAME});
				fileDialog.setFileName(TraceRanges.FILE_NAME);
				fileDialog.setFilterPath(PreferenceSupplier.getListPathExport());
				String path = fileDialog.open();
				if(path != null) {
					PreferenceSupplier.setListPathExport(fileDialog.getFilterPath());
					File file = new File(path);
					if(traceRanges.exportRules(file)) {
						MessageDialog.openInformation(e.display.getActiveShell(), EXPORT_TITLE, MESSAGE_EXPORT_SUCCESSFUL);
					} else {
						MessageDialog.openWarning(e.display.getActiveShell(), EXPORT_TITLE, MESSAGE_EXPORT_FAILED);
					}
				}
			}
		});
		//
		buttonExport.set(button);
	}

	private TraceRange getTraceRangeSelected() {

		if(listControl.get().getStructuredSelection().getFirstElement() instanceof TraceRange traceRange) {
			return traceRange;
		}
		//
		return null;
	}

	private void setInput() {

		listControl.get().setInput(traceRanges);
		//
		if(listener != null) {
			listener.handleEvent(new Event());
		}
	}
}