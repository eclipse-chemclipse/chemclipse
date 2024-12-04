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
package org.eclipse.chemclipse.wsd.converter.supplier.scf.io;

import java.time.ZoneId;

import org.eclipse.chemclipse.wsd.converter.supplier.scf.ChromatogramReaderTestCase;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.SCF;
import org.junit.Test;

public class ABCZ_F_ITest extends ChromatogramReaderTestCase {

	@Override
	protected void setUp() throws Exception {

		extensionPointId = SCF.EXTENSION_POINT_ID;
		pathImport = SCF.getAbsolutePath(SCF.TESTFILE_IMPORT_ABCZ_F);
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	@Test
	public void testChromatogram() {

		assertNotNull(chromatogram);
		assertEquals(7831, chromatogram.getNumberOfScans());
		assertEquals(3, chromatogram.getReferencedChromatograms().size());
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
