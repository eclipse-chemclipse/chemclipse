/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.processing.filter;

import java.util.Date;

/**
 * Provide the context in witch a {@link Filtered} Object was filtered
 *
 * @author Christoph Läubrich
 *
 * @param <FilteredType>
 */
public interface FilterContext<FilteredType, ConfigType> {

	/**
	 *
	 * @return the filtered object
	 */
	FilteredType getFilteredObject();

	/**
	 *
	 * @return the time when this filter was applied
	 */
	Date getFilterTime();

	/**
	 *
	 * @return the filter that was responsible for filtering this object or <code>null</code> if this is not known
	 */
	Filter<ConfigType> getFilter();

	/**
	 *
	 * @return the data config object for this filter or <code>null</code> if this is not known
	 */
	ConfigType getFilterConfig();

	public static <FilteredType, ConfigType> FilterContext<FilteredType, ConfigType> create(FilteredType filteredObject, Filter<ConfigType> filter, ConfigType config) {

		Date date = new Date();
		return new FilterContext<>() {

			@Override
			public Date getFilterTime() {

				return (Date)date.clone();
			}

			@Override
			public Filter<ConfigType> getFilter() {

				return filter;
			}

			@Override
			public ConfigType getFilterConfig() {

				return config;
			}

			@Override
			public FilteredType getFilteredObject() {

				return filteredObject;
			}
		};
	}
}
