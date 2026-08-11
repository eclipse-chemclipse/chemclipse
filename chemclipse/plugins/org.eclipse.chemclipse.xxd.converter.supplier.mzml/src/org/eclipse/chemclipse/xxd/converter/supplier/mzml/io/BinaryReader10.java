/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.converter.supplier.mzml.io;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.zip.DataFormatException;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v10.BinaryDataArrayType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v10.CVParamType;

import ms.numpress.MSNumpress;

public class BinaryReader10 extends AbstractBinaryReader {

	private BinaryReader10() {

	}

	public static Pair<String, double[]> parseBinaryData(BinaryDataArrayType binaryDataArrayType) throws DataFormatException {

		double[] values = new double[0];
		String content = "";

		if(BigInteger.ZERO.equals(binaryDataArrayType.getArrayLength())) {
			return new ImmutablePair<>(content, values);
		}

		byte[] binary = binaryDataArrayType.getBinary();
		if(binary == null) {
			return new ImmutablePair<>(content, values);
		}

		byte[] unzippedBinary = binary;
		boolean compressed = false;
		boolean doublePrecision = false;
		String numpressAccession = null;
		int multiplicator = 1;

		for(CVParamType cvParam : binaryDataArrayType.getCvParam()) {
			String accession = cvParam.getAccession();
			String name = cvParam.getName();

			if("MS:1000574".equals(accession) && "zlib compression".equals(name)) {
				compressed = true;
			}

			if(isNumpress(accession)) {
				numpressAccession = accession;
			}

			if("MS:1000521".equals(accession) && "32-bit float".equals(name)) {
				doublePrecision = false;
			} else if("MS:1000523".equals(accession) && "64-bit float".equals(name)) {
				doublePrecision = true;
			}

			if("MS:1000514".equals(accession) && "m/z array".equals(name)) {
				content = "m/z";
			} else if("MS:1000515".equals(accession) && "intensity array".equals(name)) {
				content = "intensity";
			} else if("MS:1000595".equals(accession) && "time array".equals(name)) {
				content = "time";
				multiplicator = XmlReader10.getTimeMultiplicator(cvParam);
			}
		}

		if(compressed) {
			unzippedBinary = inflate(unzippedBinary);
		}

		if(numpressAccession != null) {
			values = MSNumpress.decode(numpressAccession, unzippedBinary, unzippedBinary.length);
			for(int index = 0; index < values.length; index++) {
				values[index] *= multiplicator;
			}
			return new ImmutablePair<>(content, values);
		} else {
			ByteBuffer byteBuffer = ByteBuffer.wrap(unzippedBinary);
			byteBuffer.order(ByteOrder.LITTLE_ENDIAN); // this is always the case

			if(doublePrecision) {
				DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
				values = new double[doubleBuffer.capacity()];
				for(int index = 0; index < doubleBuffer.capacity(); index++) {
					values[index] = Double.valueOf(doubleBuffer.get(index)) * multiplicator;
				}
			} else {
				FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
				values = new double[floatBuffer.capacity()];
				for(int index = 0; index < floatBuffer.capacity(); index++) {
					values[index] = Double.valueOf(floatBuffer.get(index)) * multiplicator;
				}
			}
		}

		return new ImmutablePair<>(content, values);
	}
}
