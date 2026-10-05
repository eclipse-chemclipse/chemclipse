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
import org.apache.parquet.schema.Type;

public class ParquetReaderSupport {

	public static final String POINT = "point";
	public static final String CHUNK = "chunk";

	private static final double[] NO_NUMBERS = new double[0];

	private ParquetReaderSupport() {

	}

	public static ParquetFileReader open(Path path) throws IOException {

		ParquetReadOptions readOptions = ParquetReadOptions.builder(new PlainParquetConfiguration()) //
				.withCodecFactory(new ParquetCodecFactory()) //
				.build();
		return new ParquetFileReader(new LocalInputFile(path), readOptions);
	}

	public static RecordReader<Group> getRecordReader(MessageType schema, PageReadStore pageReadStore) {

		return new ColumnIOFactory().getColumnIO(schema).getRecordReader(pageReadStore, new GroupRecordConverter(schema));
	}

	public static Group getGroup(Group row, String field) {

		if(row == null || !row.getType().containsField(field)) {
			return null;
		}
		int index = row.getType().getFieldIndex(field);
		if(row.getFieldRepetitionCount(index) == 0) {
			return null;
		}
		return row.getGroup(index, 0);
	}

	public static double getNumber(Group group, String field, double defaultValue) {

		GroupType groupType = group.getType();
		if(!groupType.containsField(field)) {
			return defaultValue;
		}
		int index = groupType.getFieldIndex(field);
		if(group.getFieldRepetitionCount(index) == 0) {
			return defaultValue;
		}
		double number = readNumber(group, index, 0);
		return Double.isNaN(number) ? defaultValue : number;
	}

	public static String getString(Group group, String field, String defaultValue) {

		GroupType groupType = group.getType();
		if(!groupType.containsField(field)) {
			return defaultValue;
		}
		int index = groupType.getFieldIndex(field);
		if(group.getFieldRepetitionCount(index) == 0) {
			return defaultValue;
		}
		return group.getString(index, 0);
	}

	public static double[] getNumbers(Group group, String field) {

		GroupType groupType = group.getType();
		if(!groupType.containsField(field)) {
			return NO_NUMBERS;
		}
		int index = groupType.getFieldIndex(field);
		if(group.getFieldRepetitionCount(index) == 0) {
			return NO_NUMBERS;
		}
		Group list = group.getGroup(index, 0);
		int size = list.getFieldRepetitionCount(0);
		double[] numbers = new double[size];
		for(int element = 0; element < size; element++) {
			numbers[element] = readElement(list, element);
		}
		return numbers;
	}

	private static double readElement(Group list, int element) {

		if(list.getType().getType(0).isPrimitive()) {
			return readNumber(list, 0, element); // two level list
		}
		Group item = list.getGroup(0, element); // three level list
		if(item.getFieldRepetitionCount(0) == 0) {
			return Double.NaN;
		}
		return readNumber(item, 0, 0);
	}

	private static double readNumber(Group group, int index, int repetition) {

		Type type = group.getType().getType(index);
		if(!type.isPrimitive()) {
			return Double.NaN;
		}
		return switch(type.asPrimitiveType().getPrimitiveTypeName()) {
			case DOUBLE -> group.getDouble(index, repetition);
			case FLOAT -> group.getFloat(index, repetition);
			case INT64 -> group.getLong(index, repetition);
			case INT32 -> group.getInteger(index, repetition);
			default -> Double.NaN;
		};
	}
}
