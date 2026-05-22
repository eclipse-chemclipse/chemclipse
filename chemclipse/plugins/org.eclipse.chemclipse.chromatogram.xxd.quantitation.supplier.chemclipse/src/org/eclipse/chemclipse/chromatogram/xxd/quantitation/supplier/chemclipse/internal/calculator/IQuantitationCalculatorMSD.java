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
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.internal.calculator;

import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.quantitation.IQuantitationCompound;
import org.eclipse.chemclipse.model.quantitation.IQuantitationEntry;
import org.eclipse.chemclipse.msd.model.exceptions.EvaluationException;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;

public interface IQuantitationCalculatorMSD {

	/**
	 * Returns a list of quantitation entries.
	 */
	List<IQuantitationEntry> calculateQuantitationResults(IPeak peak, IQuantitationCompound quantitationCompound) throws EvaluationException;

	/**
	 * Returns a list of quantitation entries.
	 */
	List<IQuantitationEntry> calculateQuantitationResults(IPeak peak, Set<IQuantitationCompound> quantitationCompounds, IProcessingInfo<?> processingInfo);
}
