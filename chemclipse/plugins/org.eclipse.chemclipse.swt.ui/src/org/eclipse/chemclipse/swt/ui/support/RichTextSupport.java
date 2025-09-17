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
package org.eclipse.chemclipse.swt.ui.support;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.support.settings.OperatingSystemUtils;
import org.eclipse.chemclipse.swt.ui.support.editor.SWTEditor;
import org.eclipse.nebula.widgets.richtext.RichTextEditor;
import org.eclipse.nebula.widgets.richtext.RichTextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;

public class RichTextSupport {

	private static final Logger logger = Logger.getLogger(RichTextSupport.class);
	private static Boolean BROWSER_AVAILABLE = null;

	/**
	 * The return is either a RichTextEditor or Text.
	 * 
	 * @param parent
	 * @param style
	 * @return {@link Control}
	 */
	public static Control createEditor(Composite parent, int style) {

		if(isUseRichTextEditor()) {
			try {
				return new RichTextEditor(parent, style);
			} catch(Exception e) {
				logger.warn("Failed to create the Rich Text Editor - use is deactivated now.");
				PreferenceSupplier.setUseRichTextEditor(false);
				return createTextEditor(parent, style);
			}
		} else {
			return createTextEditor(parent, style);
		}
	}

	/**
	 * The return is either a RichTextViewer or Text.
	 * 
	 * @param parent
	 * @param style
	 * @return {@link Scrollable}
	 */
	public static Scrollable createViewer(Composite parent, int style) {

		if(isUseRichTextEditor()) {
			try {
				return new RichTextViewer(parent, style);
			} catch(Exception e) {
				logger.warn("Failed to create the Rich Text Viewer - use is deactivated now.");
				PreferenceSupplier.setUseRichTextEditor(false);
				return createTextEditor(parent, style);
			}
		} else {
			return createTextEditor(parent, style);
		}
	}

	/**
	 * Returns text if the editor is a
	 * RichTextEditor
	 * or Text
	 * 
	 * @param editor
	 * @return {@link String}
	 */
	public static String getEditorText(Control editor) {

		String text = "";
		if(editor instanceof RichTextEditor richTextEditor) {
			text = richTextEditor.getText();
		} else if(editor instanceof SWTEditor plainTextEditor) {
			text = plainTextEditor.getText();
		}

		return text;
	}

	/**
	 * Sets the text if the editor is a
	 * RichTextEditor
	 * or RichTextViewer
	 * or Text
	 * 
	 * @param editor
	 * @param text
	 */
	public static void setEditorText(Control editor, String text) {

		if(editor instanceof RichTextEditor richTextEditor) {
			richTextEditor.setText(text);
		} else if(editor instanceof RichTextViewer richTextViewer) {
			richTextViewer.setText(text);
		} else if(editor instanceof SWTEditor plainTextEditor) {
			plainTextEditor.setText(text);
		}
	}

	public static boolean isUseRichTextEditor() {

		boolean useRichTextEditor = PreferenceSupplier.isUseRichTextEditor();
		if(useRichTextEditor) {
			if(PreferenceSupplier.isRunBrowserCheck()) {
				if(BROWSER_AVAILABLE == null) {
					/*
					 * Check if WebKit is available - otherwise disable the rich text option
					 * org.eclipse.swt.SWTError: No more handles because there is no underlying browser available.
					 * Please ensure that WebKit with its GTK 3.x/4.x bindings is installed.
					 */
					BROWSER_AVAILABLE = false;
					Shell shell = new Shell(Display.getDefault());
					try {
						Browser browser = new Browser(shell, SWT.NONE);
						String browserType = browser.getBrowserType();
						if(OperatingSystemUtils.isWindows()) {
							BROWSER_AVAILABLE = browserType.contains("ie") || browserType.contains("edge");
						} else {
							BROWSER_AVAILABLE = browserType.contains("webkit");
						}
						browser.dispose();
					} catch(SWTError e) {
						logger.warn(e);
					} finally {
						shell.dispose();
					}
				}
				useRichTextEditor = BROWSER_AVAILABLE;
			}
		}
		return useRichTextEditor;
	}

	private static SWTEditor createTextEditor(Composite parent, int style) {

		return new SWTEditor(parent, style);
	}
}