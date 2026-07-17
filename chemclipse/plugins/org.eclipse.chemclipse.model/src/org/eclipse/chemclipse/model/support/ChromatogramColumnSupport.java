/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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

import java.util.Map;

import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.SeparationColumnFactory;
import org.eclipse.chemclipse.model.columns.SeparationColumnMapping;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.support.ColumnField;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.support.model.SeparationColumnType;

public class ChromatogramColumnSupport {

	public static void parseSeparationColumn(IChromatogram chromatogram) {

		if(PreferenceSupplier.isParseSeparationColumnFromHeader()) {
			if(chromatogram.isParseSeparationColumnEnabled()) {
				ColumnField columnField = PreferenceSupplier.getSeparationColumnField();
				SeparationColumnMapping separationColumnMapping = getSeparationColumnMapping();
				boolean parseReferences = PreferenceSupplier.isParseSeparationColumnReferencedChromatograms();
				parseSeparationColumn(chromatogram, columnField, separationColumnMapping, parseReferences);
			}
		}
	}

	public static void parseSeparationColumn(IChromatogram chromatogram, ColumnField columnField, SeparationColumnMapping separationColumnMapping, boolean parseReferences) {

		if(!separationColumnMapping.isEmpty()) {
			/*
			 * Master
			 */
			if(chromatogram.isParseSeparationColumnEnabled()) {
				mapSeparationColumn(chromatogram, columnField, separationColumnMapping);
			}
			/*
			 * References
			 */
			if(parseReferences) {
				for(IChromatogram chromatogramReference : chromatogram.getReferencedChromatograms()) {
					if(chromatogramReference != null && chromatogramReference.isParseSeparationColumnEnabled()) {
						mapSeparationColumn(chromatogramReference, columnField, separationColumnMapping);
					}
				}
			}
		}
	}

	private static void mapSeparationColumn(IChromatogram chromatogram, ColumnField columnField, SeparationColumnMapping separationColumnMapping) {

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

		return separationColumnMapping;
	}

	private static ISeparationColumn getSeparationColumn(SeparationColumnMapping separationColumnMapping, String mappedField) {

		for(Map.Entry<String, SeparationColumnType> columnMapping : separationColumnMapping.entrySet()) {
			if(mappedField.contains(columnMapping.getKey())) {
				return SeparationColumnFactory.getSeparationColumn(columnMapping.getValue());
			}
		}

		return null;
	}
}