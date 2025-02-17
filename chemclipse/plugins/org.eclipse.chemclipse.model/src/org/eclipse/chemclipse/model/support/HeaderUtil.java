/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
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

import java.io.File;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.support.HeaderField;

public class HeaderUtil {

	public static String getHeaderData(IChromatogram chromatogram, HeaderField headerField, String defaultData) {

		String headerData = null;
		//
		if(chromatogram != null) {
			if(headerField != null) {
				switch(headerField) {
					case NAME:
						headerData = validate(chromatogram.getName());
						break;
					case SAMPLE_NAME:
						headerData = validate(chromatogram.getSampleName());
						break;
					case DATA_NAME:
						headerData = validate(chromatogram.getDataName());
						break;
					case SHORT_INFO:
						headerData = validate(chromatogram.getShortInfo());
						break;
					case SAMPLE_GROUP:
						headerData = validate(chromatogram.getSampleGroup());
						break;
					case MISC_INFO:
						headerData = validate(chromatogram.getMiscInfo());
						break;
					case TAGS:
						headerData = validate(chromatogram.getTags());
						break;
					default:
						/*
						 * Do nothing, see check default.
						 */
						break;
				}
			}
		}
		//
		if(headerData == null || headerData.isEmpty()) {
			return defaultData;
		} else {
			return headerData;
		}
	}

	public static void setHeaderData(IChromatogram chromatogram, HeaderField headerField, File file) {

		if(chromatogram != null) {
			if(headerField != null) {
				if(file != null) {
					String name = file.getName();
					setHeaderData(chromatogram, headerField, name, false);
					switch(headerField) {
						case NAME:
							chromatogram.setFile(file);
							break;
						default:
							chromatogram.setFile(file);
							break;
					}
				}
			}
		}
	}

	public static void setHeaderData(IChromatogram chromatogram, HeaderField headerField, String headerData) {

		setHeaderData(chromatogram, headerField, headerData, true);
	}

	public static void setHeaderData(IChromatogram chromatogram, HeaderField headerField, String headerData, boolean setFileByName) {

		if(chromatogram != null) {
			if(headerField != null) {
				if(headerData != null) {
					switch(headerField) {
						case NAME:
							if(setFileByName) {
								chromatogram.setFile(new File(headerData));
							}
							break;
						case DATA_NAME:
							chromatogram.setDataName(headerData);
							break;
						case MISC_INFO:
							chromatogram.setMiscInfo(headerData);
							break;
						case SAMPLE_GROUP:
							chromatogram.setSampleGroup(headerData);
							break;
						case SAMPLE_NAME:
							chromatogram.setSampleName(headerData);
							break;
						case SHORT_INFO:
							chromatogram.setShortInfo(headerData);
							break;
						case TAGS:
							chromatogram.setTags(headerData);
							break;
						default:
							break;
					}
				}
			}
		}
	}

	public static HeaderField getHeaderField(String value) {

		try {
			return HeaderField.valueOf(value);
		} catch(Exception e) {
			return HeaderField.NAME;
		}
	}

	private static String validate(String value) {

		return value == null || value.isEmpty() ? null : value;
	}
}