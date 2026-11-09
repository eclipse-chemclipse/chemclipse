/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.support.model.SeparationColumnType;

public class ColumnIndexSupport {

	public static final String COLUMN_TYPE_CHROMATOGRAM = "{chromatogram}"; //$NON-NLS-1$
	public static final String COLUMN_TYPES = "polar" + "\n" + // //$NON-NLS-1$ //$NON-NLS-2$
			"semi-polar" + "\n" + // //$NON-NLS-1$ //$NON-NLS-2$
			"non-polar (apolar)" + "\n" + // //$NON-NLS-1$ //$NON-NLS-2$
			"DB-5"; //$NON-NLS-1$
	public static final String COLUMN_TYPE_DESCRIPTION = "Select the column that shall be used to select the retention index. Use e.g.:\n" + // //$NON-NLS-1$
			COLUMN_TYPE_CHROMATOGRAM + "\n" + // //$NON-NLS-1$
			COLUMN_TYPES;

	public static float getRetentionIndex(List<IColumnIndexMarker> columnIndexMarkers, String searchColumn, boolean caseSensitive, boolean removeWhiteSpace, boolean matchPartly) {

		return getRetentionIndex(0.0f, columnIndexMarkers, searchColumn, caseSensitive, removeWhiteSpace, matchPartly);
	}

	public static float getRetentionIndex(float retentionIndexTarget, List<IColumnIndexMarker> columnIndexMarkers, String searchColumn, boolean caseSensitive, boolean removeWhiteSpace, boolean matchPartly) {

		float retentionIndexReference = 0.0f;
		List<Float> retentionIndices = getRetentionIndices(columnIndexMarkers, searchColumn, caseSensitive, removeWhiteSpace, matchPartly);
		if(!retentionIndices.isEmpty()) {
			float deltaReference = Float.MAX_VALUE;
			for(float retentionIndex : retentionIndices) {
				float delta = Math.abs(retentionIndexTarget - retentionIndex);
				if(delta < deltaReference) {
					retentionIndexReference = retentionIndex;
					deltaReference = delta;
				}
			}
		}

		return retentionIndexReference;
	}

	public static List<IColumnIndexMarker> getColumnIndexMarker(List<IColumnIndexMarker> columnIndexMarkers, String searchColumn, boolean caseSensitive, boolean removeWhiteSpace, boolean matchPartly, SeparationColumnType separationColumnTypeFallback) {

		List<IColumnIndexMarker> columnIndexMarkersMatched = new ArrayList<>();
		searchColumn = adjustValue(searchColumn, caseSensitive, removeWhiteSpace);
		/*
		 * Search
		 */
		if(!searchColumn.isBlank()) {
			for(IColumnIndexMarker columnIndexMarker : columnIndexMarkers) {
				ISeparationColumn separationColumn = columnIndexMarker.getSeparationColumn();
				String separationColumnType = adjustValue(separationColumn.getSeparationColumnType().label(), caseSensitive, removeWhiteSpace);
				String name = adjustValue(separationColumn.getName(), caseSensitive, removeWhiteSpace);
				if(matchPartly) {
					if(separationColumnType.contains(searchColumn) || name.contains(searchColumn)) {
						columnIndexMarkersMatched.add(columnIndexMarker);
					}
				} else {
					if(separationColumnType.equals(searchColumn) || name.equals(searchColumn)) {
						columnIndexMarkersMatched.add(columnIndexMarker);
					}
				}

			}
		}
		/*
		 * Fallback
		 */
		if(columnIndexMarkersMatched.isEmpty()) {
			if(!SeparationColumnType.DEFAULT.equals(separationColumnTypeFallback)) {
				for(IColumnIndexMarker columnIndexMarker : columnIndexMarkers) {
					ISeparationColumn separationColumn = columnIndexMarker.getSeparationColumn();
					if(separationColumn.getSeparationColumnType().equals(separationColumnTypeFallback)) {
						columnIndexMarkersMatched.add(columnIndexMarker);
					}
				}
			}
		}

		return columnIndexMarkersMatched;
	}

	protected static String adjustValue(String value, boolean caseSensitive, boolean removeWhiteSpace) {

		value = removeWhiteSpace ? value.replace(" ", "") : value; //$NON-NLS-1$ //$NON-NLS-2$
		return caseSensitive ? value : value.toLowerCase();
	}

	private static List<Float> getRetentionIndices(List<IColumnIndexMarker> columnIndexMarkers, String searchColumn, boolean caseSensitive, boolean removeWhiteSpace, boolean matchPartly) {

		List<Float> retentionIndices = new ArrayList<>();
		List<IColumnIndexMarker> columnIndexMarkersSorted = new ArrayList<>(columnIndexMarkers);
		Collections.sort(columnIndexMarkersSorted, (c1, c2) -> Float.compare(c1.getRetentionIndex(), c2.getRetentionIndex()));
		searchColumn = adjustValue(searchColumn, caseSensitive, removeWhiteSpace);

		if(!searchColumn.isBlank()) {
			for(IColumnIndexMarker columnIndexMarker : columnIndexMarkersSorted) {
				ISeparationColumn separationColumn = columnIndexMarker.getSeparationColumn();
				String separationColumnType = adjustValue(separationColumn.getSeparationColumnType().label(), caseSensitive, removeWhiteSpace);
				String name = adjustValue(separationColumn.getName(), caseSensitive, removeWhiteSpace);

				if(matchPartly) {
					if(separationColumnType.contains(searchColumn) || name.contains(searchColumn)) {
						retentionIndices.add(columnIndexMarker.getRetentionIndex());
					}
				} else {
					if(separationColumnType.equals(searchColumn) || name.equals(searchColumn)) {
						retentionIndices.add(columnIndexMarker.getRetentionIndex());
					}
				}
			}
		}

		return retentionIndices;
	}
}