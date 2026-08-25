/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.identifier;

import java.util.List;

public interface ILibraryInformationFlavor {

	void clearFlavorMarker();

	/**
	 * Returns an unmodifiable list of the available
	 * flavor markers.
	 */
	List<IFlavorMarker> getFlavorMarkers();

	void add(IFlavorMarker flavorMarker);

	void delete(IFlavorMarker flavorMarker);
}