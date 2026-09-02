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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.dsd.model.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.chemclipse.wsd.model.core.AbstractChromatogramWSD;

public abstract class AbstractChromatogramDSD extends AbstractChromatogramWSD implements IChromatogramDSD {

	private static final long serialVersionUID = 121187623672499533L;

	private Map<Float, Nucleobase> nucleobasePerWavelength = new LinkedHashMap<>();
	private NucleotideSequence nucleotideSequence = new NucleotideSequence();
	private int nucleotideSequenceModCount = -1;

	@Override
	public NucleotideSequence getNucleotideSequence() {

		cacheNucleotideSequence();
		return nucleotideSequence;
	}

	@Override
	public String getMiscInfo() {

		cacheNucleotideSequence();
		return nucleotideSequence.toString();
	}

	@Override
	public Map<Float, Nucleobase> getWavelengthMapping() {

		return nucleobasePerWavelength;
	}

	private void cacheNucleotideSequence() {

		int modCount = getModCount();
		if(nucleotideSequenceModCount != modCount) {
			nucleotideSequence.fromScans(getScans());
			nucleotideSequenceModCount = modCount;
		}
	}
}
