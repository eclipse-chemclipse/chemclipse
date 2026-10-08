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
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.converter.methods;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.core.IImportConverter;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IMethodImportConverter extends IImportConverter {

	IProcessingInfo<IProcessMethod> convert(File file, IProgressMonitor monitor) throws IOException;
}
