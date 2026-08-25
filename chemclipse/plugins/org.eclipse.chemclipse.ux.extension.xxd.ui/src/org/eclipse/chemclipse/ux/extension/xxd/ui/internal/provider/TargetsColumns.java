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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.identifier.ComparisonMetrics;
import org.eclipse.chemclipse.model.identifier.IComparisonMetric;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;

public class TargetsColumns {

	private static final int WIDTH_METRIC = 100;

	private static final TargetColumn[] COLUMNS_LEADING = { //
			TargetColumn.VERIFIED, //
			TargetColumn.RATING, //
			TargetColumn.NAME, //
			TargetColumn.CAS //
	};

	private static final TargetColumn[] COLUMNS_TRAILING = { //
			TargetColumn.FORMULA, //
			TargetColumn.SMILES, //
			TargetColumn.INCHI, //
			TargetColumn.INCHI_KEY, //
			TargetColumn.MOL_WEIGHT, //
			TargetColumn.EXACT_MASS, //
			TargetColumn.ADVICE, //
			TargetColumn.IDENTIFIER, //
			TargetColumn.MISCELLANEOUS, //
			TargetColumn.COMMENTS, //
			TargetColumn.DATABASE, //
			TargetColumn.DATABASE_INDEX, //
			TargetColumn.CONTRIBUTOR, //
			TargetColumn.REFERENCE_ID, //
			TargetColumn.RETENTION_TIME, //
			TargetColumn.RETENTION_INDEX, //
			TargetColumn.NCBI_TAXONOMY, //
			TargetColumn.GENBANK_ACCESSION //
	};

	private static final Map<String, String> LABELS_CLASSIC = createClassicLabels();

	private final String[] titles;
	private final int[] bounds;
	private final List<TargetColumn> columns = new ArrayList<>();
	private final List<IComparisonMetric> metrics = new ArrayList<>();

	public static TargetsColumns create(Collection<?> targets) {

		return new TargetsColumns(collectMetrics(targets));
	}

	private TargetsColumns(List<IComparisonMetric> comparisonMetrics) {

		List<String> columnTitles = new ArrayList<>();
		List<Integer> columnBounds = new ArrayList<>();

		for(TargetColumn column : COLUMNS_LEADING) {
			add(column, null, column.getTitle(), column.getWidth(), columnTitles, columnBounds);
		}

		for(IComparisonMetric metric : comparisonMetrics) {
			add(null, metric, getTitle(metric, columnTitles), WIDTH_METRIC, columnTitles, columnBounds);
		}

		for(TargetColumn column : COLUMNS_TRAILING) {
			add(column, null, column.getTitle(), column.getWidth(), columnTitles, columnBounds);
		}

		titles = columnTitles.toArray(new String[0]);
		bounds = new int[columnBounds.size()];
		for(int i = 0; i < bounds.length; i++) {
			bounds[i] = columnBounds.get(i).intValue();
		}
	}

	public String[] getTitles() {

		return titles;
	}

	public int[] getBounds() {

		return bounds;
	}

	public TargetColumn getColumn(int columnIndex) {

		if(columnIndex >= 0 && columnIndex < columns.size()) {
			return columns.get(columnIndex);
		}

		return null;
	}

	public IComparisonMetric getMetric(int columnIndex) {

		if(columnIndex >= 0 && columnIndex < metrics.size()) {
			return metrics.get(columnIndex);
		}

		return null;
	}

	public boolean matches(TargetsColumns targetsColumns) {

		return targetsColumns != null && Arrays.equals(titles, targetsColumns.titles);
	}

	private void add(TargetColumn column, IComparisonMetric metric, String title, int width, List<String> columnTitles, List<Integer> columnBounds) {

		columns.add(column);
		metrics.add(metric);
		columnTitles.add(title);
		columnBounds.add(Integer.valueOf(width));
	}

	private static List<IComparisonMetric> collectMetrics(Collection<?> targets) {

		Map<String, IComparisonMetric> collected = new LinkedHashMap<>();
		if(targets != null) {
			for(Object object : targets) {
				if(object instanceof IIdentificationTarget identificationTarget) {
					for(IComparisonMetric metric : identificationTarget.getComparisonResult().getMetrics()) {
						collected.putIfAbsent(metric.getId(), metric);
					}
				}
			}
		}

		if(collected.isEmpty()) {
			return ComparisonMetrics.getMetrics();
		}

		return new ArrayList<>(collected.values());
	}

	private static String getTitle(IComparisonMetric metric, List<String> usedTitles) {

		String label = LABELS_CLASSIC.get(metric.getId());
		if(label == null || label.isEmpty()) {
			label = metric.getLabel();
		}
		if(usedTitles.contains(label)) {
			return label + " (" + metric.getId() + ")";
		}

		return label;
	}

	private static Map<String, String> createClassicLabels() {

		Map<String, String> labels = new HashMap<>();
		labels.put(ComparisonMetrics.MATCH_FACTOR, TargetsLabelProvider.MATCH_FACTOR);
		labels.put(ComparisonMetrics.REVERSE_MATCH_FACTOR, TargetsLabelProvider.REVERSE_MATCH_FACTOR);
		labels.put(ComparisonMetrics.MATCH_FACTOR_DIRECT, TargetsLabelProvider.MATCH_FACTOR_DIRECT);
		labels.put(ComparisonMetrics.REVERSE_MATCH_FACTOR_DIRECT, TargetsLabelProvider.REVERSE_MATCH_FACTOR_DIRECT);
		labels.put(ComparisonMetrics.PROBABILITY, TargetsLabelProvider.PROBABILITY);
		labels.put(ComparisonMetrics.IN_LIB_FACTOR, TargetsLabelProvider.INLIB_FACTOR);

		return labels;
	}
}
