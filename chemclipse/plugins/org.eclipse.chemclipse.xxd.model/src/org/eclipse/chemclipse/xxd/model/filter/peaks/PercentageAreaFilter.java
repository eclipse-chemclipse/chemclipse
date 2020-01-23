/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Alexander Stark - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.model.filter.peaks;

import java.util.Collection;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.filter.IPeakFilter;
import org.eclipse.chemclipse.processing.Processor;
import org.eclipse.chemclipse.processing.core.MessageConsumer;
import org.eclipse.chemclipse.processing.filter.CRUDListener;
import org.eclipse.chemclipse.processing.filter.Filter;
import org.eclipse.chemclipse.xxd.model.support.ValueFilterTreatmentOption;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.osgi.service.component.annotations.Component;

@Component(service = { IPeakFilter.class, Filter.class, Processor.class })
public class PercentageAreaFilter implements IPeakFilter<PercentageAreaFilterSettings> {

	@Override
	public String getName() {

		return "Percentage Area Filter";
	}

	@Override
	public String getDescription() {

		return "Filter peaks by percentage area values";
	}

	@Override
	public Class<PercentageAreaFilterSettings> getConfigClass() {

		return PercentageAreaFilterSettings.class;
	}

	@Override
	public boolean acceptsIPeaks(Collection<? extends IPeak> items) {

		return true;
	}

	@Override
	public <X extends IPeak> void filterIPeaks(CRUDListener<X, IPeakModel> listener, PercentageAreaFilterSettings configuration, MessageConsumer messageConsumer, IProgressMonitor monitor) throws IllegalArgumentException {

		Collection<X> read = listener.read();
		if(configuration == null) {
			configuration = createConfiguration(read);
		}
		SubMonitor subMonitor = SubMonitor.convert(monitor, read.size());
		double areaSum = calculateAreaSum(read);

		for(X peak : read) {
			double compareAreaValue = calculatePercentageAreaCompareValue(peak, areaSum);
			applySelectedOptions(compareAreaValue, configuration, listener, peak);
			checkForKeepOption(listener, configuration, peak);
			subMonitor.worked(1);
		}
	}

	private static <X extends IPeak> double calculateAreaSum(Collection<X> read) {

		double areaSum = 0;
		for(X peak : read) {
			areaSum = areaSum + peak.getIntegratedArea();
		}
		return areaSum;
	}

	private static <X extends IPeak> double calculatePercentageAreaCompareValue(X peak, double areaSum) {

		return (100 / areaSum) * peak.getIntegratedArea();
	}

	private static <X extends IPeak> void applySelectedOptions(double peakValue, PercentageAreaFilterSettings localSettings, CRUDListener<X, IPeakModel> listener, X peak) {

		switch (localSettings.getFilterSelectionCriterion()) {
		case AREA_LESS_THAN_MINIMUM:
			if(Double.compare(peakValue, localSettings.getMinimumPercentageAreaValue())<0) {
				processPeak(listener, localSettings, peak);
			}
			break;
		case AREA_GREATER_THAN_MAXIMUM:
			if(Double.compare(peakValue, localSettings.getMaximumPercentageAreaValue())>0) {
				processPeak(listener, localSettings, peak);
			}
			break;
		case AREA_NOT_WITHIN_RANGE:
			if(Double.compare(peakValue, localSettings.getMinimumPercentageAreaValue())<0 || Double.compare(peakValue, localSettings.getMaximumPercentageAreaValue())>0) {
				processPeak(listener, localSettings, peak);
			}
			break;
		default:
			throw new IllegalArgumentException("Unsupported Peak Filter Selection Criterion!");
		}
	}

	private static <X extends IPeak> void processPeak(CRUDListener<X, IPeakModel> listener, PercentageAreaFilterSettings localSettings, X peak) {

		switch (localSettings.getFilterTreatmentOption()) {
		case ENABLE_PEAK:
			peak.setActiveForAnalysis(true);
			listener.updated(peak);
			break;
		case DEACTIVATE_PEAK:
			peak.setActiveForAnalysis(false);
			listener.updated(peak);
			break;
		case KEEP_PEAK:
			peak.setActiveForAnalysis(false);
			listener.updated(peak);
			break;
		case DELETE_PEAK:
			listener.delete(peak);
			break;
		default:
			throw new IllegalArgumentException("Unsupported Peak Filter Treatment Option!");
		}
	}

	private static <X extends IPeak> void checkForKeepOption(CRUDListener<X, IPeakModel> listener, PercentageAreaFilterSettings configuration, X peak) {

		if(configuration.getFilterTreatmentOption()==ValueFilterTreatmentOption.KEEP_PEAK) {
			if(peak.isActiveForAnalysis()==true) {
				listener.delete(peak);
			} else {
				peak.setActiveForAnalysis(true);
				listener.updated(peak);
			}
		}
	}
}
