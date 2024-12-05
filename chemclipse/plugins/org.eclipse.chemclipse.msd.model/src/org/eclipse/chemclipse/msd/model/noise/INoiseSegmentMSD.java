/*******************************************************************************
 * Copyright (c) 2010, 2024 Lablicate GmbH.
 * 
 * All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.noise;

import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;

public interface INoiseSegmentMSD {

	/**
	 * Returns the analysis segment. May return null.
	 * 
	 * @return {@link IAnalysisSegment}
	 */
	IAnalysisSegment getAnalysisSegment();

	/**
	 * Returns the noise mass spectrum. May return null.
	 * 
	 * @return
	 */
	ICombinedMassSpectrum getNoiseMassSpectrum();
}