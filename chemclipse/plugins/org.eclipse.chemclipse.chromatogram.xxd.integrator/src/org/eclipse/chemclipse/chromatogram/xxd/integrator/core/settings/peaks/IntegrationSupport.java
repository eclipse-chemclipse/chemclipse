/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.csd.model.core.IChromatogramPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.support.IRetentionTimeRange;
import org.eclipse.chemclipse.model.support.RetentionTimeRange;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;

public class IntegrationSupport implements IIntegrationSupport {

	private int minimumPeakWidth = IIntegrationSupport.INITIAL_PEAK_WIDTH; // milliseconds
	private float minimumSignalToNoiseRatio = IIntegrationSupport.MIN_SIGNAL_TO_NOISE_RATIO;
	private List<IRetentionTimeRange> ranges;

	public IntegrationSupport() {

		ranges = new ArrayList<>();
	}

	// ------------------------------------------IIntegrationSupport
	@Override
	public void reset() {

		minimumPeakWidth = IIntegrationSupport.INITIAL_PEAK_WIDTH;
		minimumSignalToNoiseRatio = IIntegrationSupport.MIN_SIGNAL_TO_NOISE_RATIO;
		resetIntegratorOff();
	}

	@Override
	public void resetIntegratorOff() {

		ranges.clear();
	}

	@Override
	public void setIntegratorOff(int startRetentionTime, int stopRetentionTime) {

		IRetentionTimeRange retentionTimeRange = new RetentionTimeRange(startRetentionTime, stopRetentionTime);
		ranges.add(retentionTimeRange);
	}

	@Override
	public boolean isIntegratorOff(int startRetentionTime) {

		boolean integratorOff = false;
		exitloop:
		for(IRetentionTimeRange range : ranges) {
			if(startRetentionTime >= range.getStartRetentionTime() && startRetentionTime <= range.getStopRetentionTime()) {
				integratorOff = true;
				break exitloop;
			}
		}
		return integratorOff;
	}

	@Override
	public void setMinimumPeakWidth(int minimumPeakWidth) {

		if(minimumPeakWidth >= 0) {
			this.minimumPeakWidth = minimumPeakWidth;
		}
	}

	@Override
	public int getMinimumPeakWidth() {

		return minimumPeakWidth;
	}

	@Override
	public float getMinimumSignalToNoiseRatio() {

		return minimumSignalToNoiseRatio;
	}

	@Override
	public void setMinimumSignalToNoiseRatio(float minimumSignalToNoiseRatio) {

		if(minimumSignalToNoiseRatio >= 0) {
			this.minimumSignalToNoiseRatio = minimumSignalToNoiseRatio;
		}
	}

	// ------------------------------------------IIntegrationSupport
	// ------------------------------------------IReportDecider
	@Override
	public boolean report(IPeak peak) {

		if(peak == null) {
			return false;
		}
		/*
		 * Peak Model / Peak Width
		 */
		if(peak instanceof IPeakMSD peakMSD) {
			if(peakMSD.getPeakModel() == null) {
				return false;
			}
			if(peakMSD.getPeakModel().getWidthByInflectionPoints() < minimumPeakWidth) {
				return false;
			}
			if(isIntegratorOff(peakMSD.getPeakModel().getStartRetentionTime())) {
				return false;
			}
		} else if(peak instanceof IPeakCSD peakFID) {
			if(peakFID.getPeakModel() == null) {
				return false;
			}
			if(peakFID.getPeakModel().getWidthByInflectionPoints() < minimumPeakWidth) {
				return false;
			}
			if(isIntegratorOff(peakFID.getPeakModel().getStartRetentionTime())) {
				return false;
			}
		}
		/*
		 * S/N
		 */
		if(peak instanceof IChromatogramPeakMSD chromatogramPeak) {
			if(chromatogramPeak.getSignalToNoiseRatio() < minimumSignalToNoiseRatio) {
				return false;
			}
		} else if(peak instanceof IChromatogramPeakCSD chromatogramPeak) {
			if(chromatogramPeak.getSignalToNoiseRatio() < minimumSignalToNoiseRatio) {
				return false;
			}
		}
		return true;
	}
	// ------------------------------------------IReportDecider
}
