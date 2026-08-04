/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import java.util.List;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.support.traces.ITrace;

public interface ICombinedMassSpectrumCalculator {

	int size();

	void addIon(double ion, double abundance);

	void addIons(List<IIon> ions, IMarkedTraces<ITrace> excludedIons);

	void removeIon(double ion);

	void removeIons(IMarkedTraces<ITrace> excludedIons);

	ICombinedMassSpectrum createMassSpectrum(CalculationType calculationType);
}