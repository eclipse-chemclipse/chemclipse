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
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.swt;

import java.util.Arrays;

import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IAnalysisSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d.FilterDistributionChart;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePage;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePageScorePlot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ExtendedFilterDistributionUI extends Composite implements IExtendedPartUI {

	private FilterDistributionChart plot;
	private IAnalysisSettings settings = null;

	public ExtendedFilterDistributionUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setInput(IAnalysisSettings settings) {

		this.settings = settings;
		updatePlot();
	}

	public void updatePlot() {

		plot.setInput(settings);
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));

		createToolbarMain(this);
		plot = createPlot(this);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));

		createSettingsButton(composite);
	}

	private FilterDistributionChart createPlot(Composite parent) {

		FilterDistributionChart plot = new FilterDistributionChart(parent, SWT.BORDER);
		plot.setLayoutData(new GridData(GridData.FILL_BOTH));
		return plot;
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePage.class, PreferencePageScorePlot.class), _ -> applySettings());
	}

	private void applySettings() {

		updatePlot();
	}
}
