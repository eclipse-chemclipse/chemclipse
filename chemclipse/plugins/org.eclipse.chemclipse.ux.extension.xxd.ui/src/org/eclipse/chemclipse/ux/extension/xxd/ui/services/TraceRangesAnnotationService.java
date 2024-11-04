/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.services;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.tsd.model.services.TraceRangesSerializationService;
import org.eclipse.chemclipse.ux.extension.xxd.ui.methods.IAnnotationWidgetService;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors.TraceRangesEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

@Component(service = {IAnnotationWidgetService.class}, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class TraceRangesAnnotationService extends TraceRangesSerializationService implements IAnnotationWidgetService {

	private AtomicReference<TraceRangesEditor> traceRangesEditorControl = new AtomicReference<>();

	@Override
	public Control createWidget(Composite parent, String description, Object currentSelection) {

		TraceRangesEditor traceRangesEditor = new TraceRangesEditor(parent, SWT.NONE);
		traceRangesEditor.setToolTipText(description);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 200;
		gridData.widthHint = 500;
		traceRangesEditor.setLayoutData(gridData);
		//
		if(currentSelection instanceof String) {
			traceRangesEditor.load((String)currentSelection);
		}
		//
		traceRangesEditorControl.set(traceRangesEditor);
		return traceRangesEditor;
	}

	@Override
	public Object getValue(Object currentSelection) {

		return traceRangesEditorControl.get().getTraceRanges();
	}
}