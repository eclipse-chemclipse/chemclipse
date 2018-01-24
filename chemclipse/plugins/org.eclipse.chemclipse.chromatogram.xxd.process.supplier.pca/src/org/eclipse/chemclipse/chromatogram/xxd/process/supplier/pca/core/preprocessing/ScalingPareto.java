/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.core.preprocessing;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.model.ISample;
import org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.model.ISampleData;
import org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.model.ISamples;
import org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.model.IVariable;

public class ScalingPareto extends AbstaractScaling {

	public ScalingPareto(int centeringType) {
		super(centeringType);
	}

	@Override
	public String getDescription() {

		return "";
	}

	@Override
	public String getName() {

		return "Pareto Scaling";
	}

	@Override
	public <V extends IVariable, S extends ISample<? extends ISampleData>> void process(ISamples<V, S> samples) {

		boolean onlySeleted = isOnlySelected();
		int centeringType = getCenteringType();
		List<V> variables = samples.getVariables();
		List<S> samplesList = samples.getSampleList();
		for(int i = 0; i < variables.size(); i++) {
			final double mean = getCenteringValue(samplesList, i, centeringType);
			final double deviationSqrt = Math.sqrt(getStandartDeviation(samplesList, i, centeringType));
			for(ISample<?> sample : samplesList) {
				ISampleData sampleData = sample.getSampleData().get(i);
				if(!sampleData.isEmpty() && (sample.isSelected() || !onlySeleted)) {
					double data = sampleData.getModifiedData();
					if(deviationSqrt != 0) {
						double scaleData = (data - mean) / deviationSqrt;
						sampleData.setModifiedData(scaleData);
					}
				}
			}
		}
	}
}
