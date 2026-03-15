/*******************************************************************************
 * Copyright (c) 2011, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.result;

import org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.model.TargetTraces;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.result.AbstractChromatogramClassifierResult;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.result.ResultStatus;

public class WncClassifierResult extends AbstractChromatogramClassifierResult {

	private TargetTraces targetTraces;

	public WncClassifierResult(ResultStatus resultStatus, String description) {

		super(resultStatus, description);
	}

	public WncClassifierResult(ResultStatus resultStatus, String description, TargetTraces targetTraces) {

		super(resultStatus, description);
		this.targetTraces = targetTraces;
	}

	public TargetTraces getTargetTraces() {

		return targetTraces;
	}
}