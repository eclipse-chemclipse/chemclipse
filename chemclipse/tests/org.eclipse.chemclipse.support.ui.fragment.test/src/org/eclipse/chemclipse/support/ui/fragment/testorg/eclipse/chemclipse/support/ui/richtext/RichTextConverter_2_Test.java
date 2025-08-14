/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.support.ui.fragment.testorg.eclipse.chemclipse.support.ui.richtext;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.support.ui.richtext.RichTextConverter;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RichTextConverter_2_Test {

	@Test
	public void test1() {

		String input = "<p><span style=\"font-size: 8pt; font-family: Microsoft Sans Serif\">Test</span></p>";
		String output = "<p><span style=\"font-size: 11px; font-family: Microsoft Sans Serif\">Test</span></p>";
		assertEquals(output, RichTextConverter.convertRtfToHtml(input, false));
	}
}