/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.converter.supplier.scf.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;

import org.eclipse.chemclipse.dsd.converter.chromatogram.ChromatogramConverterDSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.SCF;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ABCZ_F_ITest {

	private IChromatogramDSD chromatogram;

	@BeforeAll
	public void setUp() throws IOException {

		File fileImport = new File(SCF.TESTFILE_IMPORT_ABCZ_F);
		IProcessingInfo<IChromatogramDSD> processingInfo = ChromatogramConverterDSD.getInstance().convert(fileImport, SCF.EXTENSION_POINT_ID, new NullProgressMonitor());
		chromatogram = processingInfo.getProcessingResult();
	}

	@Test
	public void testChromatogram() {

		assertNotNull(chromatogram);
		assertEquals(7831, chromatogram.getNumberOfScans());
		assertEquals(4, chromatogram.getWavelengths().size());
		assertEquals("NM-1999-13-abcZ", chromatogram.getSampleName());
		assertEquals(2004, chromatogram.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().getYear());
		assertEquals("Zoo3730-1519-025", chromatogram.getInstrument());
		assertEquals("A-G-GCACCGTATTTGATCCGTTGCCGAAGGTTTGGGTAAAATTCGCGATTTATTGCGCCGTTACCACCGCGTCGGTCATGAGTTGGAAAACGGTTCGGG" + //
				"TGAGGCTTTGTTGAAAGAACTCAACGAATTACAACTTGAAATCGAAGCGAAGGACGGCTGGAAGCTGGATGCGGCAGTCAAGCAGACTTTGGGCGAACTCGGTTT" + //
				"GCCGGAAAACGAAAAAATCGGCAACCTTTCCGGCGGTCAGAAAAAGCGTGTCGCCTTGGCGCAGGCTTGGGTGCAGAAGCCCGACGTATTGCTGCTGGACGAACC" + //
				"GACCAACCATTTGGATATTGACGCGATTATCTGGTTGGAAAACCTGCTCAAGGCGTTTGAAGGCAGCTTGGTCGTGATTACCCACGACCGCCGTTTTTTGGATAA" + //
				"TATCGCTACGCGGATTGTTGAACTTGACCGCGGCATTCTACGTTCCTATCCCGGCTCGTTCTCTAAATACAGTGAGAAAAAAGCGCAAGAGTTGGCAGTCAAAAC" + //
				"C-G-AACAAA----------------------------------------------" + //
				"T----------------------------------------------------------------------------T", chromatogram.getMiscInfo());
	}
}
