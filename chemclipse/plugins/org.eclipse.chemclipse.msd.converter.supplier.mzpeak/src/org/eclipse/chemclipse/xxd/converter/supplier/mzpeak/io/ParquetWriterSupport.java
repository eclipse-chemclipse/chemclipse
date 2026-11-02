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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import org.apache.parquet.conf.PlainParquetConfiguration;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.ExampleParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.io.LocalOutputFile;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

public class ParquetWriterSupport {

	/**
	 * The schema of a list of controlled or uncontrolled parameters, shared by all metadata tables.
	 */
	private static final String PARAMETERS = """
			optional group parameters (LIST) {
			  repeated group list {
			    optional group item {
			      optional group value {
			        optional int64 integer;
			        optional double float;
			        optional binary string (STRING);
			        optional boolean boolean;
			      }
			      optional binary accession (STRING);
			      optional binary name (STRING);
			      optional binary unit (STRING);
			    }
			  }
			}
			""";

	private static final String AUXILIARY_ARRAYS = """
			optional group auxiliary_arrays (LIST) {
			  repeated group list {
			    optional group item {
			      optional group data (LIST) {
			        repeated group list {
			          required int32 item (INTEGER(8,false));
			        }
			      }
			      optional group name {
			        optional group value {
			          optional int64 integer;
			          optional double float;
			          optional binary string (STRING);
			          optional boolean boolean;
			        }
			        optional binary accession (STRING);
			        optional binary name (STRING);
			        optional binary unit (STRING);
			      }
			      optional binary data_type (STRING);
			      optional binary compression (STRING);
			      optional binary unit (STRING);
			      %s
			      optional binary data_processing_id (STRING);
			    }
			  }
			}
			""".formatted(PARAMETERS);

	public static final MessageType SPECTRUM_METADATA = MessageTypeParser.parseMessageType("""
			message spectrum {
			  optional int64 index (INTEGER(64,false));
			  optional binary id (STRING);
			  optional int32 ms_level (INTEGER(8,false));
			  optional double time;
			  optional int32 scan_polarity (INTEGER(8,true));
			  optional binary spectrum_representation (STRING);
			  optional binary spectrum_type (STRING);
			  optional double lowest_observed_mz;
			  optional double highest_observed_mz;
			  optional int64 number_of_data_points (INTEGER(64,false));
			  optional int64 number_of_peaks (INTEGER(64,false));
			  optional double base_peak_mz;
			  optional float base_peak_intensity;
			  optional float total_ion_current;
			  optional binary data_processing_id (STRING);
			  %s
			  %s
			  optional int32 number_of_auxiliary_arrays (INTEGER(32,false));
			}
			""".formatted(PARAMETERS, AUXILIARY_ARRAYS));

	public static final MessageType SPECTRUM_SCANS = MessageTypeParser.parseMessageType("""
			message scan {
			  optional int64 source_index (INTEGER(64,false));
			  optional int64 scan_index (INTEGER(64,false));
			  optional double scan_start_time;
			  optional int32 instrument_configuration_id (INTEGER(32,false));
			  %s
			  optional group scan_windows (LIST) {
			    repeated group list {
			      optional group item {
			        optional float scan_window_lower_limit;
			        optional float scan_window_upper_limit;
			        %s
			      }
			    }
			  }
			}
			""".formatted(PARAMETERS, PARAMETERS));

	public static final MessageType SPECTRUM_POINTS = MessageTypeParser.parseMessageType("""
			message spectrum_data {
			  optional group point {
			    optional int64 spectrum_index (INTEGER(64,false));
			    optional double mz;
			    optional float intensity;
			  }
			}
			""");

	public static final MessageType CHROMATOGRAM_METADATA = MessageTypeParser.parseMessageType("""
			message chromatogram {
			  optional int64 index (INTEGER(64,false));
			  optional binary id (STRING);
			  optional int32 scan_polarity (INTEGER(8,true));
			  optional binary chromatogram_type (STRING);
			  optional binary data_processing_id (STRING);
			  optional int64 number_of_data_points (INTEGER(64,false));
			  %s
			  %s
			  optional int32 number_of_auxiliary_arrays (INTEGER(32,false));
			}
			""".formatted(PARAMETERS, AUXILIARY_ARRAYS));

	public static final MessageType CHROMATOGRAM_POINTS = MessageTypeParser.parseMessageType("""
			message chromatogram_data {
			  optional group point {
			    optional int64 chromatogram_index (INTEGER(64,false));
			    optional double time;
			    optional float intensity;
			    optional int64 ms_level;
			  }
			}
			""");

	private ParquetWriterSupport() {

	}

	/**
	 * Creates a ZSTD compressed Parquet file. Column statistics and the page index, which mzPeak
	 * requires, are written by default.
	 *
	 * @param keyValueMetadata
	 *            stored in the footer of the file
	 */
	public static ParquetWriter<Group> create(Path path, MessageType schema, Map<String, String> keyValueMetadata) throws IOException {

		return ExampleParquetWriter.builder(new LocalOutputFile(path)) //
				.withConf(new PlainParquetConfiguration()) //
				.withCodecFactory(new ParquetCodecFactory()) //
				.withCompressionCodec(CompressionCodecName.ZSTD) //
				.withWriteMode(ParquetFileWriter.Mode.OVERWRITE) //
				.withType(schema) //
				.withExtraMetaData(keyValueMetadata) //
				.build();
	}

	/**
	 * Adds the list valued columns every metadata table row carries, left empty.
	 */
	public static void addEmptyLists(Group row, String... fields) {

		for(String field : fields) {
			row.addGroup(field);
		}
	}
}
