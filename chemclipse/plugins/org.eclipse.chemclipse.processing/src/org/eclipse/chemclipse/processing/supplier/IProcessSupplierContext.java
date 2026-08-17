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
 * Philip Wenig - refactorings
 *******************************************************************************/
package org.eclipse.chemclipse.processing.supplier;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.chemclipse.processing.DataCategory;

public interface IProcessSupplierContext {

	/**
	 * Gets the {@link IProcessSupplier} for the given id from this context or <code>null</code> if no supplier exits for the id
	 */
	<T> IProcessSupplier<T> getSupplier(String id);

	/**
	 *
	 * iterates all available {@link IProcessSupplier}
	 */
	void visitSupplier(Consumer<? super IProcessSupplier<?>> consumer);

	default Set<IProcessSupplier<?>> getSupplier(Predicate<IProcessSupplier<?>> predicate) {

		Set<IProcessSupplier<?>> supplier = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
		visitSupplier(processSupplier -> {
			if(predicate.test(processSupplier)) {
				supplier.add(processSupplier);
			}
		});

		return supplier;
	}

	static Predicate<IProcessSupplier<?>> forDataTypes(Iterable<DataCategory> dataTypes) {

		if(dataTypes == null) {
			return _ -> true;
		}

		return processSupplier -> {

			for(DataCategory dataType : dataTypes) {
				if(processSupplier.getSupportedDataTypes().contains(dataType)) {
					return true;
				}
			}
			return false;
		};
	}

	static Predicate<IProcessSupplier<?>> createDataCategoryPredicate(DataCategory... dataCategories) {

		return supplier -> {
			if(supplier == null) {
				return false;
			}

			Set<DataCategory> supportedDataTypes = supplier.getSupportedDataTypes();
			for(DataCategory dataCategory : dataCategories) {
				if(supportedDataTypes.contains(dataCategory)) {
					return true;
				}
			}
			return false;
		};
	}
}