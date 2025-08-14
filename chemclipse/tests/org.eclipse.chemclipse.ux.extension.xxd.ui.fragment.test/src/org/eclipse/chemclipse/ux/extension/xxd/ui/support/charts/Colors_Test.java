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
package org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts;

import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.swt.graphics.Color;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("SWT is not available in headless builds.")
public class Colors_Test {

	@Test
	public void test1() {

		/*
		 * Use this test to check the colors for the preference store.
		 */
		print("DARK_GRAY", Colors.DARK_GRAY);
		print("DARK_RED", Colors.DARK_RED);
		print("GRAY", Colors.GRAY);
		print("RED", Colors.RED);
		assertTrue(true);
	}

	private void print(String name, Color color) {

		System.out.println(name + " -> " + color.toString());
	}
}