/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - add support for saving version 1.10
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.io;

import java.io.File;

import org.eclipse.chemclipse.converter.io.AbstractChromatogramWriter;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.io.ChromatogramWriterVersion110;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlReader110;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramWriter extends AbstractChromatogramWriter implements IChromatogramWriterMzML {

	@Override
	public void writeChromatogram(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		final IChromatogramWriterMzML chromatogramWriter = getChromatogramWriter();
		if(chromatogramWriter != null) {
			chromatogramWriter.writeChromatogram(file, chromatogram, monitor);
		}
	}

	private IChromatogramWriterMzML getChromatogramWriter() {

		String versionSave = PreferenceSupplier.getChromatogramVersionSave();
		if(versionSave.equals(XmlReader110.VERSION)) {
			return new ChromatogramWriterVersion110();
		}
		return null;
	}
}
