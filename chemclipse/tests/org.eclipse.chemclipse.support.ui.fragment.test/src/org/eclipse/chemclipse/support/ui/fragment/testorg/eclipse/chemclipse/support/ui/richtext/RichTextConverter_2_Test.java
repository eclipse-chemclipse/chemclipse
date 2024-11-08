/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.ui.fragment.testorg.eclipse.chemclipse.support.ui.richtext;

import org.eclipse.chemclipse.support.ui.richtext.RichTextConverter;
import org.junit.Ignore;

import junit.framework.TestCase;

@Ignore
public class RichTextConverter_2_Test extends TestCase {

	public void test1() {

		String input = "<p><span style=\"font-size: 8pt; font-family: Microsoft Sans Serif\">Test</span></p>";
		String output = "<p><span style=\"font-size: 11px; font-family: Microsoft Sans Serif\">Test</span></p>";
		assertEquals(output, RichTextConverter.convertRtfToHtml(input, false));
	}
}