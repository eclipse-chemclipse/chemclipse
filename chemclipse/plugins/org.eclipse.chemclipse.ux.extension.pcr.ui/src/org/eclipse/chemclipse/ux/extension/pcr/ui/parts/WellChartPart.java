/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.pcr.model.core.IWell;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.pcr.ui.Activator;
import org.eclipse.chemclipse.ux.extension.pcr.ui.swt.ExtendedWellChartUI;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class WellChartPart extends AbstractPart<ExtendedWellChartUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_WELL_PCR_UPDATE_SELECTION;

	@Inject
	public WellChartPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	protected ExtendedWellChartUI createControl(Composite parent) {

		return new ExtendedWellChartUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			if(isCloseEvent(topic)) {
				getControl().update(null);
				unloadData();
				return false;
			} else {
				Object object = objects.get(0);
				if(object instanceof IWell well) {
					getControl().update(well);
					return true;
				} else {
					getControl().update(null);
					return true;
				}
			}
		}
		//
		return false;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return isUpdateEvent(topic) || isCloseEvent(topic);
	}

	private boolean isUpdateEvent(String topic) {

		return TOPIC.equals(topic);
	}

	private boolean isCloseEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_PCR_CLOSE.equals(topic);
	}
}
