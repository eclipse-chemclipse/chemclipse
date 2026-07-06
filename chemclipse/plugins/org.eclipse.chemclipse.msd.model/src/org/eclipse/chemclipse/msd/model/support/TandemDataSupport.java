/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.model.support.ChromatogramSupport;
import org.eclipse.chemclipse.model.support.HeaderUtil;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.RegularMassSpectrum;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.support.traces.TraceTandemMSD;

public class TandemDataSupport {

	public static List<IChromatogramMSD> extractTandemData(IChromatogramMSD chromatogramMSD, HeaderField headerField, CondenseOption condenseOption, boolean enforceFullTimeRange, Set<TraceTandemMSD> traces, boolean separateTraces) {

		List<IChromatogramMSD> chromatograms = new ArrayList<>();
		DecimalFormat decimalFormat = getDecimalFormat(condenseOption);
		Map<Integer, Map<TraceTandemMSD, List<IIon>>> scanRangesMap = new HashMap<>();
		List<Integer> retentionTimes = new ArrayList<>();

		for(IScan scan : chromatogramMSD.getScans()) {
			if(scan instanceof IScanMSD scanMSD) {

				/*
				 * Condense the retention time to alleviate slight time deviations.
				 */
				int retentionTime = -1;
				if(decimalFormat == null) {
					retentionTime = scan.getRetentionTime();
				} else {
					try {
						double retentionTimeMinutes = Double.parseDouble(decimalFormat.format(scan.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
						retentionTime = (int)Math.round(retentionTimeMinutes * IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
					} catch(NumberFormatException e) {
					}
				}
				/*
				 * Check
				 */
				if(retentionTime >= 0) {
					/*
					 * Harmonize times.
					 */
					if(!retentionTimes.contains(retentionTime)) {
						retentionTimes.add(retentionTime);
					}
					/*
					 * Collect Traces
					 */
					for(TraceTandemMSD trace : traces) {
						for(IIon ion : scanMSD.getIons()) {
							if(ion instanceof IIonMSn ionMSn && matches(trace, ionMSn)) {
								/*
								 * Collect
								 */
								Map<TraceTandemMSD, List<IIon>> traceIons = scanRangesMap.get(retentionTime);
								if(traceIons == null) {
									traceIons = new HashMap<>();
									scanRangesMap.put(retentionTime, traceIons);
								}
								/*
								 * 173 > 121.2 @15
								 */
								List<IIon> ions = traceIons.get(trace);
								if(ions == null) {
									ions = new ArrayList<>();
									traceIons.put(trace, ions);
								}
								ions.add(ion);
							}
						}
					}
				}
			}
		}
		/*
		 * Compile
		 */
		if(separateTraces) {
			/*
			 * One Trace per Reference Chromatogram
			 */
			for(TraceTandemMSD trace : traces) {
				Map<Integer, List<IIon>> scanIonsMap = new HashMap<>();
				collectTraces(Arrays.asList(trace), scanRangesMap, retentionTimes, scanIonsMap);
				createReferenceChromatogram(chromatograms, chromatogramMSD, retentionTimes, scanIonsMap, headerField, trace.toString(), enforceFullTimeRange);
			}
		} else {
			/*
			 * All Traces in one Reference Chromatogram
			 */
			enforceFullTimeRange = true; // needed to map all traces
			Map<Integer, List<IIon>> scanIonsMap = new HashMap<>();
			collectTraces(new ArrayList<>(traces), scanRangesMap, retentionTimes, scanIonsMap);
			createReferenceChromatogram(chromatograms, chromatogramMSD, retentionTimes, scanIonsMap, headerField, "Multiple Traces", enforceFullTimeRange);
		}

		return chromatograms;
	}

	private static boolean matches(TraceTandemMSD trace, IIonMSn ion) {

		IIonTransition ionTransition = ion.getIonTransition();
		if(ionTransition != null) {
			if(trace.getParentMZ() == ionTransition.getQ1Ion()) {
				if(trace.getDaughterMZ() == ionTransition.getQ3Ion()) {
					if(trace.getCollisionEnergy() == ionTransition.getCollisionEnergy()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * May return null.
	 * 
	 * @param chromatogramFilterSettings
	 * @return {@link DecimalFormat}
	 */
	private static DecimalFormat getDecimalFormat(CondenseOption condenseOption) {

		DecimalFormat decimalFormat = null;
		switch(condenseOption) {
			case COARSE:
			case STANDARD:
			case SENSITIVE:
				decimalFormat = ValueFormat.getDecimalFormatEnglish(condenseOption.decimalPattern());
				break;
			default:
				break;
		}

		return decimalFormat;
	}

	private static void collectTraces(List<TraceTandemMSD> traces, Map<Integer, Map<TraceTandemMSD, List<IIon>>> scanRangesMap, List<Integer> retentionTimes, Map<Integer, List<IIon>> scanIonsMap) {

		for(int retentionTime : retentionTimes) {
			Map<TraceTandemMSD, List<IIon>> traceIons = scanRangesMap.get(retentionTime);
			if(traceIons != null) {
				for(TraceTandemMSD trace : traces) {
					List<IIon> ions = traceIons.get(trace);
					if(ions != null) {
						List<IIon> mappedIons = scanIonsMap.get(retentionTime);
						if(mappedIons == null) {
							mappedIons = new ArrayList<>();
							scanIonsMap.put(retentionTime, mappedIons);
						}
						mappedIons.addAll(ions);
					}
				}
			}
		}
	}

	private static void createReferenceChromatogram(List<IChromatogramMSD> chromatograms, IChromatogramMSD chromatogramMSD, List<Integer> retentionTimes, Map<Integer, List<IIon>> scanIonsMap, HeaderField headerField, String headerData, boolean enforceFullTimeRange) {

		IChromatogramMSD chromatogramReferenceMSD = new ChromatogramMSD();
		chromatogramReferenceMSD.setConverterId(chromatogramMSD.getConverterId());
		HeaderUtil.setHeaderData(chromatogramReferenceMSD, headerField, headerData);
		for(int retentionTime : retentionTimes) {
			RegularMassSpectrum scanMSD = new RegularMassSpectrum();
			scanMSD.setRetentionTime(retentionTime);
			List<IIon> ions = scanIonsMap.get(retentionTime);
			if(ions != null) {
				for(IIon ion : ions) {
					scanMSD.addIon(ion);
				}
			}
			/*
			 * Scan
			 */
			if(enforceFullTimeRange) {
				chromatogramReferenceMSD.addScan(scanMSD);
			} else {
				if(ions != null && !ions.isEmpty()) {
					chromatogramReferenceMSD.addScan(scanMSD);
				}
			}
		}

		ChromatogramSupport.calculateScanIntervalAndDelay(chromatogramReferenceMSD);
		chromatograms.add(chromatogramReferenceMSD);
	}
}