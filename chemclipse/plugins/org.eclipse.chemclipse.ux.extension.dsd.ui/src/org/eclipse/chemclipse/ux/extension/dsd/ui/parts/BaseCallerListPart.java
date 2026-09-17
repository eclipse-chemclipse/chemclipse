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

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.dsd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.dsd.ui.swt.ExtendedBaseCallerListUI;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class BaseCallerListPart extends AbstractPart<ExtendedBaseCallerListUI> {

	private static final Logger logger = Logger.getLogger(BaseCallerListPart.class);
	private static final String TOPIC = IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION;

	@Inject
	public BaseCallerListPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	public void setFocus() {

		ExtendedBaseCallerListUI control = getControl();
		if(control != null) {
			getControl().setFocus();
		}
	}

	@Override
	protected ExtendedBaseCallerListUI createControl(Composite parent) {

		return new ExtendedBaseCallerListUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			Object object = objects.get(0);
			if(isCloseEvent(topic)) {
				getControl().updateChromatogramSelection(null);
				unloadData();
				return false;
			} else if(isChromatogramEvent(topic)) {
				if(object instanceof IChromatogramSelection chromatogramSelection) {
					getControl().updateChromatogramSelection(chromatogramSelection);
					return true;
				}
			} else if(isUpdateEditorEvent(topic)) {
				logger.info(object);
				getControl().refreshTableViewer();
				return true;
			} else if(isIdentificationTopic(topic)) {
				getControl().updateChromatogramSelection();
			}
		}

		return false;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return isChromatogramEvent(topic) || isUpdateEditorEvent(topic) || isCloseEvent(topic) || isIdentificationTopic(topic);
	}

	private boolean isChromatogramEvent(String topic) {

		return IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isUpdateEditorEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_UPDATE.equals(topic);
	}

	private boolean isCloseEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE.equals(topic);
	}

	private boolean isIdentificationTopic(String topic) {

		return IChemClipseEvents.TOPIC_IDENTIFICATION_TARGETS_UPDATE_SELECTION.equals(topic);
	}
}
