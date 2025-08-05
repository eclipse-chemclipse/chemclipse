/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.support.SubtractSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedSubtractScanUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class SubtractScanPart extends AbstractPart<ExtendedSubtractScanUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_UPDATE_SESSION_SUBTRACT_MASS_SPECTRUM;

	@Inject
	public SubtractScanPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	protected ExtendedSubtractScanUI createControl(Composite parent) {

		return new ExtendedSubtractScanUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			if(isCloseEvent(topic)) {
				getControl().update(null);
				unloadData();
				return true;
			} else {
				Object object = objects.get(0);
				if(IChemClipseEvents.TOPIC_UPDATE_SESSION_SUBTRACT_MASS_SPECTRUM.equals(topic)) {
					getControl().update(SubtractSupport.getSessionSubtractMassSpectrum());
					/*
					 * Additionally try to get the latest chromatogram selection.
					 */
					List<Object> objectsSelection = Activator.getDefault().getDataUpdateSupport().getUpdates(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION);
					if(objectsSelection.size() == 1) {
						Object objectSelection = objectsSelection.get(0);
						if(objectSelection instanceof IChromatogramSelectionMSD) {
							getControl().update(objectSelection);
						}
					}
					return true;
				} else if(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION.equals(topic)) {
					if(object instanceof IChromatogramSelectionMSD) {
						getControl().update(object);
						return true;
					}
				} else if(IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION.equals(topic)) {
					if(object instanceof IPeakMSD) {
						getControl().update(object);
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return isSessionSubtractTopic(topic) || isChromatogramTopic(topic) || isPeakTopic(topic) || isCloseEvent(topic);
	}

	private boolean isSessionSubtractTopic(String topic) {

		return TOPIC.equals(topic);
	}

	private boolean isChromatogramTopic(String topic) {

		return IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isPeakTopic(String topic) {

		return IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isCloseEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE.equals(topic);
	}
}
