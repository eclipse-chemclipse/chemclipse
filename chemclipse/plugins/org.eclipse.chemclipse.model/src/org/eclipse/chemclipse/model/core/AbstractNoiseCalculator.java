/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.core;

public abstract class AbstractNoiseCalculator implements INoiseCalculator {

	private String id = ""; //$NON-NLS-1$

	@Override
	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

}