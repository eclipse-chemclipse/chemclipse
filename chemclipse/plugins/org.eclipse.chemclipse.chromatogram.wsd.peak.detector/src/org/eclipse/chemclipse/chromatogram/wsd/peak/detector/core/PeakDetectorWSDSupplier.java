/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.peak.detector.core;

import org.eclipse.chemclipse.chromatogram.peak.detector.core.AbstractPeakDetectorSupplier;
import org.eclipse.chemclipse.chromatogram.wsd.peak.detector.settings.IPeakDetectorSettingsWSD;

public class PeakDetectorWSDSupplier extends AbstractPeakDetectorSupplier<IPeakDetectorSettingsWSD> implements IPeakDetectorWSDSupplier {

	public PeakDetectorWSDSupplier(String id, String description, String peakDetectorName) {
		super(id, description, peakDetectorName);
	}

	@Override
	public Class<? extends IPeakDetectorSettingsWSD> getSettingsClass() {

		return super.getSettingsClass();
	}
}
