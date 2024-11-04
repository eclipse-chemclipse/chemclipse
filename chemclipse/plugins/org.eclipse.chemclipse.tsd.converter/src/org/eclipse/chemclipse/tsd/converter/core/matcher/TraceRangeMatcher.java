/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.converter.core.matcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.chemclipse.support.traces.TraceFactory;
import org.eclipse.chemclipse.support.traces.TraceGenericDelta;
import org.eclipse.chemclipse.support.traces.TraceHighResMSD;
import org.eclipse.chemclipse.support.traces.TraceHighResWSD;
import org.eclipse.chemclipse.tsd.model.core.TraceRange;

/*
 * ------------
 * Traces
 * ------------
 * 94.05±0.05 Qualifier
 * 150.15±0.05 Quantifier
 * ---
 * 118.05±0.05 Qualifier
 * 146.05±0.05 Quantifier
 * ---
 * 94.05±0.05, 118.05±0.05, 146.05±0.05, 150.15±0.05
 * ---
 */
public class TraceRangeMatcher {

	private int retentionTimeMin = 0;
	private int retentionTimeMax = 0;
	private List<TraceRange> traceRanges = new ArrayList<>();
	private boolean parseFully = false;

	public boolean isParseFully() {

		return parseFully;
	}

	public void setParseFully(boolean parseFully) {

		this.parseFully = parseFully;
	}

	public void addHighResMSD(String traces, int retentionTimeStart, int retentionTimeStop) {

		add(traces, retentionTimeStart, retentionTimeStop, TraceHighResMSD.class);
	}

	public void addHighResWSD(String traces, int retentionTimeStart, int retentionTimeStop) {

		add(traces, retentionTimeStart, retentionTimeStop, TraceHighResWSD.class);
	}

	/**
	 * The value map is either a index, mz or index, wavelength map.
	 * 
	 * @param valueMap
	 */
	public void applyTraceIndices(Map<Integer, Double> valueMap) {

		if(valueMap != null) {
			for(Map.Entry<Integer, Double> entry : valueMap.entrySet()) {
				int index = entry.getKey();
				double value = entry.getValue();
				for(TraceRange traceRange : traceRanges) {
					for(TraceGenericDelta trace : traceRange.getGenericTraces()) {
						if(value >= trace.getStartValue() && value <= trace.getStopValue()) {
							traceRange.getTraceIndices().add(index);
						}
					}
				}
			}
		}
	}

	/**
	 * Return the trace ranges for a given retention time.
	 * 
	 * @param retentionTime
	 * @return List<TraceRange>
	 */
	public List<TraceRange> getTraceRanges(int retentionTime) {

		List<TraceRange> selection = new ArrayList<>();
		if(isRetentionTimeInFocus(retentionTime)) {
			for(TraceRange traceRange : traceRanges) {
				if(isValidTraceRange(traceRange)) {
					if(retentionTime >= traceRange.getRetentionTimeColumn1Start() && retentionTime <= traceRange.getRetentionTimeColumn1Stop()) {
						selection.add(traceRange);
					}
				}
			}
		}
		//
		return selection;
	}

	/**
	 * Returns the indices for a given range.
	 * 
	 * @param retentionTime
	 * @param traceRanges
	 * @param start
	 * @param stop
	 * @return Set<Integer>
	 */
	public Set<Integer> getTraceIndices(int retentionTime, int start, int stop) {

		List<TraceRange> traceRanges = getTraceRanges(retentionTime);
		Set<Integer> traceIndices = new HashSet<>();
		for(TraceRange traceRange : traceRanges) {
			if(useIndices(traceRange.getTraceIndices(), start, stop)) {
				traceIndices.addAll(traceRange.getTraceIndices());
			}
		}
		//
		return traceIndices;
	}

	private boolean useIndices(Set<Integer> traceIndices, int start, int stop) {

		if(traceIndices.isEmpty()) {
			return true;
		} else {
			for(int i = start; i <= stop; i++) {
				if(traceIndices.contains(i)) {
					return true;
				}
			}
		}
		//
		return false;
	}

	private boolean isValidTraceRange(TraceRange traceRange) {

		if(traceRange.getRetentionTimeColumn1Start() == 0 && traceRange.getRetentionTimeColumn1Stop() == 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isRetentionTimeInFocus(int retentionTime) {

		if(retentionTimeMin == 0 && retentionTimeMax == 0) {
			return false;
		} else {
			return retentionTime >= retentionTimeMin && retentionTime <= retentionTimeMax;
		}
	}

	private void add(String traces, int retentionTimeColumn1Start, int retentionTimeColumn1Stop, Class<? extends TraceGenericDelta> clazz) {

		if(retentionTimeColumn1Start >= 0 && retentionTimeColumn1Stop >= 0) {
			List<? extends TraceGenericDelta> tracesGenericDelta = TraceFactory.parseTraces(traces, clazz);
			if(!tracesGenericDelta.isEmpty()) {
				/*
				 * TraceRange
				 */
				TraceRange traceRange = new TraceRange();
				traceRange.setRetentionTimeColumn1Start(retentionTimeColumn1Start);
				traceRange.setRetentionTimeColumn1Stop(retentionTimeColumn1Stop);
				traceRange.getGenericTraces().addAll(tracesGenericDelta);
				traceRanges.add(traceRange);
				/*
				 * Min/Max
				 */
				retentionTimeMin = Math.min(retentionTimeMin, retentionTimeColumn1Start);
				retentionTimeMax = Math.max(retentionTimeMax, retentionTimeColumn1Stop);
			}
		}
	}
}