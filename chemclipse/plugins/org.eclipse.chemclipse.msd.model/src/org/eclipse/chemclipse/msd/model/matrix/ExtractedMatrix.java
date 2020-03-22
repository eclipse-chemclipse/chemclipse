/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.matrix;


import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonBounds;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;

public class ExtractedMatrix {
	
	private IChromatogramSelectionMSD selection;
	private List<IScanMSD> scans;
	private int numberOfScans;
	private int numberOfIons;
	private int startIon;
	private int stopIon;
	private float[][] signal;
	
	public ExtractedMatrix(IChromatogramSelectionMSD chromatogramSelection) {
		this.selection = chromatogramSelection;
		this.numberOfScans = selection.getStopScan() - selection.getStartScan() + 1;
		this.scans = extractScans();
		if(checkHighRes( 10)) {
			throw new IllegalArgumentException();
		} else {
			this.startIon = getMinMz();
			this.stopIon = getMaxMz();
			this.numberOfIons = this.stopIon - this.startIon + 1;
			signal = new float[numberOfScans][this.numberOfIons];
			List<IIon> currentIons;
			int numberIonsCurrentScan;
			try {
				for(int scanIndex = 0; scanIndex < numberOfScans; scanIndex++) {
					currentIons = scans.get(scanIndex).getIons();
					numberIonsCurrentScan = currentIons.size(); 
					for(int j = 0; j < numberIonsCurrentScan; j++) {
						signal[ scanIndex ] [((int) Math.round(currentIons.get(j).getIon() - this.startIon))] =  currentIons.get(j).getAbundance();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Boolean checkHighRes( int limit ) {
		Iterator<IScanMSD> iterator = scans.iterator();
		IIonBounds bounds;
		double rangeAbs;
		IScanMSD currentScan;
		
		while(iterator.hasNext()) {
			currentScan = iterator.next();
			bounds = currentScan.getIonBounds();
			rangeAbs = bounds.getHighestIon().getIon() - bounds.getLowestIon().getIon();
			if(rangeAbs + limit < currentScan.getIons().size()) {
				return (true);
			}
		}
		
		return false;
	}
	
	private List<IScanMSD> extractScans() {

		List<IScanMSD> scans;
		int startRT;
		int stopRT;
		startRT = selection.getStartRetentionTime();
		stopRT = selection.getStopRetentionTime();
		scans = selection.getChromatogram() //
				.getScans() //
				.stream() //
				.filter(s -> s instanceof IScanMSD) //
				.map(IScanMSD.class::cast) //
				.filter(s -> s.getRetentionTime() >= startRT) //
				.filter(s -> s.getRetentionTime() <= stopRT) //
				.collect(Collectors.toList()); //
		return (scans);
	}
	
	private int getMinMz() {

		double min = scans.stream() //
				.flatMap(scan -> scan.getIons().stream()) //
				.min(Comparator.comparing(IIon::getIon)) //
				.get() //
				.getIon();
		int minMz = AbstractIon.getIon(min);
		return (minMz);
	}
	
	private int getMaxMz() {

		double max = scans.stream() //
				.flatMap(scan -> scan.getIons().stream()) //
				.max(Comparator.comparing(IIon::getIon)) //
				.get() //
				.getIon();
		int maxMz = AbstractIon.getIon(max);
		return (maxMz);
	}

	public float[][] getMatrix() {
		return this.signal;
	}

	public int getNumberOfScans() {
		return this.numberOfScans;
	}

	public int getNumberOfIons() {
		return this.numberOfIons;
	}

	public void updateSignal(float[][] signal, int numberOfScans, int nubmerOfIons ) {
		
		IScanMSD currentScan;
		IIon currentIon;
		
		try {
			for(int i = 1; i <= numberOfScans; i++) {
				currentScan = (IScanMSD) selection.getChromatogram().getScan(i);
				currentScan.removeAllIons();
				for(int j = startIon; j < stopIon; j++) {
					if(signal[i-1][j-startIon] != 0.0) {
						currentIon = new Ion(j, signal[i - 1][j - startIon] ); 
						currentScan.addIon(currentIon);
					}		
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}			
	}

	public int[] getScanNumbers() {
		int[] scanNumbers = scans.stream().mapToInt(scan -> scan.getScanNumber()).toArray();
		return scanNumbers;
	}

	public int[] getRetentionTimes() {
		int[] retentionTimes = scans.stream().mapToInt(scan -> scan.getRetentionTime()).toArray();
		return retentionTimes;
	}

}
