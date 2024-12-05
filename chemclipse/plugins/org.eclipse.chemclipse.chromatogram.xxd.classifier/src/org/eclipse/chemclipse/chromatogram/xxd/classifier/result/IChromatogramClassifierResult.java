/*******************************************************************************
 * Copyright (c) 2011, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.classifier.result;

public interface IChromatogramClassifierResult {

	/**
	 * Returns the result status of the applied filter.
	 * 
	 * @return {@link ResultStatus}
	 */
	ResultStatus getResultStatus();

	/**
	 * Returns a description of the applied classifier or the failure that has been
	 * occurred.
	 * 
	 * @return String
	 */
	String getDescription();
}