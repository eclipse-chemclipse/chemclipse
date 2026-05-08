/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d;

import java.util.List;

import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.SeriesConverter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesData;

public class SPlot extends AbstractPlotPCA {

	public static final int LABEL_RETENTION_TIME_MINUTES = 1;
	public static final int LABEL_DESCRIPTION = 2;

	private int labelType = LABEL_RETENTION_TIME_MINUTES;

	public SPlot(Composite parent, int style) {

		super(parent, style, "S Plot");
	}

	public int getLabelsType() {

		return labelType;
	}

	public void setLabelsType(int labelsType) {

		if(labelsType == LABEL_DESCRIPTION || labelsType == LABEL_RETENTION_TIME_MINUTES) {
			this.labelType = labelsType;
		}
	}

	public void setInput(EvaluationPCA evaluationPCA) {

		deleteSeries();
		if(evaluationPCA != null) {
			IResultsMVA results = evaluationPCA.getResults();
			if(results == null || results.getPCovarianceValues() == null || results.getPCorrValues() == null) {
				getBaseChart().redraw();
				return;
			}

			List<IScatterSeriesData> series;
			if(labelType == LABEL_RETENTION_TIME_MINUTES) {
				series = SeriesConverter.sPlotToSeries(results, evaluationPCA.getHighlightedVariables());
			} else {
				series = SeriesConverter.sPlotToSeriesDescription(results, evaluationPCA.getHighlightedVariables());
			}

			getChartSettings().getPrimaryAxisSettingsX().setTitle("p");
			getChartSettings().getPrimaryAxisSettingsY().setTitle("p(corr)");
			applySettings(getChartSettings());

			addSeriesData(series);
		} else {
			getBaseChart().redraw();
		}
	}
}
