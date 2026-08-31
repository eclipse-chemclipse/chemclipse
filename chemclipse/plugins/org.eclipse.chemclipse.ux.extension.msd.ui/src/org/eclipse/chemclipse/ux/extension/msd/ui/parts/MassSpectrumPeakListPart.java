/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.swt.ui.components.massspectrum.ExtendedMassSpectrumPeakListUI;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.msd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class MassSpectrumPeakListPart extends AbstractPart<ExtendedMassSpectrumPeakListUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION;
	private static final String TOPIC_MASS_SPECTRUM = IChemClipseEvents.TOPIC_MASS_SPECTRUM_UPDATE_SELECTION;

	@Inject
	public MassSpectrumPeakListPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	protected ExtendedMassSpectrumPeakListUI createControl(Composite parent) {

		return new ExtendedMassSpectrumPeakListUI(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			Object object = objects.get(0);
			if(object instanceof IRegularMassSpectrum regularMassSpectrum) {
				getControl().update(regularMassSpectrum);
				return true;
			}
		}
		return false;
	}

	@Override
	protected void subscribeAdditionalTopics() {

		subscribeAdditionalTopic(TOPIC_MASS_SPECTRUM, IChemClipseEvents.EVENT_BROKER_DATA);
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return TOPIC.equals(topic) || TOPIC_MASS_SPECTRUM.equals(topic);
	}
}
