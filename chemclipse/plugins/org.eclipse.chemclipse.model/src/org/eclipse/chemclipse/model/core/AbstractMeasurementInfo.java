/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.exceptions.InvalidHeaderModificationException;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.support.text.ValueFormat;

public abstract class AbstractMeasurementInfo implements IMeasurementInfo {

	private static final long serialVersionUID = 4247159773898302231L;
	private static final Logger logger = Logger.getLogger(AbstractMeasurementInfo.class);
	/*
	 * Generic Fields
	 */
	public static final String MODULATION_TIME = "Modulation Time"; //$NON-NLS-1$
	/*
	 * Harmonized Header Data
	 */
	private static final String INSTRUMENT = "Instrument"; //$NON-NLS-1$
	private static final String OPERATOR = "Operator"; //$NON-NLS-1$
	private static final String DATE = "Date"; //$NON-NLS-1$
	private static final String MISC_INFO = "Misc Info"; //$NON-NLS-1$
	private static final String MISC_INFO_SEPARATED = "Misc Info Separated"; //$NON-NLS-1$
	private static final String SHORT_INFO = "Short Info"; //$NON-NLS-1$
	private static final String DETAILED_INFO = "Detailed Info"; //$NON-NLS-1$
	private static final String SAMPLE_NAME = "Sample Name"; //$NON-NLS-1$
	private static final String SAMPLE_GROUP = "Sample Group"; //$NON-NLS-1$
	private static final String BARCODE = "Barcode"; //$NON-NLS-1$
	private static final String BARCODE_TYPE = "Barcode Type"; //$NON-NLS-1$
	private static final String SAMPLE_WEIGHT = "Sample Weight"; //$NON-NLS-1$
	private static final String SAMPLE_WEIGHT_UNIT = "Sample Weight Unit"; //$NON-NLS-1$
	private static final String DATA_NAME = "Data Name"; //$NON-NLS-1$
	private static final String FINDINGS = "Findings"; //$NON-NLS-1$
	private static final String TAGS = "Tags"; //$NON-NLS-1$

	private Set<String> protectKeys = new HashSet<>();
	private Map<String, String> headerMap = new HashMap<>();

	private DateFormat dateFormat = ValueFormat.getDateFormatEnglish(ValueFormat.FULL_DATE_PATTERN);

	public AbstractMeasurementInfo() {

		headerMap.put(INSTRUMENT, ""); //$NON-NLS-1$
		headerMap.put(OPERATOR, ""); //$NON-NLS-1$
		headerMap.put(DATE, dateFormat.format(new Date()));
		headerMap.put(MISC_INFO, ""); //$NON-NLS-1$
		headerMap.put(MISC_INFO_SEPARATED, ""); //$NON-NLS-1$
		headerMap.put(SHORT_INFO, ""); //$NON-NLS-1$
		headerMap.put(DETAILED_INFO, ""); //$NON-NLS-1$
		headerMap.put(SAMPLE_NAME, ""); //$NON-NLS-1$
		headerMap.put(SAMPLE_GROUP, ""); //$NON-NLS-1$
		headerMap.put(BARCODE, ""); //$NON-NLS-1$
		headerMap.put(BARCODE_TYPE, ""); //$NON-NLS-1$
		headerMap.put(SAMPLE_WEIGHT, Double.toString(0.0d));
		headerMap.put(SAMPLE_WEIGHT_UNIT, ""); //$NON-NLS-1$
		headerMap.put(DATA_NAME, ""); //$NON-NLS-1$
		headerMap.put(FINDINGS, ""); //$NON-NLS-1$
		headerMap.put(TAGS, ""); //$NON-NLS-1$

		protectKeys.addAll(headerMap.keySet());
	}

	@Override
	public boolean isKeyProtected(String key) {

		return protectKeys.contains(key);
	}

	@Override
	public void addProtectedKey(String key) {

		protectKeys.add(key);
	}

	@Override
	public String getHeaderData(String key) {

		return headerMap.get(key);
	}

	@Override
	public String getHeaderDataOrDefault(String key, String defaultValue) {

		return headerMap.getOrDefault(key, defaultValue);
	}

	@Override
	public boolean headerDataContainsKey(String key) {

		return headerMap.containsKey(key);
	}

	@Override
	public void putHeaderData(String key, String value) {

		headerMap.put(key, value);
	}

