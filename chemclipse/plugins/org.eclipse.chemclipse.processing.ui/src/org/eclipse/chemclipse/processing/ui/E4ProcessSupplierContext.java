/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - access member variable, probably remove this context
 *******************************************************************************/
package org.eclipse.chemclipse.processing.ui;

import java.util.function.Consumer;

import jakarta.inject.Inject;

import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplierContext;
import org.eclipse.chemclipse.processing.supplier.ProcessSupplierFactory;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;

@Creatable
public class E4ProcessSupplierContext implements IProcessSupplierContext {

	@Inject
	private IProcessSupplierContext processSupplierContext;
	@Inject
	private IEclipseContext eclipseContext;

	@SuppressWarnings("unchecked")
	@Override
	public <T> IProcessSupplier<T> getSupplier(String id) {

		IProcessSupplier<T> supplier = processSupplierContext.getSupplier(id);
		if(supplier != null) {
			Object factorySupplier = ContextInjectionFactory.invoke(supplier, ProcessSupplierFactory.class, eclipseContext, null);
			if(factorySupplier instanceof IProcessSupplier<?>) {
				ContextInjectionFactory.inject(factorySupplier, eclipseContext);
				return supplier.getClass().cast(factorySupplier);
			}
		}
		return supplier;
	}

	@Override
	public void visitSupplier(Consumer<? super IProcessSupplier<?>> consumer) {

		processSupplierContext.visitSupplier(supplier -> {

			Object factorySupplier = ContextInjectionFactory.invoke(supplier, ProcessSupplierFactory.class, eclipseContext, null);
			if(factorySupplier instanceof IProcessSupplier<?> processSupplier) {
				ContextInjectionFactory.inject(factorySupplier, eclipseContext);
				consumer.accept(processSupplier);
			} else {
				consumer.accept(supplier);
			}
		});
	}
}
