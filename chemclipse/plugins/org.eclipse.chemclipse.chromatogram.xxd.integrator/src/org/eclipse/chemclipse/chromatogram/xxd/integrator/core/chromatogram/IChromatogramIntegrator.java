/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.integrator.core.chromatogram;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.chromatogram.IChromatogramIntegrationSettings;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.IChromatogramIntegrationResults;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IChromatogramIntegrator {

	IProcessingInfo<IChromatogramIntegrationResults> integrate(IChromatogramSelection<?, ?> chromatogramSelection, IChromatogramIntegrationSettings chromatogramIntegrationSettings, IProgressMonitor monitor);

	IProcessingInfo<IChromatogramIntegrationResults> integrate(IChromatogramSelection<?, ?> chromatogramSelection, IProgressMonitor monitor);
}
