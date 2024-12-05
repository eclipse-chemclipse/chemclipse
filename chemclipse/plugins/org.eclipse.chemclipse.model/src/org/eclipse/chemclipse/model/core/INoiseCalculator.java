/*******************************************************************************
 * Copyright (c) 2014, 2024 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - refactor the API for more general use cases
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.util.List;

import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.core.runtime.IProgressMonitor;

public interface INoiseCalculator {

	void reset();

	String getName();

	float getNoiseFactor();

	float getSignalToNoiseRatio(IChromatogram<?> chromatogram, float intensity);

	List<INoiseSegment> getNoiseSegments(IChromatogram<?> chromatogram, IProgressMonitor monitor);
}