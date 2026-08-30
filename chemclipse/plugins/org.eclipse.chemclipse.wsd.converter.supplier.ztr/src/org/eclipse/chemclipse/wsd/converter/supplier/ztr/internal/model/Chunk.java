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
package org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.model;

import java.util.Arrays;
import java.util.zip.DataFormatException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.ChunkDecoder;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.internal.support.IChunkArrayReader;

public class Chunk {

	private static final Logger logger = Logger.getLogger(Chunk.class);

	String type;
	byte[] metadata;
	byte[] decodedData = new byte[0];
	boolean compressed;

	public Chunk(IChunkArrayReader reader) {

		type = reader.readString(4);
		long metadataLength = reader.readMetadataLength();
		metadata = reader.readMetadata((int)metadataLength);
		long dataLength = reader.readDataLength();
		byte[] data = reader.readData((int)dataLength);

		compressed = data.length > 0 && data[0] != ChunkDecoder.RAW;

		try {
			byte[] rawData = ChunkDecoder.decode(data);
			decodedData = Arrays.copyOfRange(rawData, 1, rawData.length); // format
		} catch(DataFormatException e) {
			logger.error(e);
		}
	}

	public String getType() {

		return type;
	}

	public byte[] getMetadata() {

		return metadata;
	}

	public byte[] getData() {

		return decodedData;
	}

	public boolean wasCompressed() {

		return compressed;
	}
}
