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
 * Matthias Mailänder - rate by score instead of by status
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

public abstract class AbstractComparisonRatingSupplier implements IRatingSupplier {

	private static final long serialVersionUID = -4927422343294543420L;

	/*
	 * By default, the no match result is set.
	 * The comparison result can be updated via the method.
	 */
	private IComparisonResult comparisonResult = ComparisonResult.COMPARISON_RESULT_NO_MATCH;

	@Override
	public String getAdvise() {

		return "";
	}

	@Override
	public void updateComparisonResult(IComparisonResult comparisonResult) {

		this.comparisonResult = comparisonResult;
	}

	protected IComparisonResult getComparisonResult() {

		return comparisonResult;
	}
}
