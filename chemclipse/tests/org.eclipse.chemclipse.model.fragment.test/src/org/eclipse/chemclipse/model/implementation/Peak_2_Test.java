/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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

public class Peak_2_Test {

	private IPeak peak = new Peak();

	@Test
	public void test1() {

		assertEquals(false, peak.isMarkedAsDeleted());
	}

	@Test
	public void test2() {

		peak.setMarkedAsDeleted(true);
		assertEquals(true, peak.isMarkedAsDeleted());
	}

	@Test
	public void test3() {

		peak.setMarkedAsDeleted(false);
		assertEquals(false, peak.isMarkedAsDeleted());
	}
}
