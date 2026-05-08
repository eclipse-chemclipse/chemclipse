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
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.parts;

import java.util.List;

import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.swt.ExtendedSPlot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jakarta.inject.Inject;

public class SPlotPart extends AbstractPartPCA<ExtendedSPlot> {

	@Inject
	public SPlotPart(Composite parent) {

		super(parent);
	}

	@Override
	public void setFocus() {

		ExtendedSPlot control = getControl();
		if(control != null) {
			control.setFocus();
		}
	}

	@Override
	protected ExtendedSPlot createControl(Composite parent) {

		return new ExtendedSPlot(parent, SWT.NONE);
	}

	@Override
	protected boolean updateData(List<Object> objects, String topic) {

		if(objects.size() == 1) {
			if(isUnloadEvent(topic)) {
				getControl().setInput(null);
				unloadData();
				return false;
			} else if(isUpdateFeaturesEvent(topic) || isUpdateResultEvent(topic) || isUpdateLabelsEvent(topic)) {
				getControl().updateInput();
				return true;
			} else {
				Object object = objects.get(0);
				if(object instanceof EvaluationPCA evaluationPCA) {
					getControl().setInput(evaluationPCA);
					return true;
				}
			}
		}
		return false;
	}
}