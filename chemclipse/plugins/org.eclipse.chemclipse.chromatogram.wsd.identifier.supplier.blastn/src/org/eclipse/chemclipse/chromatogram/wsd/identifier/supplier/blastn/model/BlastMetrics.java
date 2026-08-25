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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model;

/**
 * The metrics of a BLAST high-scoring segment pair. They are contributed via
 * the extension point "org.eclipse.chemclipse.model.comparisonMetrics", see
 * the plugin.xml of this plug-in.
 */
public class BlastMetrics {

	/**
	 * Algorithm id of a nucleotide BLAST search.
	 */
	public static final String ALGORITHM_BLASTN = "blastn";

	/**
	 * Normalized alignment score, independent of the size of the database.
	 * The higher the better, unbounded.
	 */
	public static final String BIT_SCORE = "blast.bitScore";
	/**
	 * Raw alignment score. The higher the better, unbounded.
	 */
	public static final String SCORE = "blast.score";
	/**
	 * Number of alignments of this quality expected by chance.
	 * The lower the better.
	 */
	public static final String EVALUE = "blast.evalue";
	/**
	 * Percentage of identical bases within the aligned region, 0 to 100.
	 */
	public static final String IDENTITY = "blast.identity";
	/**
	 * Percentage of the query sequence that the alignment covers with the subject sequence, 0 to 100.
	 */
	public static final String COVERAGE = "blast.coverage";
	/**
	 * Number of gaps within the aligned region. The lower the better.
	 */
	public static final String GAPS = "blast.gaps";

	private BlastMetrics() {

	}
}
