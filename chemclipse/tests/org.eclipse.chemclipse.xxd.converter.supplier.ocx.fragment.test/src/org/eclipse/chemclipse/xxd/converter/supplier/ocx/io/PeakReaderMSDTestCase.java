/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.io;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.peak.PeakConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.exceptions.TypeCastException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class PeakReaderMSDTestCase {

	protected IPeaksMSD peaks;
	protected String pathImport;
	protected File fileImport;
	private static final String EXTENSION_POINT_ID = "org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.peaks";

	@Before
	public void setUp() throws Exception {

		fileImport = new File(this.pathImport);
		IProcessingInfo<IPeaksMSD> processingInfo = PeakConverterMSD.convert(fileImport, EXTENSION_POINT_ID, new NullProgressMonitor());
		try {
			peaks = processingInfo.getProcessingResult();
		} catch(TypeCastException e) {
			peaks = null;
		}
	}
}
