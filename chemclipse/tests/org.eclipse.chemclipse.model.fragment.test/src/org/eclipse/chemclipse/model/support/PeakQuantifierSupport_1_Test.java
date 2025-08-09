/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.quantitation.IInternalStandard;
import org.junit.Before;
import org.junit.Test;

public class PeakQuantifierSupport_1_Test extends PeakQuantifierSupportTestCase {

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
	}

	@Test
	public void test1() {

		IPeak peak = getPeak();
		assertEquals("", PeakQuantifierSupport.getInternalStandardConcentrations(peak));
	}

	@Test
	public void test2() {

		IPeak peak = getPeak();
		assertEquals("", PeakQuantifierSupport.getPeakConcentrations(peak));
	}

	@Test
	public void test3() {

		IInternalStandard internalStandard = PeakQuantifierSupport.getInternalStandard(null, null);
		assertNull(internalStandard);
	}

	@Test
	public void test4() {

		IInternalStandard internalStandard = PeakQuantifierSupport.getInternalStandard("", "");
		assertNull(internalStandard);
	}

	@Test
	public void test5() {

		IInternalStandard internalStandard = PeakQuantifierSupport.getInternalStandard("Test", "");
		assertNull(internalStandard);
	}
}