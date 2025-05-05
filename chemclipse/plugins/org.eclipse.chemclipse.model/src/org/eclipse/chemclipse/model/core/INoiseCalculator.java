/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - refactor the API for more general use cases
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.util.List;

import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.core.runtime.IProgressMonitor;

public interface INoiseCalculator {

	void reset();

	String getName();

	float getNoiseFactor();

	float getSignalToNoiseRatio(IChromatogram chromatogram, ITotalScanSignals signals, float intensity);

	List<INoiseSegment> getNoiseSegments(IChromatogram chromatogram, ITotalScanSignals signals, IProgressMonitor monitor);
}
