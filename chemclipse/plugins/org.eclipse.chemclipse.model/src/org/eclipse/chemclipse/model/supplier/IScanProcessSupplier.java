/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.supplier;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.processing.supplier.IProcessExecutionConsumer;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessorPreferences;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;

public interface IScanProcessSupplier<SettingType> extends IProcessSupplier<SettingType> {

	boolean isValidFor(IScan scan);

	/**
	 * Apply this processor to the given {@link IScan}
	 *
	 * @param scan
	 *            the {@link IScan} to process
	 * @param processSettings
	 *            settings to use
	 * @return the processed {@link IScan}
	 */
	IScan apply(IScan scan, SettingType processSettings, ProcessExecutionContext context) throws InterruptedException;

	static IProcessExecutionConsumer<IScan> createConsumer(IScan scan) {

		if(scan == null) {
			return null;
		}

		return new IProcessExecutionConsumer<>() {

			AtomicReference<IScan> result = new AtomicReference<>(scan);

			@Override
			public <X> void execute(IProcessorPreferences<X> preferences, ProcessExecutionContext context) throws Exception {

				IProcessSupplier<X> supplier = preferences.getSupplier();
				if(supplier instanceof IScanProcessSupplier<X> scanProcessSupplier) {
					updateResult(scanProcessSupplier.apply(getResult(), preferences.getSettings(), context));
				}
			}

			@Override
			public <X> boolean canExecute(IProcessorPreferences<X> preferences) {

				IProcessSupplier<X> supplier = preferences.getSupplier();
				return (supplier instanceof IScanProcessSupplier<?>);
			}

			private void updateResult(IScan newScan) {

				result.set(newScan);
			}

			@Override
			public IScan getResult() {

				return result.get();
			}

			@Override
			public IProcessExecutionConsumer<IScan> withResult(Object initialResult) {

				if(initialResult instanceof IScan scan) {
					return IScanProcessSupplier.createConsumer(scan);
				}
				return null;
			}
		};
	}
}
