/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - reuse the DataExplorerUI
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.views;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.ux.extension.msd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.msd.ui.support.MassSpectrumSupport;
import org.eclipse.chemclipse.ux.extension.ui.model.DataExplorerTreeSettings;
import org.eclipse.chemclipse.ux.extension.ui.swt.MultiDataExplorerTreeUI;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class MassSpectrumFileExplorer {

	private AtomicReference<MultiDataExplorerTreeUI> dataExplorerControl = new AtomicReference<>();

	@Inject
	public MassSpectrumFileExplorer(Composite parent) {

		MultiDataExplorerTreeUI explorerUI = new MultiDataExplorerTreeUI(parent, SWT.NONE, new DataExplorerTreeSettings(Activator.getDefault().getPreferenceStore()));
		explorerUI.setSupplierFileIdentifier(Collections.singleton(MassSpectrumSupport.getInstanceEditorSupport()));
		explorerUI.expandLastDirectoryPath();
		//
		dataExplorerControl.set(explorerUI);
	}

	@Focus
	private void setFocus() {

		dataExplorerControl.get().setFocus();
	}
}