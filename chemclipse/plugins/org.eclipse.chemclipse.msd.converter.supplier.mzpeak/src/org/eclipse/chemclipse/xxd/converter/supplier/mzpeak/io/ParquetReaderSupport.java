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

import java.io.IOException;
import java.nio.file.Path;

import org.apache.parquet.ParquetReadOptions;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.conf.PlainParquetConfiguration;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.io.LocalInputFile;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.GroupType;
import org.apache.parquet.schema.MessageType;

/**
 * Reads the data tables of an mzPeak package, which store their values in a nested "point" group.
 */
public class ParquetReaderSupport {

	public static final String POINT = "point";

	private ParquetReaderSupport() {

	}

	/**
	 * Opens the given Parquet file without touching Hadoop configuration or native codecs.
	 */
	public static ParquetFileReader open(Path path) throws IOException {

		ParquetReadOptions readOptions = ParquetReadOptions.builder(new PlainParquetConfiguration()) //
				.withCodecFactory(new ParquetCodecFactory()) //
				.build();
		return new ParquetFileReader(new LocalInputFile(path), readOptions);
	}

	public static RecordReader<Group> getRecordReader(MessageType schema, PageReadStore pageReadStore) {

		return new ColumnIOFactory().getColumnIO(schema).getRecordReader(pageReadStore, new GroupRecordConverter(schema));
	}

	/**
	 * Returns the "point" group of the next row, or null when the row carries none.
	 */
	public static Group readPoint(RecordReader<Group> recordReader) {

		Group row = recordReader.read();
		if(row == null || !row.getType().containsField(POINT)) {
			return null;
		}
		if(row.getFieldRepetitionCount(row.getType().getFieldIndex(POINT)) == 0) {
			return null;
		}
		return row.getGroup(POINT, 0);
	}

	/**
	 * Reads a numeric field regardless of the primitive type the writer chose for it. Profile
	 * spectra leave entries empty, hence the default value for absent and null fields.
	 */
	public static double getNumber(Group point, String field, double defaultValue) {

		GroupType groupType = point.getType();
		if(!groupType.containsField(field)) {
			return defaultValue;
		}
		int index = groupType.getFieldIndex(field);
		if(point.getFieldRepetitionCount(index) == 0) {
			return defaultValue;
		}
		return switch(groupType.getType(index).asPrimitiveType().getPrimitiveTypeName()) {
			case DOUBLE -> point.getDouble(index, 0);
			case FLOAT -> point.getFloat(index, 0);
			case INT64 -> point.getLong(index, 0);
			case INT32 -> point.getInteger(index, 0);
			default -> defaultValue;
		};
	}
}
