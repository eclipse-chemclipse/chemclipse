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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.model.exceptions.AnalysisSupportException;
import org.junit.Test;

public class AnalysisSupport_2_Test {

	private IAnalysisSupport support;

	@Test
	public void testConstructor_1() {

		assertThrows(AnalysisSupportException.class, () -> {
			support = new AnalysisSupport(0, 0);
		});
	}

	@Test
	public void testConstructor_2() {

		assertThrows(AnalysisSupportException.class, () -> {
			support = new AnalysisSupport(-1, -1);
		});
	}

	@Test
	public void testConstructor_3() {

		support = new AnalysisSupport(3, 3);
	}

	public void testConstructor_4() {

		support = new AnalysisSupport(4, 3);
		assertEquals("NumberOfAnalysisSegments", 2, support.getNumberOfAnalysisSegments());
	}
}
