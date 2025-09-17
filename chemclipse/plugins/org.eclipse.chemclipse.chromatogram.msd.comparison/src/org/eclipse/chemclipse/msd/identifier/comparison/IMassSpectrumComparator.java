/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier.comparison;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.MatchConstraints;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;

/**
 * All extension points which support a mass spectrum comparator must implement
 * this interface.
 */
public interface IMassSpectrumComparator {

	/**
	 * This methods compares two mass spectra and returns an instance of a {@link IProcessingInfo} object.<br/>
	 * If something has gone wrong, null will be returned.<br/>
	 * The mass spectra will be left as they are.
	 *
	 * @param unknown
	 *            the first {@link IScanMSD scan} to compare
	 * @param reference
	 *            the second {@link IScanMSD scan} to compare
	 * @return {@link IProcessingInfo}
	 */
	IProcessingInfo<IComparisonResult> compare(IScanMSD unknown, IScanMSD reference, MatchConstraints matchConstraints);

	/**
	 * Validates the unknown, reference mass spectrum and the ion range.
	 *
	 * @param unknown
	 * @param reference
	 * @return {@link IProcessingInfo}
	 */
	IProcessingInfo<IComparisonResult> validate(IScanMSD unknown, IScanMSD reference);

	IMassSpectrumComparisonSupplier getMassSpectrumComparisonSupplier();
}
