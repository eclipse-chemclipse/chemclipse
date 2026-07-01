/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
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

public class SynonymsEditUI extends Composite {

	private AtomicReference<Text> textSynonym = new AtomicReference<>();
	private AtomicReference<Button> buttonAdd = new AtomicReference<>();
	private AtomicReference<Button> buttonDelete = new AtomicReference<>();
	private AtomicReference<SynonymsListUI> synonymsListUI = new AtomicReference<>();

	private ILibraryInformation libraryInformation;

	public SynonymsEditUI(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	public void update(ILibraryInformation libraryInformation) {

		this.libraryInformation = libraryInformation;
		if(libraryInformation != null) {
			synonymsListUI.get().setInput(libraryInformation.getSynonyms());
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

		createTextSynonym(composite);
		createButtonAdd(composite);
		createButtonDelete(composite);
	}

	private void createTextSynonym(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addTraverseListener(e -> {

			if(e.detail == SWT.TRAVERSE_RETURN) {
				addSynonym();
				e.doit = false;
			}
		});
		textSynonym.set(text);
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				addSynonym();
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

				deleteSynonyms();
			}
		});
		buttonDelete.set(button);
	}

	private void createTable(Composite parent) {

		SynonymsListUI listUI = new SynonymsListUI(parent, SWT.BORDER | SWT.MULTI);
		listUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		listUI.getTable().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				buttonDelete.get().setEnabled(listUI.getTable().getSelectionIndex() >= 0);
			}
		});
		synonymsListUI.set(listUI);
	}

	private void addSynonym() {

		if(libraryInformation != null) {
			String synonym = textSynonym.get().getText().trim();
			if(!synonym.isEmpty() && !libraryInformation.getSynonyms().contains(synonym)) {
				libraryInformation.getSynonyms().add(synonym);
				textSynonym.get().setText("");
				synonymsListUI.get().update(libraryInformation);
			}
		}
	}

	private void deleteSynonyms() {

		if(libraryInformation != null) {
			Table table = synonymsListUI.get().getTable();
			for(TableItem tableItem : table.getSelection()) {
				Object object = tableItem.getData();
				if(object instanceof String synonym) {
					libraryInformation.getSynonyms().remove(synonym);
				}
			}
			synonymsListUI.get().update(libraryInformation);
			buttonDelete.get().setEnabled(false);
		}
	}
}