/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.core;

import java.io.File;

import org.eclipse.chemclipse.converter.core.IImportConverter;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.model.core.ISpectrumWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IScanImportConverter extends IImportConverter {

	IProcessingInfo<ISpectrumWSD> convert(File file, IProgressMonitor monitor);
}
