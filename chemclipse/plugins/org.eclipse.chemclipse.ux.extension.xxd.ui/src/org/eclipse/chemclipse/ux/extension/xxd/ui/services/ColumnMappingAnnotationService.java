/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.service.ColumnMappingSerializationService;
import org.eclipse.chemclipse.ux.extension.xxd.ui.methods.IAnnotationWidgetService;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors.ColumnMappingEditorUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

@Component(service = {IAnnotationWidgetService.class}, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ColumnMappingAnnotationService extends ColumnMappingSerializationService implements IAnnotationWidgetService {

	private ColumnMappingEditorUI columnMappingListEditor;

	@Override
	public Control createWidget(Composite parent, String description, Object currentSelection) {

		columnMappingListEditor = new ColumnMappingEditorUI(parent, SWT.NONE);
		columnMappingListEditor.setToolTipText(description);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 200;
		gridData.widthHint = 500;
		columnMappingListEditor.setLayoutData(gridData);
		//
		if(currentSelection instanceof String) {
			columnMappingListEditor.load((String)currentSelection);
		}
		//
		return columnMappingListEditor;
	}

	@Override
	public Object getValue(Object currentSelection) {

		return columnMappingListEditor.getSeparationColumnMapping();
	}
}