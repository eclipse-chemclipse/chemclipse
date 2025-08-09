/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.core.IPeak;
import org.junit.Test;

public class Peak_1_Test {

	private IPeak peak = new Peak();

	@Test
	public void test1() {

		assertEquals(true, peak.isActiveForAnalysis());
	}

	@Test
	public void test2() {

		peak.setActiveForAnalysis(false);
		assertEquals(false, peak.isActiveForAnalysis());
	}

	@Test
	public void test3() {

		peak.setActiveForAnalysis(true);
		assertEquals(true, peak.isActiveForAnalysis());
	}
}
