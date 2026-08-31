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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;

public class ExtractTransferDefinitionIO {

	private static final Logger logger = Logger.getLogger(ExtractTransferDefinitionIO.class);

	public static final String DESCRIPTION = "Extract Transfer Definitions";
	public static final String FILE_EXTENSION = ".etd";
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION;
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION;
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")";

	public static final String SEPARATOR_TOKEN = "\t";

	public static List<ExtractTransferDefinition> importDefinitions(File file) {

		List<ExtractTransferDefinition> definitions = new ArrayList<>();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line;
			while((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if(!line.isEmpty() && !line.startsWith("#")) {
					ExtractTransferDefinition definition = parseDefinition(line);
					if(definition != null) {
						definitions.add(definition);
					}
				}
			}
		} catch(FileNotFoundException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}

		return definitions;
	}

	public static boolean exportDefinitions(File file, List<ExtractTransferDefinition> definitions) {

		boolean success = false;
		try (PrintWriter printWriter = new PrintWriter(file)) {
			for(ExtractTransferDefinition definition : definitions) {
				StringBuilder builder = new StringBuilder();
				builder.append(definition.getSourceField());
				builder.append(SEPARATOR_TOKEN);
				builder.append(definition.getRegularExpression());
				builder.append(SEPARATOR_TOKEN);
				builder.append(definition.getGroupIndex());
				builder.append(SEPARATOR_TOKEN);
				builder.append(definition.getDataType().name());
				builder.append(SEPARATOR_TOKEN);
				builder.append(definition.getSinkField());
				builder.append(SEPARATOR_TOKEN);
				builder.append(definition.isUse());
				printWriter.println(builder.toString());
			}
			printWriter.flush();
			success = true;
		} catch(FileNotFoundException e) {
			logger.warn(e);
		}
		return success;
	}

	private static ExtractTransferDefinition parseDefinition(String line) {

		String[] parts = line.split(SEPARATOR_TOKEN);
		if(parts.length >= 5) {
			try {
				String sourceField = parts[0].trim();
				String regularExpression = parts[1];
				int groupIndex = Integer.parseInt(parts[2].trim());
				DataType dataType = DataType.valueOf(parts[3].trim());
				String sinkField = parts[4].trim();
				ExtractTransferDefinition definition = new ExtractTransferDefinition(sourceField, regularExpression, groupIndex, dataType, sinkField);
				if(parts.length >= 6) {
					definition.setUse(Boolean.parseBoolean(parts[5].trim()));
				}
				return definition;
			} catch(Exception e) {
				logger.warn(e);
			}
		}
		return null;
	}
}