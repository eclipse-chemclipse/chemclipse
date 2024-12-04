/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.scf;

import java.io.File;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.chromatogram.ChromatogramConverterWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Ignore;

import junit.framework.TestCase;

@Ignore("Template to be inherited.")
public class ChromatogramReaderTestCase extends TestCase {

	protected IChromatogramWSD chromatogram;
	protected String extensionPointId;
	protected String pathImport;
	protected File fileImport;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		fileImport = new File(this.pathImport);
		IProcessingInfo<IChromatogramWSD> processingInfo = ChromatogramConverterWSD.getInstance().convert(fileImport, extensionPointId, new NullProgressMonitor());
		chromatogram = processingInfo.getProcessingResult();
	}

	@Override
	protected void tearDown() throws Exception {

		pathImport = null;
		fileImport = null;
		chromatogram = null;
		//
		super.tearDown();
	}
}
