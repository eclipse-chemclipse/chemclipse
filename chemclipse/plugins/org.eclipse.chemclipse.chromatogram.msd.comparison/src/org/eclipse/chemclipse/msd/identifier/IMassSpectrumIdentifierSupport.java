/*******************************************************************************
 * Copyright (c) 2010, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Jan Holy - implementation
 * Christoph Läubrich - add getSupplier method
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier;

import java.util.Collection;

import org.eclipse.chemclipse.model.exceptions.NoIdentifierAvailableException;
import org.eclipse.chemclipse.model.identifier.core.ISupport;

public interface IMassSpectrumIdentifierSupport extends ISupport {

	@Override
	IMassSpectrumIdentifierSupplier getIdentifierSupplier(String identifierId) throws NoIdentifierAvailableException;

	Collection<IMassSpectrumIdentifierSupplier> getSuppliers();
}
