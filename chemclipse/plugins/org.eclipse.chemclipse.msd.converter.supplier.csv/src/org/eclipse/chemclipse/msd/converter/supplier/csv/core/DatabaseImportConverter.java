/*******************************************************************************
 * Copyright (c) 2016, 2018 Matthias Mailänder.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.csv.core;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.database.AbstractDatabaseImportConverter;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class DatabaseImportConverter extends AbstractDatabaseImportConverter {

	@Override
	public IProcessingInfo convert(File file, IProgressMonitor monitor) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		processingInfo.addErrorMessage("CSV Mass Spectrum Import", "Mass spectrum import through CSV files isn't implemented yet.");
		return processingInfo;
	}
}
