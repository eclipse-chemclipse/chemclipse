/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.history.IEditHistory;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedEditHistoryUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class EditHistoryPart extends AbstractPart<ExtendedEditHistoryUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_EDIT_HISTORY_UPDATE;

	@Inject
	public EditHistoryPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	protected ExtendedEditHistoryUI createControl(Composite parent) {

		return new ExtendedEditHistoryUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			Object object = objects.get(0);
			//
			if(isEditHistoryTopic(topic)) {
				if(object instanceof IEditHistory editHistory) {
					getControl().setInput(editHistory);
					return true;
				}
			} else if(isChromatogramTopic(topic)) {
				if(object instanceof IChromatogramSelection chromatogramSelection) {
					getControl().setInput(chromatogramSelection.getChromatogram().getEditHistory());
					return true;
				}
			}
		}
		//
		return false;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return isEditHistoryTopic(topic) || isChromatogramTopic(topic);
	}

	private boolean isEditHistoryTopic(String topic) {

		return TOPIC.equals(topic);
	}

	private boolean isChromatogramTopic(String topic) {

		return IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION.equals(topic);
	}
}
