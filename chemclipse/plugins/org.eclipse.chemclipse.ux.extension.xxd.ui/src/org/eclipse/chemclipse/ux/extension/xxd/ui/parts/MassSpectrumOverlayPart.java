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
 * Matthias Mailänder - adapted for MALDI
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedMassSpectrumOverlayUI;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class MassSpectrumOverlayPart extends AbstractPart<ExtendedMassSpectrumOverlayUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION;

	@Inject
	public MassSpectrumOverlayPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	@Focus
	public void setFocus() {

		getControl().update();
	}

	@Override
	protected ExtendedMassSpectrumOverlayUI createControl(Composite parent) {

		return new ExtendedMassSpectrumOverlayUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		// No action required. Action on Focus.
		return true;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		// No action required. Action on Focus.
		return false;
	}

	@Override
	protected void preDestroy() {

		getControl().dispose();
		super.preDestroy();
	}
}
