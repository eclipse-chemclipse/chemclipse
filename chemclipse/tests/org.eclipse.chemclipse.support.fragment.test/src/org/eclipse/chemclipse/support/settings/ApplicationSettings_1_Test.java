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
package org.eclipse.chemclipse.support.settings;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ApplicationSettings_1_Test {

	@Test
	public void test1() {

		assertNotNull(ApplicationSettings.getSystemTmpDirectory());
	}

	@Test
	public void test2() {

		assertTrue(ApplicationSettings.getSystemTmpDirectory().exists());
	}
}
