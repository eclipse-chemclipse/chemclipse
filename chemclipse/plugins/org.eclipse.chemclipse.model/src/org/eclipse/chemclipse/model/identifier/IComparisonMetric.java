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

/**
 * Describes one metric an identification algorithm is able to report, e.g. the
 * match factor of a spectral search or the e-value of a sequence alignment.
 *
 * A metric is a descriptor only, it carries no value. The values are stored in
 * the {@link IComparisonResult} and are keyed by {@link #getId()}. Descriptors
 * are resolved via the {@link ComparisonMetricRegistry} and are therefore not
 * part of the serialized state of a comparison result.
 */
public interface IComparisonMetric {

	/**
	 * Stable identifier of the metric. It is persisted together with the value,
	 * hence it must not be changed once it has been released.
	 */
	String getId();

	/**
	 * Short label, used as the column header.
	 */
	String getLabel();

	/**
	 * Describes what the metric means, used as a tooltip. May be empty.
	 */
	String getDescription();

	/**
	 * Decimal format pattern used to render the value, e.g. "0.0" or "0.###E0".
	 */
	String getFormat();

	/**
	 * Whether the penalty of the comparison result is subtracted from the value.
	 * This is meaningful for metrics that are scaled from 0 to 100 like the
	 * match factor, but not for unbounded or inverted metrics.
	 */
	boolean isPenaltyApplicable(); // TODO: legacy default behavior

	/**
	 * Orders two values of this metric, best first. The default ranks higher
	 * values first. Override for metrics where a lower value is better.
	 */
	default Comparator<Double> getComparator() {

		return Comparator.reverseOrder();
	}
}
