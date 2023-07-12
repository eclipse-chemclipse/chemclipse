/*******************************************************************************
 * Copyright (c) 2022, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.columns.ISeparationColumnIndices;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.calibration.RetentionIndexUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ChromatogramDataSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.preferences.PreferencePage;

public class ExtendedChromatogramIndicesUI extends Composite implements IExtendedPartUI {

	private AtomicReference<Button> buttonToolbarInfo = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfo = new AtomicReference<>();
	private AtomicReference<RetentionIndexUI> retentionIndexControl = new AtomicReference<>();
	//
	private IChromatogramSelection<?, ?> chromatogramSelection = null;

	public ExtendedChromatogramIndicesUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setInput(IChromatogramSelection<?, ?> chromatogramSelection) {

		this.chromatogramSelection = chromatogramSelection;
		updateInput();
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		createToolbarInfo(this);
		createRetentionIndexUI(this);
		//
		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarInfo, buttonToolbarInfo.get(), IMAGE_INFO, TOOLTIP_INFO, true);
		retentionIndexControl.get().setSearchVisibility(false);
		retentionIndexControl.get().setEditVisibility(false);
	}

	private Composite createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(5, false));
		//
		createButtonToggleToolbarInfo(composite);
		createButtonToggleToolbarSearch(composite);
		createButtonToggleToolbarEdit(composite);
		createButtonHelp(composite);
		createButtonSettings(composite);
		//
		return composite;
	}

	private void createButtonToggleToolbarInfo(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarInfo, IMAGE_INFO, TOOLTIP_INFO);
		buttonToolbarInfo.set(button);
	}

	private void createButtonToggleToolbarSearch(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setText("");
		setButtonImage(button, IMAGE_SEARCH, PREFIX_ENABLE, PREFIX_DISABLE, TOOLTIP_SEARCH, false);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean enabled = retentionIndexControl.get().toggleSearchVisibility();
				setButtonImage(button, IMAGE_SEARCH, PREFIX_ENABLE, PREFIX_DISABLE, TOOLTIP_SEARCH, enabled);
			}
		});
	}

	private void createButtonToggleToolbarEdit(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setText("");
		setButtonImage(button, IMAGE_EDIT, PREFIX_ENABLE, PREFIX_DISABLE, TOOLTIP_EDIT, false);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean enabled = retentionIndexControl.get().toggleEditVisibility();
				setButtonImage(button, IMAGE_EDIT, PREFIX_ENABLE, PREFIX_DISABLE, TOOLTIP_EDIT, enabled);
			}
		});
	}

	private void createToolbarInfo(Composite parent) {

		InformationUI informationUI = new InformationUI(parent, SWT.NONE);
		informationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		toolbarInfo.set(informationUI);
	}

	private void createRetentionIndexUI(Composite parent) {

		RetentionIndexUI retentionIndexUI = new RetentionIndexUI(parent, SWT.NONE);
		retentionIndexUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		retentionIndexControl.set(retentionIndexUI);
	}

	private void createButtonSettings(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePage.class), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				applySettings();
			}
		});
	}

	private void applySettings() {

		updateInput();
	}

	private void updateInput() {

		String info = "--";
		ISeparationColumnIndices separationColumnIndices = null;
		//
		if(chromatogramSelection != null) {
			IChromatogram<?> chromatogram = chromatogramSelection.getChromatogram();
			info = ChromatogramDataSupport.getChromatogramLabel(chromatogram);
			separationColumnIndices = chromatogram.getSeparationColumnIndices();
		}
		//
		toolbarInfo.get().setText(info);
		retentionIndexControl.get().setInput(separationColumnIndices);
	}
}