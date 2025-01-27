/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.model;

import org.eclipse.chemclipse.ux.extension.ui.swt.DataExplorerTreeRoot;
import org.eclipse.jface.preference.IPreferenceStore;

public class DataExplorerTreeSettings {

	private IPreferenceStore preferenceStore = null;
	private DataExplorerTreeRoot[] dataExplorerTreeRoots = null;

	public DataExplorerTreeSettings(IPreferenceStore preferenceStore) {

		this(preferenceStore, DataExplorerTreeRoot.getDefaultRoots());
	}

	public DataExplorerTreeSettings(IPreferenceStore preferenceStore, DataExplorerTreeRoot[] dataExplorerTreeRoots) {

		this.preferenceStore = preferenceStore;
		this.dataExplorerTreeRoots = dataExplorerTreeRoots;
	}

	public IPreferenceStore getPreferenceStore() {

		return preferenceStore;
	}

	public DataExplorerTreeRoot[] getDataExplorerTreeRoots() {

		return dataExplorerTreeRoots;
	}
}