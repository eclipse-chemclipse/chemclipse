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
package org.eclipse.chemclipse.wsd.converter.supplier.ztr.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.time.ZoneId;

import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.core.ChromatogramImportConverter;
import org.eclipse.chemclipse.wsd.converter.supplier.ztr.model.IVendorChromatogram;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.util.DefaultTimeZone;

@DefaultTimeZone("CET")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ABCZ_F_ITest {

	private IVendorChromatogram chromatogram;

	@Test
	@Order(1)
	public void testChromatogram() {

		File fileImport = new File("testdata/abcZ_F.ztr");
		ChromatogramImportConverter chromatogramImportConverter = new ChromatogramImportConverter();
		IProcessingInfo<IChromatogramDSD> processingInfo = chromatogramImportConverter.convert(fileImport, new NullProgressMonitor());
		chromatogram = (IVendorChromatogram)processingInfo.getProcessingResult();
		assertNotNull(chromatogram);
	}

	@Test
	public void testMetadata() {

		assertEquals("1.2", chromatogram.getVersion().toString());
		assertEquals("Sat Apr 03 08:08:35 CEST 2004", chromatogram.getDate().toString());
		assertEquals("NM-1999-13-abcZ", chromatogram.getSampleName());
		assertEquals("Zoo3730-1519-025", chromatogram.getInstrument());
	}

	@Test
	public void testSequence() {

		assertEquals("A-G-GCACCGTATTTGATCCGTTGCCGAAGGTTTGGGTAAAATTCGCGATTTATTGCGCCGTTACCACCGCGTCGGTCATGAGTTGGAAAACGGTTCGGG" + //
				"TGAGGCTTTGTTGAAAGAACTCAACGAATTACAACTTGAAATCGAAGCGAAGGACGGCTGGAAGCTGGATGCGGCAGTCAAGCAGACTTTGGGCGAACTCGGTTT" + //
				"GCCGGAAAACGAAAAAATCGGCAACCTTTCCGGCGGTCAGAAAAAGCGTGTCGCCTTGGCGCAGGCTTGGGTGCAGAAGCCCGACGTATTGCTGCTGGACGAACC" + //
				"GACCAACCATTTGGATATTGACGCGATTATCTGGTTGGAAAACCTGCTCAAGGCGTTTGAAGGCAGCTTGGTCGTGATTACCCACGACCGCCGTTTTTTGGATAA" + //
				"TATCGCTACGCGGATTGTTGAACTTGACCGCGGCATTCTACGTTCCTATCCCGGCTCGTTCTCTAAATACAGTGAGAAAAAAGCGCAAGAGTTGGCAGTCAAAAC" + //
				"C-G-AACAAA----------------------------------------------" + //
				"T----------------------------------------------------------------------------T", chromatogram.getNucleotideSequence());
	}

	@Test
	public void testScans() {

		assertEquals(7831, chromatogram.getNumberOfScans());
		assertEquals(4, chromatogram.getWavelengths().size());
		assertEquals("NM-1999-13-abcZ", chromatogram.getSampleName());
		assertEquals(2004, chromatogram.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().getYear());
		assertEquals("Zoo3730-1519-025", chromatogram.getInstrument());
	}

	@Test
	public void testTarget() {

		IScan firstScan = chromatogram.getScan(148);
		IIdentificationTarget identificationTarget = firstScan.getTargets().iterator().next();
		assertEquals("A", identificationTarget.getLibraryInformation().getName());
	}
}
