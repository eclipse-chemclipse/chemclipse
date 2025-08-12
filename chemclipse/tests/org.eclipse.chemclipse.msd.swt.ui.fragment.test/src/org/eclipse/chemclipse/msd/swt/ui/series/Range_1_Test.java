/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.swt.ui.series;

import static org.junit.Assert.assertEquals;

import org.eclipse.swtchart.Range;
import org.junit.Test;

public class Range_1_Test {

	@Test
	public void testRange_1() {

		Range range = new Range(0, 0);
		assertEquals("lower", 0.0d, range.lower, 0);
	}

	@Test
	public void testRange_2() {

		Range range = new Range(0, 0);
		assertEquals("upper", 0.0d, range.upper, 0);
	}
}
