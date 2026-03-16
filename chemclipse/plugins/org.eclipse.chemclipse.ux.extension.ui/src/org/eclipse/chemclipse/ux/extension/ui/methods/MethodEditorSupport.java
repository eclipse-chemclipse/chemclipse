/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.ui.methods;

import java.io.File;
import java.util.Map;

import org.eclipse.chemclipse.converter.methods.MethodConverter;
import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.ux.extension.ui.editors.ProcessMethodEditor;
import org.eclipse.chemclipse.ux.extension.ui.provider.AbstractSupplierFileEditorSupport;

public class MethodEditorSupport extends AbstractSupplierFileEditorSupport {

	public MethodEditorSupport() {

		super(MethodConverter.getMethodConverterSupport().getSupplier());
	}

	@Override
	public String getType() {

		return TYPE_MTH;
	}

	@Override
	public boolean openEditor(File file, Map<HeaderField, String> headerMap) {

		return openEditor(file, headerMap, false);
	}

	@Override
	public boolean openEditor(File file, Map<HeaderField, String> headerMap, boolean batch) {

		if(isSupplierFile(file)) {
			openEditor(file, null, ProcessMethodEditor.ID, ProcessMethodEditor.CONTRIBUTION_URI, ProcessMethodEditor.ICON_URI, ProcessMethodEditor.TOOLTIP, headerMap, batch);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean openEditor(File file, Map<HeaderField, String> headerMap, ISupplier supplier) {

		return openEditor(file, headerMap, false);
	}
}