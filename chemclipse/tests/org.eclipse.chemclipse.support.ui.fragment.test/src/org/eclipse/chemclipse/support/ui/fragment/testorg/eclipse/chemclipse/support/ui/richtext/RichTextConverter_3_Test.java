/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
public class RichTextConverter_3_Test {

	@Test
	public void test1() {

		String input = "{\\rtf1\\ansi\\ansicpg1252\\deff0{\\fonttbl{\\f0\\fnil\\fcharset0 Microsoft Sans Serif;}}\n\\viewkind4\\uc1\\pard\\lang1031\\f0\\fs17 Gehalt polare S\\'e4ule - 0,993%\\par\n\\b\\fs17 AB 10.5.2006:\\b0\\fs17\\par\n-> es gibt keinen Grenzwert.\\par\n}";
		String output = "<html>\n" //
				+ "  <head>\n" //
				+ "    <style>\n" //
				+ "      <!--\n" //
				+ "      -->\n" //
				+ "    </style>\n" //
				+ "  </head>\n" //
				+ "  <body>\n" //
				+ "    <p class=default>\n" //
				+ "      <span style=\"color: #000000; font-size: 8pt; font-family: Microsoft Sans Serif\">\n" //
				+ "        Gehalt polare Säule - 0,993%\n" //
				+ "      </span>\n" //
				+ "    </p>\n" //
				+ "    <p class=default>\n" //
				+ "      <span style=\"color: #000000; font-size: 8pt; font-family: Microsoft Sans Serif\">\n" //
				+ "        <b>AB 10.5.2006:</b>\n" //
				+ "      </span>\n" //
				+ "      <span style=\"color: #000000; font-size: 8pt; font-family: Microsoft Sans Serif\">\n" //
				+ "        \n" //
				+ "      </span>\n" //
				+ "    </p>\n" //
				+ "    <p class=default>\n" //
				+ "      <span style=\"color: #000000; font-size: 8pt; font-family: Microsoft Sans Serif\">\n" //
				+ "        -> es gibt keinen Grenzwert.\n" //
				+ "      </span>\n" //
				+ "    </p>\n" //
				+ "  </body>\n" //
				+ "</html>\n"; //
		assertEquals(output, RichTextConverter.convertRtfToHtml(input));
	}
}