/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.amdis.io;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.database.IDatabaseExportConverter;
import org.eclipse.chemclipse.msd.converter.database.IDatabaseImportConverter;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msl.MSLDatabaseExportConverter;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msl.MSLDatabaseImportConverter;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

/**
 * Imports a msl file.
 */
@Ignore
public class MassSpectrumExportConverterTestCase {

	protected File exportFile;
	protected IDatabaseExportConverter exportConverter;
	protected File importFile;
	protected IMassSpectra massSpectra;
	protected IDatabaseImportConverter importConverter;

	@Before
	public void setUp() {

		exportConverter = new MSLDatabaseExportConverter();
		importConverter = new MSLDatabaseImportConverter();
	}

	@After
	public void tearDown() {

		if(exportFile.exists()) {
			exportFile.delete();
		}
	}
}
