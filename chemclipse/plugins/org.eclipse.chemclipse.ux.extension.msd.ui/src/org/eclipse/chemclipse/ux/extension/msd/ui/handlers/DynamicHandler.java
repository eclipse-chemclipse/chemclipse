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
package org.eclipse.chemclipse.ux.extension.msd.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class DynamicHandler extends AbstractHandler {

	private IChartMenuEntry cachedEntry;
	private ScrollableChart scrollableChart;

	public DynamicHandler(IChartMenuEntry cachedEntry, ScrollableChart scrollableChart) {

		this.cachedEntry = cachedEntry;
		this.scrollableChart = scrollableChart;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		cachedEntry.execute(scrollableChart.getShell(), scrollableChart);
		return null;
	}

	@Override
	public boolean isEnabled() {

		return cachedEntry.isEnabled(scrollableChart);
	}

}
