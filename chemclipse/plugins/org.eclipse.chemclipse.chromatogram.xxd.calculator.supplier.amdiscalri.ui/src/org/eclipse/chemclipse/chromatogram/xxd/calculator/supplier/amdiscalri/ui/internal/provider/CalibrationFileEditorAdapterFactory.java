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
 * Aleksandar Kurtakov - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.internal.provider;

import org.eclipse.chemclipse.ux.extension.ui.editors.EditorDescriptor;
import org.eclipse.chemclipse.ux.extension.ui.provider.ISupplierFileEditorSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.editors.CalibrationFileSupplier;
import org.eclipse.core.runtime.IAdapterFactory;

public class CalibrationFileEditorAdapterFactory implements IAdapterFactory {

	private static final CalibrationFileEditorSupport EDITOR_SUPPORT = new CalibrationFileEditorSupport();

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {

		if(adaptableObject instanceof CalibrationFileSupplier && (adapterType == ISupplierFileEditorSupport.class || adapterType == EditorDescriptor.class)) {
			return adapterType.cast(EDITOR_SUPPORT);
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {

		return new Class<?>[]{ISupplierFileEditorSupport.class, EditorDescriptor.class};
	}
}
