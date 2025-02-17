/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.integrator.core.peaks;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IPeakIntegrationSettings;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.IPeakIntegrationResults;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IPeakIntegrator {

	IProcessingInfo<IPeakIntegrationResults> integrate(IPeak peak, IPeakIntegrationSettings peakIntegrationSettings, IProgressMonitor monitor);

	IProcessingInfo<IPeakIntegrationResults> integrate(IPeak peak, IProgressMonitor monitor);

	IProcessingInfo<IPeakIntegrationResults> integrate(List<? extends IPeak> peaks, IPeakIntegrationSettings peakIntegrationSettings, IProgressMonitor monitor);

	IProcessingInfo<IPeakIntegrationResults> integrate(List<? extends IPeak> peaks, IProgressMonitor monitor);

	IProcessingInfo<IPeakIntegrationResults> integrate(IChromatogramSelection chromatogramSelection, IPeakIntegrationSettings peakIntegrationSettings, IProgressMonitor monitor);

	IProcessingInfo<IPeakIntegrationResults> integrate(IChromatogramSelection chromatogramSelection, IProgressMonitor monitor);
}
