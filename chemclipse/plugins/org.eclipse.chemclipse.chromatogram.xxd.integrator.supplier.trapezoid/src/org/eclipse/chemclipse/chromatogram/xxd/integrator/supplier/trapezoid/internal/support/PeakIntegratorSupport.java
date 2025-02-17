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
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.internal.support;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.exceptions.ValueMustNotBeNullException;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.IPeakIntegrationResult;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.IPeakIntegrationResults;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.PeakIntegrationResults;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.Activator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.processor.PeakIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.settings.PeakIntegrationSettings;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.support.l10n.TranslationSupport;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.selection.IChromatogramSelectionWSD;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.translation.TranslationService;

public class PeakIntegratorSupport {

	private static TranslationService translationService = TranslationSupport.getTranslationService();

	public IPeakIntegrationResults calculatePeakIntegrationResults(List<? extends IPeak> peaks, PeakIntegrationSettings peakIntegrationSettings, IProgressMonitor monitor) throws ValueMustNotBeNullException {

		/*
		 * Get the peak integration results.
		 */
		monitor.subTask(translationService.translate("%IntegratePeaks", Activator.getContributorURI()));
		IPeakIntegrationResults peakIntegrationResults;
		if(peaks != null) {
			PeakIntegrator peakIntegrator = new PeakIntegrator();
			peakIntegrationResults = peakIntegrator.integrate(peaks, peakIntegrationSettings, monitor);
		} else {
			peakIntegrationResults = new PeakIntegrationResults();
		}
		return peakIntegrationResults;
	}

	public IPeakIntegrationResults calculatePeakIntegrationResults(IChromatogramSelection chromatogramSelection, PeakIntegrationSettings peakIntegrationSettings, IProgressMonitor monitor) throws ValueMustNotBeNullException {

		/*
		 * Get the chromatogram.
		 */
		List<? extends IPeak> peaks = null;
		if(chromatogramSelection instanceof IChromatogramSelectionMSD chromatogramSelectionMSD) {
			IChromatogramMSD chromatogramMSD = chromatogramSelectionMSD.getChromatogram();
			peaks = chromatogramMSD.getPeaks(chromatogramSelectionMSD);
		} else if(chromatogramSelection instanceof IChromatogramSelectionCSD chromatogramSelectionCSD) {
			IChromatogramCSD chromatogramFID = chromatogramSelectionCSD.getChromatogram();
			peaks = chromatogramFID.getPeaks(chromatogramSelectionCSD);
		} else if(chromatogramSelection instanceof IChromatogramSelectionWSD chromatogramSelectionWSD) {
			IChromatogramWSD chromatogramWSD = chromatogramSelectionWSD.getChromatogram();
			peaks = chromatogramWSD.getPeaks(chromatogramSelectionWSD);
		}
		//
		return calculatePeakIntegrationResults(peaks, peakIntegrationSettings, monitor);
	}

	public IPeakIntegrationResult calculatePeakIntegrationResult(IPeak peak, PeakIntegrationSettings peakIntegrationSettings, IProgressMonitor monitor) throws ValueMustNotBeNullException {

		monitor.subTask(translationService.translate("%IntegratePeak", Activator.getContributorURI()));
		PeakIntegrator peakIntegrator = new PeakIntegrator();
		return peakIntegrator.integrate(peak, peakIntegrationSettings);
	}
}
