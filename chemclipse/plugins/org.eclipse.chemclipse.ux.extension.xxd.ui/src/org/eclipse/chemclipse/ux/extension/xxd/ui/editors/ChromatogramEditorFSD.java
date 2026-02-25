/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.editors;

import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.ui.E4ProcessSupplierContext;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import jakarta.inject.Inject;

public class ChromatogramEditorFSD extends ChromatogramEditor {

	public static final String ID = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.chromatogramEditorFSD";
	public static final String CONTRIBUTION_URI = "bundleclass://org.eclipse.chemclipse.ux.extension.xxd.ui/org.eclipse.chemclipse.ux.extension.xxd.ui.editors.ChromatogramEditorFSD";
	public static final String ICON_URI = ApplicationImageFactory.getInstance().getURI(IApplicationImage.IMAGE_CHROMATOGRAM_WSD, IApplicationImageProvider.SIZE_16x16); // TODO

	@Inject
	public ChromatogramEditorFSD(Composite parent, MPart part, MDirtyable dirtyable, Shell shell, E4ProcessSupplierContext processSupplierContext) {

		super(DataType.FSD, parent, part, dirtyable, shell, processSupplierContext);
	}
}
