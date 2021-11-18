/*******************************************************************************
 * Copyright (c) 2018, 2021 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.columns;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;

public class SeparationColumnFactory {

	private static final Logger logger = Logger.getLogger(SeparationColumnType.class);

	public static String getColumnLabel(ISeparationColumn separationColumn, int nameLength) {

		StringBuilder builder = new StringBuilder();
		if(separationColumn != null) {
			String name = separationColumn.getName();
			if(name.length() > nameLength) {
				name = name.substring(0, nameLength);
			}
			builder.append(name);
			builder.append(" (");
			builder.append(separationColumn.getSeparationColumnType().label());
			builder.append(")");
		}
		//
		return builder.toString().trim();
	}

	public static List<ISeparationColumn> getSeparationColumns() {

		List<ISeparationColumn> separationColumns = new ArrayList<>();
		separationColumns.add(getSeparationColumn(SeparationColumnType.DEFAULT));
		separationColumns.add(getSeparationColumn(SeparationColumnType.POLAR));
		separationColumns.add(getSeparationColumn(SeparationColumnType.APOLAR));
		separationColumns.add(getSeparationColumn(SeparationColumnType.SEMI_POLAR));
		return separationColumns;
	}

	public static ISeparationColumn getSeparationColumn(String name) {

		return getSeparationColumn(name, "", "", "");
	}

	public static ISeparationColumn getSeparationColumn(String name, String length, String diameter, String phase) {

		SeparationColumnType separationColumnType = mapNameToColumnType(name);
		return new SeparationColumn(name, separationColumnType, length, diameter, phase);
	}

	public static ISeparationColumn getSeparationColumn(SeparationColumnType separationColumnType) {

		return new SeparationColumn(separationColumnType.label(), separationColumnType, "", "", "");
	}

	public static ISeparationColumnIndices getSeparationColumnIndices(SeparationColumnType separationColumnType) {

		ISeparationColumnIndices separationColumnIndices = new SeparationColumnIndices();
		ISeparationColumn separationColumn = separationColumnIndices.getSeparationColumn();
		separationColumn.setName(separationColumnType.name());
		separationColumn.setSeparationColumnType(separationColumnType);
		return separationColumnIndices;
	}

	private static SeparationColumnType mapNameToColumnType(String name) {

		/*
		 * Load the user defined column mappings and
		 * add the default mappings to match the basic 3 column types.
		 */
		SeparationColumnMapping columnMappings = new SeparationColumnMapping();
		columnMappings.load(PreferenceSupplier.getSeparationColumnMappings());
		columnMappings.put(SeparationColumnType.POLAR.name(), SeparationColumnType.POLAR.name());
		columnMappings.put(SeparationColumnType.APOLAR.name(), SeparationColumnType.APOLAR.name());
		columnMappings.put(SeparationColumnType.SEMI_POLAR.name(), SeparationColumnType.SEMI_POLAR.name());
		/*
		 * If no mapping is available or the value can't be parsed
		 * by SeparationColumnType, then DEFAULT is returned.
		 */
		SeparationColumnType separationColumnType = SeparationColumnType.DEFAULT;
		String value = columnMappings.get(name);
		if(value != null) {
			try {
				separationColumnType = SeparationColumnType.valueOf(value);
			} catch(Exception e) {
				logger.warn(e);
			}
		}
		//
		return separationColumnType;
	}
}