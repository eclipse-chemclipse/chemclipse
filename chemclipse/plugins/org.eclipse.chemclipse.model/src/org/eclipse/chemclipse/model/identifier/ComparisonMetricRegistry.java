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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * Resolves the metrics and the rating supplier of an identification algorithm.
 *
 * The metrics of the classical spectral match are always available, see
 * {@link ComparisonMetrics}. Further algorithms contribute their metrics via
 * the extension point "org.eclipse.chemclipse.model.comparisonMetrics".
 */
public class ComparisonMetricRegistry {

	private static final String EXTENSION_POINT = "org.eclipse.chemclipse.model.comparisonMetrics";
	private static final String ELEMENT_METRIC = "Metric";
	private static final String ATTRIBUTE_ID = "id";
	private static final String ATTRIBUTE_LABEL = "label";
	private static final String ATTRIBUTE_DESCRIPTION = "description";
	private static final String ATTRIBUTE_FORMAT = "format";
	private static final String ATTRIBUTE_ORDER = "order";
	private static final String ATTRIBUTE_PENALTY_APPLICABLE = "penaltyApplicable";
	private static final String ATTRIBUTE_RATING_SUPPLIER = "ratingSupplier";
	private static final String ORDER_ASCENDING = "ascending";

	private static Map<String, Algorithm> algorithms = null;

	private ComparisonMetricRegistry() {

	}

	/**
	 * Returns the metrics of the given algorithm in display order. An empty
	 * list is returned if the algorithm is unknown.
	 *
	 * @param algorithmId
	 * @return {@link List}
	 */
	public static List<IComparisonMetric> getMetrics(String algorithmId) {

		Algorithm algorithm = getAlgorithms().get(algorithmId);
		if(algorithm == null) {
			return Collections.emptyList();
		}

		return Collections.unmodifiableList(new ArrayList<>(algorithm.metrics.values()));
	}

	/**
	 * Returns the descriptor of the given metric. A generic descriptor is
	 * returned if the metric has not been contributed, so that a value which
	 * was stored by a plug-in that is not installed anymore is still displayed.
	 *
	 * @param algorithmId
	 * @param metricId
	 * @return {@link IComparisonMetric}
	 */
	public static IComparisonMetric getMetric(String algorithmId, String metricId) {

		Algorithm algorithm = getAlgorithms().get(algorithmId);
		if(algorithm != null) {
			IComparisonMetric metric = algorithm.metrics.get(metricId);
			if(metric != null) {
				return metric;
			}
		}

		return ComparisonMetric.createDefault(metricId);
	}

	/**
	 * Returns a new rating supplier for the given algorithm. The rating
	 * supplier of the classical spectral match is returned if the algorithm
	 * does not contribute one, hence this method never returns null.
	 *
	 * @param algorithmId
	 * @return {@link IRatingSupplier}
	 */
	public static IRatingSupplier createRatingSupplier(String algorithmId) {

		Algorithm algorithm = getAlgorithms().get(algorithmId);
		if(algorithm != null && algorithm.ratingSupplierElement != null) {
			try {
				Object object = algorithm.ratingSupplierElement.createExecutableExtension(ATTRIBUTE_RATING_SUPPLIER);
				if(object instanceof IRatingSupplierFactory factory) {
					IRatingSupplier ratingSupplier = factory.createRatingSupplier();
					if(ratingSupplier != null) {
						return ratingSupplier;
					}
				}
			} catch(CoreException e) {
				Logger.getLogger(ComparisonMetricRegistry.class).warn(e);
			}
		}

		return new RatingSupplier();
	}

	private static synchronized Map<String, Algorithm> getAlgorithms() {

		if(algorithms == null) {
			algorithms = new HashMap<>();
			/*
			 * The classical spectral match is registered in code and not via
			 * the extension point, so that it is available in tests as well,
			 * where no extension registry is running.
			 */
			Algorithm classic = new Algorithm();
			for(IComparisonMetric metric : ComparisonMetrics.getMetrics()) {
				classic.metrics.put(metric.getId(), metric);
			}
			algorithms.put(ComparisonMetrics.ALGORITHM_CLASSIC, classic);
			readExtensions();
		}

		return algorithms;
	}

	private static void readExtensions() {

		IExtensionRegistry registry;
		try {
			registry = Platform.getExtensionRegistry();
		} catch(RuntimeException | LinkageError e) {
			/*
			 * No OSGi framework is running, e.g. in a plain unit test. Only the
			 * metrics of the classical spectral match are available then.
			 */
			return;
		}

		if(registry == null) {
			return;
		}

		for(IConfigurationElement element : registry.getConfigurationElementsFor(EXTENSION_POINT)) {
			String algorithmId = element.getAttribute(ATTRIBUTE_ID);
			if(algorithmId == null || algorithmId.isEmpty()) {
				continue;
			}
			/*
			 * An algorithm may reuse the metrics of another algorithm, hence
			 * existing entries are extended instead of being replaced.
			 */
			Algorithm algorithm = algorithms.computeIfAbsent(algorithmId, _ -> new Algorithm());
			if(element.getAttribute(ATTRIBUTE_RATING_SUPPLIER) != null) {
				algorithm.ratingSupplierElement = element;
			}
			for(IConfigurationElement child : element.getChildren(ELEMENT_METRIC)) {
				IComparisonMetric metric = readMetric(child);
				if(metric != null) {
					algorithm.metrics.put(metric.getId(), metric);
				}
			}
		}
	}

	private static IComparisonMetric readMetric(IConfigurationElement element) {

		String metricId = element.getAttribute(ATTRIBUTE_ID);
		if(metricId == null || metricId.isEmpty()) {
			Logger.getLogger(ComparisonMetricRegistry.class).warn("A metric without an id has been skipped.");
			return null;
		}

		String label = element.getAttribute(ATTRIBUTE_LABEL);
		return new ComparisonMetric( //
				metricId, //
				label != null && !label.isEmpty() ? label : metricId, //
				element.getAttribute(ATTRIBUTE_DESCRIPTION), //
				element.getAttribute(ATTRIBUTE_FORMAT), //
				Boolean.parseBoolean(element.getAttribute(ATTRIBUTE_PENALTY_APPLICABLE)), //
				ORDER_ASCENDING.equals(element.getAttribute(ATTRIBUTE_ORDER)) ? ComparisonMetric.LOWER_IS_BETTER : ComparisonMetric.HIGHER_IS_BETTER //
		);
	}

	private static class Algorithm {

		private final Map<String, IComparisonMetric> metrics = new LinkedHashMap<>();
		private IConfigurationElement ratingSupplierElement = null;
	}
}
