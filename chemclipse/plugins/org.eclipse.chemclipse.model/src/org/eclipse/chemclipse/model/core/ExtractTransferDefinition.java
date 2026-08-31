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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ExtractTransferDefinition {

	private boolean use = true;
	private String sourceField;
	private String regularExpression;
	private int groupIndex;
	private DataType dataType;
	private String sinkField;
	/*
	 * Transient
	 */
	private Pattern pattern = null;

	public ExtractTransferDefinition(String sourceField, String regularExpression, int groupIndex, DataType dataType, String sinkField) {

		this.sourceField = sourceField;
		this.regularExpression = regularExpression;
		this.groupIndex = groupIndex;
		this.dataType = dataType;
		this.sinkField = sinkField;
	}

	public boolean isUse() {

		return use;
	}

	public void setUse(boolean use) {

		this.use = use;
	}

	public String getSourceField() {

		return sourceField;
	}

	public void setSourceField(String sourceField) {

		this.sourceField = sourceField;
	}

	public String getRegularExpression() {

		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {

		this.regularExpression = regularExpression;
		this.pattern = null;
	}

	public int getGroupIndex() {

		return groupIndex;
	}

	public void setGroupIndex(int groupIndex) {

		this.groupIndex = groupIndex;
	}

	public DataType getDataType() {

		return dataType;
	}

	public void setDataType(DataType dataType) {

		this.dataType = dataType;
	}

	public String getSinkField() {

		return sinkField;
	}

	public void setSinkField(String sinkField) {

		this.sinkField = sinkField;
	}

	public Object getResult(String content) {

		Object result = null;
		if(!content.isBlank()) {
			Pattern pattern = getPattern();
			if(pattern != null) {
				Matcher matcher = pattern.matcher(content);
				if(matcher.matches()) {
					try {
						String group = matcher.group(groupIndex).trim();
						switch(dataType) {
							case STRING:
								result = group;
								break;
							case BOOLEAN:
								result = Boolean.parseBoolean(group);
								break;
							case INTEGER:
								result = Integer.parseInt(group);
								break;
							case FLOAT:
								result = Float.parseFloat(group);
								break;
							case DOUBLE:
								result = Double.parseDouble(group);
								break;
							default:
								break;
						}
					} catch(Exception e) {
					}
				}
			}
		}

		return result;
	}

	/**
	 * Might return null. Check isUse() first.
	 * 
	 * @return Pattern
	 */
	private Pattern getPattern() throws PatternSyntaxException {

		if(!regularExpression.isBlank()) {
			if(pattern == null) {
				pattern = Pattern.compile(regularExpression);
			}
			return pattern;
		}

		return null;
	}

	@Override
	public int hashCode() {

		return Objects.hash(groupIndex, sourceField, regularExpression);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		ExtractTransferDefinition other = (ExtractTransferDefinition)obj;
		return groupIndex == other.groupIndex && sourceField == other.sourceField && Objects.equals(regularExpression, other.regularExpression);
	}

	@Override
	public String toString() {

		return "ExtractTransferDefinition [sourceField=" + sourceField + ", regularExpression=" + regularExpression + ", groupIndex=" + groupIndex + "]";
	}
}