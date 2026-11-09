/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - getting rid of JavaFX
 * Lorenz Gerber - add highlight option
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d;

import java.util.List;

import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.SeriesConverter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesData;

public class LoadingsPlot extends AbstractPlotPCA {

	public static final int LABEL_RETENTION_TIME_MINUTES = 1;
	public static final int LABEL_DESCRIPTION = 2;

	private int labelType = LABEL_RETENTION_TIME_MINUTES;

	public LoadingsPlot(Composite parent, int style) {

		super(parent, style, StatisticsMessages.loadingsPlot);
	}

	public int getLabelsType() {

		return labelType;
	}

	public void setLabelsType(int labelsType) {

		if(labelsType == LABEL_DESCRIPTION || labelsType == LABEL_RETENTION_TIME_MINUTES) {
			this.labelType = labelsType;
		}
	}

	public void setInput(EvaluationPCA evaluationPCA, int pcX, int pcY) {

		deleteSeries();
		if(evaluationPCA != null) {
			IResultsMVA resultsPCA = evaluationPCA.getResults();
			List<IScatterSeriesData> series;
			if(labelType == LABEL_RETENTION_TIME_MINUTES) {
				series = SeriesConverter.basisVectorsToSeries(resultsPCA, evaluationPCA.getHighlightedVariables(), pcX, pcY);
			} else {
				series = SeriesConverter.basisVectorsToSeriesDescription(resultsPCA, pcX, pcY);
			}
			update(pcX, pcY, evaluationPCA.getResults().getExplainedVariances());

			addSeriesData(series);
		} else {
			getBaseChart().redraw();
		}
	}
}
