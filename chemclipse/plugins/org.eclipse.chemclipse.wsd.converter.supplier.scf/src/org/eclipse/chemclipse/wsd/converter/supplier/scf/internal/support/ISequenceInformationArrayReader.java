/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.scf.internal.support;

import org.eclipse.chemclipse.converter.io.support.IArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.model.Probability;

public interface ISequenceInformationArrayReader extends IArrayReader {

	int[] readPeakIndices(int numberBases);

	Probability readProbabilities(int numberBases);

	char[] readBaseCalls(int numberBases);

	byte[] readSpares(int numberBases);
}
