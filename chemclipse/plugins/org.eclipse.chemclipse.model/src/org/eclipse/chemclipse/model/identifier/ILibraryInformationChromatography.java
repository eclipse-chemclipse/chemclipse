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

public interface ILibraryInformationChromatography {

	int getRetentionTime();

	void setRetentionTime(int retentionTime);

	float getRetentionIndex();

	void setRetentionIndex(float retentionIndex);

	/**
	 * Returns an unmodifiable list of the available column
	 * index markers in the correct sort order.
	 */
	List<IColumnIndexMarker> getColumnIndexMarkers();

	void add(IColumnIndexMarker columnIndexMarker);

	void delete(IColumnIndexMarker columnIndexMarker);
}