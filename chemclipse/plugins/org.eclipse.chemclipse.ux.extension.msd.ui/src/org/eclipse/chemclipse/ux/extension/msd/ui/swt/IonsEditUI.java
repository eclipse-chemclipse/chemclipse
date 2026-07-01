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
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.ux.extension.msd.ui.dialogs.IonEditDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class IonsEditUI extends Composite {

	private AtomicReference<MassSpectrumChartCentroid> chartControl = new AtomicReference<>();
	private AtomicReference<Button> buttonAdd = new AtomicReference<>();
	private AtomicReference<Button> buttonEdit = new AtomicReference<>();
	private AtomicReference<Button> buttonDelete = new AtomicReference<>();
	private AtomicReference<IonListUI> listControl = new AtomicReference<>();

	private IScanMSD massSpectrum;

	public IonsEditUI(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	public void update(IScanMSD massSpectrum) {

		this.massSpectrum = massSpectrum;
		chartControl.get().update(massSpectrum);
		if(massSpectrum != null) {
			listControl.get().setInput(massSpectrum.getIons());
		} else {
			listControl.get().clear();
		}
		updateButtons();
	}

	private void initialize() {

		setLayout(new FillLayout());
		SashForm sashForm = new SashForm(this, SWT.VERTICAL);

		createChart(sashForm);
		createBottomSection(sashForm);

		sashForm.setWeights(60, 40);
	}

	private void createChart(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		MassSpectrumChartCentroid chart = new MassSpectrumChartCentroid(composite, SWT.NONE);
		chartControl.set(chart);
	}

	private void createBottomSection(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		createToolbarMain(composite);
		createTable(composite);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
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

				addIon();
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

				editIon();
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

				deleteIons();
			}
		});
		buttonDelete.set(button);
	}

	private void createTable(Composite parent) {

		IonListUI listUI = new IonListUI(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
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

	private void addIon() {

		if(massSpectrum != null) {
			IonEditDialog dialog = new IonEditDialog(getShell(), null);
			if(dialog.open() == Window.OK) {
				IIon ion = dialog.getIon();
				if(ion != null) {
					massSpectrum.addIon(ion);
					updateTableAndChart();
				}
			}
		}
	}

	private void editIon() {

		if(massSpectrum != null) {
			IStructuredSelection selection = listControl.get().getStructuredSelection();
			if(selection.size() == 1 && selection.getFirstElement() instanceof IIon existing) {
				IonEditDialog dialog = new IonEditDialog(getShell(), existing);
				if(dialog.open() == Window.OK) {
					IIon updated = dialog.getIon();
					if(updated != null) {
						massSpectrum.removeIon(existing);
						massSpectrum.addIon(updated);
						updateTableAndChart();
					}
				}
			}
		}
	}

	private void deleteIons() {

		if(massSpectrum != null) {
			IStructuredSelection selection = listControl.get().getStructuredSelection();
			for(Object object : selection.toList()) {
				if(object instanceof IIon ion) {
					massSpectrum.removeIon(ion);
				}
			}
			updateTableAndChart();
		}
	}

	private void updateTableAndChart() {

		listControl.get().setInput(massSpectrum.getIons());
		chartControl.get().update(massSpectrum);
		updateButtons();
	}

	private void updateButtons() {

		int selectionCount = listControl.get().getTable().getSelectionCount();
		buttonEdit.get().setEnabled(selectionCount == 1);
		buttonDelete.get().setEnabled(selectionCount > 0);
	}
}