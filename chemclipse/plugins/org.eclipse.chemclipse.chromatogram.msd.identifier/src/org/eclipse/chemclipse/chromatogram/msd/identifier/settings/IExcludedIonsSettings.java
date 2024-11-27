/*******************************************************************************
 * Copyright (c) 2008, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.identifier.settings;

import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;

public interface IExcludedIonsSettings extends IIdentifierSettings {

	/**
	 * Returns the excludedIons instance.
	 * 
	 * @return {@link IMarkedIons}
	 */
	IMarkedIons getExcludedIons();
}
