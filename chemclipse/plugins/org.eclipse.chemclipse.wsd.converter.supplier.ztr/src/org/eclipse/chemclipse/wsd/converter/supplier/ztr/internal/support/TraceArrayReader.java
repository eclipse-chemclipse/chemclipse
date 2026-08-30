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
package org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.converter.io.support.AbstractArrayReader;

public class TraceArrayReader extends AbstractArrayReader implements ITraceArrayReader {

	private List<Integer> adenine = new ArrayList<>();
	private List<Integer> cytosine = new ArrayList<>();
	private List<Integer> guanine = new ArrayList<>();
	private List<Integer> thymine = new ArrayList<>();
	private int samples = 0;

	public TraceArrayReader(byte[] data) throws IOException {

		super(data);

		samples = data.length / (4 * Short.BYTES);

		for(int i = 0; i < samples; i++) {
			adenine.add(read2BUIntegerBE());
		}

		for(int i = 0; i < samples; i++) {
			cytosine.add(read2BUIntegerBE());
		}

		for(int i = 0; i < samples; i++) {
			guanine.add(read2BUIntegerBE());
		}

		for(int i = 0; i < samples; i++) {
			thymine.add(read2BUIntegerBE());
		}
	}

	@Override
	public int getSamples() {

		return samples;
	}

	@Override
	public List<Integer> getAdenine() {

		return adenine;
	}

	@Override
	public List<Integer> getCytosine() {

		return cytosine;
	}

	@Override
	public List<Integer> getGuanine() {

		return guanine;
	}

	@Override
	public List<Integer> getThymine() {

		return thymine;
	}
}
