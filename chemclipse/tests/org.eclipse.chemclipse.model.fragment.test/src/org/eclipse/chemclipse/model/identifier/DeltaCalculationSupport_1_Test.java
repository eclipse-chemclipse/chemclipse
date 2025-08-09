/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.identifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DeltaCalculationSupport_1_Test {

	@Test
	public void test1() {

		double unknown = 2000.0d;
		double reference = 2000.0d;
		double delta = 0.0d;
		assertTrue(DeltaCalculationSupport.useTarget(unknown, reference, delta));
	}

	@Test
	public void test2() {

		double unknown = 2000.0d;
		double reference = 1990.0d;
		double delta = 10.0d;
		assertTrue(DeltaCalculationSupport.useTarget(unknown, reference, delta));
	}

	@Test
	public void test3() {

		double unknown = 2000.0d;
		double reference = 2010.0d;
		double delta = 10.0d;
		assertTrue(DeltaCalculationSupport.useTarget(unknown, reference, delta));
	}

	@Test
	public void test4() {

		double unknown = 2000.0d;
		double reference = 1989.0d;
		double delta = 10.0d;
		assertFalse(DeltaCalculationSupport.useTarget(unknown, reference, delta));
	}

	@Test
	public void test5() {

		double unknown = 2000.0d;
		double reference = 2011.0d;
		double delta = 10.0d;
		assertFalse(DeltaCalculationSupport.useTarget(unknown, reference, delta));
	}
}