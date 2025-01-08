/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.io;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.msd.converter.io.IMassSpectraWriter;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io.MassSpectrumWriterVersion22;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class MassSpectrumWriter implements IMassSpectraWriter {

	@Override
	public void write(File file, IScanMSD massSpectrum, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		getMassSpectrumWriter().write(file, massSpectrum, append, monitor);
	}

	@Override
	public void write(File file, IMassSpectra massSpectra, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		getMassSpectrumWriter().write(file, massSpectra, append, monitor);
	}

	private IMassSpectraWriter getMassSpectrumWriter() {

		String versionSave = PreferenceSupplier.getMassSpectrumVersionSave();
		if(versionSave.equals(MassSpectrumWriterVersion22.VERSION)) {
			return new MassSpectrumWriterVersion22();
		}
		return null;
	}
}
