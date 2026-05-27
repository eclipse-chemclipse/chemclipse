/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - execute updates in own eventqueue, optimize display of target spectrum
 * Lorenz Gerber - allow null IScanMSD in updateData
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedComparisonScanUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class ComparisonScanChartPart extends AbstractPart<ExtendedComparisonScanUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION;

	@Inject
	public ComparisonScanChartPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	protected ExtendedComparisonScanUI createControl(Composite parent) {

		return new ExtendedComparisonScanUI(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {

		getControl().setFocus();
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(isChromatogramCloseEvent(topic) || isLibraryCloseEvent(topic)) {
			getControl().clear();
			return false;
		} else {
			if(objects.size() == 1) {
				Object object = objects.get(0);
				if(isPeakUpdateEvent(topic) || isScanUpdateEvent(topic)) {
					/*
					 * Scan/Peak with or without target.
					 */
					IScanMSD scan = null;
					IIdentificationTarget identificationTarget = null;
					if(object instanceof IScanMSD scanMSD) {
						identificationTarget = IIdentificationTarget.getIdentificationTarget(scanMSD);
						scan = scanMSD;
					} else if(object instanceof IPeakMSD peakMSD) {
						identificationTarget = IIdentificationTarget.getIdentificationTarget(peakMSD);
						scan = peakMSD.getPeakModel().getPeakMassSpectrum();
					}
					getControl().update(scan, identificationTarget);
					return true;
				} else if(isScanTargetComparisonEvent(topic)) {
					if(object instanceof Object[] values) {
						Object object1 = values[0];
						Object object2 = values[1];
						if(object1 instanceof IScanMSD unknownMassSpectrum && object2 instanceof IIdentificationTarget identificationTarget) {
							getControl().update(unknownMassSpectrum, identificationTarget);
						}
					}
				} else if(isScanReferenceComparisonEvent(topic)) {
					if(object instanceof Object[] values && values.length >= 2) {
						IScanMSD unknownMassSpectrum = values[0] instanceof IScanMSD s ? s : null;
						IScanMSD referenceMassSpectrum = values[1] instanceof IScanMSD s ? s : null;
						getControl().update(unknownMassSpectrum, referenceMassSpectrum);
					}
				}
			}
		}

		return false;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return isPeakUpdateEvent(topic) || isScanUpdateEvent(topic) || isScanTargetComparisonEvent(topic) || isScanReferenceComparisonEvent(topic) || isChromatogramCloseEvent(topic) || isLibraryCloseEvent(topic);
	}

	private boolean isScanUpdateEvent(String topic) {

		return IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isPeakUpdateEvent(String topic) {

		return IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isScanTargetComparisonEvent(String topic) {

		return IChemClipseEvents.TOPIC_SCAN_TARGET_UPDATE_COMPARISON.equals(topic);
	}

	private boolean isScanReferenceComparisonEvent(String topic) {

		return IChemClipseEvents.TOPIC_SCAN_REFERENCE_UPDATE_COMPARISON.equals(topic);
	}

	private boolean isChromatogramCloseEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE.equals(topic);
	}

	private boolean isLibraryCloseEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_LIBRARY_CLOSE.equals(topic);
	}
}