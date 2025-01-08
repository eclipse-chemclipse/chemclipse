/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.PeakListBinaryType;

public class WriterVersion105 {

	public static PeakListBinaryType createFromFloats(float[] floats) {

		FloatBuffer floatBuffer = FloatBuffer.wrap(floats);
		ByteBuffer byteBuffer = ByteBuffer.allocate(floatBuffer.capacity() * Float.BYTES);
		byteBuffer.asFloatBuffer().put(floatBuffer);
		return createBinaryType(byteBuffer, 32);
	}

	public static PeakListBinaryType createFromDoubles(double[] doubles) {

		DoubleBuffer doubleBuffer = DoubleBuffer.wrap(doubles);
		ByteBuffer byteBuffer = ByteBuffer.allocate(doubleBuffer.capacity() * Double.BYTES);
		byteBuffer.asDoubleBuffer().put(doubleBuffer);
		return createBinaryType(byteBuffer, 64);
	}

	private static PeakListBinaryType createBinaryType(ByteBuffer byteBuffer, int precision) {

		Data data = new Data();
		data.setPrecision(precision);
		byteBuffer.order(ByteOrder.BIG_ENDIAN);
		data.setEndian("big");
		data.setValue(byteBuffer.array());
		data.setLength(byteBuffer.capacity());
		PeakListBinaryType peakListBinaryType = new PeakListBinaryType();
		peakListBinaryType.setData(data);
		return peakListBinaryType;
	}
}
