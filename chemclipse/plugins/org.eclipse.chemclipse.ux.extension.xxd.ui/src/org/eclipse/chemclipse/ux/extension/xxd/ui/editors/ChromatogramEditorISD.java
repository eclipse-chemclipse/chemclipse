/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.editors;

import javax.inject.Inject;

import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.ui.E4ProcessSupplierContext;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ChromatogramEditorISD extends ChromatogramEditor {

	public static final String ID = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.chromatogramEditorISD";
	public static final String CONTRIBUTION_URI = "bundleclass://org.eclipse.chemclipse.ux.extension.xxd.ui/org.eclipse.chemclipse.ux.extension.xxd.ui.editors.ChromatogramEditorISD";

	@Inject
	public ChromatogramEditorISD(Composite parent, MPart part, MDirtyable dirtyable, Shell shell, E4ProcessSupplierContext filterFactory, IEclipseContext eclipseContext) {

		super(DataType.ISD, parent, part, dirtyable, shell, filterFactory, eclipseContext);
	}
}