/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.ui.swt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.model.TargetTrace;
import org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.model.TargetTraces;
import org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.ui.internal.provider.TargetTraceInputValidator;
import org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.validators.TargetTraceValidator;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.ux.extension.ui.methods.IChangeListener;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class TargetTracesEditor extends Composite implements IChangeListener, IExtendedPartUI {

	private static final String DIALOG_TITLE = "Target Trace";

	private static final String ADD_TOOLTIP = "Add a new target trace.";
	private static final String EDIT_TOOLTIP = "Edit the selected target trace.";
	private static final String REMOVE_TOOLTIP = "Remove selected target trace.";
	private static final String REMOVE_ALL_TOOLTIP = "Remove all target traces.";

	private static final String MESSAGE_ADD = "You can create a new target trace here.";
	private static final String MESSAGE_EDIT = "Edit the selected target trace.";
	private static final String MESSAGE_REMOVE = "Do you want to delete the selected target trace?";
	private static final String MESSAGE_REMOVE_ALL = "Do you want to delete all target trace?";

	private static final String CATEGORY = "Target Trace";
	private static final String DELETE = "Delete";

	private AtomicReference<TargetTraceListUI> tableViewer = new AtomicReference<>();

	private List<Button> buttons = new ArrayList<>();
	private List<Listener> listeners = new ArrayList<>();

	private TargetTraces targetTraces = new TargetTraces();

	public TargetTracesEditor(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void updateInput() {

		tableViewer.get().setInput(targetTraces);
		for(Listener listener : listeners) {
			listener.handleEvent(new Event());
		}
	}

	public TargetTraces getRetentionIndexAssigner() {

		return targetTraces;
	}

	public void setRetentionIndexMarker(TargetTraces retentionIndexAssigner) {

		this.targetTraces = retentionIndexAssigner;
		updateInput();
	}

	public void load(String entries) {

		targetTraces.load(entries);
		updateInput();
	}

	public String save() {

		return targetTraces.save();
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

		createToolbarMain(this);
		createTableSection(this);

		initialize();
	}

	private void initialize() {

		updateInput();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(4, false));

		add(createButtonAdd(composite));
		add(createButtonEdit(composite));
		add(createButtonRemove(composite));
		add(createButtonRemoveAll(composite));
	}

	private void add(Button button) {

		buttons.add(button);
	}

	private void createTableSection(Composite parent) {

		TargetTraceListUI targetTraceListUI = new TargetTraceListUI(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = targetTraceListUI.getTable();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 600;
		gridData.heightHint = 400;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);

		targetTraceListUI.setUpdateListener(this::updateInput);

		Shell shell = targetTraceListUI.getTable().getShell();
		ITableSettings tableSettings = targetTraceListUI.getTableSettings();
		addDeleteMenuEntry(shell, tableSettings);
		addKeyEventProcessors(shell, tableSettings);
		targetTraceListUI.applySettings(tableSettings);

		tableViewer.set(targetTraceListUI);
	}

	private Button createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText(ADD_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				InputDialog dialog = new InputDialog(e.display.getActiveShell(), DIALOG_TITLE, MESSAGE_ADD, TargetTraceValidator.EXAMPLE_SINGLE, new TargetTraceInputValidator(targetTraces));
				if(IDialogConstants.OK_ID == dialog.open()) {
					String item = dialog.getValue();
					TargetTrace setting = targetTraces.extractSettingInstance(item);
					if(setting != null) {
						targetTraces.add(setting);
						updateInput();
					}
				}
			}
		});

		return button;
	}

	private Button createButtonEdit(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText(EDIT_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IStructuredSelection structuredSelection = (IStructuredSelection)tableViewer.get().getSelection();
				Object object = structuredSelection.getFirstElement();
				if(object instanceof TargetTrace setting) {
					TargetTraces targetTracesModified = new TargetTraces();
					for(TargetTrace targetTrace : targetTraces.values()) {
						targetTracesModified.add(targetTrace);
					}
					targetTracesModified.remove(setting);
					InputDialog dialog = new InputDialog(e.display.getActiveShell(), DIALOG_TITLE, MESSAGE_EDIT, targetTraces.extractSetting(setting), new TargetTraceInputValidator(targetTracesModified));
					if(IDialogConstants.OK_ID == dialog.open()) {
						String item = dialog.getValue();
						TargetTrace settingEdited = targetTraces.extractSettingInstance(item);
						setting.copyFrom(settingEdited);
						updateInput();
					}
				}
			}
		});

		return button;
	}

	private Button createButtonRemove(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText(REMOVE_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteItems(e.display.getActiveShell());
			}
		});

		return button;
	}

	private Button createButtonRemoveAll(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText(REMOVE_ALL_TOOLTIP);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE_ALL, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openQuestion(e.display.getActiveShell(), DIALOG_TITLE, MESSAGE_REMOVE_ALL)) {
					targetTraces.clear();
					updateInput();
				}
			}
		});

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

		tableSettings.addKeyEventProcessor((extendedTableViewer, e) -> {

			if(e.keyCode == SWT.DEL) {
				deleteItems(shell);
			}
		});
	}

	private void deleteItems(Shell shell) {

		if(MessageDialog.openQuestion(shell, DIALOG_TITLE, MESSAGE_REMOVE)) {
			IStructuredSelection structuredSelection = (IStructuredSelection)tableViewer.get().getSelection();
			for(Object object : structuredSelection.toArray()) {
				if(object instanceof TargetTrace targetTrace) {
					targetTraces.remove(targetTrace.getIon());
				}
			}
			updateInput();
		}
	}
}
