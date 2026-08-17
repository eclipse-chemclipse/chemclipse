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
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.quantitation.IQuantitationCompound;
import org.eclipse.chemclipse.model.quantitation.IQuantitationPeak;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.swt.ui.support.IColorScheme;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.charts.PeaksChart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePagePeaksAxes;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.PeakChartSupport;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.extensions.linecharts.ICompressionSupport;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;

public class QuantPeaksChartUI extends Composite implements IExtendedPartUI {

	private IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	private PeakChartSupport peakChartSupport = new PeakChartSupport();
	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.000");

	private AtomicReference<PeaksChart> chartControl = new AtomicReference<>();
	private IQuantitationCompound quantitationCompound;

	public QuantPeaksChartUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void update(IQuantitationCompound quantitationCompound) {

		this.quantitationCompound = quantitationCompound;
		setQuantitationCompound();
	}

	private void createControl() {

		setLayout(new FillLayout());

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));

		createToolbarMain(composite);
		createPeaksChart(composite);

		initialize();
	}

	private void initialize() {

	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(3, false));

		createButtonToggleChartLegend(composite, chartControl, IMAGE_LEGEND);
		createResetButton(composite);
		createSettingsButton(composite);
	}

	private void createResetButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the chart");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RESET, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				reset();
			}
		});
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePagePeaksAxes.class), _ -> applySettings());
	}

	private void createPeaksChart(Composite parent) {

		PeaksChart peaksChart = new PeaksChart(parent, SWT.NONE);
		peaksChart.setLayoutData(new GridData(GridData.FILL_BOTH));

		chartControl.set(peaksChart);
	}

	private void applySettings() {

		chartControl.get().modifyAxes(true);
		setQuantitationCompound();
	}

	private void reset() {

		setQuantitationCompound();
	}

	private void setQuantitationCompound() {

		PeaksChart peaksChart = chartControl.get();
		peaksChart.deleteSeries();
		if(quantitationCompound != null) {
			List<ILineSeriesData> lineSeriesDataList = new ArrayList<>();

			int counter = 1;
			IColorScheme colors = Colors.getColorScheme(preferenceStore.getString(PreferenceSupplier.P_COLOR_SCHEME_DISPLAY_PEAKS));
			boolean enableArea = preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_AREA_DISPLAY_PEAKS);

			for(Object object : quantitationCompound.getQuantitationPeaks()) {
				if(object instanceof IQuantitationPeak quantitationPeak) {

					StringBuilder builder = new StringBuilder();
					builder.append("P");
					builder.append(counter);
					builder.append(" (");
					builder.append(decimalFormat.format(quantitationPeak.getConcentration()));
					builder.append(" ");
					builder.append(quantitationPeak.getConcentrationUnit());
					builder.append(")");
					IPeak peak = quantitationPeak.getReferencePeak();
					ILineSeriesData lineSeriesData = peakChartSupport.getPeak(peak, false, false, colors.getColor(), builder.toString());
					ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
					lineSeriesSettings.setEnableArea(enableArea);
					lineSeriesDataList.add(lineSeriesData);

					counter++;
					colors.incrementColor();
				}
			}

			peaksChart.addSeriesData(lineSeriesDataList, ICompressionSupport.NO_COMPRESSION);
		}
	}
}
