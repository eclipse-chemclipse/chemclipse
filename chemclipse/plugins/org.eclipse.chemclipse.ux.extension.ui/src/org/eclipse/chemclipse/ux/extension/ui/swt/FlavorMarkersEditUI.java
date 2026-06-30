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
package org.eclipse.chemclipse.ux.extension.ui.swt;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.identifier.IFlavorMarker;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.ux.extension.ui.dialogs.FlavorMarkerEditDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class FlavorMarkersEditUI extends Composite {

	private AtomicReference<Button> buttonAdd = new AtomicReference<>();
	private AtomicReference<Button> buttonEdit = new AtomicReference<>();
	private AtomicReference<Button> buttonDelete = new AtomicReference<>();
	private AtomicReference<FlavorMarkerListUI> listControl = new AtomicReference<>();

	private ILibraryInformation libraryInformation;

	public FlavorMarkersEditUI(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	public void update(ILibraryInformation libraryInformation) {

		this.libraryInformation = libraryInformation;
		if(libraryInformation != null) {
			listControl.get().setInput(libraryInformation.getFlavorMarkers());
		} else {
			listControl.get().clear();
		}
		updateButtons();
	}

	private void initialize() {

		setLayout(new FillLayout());
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		createToolbarMain(composite);
		createTable(composite);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(3, false));

		createButtonAdd(composite);
		createButtonEdit(composite);
		createButtonDelete(composite);
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				addFlavorMarker();
			}
		});
		buttonAdd.set(button);
	}

	private void createButtonEdit(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setEnabled(false);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				editFlavorMarker();
			}
		});
		buttonEdit.set(button);
	}

	private void createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setEnabled(false);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteFlavorMarkers();
			}
		});
		buttonDelete.set(button);
	}

	private void createTable(Composite parent) {

		FlavorMarkerListUI listUI = new FlavorMarkerListUI(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		Table table = listUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateButtons();
			}
		});
		listControl.set(listUI);
	}

	private void addFlavorMarker() {

		if(libraryInformation != null) {
			FlavorMarkerEditDialog dialog = new FlavorMarkerEditDialog(getShell(), null);
			if(dialog.open() == Window.OK) {
				IFlavorMarker marker = dialog.getFlavorMarker();
				if(marker != null) {
					libraryInformation.add(marker);
					listControl.get().setInput(libraryInformation.getFlavorMarkers());
				}
			}
		}
	}

	private void editFlavorMarker() {

		if(libraryInformation != null) {
			IStructuredSelection selection = listControl.get().getStructuredSelection();
			if(selection.size() == 1 && selection.getFirstElement() instanceof IFlavorMarker existing) {
				FlavorMarkerEditDialog dialog = new FlavorMarkerEditDialog(getShell(), existing);
				if(dialog.open() == Window.OK) {
					IFlavorMarker updated = dialog.getFlavorMarker();
					if(updated != null) {
						libraryInformation.delete(existing);
						libraryInformation.add(updated);
						listControl.get().setInput(libraryInformation.getFlavorMarkers());
					}
				}
			}
		}
	}

	private void deleteFlavorMarkers() {

		if(libraryInformation != null) {
			IStructuredSelection selection = listControl.get().getStructuredSelection();
			for(Object object : selection.toList()) {
				if(object instanceof IFlavorMarker marker) {
					libraryInformation.delete(marker);
				}
			}
			listControl.get().setInput(libraryInformation.getFlavorMarkers());
			updateButtons();
		}
	}

	private void updateButtons() {

		int selectionCount = listControl.get().getTable().getSelectionCount();
		buttonEdit.get().setEnabled(selectionCount == 1);
		buttonDelete.get().setEnabled(selectionCount > 0);
	}
}