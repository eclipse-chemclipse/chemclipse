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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.download;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An entry of the NCBI BLAST database metadata, see
 * https://ftp.ncbi.nlm.nih.gov/blast/db/blastdb-metadata-1-1.json
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BlastDatabase( //
		@JsonProperty("dbname") String name, //
		@JsonProperty("dbtype") String type, //
		@JsonProperty("description") String description, //
		@JsonProperty("last-updated") String lastUpdated, //
		@JsonProperty("number-of-volumes") int volumes, //
		@JsonProperty("bytes-total") long size, //
		@JsonProperty("bytes-total-compressed") long compressedSize, //
		@JsonProperty("files") List<String> files) {

	/**
	 * Cloud mirrors offer no archives.
	 */
	public long downloadSize() {

		return compressedSize > 0 ? compressedSize : size;
	}
}
