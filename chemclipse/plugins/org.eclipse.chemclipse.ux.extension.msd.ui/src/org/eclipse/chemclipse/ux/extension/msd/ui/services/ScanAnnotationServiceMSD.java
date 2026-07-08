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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.services;

import org.eclipse.chemclipse.msd.model.service.ScanSerializationServiceMSD;
import org.eclipse.chemclipse.support.ui.services.IAnnotationWidgetService;
import org.eclipse.chemclipse.ux.extension.msd.ui.swt.MassSpectrumEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

@Component(service = {IAnnotationWidgetService.class}, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ScanAnnotationServiceMSD extends ScanSerializationServiceMSD implements IAnnotationWidgetService {

	private MassSpectrumEditor editor;

	@Override
	public Control createWidget(Composite parent, String description, Object currentSelection) {

		editor = new MassSpectrumEditor(parent, SWT.BORDER);
		editor.setToolTipText(description);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 400;
		gridData.widthHint = 600;
		editor.setLayoutData(gridData);

		if(currentSelection instanceof String text) {
			editor.load(text);
		}

		return editor;
	}

	@Override
	public Object getValue(Object currentSelection) {

		return editor.getMassSpectrum();
	}
}