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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.report.massspectra;

import java.io.File;

import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.report.settings.IMassSpectraReportSettings;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IMassSpectraReportGenerator {

	IProcessingInfo<?> generate(File file, boolean append, IScanMSD massSpectrum, IMassSpectraReportSettings massSpectraReportSettings, IProgressMonitor monitor);

	IProcessingInfo<?> generate(File file, boolean append, IScanMSD massSpectrum, IProgressMonitor monitor);

	IProcessingInfo<?> generate(File file, boolean append, IMassSpectra massSpectra, IMassSpectraReportSettings massSpectraReportSettings, IProgressMonitor monitor);

	IProcessingInfo<?> generate(File file, boolean append, IMassSpectra massSpectra, IProgressMonitor monitor);

	/**
	 * This method validates whether the file is writable or not.<br/>
	 *
	 * @param file
	 * @return {@link IProcessingInfo}
	 */
	IProcessingInfo<?> validate(File file);
}
