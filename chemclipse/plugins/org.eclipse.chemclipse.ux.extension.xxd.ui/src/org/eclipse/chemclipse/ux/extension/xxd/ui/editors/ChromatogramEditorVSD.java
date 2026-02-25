/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.editors;

import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.ui.E4ProcessSupplierContext;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import jakarta.inject.Inject;

public class ChromatogramEditorVSD extends ChromatogramEditor {

	public static final String ID = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.chromatogramEditorVSD";
	public static final String CONTRIBUTION_URI = "bundleclass://org.eclipse.chemclipse.ux.extension.xxd.ui/org.eclipse.chemclipse.ux.extension.xxd.ui.editors.ChromatogramEditorVSD";

	@Inject
	public ChromatogramEditorVSD(Composite parent, MPart part, MDirtyable dirtyable, Shell shell, E4ProcessSupplierContext processSupplierContext) {

		super(DataType.VSD, parent, part, dirtyable, shell, processSupplierContext);
	}
}
