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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.wsd.model.core.AbstractChromatogramWSD;

public abstract class AbstractChromatogramDSD extends AbstractChromatogramWSD implements IChromatogramDSD {

	private static final long serialVersionUID = 121187623672499533L;

	private String nucleotideSequence = "";
	private Map<Float, Nucleobase> nucleobasePerWavelength = new HashMap<>();

	@Override
	public String getNucleotideSequence() {

		return nucleotideSequence;
	}

	@Override
	public void setNucleotideSequence(String nucleotideSequence) {

		this.nucleotideSequence = nucleotideSequence;
	}

	@Override
	public Map<Float, Nucleobase> getWavelengthMapping() {

		return nucleobasePerWavelength;
	}
}
