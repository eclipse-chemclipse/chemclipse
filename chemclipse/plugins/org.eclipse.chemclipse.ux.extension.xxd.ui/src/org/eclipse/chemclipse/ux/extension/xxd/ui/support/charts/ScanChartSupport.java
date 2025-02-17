/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.comparator.IonValueComparator;
import org.eclipse.chemclipse.support.comparator.SortOrder;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.DisplayType;
import org.eclipse.chemclipse.vsd.model.core.IScanVSD;
import org.eclipse.chemclipse.vsd.model.core.ISignalVSD;
import org.eclipse.chemclipse.wsd.model.comparator.WavelengthValueComparator;
import org.eclipse.chemclipse.wsd.model.core.IScanSignalWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.chemclipse.wsd.model.core.selection.IChromatogramSelectionWSD;
import org.eclipse.chemclipse.wsd.model.core.support.IMarkedWavelength;
import org.eclipse.chemclipse.wsd.model.core.support.IMarkedWavelengths;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class ScanChartSupport {

	private IonValueComparator ionValueComparator = new IonValueComparator(SortOrder.ASC);
	private WavelengthValueComparator wavelengthValueComparator = new WavelengthValueComparator(SortOrder.ASC);

	public IBarSeriesData getBarSeriesData(IScan scan, String postfix, boolean mirrored) {

		ISeriesData seriesData = getSeriesData(scan, postfix, mirrored);
		IBarSeriesData barSeriesData = new BarSeriesData(seriesData);
		return barSeriesData;
	}

	public ILineSeriesData getLineSeriesData(IScan scan, String postfix, boolean mirrored) {

		ISeriesData seriesData = getSeriesData(scan, postfix, mirrored);
		ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
		return lineSeriesData;
	}

	public ILineSeriesData getLineSeriesDataPoint(IScan scan, boolean mirrored, String seriesId, DisplayType displayType, IMarkedTraces<?> markedSignals) {

		List<IScan> scans = new ArrayList<>();
		scans.add(scan);
		return getLineSeriesDataPoint(scans, mirrored, seriesId, displayType, markedSignals);
	}

	public ILineSeriesData getLineSeriesDataPoint(List<IScan> scans, boolean mirrored, String seriesId) {

		IMarkedTraces<?> markedSignals = null;
		return getLineSeriesDataPoint(scans, mirrored, seriesId, DisplayType.TIC, markedSignals);
	}

	public ILineSeriesData getLineSeriesDataPoint(List<IScan> scans, boolean mirrored, String seriesId, DisplayType displayType, IChromatogramSelection chromatogramSelection) {

		IMarkedTraces<?> markedSignals = null;
		if(displayType.equals(DisplayType.SWC)) {
			markedSignals = ((IChromatogramSelectionWSD)chromatogramSelection).getSelectedWavelengths();
		}
		return getLineSeriesDataPoint(scans, mirrored, seriesId, displayType, markedSignals);
	}

	public ILineSeriesData getLineSeriesDataPoint(IScan scan, boolean mirrored, String seriesId, DisplayType displayType, IChromatogramSelection chromatogramSelection) {

		return getLineSeriesDataPoint(Collections.singletonList(scan), mirrored, seriesId, displayType, chromatogramSelection);
	}

	public ILineSeriesData getLineSeriesDataPoint(List<IScan> scans, boolean mirrored, String seriesId, DisplayType displayType, IMarkedTraces<?> markedSignals) {

		List<Double> xSeries = new ArrayList<>(scans.size());
		List<Double> ySeries = new ArrayList<>(scans.size());
		//
		for(IScan scan : scans) {
			if(displayType.equals(DisplayType.TIC)) {
				if(scan != null) {
					xSeries.add((double)scan.getRetentionTime());
					ySeries.add((double)((mirrored) ? scan.getTotalSignal() * -1 : scan.getTotalSignal()));
				}
			} else if(displayType.equals(DisplayType.SWC)) {
				if(scan instanceof IScanWSD scanWSD && markedSignals instanceof IMarkedWavelengths markedWavelengths) {
					Iterator<IMarkedWavelength> markedWavelengthsIterator = markedWavelengths.iterator();
					if(markedWavelengthsIterator.hasNext()) {
						float trace = (float)markedWavelengthsIterator.next().getTrace();
						Optional<IScanSignalWSD> scanSignal = scanWSD.getScanSignal(trace);
						if(scanSignal.isPresent()) {
							xSeries.add((double)scan.getRetentionTime());
							ySeries.add((double)scanSignal.get().getAbsorbance());
						}
					}
				}
			}
		}
		//
		ISeriesData seriesData = new SeriesData(xSeries.stream().mapToDouble(Double::doubleValue).toArray(), ySeries.stream().mapToDouble(Double::doubleValue).toArray(), seriesId);
		ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
		return lineSeriesData;
	}

	private ISeriesData getSeriesData(IScan scan, String postfix, boolean mirrored) {

		double[] xSeries;
		double[] ySeries;
		String scanNumber = (scan.getScanNumber() > 0 ? Integer.toString(scan.getScanNumber()) : "--");
		String id = "Scan " + scanNumber;
		if(!"".equals(postfix)) {
			id += " " + postfix;
		}
		/*
		 * Sort the scan data, otherwise the line chart could be odd.
		 */
		if(scan instanceof IScanMSD scanMSD) {
			/*
			 * MSD
			 */
			List<IIon> ions = new ArrayList<IIon>(scanMSD.getIons());
			Collections.sort(ions, ionValueComparator);
			int size = ions.size();
			xSeries = new double[size];
			ySeries = new double[size];
			int index = 0;
			for(IIon ion : ions) {
				xSeries[index] = ion.getIon();
				ySeries[index] = (mirrored) ? ion.getAbundance() * -1 : ion.getAbundance();
				index++;
			}
		} else if(scan instanceof IScanCSD scanCSD) {
			/*
			 * CSD
			 */
			xSeries = new double[]{scanCSD.getRetentionTime()};
			ySeries = new double[]{(mirrored) ? scanCSD.getTotalSignal() * -1 : scanCSD.getTotalSignal()};
		} else if(scan instanceof IScanWSD scanWSD) {
			/*
			 * WSD
			 */
			List<IScanSignalWSD> scanSignalsWSD = new ArrayList<IScanSignalWSD>(scanWSD.getScanSignals());
			Collections.sort(scanSignalsWSD, wavelengthValueComparator);
			int size = scanSignalsWSD.size();
			xSeries = new double[size];
			ySeries = new double[size];
			int index = 0;
			for(IScanSignalWSD scanSignalWSD : scanSignalsWSD) {
				xSeries[index] = scanSignalWSD.getWavelength();
				ySeries[index] = (mirrored) ? scanSignalWSD.getAbsorbance() * -1 : scanSignalWSD.getAbsorbance();
				index++;
			}
		} else if(scan instanceof IScanVSD scanVSD) {
			/*
			 * VSD
			 */
			TreeSet<ISignalVSD> scanSignalsVSD = scanVSD.getProcessedSignals();
			int size = scanSignalsVSD.size();
			xSeries = new double[size];
			ySeries = new double[size];
			int index = 0;
			for(ISignalVSD scanSignalVSD : scanSignalsVSD) {
				xSeries[index] = scanSignalVSD.getWavenumber();
				ySeries[index] = (mirrored) ? scanSignalVSD.getIntensity() * -1 : scanSignalVSD.getIntensity();
				index++;
			}
		} else {
			xSeries = new double[0];
			ySeries = new double[0];
		}
		//
		return new SeriesData(xSeries, ySeries, id);
	}
}
