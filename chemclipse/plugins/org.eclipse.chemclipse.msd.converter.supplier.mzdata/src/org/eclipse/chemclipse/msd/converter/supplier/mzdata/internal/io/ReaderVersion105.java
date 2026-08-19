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
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Data;

public class ReaderVersion105 {

	private ReaderVersion105() {

	}

	public static double[] parseData(Data data) {

		double[] values = new double[0];
		ByteBuffer byteBuffer = ByteBuffer.wrap(data.getValue());
		/*
		 * Byte Order
		 */
		String endian = data.getEndian();
		if(endian != null && endian.equals("big")) {
			byteBuffer.order(ByteOrder.BIG_ENDIAN);
		} else {
			byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		}
		/*
		 * Data Type
		 */
		int precision = data.getPrecision();
		if(precision == 64) {
			DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
			values = new double[doubleBuffer.capacity()];
			for(int index = 0; index < doubleBuffer.capacity(); index++) {
				values[index] = doubleBuffer.get(index);
			}
		} else if(precision == 32) {
			FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
			values = new double[floatBuffer.capacity()];
			for(int index = 0; index < floatBuffer.capacity(); index++) {
				values[index] = floatBuffer.get(index);
			}
		}
		return values;
	}
}
