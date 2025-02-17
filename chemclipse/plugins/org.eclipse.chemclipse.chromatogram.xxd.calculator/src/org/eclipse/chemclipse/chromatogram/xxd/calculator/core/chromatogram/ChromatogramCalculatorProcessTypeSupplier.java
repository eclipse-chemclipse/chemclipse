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
 * Philip Wenig - enable ISD
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.core.chromatogram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.exceptions.NoChromatogramCalculatorSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.settings.IChromatogramCalculatorSettings;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.supplier.ChromatogramSelectionProcessorSupplier;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.core.IMessageConsumer;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessTypeSupplier;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.service.component.annotations.Component;

@Component(service = IProcessTypeSupplier.class)
public class ChromatogramCalculatorProcessTypeSupplier implements IProcessTypeSupplier {

	@Override
	public String getCategory() {

		return ICategories.CHROMATOGRAM_CALCULATOR;
	}

	@Override
	public Collection<IProcessSupplier<?>> getProcessorSuppliers() {

		List<IProcessSupplier<?>> list = new ArrayList<>();
		try {
			IChromatogramCalculatorSupport support = ChromatogramCalculator.getChromatogramCalculatorSupport();
			for(String processorId : support.getAvailableCalculatorIds()) {
				IChromatogramCalculatorSupplier supplier = support.getCalculatorSupplier(processorId);
				list.add(new ChromatogramCalculatorProcessorSupplier(supplier, this));
			}
			return list;
		} catch(NoChromatogramCalculatorSupplierAvailableException e) {
			return Collections.emptyList();
		}
	}

	private static final class ChromatogramCalculatorProcessorSupplier extends ChromatogramSelectionProcessorSupplier<IChromatogramCalculatorSettings> {

		@SuppressWarnings("unchecked")
		public ChromatogramCalculatorProcessorSupplier(IChromatogramCalculatorSupplier supplier, IProcessTypeSupplier parent) {

			super(supplier.getId(), supplier.getCalculatorName(), supplier.getDescription(), (Class<IChromatogramCalculatorSettings>)supplier.getSettingsClass(), parent, DataType.MSD, DataType.CSD, DataType.WSD, DataType.VSD);
		}

		@Override
		public IChromatogramSelection apply(IChromatogramSelection chromatogramSelection, IChromatogramCalculatorSettings processSettings, IMessageConsumer messageConsumer, IProgressMonitor monitor) {

			if(processSettings == null) {
				messageConsumer.addMessages(ChromatogramCalculator.applyCalculator(chromatogramSelection, getId(), monitor));
			} else {
				messageConsumer.addMessages(ChromatogramCalculator.applyCalculator(chromatogramSelection, processSettings, getId(), monitor));
			}
			return chromatogramSelection;
		}
	}
}
