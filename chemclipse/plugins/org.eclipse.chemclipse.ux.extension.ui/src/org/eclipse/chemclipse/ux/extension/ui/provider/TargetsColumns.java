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
package org.eclipse.chemclipse.ux.extension.ui.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.identifier.ComparisonMetricsClassic;
import org.eclipse.chemclipse.model.identifier.IComparisonMetric;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;

public class TargetsColumns {

	private static final int WIDTH_METRIC = 100;

	private static final TargetColumn[] COLUMNS_LEADING = { //
			TargetColumn.VERIFIED, //
			TargetColumn.RATING, //
			TargetColumn.NAME, //

	};

	public static final TargetColumn[] CHEMICAL_IDENTIFIERS = { //
			TargetColumn.CAS, //
			TargetColumn.FORMULA, //
			TargetColumn.SMILES, //
			TargetColumn.INCHI, //
			TargetColumn.INCHI_KEY, //
			TargetColumn.MOL_WEIGHT, //
			TargetColumn.EXACT_MASS, //
	};

	private static final TargetColumn[] DATABASE_RELATED = { //

			TargetColumn.ADVICE, //
			TargetColumn.IDENTIFIER, //
			TargetColumn.MISCELLANEOUS, //
			TargetColumn.COMMENTS, //
			TargetColumn.DATABASE, //
			TargetColumn.DATABASE_INDEX, //
			TargetColumn.CONTRIBUTOR, //
			TargetColumn.REFERENCE_ID, //

	};

	public static final TargetColumn[] CHROMATOGRAPHY_RELATED = { //
			TargetColumn.RETENTION_TIME, //
			TargetColumn.RETENTION_INDEX, //
	};

	public static final TargetColumn[] BIOLOGICAL_IDENTIFIERS = { //
			TargetColumn.NCBI_TAXONOMY, //
			TargetColumn.GENBANK_ACCESSION //
	};

	private static final Map<String, String> LABELS_CLASSIC = createClassicLabels();

	private final String[] titles;
	private final int[] bounds;
	private final List<TargetColumn> columns = new ArrayList<>();
	private final List<IComparisonMetric> metrics = new ArrayList<>();

	public static TargetsColumns create(Collection<?> targets, Collection<TargetColumn> columnsTrailing) {

		return new TargetsColumns(collectMetrics(targets), columnsTrailing);
	}

	private TargetsColumns(List<IComparisonMetric> comparisonMetrics, Collection<TargetColumn> identifiers) {

		List<String> columnTitles = new ArrayList<>();
		List<Integer> columnBounds = new ArrayList<>();

		for(TargetColumn column : COLUMNS_LEADING) {
			add(column, null, column.getTitle(), column.getWidth(), columnTitles, columnBounds);
		}

		for(IComparisonMetric metric : comparisonMetrics) {
			add(null, metric, getTitle(metric, columnTitles), WIDTH_METRIC, columnTitles, columnBounds);
		}

		for(TargetColumn column : DATABASE_RELATED) {
			add(column, null, column.getTitle(), column.getWidth(), columnTitles, columnBounds);
		}

		for(TargetColumn column : identifiers) {
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
			return ComparisonMetricsClassic.getMetrics();
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
		labels.put(ComparisonMetricsClassic.MATCH_FACTOR, "Match Factor");
		labels.put(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR, "Reverse Match Factor");
		labels.put(ComparisonMetricsClassic.MATCH_FACTOR_DIRECT, "Match Factor Direct");
		labels.put(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR_DIRECT, "Reverse Match Factor Direct");
		labels.put(ComparisonMetricsClassic.PROBABILITY, "Probability");
		labels.put(ComparisonMetricsClassic.IN_LIB_FACTOR, "InLib Factor");

		return labels;
	}
}
