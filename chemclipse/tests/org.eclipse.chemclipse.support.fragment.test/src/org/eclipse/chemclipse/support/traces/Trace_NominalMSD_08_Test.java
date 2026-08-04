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
public class Trace_NominalMSD_08_Test {

	@Test
	public void test01() {

		TraceNominalMSD trace = new TraceNominalMSD();
		assertEquals(0, trace.getMZ());
	}

	@Test
	public void test02() {

		TraceNominalMSD trace = new TraceNominalMSD(0);
		assertEquals(0, trace.getMZ());
	}

	@Test
	public void test03() {

		TraceNominalMSD trace = new TraceNominalMSD(18);
		assertEquals(18, trace.getMZ());
	}
}