/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.model.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.chemclipse.support.traces.TraceFactory;
import org.eclipse.chemclipse.support.traces.TraceGeneric;
import org.eclipse.chemclipse.support.traces.TraceGenericDelta;

public class TraceRange {

	private int retentionTimeColumn1Start = 0;
	private int retentionTimeColumn1Stop = 0;
	private int retentionTimeColumn2Start = 0;
	private int retentionTimeColumn2Stop = 0;
	private String scanIndicesColumn2 = "";
	private String name = "";
	private String traces = "";
	private SecondDimensionHint secondDimensionHint = SecondDimensionHint.NONE;
	/*
	 * Transient
	 * Used to parse traces from GCxGC chromatograms.
	 */
	private Set<Integer> scanIndices = new HashSet<>();
	private List<TraceGenericDelta> genericTraces = new ArrayList<>();
	private Set<Integer> traceIndices = new HashSet<>();

	public int getRetentionTimeColumn1Start() {

		return retentionTimeColumn1Start;
	}

	public void setRetentionTimeColumn1Start(int retentionTimeColumn1Start) {

		this.retentionTimeColumn1Start = retentionTimeColumn1Start;
	}

	public int getRetentionTimeColumn1Stop() {

		return retentionTimeColumn1Stop;
	}

	public void setRetentionTimeColumn1Stop(int retentionTimeColumn1Stop) {

		this.retentionTimeColumn1Stop = retentionTimeColumn1Stop;
	}

	public int getRetentionTimeColumn2Start() {

		return retentionTimeColumn2Start;
	}

	public void setRetentionTimeColumn2Start(int retentionTimeColumn2Start) {

		this.retentionTimeColumn2Start = retentionTimeColumn2Start;
	}

	public int getRetentionTimeColumn2Stop() {

		return retentionTimeColumn2Stop;
	}

	public void setRetentionTimeColumn2Stop(int retentionTimeColumn2Stop) {

		this.retentionTimeColumn2Stop = retentionTimeColumn2Stop;
	}

	public String getScanIndicesColumn2() {

		return scanIndicesColumn2;
	}

	public void setScanIndicesColumn2(String scanIndicesColumn2) {

		this.scanIndicesColumn2 = scanIndicesColumn2 == null ? "" : scanIndicesColumn2;
		scanIndices.clear();
		//
		List<TraceGeneric> genericTraces = TraceFactory.parseTraces(this.scanIndicesColumn2, TraceGeneric.class);
		for(TraceGeneric traceGeneric : genericTraces) {
			scanIndices.add(traceGeneric.getTrace());
		}
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getTraces() {

		return traces;
	}

	public void setTraces(String traces) {

		this.traces = traces;
	}

	public SecondDimensionHint getSecondDimensionHint() {

		return secondDimensionHint;
	}

	public void setSecondDimensionHint(SecondDimensionHint secondDimensionHint) {

		this.secondDimensionHint = secondDimensionHint;
	}

	public Set<Integer> getScanIndices() {

		return scanIndices;
	}

	public List<TraceGenericDelta> getGenericTraces() {

		return genericTraces;
	}

	public Set<Integer> getTraceIndices() {

		return traceIndices;
	}

	public boolean matches(int retentionTime, int scanSecondDimension) {

		if(matchesRetentionTime(retentionTime)) {
			if(scanIndices.contains(scanSecondDimension)) {
				return true;
			}
		}
		//
		return false;
	}

	private boolean matchesRetentionTime(int retentionTimeColumn1) {

		if(retentionTimeColumn1Start == 0 && retentionTimeColumn1Stop == 0) {
			return true;
		} else if(retentionTimeColumn1 >= retentionTimeColumn1Start && retentionTimeColumn1 <= retentionTimeColumn1Stop) {
			return true;
		}
		//
		return false;
	}

	@Override
	public int hashCode() {

		return Objects.hash(name, retentionTimeColumn1Start, retentionTimeColumn1Stop);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		TraceRange other = (TraceRange)obj;
		return Objects.equals(name, other.name) && retentionTimeColumn1Start == other.retentionTimeColumn1Start && retentionTimeColumn1Stop == other.retentionTimeColumn1Stop;
	}

	@Override
	public String toString() {

		return "TraceRange [retentionTimeColumn1Start=" + retentionTimeColumn1Start + ", retentionTimeColumn1Stop=" + retentionTimeColumn1Stop + ", retentionTimeColumn2Start=" + retentionTimeColumn2Start + ", retentionTimeColumn2Stop=" + retentionTimeColumn2Stop + ", scanIndicesColumn2=" + scanIndicesColumn2 + ", name=" + name + ", traces=" + traces + ", secondDimensionHint=" + secondDimensionHint + ", scanIndices=" + scanIndices + ", genericTraces=" + genericTraces + ", traceIndices=" + traceIndices + "]";
	}
}