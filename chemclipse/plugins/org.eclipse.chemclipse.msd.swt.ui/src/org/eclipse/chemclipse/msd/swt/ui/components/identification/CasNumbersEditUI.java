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
package org.eclipse.chemclipse.msd.swt.ui.components.identification;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.cas.CasValidator;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class CasNumbersEditUI extends Composite {

	private final CasValidator casValidator = new CasValidator(true);

	private AtomicReference<Text> textCasNumber = new AtomicReference<>();
	private AtomicReference<Button> buttonAdd = new AtomicReference<>();
	private AtomicReference<Button> buttonDelete = new AtomicReference<>();
	private AtomicReference<CasNumbersListUI> casNumbersListUI = new AtomicReference<>();

	private ILibraryInformation libraryInformation;

	public CasNumbersEditUI(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	public void update(ILibraryInformation libraryInformation) {

		this.libraryInformation = libraryInformation;
		if(libraryInformation != null) {
			casNumbersListUI.get().setInput(libraryInformation.getCasNumbers());
		}
	}

	private void initialize() {

		setLayout(new FillLayout());
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		createToolbar(composite);
		createTable(composite);
	}

	private void createToolbar(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(3, false));

		createTextCasNumber(composite);
		createButtonAdd(composite);
		createButtonDelete(composite);
	}

	private void createTextCasNumber(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		ControlDecoration controlDecoration = new ControlDecoration(text, SWT.LEFT | SWT.TOP);
		controlDecoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
		controlDecoration.hide();

		text.addModifyListener(e -> {
			IStatus status = casValidator.validate(text.getText().trim());
			if(status.isOK()) {
				controlDecoration.hide();
			} else {
				controlDecoration.showHoverText(status.getMessage());
				controlDecoration.show();
			}
		});
		text.addTraverseListener(e -> {

			if(e.detail == SWT.TRAVERSE_RETURN) {
				addCasNumber();
				e.doit = false;
			}
		});
		textCasNumber.set(text);
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				addCasNumber();
			}
		});
		buttonAdd.set(button);
	}

	private void createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setEnabled(false);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteCasNumbers();
			}
		});
		buttonDelete.set(button);
	}

	private void createTable(Composite parent) {

		CasNumbersListUI listUI = new CasNumbersListUI(parent, SWT.BORDER | SWT.MULTI);
		listUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		listUI.getTable().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				buttonDelete.get().setEnabled(listUI.getTable().getSelectionIndex() >= 0);
			}
		});
		casNumbersListUI.set(listUI);
	}

	private void addCasNumber() {

		if(libraryInformation != null) {
			String casNumber = textCasNumber.get().getText().trim();
			if(!casNumber.isEmpty() && casValidator.validate(casNumber).isOK()) {
				libraryInformation.addCasNumber(casNumber);
				textCasNumber.get().setText("");
				casNumbersListUI.get().update(libraryInformation);
			}
		}
	}

	private void deleteCasNumbers() {

		if(libraryInformation != null) {
			Table table = casNumbersListUI.get().getTable();
			for(TableItem tableItem : table.getSelection()) {
				Object object = tableItem.getData();
				if(object instanceof String casNumber) {
					libraryInformation.deleteCasNumber(casNumber);
				}
			}
			casNumbersListUI.get().update(libraryInformation);
			buttonDelete.get().setEnabled(false);
		}
	}
}