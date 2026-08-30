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

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class ChunkDecoder {

	public static final byte RAW = 0;
	private static final byte RUN_LENGTH = 1;
	private static final byte ZLIB = 2;
	private static final byte DELTA_16_BIT = 65;
	private static final byte DELTA_32_BIT = 66;
	private static final byte SHRINK_16_TO_8 = 70;
	private static final byte SHRINK_32_TO_8 = 71;
	private static final byte FOLLOW = 72;
	private static final byte ESCAPE = -128;

	private ChunkDecoder() {

	}

	public static byte[] decode(byte[] data) throws DataFormatException {

		byte[] decoded = data;
		while(decoded.length > 0 && decoded[0] != RAW) {
			decoded = switch(decoded[0]) {
				case RUN_LENGTH -> expandRunLength(decoded);
				case ZLIB -> inflate(decoded);
				case DELTA_16_BIT -> accumulate16Bit(decoded);
				case DELTA_32_BIT -> accumulate32Bit(decoded);
				case SHRINK_16_TO_8 -> expand8To16(decoded);
				case SHRINK_32_TO_8 -> expand8To32(decoded);
				case FOLLOW -> unfollow(decoded);
				default -> throw new DataFormatException("Unexpected format: " + decoded[0]);
			};
		}
		return decoded;
	}

	private static byte[] expandRunLength(byte[] data) throws DataFormatException {

		int length = ByteBuffer.wrap(data, 1, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
		byte guard = data[5];
		byte[] expanded = new byte[length];

		int source = 6;
		int target = 0;
		while(target < length) {
			byte value = data[source++];
			if(value != guard) {
				expanded[target++] = value;
			} else {
				int count = data[source++] & 0xFF;
				if(count == 0) {
					expanded[target++] = guard;
				} else {
					Arrays.fill(expanded, target, target + count, data[source++]);
					target += count;
				}
			}
		}
		return expanded;
	}

	private static byte[] inflate(byte[] data) throws DataFormatException {

		int uncompressedLength = ByteBuffer.wrap(data, 1, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
		byte[] inflated = new byte[uncompressedLength];

		try (Inflater inflater = new Inflater()) {
			inflater.setInput(data, 5, data.length - 5);
			int total = 0;
			while(total < uncompressedLength && !inflater.finished()) {
				int written = inflater.inflate(inflated, total, uncompressedLength - total);
				if(written == 0 && (inflater.needsInput() || inflater.needsDictionary())) {
					break;
				}
				total += written;
			}
			if(total != uncompressedLength) {
				throw new DataFormatException("Expected " + uncompressedLength + " but inflated " + total + " bytes.");
			}
		}
		return inflated;
	}

	private static byte[] accumulate16Bit(byte[] data) {

		int level = data[1];
		byte[] accumulated = new byte[data.length - 2];
		int u1 = 0;
		int u2 = 0;
		int u3 = 0;

		for(int i = 0; i + 1 < accumulated.length; i += 2) {
			int previous = predict(level, u1, u2, u3);
			u3 = u2;
			u2 = u1;
			u1 = (((data[i + 2] & 0xFF) << 8 | data[i + 3] & 0xFF) + previous) & 0xFFFF;
			accumulated[i] = (byte)(u1 >> 8);
			accumulated[i + 1] = (byte)u1;
		}
		return accumulated;
	}

	private static byte[] accumulate32Bit(byte[] data) {

		int level = data[1];
		byte[] accumulated = new byte[data.length - 4];
		int u1 = 0;
		int u2 = 0;
		int u3 = 0;

		for(int i = 0; i + 3 < accumulated.length; i += 4) {
			int previous = predict(level, u1, u2, u3);
			u3 = u2;
			u2 = u1;
			u1 = (data[i + 4] & 0xFF) << 24 | (data[i + 5] & 0xFF) << 16 | (data[i + 6] & 0xFF) << 8 | data[i + 7] & 0xFF;
			u1 += previous;
			accumulated[i] = (byte)(u1 >> 24);
			accumulated[i + 1] = (byte)(u1 >> 16);
			accumulated[i + 2] = (byte)(u1 >> 8);
			accumulated[i + 3] = (byte)u1;
		}
		return accumulated;
	}

	private static int predict(int level, int u1, int u2, int u3) {

		return switch(level) {
			case 1 -> u1;
			case 2 -> 2 * u1 - u2;
			case 3 -> 3 * u1 - 3 * u2 + u3;
			default -> 0;
		};
	}

	private static byte[] expand8To16(byte[] data) {

		ByteArrayOutputStream expanded = new ByteArrayOutputStream(2 * data.length);
		int source = 1;
		while(source < data.length) {
			byte value = data[source++];
			if(value == ESCAPE) {
				expanded.write(data[source++]);
				expanded.write(data[source++]);
			} else {
				expanded.write(value < 0 ? 0xFF : 0x00);
				expanded.write(value);
			}
		}
		return expanded.toByteArray();
	}

	private static byte[] expand8To32(byte[] data) {

		ByteArrayOutputStream expanded = new ByteArrayOutputStream(4 * data.length);
		int source = 1;
		while(source < data.length) {
			byte value = data[source++];
			if(value == ESCAPE) {
				expanded.write(data[source++]);
				expanded.write(data[source++]);
				expanded.write(data[source++]);
				expanded.write(data[source++]);
			} else {
				int sign = value < 0 ? 0xFF : 0x00;
				expanded.write(sign);
				expanded.write(sign);
				expanded.write(sign);
				expanded.write(value);
			}
		}
		return expanded.toByteArray();
	}

	private static byte[] unfollow(byte[] data) {

		byte[] successors = Arrays.copyOfRange(data, 1, 257);
		byte[] unfollowed = new byte[data.length - 257];

		unfollowed[0] = data[257];
		for(int i = 1; i < unfollowed.length; i++) {
			unfollowed[i] = (byte)(successors[unfollowed[i - 1] & 0xFF] - data[257 + i]);
		}
		return unfollowed;
	}
}
