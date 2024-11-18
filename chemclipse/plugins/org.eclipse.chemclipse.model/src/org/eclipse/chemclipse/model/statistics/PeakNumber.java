/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.statistics;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.chemclipse.support.text.ValueFormat;

public class PeakNumber extends AbstractVariable implements IPeakNumber {

	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0");
	private int peakNumber = 0;

	public PeakNumber(Integer peakNumber) {

		super();
		this.peakNumber = peakNumber;
		setValue(convertValue());
		setType(IPeakNumber.TYPE);
		setSelected(true);
	}

	public static Set<PeakNumber> create(List<Integer> peakNumbers) {

		Set<PeakNumber> peakNumberSet = new TreeSet<>();
		for(int i = 0; i < peakNumbers.size(); i++) {
			peakNumberSet.add(new PeakNumber(peakNumbers.get(i)));
		}
		return peakNumberSet;
	}

	private String convertValue() {

		return decimalFormat.format(getPeakNumber());
	}

	@Override
	public int compareTo(IVariable o) {

		if(o instanceof IPeakNumber peakNumber) {
			return Integer.compare(getPeakNumber(), peakNumber.getPeakNumber());
		}
		return 0;
	}

	@Override
	public int getPeakNumber() {

		return peakNumber;
	}

	@Override
	public void setPeakNumber(int peakNumber) {

		this.peakNumber = peakNumber;
		setValue(convertValue());
	}
}
