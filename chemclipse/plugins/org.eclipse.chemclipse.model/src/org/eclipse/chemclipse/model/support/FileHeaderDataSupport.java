/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.support;

import org.eclipse.chemclipse.model.core.FileHeaderData;
import org.eclipse.chemclipse.model.core.support.HeaderField;

public class FileHeaderDataSupport {

	/*
	 * A line break is not supported by this regular expression header field option.
	 */
	public static final String VALUE_DELIMITER = "====="; //$NON-NLS-1$
	public static final String SETTING_DEFAULT = "SHORT_INFO=====(.*)=====0"; //$NON-NLS-1$

	public static void load(FileHeaderData fileHeaderData, String value) {

		/*
		 * Escape the delimiter.
		 */
		String[] values = value.split("\\" + VALUE_DELIMITER); //$NON-NLS-1$
		if(values.length == 3) {
			fileHeaderData.setHeaderField(getHeaderField(values[0]));
			fileHeaderData.setRegularExpression(values[1].trim());
			fileHeaderData.setGroupIndex(getGroupIndex(values[2]));
		}
	}

	public static String save(FileHeaderData fileHeaderData) {

		StringBuilder builder = new StringBuilder();

		builder.append(fileHeaderData.getHeaderField().name());
		builder.append(VALUE_DELIMITER);
		builder.append(fileHeaderData.getRegularExpression());
		builder.append(VALUE_DELIMITER);
		builder.append(Integer.toString(fileHeaderData.getGroupIndex()));

		return builder.toString();
	}

	private static HeaderField getHeaderField(String value) {

		try {
			return HeaderField.valueOf(value.trim());
		} catch(Exception e) {
			return HeaderField.DEFAULT;
		}
	}

	private static int getGroupIndex(String value) {

		try {
			return Integer.valueOf(value.trim());
		} catch(NumberFormatException e) {
			return 0;
		}
	}
}