/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OperatingSystemUtils_1_Test {

	@Test
	public void testGetLineDelimiter_1() {

		String delimiter = OperatingSystemUtils.getLineDelimiter();
		String os = System.getProperty("os.name").toLowerCase();
		if(os.startsWith("windows")) {
			assertEquals("\r\n", delimiter);
		} else if(os.startsWith("mac")) {
			assertEquals("\r", delimiter);
		} else if(os.startsWith("unix")) {
			assertEquals("\n", delimiter);
		} else if(os.startsWith("linux")) {
			assertEquals("\n", delimiter);
		} else {
			assertEquals("\r\n", delimiter);
		}
	}
}
