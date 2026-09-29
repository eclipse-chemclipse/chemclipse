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
package org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

import org.apache.parquet.bytes.BytesInput;
import org.apache.parquet.compression.CompressionCodecFactory;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import io.airlift.compress.snappy.SnappyDecompressor;
import io.airlift.compress.zstd.ZstdDecompressor;

/**
 * Decompresses Parquet pages with pure Java codecs.
 * <p>
 * The factory parquet-java installs by default routes ZSTD through zstd-jni and SNAPPY through
 * snappy-java, which both ship platform specific native libraries. Reading mzPeak needs neither, so
 * aircompressor is used instead and the native libraries stay out of the dependency chain.
 */
public class ParquetCodecFactory implements CompressionCodecFactory {

	@Override
	public BytesInputCompressor getCompressor(CompressionCodecName codecName) {

		throw new UnsupportedOperationException("mzPeak is read only.");
	}

	@Override
	public BytesInputDecompressor getDecompressor(CompressionCodecName codecName) {

		return switch(codecName) {
			case UNCOMPRESSED -> new UncompressedDecompressor();
			case ZSTD -> new ZstandardDecompressor();
			case SNAPPY -> new SnappyBlockDecompressor();
			case GZIP -> new GzipDecompressor();
			default -> throw new UnsupportedOperationException("Unsupported Parquet compression codec: " + codecName);
		};
	}

	@Override
	public void release() {

	}

	private static class UncompressedDecompressor implements BytesInputDecompressor {

		@Override
		public BytesInput decompress(BytesInput bytes, int uncompressedSize) throws IOException {

			return bytes;
		}

		@Override
		public void decompress(ByteBuffer input, int compressedSize, ByteBuffer output, int uncompressedSize) throws IOException {

			output.put(input);
		}

		@Override
		public void release() {

		}
	}

	private abstract static class BlockDecompressor implements BytesInputDecompressor {

		protected abstract int decompress(byte[] input, int inputLength, byte[] output, int outputLength);

		@Override
		public BytesInput decompress(BytesInput bytes, int uncompressedSize) throws IOException {

			byte[] input = bytes.toInputStream().readAllBytes();
			byte[] output = new byte[uncompressedSize];
			decompress(input, input.length, output, uncompressedSize);
			return BytesInput.from(output);
		}

		@Override
		public void decompress(ByteBuffer input, int compressedSize, ByteBuffer output, int uncompressedSize) throws IOException {

			byte[] compressed = new byte[compressedSize];
			input.get(compressed);
			byte[] uncompressed = new byte[uncompressedSize];
			decompress(compressed, compressedSize, uncompressed, uncompressedSize);
			output.put(uncompressed, 0, uncompressedSize);
		}

		@Override
		public void release() {

		}
	}

	private static class ZstandardDecompressor extends BlockDecompressor {

		private final ZstdDecompressor decompressor = new ZstdDecompressor();

		@Override
		protected int decompress(byte[] input, int inputLength, byte[] output, int outputLength) {

			return decompressor.decompress(input, 0, inputLength, output, 0, outputLength);
		}
	}

	private static class SnappyBlockDecompressor extends BlockDecompressor {

		private final SnappyDecompressor decompressor = new SnappyDecompressor();

		@Override
		protected int decompress(byte[] input, int inputLength, byte[] output, int outputLength) {

			return decompressor.decompress(input, 0, inputLength, output, 0, outputLength);
		}
	}

	private static class GzipDecompressor implements BytesInputDecompressor {

		@Override
		public BytesInput decompress(BytesInput bytes, int uncompressedSize) throws IOException {

			try (GZIPInputStream inputStream = new GZIPInputStream(bytes.toInputStream())) {
				return BytesInput.from(inputStream.readNBytes(uncompressedSize));
			}
		}

		@Override
		public void decompress(ByteBuffer input, int compressedSize, ByteBuffer output, int uncompressedSize) throws IOException {

			byte[] compressed = new byte[compressedSize];
			input.get(compressed);
			try (GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
				output.put(inputStream.readNBytes(uncompressedSize));
			}
		}

		@Override
		public void release() {

		}
	}
}
