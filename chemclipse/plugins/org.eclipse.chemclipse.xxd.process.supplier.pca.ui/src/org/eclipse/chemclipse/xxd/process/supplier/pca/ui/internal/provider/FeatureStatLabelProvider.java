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
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.eclipse.chemclipse.model.statistics.ISampleData;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.eclipse.swt.graphics.Image;

public class FeatureStatLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String VARIABLE = StatisticsMessages.variable;
	public static final String NAME = StatisticsMessages.name;
	public static final String COUNT = StatisticsMessages.count;
	public static final String MEAN = StatisticsMessages.mean;
	public static final String MIN = StatisticsMessages.min;
	public static final String MAX = StatisticsMessages.max;
	public static final String RELATIVESTDDEV = "RSD"; //$NON-NLS-1$
	public static final String SKEWNESS = StatisticsMessages.skewness;
	public static final String KURTOSIS = StatisticsMessages.excessKurtosis;

	private DecimalFormat decimalFormat = getDecimalFormat();

	public static final String[] TITLES = {//
			VARIABLE, //
			NAME, //
			COUNT, //
			MEAN, //
			MIN, //
			MAX, //
			RELATIVESTDDEV, //
			SKEWNESS, //
			KURTOSIS, //
	};
	public static final int[] BOUNDS = {//
			50, //
			280, //
			50, //
			140, //
			140, //
			140, //
			100, //
			100, //
			100 //
	};

	public FeatureStatLabelProvider(String pattern) {

		super(pattern);
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = ""; //$NON-NLS-1$
		if(element instanceof Feature feature) {
			IVariable variable = feature.getVariable();
			List<ISampleData<?>> sampleData = feature.getSampleData();
			DescriptiveStatistics stats = new DescriptiveStatistics();
			for(int i = 0; i < sampleData.size(); i++) {
				if(!Double.isNaN(sampleData.get(i).getData())) {
					stats.addValue(sampleData.get(i).getData());
				}

			}
			double value;

			switch(columnIndex) {
				case 0:
					text = variable.getValue();
					break;
				case 1:
					text = variable.getDescription();
					break;
				case 2:
					value = stats.getN();
					text = Double.isNaN(value) ? "NaN" : decimalFormat.format(value); //$NON-NLS-1$
					break;
				case 3:
					value = stats.getMean();
					text = Double.isNaN(value) ? "NaN" : decimalFormat.format(value); //$NON-NLS-1$
					break;
				case 4:
					value = stats.getMin();
					text = Double.isNaN(value) ? "NaN" : decimalFormat.format(value); //$NON-NLS-1$
					break;
				case 5:
					value = stats.getMax();
					text = Double.isNaN(value) ? "NaN" : decimalFormat.format(value); //$NON-NLS-1$
					break;
				case 6:
					value = 100.0 / stats.getSum() * stats.getStandardDeviation();
					text = Double.isNaN(value) ? "NaN" : decimalFormat.format(value); //$NON-NLS-1$
					break;
				case 7:
					value = stats.getSkewness();
					text = Double.isNaN(value) ? "NaN" : decimalFormat.format(value); //$NON-NLS-1$
					break;
				case 8:
					value = stats.getKurtosis() - 3.0;
					text = Double.isNaN(value) ? "NaN" : decimalFormat.format(value); //$NON-NLS-1$
					break;
				default:
					break;
			}
		}
		return text;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		return null;
	}
}
