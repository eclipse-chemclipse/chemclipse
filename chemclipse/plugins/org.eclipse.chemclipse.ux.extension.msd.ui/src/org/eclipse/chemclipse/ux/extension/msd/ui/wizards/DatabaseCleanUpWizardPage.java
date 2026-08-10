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
package org.eclipse.chemclipse.ux.extension.msd.ui.wizards;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.ux.extension.msd.ui.swt.DatabaseCleanUpTableUI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class DatabaseCleanUpWizardPage extends WizardPage {

	private final List<DatabaseEntryClean> entries;
	private AtomicReference<DatabaseCleanUpTableUI> tableControl = new AtomicReference<>();

	public DatabaseCleanUpWizardPage(List<DatabaseEntryClean> entries) {

		super("DatabaseCleanUpWizardPage");
		setTitle("Clean Up Mass Spectrum Library");
		setDescription("The following invalid CAS numbers were found. Select and delete entries you want to keep, then click Finish to remove the remaining ones.");
		this.entries = entries;
	}

	public List<DatabaseEntryClean> getEntries() {

		return entries;
	}

	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		createToolbarMain(composite);
		createTable(composite);

		setControl(composite);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));

		createButtonDeleteEntries(composite);
	}

	private void createButtonDeleteEntries(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Delete the selected entries.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteSelectedEntries(tableControl.get());
			}
		});
	}

	private void createTable(Composite parent) {

		DatabaseCleanUpTableUI tableUI = new DatabaseCleanUpTableUI(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tableUI.getTable().addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if(e.keyCode == SWT.DEL) {
					deleteSelectedEntries(tableUI);
				}
			}
		});
		tableUI.setInput(entries);
		tableControl.set(tableUI);
	}

	private void deleteSelectedEntries(DatabaseCleanUpTableUI tableUI) {

		if(tableUI != null) {
			IStructuredSelection selection = tableUI.getStructuredSelection();
			for(Object obj : selection.toList()) {
				if(obj instanceof DatabaseEntryClean entry) {
					entries.remove(entry);
				}
			}
			tableUI.setInput(entries);
		}
	}
}