/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - adjust to new API
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.ui.internal.provider;

import static org.eclipse.chemclipse.support.ui.swt.columns.ColumnBuilder.defaultSortableColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.support.ui.swt.columns.ColumnDefinition;
import org.eclipse.chemclipse.support.ui.swt.columns.ColumnDefinitionProvider;

public class ResultTitles implements ColumnDefinitionProvider {

	@Override
	public Collection<? extends ColumnDefinition<?, ?>> getColumnDefinitions() {

		List<ColumnDefinition<?, ?>> list = new ArrayList<>();
		list.add(defaultSortableColumn(ExtensionMessages.startScan, 150, new Function<ICombinedMassSpectrum, Integer>() {

			@Override
			public Integer apply(ICombinedMassSpectrum spectrum) {

				return spectrum.getStartScan();
			}
		}).create());
		list.add(defaultSortableColumn(ExtensionMessages.stopScan, 150, new Function<ICombinedMassSpectrum, Integer>() {

			@Override
			public Integer apply(ICombinedMassSpectrum spectrum) {

				return spectrum.getStopScan();
			}
		}).create());
		list.add(defaultSortableColumn(ExtensionMessages.startRT, 150, new Function<ICombinedMassSpectrum, Integer>() {

			@Override
			public Integer apply(ICombinedMassSpectrum spectrum) {

				return spectrum.getStartRetentionTime();
			}
		}).create());
		list.add(defaultSortableColumn(ExtensionMessages.stopRT, 150, new Function<ICombinedMassSpectrum, Integer>() {

			@Override
			public Integer apply(ICombinedMassSpectrum spectrum) {

				return spectrum.getStopRetentionTime();
			}
		}).create());
		list.add(defaultSortableColumn(ExtensionMessages.startRI, 150, new Function<ICombinedMassSpectrum, Float>() {

			@Override
			public Float apply(ICombinedMassSpectrum spectrum) {

				return spectrum.getStartRetentionIndex();
			}
		}).create());
		list.add(defaultSortableColumn(ExtensionMessages.stopRI, 150, new Function<ICombinedMassSpectrum, Float>() {

			@Override
			public Float apply(ICombinedMassSpectrum spectrum) {

				return spectrum.getStopRetentionIndex();
			}
		}).create());
		return list;
	}
}
