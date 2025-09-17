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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier.library;

import org.eclipse.chemclipse.model.exceptions.ValueMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;

public abstract class AbstractLibraryService implements ILibraryService {

	public void validateIdentificationTarget(IIdentificationTarget identificationTarget) throws ValueMustNotBeNullException {

		if(identificationTarget == null) {
			throw new ValueMustNotBeNullException("The identification target must not be null.");
		}
	}
}
