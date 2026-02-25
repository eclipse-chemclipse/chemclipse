/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
import org.eclipse.chemclipse.processing.supplier.IProcessSupplierContext;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ChromatogramEditor extends AbstractChromatogramEditor {

	private final Composite parent;

	public ChromatogramEditor(DataType dataType, Composite parent, MPart part, MDirtyable dirtyable, Shell shell, IProcessSupplierContext processSupplierContext) {

		super(dataType, parent, part, dirtyable, processSupplierContext, shell);
		this.parent = parent;
	}

	@Override
	public void setFocus() {

		parent.setFocus();
	}
}