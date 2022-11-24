/*******************************************************************************
 * Copyright (c) 2022 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.statistics.ISample;

public class SampleTemplateIO {

	public static final String DESCRIPTION = "PCA Sample Template";
	public static final String FILE_EXTENSION = ".pst";
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION;
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION;
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")";
	//
	private static final String VALUE_DELIMITER = "\t";

	public static void write(File file, List<ISample> samples) throws FileNotFoundException {

		try (PrintWriter printWriter = new PrintWriter(file)) {
			/*
			 * Header
			 */
			printWriter.print("Sample Name");
			printWriter.print(VALUE_DELIMITER);
			printWriter.print("Use");
			printWriter.print(VALUE_DELIMITER);
			printWriter.print("Color");
			printWriter.print(VALUE_DELIMITER);
			printWriter.print("Group Name");
			printWriter.print(VALUE_DELIMITER);
			printWriter.print("Classification");
			printWriter.print(VALUE_DELIMITER);
			printWriter.print("Description");
			printWriter.println();
			/*
			 * Data
			 */
			for(ISample sample : samples) {
				printWriter.print(sample.getSampleName());
				printWriter.print(VALUE_DELIMITER);
				printWriter.print(sample.isSelected());
				printWriter.print(VALUE_DELIMITER);
				printWriter.print(sample.getRGB());
				printWriter.print(VALUE_DELIMITER);
				printWriter.print(sample.getGroupName());
				printWriter.print(VALUE_DELIMITER);
				printWriter.print(sample.getClassification());
				printWriter.print(VALUE_DELIMITER);
				printWriter.print(sample.getDescription());
				printWriter.println();
			}
		}
	}

	public static void read(File file, List<ISample> samples) throws IOException {

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			/*
			 * Map the data
			 */
			Map<String, ISample> sampleMap = new HashMap<>();
			for(ISample sample : samples) {
				sampleMap.put(sample.getSampleName(), sample);
			}
			/*
			 * Assign the value.
			 */
			int row = 0;
			String line;
			while((line = bufferedReader.readLine()) != null) {
				/*
				 * First row is the header
				 */
				if(row > 0) {
					String[] values = line.trim().split(VALUE_DELIMITER);
					if(values.length >= 6) {
						String sampleName = values[0].trim();
						ISample sample = sampleMap.get(sampleName);
						if(sample != null) {
							sample.setSelected(Boolean.valueOf(values[1].trim()));
							sample.setRGB(values[2].trim());
							sample.setGroupName(values[3].trim());
							sample.setClassification(values[4].trim());
							sample.setDescription(values[5].trim());
						}
					}
				}
				row++;
			}
		}
	}
}