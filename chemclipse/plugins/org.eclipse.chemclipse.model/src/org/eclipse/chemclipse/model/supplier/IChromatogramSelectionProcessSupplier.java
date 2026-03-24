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
 * Philip Wenig - process macro recorder
 *******************************************************************************/
package org.eclipse.chemclipse.model.supplier;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.supplier.IProcessExecutionConsumer;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessorPreferences;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;

public interface IChromatogramSelectionProcessSupplier<SettingType> extends IProcessSupplier<SettingType> {

	/**
	 * Apply this processor to the given {@link IChromatogramSelection}
	 *
	 * @param chromatogramSelection
	 *            the {@link IChromatogramSelection} to process
	 * @param processSettings
	 *            settings to use
	 * @return the processed {@link IChromatogramSelection}
	 */
	IChromatogramSelection apply(IChromatogramSelection chromatogramSelection, SettingType processSettings, ProcessExecutionContext context) throws InterruptedException;

	static IProcessExecutionConsumer<IChromatogramSelection> createConsumer(IChromatogramSelection chromatogramSelection) {

		if(chromatogramSelection == null) {
			return null;
		}

		return new IProcessExecutionConsumer<>() {

			AtomicReference<IChromatogramSelection> result = new AtomicReference<>(chromatogramSelection);

			@Override
			public <X> void execute(IProcessorPreferences<X> preferences, ProcessExecutionContext context) throws Exception {

				IProcessSupplier<X> supplier = preferences.getSupplier();
				if(supplier instanceof IChromatogramSelectionProcessSupplier<X> chromatogramSelectionProcessSupplier) {
					updateResult(chromatogramSelectionProcessSupplier.apply(getResult(), preferences.getSettings(), context));
				} else if(supplier instanceof IMeasurementProcessSupplier<X> measurementProcessSupplier) {
					IChromatogram chromatogram = getResult().getChromatogram();
					measurementProcessSupplier.applyProcessor(Collections.singleton(chromatogram), preferences.getSettings(), context);
				}
			}

			@Override
			public <X> boolean canExecute(IProcessorPreferences<X> preferences) {

				IProcessSupplier<X> supplier = preferences.getSupplier();
				return (supplier instanceof IChromatogramSelectionProcessSupplier<?>) || (supplier instanceof IMeasurementProcessSupplier<?>);
			}

			private void updateResult(IChromatogramSelection newSelection) {

				result.set(newSelection);
			}

			@Override
			public IChromatogramSelection getResult() {

				return result.get();
			}

			@Override
			public IProcessExecutionConsumer<IChromatogramSelection> withResult(Object initialResult) {

				if(initialResult instanceof IChromatogramSelection chromatogramSelection) {
					return IChromatogramSelectionProcessSupplier.createConsumer(chromatogramSelection);
				}
				return null;
			}
		};
	}
}
