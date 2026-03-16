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

import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.eclipse.chemclipse.model.statistics.ISampleData;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.jface.viewers.Viewer;

public class FeatureStatComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof Feature feature1 && e2 instanceof Feature feature2) {
			IVariable variable1 = feature1.getVariable();
			IVariable variable2 = feature2.getVariable();
			DescriptiveStatistics stats = new DescriptiveStatistics();
			List<ISampleData<?>> sampleData1 = feature1.getSampleData();
			List<ISampleData<?>> sampleData2 = feature2.getSampleData();

			int columnIndex = getPropertyIndex();
			switch(columnIndex) {
				case 0:
					try {
						double value1 = Double.parseDouble(variable1.getValue());
						double value2 = Double.parseDouble(variable2.getValue());
						sortOrder = Double.compare(value2, value1);
					} catch(Exception e) {
						sortOrder = variable2.getValue().compareTo(variable1.getValue());
					}
					break;
				case 1:
					try {
						double value1 = Double.parseDouble(variable1.getValue());
						double value2 = Double.parseDouble(variable2.getValue());
						sortOrder = Double.compare(value2, value1);
					} catch(Exception e) {
						sortOrder = variable2.getDescription().compareTo(variable1.getDescription());
					}
					break;
				case 2:
					for(int i = 0; i < sampleData1.size(); i++) {
						if(!Double.isNaN(sampleData1.get(i).getData())) {
							stats.addValue(sampleData1.get(i).getData());
						}
					}
					long count1 = stats.getN();
					stats = new DescriptiveStatistics();
					for(int i = 0; i < sampleData2.size(); i++) {
						if(!Double.isNaN(sampleData2.get(i).getData())) {
							stats.addValue(sampleData2.get(i).getData());
						}
					}
					Long count2 = stats.getN();
					sortOrder = Double.compare(count2, count1);
					break;
				case 3:
					for(int i = 0; i < sampleData1.size(); i++) {
						if(!Double.isNaN(sampleData1.get(i).getData())) {
							stats.addValue(sampleData1.get(i).getData());
						}
					}
					Double mean1 = 0.0;
					if(!Double.isNaN(stats.getMean())) {
						mean1 = stats.getMean();
					}
					stats = new DescriptiveStatistics();
					for(int i = 0; i < sampleData2.size(); i++) {
						if(!Double.isNaN(sampleData2.get(i).getData())) {
							stats.addValue(sampleData2.get(i).getData());
						}
					}
					Double mean2 = 0.0;
					if(!Double.isNaN(stats.getMean())) {
						mean2 = stats.getMean();
					}
					sortOrder = Double.compare(mean2, mean1);
					break;
				case 4:
					for(int i = 0; i < sampleData1.size(); i++) {
						if(!Double.isNaN(sampleData1.get(i).getData())) {
							stats.addValue(sampleData1.get(i).getData());
						}
					}
					Double min1 = 0.0;
					if(!Double.isNaN(stats.getMin())) {
						min1 = stats.getMin();
					}
					stats = new DescriptiveStatistics();
					for(int i = 0; i < sampleData2.size(); i++) {
						if(!Double.isNaN(sampleData2.get(i).getData())) {
							stats.addValue(sampleData2.get(i).getData());
						}
					}
					Double min2 = 0.0;
					if(!Double.isNaN(stats.getMin())) {
						min2 = stats.getMin();
					}
					sortOrder = Double.compare(min2, min1);
					break;
				case 5:
					for(int i = 0; i < sampleData1.size(); i++) {
						if(!Double.isNaN(sampleData1.get(i).getData())) {
							stats.addValue(sampleData1.get(i).getData());
						}
					}
					Double max1 = 0.0;
					if(!Double.isNaN(stats.getMax())) {
						max1 = stats.getMax();
					}
					stats = new DescriptiveStatistics();
					for(int i = 0; i < sampleData2.size(); i++) {
						if(!Double.isNaN(sampleData2.get(i).getData())) {
							stats.addValue(sampleData2.get(i).getData());
						}
					}
					Double max2 = 0.0;
					if(!Double.isNaN(stats.getMax())) {
						max2 = stats.getMax();
					}
					sortOrder = Double.compare(max2, max1);
					break;
				case 6:
					for(int i = 0; i < sampleData1.size(); i++) {
						if(!Double.isNaN(sampleData1.get(i).getData())) {
							stats.addValue(sampleData1.get(i).getData());
						}
					}
					Double rsd1 = stats.getStandardDeviation();
					rsd1 = 100.0 / stats.getSum() * rsd1;
					stats = new DescriptiveStatistics();
					for(int i = 0; i < sampleData2.size(); i++) {
						if(!Double.isNaN(sampleData2.get(i).getData())) {
							stats.addValue(sampleData2.get(i).getData());
						}
					}
					Double rsd2 = stats.getStandardDeviation();
					rsd2 = 100.0 / stats.getSum() * rsd2;
					sortOrder = Double.compare(rsd2, rsd1);
					break;
				case 7:
					for(int i = 0; i < sampleData1.size(); i++) {
						if(!Double.isNaN(sampleData1.get(i).getData())) {
							stats.addValue(sampleData1.get(i).getData());
						}
					}
					Double skew1 = 0.0;
					if(!Double.isNaN(stats.getSkewness())) {
						skew1 = stats.getSkewness();
					}
					stats = new DescriptiveStatistics();
					for(int i = 0; i < sampleData2.size(); i++) {
						if(!Double.isNaN(sampleData2.get(i).getData())) {
							stats.addValue(sampleData2.get(i).getData());
						}
					}
					Double skew2 = 0.0;
					if(!Double.isNaN(stats.getSkewness())) {
						skew2 = stats.getSkewness();
					}
					sortOrder = Double.compare(skew2, skew1);
					break;
				case 8:
					for(int i = 0; i < sampleData1.size(); i++) {
						if(!Double.isNaN(sampleData1.get(i).getData())) {
							stats.addValue(sampleData1.get(i).getData());
						}
					}
					Double kurt1 = 0.0;
					if(!Double.isNaN(stats.getKurtosis())) {
						kurt1 = stats.getKurtosis();
					}
					stats = new DescriptiveStatistics();
					for(int i = 0; i < sampleData2.size(); i++) {
						if(!Double.isNaN(sampleData2.get(i).getData())) {
							stats.addValue(sampleData2.get(i).getData());
						}
					}
					Double kurt2 = 0.0;
					if(!Double.isNaN(stats.getKurtosis())) {
						kurt2 = stats.getKurtosis();
					}
					sortOrder = Double.compare(kurt2, kurt1);
					break;
				default:
					break;
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}
