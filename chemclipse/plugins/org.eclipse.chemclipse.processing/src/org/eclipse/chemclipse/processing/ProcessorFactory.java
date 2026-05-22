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
package org.eclipse.chemclipse.processing;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * A ProcessorFactory service allows access to all currently known {@link Filter} in the system
 *
 * @author Christoph Läubrich
 *
 */
public interface ProcessorFactory {

	/**
	 * Returns all processors know to this ProcessorFactory that match the given processorType and acceptor (if given)
	 *
	 * @param processorType
	 *            the subtype of the {@link org.eclipse.chemclipse.processing.Processor} to fetch
	 * @param acceptor
	 *            an acceptor function that can narrow the result or <code>null</code> if all {@link org.eclipse.chemclipse.processing.Processor}s should be returned
	 * @return the filters that are matched
	 */
	<T extends Processor<?>> Collection<T> getProcessors(Class<T> processorType, BiPredicate<? super T, Map<String, ?>> acceptor);

	/**
	 * Helper method to create generic Class types for subinterfaces that satisfy the {@link #getFilters(Class, BiFunction)} method, e.g.
	 * <pre>Collection&lt;IScanFilter&lt;?&gt;&gt; scanFilter = filterFactory.getFilters(FilterFactory.genericClass(IScanFilter.class), new BiFunction&lt;IScanFilter&lt;?&gt;, Map&lt;String, ?&gt;, Boolean&gt;() { ...});</pre>
	 */
	@SuppressWarnings("unchecked")
	static <T> Class<T> genericClass(Class<?> cls) {

		return (Class<T>)cls;
	}
}
