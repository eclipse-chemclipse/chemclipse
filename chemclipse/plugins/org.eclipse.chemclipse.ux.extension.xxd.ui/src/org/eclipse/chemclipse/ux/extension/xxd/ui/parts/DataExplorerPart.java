/*******************************************************************************
 * Copyright (c) 2016, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - Move custom made toolbar to the native toolbar with E4Handlers
 * Philip Wenig - modularization
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import org.eclipse.chemclipse.processing.converter.ISupplierFileIdentifier;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.DataExplorerUI;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Composite;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

public class DataExplorerPart {

	@Inject
	private ISupplierFileIdentifier supplierFileIdentifier;
	//
	private DataExplorerUI dataExplorerUI;

	@PostConstruct
	public void init(Composite parent, MPart part) {

		dataExplorerUI = new DataExplorerUI(parent, supplierFileIdentifier);
	}

	public DataExplorerUI getDataExplorerUI() {

		return dataExplorerUI;
	}

	@Focus
	public void focus() {

		dataExplorerUI.setFocus();
	}
}