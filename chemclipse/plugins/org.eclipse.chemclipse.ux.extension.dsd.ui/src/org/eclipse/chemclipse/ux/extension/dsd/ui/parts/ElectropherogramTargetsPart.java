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
package org.eclipse.chemclipse.ux.extension.dsd.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.dsd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.dsd.ui.swt.ExtendedElectropherogramTargetsUI;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class ElectropherogramTargetsPart extends AbstractPart<ExtendedElectropherogramTargetsUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION;

	@Inject
	public ElectropherogramTargetsPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	protected ExtendedElectropherogramTargetsUI createControl(Composite parent) {

		return new ExtendedElectropherogramTargetsUI(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {

		ExtendedElectropherogramTargetsUI control = getControl();
		if(control != null) {
			control.setFocus();
		}
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			if(isCloseEvent(topic)) {
				getControl().clear();
				getControl().updateChromatogram(null);
				unloadData();
				return true;
			} else {
				Object object = objects.get(0);
				if(isChromatogramTopic(topic)) {
					if(object instanceof IChromatogramSelection chromatogramSelection) {
						getControl().updateChromatogram(chromatogramSelection);
						return true;
					}
				} else if(isPartUpdateEvent(topic)) {
					getControl().updatePart();
				} else if(isIdentificationTopic(topic)) {
					getControl().updatePart();
					return true;
				}
			}
		}

		return false;

	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return isChromatogramTopic(topic) || //
				isIdentificationTopic(topic) || //
				isPartUpdateEvent(topic) || //
				isCloseEvent(topic); //
	}

	private boolean isChromatogramTopic(String topic) {

		return IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isIdentificationTopic(String topic) {

		return IChemClipseEvents.TOPIC_IDENTIFICATION_TARGETS_UPDATE_SELECTION.equals(topic);
	}

	private boolean isPartUpdateEvent(String topic) {

		return IChemClipseEvents.TOPIC_PART_UPDATE.equals(topic);
	}

	private boolean isCloseEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE.equals(topic);
	}
}
