/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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

import java.util.Map;

import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.SeparationColumnFactory;
import org.eclipse.chemclipse.model.columns.SeparationColumnMapping;
import org.eclipse.chemclipse.model.columns.SeparationColumnType;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.support.ColumnField;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;

public class ChromatogramColumnSupport {

	public static void parseSeparationColumn(IChromatogram<?> chromatogram) {

		if(PreferenceSupplier.isParseSeparationColumnFromHeader()) {
			ColumnField columnField = PreferenceSupplier.getSeparationColumnField();
			SeparationColumnMapping separationColumnMapping = getSeparationColumnMapping();
			boolean parseReferences = PreferenceSupplier.isParseSeparationColumnReferencedChromatograms();
			parseSeparationColumn(chromatogram, columnField, separationColumnMapping, parseReferences);
		}
	}

	public static void parseSeparationColumn(IChromatogram<?> chromatogram, ColumnField columnField, SeparationColumnMapping separationColumnMapping, boolean parseReferences) {

		if(!separationColumnMapping.isEmpty()) {
			mapSeparationColumn(chromatogram, columnField, separationColumnMapping);
			if(parseReferences) {
				for(IChromatogram<?> chromatogramReference : chromatogram.getReferencedChromatograms()) {
					mapSeparationColumn(chromatogramReference, columnField, separationColumnMapping);
				}
			}
		}
	}

	private static void mapSeparationColumn(IChromatogram<?> chromatogram, ColumnField columnField, SeparationColumnMapping separationColumnMapping) {

		String mappedData = ColumnUtil.getColumnData(chromatogram, columnField, "");
		if(!mappedData.isEmpty()) {
			ISeparationColumn separationColumn = getSeparationColumn(separationColumnMapping, mappedData);
			if(separationColumn != null) {
				chromatogram.getSeparationColumnIndices().setSeparationColumn(separationColumn);
			}
		}
	}

	private static SeparationColumnMapping getSeparationColumnMapping() {

		SeparationColumnMapping separationColumnMapping = new SeparationColumnMapping();
		separationColumnMapping.load(PreferenceSupplier.getSeparationColumnMappings());
		//
		return separationColumnMapping;
	}

	private static ISeparationColumn getSeparationColumn(SeparationColumnMapping separationColumnMapping, String mappedField) {

		for(Map.Entry<String, SeparationColumnType> columnMapping : separationColumnMapping.entrySet()) {
			if(mappedField.contains(columnMapping.getKey())) {
				return SeparationColumnFactory.getSeparationColumn(columnMapping.getValue());
			}
		}
		//
		return null;
	}
}