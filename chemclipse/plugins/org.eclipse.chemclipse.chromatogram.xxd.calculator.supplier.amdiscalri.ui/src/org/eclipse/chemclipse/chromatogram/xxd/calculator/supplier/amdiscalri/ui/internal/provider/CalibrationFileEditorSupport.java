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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.editors.EditorCalibration;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.ux.extension.ui.editors.EditorDescriptor;
import org.eclipse.chemclipse.ux.extension.ui.provider.AbstractSupplierFileEditorSupport;
import org.eclipse.jface.resource.ImageDescriptor;

public class CalibrationFileEditorSupport extends AbstractSupplierFileEditorSupport implements EditorDescriptor {

	private static final Logger logger = Logger.getLogger(CalibrationFileEditorSupport.class);

	public CalibrationFileEditorSupport() {

		super(Collections.emptyList());
	}

	@Override
	public String getType() {

		return "CAL";
	}

	@Override
	public ImageDescriptor getImageDescriptor() {

		try {
			return ImageDescriptor.createFromURL(URI.create(EditorCalibration.ICON_URI).toURL());
		} catch(MalformedURLException e) {
			logger.warn(e);
			return null;
		}
	}

	@Override
	public boolean openEditor(File file, boolean batch) {

		return openEditor(file, Collections.emptyMap(), batch);
	}

	@Override
	public boolean openEditor(File file, Map<HeaderField, String> headerMap, boolean batch) {

		openEditor(file, null, EditorCalibration.ID, EditorCalibration.CONTRIBUTION_URI, EditorCalibration.ICON_URI, EditorCalibration.TOOLTIP, headerMap, batch);
		return true;
	}

	@Override
	public boolean openEditor(File file, ISupplier supplier) {

		return openEditor(file, false);
	}

	@Override
	public boolean openEditor(File file, Map<HeaderField, String> headerMap, ISupplier supplier) {

		return openEditor(file, headerMap, false);
	}
}
