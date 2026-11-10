/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.csd.model.implementation;

import org.eclipse.chemclipse.csd.model.core.AbstractScanCSD;

public class ScanCSD extends AbstractScanCSD {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = 5950220210929831243L;
	private float totalSignal = 0.0f;

	public ScanCSD(int retentionTime, float totalSignal) {

		super();
		setRetentionTime(retentionTime);
		setTotalSignal(totalSignal);
	}

	public ScanCSD(float totalSignal) {

		setTotalSignal(totalSignal);
	}

	@Override
	public float getTotalSignal() {

		return totalSignal;
	}

	@Override
	public void adjustTotalSignal(float totalSignal) {

		this.totalSignal = totalSignal;
	}

	private void setTotalSignal(float totalSignal) {

		this.totalSignal = totalSignal;
	}
}
