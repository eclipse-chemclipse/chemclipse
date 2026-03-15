/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.implementation;

import org.eclipse.chemclipse.model.core.AbstractMeasurementResult;

public class MeasurementResult extends AbstractMeasurementResult<Object> {

	private static final long serialVersionUID = -8311848081084150204L;

	public MeasurementResult(String name, String identifier, String description, Object result) {

		super(name, identifier, description, result);
	}
}
