/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Matthias Mailänder - add support for saving version 1.05
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.io;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramWriter;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDWriter;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.io.ChromatogramWriterVersion105;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramWriter extends AbstractChromatogramWriter implements IChromatogramMSDWriter {

	@Override
	public void writeChromatogram(File file, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		final IChromatogramMSDWriter chromatogramWriter = getChromatogramWriter();
		if(chromatogramWriter != null) {
			chromatogramWriter.writeChromatogram(file, chromatogram, monitor);
		}
	}

	private IChromatogramMSDWriter getChromatogramWriter() {

		String versionSave = PreferenceSupplier.getChromatogramVersionSave();
		if(versionSave.equals(ChromatogramWriterVersion105.VERSION)) {
			return new ChromatogramWriterVersion105();
		}
		return null;
	}
}
