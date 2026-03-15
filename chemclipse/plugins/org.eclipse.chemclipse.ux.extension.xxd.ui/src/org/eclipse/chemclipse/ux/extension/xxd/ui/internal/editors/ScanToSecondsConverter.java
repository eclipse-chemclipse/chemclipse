/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.editors;

import org.eclipse.swtchart.extensions.core.AbstractAxisScaleConverter;

public class ScanToSecondsConverter extends AbstractAxisScaleConverter {

	/*
	 * NMR
	 */
	private static final double SECOND_CORRELATION_FACTOR = 16384.0d;

	@Override
	public double convertToSecondaryUnit(double primaryValue) {

		return primaryValue / SECOND_CORRELATION_FACTOR;
	}

	@Override
	public double convertToPrimaryUnit(double secondaryValue) {

		return secondaryValue * SECOND_CORRELATION_FACTOR;
	}
}
