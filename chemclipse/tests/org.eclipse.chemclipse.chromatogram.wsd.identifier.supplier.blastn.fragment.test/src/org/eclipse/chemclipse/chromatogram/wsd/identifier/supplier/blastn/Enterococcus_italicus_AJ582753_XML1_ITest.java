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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io.XmlReaderVersion1;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.BlastOutput;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Hit;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Hits;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Hsp;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Iteration;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Parameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;

@TestInstance(Lifecycle.PER_CLASS)
public class Enterococcus_italicus_AJ582753_XML1_ITest {

	private BlastOutput blastOutput;

	@BeforeAll
	public void setUp() throws SAXException, IOException, JAXBException, ParserConfigurationException {

		File importFile = new File("testData/Enterococcus_italicus_AJ582753v1.xml");
		InputSource inputSource = new InputSource(new FileInputStream(importFile));
		blastOutput = XmlReaderVersion1.getBlastOutput(inputSource);
	}

	@Test
	public void testParameters() {

		assertEquals("16S_ribosomal_RNA", blastOutput.getDb());
		assertEquals("blastn", blastOutput.getProgram());

		Parameters parameters = blastOutput.getParam().getParameters();
		assertEquals(10, parameters.getExpect());
		assertEquals("L;m;", parameters.getFilter());
		assertEquals(0, parameters.getGapExtend().intValue());
		assertEquals(0, parameters.getGapOpen().intValue());
		assertEquals(1, parameters.getScMatch().intValue());
		assertEquals(-2, parameters.getScMismatch().intValue());

		assertEquals("Enterococcus_italicus__TP1.5__AJ582753", blastOutput.getQueryDef());
		assertEquals("Query_1", blastOutput.getQueryID());
		assertEquals(1396, blastOutput.getQueryLen().intValue());
		assertEquals("Zheng Zhang, Scott Schwartz, Lukas Wagner, and Webb Miller (2000), \"A greedy algorithm for aligning DNA sequences\", J Comput Biol 2000; 7(1-2):203-14.", blastOutput.getReference());
		assertEquals("BLASTN 2.16.0+", blastOutput.getVersion());
	}

	@Test
	public void testIterations() {

		Iteration firstIteration = blastOutput.getIterations().getIteration().getFirst();

		assertEquals(1, firstIteration.getIterNum().intValue());
		assertEquals("Enterococcus_italicus__TP1.5__AJ582753", firstIteration.getQueryDef());
		assertEquals("Query_1", firstIteration.getQueryID());
		assertEquals(1396, firstIteration.getQueryLen().intValue());
	}

