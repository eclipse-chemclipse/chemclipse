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
package org.eclipse.chemclipse.wsd.converter.supplier.scf.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.NotImplementedException;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.wsd.converter.io.AbstractChromatogramWSDWriter;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramWriter extends AbstractChromatogramWSDWriter {

	@Override
	public void writeChromatogram(File file, IChromatogramWSD chromatogram, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		throw new NotImplementedException();
	}
}
