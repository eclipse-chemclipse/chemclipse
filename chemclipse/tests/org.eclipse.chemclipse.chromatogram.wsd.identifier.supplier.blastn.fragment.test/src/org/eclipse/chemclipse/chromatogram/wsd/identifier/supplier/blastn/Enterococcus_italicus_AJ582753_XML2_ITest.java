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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io.XmlReaderVersion2;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.BlastOutput2;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Hit;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.HitDescr;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Hsp;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Parameters;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search.Hits;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;

@TestInstance(Lifecycle.PER_CLASS)
public class Enterococcus_italicus_AJ582753_XML2_ITest {

	private BlastOutput2 blastOutput;

	@BeforeAll
	public void setUp() throws SAXException, IOException, JAXBException, ParserConfigurationException {

		File importFile = new File("testData/Enterococcus_italicus_AJ582753v2.xml");
		InputSource inputSource = new InputSource(new FileInputStream(importFile));
		blastOutput = XmlReaderVersion2.getBlastOutput(inputSource);
	}

	@Test
	public void testParameters() {

		assertNotNull(blastOutput);
		assertEquals("blastn", blastOutput.getReport().getReport().getProgram());
		assertEquals("BLASTN 2.16.0+", blastOutput.getReport().getReport().getVersion());
		assertEquals("Zheng Zhang, Scott Schwartz, Lukas Wagner, and Webb Miller (2000), \"A greedy algorithm for aligning DNA sequences\", J Comput Biol 2000; 7(1-2):203-14.", blastOutput.getReport().getReport().getReference());

		Parameters parameters = blastOutput.getReport().getReport().getParams().getParameters();
		assertEquals(10, parameters.getExpect());
		assertEquals(1, parameters.getScMatch().intValue());
		assertEquals(-2, parameters.getScMismatch().intValue());
		assertEquals(0, parameters.getGapOpen().intValue());
		assertEquals(0, parameters.getGapExtend().intValue());
		assertEquals("L;m;", parameters.getFilter());
	}

	@Test
	public void testSearch() {

		Search search = blastOutput.getReport().getReport().getResults().getResults().getSearch().getSearch();

		assertEquals("Query_1", search.getQueryId());
		assertEquals("Enterococcus_italicus__TP1.5__AJ582753", search.getQueryTitle());
		assertEquals(1396, search.getQueryLen().intValue());
	}

