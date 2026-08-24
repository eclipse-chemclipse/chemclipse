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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.dsd.converter.io;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.io.IChromatogramWriter;
import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IChromatogramDSDWriter extends IChromatogramWriter {

	void writeChromatogram(File file, IChromatogramDSD chromatogram, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException;
}
