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
 * Philip Wenig - refactor menu categories
 *******************************************************************************/
package org.eclipse.chemclipse.model.filter;

import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.filter.Filter;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;

/**
 * A {@link Filter} Extension interface for filters that can work on {@link IPeak}s.
 * This is part of the ChemClipse FilterFramework, to make the Filter available simply register it with the OSGi Service factory under the {@link Filter} interface, implementors are encouraged to also register each filter under all sub(filter) interface.
 * 
 * @author Christoph Läubrich
 */
public interface IPeakFilter<ConfigType> extends Filter<ConfigType> {

	/**
	 * Filters the given Collection of {@link IPeak}s with this filter and returns the result.
	 * The resulting Collection could either be the same or a new collection, might have more or less items
	 * 
	 * @param configuration
	 *            the configuration to apply or <code>null</code> if no special configuration is desired
	 * 
	 * @param context
	 *            to be used for access to progress monitor and messaging
	 * @throws IllegalArgumentException
	 *             if the given {@link IPeak}s are incompatible with this filter
	 */
	void filterPeaks(IChromatogramSelection chromatogramSelection, ConfigType configuration, ProcessExecutionContext context) throws IllegalArgumentException;

	/**
	 * Creates a new configuration that is specially suited for the given {@link IPeak} types
	 * 
	 * @return a new configuration for this items or the default configuration if items is empty or no suitable configuration can be created
	 * 
	 */
	default ConfigType createConfiguration(Collection<IPeak> items) {

		return createNewConfiguration();
	}

	/**
	 * The default implementation returns all data categories, implementors might override if the can only work on a certain sub category
	 */
	@Override
	default DataCategory[] getDataCategories() {

		return new DataCategory[]{DataCategory.CSD, DataCategory.MSD, DataCategory.WSD, DataCategory.VSD};
	}

	List<String> getLegacyIDs();
}
