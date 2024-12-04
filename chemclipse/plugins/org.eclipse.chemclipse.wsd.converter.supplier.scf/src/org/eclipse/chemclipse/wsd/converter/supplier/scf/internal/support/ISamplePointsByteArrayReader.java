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

public interface ISamplePointsByteArrayReader extends IArrayReader {

	byte[] readAdenine(int numberBases);

	byte[] readThymine(int numberBases);

	byte[] readGuanine(int numberBases);

	byte[] readCytosine(int numberBases);
}