	@Test
	public void testFirsthit() {

		Hits hits = blastOutput.getReport().getReport().getResults().getResults().getSearch().getSearch().getHits();
		Hit firstHit = hits.getHit().getFirst();
		assertEquals(1, firstHit.getNum().intValue());
		assertEquals(1396, firstHit.getLen().intValue());

		HitDescr hitDescription = firstHit.getDescription().getHitDescr().getFirst();
		assertEquals("gi|558508648|ref|NR_104571.1|", hitDescription.getId());
		assertEquals(888064, hitDescription.getTaxid().intValue());
		assertEquals("NR_104571", hitDescription.getAccession());
		assertEquals("Enterococcus italicus DSM 15952 strain TP1.5 16S ribosomal RNA, partial sequence", hitDescription.getTitle());

		Hsp hsp = firstHit.getHsps().getHsp().getFirst();
		assertEquals(1, hsp.getNum().intValue());
		assertEquals(2567.96, hsp.getBitScore());
		assertEquals(1390, hsp.getScore());
		assertEquals(0, hsp.getEvalue());
		assertEquals(1396, hsp.getIdentity().intValue());
		assertEquals(1, hsp.getQueryFrom().intValue());
		assertEquals("Plus", hsp.getQueryStrand());
		assertEquals(1, hsp.getHitFrom().intValue());
		assertEquals(1396, hsp.getHitTo().intValue());
		assertEquals("Plus", hsp.getHitStrand());
		assertEquals(1396, hsp.getAlignLen().intValue());
		assertEquals(0, hsp.getGaps().intValue());
		assertEquals("GCTGGCGGCGTGCCTAATACATGCAAGTTGAACGCTTCTTTCTTATCGAACTTCGGTTCACCAAGAAAGAAGAGTAGCGAACGGGTGAGTAACACGTGGGTAACCTGCCCATCAGCGGGGGATAACACTTGGAAACAGGTGCTAATACCGCATAATACTTTTTCTCTCATGAGTGAAAGTTGAAAGGCGCTTTTGCGTCACTGATGGATGGACCCGCGGTGCATTAGCTAGTTGGTAGGGTAACGGCCTACCAAGGCAACGATGCATAGCCGACCTGAGAGGGTGATCGGCCACACTGGGACTGAGACACGGCCCAGACTCCTACGGGAGGCAGCAGTAGGGAATCTTCGGCAATGGACGAAAGTCTGACCGAGCAACGCCGCGTGAGTGAAGAAGGTTTTCGGATCGTAAAACTCTGTTGTTAGAGAAGAACAAGGATGAGAAGAGAATGTTCATCCCTTGACGGTATCTAACCAGAAAGCCACGGCTAACTACGTGCCAGCAGCCGCGGTAATACGTAGGTGGCAAGCGTTGTCCGGATTTATTGGGCGTAAAGCGAGCGCAGGCGGTTCTTTAAGTCTGATGTGAAAGCCCCCGGCTCAACCGGGGAGGGTCATTGGAAACTGGGGAACTTGAGTGCANAANAGGANAGTGGAATTCCATGTGTAGCGGTGAAATGCGTATATATATGGAGGAACACCAGTGGCGAAGGCGGCTCTCTGGTCTGTAACTGACGCTGAGGCTCGAAAGCGTGGGGAGCAAACAGGATTAGATACCCTGGTAGTCCACGCCGTAAACGATGAGTGCTAAGTGTTGGAGGGTTTCCGCCCTTCAGTGCTGCAGCTAACGCATTAAGCACTCCGCCTGGGGAGTACGACCGCAAGGTTGAAACTCAAAGGAATTGACGGGGGCCCGCACAAGCGGTGGAGCATGTGGTTTAATTCGAAGCAACGCGAAGAACCTTACCAGGTCTTGACATCCTTTGACCACTCTAGAGATAGAGCTTTCCCTTCGGGGACAAAGTGACAGGTGGTGCATGGTTGTCGTCAGCTCGTGTCGTGAGATGTTGGGTTAAGTCCCGCAACGAGCGCAACCCTTATTGTTAGTTGCCATCATTTAGTTGGGCACTATAGCGAGACTGCCGGTGACAAACCGGAGGAAGGTGGGGATGACGTCAAATCATCATGCCCCTTATGACCTGGGCTACACACGTGCTACAATGGGAAGTACAACGAGTTGCGAAGTCGCGAGGCTAAGCTAATCTCTTAAAGCTTCTACTCAGTTCGGATTGTAGGCTGCAACTCGCCTCACATGAAGCCGGAATCGCTAGTAATCGCGGATCAGCACGCCGCGGTGAATACGTTCCCGGGCCTTGTACACACCGCCCGTCACACCA", hsp.getQseq());
		assertEquals("GCTGGCGGCGTGCCTAATACATGCAAGTTGAACGCTTCTTTCTTATCGAACTTCGGTTCACCAAGAAAGAAGAGTAGCGAACGGGTGAGTAACACGTGGGTAACCTGCCCATCAGCGGGGGATAACACTTGGAAACAGGTGCTAATACCGCATAATACTTTTTCTCTCATGAGTGAAAGTTGAAAGGCGCTTTTGCGTCACTGATGGATGGACCCGCGGTGCATTAGCTAGTTGGTAGGGTAACGGCCTACCAAGGCAACGATGCATAGCCGACCTGAGAGGGTGATCGGCCACACTGGGACTGAGACACGGCCCAGACTCCTACGGGAGGCAGCAGTAGGGAATCTTCGGCAATGGACGAAAGTCTGACCGAGCAACGCCGCGTGAGTGAAGAAGGTTTTCGGATCGTAAAACTCTGTTGTTAGAGAAGAACAAGGATGAGAAGAGAATGTTCATCCCTTGACGGTATCTAACCAGAAAGCCACGGCTAACTACGTGCCAGCAGCCGCGGTAATACGTAGGTGGCAAGCGTTGTCCGGATTTATTGGGCGTAAAGCGAGCGCAGGCGGTTCTTTAAGTCTGATGTGAAAGCCCCCGGCTCAACCGGGGAGGGTCATTGGAAACTGGGGAACTTGAGTGCANAANAGGANAGTGGAATTCCATGTGTAGCGGTGAAATGCGTATATATATGGAGGAACACCAGTGGCGAAGGCGGCTCTCTGGTCTGTAACTGACGCTGAGGCTCGAAAGCGTGGGGAGCAAACAGGATTAGATACCCTGGTAGTCCACGCCGTAAACGATGAGTGCTAAGTGTTGGAGGGTTTCCGCCCTTCAGTGCTGCAGCTAACGCATTAAGCACTCCGCCTGGGGAGTACGACCGCAAGGTTGAAACTCAAAGGAATTGACGGGGGCCCGCACAAGCGGTGGAGCATGTGGTTTAATTCGAAGCAACGCGAAGAACCTTACCAGGTCTTGACATCCTTTGACCACTCTAGAGATAGAGCTTTCCCTTCGGGGACAAAGTGACAGGTGGTGCATGGTTGTCGTCAGCTCGTGTCGTGAGATGTTGGGTTAAGTCCCGCAACGAGCGCAACCCTTATTGTTAGTTGCCATCATTTAGTTGGGCACTATAGCGAGACTGCCGGTGACAAACCGGAGGAAGGTGGGGATGACGTCAAATCATCATGCCCCTTATGACCTGGGCTACACACGTGCTACAATGGGAAGTACAACGAGTTGCGAAGTCGCGAGGCTAAGCTAATCTCTTAAAGCTTCTACTCAGTTCGGATTGTAGGCTGCAACTCGCCTCACATGAAGCCGGAATCGCTAGTAATCGCGGATCAGCACGCCGCGGTGAATACGTTCCCGGGCCTTGTACACACCGCCCGTCACACCA", hsp.getHseq());
		assertEquals("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||", hsp.getMidline());
	}
}
