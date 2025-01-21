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
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.junit.Test;

public class RetentionIndexTypeSupport_1_Test {

	@Test
	public void test1() {

		assertEquals("POLAR", RetentionIndexTypeSupport.getBackwardCompatibleName(null));
	}

	@Test
	public void test2() {

		assertEquals("POLAR", RetentionIndexTypeSupport.getBackwardCompatibleName(SeparationColumnType.DEFAULT));
	}

	@Test
	public void test3() {

		assertEquals("POLAR", RetentionIndexTypeSupport.getBackwardCompatibleName(SeparationColumnType.POLAR));
	}

	@Test
	public void test4() {

		assertEquals("SEMIPOLAR", RetentionIndexTypeSupport.getBackwardCompatibleName(SeparationColumnType.SEMI_POLAR));
	}

	@Test
	public void test5() {

		assertEquals("APOLAR", RetentionIndexTypeSupport.getBackwardCompatibleName(SeparationColumnType.NON_POLAR));
	}
}