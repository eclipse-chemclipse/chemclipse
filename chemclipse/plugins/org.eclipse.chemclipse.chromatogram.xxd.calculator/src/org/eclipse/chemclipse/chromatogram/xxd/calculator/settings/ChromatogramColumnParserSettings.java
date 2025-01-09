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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.settings;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.classifier.settings.IChromatogramClassifierSettings;
import org.eclipse.chemclipse.model.columns.SeparationColumnMapping;
import org.eclipse.chemclipse.model.core.support.ColumnField;
import org.eclipse.chemclipse.support.literature.LiteratureReference;
import org.eclipse.chemclipse.support.settings.SystemSettings;
import org.eclipse.chemclipse.support.settings.SystemSettingsStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@SystemSettings(SystemSettingsStrategy.NEW_INSTANCE)
public class ChromatogramColumnParserSettings implements IChromatogramClassifierSettings {

	@JsonProperty(value = "Column Field")
	@JsonPropertyDescription(value = "Extract the column information from the selected header field.")
	private ColumnField columnField = ColumnField.COLUMN_DETAILS;
	@JsonProperty(value = "Separation Column Mapping")
	@JsonPropertyDescription(value = "Use the following mappings to extract the column from the header field.")
	private SeparationColumnMapping separationColumnMapping = new SeparationColumnMapping();
	@JsonProperty(value = "Parse References", defaultValue = "true")
	@JsonPropertyDescription(value = "Parse referenced chromatograms.")
	private boolean parseReferences = true;

	public ColumnField getColumnField() {

		return columnField;
	}

	public void setColumnField(ColumnField columnField) {

		this.columnField = columnField;
	}

	public SeparationColumnMapping getSeparationColumnMapping() {

		return separationColumnMapping;
	}

	public void setSeparationColumnMapping(SeparationColumnMapping separationColumnMapping) {

		this.separationColumnMapping = separationColumnMapping;
	}

	public boolean isParseReferences() {

		return parseReferences;
	}

	public void setParseReferences(boolean parseReferences) {

		this.parseReferences = parseReferences;
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		return null;
	}
}