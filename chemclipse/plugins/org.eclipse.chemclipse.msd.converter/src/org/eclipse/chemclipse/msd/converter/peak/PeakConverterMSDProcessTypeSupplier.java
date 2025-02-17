/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring to dynamic export name
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.peak;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.supplier.IChromatogramSelectionProcessSupplier;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.PeaksMSD;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.supplier.AbstractProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessTypeSupplier;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.osgi.service.component.annotations.Component;

@Component(service = {IProcessTypeSupplier.class})
public class PeakConverterMSDProcessTypeSupplier implements IProcessTypeSupplier {

	@Override
	public String getCategory() {

		return ICategories.PEAK_EXPORT;
	}

	@Override
	public Collection<IProcessSupplier<?>> getProcessorSuppliers() {

		List<IProcessSupplier<?>> list = new ArrayList<>();
		List<ISupplier> exportSupplier = PeakConverterMSD.getPeakConverterSupport().getExportSupplier();
		for(ISupplier supplier : exportSupplier) {
			IPeakExportConverter converter = PeakConverterMSD.getPeakExportConverter(supplier.getId());
			if(converter != null) {
				list.add(new PeakConverterMSDProcessSupplier(supplier, converter, this));
			}
		}
		return list;
	}

	private static final class PeakConverterMSDProcessSupplier extends AbstractProcessSupplier<PeakExportSettings> implements IChromatogramSelectionProcessSupplier<PeakExportSettings> {

		private final IPeakExportConverter converter;
		private final ISupplier supplier;

		public PeakConverterMSDProcessSupplier(ISupplier supplier, IPeakExportConverter converter, IProcessTypeSupplier parent) {

			super("PeakConverterMSD." + supplier.getId(), supplier.getFilterName(), supplier.getDescription(), PeakExportSettings.class, parent, DataCategory.MSD);
			this.supplier = supplier;
			this.converter = converter;
		}

		@Override
		public IChromatogramSelection apply(IChromatogramSelection chromatogramSelection, PeakExportSettings processSettings, ProcessExecutionContext context) {

			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			if(chromatogram instanceof IChromatogramMSD msd) {
				IProcessingInfo<File> info = converter.convert(processSettings.getExportFile(supplier.getFileExtension(), chromatogram), createPeaks(msd), false, context.getProgressMonitor());
				context.addMessages(info);
			} else {
				context.addWarnMessage(getName(), "Can only export MSD Data, skipping...");
			}
			return chromatogramSelection;
		}

		private PeaksMSD createPeaks(IChromatogramMSD chromatogram) {

			return new PeaksMSD() {

				@Override
				public String getName() {

					return chromatogram.getName();
				}

				@Override
				public List<IPeakMSD> getPeaks() {

					return Collections.unmodifiableList(chromatogram.getPeaks());
				}
			};
		}
	}
}
