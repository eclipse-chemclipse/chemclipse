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
package org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.eclipse.chemclipse.chromatogram.msd.filter.exceptions.NoMassSpectrumFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.supplier.ChromatogramSelectionProcessSupplier;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IMessageConsumer;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessTypeSupplier;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class AbstractChromatogramSelectionMassSpectrumFilterProcessTypeSupplier implements IProcessTypeSupplier {

	private static final Logger logger = Logger.getLogger(AbstractChromatogramSelectionMassSpectrumFilterProcessTypeSupplier.class);

	private final String category;
	private final String prefix;
	private final Function<IChromatogramSelection, List<IScanMSD>> extractionFunction;

	public AbstractChromatogramSelectionMassSpectrumFilterProcessTypeSupplier(String category, String prefix, Function<IChromatogramSelection, List<IScanMSD>> extractionFunction) {

		this.category = category;
		this.prefix = prefix;
		this.extractionFunction = extractionFunction;
	}

	@Override
	public String getCategory() {

		return category;
	}

	@Override
	public Collection<IProcessSupplier<?>> getProcessorSuppliers() {

		try {
			IMassSpectrumFilterSupport filterSupport = MassSpectrumFilter.getMassSpectrumFilterSupport();
			List<IProcessSupplier<?>> list = new ArrayList<>();
			for(String id : filterSupport.getAvailableFilterIds()) {
				list.add(new MassSpectrumFilterProcessorSupplier(prefix, filterSupport.getFilterSupplier(id), extractionFunction, this));
			}
			return list;
		} catch(NoMassSpectrumFilterSupplierAvailableException e) {
			return Collections.emptyList();
		}
	}

	private static final class MassSpectrumFilterProcessorSupplier extends ChromatogramSelectionProcessSupplier<IMassSpectrumFilterSettings> {

		private final IMassSpectrumFilterSupplier supplier;
		private final Function<IChromatogramSelection, List<IScanMSD>> extractionFunction;

		@SuppressWarnings("unchecked")
		public MassSpectrumFilterProcessorSupplier(String prefix, IMassSpectrumFilterSupplier supplier, Function<IChromatogramSelection, List<IScanMSD>> extractionFunction, IProcessTypeSupplier parent) {

			super(prefix + supplier.getId(), supplier.getFilterName(), supplier.getDescription(), (Class<IMassSpectrumFilterSettings>)supplier.getSettingsClass(), parent, DataType.MSD);
			this.supplier = supplier;
			this.extractionFunction = extractionFunction;
			getLiteratureReferences().addAll(supplier.getLiteratureReferences());
		}

		@Override
		public IChromatogramSelection apply(IChromatogramSelection chromatogramSelection, IMassSpectrumFilterSettings processSettings, IMessageConsumer messageConsumer, IProgressMonitor monitor) {

			List<IScanMSD> massSpectras = extractionFunction.apply(chromatogramSelection);
			messageConsumer.addMessages(MassSpectrumFilter.applyFilter(massSpectras, processSettings, supplier.getId(), monitor));
			chromatogramSelection.getChromatogram().setDirty(true);
			return chromatogramSelection;
		}

		@Override
		public boolean isValidFor(IChromatogramSelection chromatogramSelection) {

			if(chromatogramSelection == null) {
				return false;
			}
			IMassSpectrumFilterSettings massSpectrumFilterSettings = createSettings();
			if(massSpectrumFilterSettings == null) {
				return true;
			}
			/*
			 * Valid if at least one mass spectrum qualifies,
			 * e.g. MS/MS scans in a data-dependent acquisition.
			 */
			return extractionFunction.apply(chromatogramSelection).stream().anyMatch(massSpectrumFilterSettings::isValidFor);
		}

		private IMassSpectrumFilterSettings createSettings() {

			Class<IMassSpectrumFilterSettings> settingsClass = getSettingsClass();
			if(settingsClass != null) {
				try {
					return settingsClass.getDeclaredConstructor().newInstance();
				} catch(InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					logger.error(e);
				}
			}
			return null;
		}
	}
}
