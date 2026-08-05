/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class ByteReaderVersion3 {

	public static double[] readValues(byte[] bytes, String byteOrder, BigInteger precision, String compressionType) throws DataFormatException {

		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		/*
		 * Byte Order
		 */
		if(byteOrder != null && byteOrder.equals("network")) {
			byteBuffer.order(ByteOrder.BIG_ENDIAN);
		} else {
			byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		}
		/*
		 * Compression
		 */
		if(compressionType != null && compressionType.equalsIgnoreCase("zlib")) {
			try (Inflater inflater = new Inflater()) {
				inflater.reset();
				inflater.setInput(byteBuffer.array());
				byte[] byteArray = new byte[byteBuffer.capacity() * 10];
				byteBuffer = ByteBuffer.wrap(byteArray, 0, inflater.inflate(byteArray));
			}
		}
		/*
		 * Precision
		 */
		double[] values;
		if(precision != null && precision.intValue() == 64) {
			DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
			values = new double[doubleBuffer.capacity()];
			for(int index = 0; index < doubleBuffer.capacity(); index++) {
				values[index] = doubleBuffer.get(index);
			}
		} else {
			FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
			values = new double[floatBuffer.capacity()];
			for(int index = 0; index < floatBuffer.capacity(); index++) {
				values[index] = floatBuffer.get(index);
			}
		}
		return values;
	}
}
