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
package org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.io;

import java.util.Arrays;
import java.util.Map;

import org.apache.parquet.example.data.Group;
import org.eclipse.chemclipse.logging.core.Logger;

import ms.numpress.MSNumpress;

public class ChunkReaderSupport {

	private static final Logger logger = Logger.getLogger(ChunkReaderSupport.class);

	private static final String CHUNK_ENCODING = "chunk_encoding";
	private static final String CHUNK_START = "_chunk_start";
	private static final String CHUNK_VALUES = "_chunk_values";

	private static final String NO_COMPRESSION = "MS:1000576";
	private static final String DELTA_PREDICTION = "MS:1003089";

	private static final Map<String, String> NUMPRESS_SCHEMES = Map.of( //
			MSNumpress.ACC_NUMPRESS_LINEAR, "linear", // linear prediction compression
			MSNumpress.ACC_NUMPRESS_PIC, "pic", // positive integer compression
			MSNumpress.ACC_NUMPRESS_SLOF, "slof"); // short logged float compression

	private ChunkReaderSupport() {

	}

	/**
	 * Returns the values an array of the chunk holds, decoded when MS Numpress was used to write
	 * them, and an empty array when the chunk holds no such array.
	 * 
	 * @param array
	 *            the name the array has in this table, e.g. "intensity"
	 */
	public static double[] readValues(Group chunk, String array) {

		for(String accession : NUMPRESS_SCHEMES.keySet()) {
			double[] values = readNumpress(chunk, array, accession);
			if(values != null) {
				return values;
			}
		}
		return ParquetReaderSupport.getNumbers(chunk, array);
	}

	/**
	 * Returns one axis value per intensity of the chunk, in the order the chunk stores them.
	 * A value the writer left null separates two runs of points and is reported as
	 * {@link Double#NaN}, as is every value of a chunk whose encoding is not supported.
	 * 
	 * @param axis
	 *            the name the axis has in this table, e.g. "mz" or "time"
	 * @param count
	 *            the number of intensities of the chunk
	 */
	public static double[] readAxis(Group chunk, String axis, int count) {

		double[] axisValues = new double[count];
		String encoding = ParquetReaderSupport.getString(chunk, CHUNK_ENCODING, NO_COMPRESSION);
		if(NUMPRESS_SCHEMES.containsKey(encoding)) {
			/*
			 * MS Numpress carries every value of the chunk, the one "<axis>_chunk_start" would hold
			 * included, and restores absolute coordinates on its own.
			 */
			return fit(readNumpress(chunk, axis, encoding), count);
		}
		boolean isDeltaPredicted = DELTA_PREDICTION.equals(encoding);
		if(!isDeltaPredicted && !NO_COMPRESSION.equals(encoding)) {
			logger.warn("Unsupported chunk encoding: " + encoding);
			Arrays.fill(axisValues, Double.NaN);
			return axisValues;
		}
		double[] values = ParquetReaderSupport.getNumbers(chunk, axis + CHUNK_VALUES);
		/*
		 * A chunk that opens with a data point keeps that first value in "<axis>_chunk_start" and
		 * therefore holds one value less than it holds intensities. A chunk that opens with a gap
		 * carries the null marker of that gap in the values instead, so both are of equal length.
		 */
		boolean startsWithValue = values.length == count - 1;
		double previous = Double.NaN;
		for(int i = 0; i < count; i++) {
			if(startsWithValue && i == 0) {
				previous = ParquetReaderSupport.getNumber(chunk, axis + CHUNK_START, Double.NaN);
			} else {
				int index = startsWithValue ? i - 1 : i;
				double value = index < values.length ? values[index] : Double.NaN;
				if(Double.isNaN(value) || !isDeltaPredicted) {
					previous = value;
				} else {
					// the first value after a gap restarts the prediction with an absolute coordinate
					previous = Double.isNaN(previous) ? value : previous + value;
				}
			}
			axisValues[i] = previous;
		}
		return axisValues;
	}

	private static double[] readNumpress(Group chunk, String array, String accession) {

		byte[] buffer = ParquetReaderSupport.getBytes(chunk, array + "_numpress_" + NUMPRESS_SCHEMES.get(accession) + "_bytes");
		if(buffer.length == 0) {
			return null;
		}
		try {
			return MSNumpress.decode(accession, buffer, buffer.length);
		} catch(IllegalArgumentException e) {
			logger.warn(e);
			return null;
		}
	}

	private static double[] fit(double[] values, int count) {

		if(values != null && values.length == count) {
			return values;
		}
		double[] axisValues = new double[count];
		Arrays.fill(axisValues, Double.NaN);
		if(values != null) {
			System.arraycopy(values, 0, axisValues, 0, Math.min(values.length, count));
		}
		return axisValues;
	}
}
