/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.parts.AbstractPart;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.MeasurementResultNotification;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedMeasurementResultUI;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class MeasurementResultsPart extends AbstractPart<ExtendedMeasurementResultUI> {

	private static final String TOPIC = IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION;
	//
	@Inject
	private MeasurementResultNotification measurementResultNotification;

	@Inject
	public MeasurementResultsPart(Composite parent) {

		super(parent, TOPIC, Activator.getDefault().getDataUpdateSupport());
	}

	@Override
	protected ExtendedMeasurementResultUI createControl(Composite parent) {

		ExtendedMeasurementResultUI extendedMeasurementResultUI = new ExtendedMeasurementResultUI(parent, SWT.NONE);
		ComboViewer comboViewer = extendedMeasurementResultUI.getComboMeasurementResults();
		comboViewer.addSelectionChangedListener(event -> {

			IMeasurementResult<?> measurementResult = null;
			if(event.getSelection() instanceof IStructuredSelection structuredSelection) {
				if(structuredSelection.getFirstElement() instanceof IMeasurementResult<?> measurementResultSelection) {
					measurementResult = measurementResultSelection;
				}
			}
			//
			measurementResultNotification.select(measurementResult);
		});
		//
		return extendedMeasurementResultUI;
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			IChromatogram chromatogram = null;
			Object object = objects.get(0);
			if(isUpdateEvent(topic)) {
				if(object instanceof IChromatogramSelection selection) {
					chromatogram = selection.getChromatogram();
				}
			}
			//
			measurementResultNotification.select(null);
			getControl().setInput(chromatogram);
			//
			return true;
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