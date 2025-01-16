/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.filter.chromatogram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.supplier.IChromatogramSelectionProcessSupplier;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.supplier.AbstractProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessTypeSupplier;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.eclipse.chemclipse.xxd.filter.chromatogram.settings.MeasurementResultsFilterSettings;
import org.osgi.service.component.annotations.Component;

@Component(service = {IProcessTypeSupplier.class})
public class MeasurementResultsFilter implements IProcessTypeSupplier {

	private static final String ID = "org.eclipse.chemclipse.xxd.filter.chromatogram.measurementResultsDelete";
	private static final String NAME = "Measurement Results (Delete)";
	private static final String DESCRIPTION = "Deletes the selected measurement results from the chromatogram.";

	@Override
	public String getCategory() {

		return ICategories.CHROMATOGRAM_FILTER;
	}

	@Override
	public Collection<IProcessSupplier<?>> getProcessorSuppliers() {

		return Collections.singleton(new ProcessSupplier(this));
	}

	private static final class ProcessSupplier extends AbstractProcessSupplier<MeasurementResultsFilterSettings> implements IChromatogramSelectionProcessSupplier<MeasurementResultsFilterSettings> {

		public ProcessSupplier(IProcessTypeSupplier parent) {

			super(ID, NAME, DESCRIPTION, MeasurementResultsFilterSettings.class, parent, DataCategory.CSD, DataCategory.MSD, DataCategory.VSD, DataCategory.WSD);
		}

		@Override
		public IChromatogramSelection<?, ?> apply(IChromatogramSelection<?, ?> chromatogramSelection, MeasurementResultsFilterSettings processSettings, ProcessExecutionContext context) throws InterruptedException {

			IChromatogram<?> chromatogram = chromatogramSelection.getChromatogram();
			switch(processSettings.getMeasurementResultOption()) {
				case IDENTIFIER_ALL:
					clearMeasurementResults(chromatogram);
					break;
				case IDENTIFIER_SPECIFIC:
					deleteMeasurementResults(chromatogram, processSettings.getTarget(), false);
					break;
				case IDENTIFIER_REGEX:
					deleteMeasurementResults(chromatogram, processSettings.getTarget(), true);
					break;
				default:
					break;
			}
			//
			return chromatogramSelection;
		}

		private void clearMeasurementResults(IChromatogram<?> chromatogram) {

			chromatogram.removeAllMeasurementResults();
		}

		private void deleteMeasurementResults(IChromatogram<?> chromatogram, String target, boolean isRegularExpression) {

			if(!target.isBlank()) {
				Set<String> identifiers = new HashSet<>();
				List<IMeasurementResult<?>> measurementResults = new ArrayList<>(chromatogram.getMeasurementResults());
				for(IMeasurementResult<?> measurementResult : measurementResults) {
					String identifier = measurementResult.getIdentifier();
					if(isRegularExpression) {
						if(identifier.matches(target)) {
							identifiers.add(identifier);
						}
					} else {
						if(identifier.equals(target)) {
							identifiers.add(identifier);
						}
					}
				}
				//
				deleteMeasurementResults(chromatogram, identifiers);
			}
		}

		private void deleteMeasurementResults(IChromatogram<?> chromatogram, Set<String> identifiers) {

			for(String identifier : identifiers) {
				chromatogram.deleteMeasurementResult(identifier);
			}
		}
	}
}