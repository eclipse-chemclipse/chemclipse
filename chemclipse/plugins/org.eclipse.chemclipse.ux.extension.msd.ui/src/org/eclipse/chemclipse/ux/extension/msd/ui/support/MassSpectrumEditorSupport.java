/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.msd.ui.support;

import java.io.File;
import java.util.Map;

import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverter;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.ux.extension.msd.ui.editors.MassSpectrumEditor;
import org.eclipse.chemclipse.ux.extension.ui.provider.AbstractSupplierFileEditorSupport;

public class MassSpectrumEditorSupport extends AbstractSupplierFileEditorSupport {

	public MassSpectrumEditorSupport() {

		super(MassSpectrumConverter.getMassSpectrumConverterSupport().getSupplier());
	}

	@Override
	public String getType() {

		return TYPE_SCAN_MSD;
	}

	@Override
	public boolean openEditor(File file, Map<HeaderField, String> headerMap) {

		return openEditor(file, headerMap, false);
	}

	@Override
	public boolean openEditor(final File file, Map<HeaderField, String> headerMap, boolean batch) {

		/*
		 * Check that the selected file or directory is a valid database.
		 */
		if(isSupplierFile(file)) {
			openEditor(file, null, MassSpectrumEditor.ID, MassSpectrumEditor.CONTRIBUTION_URI, MassSpectrumEditor.ICON_URI, MassSpectrumEditor.TOOLTIP, headerMap, batch);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void openOverview(final File file) {

		/*
		 * Check that the selected file or directory is a valid mass spectrum.
		 */
		if(isSupplierFile(file)) {
			/*
			 * TODO - Probably show an abstract overview like the chromatogram overview part.
			 */
		}
	}

	@Override
	public boolean openEditor(File file, Map<HeaderField, String> headerMap, ISupplier supplier) {

		return openEditor(file, headerMap);
	}
}