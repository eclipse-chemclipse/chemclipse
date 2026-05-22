/*******************************************************************************
 * Copyright (c) 2015, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import java.util.List;

public interface IIonProvider {

	/**
	 * Returns the ion list.
	 */
	List<IIon> getIons();

	/**
	 * Returns the number of stored ions.<br/>
	 * If no ions are stored, 0 will be returned.
	 */
	int getNumberOfIons();

	/**
	 * Returns true when there are no ions.
	 */
	boolean isEmpty();
}
