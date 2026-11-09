/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.l10n.ModelMessages;

public class TargetTransferSupport {

	/**
	 * Returns a message if something went wrong.
	 * Null if everything is ok.
	 * 
	 * 
	 * @param peaksSource
	 * @param peaksSink
	 * @param retentionTimeDelta
	 * @param useBestTargetOnly
	 * @return
	 */
	public String transferPeakTargets(List<? extends IPeak> peaksSource, List<? extends IPeak> peaksSink, int retentionTimeDelta, boolean useBestTargetOnly) {

		if(!peaksSource.isEmpty()) {
			if(!peaksSink.isEmpty()) {
				for(IPeak peakSink : peaksSink) {
					for(IPeak peakSource : peaksSource) {
						int retentionTimePeakSink = peakSink.getPeakModel().getRetentionTimeAtPeakMaximum();
						int retentionTimePeakSource = peakSource.getPeakModel().getRetentionTimeAtPeakMaximum();
						if(isPeakInFocus(retentionTimePeakSink, retentionTimePeakSource, retentionTimeDelta)) {
							/*
							 * Best target or all?
							 */
							if(useBestTargetOnly) {
								IIdentificationTarget peakTarget = IIdentificationTarget.getIdentificationTarget(peakSource);
								transferPeakTarget(peakTarget, peakSink);
							} else {
								for(IIdentificationTarget peakTarget : peakSource.getTargets()) {
									transferPeakTarget(peakTarget, peakSink);
								}
							}
						}
					}
				}
			} else {
				return ModelMessages.sinkChromatogramContainsNoPeaks;
			}
		} else {
			return ModelMessages.sourceChromatogramContainsNoPeaks;
		}

		return null;
	}

	public String transferScanTargets(List<IScan> scansSource, IChromatogram chromatogramSink, boolean useBestTargetOnly) {

		if(!scansSource.isEmpty()) {
			for(IScan scanSource : scansSource) {
				int scanNumber = chromatogramSink.getScanNumber(scanSource.getRetentionTime());
				if(scanNumber > 0) {
					IScan scanSink = chromatogramSink.getScan(scanNumber);
					if(scanSink != null) {
						if(useBestTargetOnly) {
							IIdentificationTarget scanTarget = IIdentificationTarget.getIdentificationTarget(scanSource);
							transferScanTarget(scanTarget, scanSink);
						} else {
							for(IIdentificationTarget scanTarget : scanSource.getTargets()) {
								transferScanTarget(scanTarget, scanSink);
							}
						}
					}
				}
			}
		} else {
			return ModelMessages.sourceChromatogramContainsNoIdentifiedScans;
		}

		return null;
	}

	private boolean isPeakInFocus(int retentionTimePeakSink, int retentionTimePeakSource, int retentionTimeDelta) {

		if(retentionTimePeakSink >= (retentionTimePeakSource - retentionTimeDelta) && retentionTimePeakSink <= (retentionTimePeakSource + retentionTimeDelta)) {
			return true;
		} else {
			return false;
		}
	}

	private void transferPeakTarget(IIdentificationTarget identificationTargetSource, IPeak peakSink) {

		peakSink.getTargets().add(copyIdentificationTarget(identificationTargetSource));
	}

	private void transferScanTarget(IIdentificationTarget identificationTargetSource, IScan scanSink) {

		scanSink.getTargets().add(copyIdentificationTarget(identificationTargetSource));
	}

	private IIdentificationTarget copyIdentificationTarget(IIdentificationTarget identificationTargetSource) {

		ILibraryInformation libraryInformation = new LibraryInformation(identificationTargetSource.getLibraryInformation());
		IComparisonResult comparisonResult = new ComparisonResult(identificationTargetSource.getComparisonResult());
		return new IdentificationTarget(libraryInformation, comparisonResult);
	}
}
