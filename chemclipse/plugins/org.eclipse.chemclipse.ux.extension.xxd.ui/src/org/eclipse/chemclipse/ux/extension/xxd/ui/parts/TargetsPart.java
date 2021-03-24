/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedTargetsUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class TargetsPart extends AbstractPart<ExtendedTargetsUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION;
	private Object cachedChromatogram = null;

	@Inject
	public TargetsPart(Composite parent) {

		super(parent, TOPIC);
	}

	@Override
	protected ExtendedTargetsUI createControl(Composite parent) {

		return new ExtendedTargetsUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			if(isCloseEvent(topic)) {
				getControl().update(null);
				return true;
			} else {
				Object object = objects.get(0);
				if(isChromatogramTopic(topic)) {
					if(object instanceof IChromatogramSelection) {
						IChromatogramSelection<?, ?> chromatogramSelection = (IChromatogramSelection<?, ?>)object;
						object = chromatogramSelection.getChromatogram();
						if(object != cachedChromatogram) {
							getControl().update(object);
							cachedChromatogram = object;
							return true;
						}
					}
				} else if(isScanTopic(topic) || isPeakTopic(topic) || isIdentificationTopic(topic)) {
					getControl().update(object);
					return true;
				}
			}
		}
		//
		return false;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return isChromatogramTopic(topic) || isScanTopic(topic) || isPeakTopic(topic) || isIdentificationTopic(topic) || isCloseEvent(topic);
	}

	private boolean isChromatogramTopic(String topic) {

		return IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isScanTopic(String topic) {

		return IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isPeakTopic(String topic) {

		return TOPIC.equals(topic);
	}

	private boolean isIdentificationTopic(String topic) {

		return IChemClipseEvents.TOPIC_IDENTIFICATION_TARGETS_UPDATE_SELECTION.equals(topic);
	}

	private boolean isCloseEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE.equals(topic);
	}
}
