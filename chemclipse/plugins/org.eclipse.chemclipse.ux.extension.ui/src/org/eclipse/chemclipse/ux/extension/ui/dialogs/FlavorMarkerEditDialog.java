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
package org.eclipse.chemclipse.ux.extension.ui.dialogs;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.identifier.FlavorMarker;
import org.eclipse.chemclipse.model.identifier.IFlavorMarker;
import org.eclipse.chemclipse.model.identifier.IOdorThreshold;
import org.eclipse.chemclipse.model.identifier.OdorThreshold;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class FlavorMarkerEditDialog extends TitleAreaDialog {

	private AtomicReference<Text> textOdor = new AtomicReference<>();
	private AtomicReference<Text> textMatrix = new AtomicReference<>();
	private AtomicReference<Text> textSolvent = new AtomicReference<>();
	private AtomicReference<Text> textSamplePreparation = new AtomicReference<>();
	private AtomicReference<Text> textLiteratureReference = new AtomicReference<>();
	private AtomicReference<Text> textThresholdContent = new AtomicReference<>();
	private AtomicReference<Text> textThresholdUnit = new AtomicReference<>();
	private AtomicReference<Button> buttonThresholdDelete = new AtomicReference<>();
	private AtomicReference<Table> tableThresholds = new AtomicReference<>();

	private final IFlavorMarker flavorMarker;
	private final Set<IOdorThreshold> odorThresholds = new HashSet<>();
	private IFlavorMarker result = null;

	public FlavorMarkerEditDialog(Shell parentShell, IFlavorMarker flavorMarker) {

		super(parentShell);
		this.flavorMarker = flavorMarker;
		if(flavorMarker != null) {
			odorThresholds.addAll(flavorMarker.getOdorThresholds());
		}
	}

	public IFlavorMarker getFlavorMarker() {

		return result;
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText(flavorMarker == null ? "Add Flavor Marker" : "Edit Flavor Marker");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("Flavor Marker");
		setMessage("Define the odor, matrix, solvent and properties.", IMessageProvider.INFORMATION);
		Composite container = (Composite)super.createDialogArea(parent);

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createLabel(composite, "Odor:");
		textOdor.set(createText(composite, flavorMarker != null ? flavorMarker.getOdor() : ""));

		createLabel(composite, "Matrix:");
		textMatrix.set(createText(composite, flavorMarker != null ? flavorMarker.getMatrix() : ""));

		createLabel(composite, "Solvent:");
		textSolvent.set(createText(composite, flavorMarker != null ? flavorMarker.getSolvent() : ""));

		createLabel(composite, "Sample Preparation:");
		textSamplePreparation.set(createText(composite, flavorMarker != null ? flavorMarker.getSamplePreparation() : ""));

		Label labelLiteratureReference = new Label(composite, SWT.NONE);
		labelLiteratureReference.setText("Literature Reference:");
		labelLiteratureReference.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		textLiteratureReference.set(createTextMultiLine(composite, flavorMarker != null ? flavorMarker.getLiteratureReference() : ""));

		createSeparator(composite);
		createThresholdSection(composite);

		return container;
	}

	@Override
	protected void okPressed() {

		String odor = textOdor.get().getText().trim();
		String matrix = textMatrix.get().getText().trim();
		String solvent = textSolvent.get().getText().trim();
		FlavorMarker flavorMarker = new FlavorMarker(odor, matrix, solvent);
		flavorMarker.setSamplePreparation(textSamplePreparation.get().getText().trim());
		flavorMarker.setLiteratureReference(textLiteratureReference.get().getText().trim());
		odorThresholds.forEach(flavorMarker::add);
		result = flavorMarker;
		super.okPressed();
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	@Override
	protected Point getInitialSize() {

		return new Point(550, 680);
	}

	private void createSeparator(Composite parent) {

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		separator.setLayoutData(gridData);
	}

	private void createThresholdSection(Composite parent) {

		Label sectionLabel = new Label(parent, SWT.NONE);
		sectionLabel.setText("Odor Thresholds:");
		GridData sectionData = new GridData(GridData.FILL_HORIZONTAL);
		sectionData.horizontalSpan = 2;
		sectionLabel.setLayoutData(sectionData);

		createThresholdToolbar(parent);
		createThresholdTable(parent);
	}

	private void createThresholdToolbar(Composite parent) {

		Composite toolbar = new Composite(parent, SWT.NONE);
		toolbar.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData toolbarData = new GridData(GridData.FILL_HORIZONTAL);
		toolbarData.horizontalSpan = 2;
		toolbar.setLayoutData(toolbarData);
		toolbar.setLayout(new GridLayout(6, false));

		Label labelContent = new Label(toolbar, SWT.NONE);
		labelContent.setText("Content:");

		Text textContent = new Text(toolbar, SWT.BORDER);
		textContent.setToolTipText("Use e.g.: 5.5");
		textContent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textThresholdContent.set(textContent);

		Label labelUnit = new Label(toolbar, SWT.NONE);
		labelUnit.setText("Unit:");

		Text textUnit = new Text(toolbar, SWT.BORDER);
		textUnit.setToolTipText("Use e.g.: (µg/kg Water)");
		textUnit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textThresholdUnit.set(textUnit);

		Button buttonAdd = new Button(toolbar, SWT.PUSH);
		buttonAdd.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		buttonAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				addOdorThreshold();
			}
		});

		Button buttonDelete = new Button(toolbar, SWT.PUSH);
		buttonDelete.setEnabled(false);
		buttonDelete.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		buttonDelete.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteOdorThresholds();
			}
		});
		buttonThresholdDelete.set(buttonDelete);
	}

	private void createThresholdTable(Composite parent) {

		Table table = new Table(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GridData tableData = new GridData(GridData.FILL_BOTH);
		tableData.horizontalSpan = 2;
		tableData.heightHint = 120;
		table.setLayoutData(tableData);

		TableColumn columnContent = new TableColumn(table, SWT.NONE);
		columnContent.setText("Content");
		columnContent.setWidth(250);

		TableColumn columnUnit = new TableColumn(table, SWT.NONE);
		columnUnit.setText("Unit");
		columnUnit.setWidth(200);

		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				buttonThresholdDelete.get().setEnabled(table.getSelectionCount() > 0);
			}
		});

		tableThresholds.set(table);
		updateThresholdTable();
	}

	private void addOdorThreshold() {

		String content = textThresholdContent.get().getText().trim();
		String unit = textThresholdUnit.get().getText().trim();
		if(!content.isEmpty()) {
			IOdorThreshold threshold = new OdorThreshold(content, unit);
			odorThresholds.add(threshold);
			textThresholdContent.get().setText("");
			textThresholdUnit.get().setText("");
			updateThresholdTable();
			textThresholdContent.get().setFocus();
		}
	}

	private void deleteOdorThresholds() {

		Table table = tableThresholds.get();
		for(TableItem item : table.getSelection()) {
			if(item.getData() instanceof IOdorThreshold threshold) {
				odorThresholds.remove(threshold);
			}
		}
		updateThresholdTable();
		buttonThresholdDelete.get().setEnabled(false);
	}

	private void updateThresholdTable() {

		Table table = tableThresholds.get();
		table.removeAll();
		for(IOdorThreshold threshold : odorThresholds) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, threshold.getContent());
			item.setText(1, threshold.getUnit());
			item.setData(threshold);
		}
	}

	private void createLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}

	private Text createText(Composite parent, String value) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText(value != null ? value : "");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return text;
	}

	private Text createTextMultiLine(Composite parent, String value) {

		Text text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		text.setText(value != null ? value : "");
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 150;
		text.setLayoutData(gridData);
		return text;
	}
}