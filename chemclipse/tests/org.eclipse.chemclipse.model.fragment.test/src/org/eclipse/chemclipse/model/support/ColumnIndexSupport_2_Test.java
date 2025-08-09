/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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

import org.junit.Test;

public class ColumnIndexSupport_2_Test {

	@Test
	public void test1() {

		assertEquals("db 5", ColumnIndexSupport.adjustValue("DB 5", false, false));
	}

	@Test
	public void test2() {

		assertEquals("DB 5", ColumnIndexSupport.adjustValue("DB 5", true, false));
	}

	@Test
	public void test3() {

		assertEquals("db5", ColumnIndexSupport.adjustValue("DB 5", false, true));
	}

	@Test
	public void test4() {

		assertEquals("DB5", ColumnIndexSupport.adjustValue("DB 5", true, true));
	}
}