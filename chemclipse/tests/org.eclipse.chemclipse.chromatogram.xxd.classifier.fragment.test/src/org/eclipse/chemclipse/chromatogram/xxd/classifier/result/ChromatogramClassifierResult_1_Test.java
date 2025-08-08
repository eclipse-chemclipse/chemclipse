/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.classifier.result;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChromatogramClassifierResult_1_Test {

	@Test
	public void testGetResultStatus_1() {

		ResultStatus status = ResultStatus.EXCEPTION;
		TestChromatogramClassifierResult chromatogramClassifierResult = new TestChromatogramClassifierResult(status, "");
		assertEquals("ResultStatus", ResultStatus.EXCEPTION, chromatogramClassifierResult.getResultStatus());
	}

	@Test
	public void testGetResultStatus_2() {

		ResultStatus status = ResultStatus.OK;
		TestChromatogramClassifierResult chromatogramClassifierResult = new TestChromatogramClassifierResult(status, "");
		assertEquals("ResultStatus", ResultStatus.OK, chromatogramClassifierResult.getResultStatus());
	}

	@Test
	public void testGetResultStatus_3() {

		ResultStatus status = ResultStatus.UNDEFINED;
		TestChromatogramClassifierResult chromatogramClassifierResult = new TestChromatogramClassifierResult(status, "");
		assertEquals("ResultStatus", ResultStatus.UNDEFINED, chromatogramClassifierResult.getResultStatus());
	}

	@Test
	public void testGetDescription_3() {

		ResultStatus status = ResultStatus.UNDEFINED;
		TestChromatogramClassifierResult chromatogramClassifierResult = new TestChromatogramClassifierResult(status, "My test description.");
		assertEquals("Description", "My test description.", chromatogramClassifierResult.getDescription());
	}

	@Test
	public void testGetDescription_4() {

		ResultStatus status = ResultStatus.UNDEFINED;
		TestChromatogramClassifierResult chromatogramClassifierResult = new TestChromatogramClassifierResult(status, "");
		assertEquals("Description", "", chromatogramClassifierResult.getDescription());
	}
}
