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
 * Lorenz Gerber - add sample highlighting
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.SeriesConverter;
import org.eclipse.swt.widgets.Composite;

public class ScorePlot extends AbstractPlotPCA {

	private final Map<ISample, IResultMVA> extractedResults = new HashMap<>();

	public ScorePlot(Composite parent, int style) {

		super(parent, style, StatisticsMessages.scorePlot);
	}

	public void setInput(EvaluationPCA evaluationPCA, int pcX, int pcY) {

		deleteSeries();
		if(evaluationPCA != null) {
			IResultsMVA resultsPCA = evaluationPCA.getResults();
			List<ISample> highlightedSamples = evaluationPCA.getHighlightedSamples();
			update(pcX, pcY, evaluationPCA.getResults().getExplainedVariances());
			addSeriesData(SeriesConverter.sampleToSeries(resultsPCA, highlightedSamples, pcX, pcY, extractedResults));
		} else {
			getBaseChart().redraw();
		}
	}
}