	@Override
	public void putHeaderData(Map<String, String> headerData) {

		for(Map.Entry<String, String> entry : headerData.entrySet()) {
			putHeaderData(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void removeHeaderData(String key) throws InvalidHeaderModificationException {

		if(isKeyProtected(key)) {
			throw new InvalidHeaderModificationException("It's not possible to remove the following key: " + key); //$NON-NLS-1$
		} else {
			headerMap.remove(key);
		}
	}

	@Override
	public Map<String, String> getHeaderDataMap() {

		return Collections.unmodifiableMap(headerMap);
	}

	@Override
	public String getInstrument() {

		return getHeaderData(INSTRUMENT);
	}

	@Override
	public void setInstrument(String instrument) {

		putHeaderData(INSTRUMENT, instrument);
	}

	@Override
	public String getOperator() {

		return getHeaderData(OPERATOR);
	}

	@Override
	public void setOperator(String operator) {

		putHeaderData(OPERATOR, operator);
	}

	@Override
	public Date getDate() {

		try {
			return dateFormat.parse(getHeaderData(DATE));
		} catch(ParseException e) {
			logger.warn(e);
			return new Date();
		}
	}

	@Override
	public void setDate(Date date) {

		if(date != null) {
			putHeaderData(DATE, dateFormat.format(date));
		} else {
			putHeaderData(DATE, ""); //$NON-NLS-1$
		}
	}

	@Override
	public String getMiscInfo() {

		return getHeaderData(MISC_INFO);
	}

	@Override
	public void setMiscInfo(String miscInfo) {

		if(miscInfo != null) {
			String[] values = miscInfo.split(PreferenceSupplier.getMiscSeparator());
			if(values.length >= 2) {
				putHeaderData(MISC_INFO, values[0]);
				StringBuilder builder = new StringBuilder();
				for(int i = 1; i < values.length; i++) {
					builder.append(values[i].trim());
					builder.append(PreferenceSupplier.getMiscSeparatedDelimiter());
				}
				putHeaderData(MISC_INFO_SEPARATED, builder.toString().trim());
			} else {
				putHeaderData(MISC_INFO, miscInfo);
			}
		} else {
			putHeaderData(MISC_INFO, ""); //$NON-NLS-1$
			putHeaderData(MISC_INFO_SEPARATED, ""); //$NON-NLS-1$
		}
	}

	@Override
	public String getMiscInfoSeparated() {

		return getHeaderData(MISC_INFO_SEPARATED);
	}

	@Override
	public void setMiscInfoSeparated(String miscInfoSeparated) {

		putHeaderData(MISC_INFO_SEPARATED, miscInfoSeparated);
	}

	@Override
	public String getShortInfo() {

		return getHeaderData(SHORT_INFO);
	}

	@Override
	public void setShortInfo(String shortInfo) {

		if(shortInfo != null) {
			putHeaderData(SHORT_INFO, shortInfo);
		} else {
			putHeaderData(SHORT_INFO, ""); //$NON-NLS-1$
		}
	}

	@Override
	public String getDetailedInfo() {

		return getHeaderData(DETAILED_INFO);
	}

	@Override
	public void setDetailedInfo(String detailedInfo) {

		if(detailedInfo != null) {
			putHeaderData(DETAILED_INFO, detailedInfo);
		} else {
			putHeaderData(DETAILED_INFO, ""); //$NON-NLS-1$
		}
	}

	@Override
	public String getSampleName() {

		return getHeaderData(SAMPLE_NAME);
	}

	@Override
	public void setSampleName(String sampleName) {

		if(sampleName != null) {
			putHeaderData(SAMPLE_NAME, sampleName);
		} else {
			putHeaderData(SAMPLE_NAME, ""); //$NON-NLS-1$
		}
	}

	@Override
	public String getSampleGroup() {

		return getHeaderData(SAMPLE_GROUP);
	}

	@Override
	public void setSampleGroup(String sampleGroup) {

		if(sampleGroup != null) {
			putHeaderData(SAMPLE_GROUP, sampleGroup);
		} else {
			putHeaderData(SAMPLE_GROUP, ""); //$NON-NLS-1$
		}
	}

	@Override
	public String getBarcode() {

		return getHeaderData(BARCODE);
	}

	@Override
	public void setBarcode(String barcode) {

		if(barcode != null) {
			putHeaderData(BARCODE, barcode);
		} else {
			putHeaderData(BARCODE, ""); //$NON-NLS-1$
		}
	}

	@Override
	public String getBarcodeType() {

		return getHeaderData(BARCODE_TYPE);
	}

	@Override
	public void setBarcodeType(String barcodeType) {

		if(barcodeType != null) {
			putHeaderData(BARCODE_TYPE, barcodeType);
		} else {
			putHeaderData(BARCODE_TYPE, ""); //$NON-NLS-1$
		}
	}

	@Override
	public double getSampleWeight() {

		try {
			return Double.parseDouble(getHeaderData(SAMPLE_WEIGHT));
		} catch(Exception e) {
			return 0.0f;
		}
	}

	@Override
	public void setSampleWeight(double sampleWeight) {

		if(sampleWeight >= 0) {
			headerMap.put(SAMPLE_WEIGHT, Double.toString(sampleWeight));
		} else {
			headerMap.put(SAMPLE_WEIGHT, Double.toString(0.0d));
		}
	}

	@Override
	public String getSampleWeightUnit() {

		return getHeaderData(SAMPLE_WEIGHT_UNIT);
	}

	@Override
	public void setSampleWeightUnit(String sampleWeightUnit) {

		if(sampleWeightUnit != null) {
			putHeaderData(SAMPLE_WEIGHT_UNIT, sampleWeightUnit);
		} else {
			putHeaderData(SAMPLE_WEIGHT_UNIT, ""); //$NON-NLS-1$
		}
	}

	@Override
	public String getDataName() {

		return getHeaderData(DATA_NAME);
	}

	@Override
	public void setDataName(String dataName) {

		if(dataName != null) {
			putHeaderData(DATA_NAME, dataName);
		} else {
			putHeaderData(DATA_NAME, ""); //$NON-NLS-1$
		}
	}

	@Override
	public String getFindings() {

		return getHeaderData(FINDINGS);
	}

	@Override
	public void setFindings(String findings) {

		putHeaderData(FINDINGS, findings != null ? findings : ""); //$NON-NLS-1$
	}

	@Override
	public String getTags() {

		return getHeaderData(TAGS);
	}

	@Override
	public void setTags(String tags) {

		putHeaderData(TAGS, tags != null ? tags : ""); //$NON-NLS-1$
	}
}