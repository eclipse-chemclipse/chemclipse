/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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
 * Christoph Läubrich - add methods to query if progress is desired or a target is valid
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier.library;

import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public interface ILibraryService {

	IProcessingInfo<IMassSpectra> identify(IIdentificationTarget identificationTarget, IProgressMonitor monitor);

	boolean accepts(IIdentificationTarget identificationTarget);

	boolean requireProgressMonitor();
}
