/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.support.ColumnField;
import org.eclipse.chemclipse.model.core.support.HeaderField;

public class ColumnUtil {

	public static String getColumnData(IChromatogram chromatogram, ColumnField columnField, String defaultData) {

		String columnData = null;
		//
		if(chromatogram != null) {
			if(columnField != null) {
				if(ColumnField.COLUMN_DETAILS.equals(columnField)) {
					columnData = chromatogram.getColumnDetails();
				} else {
					HeaderField headerField;
					switch(columnField) {
						case NAME:
							headerField = HeaderField.NAME;
							break;
						case DATA_NAME:
							headerField = HeaderField.DATA_NAME;
							break;
						case SAMPLE_NAME:
							headerField = HeaderField.SAMPLE_NAME;
							break;
						case SAMPLE_GROUP:
							headerField = HeaderField.SAMPLE_GROUP;
							break;
						case SHORT_INFO:
							headerField = HeaderField.SHORT_INFO;
							break;
						case MISC_INFO:
							headerField = HeaderField.MISC_INFO;
							break;
						case TAGS:
							headerField = HeaderField.TAGS;
							break;
						default:
							headerField = HeaderField.DEFAULT;
							break;
					}
					//
					columnData = HeaderUtil.getHeaderData(chromatogram, headerField, defaultData);
				}
			}
		}
		//
		if(columnData == null || columnData.isEmpty()) {
			return defaultData;
		} else {
			return columnData;
		}
	}

	public static ColumnField getColumnField(String value) {

		try {
			return ColumnField.valueOf(value);
		} catch(Exception e) {
			return ColumnField.COLUMN_DETAILS;
		}
	}
}