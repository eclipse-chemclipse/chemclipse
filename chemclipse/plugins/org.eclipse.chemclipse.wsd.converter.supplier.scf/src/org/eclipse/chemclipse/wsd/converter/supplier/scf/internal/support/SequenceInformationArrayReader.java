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
import org.eclipse.chemclipse.wsd.converter.supplier.scf.model.Probability;

public class SequenceInformationArrayReader extends AbstractArrayReader implements ISequenceInformationArrayReader {

	public SequenceInformationArrayReader(File file) throws IOException {

		super(file);
	}

	@Override
	public int[] readPeakIndices(int numberBases) {

		return readIntegers(numberBases);
	}

	@Override
	public Probability readProbabilities(int numberBases) {

		return new Probability(readBytes(numberBases), readBytes(numberBases), readBytes(numberBases), readBytes(numberBases));
	}

	@Override
	public char[] readBaseCalls(int numberBases) {

		return readCharacters(numberBases);
	}

	@Override
	public byte[] readSpares(int numberBases) {

		return readBytes(numberBases);
	}

	private int[] readIntegers(int dataPoints) {

		int[] intArray = new int[dataPoints];
		for(int i = 0; i < dataPoints; i++) {
			intArray[i] = read4BIntegerBE();
		}
		return intArray;
	}

	private char[] readCharacters(int dataPoints) {

		char[] charArray = new char[dataPoints];
		for(int i = 0; i < dataPoints; i++) {
			charArray[i] = (char)readByte();
		}
		return charArray;
	}
}
