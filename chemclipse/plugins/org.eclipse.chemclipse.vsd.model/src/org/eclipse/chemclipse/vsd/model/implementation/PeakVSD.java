/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.vsd.model.implementation;

import org.eclipse.chemclipse.vsd.model.core.AbstractPeakVSD;
import org.eclipse.chemclipse.vsd.model.core.IPeakModelVSD;
import org.eclipse.chemclipse.vsd.model.core.IPeakVSD;

public class PeakVSD extends AbstractPeakVSD {

	public PeakVSD(IPeakModelVSD peakModel, String modelDescription) throws IllegalArgumentException {

		super(peakModel, modelDescription);
	}

	public PeakVSD(IPeakModelVSD peakModel) throws IllegalArgumentException {

		super(peakModel);
	}

	public PeakVSD(IPeakVSD template) {

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