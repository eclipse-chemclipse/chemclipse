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
package org.eclipse.chemclipse.model.identifier;

import java.util.Comparator;

public class ComparisonMetric implements IComparisonMetric {

	public static final Comparator<Double> HIGHER_IS_BETTER = Comparator.reverseOrder();
	public static final Comparator<Double> LOWER_IS_BETTER = Comparator.naturalOrder();

	private static final String DEFAULT_FORMAT = "0.0";

	private final String id;
	private final String label;
	private final String description;
	private final String format;
	private final boolean penaltyApplicable;
	private final Comparator<Double> comparator;

	/**
	 * Creates a generic descriptor for an unknown metric id.
	 */
	public static ComparisonMetric createDefault(String id) {

		return new ComparisonMetric(id, id, "", DEFAULT_FORMAT, false, HIGHER_IS_BETTER);
	}

	public ComparisonMetric(String id, String label, String description, String format, boolean penaltyApplicable, Comparator<Double> comparator) {

		this.id = id;
		this.label = label;
		this.description = description != null ? description : "";
		this.format = format != null && !format.isEmpty() ? format : DEFAULT_FORMAT;
		this.penaltyApplicable = penaltyApplicable;
		this.comparator = comparator != null ? comparator : HIGHER_IS_BETTER;
	}

	@Override
	public String getId() {

		return id;
	}

	@Override
	public String getLabel() {

		return label;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public String getFormat() {

		return format;
	}

	@Override
	public boolean isPenaltyApplicable() {

		return penaltyApplicable;
	}

	@Override
	public Comparator<Double> getComparator() {

		return comparator;
	}

	@Override
	public String toString() {

		return "ComparisonMetric [id=" + id + ", label=" + label + "]";
	}
}
