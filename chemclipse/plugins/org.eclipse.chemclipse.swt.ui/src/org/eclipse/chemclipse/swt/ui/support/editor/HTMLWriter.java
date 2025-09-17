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
 * Aleksandar Kurtakov - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.swt.ui.support.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

public class HTMLWriter {

	public static String toHTML(StyledText st) {

		String text = st.getText();
		StyleRange[] ranges = st.getStyleRanges();

		Arrays.sort(ranges, Comparator.comparingInt(r -> r.start));

		TreeSet<Integer> cuts = new TreeSet<>();
		cuts.add(0);
		cuts.add(text.length());
		for(StyleRange r : ranges) {
			cuts.add(r.start);
			cuts.add(r.start + r.length);
		}

		StringBuilder out = new StringBuilder();
		out.append("<div>");

		Integer prev = null;
		for(Integer pos : cuts) {
			if(prev == null) {
				prev = pos;
				continue;
			}
			int segStart = prev;
			int segEnd = pos;
			if(segStart >= segEnd) {
				prev = pos;
				continue;
			}

			String segText = escapeHtml(text.substring(segStart, segEnd));

			List<StyleRange> covering = new ArrayList<>();
			for(StyleRange r : ranges) {
				if(r.start <= segStart && (r.start + r.length) >= segEnd) {
					covering.add(r);
				}
			}

			int fontStyle = SWT.NONE;
			boolean underline = false;
			boolean strike = false;

			for(StyleRange r : covering) {
				fontStyle |= r.fontStyle;
				if(r.underline)
					underline = true;
				if(r.strikeout)
					strike = true;
			}

			List<String> openTags = new ArrayList<>();
			List<String> closeTags = new ArrayList<>();

			if((fontStyle & SWT.BOLD) != 0) {
				openTags.add("<b>");
				closeTags.add(0, "</b>");
			}
			if((fontStyle & SWT.ITALIC) != 0) {
				openTags.add("<i>");
				closeTags.add(0, "</i>");
			}
			if(underline) {
				openTags.add("<u>");
				closeTags.add(0, "</u>");
			}
			if(strike) {
				openTags.add("<s>");
				closeTags.add(0, "</s>");
			}

			for(String t : openTags)
				out.append(t);
			out.append(segText.replace("\n", "<br/>"));
			
			for(String t : closeTags)
				out.append(t);

			prev = pos;
		}

		out.append("</div>");
		return out.toString();
	}

	private static String escapeHtml(String s) {

		if(s == null)
			return "";
		StringBuilder sb = new StringBuilder(Math.max(16, s.length()));
		for(char c : s.toCharArray()) {
			switch(c) {
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '&':
					sb.append("&amp;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\r':
					break; // ignore
				default:
					sb.append(c);
			}
		}
		return sb.toString();
	}

}
