/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.core.chromatogram;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.settings.IChromatogramCalculatorSettings;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * A chromatogram calculator is used for example to calculate retention indices (RI) for
 * scans and/or peaks. It extends the chromatographic data. We first thought
 * to put this functionality under the category of filters. But people hesitate to use it,
 * cause filtering normally means to remove things. That's why an own category has been created.
 *
 */
public interface IChromatogramCalculator {

	/**
	 * Apply the calculator in the given chromatogram selection and take care of the
	 * calculator settings.<br/>
	 * Return an {@link IChromatogramCalculatorProcessingInfo} instance.<br/>
	 * If there is no monitor instance, use a {@link NullProgressMonitor}.
	 */
	IProcessingInfo<?> applyCalculator(IChromatogramSelection chromatogramSelection, IChromatogramCalculatorSettings chromatogramCalculatorSettings, IProgressMonitor monitor);

	/**
	 * Validates the selection and settings and returns a process info instance.
	 */
	IProcessingInfo<?> validate(IChromatogramSelection chromatogramSelection, IChromatogramCalculatorSettings chromatogramCalculatorSettings);
}
