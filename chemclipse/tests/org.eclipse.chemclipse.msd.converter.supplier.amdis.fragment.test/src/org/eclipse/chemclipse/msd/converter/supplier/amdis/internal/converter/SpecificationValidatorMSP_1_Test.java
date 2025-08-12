/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.amdis.internal.converter;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.junit.Before;
import org.junit.Test;

public class SpecificationValidatorMSP_1_Test {

	private File file;
	private String spec;

	@Before
	public void setUp() throws Exception {

		spec = TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_SPEC_MSP);
	}

	@Test
	public void testValidateAgilentSpecification_1() {

		file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_MSP_1));
		file = SpecificationValidatorMSP.validateSpecification(file);
		assertEquals("File", spec, file.getAbsolutePath());
	}

	@Test
	public void testValidateAgilentSpecification_3() {

		file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_MSP_2));
		file = SpecificationValidatorMSP.validateSpecification(file);
		assertEquals("File", spec, file.getAbsolutePath());
	}

	@Test
	public void testValidateAgilentSpecification_4() {

		file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_MSP_3));
		file = SpecificationValidatorMSP.validateSpecification(file);
		assertEquals("File", spec, file.getAbsolutePath());
	}
}
