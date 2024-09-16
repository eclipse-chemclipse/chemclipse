/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.traces;

public class Trace_HighResMSD_00_Test extends TraceTestCase {

	@Override
	protected void setUp() throws Exception {

		super.setUp();
	}

	public void test1() {

		assertNull(TraceFactory.parseTrace("", TraceHighResMSD.class));
	}

	public void test2() {

		assertNull(TraceFactory.parseTrace("A", TraceHighResMSD.class));
	}

	public void test3() {

		assertNull(TraceFactory.parseTrace("0", TraceHighResMSD.class));
	}

	public void test4() {

		assertNull(TraceFactory.parseTrace("-1", TraceHighResMSD.class));
	}
}