/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class MassSpectraSelectionUI extends Composite {

	private AtomicReference<Button> buttonPreviousControl = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboViewerControl = new AtomicReference<>();
	private AtomicReference<Button> buttonNextControl = new AtomicReference<>();
	private AtomicReference<Button> buttonRemoveControl = new AtomicReference<>();
	private AtomicReference<Button> buttonRefreshControl = new AtomicReference<>();
	//
	private IMassSpectra massSpectra;

	public MassSpectraSelectionUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void update(IMassSpectra massSpectra) {

		this.massSpectra = massSpectra;
		//
		ComboViewer comboViewer = comboViewerControl.get();
		comboViewer.setInput(massSpectra.getList());
		comboViewer.getCombo().select(0);
		updateButtons();
	}

	public void addSelectionChangeListener(ISelectionChangedListener selectionChangeListener) {

		ComboViewer comboViewer = comboViewerControl.get();
		comboViewer.addSelectionChangedListener(selectionChangeListener);
	}

	public void updateInput() {

		updateButtons();
	}

	private void createControl() {

		setLayout(new FillLayout());
		//
		Composite composite = new Composite(this, SWT.NONE);
		GridLayout gridLayout = new GridLayout(9, false);
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		composite.setLayout(gridLayout);
		//
		buttonPreviousControl.set(createButtonSelectPreviousMassSpectrum(composite));
		createComboChromatograms(composite);
		buttonNextControl.set(createButtonSelectNextMassSpectrum(composite));
		buttonRefreshControl.set(createButtonRefresh(composite));
		buttonRemoveControl.set(createButtonRemoveMassSpectrum(composite));
	}

	private void createComboChromatograms(Composite parent) {

		ComboViewer comboViewer = new EnhancedComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
					String identifier = standaloneMassSpectrum.getIdentifier();
					if(identifier != null && !identifier.isEmpty()) {
						return identifier;
					} else {
						return standaloneMassSpectrum.getName();
					}
				}
				return "n./a.";
			}
		});
		combo.setToolTipText("Select a mass spectrum.");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comboViewerControl.set(comboViewer);
	}

	private Button createButtonSelectPreviousMassSpectrum(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Select previous mass spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ARROW_BACKWARD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateComboViewerSelection(-1);
				updateButtons();
			}
		});
		return button;
	}

	private Button createButtonSelectNextMassSpectrum(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Select next mass spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ARROW_FORWARD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateComboViewerSelection(+1);
				updateButtons();
			}
		});
		return button;
	}

	private void updateComboViewerSelection(int step) {

		ComboViewer comboViewer = comboViewerControl.get();
		if(comboViewer != null) {
			Combo combo = comboViewer.getCombo();
			int selectionIndex = comboViewer.getCombo().getSelectionIndex();
			combo.select(selectionIndex + step);
		}
	}

	private Button createButtonRemoveMassSpectrum(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Remove the mass spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				ComboViewer comboViewer = comboViewerControl.get();
				int selectionIndex = comboViewer.getCombo().getSelectionIndex();
				IScanMSD massSpectrum = massSpectra.getMassSpectrum(selectionIndex + 1);
				massSpectra.removeMassSpectrum(massSpectrum);
				comboViewer.remove(massSpectrum);
				updateButtons();
			}
		});
		return button;
	}

	private Button createButtonRefresh(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Refresh the mass spectra selection.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_REFRESH, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				update();
			}
		});
		return button;
	}

	private void updateButtons() {

		ComboViewer comboViewer = comboViewerControl.get();
		int selectionIndex = comboViewer.getCombo().getSelectionIndex();
		int size = massSpectra.size();
		buttonNextControl.get().setEnabled(size > 1 && selectionIndex < size - 1);
		buttonPreviousControl.get().setEnabled(size > 1 && selectionIndex > 0);
		buttonRemoveControl.get().setEnabled(size > 0);
		buttonRefreshControl.get().setEnabled(true);
	}
}