	@Test
	public void testHits() {

		Hits hits = blastOutput.getIterations().getIteration().getFirst().getHits();

		Hit firstHit = hits.getHit().getFirst();
		assertEquals("NR_104571", firstHit.getAccession());
		assertEquals("Enterococcus italicus DSM 15952 strain TP1.5 16S ribosomal RNA, partial sequence", firstHit.getDef());
		assertEquals("gi|558508648|ref|NR_104571.1|", firstHit.getId());
		assertEquals(1396, firstHit.getLen().intValue());
		assertEquals(1, firstHit.getNum().intValue());

		Hsp hsp = firstHit.getHsps().getHsp().getFirst();
		assertEquals(1396, hsp.getAlignLen().intValue());
		assertEquals(2567.96, hsp.getBitScore());
		assertEquals(0, hsp.getEvalue());
		assertEquals(0, hsp.getGaps().intValue());
		assertEquals(1, hsp.getHitFrame().intValue());
		assertEquals(1, hsp.getHitFrom().intValue());
		assertEquals(1396, hsp.getHitTo().intValue());
		assertEquals("GCTGGCGGCGTGCCTAATACATGCAAGTTGAACGCTTCTTTCTTATCGAACTTCGGTTCACCAAGAAAGAAGAGTAGCGAACGGGTGAGTAACACGTGGGTAACCTGCCCATCAGCGGGGGATAACACTTGGAAACAGGTGCTAATACCGCATAATACTTTTTCTCTCATGAGTGAAAGTTGAAAGGCGCTTTTGCGTCACTGATGGATGGACCCGCGGTGCATTAGCTAGTTGGTAGGGTAACGGCCTACCAAGGCAACGATGCATAGCCGACCTGAGAGGGTGATCGGCCACACTGGGACTGAGACACGGCCCAGACTCCTACGGGAGGCAGCAGTAGGGAATCTTCGGCAATGGACGAAAGTCTGACCGAGCAACGCCGCGTGAGTGAAGAAGGTTTTCGGATCGTAAAACTCTGTTGTTAGAGAAGAACAAGGATGAGAAGAGAATGTTCATCCCTTGACGGTATCTAACCAGAAAGCCACGGCTAACTACGTGCCAGCAGCCGCGGTAATACGTAGGTGGCAAGCGTTGTCCGGATTTATTGGGCGTAAAGCGAGCGCAGGCGGTTCTTTAAGTCTGATGTGAAAGCCCCCGGCTCAACCGGGGAGGGTCATTGGAAACTGGGGAACTTGAGTGCANAANAGGANAGTGGAATTCCATGTGTAGCGGTGAAATGCGTATATATATGGAGGAACACCAGTGGCGAAGGCGGCTCTCTGGTCTGTAACTGACGCTGAGGCTCGAAAGCGTGGGGAGCAAACAGGATTAGATACCCTGGTAGTCCACGCCGTAAACGATGAGTGCTAAGTGTTGGAGGGTTTCCGCCCTTCAGTGCTGCAGCTAACGCATTAAGCACTCCGCCTGGGGAGTACGACCGCAAGGTTGAAACTCAAAGGAATTGACGGGGGCCCGCACAAGCGGTGGAGCATGTGGTTTAATTCGAAGCAACGCGAAGAACCTTACCAGGTCTTGACATCCTTTGACCACTCTAGAGATAGAGCTTTCCCTTCGGGGACAAAGTGACAGGTGGTGCATGGTTGTCGTCAGCTCGTGTCGTGAGATGTTGGGTTAAGTCCCGCAACGAGCGCAACCCTTATTGTTAGTTGCCATCATTTAGTTGGGCACTATAGCGAGACTGCCGGTGACAAACCGGAGGAAGGTGGGGATGACGTCAAATCATCATGCCCCTTATGACCTGGGCTACACACGTGCTACAATGGGAAGTACAACGAGTTGCGAAGTCGCGAGGCTAAGCTAATCTCTTAAAGCTTCTACTCAGTTCGGATTGTAGGCTGCAACTCGCCTCACATGAAGCCGGAATCGCTAGTAATCGCGGATCAGCACGCCGCGGTGAATACGTTCCCGGGCCTTGTACACACCGCCCGTCACACCA", hsp.getHseq());
		assertEquals(1396, hsp.getIdentity().intValue());
		assertEquals("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||", hsp.getMidline());
		assertEquals(1, hsp.getNum().intValue());
		assertEquals(1396, hsp.getPositive().intValue());
		assertEquals("GCTGGCGGCGTGCCTAATACATGCAAGTTGAACGCTTCTTTCTTATCGAACTTCGGTTCACCAAGAAAGAAGAGTAGCGAACGGGTGAGTAACACGTGGGTAACCTGCCCATCAGCGGGGGATAACACTTGGAAACAGGTGCTAATACCGCATAATACTTTTTCTCTCATGAGTGAAAGTTGAAAGGCGCTTTTGCGTCACTGATGGATGGACCCGCGGTGCATTAGCTAGTTGGTAGGGTAACGGCCTACCAAGGCAACGATGCATAGCCGACCTGAGAGGGTGATCGGCCACACTGGGACTGAGACACGGCCCAGACTCCTACGGGAGGCAGCAGTAGGGAATCTTCGGCAATGGACGAAAGTCTGACCGAGCAACGCCGCGTGAGTGAAGAAGGTTTTCGGATCGTAAAACTCTGTTGTTAGAGAAGAACAAGGATGAGAAGAGAATGTTCATCCCTTGACGGTATCTAACCAGAAAGCCACGGCTAACTACGTGCCAGCAGCCGCGGTAATACGTAGGTGGCAAGCGTTGTCCGGATTTATTGGGCGTAAAGCGAGCGCAGGCGGTTCTTTAAGTCTGATGTGAAAGCCCCCGGCTCAACCGGGGAGGGTCATTGGAAACTGGGGAACTTGAGTGCANAANAGGANAGTGGAATTCCATGTGTAGCGGTGAAATGCGTATATATATGGAGGAACACCAGTGGCGAAGGCGGCTCTCTGGTCTGTAACTGACGCTGAGGCTCGAAAGCGTGGGGAGCAAACAGGATTAGATACCCTGGTAGTCCACGCCGTAAACGATGAGTGCTAAGTGTTGGAGGGTTTCCGCCCTTCAGTGCTGCAGCTAACGCATTAAGCACTCCGCCTGGGGAGTACGACCGCAAGGTTGAAACTCAAAGGAATTGACGGGGGCCCGCACAAGCGGTGGAGCATGTGGTTTAATTCGAAGCAACGCGAAGAACCTTACCAGGTCTTGACATCCTTTGACCACTCTAGAGATAGAGCTTTCCCTTCGGGGACAAAGTGACAGGTGGTGCATGGTTGTCGTCAGCTCGTGTCGTGAGATGTTGGGTTAAGTCCCGCAACGAGCGCAACCCTTATTGTTAGTTGCCATCATTTAGTTGGGCACTATAGCGAGACTGCCGGTGACAAACCGGAGGAAGGTGGGGATGACGTCAAATCATCATGCCCCTTATGACCTGGGCTACACACGTGCTACAATGGGAAGTACAACGAGTTGCGAAGTCGCGAGGCTAAGCTAATCTCTTAAAGCTTCTACTCAGTTCGGATTGTAGGCTGCAACTCGCCTCACATGAAGCCGGAATCGCTAGTAATCGCGGATCAGCACGCCGCGGTGAATACGTTCCCGGGCCTTGTACACACCGCCCGTCACACCA", hsp.getQseq());
		assertEquals(1, hsp.getQueryFrame().intValue());
		assertEquals(1, hsp.getQueryFrom().intValue());
		assertEquals(1396, hsp.getQueryTo().intValue());
	}
}
