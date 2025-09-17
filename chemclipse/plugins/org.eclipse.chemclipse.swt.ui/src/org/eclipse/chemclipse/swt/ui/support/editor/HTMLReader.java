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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;

public class HTMLReader {
    private final String html;
    private final Display display;

    private StringBuilder text = new StringBuilder();
    private List<StyleRange> styles = new ArrayList<>();

    // simple style stack
    private static class CurrentStyle {
        int fontStyle = SWT.NONE;
        boolean underline = false;
        FontData fontData = null;
    }
    private Deque<CurrentStyle> stack = new ArrayDeque<>();

    public HTMLReader(String html) {
        this.html = html;
        this.display = Display.getCurrent();
    }

    public void applyTo(StyledText st) {

		parse();
		Listener[] listeners = st.getListeners(SWT.Modify);
		for(Listener listener : listeners) {
			st.removeListener(SWT.Modify, listener);
		}
		st.setText(text.toString());
		for(StyleRange sr : styles) {
			st.setStyleRange(sr);
		}
		for(Listener listener : listeners) {
			st.addListener(SWT.Modify, listener);
		}
    }

    private void parse() {
        stack.clear();
        CurrentStyle base = new CurrentStyle();
        stack.push(base);

        Pattern token = Pattern.compile("(?s)<(/)?([a-zA-Z0-9]+)([^>]*)>|([^<]+)");
        Matcher m = token.matcher(html);
        while (m.find()) {
            if (m.group(4) != null) { // text
                String t = unescape(m.group(4));
                appendText(t);
            } else {
                String tag = m.group(2).toLowerCase();
                boolean closing = m.group(1) != null;
                String attrs = m.group(3);

                if (closing) {
                    if (!stack.isEmpty()) stack.pop();
                } else {
                    CurrentStyle cs = copy(stack.peek());
                    applyTagToStyle(cs, tag, attrs);
                    stack.push(cs);
                    if (tag.equals("br")) {
                    	stack.pop();
                    	text.append("\n");
                    }
                }
            }
        }
    }

    private void appendText(String t) {
        if (t.isEmpty()) return;
        CurrentStyle cs = stack.peek();
        int start = text.length();
        text.append(t);
        int len = t.length();
        StyleRange sr = new StyleRange();
        sr.start = start;
        sr.length = len;
        sr.fontStyle = cs.fontStyle;
        sr.underline = cs.underline;
        if (cs.fontData != null) sr.font = new Font(display, cs.fontData);
        styles.add(sr);
    }

    private void applyTagToStyle(CurrentStyle cs, String tag, String attrs) {
        switch (tag) {
            case "b":
            case "strong":
                cs.fontStyle |= SWT.BOLD; break;
            case "i":
            case "em":
                cs.fontStyle |= SWT.ITALIC; break;
            case "u":
                cs.underline = true; break;
        }
    }

    private CurrentStyle copy(CurrentStyle s) {
        CurrentStyle c = new CurrentStyle();
        c.fontStyle = s.fontStyle;
        c.underline = s.underline;
        c.fontData = s.fontData;
        return c;
    }

    private String unescape(String s) {
        return s.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("&quot;","\"");
    }

}
