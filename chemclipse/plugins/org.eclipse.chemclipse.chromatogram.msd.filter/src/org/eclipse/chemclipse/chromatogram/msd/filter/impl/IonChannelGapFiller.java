/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.chemclipse.chromatogram.msd.filter.impl.settings.CoverageReference;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * Fills missing values (zeros) in nominal ion channels: runs of scans in which an m/z is missing while
 * the scans before and after carry it. Typical for a mass range that starts right at an m/z (the lowest
 * channel is acquired only in some scans) or for single ions missing from otherwise complete spectra.
 * Such gaps draw comb-like ion traces and feed false zeros into deconvolution.
 * <p>
 * A gap is only filled if it has signal on both sides - leading and trailing zeros stay. Then:
 * <ul>
 * <li>in a <b>dense</b> channel - signal in at least the minimum coverage of the scans of the
 * reference span ({@link CoverageReference}) - gaps of any length are filled: the ion is part of the
 * spectra, the zeros are dropouts;</li>
 * <li>in any other channel only gaps of at most {@code maxShortGap} scans - longer runs there are a
 * real absence of the ion.</li>
 * </ul>
 * The filled values are the linear interpolation between the two neighbours, added as nominal ions to
 * the gap scans only; all other ions keep their exact m/z and abundance. Nominal (unit mass) data only:
 * high resolution data are left untouched.
 */
public final class IonChannelGapFiller {

	/**
	 * @param filled
	 *            number of values filled in
	 * @param filledPerIon
	 *            nominal m/z -> number of values filled in that channel, ascending m/z
	 * @param highResolution
	 *            true if nothing was done because the data are high resolution
	 */
	public record Result(int filled, Map<Integer, Integer> filledPerIon, boolean highResolution) {

		static final Result NONE = new Result(0, Collections.emptyMap(), false);
		static final Result HIGH_RESOLUTION = new Result(0, Collections.emptyMap(), true);

