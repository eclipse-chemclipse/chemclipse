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
package org.eclipse.chemclipse.model.columns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SeparationColumnFactory_2_Test {

	@Test
	public void test1() {

		assertEquals(4, SeparationColumnFactory.getSeparationColumns().size());
	}
}