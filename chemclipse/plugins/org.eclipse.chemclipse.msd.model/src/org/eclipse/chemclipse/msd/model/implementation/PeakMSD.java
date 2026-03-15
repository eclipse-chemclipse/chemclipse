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
package org.eclipse.chemclipse.msd.model.implementation;

import org.eclipse.chemclipse.msd.model.core.AbstractPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;

public class PeakMSD extends AbstractPeakMSD {

	public PeakMSD(IPeakModelMSD peakModel, String modelDescription) throws IllegalArgumentException {

		super(peakModel, modelDescription);
	}

	public PeakMSD(IPeakModelMSD peakModel) throws IllegalArgumentException {

		super(peakModel);
	}

	public PeakMSD(IPeakMSD template) {

		this(template.getPeakModel());
		setActiveForAnalysis(template.isActiveForAnalysis());
		setDetectorDescription(template.getDetectorDescription());
		setIntegratedArea(template.getIntegrationEntries(), template.getIntegratorDescription());
		setIntegratorDescription(template.getIntegratorDescription());
		setModelDescription(template.getModelDescription());
		setPeakType(template.getPeakType());
		setQuantifierDescription(template.getQuantifierDescription());
		setSuggestedNumberOfComponents(template.getSuggestedNumberOfComponents());
		getTargets().addAll(template.getTargets());
	}
}