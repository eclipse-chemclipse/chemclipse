/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.traces;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.traces.NamedTrace;
import org.eclipse.chemclipse.model.traces.NamedTraces;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.support.validators.TraceValidator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.validation.NamedTraceInputValidator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

public class NamedTracesUI extends Composite {

	private static final String TOOLTIP_TEXT = "Edit the traces.";
	private static final String IMPORT_TITLE = "Import " + NamedTraces.DESCRIPTION;
	private static final String EXPORT_TITLE = "Export " + NamedTraces.DESCRIPTION;
	private static final String NO_SELECTION = "--";

	private AtomicReference<ComboViewer> comboViewerControl = new AtomicReference<>();
	private AtomicReference<Text> textTraces = new AtomicReference<>();
	private AtomicReference<Button> buttonAdd = new AtomicReference<>();
	private AtomicReference<Button> buttonDelete = new AtomicReference<>();
	private AtomicReference<Button> buttonImport = new AtomicReference<>();
	private AtomicReference<Button> buttonExport = new AtomicReference<>();

	private NamedTraces namedTraces = null;
	private NamedTrace namedTrace = null;
	private boolean allowNoSelection = false;

	private IUpdateListener updateListener = null;

	public NamedTracesUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setInput(NamedTraces namedTraces) {

		this.setInput(namedTraces, false);
	}

	public void setInput(NamedTraces namedTraces, boolean allowNoSelection) {

		this.namedTraces = namedTraces;
		this.allowNoSelection = allowNoSelection;
		updateInput(null);
	}

