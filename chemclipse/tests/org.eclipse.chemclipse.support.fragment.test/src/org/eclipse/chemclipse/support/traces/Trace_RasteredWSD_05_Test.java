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
package org.eclipse.chemclipse.support.traces;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class Trace_RasteredWSD_05_Test {

	@Test
	public void test01() {

		TraceRasteredWSD trace = new TraceRasteredWSD();
		assertEquals(0, trace.getWavelength());
	}

	@Test
	public void test02() {

		TraceRasteredWSD trace = new TraceRasteredWSD(0);
		assertEquals(0, trace.getWavelength());
	}

	@Test
	public void test03() {

		TraceRasteredWSD trace = new TraceRasteredWSD(202);
		assertEquals(202, trace.getWavelength());
	}
}