/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.core.PeakRetentionTimeMap;

public class ObservedPeakList<E extends IPeak> extends ArrayList<E> {

	private static final long serialVersionUID = 1566079524847077478L;

	private PeakRetentionTimeMap<E> peakRetentionTimeMap = new PeakRetentionTimeMap<>();

	@Override
	public boolean add(E element) {

		if(!hasValidPeakModel(element)) {
			return false;
		}
		peakRetentionTimeMap.addPeak(element);
		return super.add(element);
	}

	@Override
	public void add(int index, E element) {

		if(!hasValidPeakModel(element)) {
			return;
		}
		peakRetentionTimeMap.addPeak(element);
		super.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends E> elements) {

		List<E> filteredElements = new ArrayList<>();
		for(E element : elements) {
			if(hasValidPeakModel(element)) {
				filteredElements.add(element);
			}
		}

		for(E element : filteredElements) {
			peakRetentionTimeMap.addPeak(element);
		}
		return super.addAll(filteredElements);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> elements) {

		List<E> filteredElements = new ArrayList<>();
		for(E element : elements) {
			if(hasValidPeakModel(element)) {
				filteredElements.add(element);
			}
		}

		for(E element : filteredElements) {
			peakRetentionTimeMap.addPeak(element);
		}
		return super.addAll(index, filteredElements);
	}

	@Override
	public E remove(int index) {

		E element = super.remove(index);
		peakRetentionTimeMap.addPeak(element);
		element.setMarkedAsDeleted(true);
		return element;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object object) {

		if(object instanceof IPeak peak) {
			peak.setMarkedAsDeleted(true);
			peakRetentionTimeMap.removePeak((E)peak);
		}
		return super.remove(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean removeAll(Collection<?> collection) {

		for(Object object : collection) {
			if(object instanceof IPeak peak) {
				peak.setMarkedAsDeleted(true);
				peakRetentionTimeMap.removePeak((E)peak);
			}
		}
		return super.removeAll(collection);
	}

	@Override
	public void clear() {

		for(E peak : this) {
			peak.setMarkedAsDeleted(true);
			peakRetentionTimeMap.removePeak(peak);
		}
		super.clear();
	}

	public PeakRetentionTimeMap<E> getRetentionTimeMap() {

		return peakRetentionTimeMap;
	}

	private boolean hasValidPeakModel(E peak) {

		IPeakModel peakModel = peak.getPeakModel();
		if(peakModel.areInflectionPointsAvailable()) {
			return peakModel.getWidthByInflectionPoints() > 0;
		} else {
			return true;
		}
	}
}