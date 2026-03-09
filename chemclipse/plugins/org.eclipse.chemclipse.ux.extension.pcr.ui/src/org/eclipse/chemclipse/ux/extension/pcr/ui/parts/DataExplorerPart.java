/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - Move custom made toolbar to the native toolbar with E4Handlers
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui.parts;

import org.eclipse.chemclipse.ux.extension.pcr.ui.swt.DataExplorerUI;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.widgets.Composite;

import jakarta.annotation.PostConstruct;

public class DataExplorerPart {

	private DataExplorerUI dataExplorerUI;

	@PostConstruct
	public void init(Composite parent) {

		dataExplorerUI = new DataExplorerUI(parent);
	}

	public DataExplorerUI getDataExplorerUI() {

		return dataExplorerUI;
	}

	@Focus
	public void focus() {

		dataExplorerUI.setFocus();
	}
}