		/**
		 * @param shown
		 *            number of channels listed
		 * @return e.g. "m/z 29: 2290, m/z 31: 12 and 5 more channel(s)" - the channels with the most
		 *         filled values first; empty if nothing was filled
		 */
		public String describeChannels(int shown) {

			List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(filledPerIon.entrySet());
			entries.sort(Map.Entry.<Integer, Integer> comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()));
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < entries.size() && i < shown; i++) {
				if(i > 0) {
					builder.append(", ");
				}
				builder.append("m/z ").append(entries.get(i).getKey()).append(": ").append(entries.get(i).getValue());
			}
			if(entries.size() > shown) {
				builder.append(" and ").append(entries.size() - shown).append(" more channel(s)");
			}
			return builder.toString();
		}
	}

	/*
	 * Same heuristic as ExtractedMatrix: more ions in a scan than nominal positions in its m/z range.
	 */
	private static final int HIGH_RESOLUTION_LIMIT = 10;

	private IonChannelGapFiller() {

	}

	/**
	 * Fills the whole chromatogram in place: loads lazily loaded scans for good first and marks the
	 * chromatogram dirty if anything was filled.
	 */
	public static Result fill(IChromatogramMSD chromatogram, int minCoverage, CoverageReference coverageReference, int maxShortGap) {

		if(chromatogram == null) {
			return Result.NONE;
		}
		chromatogram.enforceLoadScanProxies(new NullProgressMonitor());
		List<IScanMSD> scans = new ArrayList<>();
		for(IScan scan : chromatogram.getScans()) {
			if(scan instanceof IScanMSD scanMSD) {
				scans.add(scanMSD);
			}
		}
		Result result = fill(scans, minCoverage, coverageReference, maxShortGap);
		if(result.filled() > 0) {
			chromatogram.setDirty(true);
		}
		return result;
	}

	/**
	 * Fills the scans in place. Lazily loaded scans must be loaded for good by the caller, otherwise the
	 * added ions may get lost.
	 *
	 * @param scans
	 *            consecutive scans, in retention time order
	 * @param minCoverage
	 *            percent (0-100) of scans with signal from which a channel is dense
	 * @param coverageReference
	 *            span the coverage is measured over; null = {@link CoverageReference#FIRST_SIGNAL}
	 * @param maxShortGap
	 *            longest run of missing scans that is filled in channels that are not dense (0 = none)
	 * @return what was filled
	 */
	public static Result fill(List<IScanMSD> scans, int minCoverage, CoverageReference coverageReference, int maxShortGap) {

		if(scans == null || scans.isEmpty()) {
			return Result.NONE;
		}
		/*
		 * Nominal channel range over all scans.
		 */
		int startIon = Integer.MAX_VALUE;
		int stopIon = Integer.MIN_VALUE;
		for(IScanMSD scan : scans) {
			if(isHighResolution(scan)) {
				return Result.HIGH_RESOLUTION;
			}
			for(IIon ion : scan.getIons()) {
				int mz = AbstractIon.getIon(ion.getIon());
				startIon = Math.min(startIon, mz);
				stopIon = Math.max(stopIon, mz);
			}
		}
		if(startIon > stopIon) {
			return Result.NONE;
		}
		int width = stopIon - startIon + 1;
		int scanCount = scans.size();
		/*
		 * Pass 1: per channel the first scan with signal and the number of scans with signal - which
		 * channels are dense.
		 */
		int[] firstSignal = new int[width];
		Arrays.fill(firstSignal, -1);
		int[] signalCount = new int[width];
		for(int index = 0; index < scanCount; index++) {
			float[] current = nominalAbundances(scans.get(index), startIon, width);
			for(int channel = 0; channel < width; channel++) {
				if(current[channel] > 0) {
					if(firstSignal[channel] < 0) {
						firstSignal[channel] = index;
					}
					signalCount[channel]++;
				}
			}
		}
		boolean fromFirstSignal = coverageReference != CoverageReference.WHOLE_RANGE;
		boolean[] dense = new boolean[width];
		for(int channel = 0; channel < width; channel++) {
			if(firstSignal[channel] >= 0) {
				int span = fromFirstSignal ? scanCount - firstSignal[channel] : scanCount;
				dense[channel] = 100L * signalCount[channel] >= (long)minCoverage * span;
			}
		}
		/*
		 * Pass 2: per channel the last value > 0 and the number of zero scans since (-1 = no signal yet,
		 * so leading zeros are never filled). When a channel comes back after a gap that may be filled,
		 * the gap is filled; a gap that never ends (trailing zeros) is not.
		 */
		float[] lastValue = new float[width];
		int[] zeroRun = new int[width];
		Arrays.fill(zeroRun, -1);
		int[] filledPerChannel = new int[width];
		int filled = 0;
		for(int index = 0; index < scanCount; index++) {
			float[] current = nominalAbundances(scans.get(index), startIon, width);
			for(int channel = 0; channel < width; channel++) {
				float value = current[channel];
				if(value > 0) {
					int run = zeroRun[channel];
					if(run >= 1 && (dense[channel] || run <= maxShortGap)) {
						float before = lastValue[channel];
						for(int step = 1; step <= run; step++) {
							float abundance = before + (value - before) * step / (run + 1);
							scans.get(index - run - 1 + step).addIon(new Ion(startIon + channel, abundance));
						}
						filledPerChannel[channel] += run;
						filled += run;
					}
					lastValue[channel] = value;
					zeroRun[channel] = 0;
				} else if(zeroRun[channel] >= 0) {
					zeroRun[channel]++;
				}
			}
		}
		if(filled == 0) {
			return Result.NONE;
		}
		Map<Integer, Integer> filledPerIon = new TreeMap<>();
		for(int channel = 0; channel < width; channel++) {
			if(filledPerChannel[channel] > 0) {
				filledPerIon.put(startIon + channel, filledPerChannel[channel]);
			}
		}
		return new Result(filled, Collections.unmodifiableMap(filledPerIon), false);
	}

	private static boolean isHighResolution(IScanMSD scan) {

		List<IIon> ions = scan.getIons();
		if(ions.size() <= HIGH_RESOLUTION_LIMIT) {
			return false;
		}
		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;
		for(IIon ion : ions) {
			min = Math.min(min, ion.getIon());
			max = Math.max(max, ion.getIon());
		}
		return max - min + HIGH_RESOLUTION_LIMIT < ions.size();
	}

	/**
	 * Abundances summed per nominal m/z, index = m/z - startIon.
	 */
	private static float[] nominalAbundances(IScanMSD scan, int startIon, int width) {

		float[] abundances = new float[width];
		for(IIon ion : scan.getIons()) {
			abundances[AbstractIon.getIon(ion.getIon()) - startIon] += ion.getAbundance();
		}
		return abundances;
	}
}
