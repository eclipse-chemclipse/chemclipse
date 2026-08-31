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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.core.DataType;
import org.eclipse.chemclipse.model.core.ExtractTransferDefinition;
import org.eclipse.chemclipse.model.core.ExtractTransferDefinitionIO;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.swt.impl.ExtractTransferDefinitionEditDialog;
import org.eclipse.chemclipse.support.ui.swt.impl.ExtractTransferDefinitionListUI;
import org.eclipse.chemclipse.support.ui.swt.impl.ExtractTransferPreviewListUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class ExtractTransferDialog extends TitleAreaDialog {

	private static final Map<String, String> LIBRARY_FIELDS = new LinkedHashMap<>();

	static {
		LIBRARY_FIELDS.put("name", "Name");
		LIBRARY_FIELDS.put("referenceIdentifier", "Reference Identifier");
		LIBRARY_FIELDS.put("comments", "Comments");
		LIBRARY_FIELDS.put("miscellaneous", "Miscellaneous");
		LIBRARY_FIELDS.put("casNumber", "CAS Number");
		LIBRARY_FIELDS.put("formula", "Formula");
		LIBRARY_FIELDS.put("smiles", "SMILES");
		LIBRARY_FIELDS.put("inChI", "InChI");
		LIBRARY_FIELDS.put("database", "Database");
		LIBRARY_FIELDS.put("contributor", "Contributor");
		LIBRARY_FIELDS.put("retentionTime", "Retention Time");
		LIBRARY_FIELDS.put("retentionIndex", "Retention Index");
	}

	private static final String IMPORT_TITLE = "Import Extract Transfer Definitions";
	private static final String EXPORT_TITLE = "Export Extract Transfer Definitions";
	private static final String MESSAGE_EXPORT_SUCCESSFUL = "Extract transfer definitions have been exported successfully.";
	private static final String MESSAGE_EXPORT_FAILED = "Failed to export the extract transfer definitions.";

	private static final int PREVIEW_LIMIT = 50;

	private AtomicReference<ExtractTransferDefinitionListUI> definitionsTableControl = new AtomicReference<>();
	private AtomicReference<ExtractTransferPreviewListUI> previewTableControl = new AtomicReference<>();

	private List<ExtractTransferDefinition> definitions = new ArrayList<>();
	private final IMassSpectra massSpectra;

	public ExtractTransferDialog(Shell parentShell, IMassSpectra massSpectra) {

		super(parentShell);
		this.massSpectra = massSpectra;
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText("Extract and Transfer");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite control = (Composite)super.createDialogArea(parent);
		setTitle("Extract and Transfer Library Information");
		setMessage("Define rules to extract data from source fields and transfer them to sink fields.");
		control.setBackgroundMode(SWT.INHERIT_DEFAULT);

		SashForm sashForm = new SashForm(control, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		createDefinitionsPart(sashForm);
		createPreviewPart(sashForm);

		sashForm.setWeights(60, 40);
		initialize();

		return control;
	}

	private void initialize() {

		definitionsTableControl.get().setInput(definitions);
		updatePreview();
	}

	private void createDefinitionsPart(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));

		createToolbarMain(composite);
		createDefinitionsListUI(composite);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(5, false));

		createButtonAdd(composite);
		createButtonModify(composite);
		createButtonRemove(composite);
		createButtonImport(composite);
		createButtonExport(composite);
	}

	private void createDefinitionsListUI(Composite parent) {

		ExtractTransferDefinitionListUI listUI = new ExtractTransferDefinitionListUI(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL, LIBRARY_FIELDS);
		listUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

		definitionsTableControl.set(listUI);
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Add a new definition.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				ExtractTransferDefinitionEditDialog dialog = new ExtractTransferDefinitionEditDialog(getShell(), null, LIBRARY_FIELDS);
				if(dialog.open() == Window.OK) {
					definitions.add(dialog.getDefinition());
					definitionsTableControl.get().refresh();
					updatePreview();
				}
			}
		});
	}

	private void createButtonModify(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Modify the selected definition.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object selection = definitionsTableControl.get().getStructuredSelection().getFirstElement();
				if(selection instanceof ExtractTransferDefinition def) {
					int index = definitions.indexOf(def);
					ExtractTransferDefinitionEditDialog dialog = new ExtractTransferDefinitionEditDialog(getShell(), def, LIBRARY_FIELDS);
					if(dialog.open() == Window.OK && index >= 0) {
						definitions.set(index, dialog.getDefinition());
						definitionsTableControl.get().refresh();
						updatePreview();
					}
				}
			}
		});
	}

	private void createButtonRemove(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Remove the selected definition.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object selection = definitionsTableControl.get().getStructuredSelection().getFirstElement();
				if(selection instanceof ExtractTransferDefinition def) {
					definitions.remove(def);
					definitionsTableControl.get().refresh();
					updatePreview();
				}
			}
		});
	}

	private void createButtonImport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText(IMPORT_TITLE);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IMPORT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(getShell(), SWT.READ_ONLY);
				fileDialog.setText(IMPORT_TITLE);
				fileDialog.setFilterExtensions(new String[]{ExtractTransferDefinitionIO.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{ExtractTransferDefinitionIO.FILTER_NAME});
				String path = fileDialog.open();
				if(path != null) {
					definitions.addAll(ExtractTransferDefinitionIO.importDefinitions(new File(path)));
					definitionsTableControl.get().refresh();
					updatePreview();
				}
			}
		});
	}

	private void createButtonExport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText(EXPORT_TITLE);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXPORT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
				fileDialog.setOverwrite(true);
				fileDialog.setText(EXPORT_TITLE);
				fileDialog.setFilterExtensions(new String[]{ExtractTransferDefinitionIO.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{ExtractTransferDefinitionIO.FILTER_NAME});
				fileDialog.setFileName(ExtractTransferDefinitionIO.FILE_NAME);
				String path = fileDialog.open();
				if(path != null) {
					if(ExtractTransferDefinitionIO.exportDefinitions(new File(path), definitions)) {
						MessageDialog.openInformation(getShell(), EXPORT_TITLE, MESSAGE_EXPORT_SUCCESSFUL);
					} else {
						MessageDialog.openWarning(getShell(), EXPORT_TITLE, MESSAGE_EXPORT_FAILED);
					}
				}
			}
		});
	}

	private void createPreviewPart(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));

		ExtractTransferPreviewListUI listUI = new ExtractTransferPreviewListUI(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		listUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		previewTableControl.set(listUI);
	}

	private void updatePreview() {

		List<String[]> previewEntries = new ArrayList<>();
		if(massSpectra != null && !definitions.isEmpty()) {
			int count = 0;
			for(IScanMSD scan : massSpectra.getList()) {
				if(count >= PREVIEW_LIMIT) {
					break;
				}
				ILibraryInformation libraryInformation = getLibraryInformation(scan);
				if(libraryInformation != null) {
					for(ExtractTransferDefinition definition : definitions) {
						String sourceValue = getFieldValue(libraryInformation, definition.getSourceField());
						if(sourceValue != null && !sourceValue.isBlank()) {
							Object result = definition.getResult(sourceValue);
							if(result != null) {
								String sourceLabel = LIBRARY_FIELDS.getOrDefault(definition.getSourceField(), definition.getSourceField());
								String sinkLabel = LIBRARY_FIELDS.getOrDefault(definition.getSinkField(), definition.getSinkField());
								previewEntries.add(new String[]{String.valueOf(definition.isUse()), sourceLabel + ": '" + sourceValue + "'", sinkLabel + ": '" + result + "'"});
								count++;
							}
						}
					}
				}
			}
		}
		previewTableControl.get().setInput(previewEntries);
	}

	@Override
	protected void okPressed() {

		applyDefinitions();
		super.okPressed();
	}

	private void applyDefinitions() {

		if(massSpectra == null || definitions.isEmpty()) {
			return;
		}
		/*
		 * Definitions that write to 'name' run last so that other definitions
		 * still read the original name value.
		 */
		List<ExtractTransferDefinition> sorted = new ArrayList<>(definitions);
		sorted.sort((d1, d2) -> {
			boolean nameSinkD1 = "name".equals(d1.getSinkField());
			boolean nameSinkD2 = "name".equals(d2.getSinkField());
			if(nameSinkD1 == nameSinkD2) {
				return 0;
			}
			return nameSinkD1 ? 1 : -1;
		});

		for(IScanMSD scan : massSpectra.getList()) {
			ILibraryInformation libraryInformation = getLibraryInformation(scan);
			if(libraryInformation != null) {
				for(ExtractTransferDefinition definition : sorted) {
					if(!definition.isUse()) {
						continue;
					}
					String sourceValue = getFieldValue(libraryInformation, definition.getSourceField());
					if(sourceValue != null && !sourceValue.isBlank()) {
						Object result = definition.getResult(sourceValue);
						if(result != null) {
							setFieldValue(libraryInformation, definition.getSinkField(), result, definition.getDataType());
						}
					}
				}
			}
		}
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	@Override
	protected Point getInitialSize() {

		return new Point(800, 600);
	}

	private ILibraryInformation getLibraryInformation(IScanMSD scan) {

		if(scan instanceof IRegularLibraryMassSpectrum libraryMassSpectrum) {
			return libraryMassSpectrum.getLibraryInformation();
		}
		return IIdentificationTarget.getLibraryInformation(scan);
	}

	private static String getFieldValue(ILibraryInformation libraryInformation, String fieldName) {

		if(libraryInformation == null || fieldName == null) {
			return "";
		}

		return switch(fieldName) {
			case "name" -> libraryInformation.getName();
			case "referenceIdentifier" -> libraryInformation.getReferenceIdentifier();
			case "comments" -> libraryInformation.getComments();
			case "miscellaneous" -> libraryInformation.getMiscellaneous();
			case "casNumber" -> libraryInformation.getCasNumber();
			case "formula" -> libraryInformation.getFormula();
			case "smiles" -> libraryInformation.getSmiles();
			case "inChI" -> libraryInformation.getInChI();
			case "database" -> libraryInformation.getDatabase();
			case "contributor" -> libraryInformation.getContributor();
			case "retentionTime" -> String.valueOf(libraryInformation.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
			case "retentionIndex" -> String.valueOf(libraryInformation.getRetentionIndex());
			default -> "";
		};
	}

	private static void setFieldValue(ILibraryInformation info, String fieldName, Object value, DataType dataType) {

		if(info == null || fieldName == null || value == null || dataType == null) {
			return;
		}
		String strValue = value.toString();
		switch(fieldName) {
			case "name" -> info.setName(strValue);
			case "referenceIdentifier" -> info.setReferenceIdentifier(strValue);
			case "comments" -> info.setComments(strValue);
			case "miscellaneous" -> info.setMiscellaneous(strValue);
			case "casNumber" -> info.setCasNumber(strValue);
			case "formula" -> info.setFormula(strValue);
			case "smiles" -> info.setSmiles(strValue);
			case "inChI" -> info.setInChI(strValue);
			case "database" -> info.setDatabase(strValue);
			case "contributor" -> info.setContributor(strValue);
			case "retentionTime" -> {
				try {
					if(value instanceof Double d) {
						info.setRetentionTime((int)(d * IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
					} else if(value instanceof Float f) {
						info.setRetentionTime((int)(f * IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
					} else if(value instanceof Number number) {
						info.setRetentionTime(number.intValue());
					} else {
						info.setRetentionTime(Integer.parseInt(strValue));
					}
				} catch(NumberFormatException e) {
				}
			}
			case "retentionIndex" -> {
				try {
					if(value instanceof Number number) {
						info.setRetentionIndex(number.floatValue());
					} else {
						info.setRetentionIndex(Float.parseFloat(strValue));
					}
				} catch(NumberFormatException e) {
				}
			}
		}
	}
}