/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedScanInfoUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class ChromtogramScanInfoPart extends AbstractPart<ExtendedScanInfoUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION;

	@Inject
	public ChromtogramScanInfoPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	protected ExtendedScanInfoUI createControl(Composite parent) {

		return new ExtendedScanInfoUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			Object object = objects.get(0);
			if(isUpdateEvent(topic)) {
				if(object instanceof IChromatogramSelectionMSD) {
					getControl().setInput(object);
					return true;
				}
			} else if(isCloseEvent(topic)) {
				getControl().setInput(null);
				unloadData();
				return false;
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

		return IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE.equals(topic);
	}
}