	@Override
	public void update() {

		super.update();
		updateNamedTrace();
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public String[] getItems() {

		return comboViewerControl.get().getCombo().getItems();
	}

	public void select(int index) {

		/*
		 * Increase index, because the first item
		 * is the empty selection operator.
		 */
		if(allowNoSelection) {
			index++;
		}

		if(index >= 0 && index < getItems().length) {
			comboViewerControl.get().getCombo().select(index);
			Object object = comboViewerControl.get().getStructuredSelection().getFirstElement();
			if(object instanceof NamedTrace trace) {
				namedTrace = trace;
				updateNamedTrace();
			}
		}
	}

	/**
	 * Could be null if none has been set.
	 * 
	 * @return {@link NamedTraces}
	 */
	public NamedTraces getNamedTraces() {

		return namedTraces;
	}

	/**
	 * Could be null if none is selected.
	 * 
	 * @return {@link NamedTrace}
	 */
	public NamedTrace getNamedTrace() {

		return namedTrace;
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		setLayout(gridLayout);
		//
		createComboViewer(this);
		createText(this);
		createButtonAdd(this);
		createButtonDelete(this);
		createButtonImport(this);
		createButtonExport(this);
	}

	private void createComboViewer(Composite composite) {

		ComboViewer comboViewer = new EnhancedComboViewer(composite, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof NamedTrace namedTrace) {
					return namedTrace.getIdentifier();
				} else if(element instanceof String value) {
					return value;
				}
				return null;
			}
		});
		/*
		 * Select the item.
		 */
		combo.setToolTipText("Select a named trace.");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = comboViewer.getStructuredSelection().getFirstElement();
				if(object instanceof NamedTrace selectedNamedTrace) {
					namedTrace = selectedNamedTrace;
				} else {
					namedTrace = null;
				}
				updateNamedTrace();
				fireUpdate();
			}
		});
		//
		comboViewerControl.set(comboViewer);
	}

	private void createText(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		text.setToolTipText(TOOLTIP_TEXT);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		TraceValidator traceValidator = new TraceValidator();
		ControlDecoration controlDecoration = new ControlDecoration(text, SWT.LEFT | SWT.TOP);
		//
		text.addModifyListener(event -> {

			if(namedTrace != null) {
				if(validate(traceValidator, controlDecoration, text)) {
					namedTrace.setTraces(traceValidator.getTracesAsString());
					fireUpdate();
				}
			}
		});
		//
		textTraces.set(text);
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Add a new named trace.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(namedTraces != null) {
					InputDialog dialog = new InputDialog(e.display.getActiveShell(), "Named Trace", "Create a new named trace.", "Hydrocarbons | 57 71 85", new NamedTraceInputValidator(namedTraces.keySet()));
					if(IDialogConstants.OK_ID == dialog.open()) {
						String item = dialog.getValue();
						NamedTrace namedTraceNew = namedTraces.extractNamedTrace(item);
						if(namedTraceNew != null) {
							namedTraces.add(namedTraceNew);
							namedTrace = namedTraceNew;
							updateInput(namedTrace.getIdentifier());
							fireUpdate();
						}
					}
				}
			}
		});

		buttonAdd.set(button);
	}

	private void createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete the selected named trace.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openQuestion(e.display.getActiveShell(), "Named Trace", "Would you like to delete the selected named trace?")) {
					Object object = comboViewerControl.get().getStructuredSelection().getFirstElement();
					if(object instanceof NamedTrace selectedNamedTrace) {
						namedTrace = null;
						namedTraces.remove(selectedNamedTrace);
						updateInput(null);
						fireUpdate();
					}
				}
			}
		});

		buttonDelete.set(button);
	}

	private void createButtonImport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Import named traces.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IMPORT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(namedTraces != null) {
					IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
					FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.READ_ONLY);
					fileDialog.setText(IMPORT_TITLE);
					fileDialog.setFilterExtensions(new String[]{NamedTraces.FILTER_EXTENSION});
					fileDialog.setFilterNames(new String[]{NamedTraces.FILTER_NAME});
					fileDialog.setFilterPath(preferenceStore.getString(PreferenceSupplier.P_NAMED_TRACES_TEMPLATE_FOLDER));
					String path = fileDialog.open();
					if(path != null) {
						preferenceStore.setValue(PreferenceSupplier.P_NAMED_TRACES_TEMPLATE_FOLDER, fileDialog.getFilterPath());
						File file = new File(path);
						namedTraces.importItems(file);
						MessageDialog.openInformation(e.display.getActiveShell(), IMPORT_TITLE, "Named traces have been imported successfully.");
						updateInput(null);
						fireUpdate();
					}
				}
			}
		});

		buttonImport.set(button);
	}

	private void createButtonExport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Export named traces.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXPORT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(namedTraces != null) {
					IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
					FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
					fileDialog.setOverwrite(true);
					fileDialog.setText(EXPORT_TITLE);
					fileDialog.setFilterExtensions(new String[]{NamedTraces.FILTER_EXTENSION});
					fileDialog.setFilterNames(new String[]{NamedTraces.FILTER_NAME});
					fileDialog.setFileName(NamedTraces.FILE_NAME);
					fileDialog.setFilterPath(preferenceStore.getString(PreferenceSupplier.P_NAMED_TRACES_TEMPLATE_FOLDER));
					String path = fileDialog.open();
					if(path != null) {
						preferenceStore.setValue(PreferenceSupplier.P_NAMED_TRACES_TEMPLATE_FOLDER, fileDialog.getFilterPath());
						File file = new File(path);
						if(namedTraces.exportItems(file)) {
							MessageDialog.openInformation(e.display.getActiveShell(), EXPORT_TITLE, "Named traces have been exported successfully.");
						} else {
							MessageDialog.openWarning(e.display.getActiveShell(), EXPORT_TITLE, "Failed to export the named traces.");
						}
					}
				}
			}
		});

		buttonExport.set(button);
	}

	private void updateInput(String identifier) {

		List<Object> input = new ArrayList<Object>();
		if(allowNoSelection) {
			input.add(NO_SELECTION);
		}
		ComboViewer comboViewer = comboViewerControl.get();

		namedTrace = null;
		if(namedTraces != null) {
			/*
			 * Sort the traces by identifier.
			 */
			List<NamedTrace> traces = new ArrayList<>(namedTraces.values());
			Collections.sort(traces, (t1, t2) -> t1.getIdentifier().compareTo(t2.getIdentifier()));
			input.addAll(traces);
			/*
			 * Set selection
			 */
			Object object = comboViewer.getStructuredSelection().getFirstElement();
			if(identifier == null) {
				if(object instanceof NamedTrace selection) {
					namedTrace = getNamedTrace(selection.getIdentifier());
				}
			} else {
				namedTrace = getNamedTrace(identifier);
			}
		}

		comboViewer.setInput(input);
		Object selection = determineSelection(input, namedTrace);
		if(selection != null) {
			comboViewer.setSelection(new StructuredSelection(selection));
			if(namedTrace == null) {
				if(selection instanceof NamedTrace trace) {
					namedTrace = trace;
				}
			}
		}

		updateNamedTrace();
		updateWidgets();
	}

	private NamedTrace getNamedTrace(String identifier) {

		for(NamedTrace namedTrace : namedTraces.values()) {
			if(namedTrace.getIdentifier().equals(identifier)) {
				return namedTrace;
			}
		}

		return namedTrace;
	}

	private Object determineSelection(List<Object> input, NamedTrace namedTrace) {

		if(namedTrace != null) {
			return namedTrace;
		} else {
			if(allowNoSelection) {
				return NO_SELECTION;
			} else {
				if(!input.isEmpty()) {
					return input.get(0);
				}
			}
		}

		return null;
	}

	private void updateWidgets() {

		boolean tracesAvailable = namedTraces != null;
		enableWidgets(tracesAvailable);
		if(tracesAvailable) {
			int indexDelete = allowNoSelection ? 0 : -1;
			buttonDelete.get().setEnabled(comboViewerControl.get().getCombo().getSelectionIndex() > indexDelete);
		}
	}

	private void enableWidgets(boolean enabled) {

		buttonAdd.get().setEnabled(enabled);
		buttonDelete.get().setEnabled(enabled);
		buttonImport.get().setEnabled(enabled);
		buttonExport.get().setEnabled(enabled);
	}

	private void updateNamedTrace() {

		textTraces.get().setText(namedTrace != null ? namedTrace.getTraces() : "");
		buttonDelete.get().setEnabled(namedTrace != null);
	}

	private boolean validate(IValidator<Object> validator, ControlDecoration controlDecoration, Text text) {

		IStatus status = validator.validate(text.getText().trim());
		if(status.isOK()) {
			controlDecoration.hide();
			text.setToolTipText(TOOLTIP_TEXT);
			return true;
		} else {
			controlDecoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage());
			controlDecoration.showHoverText(status.getMessage());
			controlDecoration.show();
			text.setToolTipText(status.getMessage());
			return false;
		}
	}

	private void fireUpdate() {

		if(updateListener != null) {
			getDisplay().asyncExec(() -> updateListener.update());
		}
	}
}