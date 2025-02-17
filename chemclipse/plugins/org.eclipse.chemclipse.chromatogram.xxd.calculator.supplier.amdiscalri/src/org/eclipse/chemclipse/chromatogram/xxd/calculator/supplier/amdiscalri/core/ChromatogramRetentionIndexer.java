/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.core;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.RetentionIndexCalculator;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.RetentionIndexSupport;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.model.RetentionIndexMarker;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.settings.RetentionIndexSettings;
import org.eclipse.chemclipse.model.columns.ISeparationColumnIndices;
import org.eclipse.chemclipse.model.core.IChromatogram;
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
public class ChromatogramRetentionIndexer implements IProcessTypeSupplier {

	private static final String ID = "org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.retentionindex";
	private static final String NAME = "Retention Index Calculator (embedded)";
	private static final String DESCRIPTION = "Calculates the retention indices for scans and peaks in the chromatogram.";

	@Override
	public String getCategory() {

		return ICategories.CHROMATOGRAM_CALCULATOR;
	}

	@Override
	public Collection<IProcessSupplier<?>> getProcessorSuppliers() {

		return Collections.singleton(new ProcessSupplier(this));
	}

	private static final class ProcessSupplier extends AbstractProcessSupplier<RetentionIndexSettings> implements IChromatogramSelectionProcessSupplier<RetentionIndexSettings> {

		public ProcessSupplier(IProcessTypeSupplier parent) {

			super(ID, NAME, DESCRIPTION, RetentionIndexSettings.class, parent, DataCategory.MSD, DataCategory.CSD);
		}

		@Override
		public IChromatogramSelection apply(IChromatogramSelection chromatogramSelection, RetentionIndexSettings processSettings, ProcessExecutionContext context) throws InterruptedException {

			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			boolean extrapolateLeft = processSettings.isExtrapolateLeft();
			boolean extrapolateRight = processSettings.isExtrapolateRight();
			RetentionIndexMarker retentionIndexMarker = RetentionIndexSupport.getRetentionIndexMarker(processSettings.getRetentionIndexMarker(), chromatogram, extrapolateLeft, extrapolateRight);
			boolean processReferenceChromatograms = processSettings.isProcessReferenceChromatograms();
			RetentionIndexCalculator.calculateIndex(chromatogram, retentionIndexMarker, processReferenceChromatograms);
			/*
			 * Store the retention index marker in the chromatogram.
			 */
			if(processSettings.isStoreInChromatogram()) {
				ISeparationColumnIndices separationColumnIndices = chromatogram.getSeparationColumnIndices();
				RetentionIndexSupport.transferRetentionIndexMarker(retentionIndexMarker, separationColumnIndices);
			}
			//
			return chromatogramSelection;
		}
	}
}
