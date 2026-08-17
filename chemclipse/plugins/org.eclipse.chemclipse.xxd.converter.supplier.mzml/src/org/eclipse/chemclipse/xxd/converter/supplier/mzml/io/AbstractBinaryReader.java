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
package org.eclipse.chemclipse.xxd.converter.supplier.mzml.io;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import ms.numpress.MSNumpress;

public abstract class AbstractBinaryReader {

	public static byte[] inflate(byte[] input) throws DataFormatException {

		try (Inflater inflater = new Inflater()) {
			inflater.setInput(input);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[8192];

			try {
				while(!inflater.finished()) {
					int count = inflater.inflate(buffer);
					if(count > 0) {
						outputStream.write(buffer, 0, count);
					} else if(inflater.needsInput() || inflater.needsDictionary()) {
						break;
					}
				}
				return outputStream.toByteArray();
			} finally {
				inflater.end();
			}
		}
	}

	public static boolean isNumpress(String accession) {

		return MSNumpress.ACC_NUMPRESS_LINEAR.equals(accession) || //
				MSNumpress.ACC_NUMPRESS_PIC.equals(accession) || //
				MSNumpress.ACC_NUMPRESS_SLOF.equals(accession);
	}
}