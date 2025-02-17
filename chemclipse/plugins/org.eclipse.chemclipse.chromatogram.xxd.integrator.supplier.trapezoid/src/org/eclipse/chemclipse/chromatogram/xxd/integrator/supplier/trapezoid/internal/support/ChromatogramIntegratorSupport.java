/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.internal.support;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.ChromatogramIntegrationResult;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.ChromatogramIntegrationResults;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.IChromatogramIntegrationResult;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.IChromatogramIntegrationResults;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.Activator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.processor.BackgroundIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.processor.ChromatogramIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.settings.ChromatogramIntegrationSettings;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IIntegrationEntry;
import org.eclipse.chemclipse.model.core.ISignal;
import org.eclipse.chemclipse.model.implementation.IntegrationEntry;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.support.l10n.TranslationSupport;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.translation.TranslationService;

public class ChromatogramIntegratorSupport {

	private static TranslationService translationService = TranslationSupport.getTranslationService();
	//
	public static final String INTEGRATOR_DESCRIPTION = translationService.translate("%Trapezoid", Activator.getContributorURI());

	public IChromatogramIntegrationResults calculateChromatogramIntegrationResults(IChromatogramSelection chromatogramSelection, ChromatogramIntegrationSettings chromatogramIntegrationSettings, IProgressMonitor monitor) {

		/*
		 * Get the chromatogram and background area.
		 */
		double scaleFactor = chromatogramIntegrationSettings.getScaleFactor();
		List<IIntegrationEntry> chromatogramIntegrationEntries = calculateChromatogramIntegrationEntry(chromatogramSelection, scaleFactor, monitor);
		List<IIntegrationEntry> backgroundIntegrationEntries = calculateBackgroundIntegrationEntry(chromatogramSelection, scaleFactor, monitor);
		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		chromatogram.setIntegratedArea(chromatogramIntegrationEntries, backgroundIntegrationEntries, INTEGRATOR_DESCRIPTION);
		/*
		 * Chromatogram Results
		 */
		double chromatogramArea = getArea(chromatogramIntegrationEntries);
		double backgroundArea = getArea(backgroundIntegrationEntries);
		IChromatogramIntegrationResults chromatogramIntegrationResults = new ChromatogramIntegrationResults();
		IChromatogramIntegrationResult chromatogramIntegrationResult = new ChromatogramIntegrationResult(chromatogramArea, backgroundArea);
		chromatogramIntegrationResults.add(chromatogramIntegrationResult);
		return chromatogramIntegrationResults;
	}

	/**
	 * May return null.
	 * 
	 * @param chromatogramSelection
	 * @param monitor
	 * @return
	 */
	private List<IIntegrationEntry> calculateChromatogramIntegrationEntry(IChromatogramSelection chromatogramSelection, double scaleFactor, IProgressMonitor monitor) {

		List<IIntegrationEntry> chromatogramIntegrationEntries = new ArrayList<>();
		ChromatogramIntegrator chromatogramIntegrator = new ChromatogramIntegrator();
		double chromatogramArea = chromatogramIntegrator.integrate(chromatogramSelection);
		/*
		 * Create the MSD/FID entry.
		 */
		IIntegrationEntry chromatogramIntegrationEntry = null;
		if(chromatogramSelection instanceof IChromatogramSelectionMSD) {
			chromatogramIntegrationEntry = new IntegrationEntry(ISignal.TOTAL_INTENSITY, chromatogramArea * scaleFactor);
		} else if(chromatogramSelection instanceof IChromatogramSelectionCSD) {
			chromatogramIntegrationEntry = new IntegrationEntry(chromatogramArea * scaleFactor);
		}
		//
		if(chromatogramIntegrationEntry != null) {
			chromatogramIntegrationEntries.add(chromatogramIntegrationEntry);
		}
		//
		return chromatogramIntegrationEntries;
	}

	private List<IIntegrationEntry> calculateBackgroundIntegrationEntry(IChromatogramSelection chromatogramSelection, double scaleFactor, IProgressMonitor monitor) {

		List<IIntegrationEntry> backgroundIntegrationEntries = new ArrayList<>();
		BackgroundIntegrator backgroundIntegrator = new BackgroundIntegrator();
		double backgroundArea = backgroundIntegrator.integrate(chromatogramSelection);
		/*
		 * Create the MSD/FID entry.
		 */
		IIntegrationEntry backgroundIntegrationEntry = null;
		if(chromatogramSelection instanceof IChromatogramSelectionMSD) {
			backgroundIntegrationEntry = new IntegrationEntry(ISignal.TOTAL_INTENSITY, backgroundArea * scaleFactor);
		} else if(chromatogramSelection instanceof IChromatogramSelectionCSD) {
			backgroundIntegrationEntry = new IntegrationEntry(backgroundArea * scaleFactor);
		}
		//
		if(backgroundIntegrationEntry != null) {
			backgroundIntegrationEntries.add(backgroundIntegrationEntry);
		}
		//
		return backgroundIntegrationEntries;
	}

	private double getArea(List<IIntegrationEntry> integrationEntries) {

		double area = 0.0d;
		for(IIntegrationEntry integrationEntry : integrationEntries) {
			area += integrationEntry.getIntegratedArea();
		}
		return area;
	}
}
