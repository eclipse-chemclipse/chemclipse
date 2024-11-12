/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.SeriesConverter;
import org.eclipse.swt.widgets.Composite;

public class ScorePlot extends AbtractPlotPCA {

	private final Map<String, IResultPCA> extractedResults = new HashMap<>();

	public ScorePlot(Composite parent, int style) {

		super(parent, style, "Score Plot");
	}

	public void setInput(EvaluationPCA evaluationPCA, int pcX, int pcY) {

		deleteSeries();
		if(evaluationPCA != null) {
			IResultsPCA<? extends IResultPCA, ?> resultsPCA = evaluationPCA.getResults();
			List<ISample> highlightedSamples = evaluationPCA.getHighlightedSamples();
			addSeriesData(SeriesConverter.sampleToSeries(resultsPCA, highlightedSamples, pcX, pcY, extractedResults));
			update(pcX, pcY, resultsPCA.getExplainedVariances());
		}
		redraw();
	}
}
