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
package org.eclipse.chemclipse.model.core;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.chemclipse.model.core.support.HeaderField;

public class FileHeaderData {

	private HeaderField headerField;
	private String regularExpression;
	private int groupIndex;
	/*
	 * Transient
	 */
	private Pattern pattern = null;

	public FileHeaderData() {

		/*
		 * Extract all data from the given field.
		 */
		this(HeaderField.DEFAULT, "(.*)", 0); //$NON-NLS-1$
	}

	public FileHeaderData(HeaderField headerField, String regularExpression, int groupIndex) {

		this.headerField = headerField;
		this.regularExpression = regularExpression;
		this.groupIndex = groupIndex;
	}

	public HeaderField getHeaderField() {

		return headerField;
	}

	public void setHeaderField(HeaderField headerField) {

		this.headerField = headerField;
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

	/**
	 * If the regular expression is blank, then
	 * it indicates that the file header data shall
	 * not be used to extract data. It could be happen
	 * in a dynamic context to extract header data
	 * on demand.
	 * 
	 * @return
	 */
	public boolean isUse() {

		return !regularExpression.isBlank();
	}

	/**
	 * Might return null. Check isUse() first.
	 * 
	 * @return Pattern
	 */
	public Pattern getPattern() throws PatternSyntaxException {

		if(isUse()) {
			if(pattern == null) {
				pattern = Pattern.compile(regularExpression);
			}
			return pattern;
		}

		return null;
	}

	@Override
	public int hashCode() {

		return Objects.hash(groupIndex, headerField, regularExpression);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		FileHeaderData other = (FileHeaderData)obj;
		return groupIndex == other.groupIndex && headerField == other.headerField && Objects.equals(regularExpression, other.regularExpression);
	}

	@Override
	public String toString() {

		return "FileHeaderData [headerField=" + headerField + ", regularExpression=" + regularExpression + ", groupIndex=" + groupIndex + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}