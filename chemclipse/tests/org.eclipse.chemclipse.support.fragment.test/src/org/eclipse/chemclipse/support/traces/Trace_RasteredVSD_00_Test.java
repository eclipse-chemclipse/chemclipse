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

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Trace_RasteredVSD_00_Test {

	@Test
	public void test1() {

		assertNull(TraceFactory.parseTrace("", TraceRasteredVSD.class));
	}

	@Test
	public void test2() {

		assertNull(TraceFactory.parseTrace("A", TraceRasteredVSD.class));
	}

	@Test
	public void test3() {

		assertNull(TraceFactory.parseTrace("0", TraceRasteredVSD.class));
	}

	@Test
	public void test4() {

		assertNull(TraceFactory.parseTrace("-1", TraceRasteredVSD.class));
	}
}