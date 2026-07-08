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

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.cas.CasValidator;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.swt.ui.components.identification.SynonymsEditUI;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.ux.extension.msd.ui.swt.MassSpectrumEditor;
import org.eclipse.chemclipse.ux.extension.ui.swt.ColumnIndicesEditUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.FlavorMarkersEditUI;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class LibraryEntryEditDialog extends TitleAreaDialog {

	private final CasValidator casValidator = new CasValidator(true);
	private final DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglishModifiable();

	private AtomicReference<Text> textName = new AtomicReference<>();
	private AtomicReference<Text> textCasNumber = new AtomicReference<>();
	private AtomicReference<Text> textSmiles = new AtomicReference<>();
	private AtomicReference<Text> textRetentionTime = new AtomicReference<>();
	private AtomicReference<Text> textMolWeight = new AtomicReference<>();
	private AtomicReference<Text> textExactMass = new AtomicReference<>();
	private AtomicReference<Text> textFormula = new AtomicReference<>();
	private AtomicReference<Text> textInChI = new AtomicReference<>();
	private AtomicReference<Text> textReferenceIdentifier = new AtomicReference<>();
	private AtomicReference<Text> textComments = new AtomicReference<>();

	private IRegularLibraryMassSpectrum massSpectrum;

	public LibraryEntryEditDialog(Shell parentShell, IRegularLibraryMassSpectrum massSpectrum) {

		super(parentShell);
		this.massSpectrum = massSpectrum;
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText("Edit Library Entry");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("Library Entry");
		setMessage("Edit the library entry.", IMessageProvider.INFORMATION);
		Composite container = (Composite)super.createDialogArea(parent);
		container.setBackgroundMode(SWT.INHERIT_DEFAULT);

		TabFolder tabFolder = new TabFolder(container, SWT.BOTTOM);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTabGeneral(tabFolder);
		createTabIons(tabFolder);
		createTabSynonyms(tabFolder);
		createTabColumnIndices(tabFolder);
		createTabFlavorMarkers(tabFolder);

		return container;
	}

	@Override
	protected void okPressed() {

		ILibraryInformation libraryInformation = massSpectrum.getLibraryInformation();
		if(libraryInformation != null) {
			libraryInformation.setName(textName.get().getText().trim());
			libraryInformation.setCasNumber(textCasNumber.get().getText().trim());
			libraryInformation.setSmiles(textSmiles.get().getText().trim());
			libraryInformation.setFormula(textFormula.get().getText().trim());
			libraryInformation.setInChI(textInChI.get().getText().trim());
			libraryInformation.setReferenceIdentifier(textReferenceIdentifier.get().getText().trim());
			libraryInformation.setComments(textComments.get().getText().trim());
			/*
			 * Retention Time
			 */
			try {
				massSpectrum.setRetentionTime((int)(Double.parseDouble(textRetentionTime.get().getText().trim()) * IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
			} catch(NumberFormatException e) {
			}
			/*
			 * Mol Weight
			 */
			try {
				libraryInformation.setMolWeight(Double.parseDouble(textMolWeight.get().getText().trim()));
			} catch(NumberFormatException e) {
			}
			/*
			 * Exact Mass
			 */
			try {
				libraryInformation.setExactMass(Double.parseDouble(textExactMass.get().getText().trim()));
			} catch(NumberFormatException e) {
			}
		}
		super.okPressed();
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	@Override
	protected Point getInitialSize() {

		return new Point(700, 700);
	}

	private void createTabGeneral(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("General");

		ILibraryInformation libraryInformation = massSpectrum.getLibraryInformation();

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		createLabel(composite, "Name:");
		textName.set(createText(composite, libraryInformation != null ? libraryInformation.getName() : ""));

		createLabel(composite, "CAS Number:");
		textCasNumber.set(createTextCasNumber(composite, libraryInformation != null ? libraryInformation.getCasNumber() : ""));

		createLabel(composite, "SMILES:");
		textSmiles.set(createText(composite, libraryInformation != null ? libraryInformation.getSmiles() : ""));

		createLabel(composite, "Retention Time [min]:");
		textRetentionTime.set(createText(composite, libraryInformation != null ? decimalFormat.format(massSpectrum.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR) : ""));

		createLabel(composite, "Mol Weight (MW):");
		textMolWeight.set(createText(composite, libraryInformation != null ? decimalFormat.format(libraryInformation.getMolWeight()) : ""));

		createLabel(composite, "Exact Mass:");
		textExactMass.set(createText(composite, libraryInformation != null ? decimalFormat.format(libraryInformation.getExactMass()) : ""));

		createLabel(composite, "Formula:");
		textFormula.set(createText(composite, libraryInformation != null ? libraryInformation.getFormula() : ""));

		createLabel(composite, "InChI:");
		textInChI.set(createText(composite, libraryInformation != null ? libraryInformation.getInChI() : ""));

		createLabel(composite, "Reference Identifier:");
		textReferenceIdentifier.set(createText(composite, libraryInformation != null ? libraryInformation.getReferenceIdentifier() : ""));

		createLabel(composite, "Comments:");
		textComments.set(createTextMultiLine(composite, libraryInformation != null ? libraryInformation.getComments() : ""));

		tabItem.setControl(composite);
	}

	private void createTabIons(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Ions");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout());

		MassSpectrumEditor ionsEditUI = new MassSpectrumEditor(composite, SWT.NONE);
		ionsEditUI.update(massSpectrum);

		tabItem.setControl(composite);
	}

	private void createTabSynonyms(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Synonyms");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout());

		SynonymsEditUI synonymsEditUI = new SynonymsEditUI(composite, SWT.NONE);
		synonymsEditUI.update(massSpectrum.getLibraryInformation());

		tabItem.setControl(composite);
	}

	private void createTabFlavorMarkers(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Flavor");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout());

		FlavorMarkersEditUI flavorMarkersEditUI = new FlavorMarkersEditUI(composite, SWT.NONE);
		flavorMarkersEditUI.update(massSpectrum.getLibraryInformation());

		tabItem.setControl(composite);
	}

	private void createTabColumnIndices(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Column Indices");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout());

		ColumnIndicesEditUI columnIndicesEditUI = new ColumnIndicesEditUI(composite, SWT.NONE);
		columnIndicesEditUI.update(massSpectrum.getLibraryInformation());

		tabItem.setControl(composite);
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

	private Text createTextCasNumber(Composite parent, String value) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText(value != null ? value : "");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		ControlDecoration controlDecoration = new ControlDecoration(text, SWT.LEFT | SWT.TOP);
		text.addModifyListener(e -> validate(casValidator, controlDecoration, text));

		return text;
	}

	private Text createTextMultiLine(Composite parent, String value) {

		Text text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		text.setText(value != null ? value : "");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 200;
		text.setLayoutData(gridData);

		return text;
	}

	private boolean validate(IValidator<String> validator, ControlDecoration controlDecoration, Text text) {

		IStatus status = validator.validate(text.getText().trim());
		if(status.isOK()) {
			controlDecoration.hide();
			return true;
		} else {
			controlDecoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
			controlDecoration.showHoverText(status.getMessage());
			controlDecoration.show();
			return false;
		}
	}
}