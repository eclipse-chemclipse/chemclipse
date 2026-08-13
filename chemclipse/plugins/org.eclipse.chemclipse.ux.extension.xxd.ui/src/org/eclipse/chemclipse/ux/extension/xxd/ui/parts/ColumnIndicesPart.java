/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedColumnIndicesUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class ColumnIndicesPart extends AbstractLibraryInformationPart<ExtendedColumnIndicesUI> {

	@Inject
	public ColumnIndicesPart(Composite parent) {

		super(parent);
	}

	@Override
	protected ExtendedColumnIndicesUI createControl(Composite parent) {

		return new ExtendedColumnIndicesUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		super.updateData(objects, topic);

		if(objects.size() == 1) {
			if(isChromatogramEvent(topic)) {
				getControl().updateInput();
				return true;
			} else if(isCloseEvent(topic)) {
				getControl().setInput(null);
				unloadData();
				return false;
			}
		}

		return false;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return super.isUpdateTopic(topic) || isChromatogramEvent(topic) || isCloseEvent(topic);
	}

	private boolean isChromatogramEvent(String topic) {

		return IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isCloseEvent(String topic) {

		return IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE.equals(topic);
	}
}