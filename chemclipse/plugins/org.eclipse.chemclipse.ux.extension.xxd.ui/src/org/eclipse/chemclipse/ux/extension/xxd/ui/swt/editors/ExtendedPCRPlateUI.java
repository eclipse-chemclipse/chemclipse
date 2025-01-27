/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors;

import java.util.List;

import org.eclipse.chemclipse.pcr.model.core.IPlate;
import org.eclipse.chemclipse.pcr.model.core.IWell;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.swt.EnhancedCombo;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.PCRPlate;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class ExtendedPCRPlateUI extends Composite implements IExtendedPartUI {

	private Label labelDataInfo;
	private PCRPlate pcrPlate;
	private Combo comboSubsets;
	private Combo comboChannels;
	//
	private IPlate plate;

	public ExtendedPCRPlateUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void update(IPlate plate) {

		this.plate = plate;
		updateWidget();
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		createPlateUI(this);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(4, false));
		//
		createDataInfoLabel(composite);
		comboSubsets = createComboSubsets(composite);
		comboChannels = createComboChannels(composite);
		createResetButton(composite);
	}

	private void createDataInfoLabel(Composite parent) {

		labelDataInfo = new Label(parent, SWT.NONE);
		labelDataInfo.setText(""); //$NON-NLS-1$
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		labelDataInfo.setLayoutData(gridData);
	}

	private Combo createComboSubsets(Composite parent) {

		Combo combo = EnhancedCombo.create(parent, SWT.READ_ONLY);
		combo.setToolTipText(ExtensionMessages.subsetSelection);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String activeSubset = combo.getText();
				if(plate != null) {
					plate.setActiveSubset(activeSubset);
					fireUpdate(e.widget.getDisplay(), plate);
					fireUpdate(e.widget.getDisplay(), pcrPlate.getSelectedWell());
					pcrPlate.refresh();
				}
			}
		});
		//
		return combo;
	}

	private Combo createComboChannels(Composite parent) {

		Combo combo = EnhancedCombo.create(parent, SWT.READ_ONLY);
		combo.setToolTipText(ExtensionMessages.channelSpecification);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(plate != null) {
					plate.setActiveChannel(combo.getSelectionIndex());
					fireUpdate(e.widget.getDisplay(), plate);
					fireUpdate(e.widget.getDisplay(), pcrPlate.getSelectedWell());
					pcrPlate.refresh();
				}
			}
		});
		//
		return combo;
	}

	private void createResetButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText(ExtensionMessages.resetPlate);
		button.setText(""); //$NON-NLS-1$
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RESET, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				reset();
			}
		});
	}

	private void reset() {

		updateWidget();
	}

	private void createPlateUI(Composite parent) {

		pcrPlate = new PCRPlate(parent, SWT.BORDER);
		pcrPlate.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	private void fireUpdate(Display display, IWell well) {

		UpdateNotifierUI.update(display, IChemClipseEvents.TOPIC_WELL_PCR_UPDATE_SELECTION, well);
	}

	private void fireUpdate(Display display, IPlate plate) {

		UpdateNotifierUI.update(display, IChemClipseEvents.TOPIC_PLATE_PCR_UPDATE_SELECTION, plate);
	}

	private void updateWidget() {

		updateInfo();
		updateWellPositions();
		updateSubsetCombo();
		updateChannelSpecifications();
	}

	private void updateInfo() {

		if(plate != null) {
			labelDataInfo.setText(NLS.bind(ExtensionMessages.wells, plate.getWells().size()));
		} else {
			labelDataInfo.setText(""); //$NON-NLS-1$
		}
	}

	private void updateWellPositions() {

		if(plate != null) {
			pcrPlate.setPlate(plate);
		} else {
			pcrPlate.setPlate(null);
		}
	}

	private void updateSubsetCombo() {

		if(plate != null) {
			int selectionIndex = comboSubsets.getSelectionIndex();
			List<String> subsets = plate.getSampleSubsets();
			comboSubsets.setItems(subsets.toArray(new String[subsets.size()]));
			setComboSelection(subsets, comboSubsets, selectionIndex);
		} else {
			comboSubsets.setItems(""); //$NON-NLS-1$
		}
	}

	private void updateChannelSpecifications() {

		if(plate != null) {
			int selectionIndex = comboChannels.getSelectionIndex();
			List<String> channels = plate.getActiveChannels();
			comboChannels.setItems(channels.toArray(new String[channels.size()]));
			setComboSelection(channels, comboChannels, selectionIndex);
		} else {
			comboChannels.setItems(""); //$NON-NLS-1$
		}
	}

	private void setComboSelection(List<String> items, Combo combo, int selectionIndex) {

		/*
		 * Set the last selection.
		 */
		if(!items.isEmpty()) {
			if(selectionIndex < 0) {
				combo.select(0);
			} else {
				if(selectionIndex < items.size()) {
					combo.select(selectionIndex);
				}
			}
		}
	}
}
