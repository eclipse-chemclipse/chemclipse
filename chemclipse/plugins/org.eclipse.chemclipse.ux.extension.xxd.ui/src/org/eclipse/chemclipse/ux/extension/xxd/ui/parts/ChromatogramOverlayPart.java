/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - allow zoom-lock
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.EditorUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedChromatogramOverlayUI;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class ChromatogramOverlayPart extends AbstractPart<ExtendedChromatogramOverlayUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION;
	private final EditorUpdateSupport editorUpdateSupport = new EditorUpdateSupport();

	@Inject
	public ChromatogramOverlayPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	protected ExtendedChromatogramOverlayUI createControl(Composite parent) {

		return new ExtendedChromatogramOverlayUI(parent, SWT.BORDER);
	}

	@Override
	@Focus
	public void setFocus() {

		getControl().update(editorUpdateSupport.getChromatogramSelections());
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			Object object = objects.get(0);
			if(isChromatogramUpdateEvent(topic)) {
				if(object instanceof IChromatogramSelection chromatogramSelection) {
					getControl().update(chromatogramSelection);
					return true;
				}
			} else if(isPeakUpdateEvent(topic) || isScanUpdateEvent(topic)) {
				IIdentificationTarget identificationTarget = null;
				if(object instanceof IScanMSD scanMSD) {
					identificationTarget = IIdentificationTarget.getIdentificationTarget(scanMSD);
				} else if(object instanceof IPeakMSD peakMSD) {
					identificationTarget = IIdentificationTarget.getIdentificationTarget(peakMSD);
				}
				getControl().update(identificationTarget);
				return true;
			}
		}

		return false;
	}

	@Override
	protected boolean isUpdateTopic(String topic) {

		return isChromatogramUpdateEvent(topic) || isPeakUpdateEvent(topic) || isScanUpdateEvent(topic);
	}

	private boolean isChromatogramUpdateEvent(String topic) {

		return TOPIC.equals(topic);
	}

	private boolean isScanUpdateEvent(String topic) {

		return IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION.equals(topic);
	}

	private boolean isPeakUpdateEvent(String topic) {

		return IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION.equals(topic);
	}
}
