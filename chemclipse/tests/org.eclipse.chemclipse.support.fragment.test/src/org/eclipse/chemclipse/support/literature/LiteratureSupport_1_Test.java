/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.literature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LiteratureSupport_1_Test {

	private String content;

	@Before
	public void setUp() {

		content = getNewlineRIS();
	}

	@Test
	public void test1() {

		assertEquals("https://doi.org/10.1186/1471-2105-11-405", LiteratureSupport.getContainedLink(content));
	}

	@Test
	public void test2a() {

		assertEquals("OpenChrom: a cross-platform open source software for the mass spectrometric analysis of chromatographic data", LiteratureSupport.getTitle(content));
	}

	@Test
	public void test2b() {

		assertEquals("OpenChrom: a cross-platform open source software for the mass spectrometric analysis of chromatographic data", LiteratureSupport.getTitle(getFlattenRIS()));
	}

	@Test
	public void test2c() {

		assertEquals("OpenChrom: a cross-platform open source software for the mass spectrometric analysis of chromatographic data", LiteratureSupport.getTitle(getFormattedOriginalRIS()));
	}

	@Test
	public void test2d() {

		assertEquals("OpenChrom: a cross-platform open source software for the mass spectrometric analysis of chromatographic data", LiteratureSupport.getTitle(getFormattedMappedRIS()));
	}

	@Test
	public void test2e() {

		assertEquals("", LiteratureSupport.getTitle(""));
	}

	@Test
	public void test2f() {

		assertEquals("", LiteratureSupport.getTitle(null));
	}

	@Test
	public void test3a() {

		assertEquals("Reference", LiteratureSupport.getMappedIdentifierRIS("TY"));
	}

	@Test
	public void test3b() {

		assertEquals("Type of Work", LiteratureSupport.getMappedIdentifierRIS("M3"));
	}

	@Test
	public void test3c() {

		assertEquals("Access Date", LiteratureSupport.getMappedIdentifierRIS("Y2"));
	}

	@Test
	public void test3d() {

		assertEquals("Unknown", LiteratureSupport.getMappedIdentifierRIS(""));
	}

	@Test
	public void test3e() {

		assertEquals("Unknown", LiteratureSupport.getMappedIdentifierRIS(null));
	}

	@Test
	public void test4a() {

		assertTrue(LiteratureSupport.isEndOfReferenceRIS("ER"));
	}

	@Test
	public void test4b() {

		assertFalse(LiteratureSupport.isEndOfReferenceRIS("TY"));
	}

	@Test
	public void test4c() {

		assertFalse(LiteratureSupport.isEndOfReferenceRIS(""));
	}

	@Test
	public void test4d() {

		assertFalse(LiteratureSupport.isEndOfReferenceRIS(null));
	}

	@Test
	public void test5a() {

		assertTrue(LiteratureSupport.isFormatRIS(content));
	}

	@Test
	public void test5b() {

		assertFalse(LiteratureSupport.isFormatRIS(getOther()));
	}

	@Test
	public void test6a() {

		assertEquals(getFormattedOriginalRIS(), LiteratureSupport.getFormattedRIS(getNewlineRIS(), false));
	}

	@Test
	public void test6b() {

		assertEquals(getFormattedOriginalRIS(), LiteratureSupport.getFormattedRIS(getFlattenRIS(), false));
	}

	@Test
	public void test6c() {

		assertEquals("", LiteratureSupport.getFormattedRIS("", false));
	}

	@Test
	public void test6d() {

		assertEquals("", LiteratureSupport.getFormattedRIS(null, false));
	}

	@Test
	public void test7a() {

		assertEquals(getFormattedMappedRIS(), LiteratureSupport.getFormattedRIS(getNewlineRIS(), true));
	}

	@Test
	public void test7b() {

		assertEquals(getFormattedMappedRIS(), LiteratureSupport.getFormattedRIS(getFlattenRIS(), true));
	}

	@Test
	public void test7c() {

		assertEquals("", LiteratureSupport.getFormattedRIS("", true));
	}

	@Test
	public void test7d() {

		assertEquals("", LiteratureSupport.getFormattedRIS(null, true));
	}

	@Test
	public void test8a() {

		assertEquals("OpenChrom: a cross-platform open source software", LiteratureSupport.getTitle("TI  - OpenChrom: a cross-platform open source software\n"));
	}

	@Test
	public void test8b() {

		assertEquals("OpenChrom: a cross-platform open source software", LiteratureSupport.getTitle("TI  - OpenChrom: a cross-platform open source software JO  - BMC"));
	}

	@Test
	public void test8c() {

		assertEquals("OpenChrom: a cross-platform open source software", LiteratureSupport.getTitle("TI  - OpenChrom: a cross-platform open source software"));
	}

	@Test
	public void test9a() {

		assertEquals("OpenChrom: a cross-platform open source software", LiteratureSupport.getTitle("T1  - OpenChrom: a cross-platform open source software\n"));
	}

	@Test
	public void test9b() {

		assertEquals("OpenChrom: a cross-platform open source software", LiteratureSupport.getTitle("T1  - OpenChrom: a cross-platform open source software JO  - BMC"));
	}

	@Test
	public void test9c() {

		assertEquals("OpenChrom: a cross-platform open source software", LiteratureSupport.getTitle("T1  - OpenChrom: a cross-platform open source software"));
	}

	@Test
	public void test10a() {

		assertEquals("OpenChrom: a cross-platform open source software", LiteratureSupport.getTitle("Title: OpenChrom: a cross-platform open source software\n"));
	}

	@Test
	public void test10b() {

		assertEquals("OpenChrom: a cross-platform open source software", LiteratureSupport.getTitle("Title: OpenChrom: a cross-platform open source software JO  - BMC"));
	}

	@Test
	public void test10c() {

		assertEquals("OpenChrom: a cross-platform open source software", LiteratureSupport.getTitle("Title: OpenChrom: a cross-platform open source software"));
	}

	private String getNewlineRIS() {

		return "TY  - JOUR\n" + "AU  - Wenig, Philip\n" + "AU  - Odermatt, Juergen\n" + "PY  - 2010\n" + "DA  - 2010/07/30\n" + "TI  - OpenChrom: a cross-platform open source software for the mass spectrometric analysis of chromatographic data\n" + "JO  - BMC Bioinformatics\n" + "SP  - 405\n" + "VL  - 11\n" + "IS  - 1\n" + "AB  - Today, data evaluation has become a bottleneck in chromatographic science. Analytical instruments equipped with automated samplers yield large amounts of measurement data, which needs to be verified and analyzed. Since nearly every GC/MS instrument vendor offers its own data format and software tools, the consequences are problems with data exchange and a lack of comparability between the analytical results. To challenge this situation a number of either commercial or non-profit software applications have been developed. These applications provide functionalities to import and analyze several data formats but have shortcomings in terms of the transparency of the implemented analytical algorithms and/or are restricted to a specific computer platform.\n" + "SN  - 1471-2105\n" + "UR  - https://doi.org/10.1186/1471-2105-11-405\n" + "DO  - 10.1186/1471-2105-11-405\n" + "ID  - Wenig2010\n" + "ER  - ";
	}

	private String getFlattenRIS() {

		return "TY  - JOUR AU  - Wenig, Philip AU  - Odermatt, Juergen PY  - 2010 DA  - 2010/07/30 TI  - OpenChrom: a cross-platform open source software for the mass spectrometric analysis of chromatographic data JO  - BMC Bioinformatics SP  - 405 VL  - 11 IS  - 1 AB  - Today, data evaluation has become a bottleneck in chromatographic science. Analytical instruments equipped with automated samplers yield large amounts of measurement data, which needs to be verified and analyzed. Since nearly every GC/MS instrument vendor offers its own data format and software tools, the consequences are problems with data exchange and a lack of comparability between the analytical results. To challenge this situation a number of either commercial or non-profit software applications have been developed. These applications provide functionalities to import and analyze several data formats but have shortcomings in terms of the transparency of the implemented analytical algorithms and/or are restricted to a specific computer platform. SN  - 1471-2105 UR  - https://doi.org/10.1186/1471-2105-11-405 DO  - 10.1186/1471-2105-11-405 ID  - Wenig2010 ER  - ";
	}

	private String getOther() {

		return "OpenChrom: a cross-platform open source software for the mass spectrometric analysis of chromatographic data";
	}

	private String getFormattedOriginalRIS() {

		return "TY  - JOUR\n" //
				+ "AU  - Wenig, Philip\n" //
				+ "AU  - Odermatt, Juergen\n" //
				+ "PY  - 2010\n" //
				+ "DA  - 2010/07/30\n" //
				+ "TI  - OpenChrom: a cross-platform open source software for the mass spectrometric analysis of chromatographic data\n" //
				+ "JO  - BMC Bioinformatics\n" //
				+ "SP  - 405\n" //
				+ "VL  - 11\n" //
				+ "IS  - 1\n" //
				+ "AB  - Today, data evaluation has become a bottleneck in chromatographic science. Analytical instruments equipped with automated samplers yield large amounts of measurement data, which needs to be verified and analyzed. Since nearly every GC/MS instrument vendor offers its own data format and software tools, the consequences are problems with data exchange and a lack of comparability between the analytical results. To challenge this situation a number of either commercial or non-profit software applications have been developed. These applications provide functionalities to import and analyze several data formats but have shortcomings in terms of the transparency of the implemented analytical algorithms and/or are restricted to a specific computer platform.\n" //
				+ "SN  - 1471-2105\n" //
				+ "UR  - https://doi.org/10.1186/1471-2105-11-405\n" //
				+ "DO  - 10.1186/1471-2105-11-405\n" //
				+ "ID  - Wenig2010\n" //
				+ "ER  -\n"; //
	}

	private String getFormattedMappedRIS() {

		return "Reference: JOUR\n" //
				+ "Author: Wenig, Philip\n" //
				+ "Author: Odermatt, Juergen\n" //
				+ "Publication Year: 2010\n" //
				+ "Date: 2010/07/30\n" //
				+ "Title: OpenChrom: a cross-platform open source software for the mass spectrometric analysis of chromatographic data\n" //
				+ "Journal/Periodical: BMC Bioinformatics\n" //
				+ "Start Page: 405\n" //
				+ "Volume Number: 11\n" //
				+ "Issue Number: 1\n" //
				+ "Abstract: Today, data evaluation has become a bottleneck in chromatographic science. Analytical instruments equipped with automated samplers yield large amounts of measurement data, which needs to be verified and analyzed. Since nearly every GC/MS instrument vendor offers its own data format and software tools, the consequences are problems with data exchange and a lack of comparability between the analytical results. To challenge this situation a number of either commercial or non-profit software applications have been developed. These applications provide functionalities to import and analyze several data formats but have shortcomings in terms of the transparency of the implemented analytical algorithms and/or are restricted to a specific computer platform.\n" //
				+ "ISBN/ISSN: 1471-2105\n" //
				+ "URL: https://doi.org/10.1186/1471-2105-11-405\n" //
				+ "DOI: 10.1186/1471-2105-11-405\n" //
				+ "Reference ID: Wenig2010\n" //
				+ "ER  -\n"; //
	}
}