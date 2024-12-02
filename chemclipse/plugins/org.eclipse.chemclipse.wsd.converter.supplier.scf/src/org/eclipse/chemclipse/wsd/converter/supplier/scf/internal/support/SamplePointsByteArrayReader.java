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

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.io.support.AbstractArrayReader;

public class SamplePointsByteArrayReader extends AbstractArrayReader implements ISamplePointsByteArrayReader {

	public SamplePointsByteArrayReader(File file) throws IOException {

		super(file);
	}

	@Override
	public byte[] readAdenine(int numberBases) {

		return readBytes(numberBases);
	}

	@Override
	public byte[] readThymine(int numberBases) {

		return readBytes(numberBases);
	}

	@Override
	public byte[] readGuanine(int numberBases) {

		return readBytes(numberBases);
	}

	@Override
	public byte[] readCytosine(int numberBases) {

		return readBytes(numberBases);
	}
}
