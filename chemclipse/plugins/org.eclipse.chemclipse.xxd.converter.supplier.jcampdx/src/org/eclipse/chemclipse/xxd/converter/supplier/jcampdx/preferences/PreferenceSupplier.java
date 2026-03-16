/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.converter.supplier.jcampdx.preferences;

import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final String P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_1 = "separationColumnTypeRetentionIndex1";
	public static final String DEF_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_1 = SeparationColumnType.NON_POLAR.name();
	public static final String P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_2 = "separationColumnTypeRetentionIndex2";
	public static final String DEF_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_2 = SeparationColumnType.POLAR.name();
	public static final String P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_3 = "separationColumnTypeRetentionIndex3";
	public static final String DEF_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_3 = SeparationColumnType.SEMI_POLAR.name();

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_1, DEF_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_1);
		putDefault(P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_2, DEF_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_2);
		putDefault(P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_3, DEF_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_3);
	}

	public static SeparationColumnType getSeparationColumnTypeRetentionIndex1() {

		return getSeparationColumnType(INSTANCE().get(P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_1, DEF_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_1));
	}

	public static void setSeparationColumnTypeRetentionIndex1(SeparationColumnType separationColumnType) {

		INSTANCE().put(P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_1, separationColumnType.name());
	}

	public static SeparationColumnType getSeparationColumnTypeRetentionIndex2() {

		return getSeparationColumnType(INSTANCE().get(P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_2, DEF_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_2));
	}

	public static void setSeparationColumnTypeRetentionIndex2(SeparationColumnType separationColumnType) {

		INSTANCE().put(P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_2, separationColumnType.name());
	}

	public static SeparationColumnType getSeparationColumnTypeRetentionIndex3() {

		return getSeparationColumnType(INSTANCE().get(P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_3, DEF_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_3));
	}

	public static void setSeparationColumnTypeRetentionIndex3(SeparationColumnType separationColumnType) {

		INSTANCE().put(P_SEPARATION_COLUMN_TYPE_RETENTION_INDEX_3, separationColumnType.name());
	}

	private static SeparationColumnType getSeparationColumnType(String name) {

		try {
			return SeparationColumnType.valueOf(name);
		} catch(Exception e) {
			return SeparationColumnType.DEFAULT;
		}
	}
}