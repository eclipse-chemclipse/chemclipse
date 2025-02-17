/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.vsd.filter.processors;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.chemclipse.chromatogram.vsd.filter.settings.WavenumberRemoverSettings;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.supplier.IChromatogramSelectionProcessSupplier;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.supplier.AbstractProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessTypeSupplier;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.eclipse.chemclipse.vsd.model.core.IChromatogramVSD;
import org.eclipse.chemclipse.vsd.model.core.IScanVSD;
import org.eclipse.chemclipse.vsd.model.core.selection.IChromatogramSelectionVSD;
import org.osgi.service.component.annotations.Component;

@Component(service = {IProcessTypeSupplier.class})
public class WavenumberRemover implements IProcessTypeSupplier {

	private static final String ID = "org.eclipse.chemclipse.chromatogram.vsd.filter.processors.wavenumberRemover";
	private static final String NAME = "Wavenumber Remover";
	private static final String DESCRIPTION = "Removes wavenumbers from a VSD chromatogram.";

	@Override
	public String getCategory() {

		return ICategories.SCAN_FILTER;
	}

	@Override
	public Collection<IProcessSupplier<?>> getProcessorSuppliers() {

		return Collections.singleton(new ProcessSupplier(this));
	}

	private static final class ProcessSupplier extends AbstractProcessSupplier<WavenumberRemoverSettings> implements IChromatogramSelectionProcessSupplier<WavenumberRemoverSettings> {

		public ProcessSupplier(IProcessTypeSupplier parent) {

			super(ID, NAME, DESCRIPTION, WavenumberRemoverSettings.class, parent, DataCategory.VSD);
		}

		@Override
		public IChromatogramSelection apply(IChromatogramSelection chromatogramSelection, WavenumberRemoverSettings processSettings, ProcessExecutionContext context) throws InterruptedException {

			if(chromatogramSelection instanceof IChromatogramSelectionVSD chromatogramSelectionISD) {
				Set<Integer> wavenumbers = getWavenumbers(processSettings.getWavenumbers());
				if(!wavenumbers.isEmpty()) {
					/*
					 * Settings
					 */
					MarkedTraceModus markedTraceModus = processSettings.getMarkMode();
					IChromatogramVSD chromatogramISD = chromatogramSelectionISD.getChromatogram();
					int startScan = chromatogramISD.getScanNumber(chromatogramSelection.getStartRetentionTime());
					int stopScan = chromatogramISD.getScanNumber(chromatogramSelection.getStopRetentionTime());
					//
					for(int scan = startScan; scan <= stopScan; scan++) {
						IScan scanX = chromatogramISD.getScan(scan);
						if(scanX instanceof IScanVSD scanISD) {
							if(MarkedTraceModus.INCLUDE.equals(markedTraceModus)) {
								scanISD.removeWavenumbers(wavenumbers);
							} else {
								scanISD.keepWavenumbers(wavenumbers);
							}
						}
					}
				}
			}
			return chromatogramSelection;
		}

		private Set<Integer> getWavenumbers(String selection) {

			Set<Integer> wavelengths = new HashSet<>();
			//
			String[] values = selection.split(" ");
			for(String value : values) {
				try {
					int wavelength = Integer.parseInt(value.trim());
					wavelengths.add(wavelength);
				} catch(NumberFormatException e) {
				}
			}
			//
			return wavelengths;
		}
	}
}
