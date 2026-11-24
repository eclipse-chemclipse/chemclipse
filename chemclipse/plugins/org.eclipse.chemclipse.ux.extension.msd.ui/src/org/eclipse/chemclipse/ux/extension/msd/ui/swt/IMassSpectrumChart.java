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
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.support.updates.IUpdateListener;

public interface IMassSpectrumChart extends IUpdateListener {

	void update(IScanMSD massSpectrum);

	/**
	 * Rebuilds the processor menu, e.g. if the menu blacklist was modified.
	 */
	void refreshMenu();
}