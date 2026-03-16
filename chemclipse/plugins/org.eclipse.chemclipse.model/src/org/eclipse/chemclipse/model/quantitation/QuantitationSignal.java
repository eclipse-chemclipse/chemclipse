/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.quantitation;

public class QuantitationSignal extends AbstractQuantitationSignal {

	public QuantitationSignal(double signal, double relativeResponse) {

		super(signal, relativeResponse);
	}

	public QuantitationSignal(double signal, double relativeResponse, double uncertainty) {

		super(signal, relativeResponse, uncertainty);
	}

	public QuantitationSignal(double signal, double relativeResponse, double uncertainty, boolean use) {

		super(signal, relativeResponse, uncertainty, use);
	}
}