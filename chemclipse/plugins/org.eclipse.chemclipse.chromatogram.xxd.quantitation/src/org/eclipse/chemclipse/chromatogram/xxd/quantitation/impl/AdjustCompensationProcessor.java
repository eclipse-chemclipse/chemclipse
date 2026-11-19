/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.impl;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.quantitation.IInternalStandard;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.supplier.IChromatogramSelectionProcessSupplier;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.supplier.AbstractProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessTypeSupplier;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.osgi.service.component.annotations.Component;

@Component(service = {IProcessTypeSupplier.class})
public class AdjustCompensationProcessor implements IProcessTypeSupplier {

	private static final String ID = "org.eclipse.chemclipse.chromatogram.xxd.quantitation.adjustCompensationProcessor";
	private static final String NAME = "Adjust Compensation Factor (ISTD)";
	private static final String DESCRIPTION = "Use the e.g. the sample weight to adjust the compensation factor.";

	@Override
	public String getCategory() {

		return ICategories.PEAK_QUANTIFIER;
	}

	@Override
	public Collection<IProcessSupplier<?>> getProcessorSuppliers() {

		return Collections.singleton(new ProcessSupplier(this));
	}

	private static final class ProcessSupplier extends AbstractProcessSupplier<AdjustCompensationSettings> implements IChromatogramSelectionProcessSupplier<AdjustCompensationSettings> {

		public ProcessSupplier(IProcessTypeSupplier parent) {

			super(ID, NAME, DESCRIPTION, AdjustCompensationSettings.class, parent, DataCategory.MSD, DataCategory.CSD);
		}

		@Override
		public IChromatogramSelection apply(IChromatogramSelection chromatogramSelection, AdjustCompensationSettings processSettings, ProcessExecutionContext context) throws InterruptedException {

			double setpoint = processSettings.getSetpoint();
			double actualValue = processSettings.getActualValue();
			CompensationCalculator compensationCalculator = new CompensationCalculator(setpoint, actualValue);

			if(compensationCalculator.isEnabled()) {

				IChromatogram chromatogram = chromatogramSelection.getChromatogram();
				for(IPeak peak : chromatogram.getPeaks()) {
					for(IInternalStandard internalStandard : peak.getInternalStandards()) {
						double concentrationSetpoint = internalStandard.getConcentration();
						if(concentrationSetpoint > 0) {
							double compensationFactorSetpoint = internalStandard.getCompensationFactor();
							if(compensationFactorSetpoint > 0) {
								double compensationFactorActualValue = compensationCalculator.getCompensationFactorCalculated(concentrationSetpoint, compensationFactorSetpoint);
								if(compensationFactorActualValue > 0) {
									internalStandard.setCompensationFactor(compensationFactorActualValue);
								}
							}
						}
					}
				}
			}

			return chromatogramSelection;
		}
	}
}