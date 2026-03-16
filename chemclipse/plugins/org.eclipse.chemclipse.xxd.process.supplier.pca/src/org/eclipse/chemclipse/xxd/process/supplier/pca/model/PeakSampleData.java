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
 * Jan Holy - initial API and implementation
 * Philip Wenig - adjustment type
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.model;

import java.util.Optional;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.statistics.SampleData;

public class PeakSampleData extends SampleData<IPeak> {

	private Optional<IPeak> peak;

	public PeakSampleData() {

		super();
		peak = Optional.empty();
	}

	public PeakSampleData(double data, IPeak data2) {

		super(data, data2);
		peak = Optional.empty();
	}

	public Optional<IPeak> getPeak() {

		return peak;
	}

	public void setPeak(IPeak peak) {

		this.peak = Optional.of(peak);
	}
}