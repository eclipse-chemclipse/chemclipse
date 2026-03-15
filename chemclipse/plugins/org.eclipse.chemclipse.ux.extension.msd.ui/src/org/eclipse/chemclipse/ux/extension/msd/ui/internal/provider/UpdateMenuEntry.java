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
package org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider;

import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.ux.extension.msd.ui.swt.IMassSpectrumChart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.menu.AbstractChartMenuEntry;

public class UpdateMenuEntry extends AbstractChartMenuEntry {

	@Override
	public String getCategory() {

		return ICategories.MASS_SPECTRUM;
	}

	@Override
	public String getName() {

		return "Update";
	}

	@Override
	public Image getIcon() {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_REFRESH, IApplicationImageProvider.SIZE_16x16);
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		if(scrollableChart instanceof IMassSpectrumChart massSpectrumChart) {
			massSpectrumChart.update();
		}
	}
}
