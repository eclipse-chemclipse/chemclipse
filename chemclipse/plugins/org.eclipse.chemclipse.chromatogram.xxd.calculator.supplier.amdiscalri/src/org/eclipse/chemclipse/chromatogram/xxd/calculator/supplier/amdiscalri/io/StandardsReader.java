/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.converter.PathResolver;
import org.eclipse.chemclipse.model.columns.IRetentionIndexEntry;
import org.eclipse.chemclipse.model.columns.RetentionIndexEntry;
import org.eclipse.chemclipse.msd.converter.database.DatabaseConverter;
import org.eclipse.chemclipse.msd.model.core.ILibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class StandardsReader {

	public static final String ALKANES = "standards/alkanes.msl";

	public IMassSpectra getStandardsMassSpectra() throws IOException {

		Bundle bundle = FrameworkUtil.getBundle(getClass());
		File file = PathResolver.getFile(bundle, ALKANES);
		IProcessingInfo<IMassSpectra> processingInfo = DatabaseConverter.convert(file, new NullProgressMonitor());
		return processingInfo.getProcessingResult();
	}

	public List<IRetentionIndexEntry> getStandardsList() {

		List<IRetentionIndexEntry> retentionIndexEntries = new ArrayList<>();

		try {
			IMassSpectra massSpectra = getStandardsMassSpectra();

			for(IScanMSD massSpectrum : massSpectra.getList()) {
				if(massSpectrum instanceof ILibraryMassSpectrum libraryMassSpectrum) {
					String name = libraryMassSpectrum.getLibraryInformation().getName();
					int retentionTime = massSpectrum.getRetentionTime();
					float retentionIndex = massSpectrum.getRetentionIndex();
					IRetentionIndexEntry retentionIndexEntry = new RetentionIndexEntry(retentionTime, retentionIndex, name);
					retentionIndexEntries.add(retentionIndexEntry);
				}
			}
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retentionIndexEntries;
	}
}
