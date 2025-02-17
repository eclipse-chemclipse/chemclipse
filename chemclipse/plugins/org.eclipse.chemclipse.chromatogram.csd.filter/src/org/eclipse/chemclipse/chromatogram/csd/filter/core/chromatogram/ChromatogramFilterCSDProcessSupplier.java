/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 * Philip Wenig - refactor menu categories
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.csd.filter.core.chromatogram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.csd.filter.l10n.Messages;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.IChromatogramFilterSupplier;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.supplier.ChromatogramSelectionProcessorSupplier;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.core.IMessageConsumer;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessTypeSupplier;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.service.component.annotations.Component;

@Component(service = {IProcessTypeSupplier.class})
public class ChromatogramFilterCSDProcessSupplier implements IProcessTypeSupplier {

	@Override
	public String getCategory() {

		return ICategories.CHROMATOGRAM_FILTER;
	}

	@Override
	public Collection<IProcessSupplier<?>> getProcessorSuppliers() {

		try {
			IChromatogramFilterSupportCSD support = ChromatogramFilterCSD.getChromatogramFilterSupport();
			List<IProcessSupplier<?>> list = new ArrayList<>();
			for(String processorId : support.getAvailableFilterIds()) {
				list.add(new ChromatogramFilterCSDProcessorSupplier(support.getFilterSupplier(processorId), this));
			}
			return list;
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			return Collections.emptyList();
		}
	}

	private static final class ChromatogramFilterCSDProcessorSupplier extends ChromatogramSelectionProcessorSupplier<IChromatogramFilterSettings> {

		private IChromatogramFilterSupplier supplier;

		@SuppressWarnings("unchecked")
		public ChromatogramFilterCSDProcessorSupplier(IChromatogramFilterSupplier supplier, IProcessTypeSupplier parent) {

			super("ChromatogramFilterCSD." + supplier.getId(), supplier.getFilterName(), supplier.getDescription(), (Class<IChromatogramFilterSettings>)supplier.getSettingsClass(), parent, DataType.CSD); //$NON-NLS-1$
			this.supplier = supplier;
		}

		@Override
		public IChromatogramSelection apply(IChromatogramSelection chromatogramSelection, IChromatogramFilterSettings processSettings, IMessageConsumer messageConsumer, IProgressMonitor monitor) {

			if(chromatogramSelection instanceof IChromatogramSelectionCSD chromatogramSelectionCSD) {
				if(processSettings instanceof IChromatogramFilterSettings) {
					messageConsumer.addMessages(ChromatogramFilterCSD.applyFilter(chromatogramSelectionCSD, processSettings, supplier.getId(), monitor));
				} else {
					messageConsumer.addMessages(ChromatogramFilterCSD.applyFilter(chromatogramSelectionCSD, supplier.getId(), monitor));
				}
			} else {
				messageConsumer.addWarnMessage(getName(), Messages.onlyCSDchromatogramSupported);
			}
			return chromatogramSelection;
		}

		@Override
		public boolean matchesId(String id) {

			return super.matchesId(id) || supplier.getId().equals(id);
		}
	}
}
