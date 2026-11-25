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
package org.eclipse.chemclipse.msd.filter.supplier.shift.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum.AbstractMassSpectrumFilter;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.MassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.msd.filter.supplier.shift.settings.MassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.IonMSn;
import org.eclipse.chemclipse.msd.model.implementation.IonTransition;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Corrects a mass calibration error by adding constant offsets to the m/z values of a scan.
 * The ion offset applies to the ions, the profile peaks and the product (Q3) filter of tandem MS transitions.
 * The precursor offset applies to the precursor of the scan and the precursor (Q1) filter of tandem MS transitions.
 */
public class MassSpectrumFilter extends AbstractMassSpectrumFilter {

	private static final String DESCRIPTION = "Mass Shift";

	@Override
	public IProcessingInfo<IMassSpectrumFilterResult> applyFilter(List<IScanMSD> massSpectra, IMassSpectrumFilterSettings filterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(massSpectra, filterSettings);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}
		if(filterSettings instanceof MassSpectrumFilterSettings massSpectrumFilterSettings) {
			for(IScanMSD massSpectrum : massSpectra) {
				processingInfo.addMessages(shift(massSpectrum, massSpectrumFilterSettings));
			}
		}
		return processingInfo;
	}

	private IProcessingInfo<IMassSpectrumFilterResult> shift(IScanMSD scanMSD, MassSpectrumFilterSettings filterSettings) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(scanMSD, filterSettings);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}
		double ionOffset = filterSettings.getIonOffset();
		double precursorOffset = filterSettings.getPrecursorOffset();
		if(ionOffset == 0 && precursorOffset == 0) {
			processingInfo.setProcessingResult(new MassSpectrumFilterResult(ResultStatus.OK, "No offset given, the mass spectrum is unchanged."));
			return processingInfo;
		}
		if(!isShiftable(scanMSD, ionOffset, precursorOffset)) {
			processingInfo.addErrorMessage(DESCRIPTION, "The offsets would result in a negative m/z value.");
			return processingInfo;
		}
		shiftIons(scanMSD, ionOffset, precursorOffset);
		if(ionOffset != 0 && scanMSD instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
			for(IMassSpectrumPeak peak : standaloneMassSpectrum.getPeaks()) {
				peak.setIon(peak.getIon() + ionOffset);
			}
		}
		if(scanMSD instanceof IRegularMassSpectrum regularMassSpectrum) {
			regularMassSpectrum.setPrecursorIon(shiftMZ(regularMassSpectrum.getPrecursorIon(), precursorOffset));
		}
		scanMSD.setDirty(true);
		processingInfo.setProcessingResult(new MassSpectrumFilterResult(ResultStatus.OK, "The ions were shifted by " + ionOffset + " Da and the precursor by " + precursorOffset + " Da."));
		return processingInfo;
	}

	/*
	 * Checked up front so that a scan is shifted either completely or not at all.
	 */
	private static boolean isShiftable(IScanMSD scanMSD, double ionOffset, double precursorOffset) {

		for(IIon ion : scanMSD.getIons()) {
			if(ion.getIon() + ionOffset < 0) {
				return false;
			}
		}
		if(scanMSD instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
			for(IMassSpectrumPeak peak : standaloneMassSpectrum.getPeaks()) {
				if(peak.getIon() + ionOffset < 0) {
					return false;
				}
			}
		}
		if(scanMSD instanceof IRegularMassSpectrum regularMassSpectrum) {
			double precursorIon = regularMassSpectrum.getPrecursorIon();
			if(precursorIon > 0 && precursorIon + precursorOffset < 0) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Ion transitions can't be modified, so tandem MS ions are replaced.
	 * All other ions are moved in place to keep their vendor specific type.
	 */
	private static void shiftIons(IScanMSD scanMSD, double ionOffset, double precursorOffset) {

		List<IIon> shiftedIons = new ArrayList<>(scanMSD.getNumberOfIons());
		for(IIon ion : scanMSD.getIons()) {
			double mz = ion.getIon() + ionOffset;
			if(ion instanceof IIonMSn ionMSn && ionMSn.getIonTransition() != null) {
				shiftedIons.add(new IonMSn(mz, ion.getAbundance(), shiftIonTransition(ionMSn.getIonTransition(), ionOffset, precursorOffset)));
			} else {
				ion.setIon(mz);
				shiftedIons.add(ion);
			}
		}
		scanMSD.removeAllIons();
		for(IIon ion : shiftedIons) {
			scanMSD.addIon(ion, false);
		}
	}

	private static IIonTransition shiftIonTransition(IIonTransition ionTransition, double ionOffset, double precursorOffset) {

		IIonTransition shiftedIonTransition = new IonTransition( //
				shiftMZ(ionTransition.getQ1StartIon(), precursorOffset), //
				shiftMZ(ionTransition.getQ1StopIon(), precursorOffset), //
				shiftMZ(ionTransition.getQ3StartIon(), ionOffset), //
				shiftMZ(ionTransition.getQ3StopIon(), ionOffset), //
				ionTransition.getCollisionEnergy(), //
				ionTransition.getQ1Resolution(), //
				ionTransition.getQ3Resolution(), //
				ionTransition.getTransitionGroup(), //
				ionTransition.getCompoundName());
		shiftedIonTransition.updateQ3Ion(shiftMZ(ionTransition.getQ3Ion(), ionOffset));
		shiftedIonTransition.setDwell(ionTransition.getDwell());
		return shiftedIonTransition;
	}

	/*
	 * Zero marks an unset value, which stays unset.
	 */
	private static double shiftMZ(double mz, double offset) {

		if(mz <= 0) {
			return mz;
		}
		return Math.max(0, mz + offset);
	}
}
