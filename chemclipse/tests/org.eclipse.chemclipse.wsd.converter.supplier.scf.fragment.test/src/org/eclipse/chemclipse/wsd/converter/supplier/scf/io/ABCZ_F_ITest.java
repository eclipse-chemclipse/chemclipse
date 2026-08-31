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
import java.time.ZoneId;

import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.core.ChromatogramImportConverter;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ABCZ_F_ITest {

	private IChromatogramDSD chromatogram;

	@BeforeAll
	public void setUp() {

		File fileImport = new File("testdata/abcZ_F.scf");
		ChromatogramImportConverter chromatogramImportConverter = new ChromatogramImportConverter();
		IProcessingInfo<IChromatogramDSD> processingInfo = chromatogramImportConverter.convert(fileImport, new NullProgressMonitor());
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
		assertEquals("ANGNGCACCGTATTTGATCCGTTGCCGAAGGTTTGGGTAAAATTCGCGATTTATTGCGCCGTTACCACCGCGTCGGTCATGAGTTGGAAAACGGTTCGGGTGAGGCTTTGTTGAAAGAACTCAACGAATTACAACTTGAAATCGAAGCGAAGGACGGCTGGAAGCTGGATGCGGCAGTCAAGCAGACTTTGGGCGAACTCGGTTTGCCGGAAAACGAAAAAATCGGCAACCTTTCCGGCGGTCAGAAAAAGCGTGTCGCCTTGGCGCAGGCTTGGGTGCAGAAGCCCGACGTATTGCTGCTGGACGAACCGACCAACCATTTGGATATTGACGCGATTATCTGGTTGGAAAACCTGCTCAAGGCGTTTGAAGGCAGCTTGGTCGTGATTACCCACGACCGCCGTTTTTTGGATAATATCGCTACGCGGATTGTTGAACTTGACCGCGGCATTCTACGTTCCTATCCCGGCTCGTTCTCTAAATACAGTGAGAAAAAAGCGCAAGAGTTGGCAGTCAAAACCNGNAACAAANNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNTNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNT", chromatogram.getNucleotideSequence().toString());
	}

	@Test
	public void testTarget() {

		IScan firstScan = chromatogram.getScan(148);
		IIdentificationTarget identificationTarget = firstScan.getTargets().iterator().next();
		assertEquals("A", identificationTarget.getLibraryInformation().getName());
	}
}
