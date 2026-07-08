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

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MassSpectrumEditDialog extends TitleAreaDialog {

	private AtomicReference<Text> textIon = new AtomicReference<>();
	private AtomicReference<Text> textAbundance = new AtomicReference<>();

	private final IIon ion;
	private IIon result = null;

	public MassSpectrumEditDialog(Shell parentShell, IIon ion) {

		super(parentShell);
		this.ion = ion;
	}

	public IIon getIon() {

		return result;
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText(ion == null ? "Add Ion" : "Edit Ion");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("Ion");
		setMessage("Define the m/z value and abundance.", IMessageProvider.INFORMATION);
		Composite container = (Composite)super.createDialogArea(parent);

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createLabel(composite, "Ion (m/z):");
		textIon.set(createText(composite, ion != null ? Double.toString(ion.getIon()) : "0.0"));

		createLabel(composite, "Abundance:");
		textAbundance.set(createText(composite, ion != null ? Float.toString(ion.getAbundance()) : "0.0"));

		return container;
	}

	@Override
	protected void okPressed() {

		try {
			double ionValue = Double.parseDouble(textIon.get().getText().trim());
			float abundance = Float.parseFloat(textAbundance.get().getText().trim());
			result = new Ion(ionValue, abundance);
			super.okPressed();
		} catch(NumberFormatException e) {
			setErrorMessage("Ion (m/z) and Abundance must be valid numbers.");
		}
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	@Override
	protected Point getInitialSize() {

		return new Point(350, 250);
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
}