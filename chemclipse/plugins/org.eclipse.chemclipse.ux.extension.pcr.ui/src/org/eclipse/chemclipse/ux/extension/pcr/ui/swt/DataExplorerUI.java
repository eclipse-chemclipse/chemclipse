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
 * Christoph Läubrich - performance optimization and cleanup, refactor handling of Suppliers
 * Matthias Mailänder - right-click refresh option
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.ux.extension.pcr.ui.Activator;
import org.eclipse.chemclipse.ux.extension.pcr.ui.support.SupplierEditorSupport;
import org.eclipse.chemclipse.ux.extension.ui.model.DataExplorerTreeSettings;
import org.eclipse.chemclipse.ux.extension.ui.provider.ISupplierFileEditorSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.MultiDataExplorerTreeUI;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class DataExplorerUI extends MultiDataExplorerTreeUI {

	public DataExplorerUI(Composite parent) {

		super(parent, SWT.NONE, new DataExplorerTreeSettings(Activator.getDefault().getPreferenceStore()));
		setSupplierFileEditorSupport();
	}

	@Override
	protected void setSupplierFileEditorSupport() {

		IEclipseContext context = Activator.getDefault().getEclipseContext();
		List<ISupplierFileEditorSupport> editorSupportList = new ArrayList<>();
		editorSupportList.add(new SupplierEditorSupport(() -> context));
		setSupplierFileIdentifier(editorSupportList);
		expandLastDirectoryPath();
	}
}