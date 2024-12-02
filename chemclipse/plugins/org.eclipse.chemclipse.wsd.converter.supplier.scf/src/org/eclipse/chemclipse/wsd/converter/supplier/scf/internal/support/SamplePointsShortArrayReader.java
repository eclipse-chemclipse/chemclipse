/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.scf.internal.support;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.io.support.AbstractArrayReader;

public class SamplePointsShortArrayReader extends AbstractArrayReader implements ISamplePointsShortArrayReader {

	public SamplePointsShortArrayReader(File file) throws IOException {

		super(file);
	}

	@Override
	public short[] readAdenine(int numberBases) {

		return readShorts(numberBases);
	}

	@Override
	public short[] readThymine(int numberBases) {

		return readShorts(numberBases);
	}

	@Override
	public short[] readGuanine(int numberBases) {

		return readShorts(numberBases);
	}

	@Override
	public short[] readCytosine(int numberBases) {

		return readShorts(numberBases);
	}

	private short[] readShorts(int dataPoints) {

		short[] shortArray = new short[dataPoints];
		for(int i = 0; i < dataPoints; i++) {
			shortArray[i] = read2BShortBE();
		}
		return shortArray;
	}
}
