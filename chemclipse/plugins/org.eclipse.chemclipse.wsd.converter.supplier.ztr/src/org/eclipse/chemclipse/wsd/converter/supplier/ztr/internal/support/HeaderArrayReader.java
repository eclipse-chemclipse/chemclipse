/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.io.support.AbstractArrayReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.model.Version;

public class HeaderArrayReader extends AbstractArrayReader implements IHeaderArrayReader {

	public HeaderArrayReader(File file) throws IOException {

		super(file);
	}

	@Override
	public String readMagicNumber() {

		return readBytesAsString(8);
	}

	@Override
	public Version readVersion() {

		byte major = readByte();
		byte minor = readByte();
		return new Version(major, minor);
	}
}
