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

import static org.eclipse.swt.SWT.BOLD;
import static org.eclipse.swt.SWT.ITALIC;
import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class SWTEditor extends Composite {

	Display display;
	Composite parent;
	StyledText styledText;
	ToolItem boldControl, italicControl;

	boolean insert = true;
	boolean readOnly = false;
	StyleRange[] selectedRanges;
	int newCharCount, start;
	int styleState;
	String link;

	// Resources
	Image iBold, iItalic, iUnderline, iStrikeout;
	Image iSpacing, iIndent, iBulletList, iNumberedList;
	Font font, textFont;

	static final int MARGIN = 5;
	static final int FONT_STYLE = BOLD | ITALIC;
	static final int STRIKEOUT = 1 << 3;
	static final int FONT = 1 << 6;
	static final int UNDERLINE = 1 << 9;

	static String getResourceString(String key) {

		return key;
	}

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		SWTEditor editor = new SWTEditor(shell, SWT.READ_ONLY);
		editor.setText("<b>test</b>");
		shell.setSize(1000, 700);
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch())
				display.sleep();
		}
		editor.releaseResources();
		display.dispose();

	}

	public void setText(String text) {

		HTMLReader parser = new HTMLReader(text);
		parser.applyTo(styledText);
	}

	public String getText() {

		return HTMLWriter.toHTML(styledText);
	}

	public SWTEditor(Composite parent, int style) {

		super(parent, style);
		setLayout(new GridLayout(1, true));
		this.parent = parent;
		initResources();
		if ((style & SWT.READ_ONLY) != 0 ) {
			readOnly= true;
		}
		if (readOnly) {
			styledText = new StyledText(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL| SWT.READ_ONLY);
		} else {
			createToolBar();
			styledText = new StyledText(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		}
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		styledText.setLayoutData(gridData);
		installListeners();
		updateToolBar();
	}

	void addControl(Control control) {

		int offset = styledText.getCaretOffset();
		styledText.replaceTextRange(offset, 0, "\uFFFC"); //$NON-NLS-1$
		StyleRange style = new StyleRange();
		Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int ascent = 2 * size.y / 3;
		int descent = size.y - ascent;
		style.metrics = new GlyphMetrics(ascent + MARGIN, descent + MARGIN, size.x + 2 * MARGIN);
		style.data = control;
		int[] ranges = {offset, 1};
		StyleRange[] styles = {style};
		styledText.setStyleRanges(0, 0, ranges, styles);
		control.setSize(size);
	}

	void createToolBar() {

		ToolBar styleToolBar = new ToolBar(this, SWT.FLAT);
		boldControl = new ToolItem(styleToolBar, SWT.CHECK);
		boldControl.setImage(iBold);
		boldControl.setToolTipText(getResourceString("Bold")); //$NON-NLS-1$
		boldControl.addSelectionListener(widgetSelectedAdapter(event -> setStyle(BOLD)));

		italicControl = new ToolItem(styleToolBar, SWT.CHECK);
		italicControl.setImage(iItalic);
		italicControl.setToolTipText(getResourceString("Italic")); //$NON-NLS-1$
		italicControl.addSelectionListener(widgetSelectedAdapter(event -> setStyle(ITALIC)));

		final ToolItem underlineControl = new ToolItem(styleToolBar, SWT.CHECK);
		underlineControl.setImage(iUnderline);
		underlineControl.setToolTipText(getResourceString("Underline")); //$NON-NLS-1$
		underlineControl.addSelectionListener(widgetSelectedAdapter(event -> {
			setStyle(UNDERLINE);
		}));

		ToolItem strikeoutControl = new ToolItem(styleToolBar, SWT.CHECK);
		strikeoutControl.setImage(iStrikeout);
		strikeoutControl.setToolTipText(getResourceString("Strikeout")); //$NON-NLS-1$
		strikeoutControl.addSelectionListener(widgetSelectedAdapter(event -> {
			setStyle(STRIKEOUT);
		}));

	}

	void disposeRanges(StyleRange[] ranges) {

		StyleRange[] allRanges = styledText.getStyleRanges(0, styledText.getCharCount(), false);
		for(StyleRange rangeToDispose : ranges) {
			boolean disposeFont = true;
			for(StyleRange range : allRanges) {
				if(disposeFont && rangeToDispose.font == range.font) {
					disposeFont = false;
					break;
				}
			}
			if(disposeFont && rangeToDispose.font != textFont && rangeToDispose.font != null)
				rangeToDispose.font.dispose();

			Object data = rangeToDispose.data;
			if(data != null) {
				if(data instanceof Image)
					((Image)data).dispose();
				if(data instanceof Control)
					((Control)data).dispose();
			}
		}
	}

	void disposeResource(Font font) {

		if(font == null)
			return;
		StyleRange[] styles = styledText.getStyleRanges(0, styledText.getCharCount(), false);
		int index = 0;
		while(index < styles.length) {
			if(styles[index].font == font)
				break;
			index++;
		}
		if(index == styles.length)
			font.dispose();
	}

	void handleKeyDown(Event event) {

		if(event.keyCode == SWT.INSERT) {
			insert = !insert;
		}
	}

	void handleModify(ModifyEvent event) {

		if(newCharCount > 0 && start >= 0) {
			StyleRange style = new StyleRange();
			if(textFont != null && !textFont.equals(styledText.getFont())) {
				style.font = textFont;
			} else {
				style.fontStyle = SWT.NONE;
				if(boldControl.getSelection())
					style.fontStyle |= SWT.BOLD;
				if(italicControl.getSelection())
					style.fontStyle |= SWT.ITALIC;
			}
			int underlineStyle = styleState & UNDERLINE;
			if(underlineStyle != 0) {
				style.underline = true;
				style.underlineStyle = SWT.UNDERLINE_SINGLE;
			}
			if((styleState & STRIKEOUT) != 0) {
				style.strikeout = true;
			}
			int[] ranges = {start, newCharCount};
			StyleRange[] styles = {style};
			styledText.setStyleRanges(start, newCharCount, ranges, styles);
		}
		disposeRanges(selectedRanges);
	}

	void handleVerifyText(VerifyEvent event) {

		start = event.start;
		newCharCount = event.text.length();
		int replaceCharCount = event.end - start;

		// mark styles to be disposed
		selectedRanges = styledText.getStyleRanges(start, replaceCharCount, false);
	}

	void initResources() {

		iBold = loadImage(display, "bold"); //$NON-NLS-1$
		iItalic = loadImage(display, "italic"); //$NON-NLS-1$
		iUnderline = loadImage(display, "underline"); //$NON-NLS-1$
		iStrikeout = loadImage(display, "strikeout"); //$NON-NLS-1$
		iBulletList = loadImage(display, "para_bul"); //$NON-NLS-1$
		iNumberedList = loadImage(display, "para_num"); //$NON-NLS-1$
	}

	void installListeners() {

		styledText.addCaretListener(event -> {
			updateToolBar();
		});
		styledText.addListener(SWT.KeyDown, this::handleKeyDown);
		styledText.addVerifyListener(this::handleVerifyText);
		styledText.addModifyListener(this::handleModify);
	}

	Image loadImage(Display display, String fileName) {

		Image image = null;
		try (InputStream sourceStream = getClass().getResourceAsStream(fileName + ".png")) { //$NON-NLS-1$
			ImageData source = new ImageData(sourceStream);
			ImageData mask = source.getTransparencyMask();
			image = new Image(display, source, mask);
		} catch(IOException e) {
			showError(getResourceString("Error"), e.getMessage()); //$NON-NLS-1$
		}
		return image;
	}

	void releaseResources() {

		iBold.dispose();
		iBold = null;
		iItalic.dispose();
		iItalic = null;
		iUnderline.dispose();
		iUnderline = null;
		iStrikeout.dispose();
		iStrikeout = null;
		iBulletList.dispose();
		iBulletList = null;
		iNumberedList.dispose();
		iNumberedList = null;

		if(textFont != null)
			textFont.dispose();
		textFont = null;

		if(font != null)
			font.dispose();
		font = null;
	}

	void setBullet(int type) {

		Point selection = styledText.getSelection();
		int lineStart = styledText.getLineAtOffset(selection.x);
		int lineEnd = styledText.getLineAtOffset(selection.y);
		StyleRange styleRange = new StyleRange();
		Bullet bullet = new Bullet(type, styleRange);
		bullet.text = ".";
		for(int lineIndex = lineStart; lineIndex <= lineEnd; lineIndex++) {
			Bullet oldBullet = styledText.getLineBullet(lineIndex);
			styledText.setLineBullet(lineIndex, 1, oldBullet != null ? null : bullet);
		}
	}

	void setStyle(int style) {

		int[] ranges = styledText.getSelectionRanges();
		int i = 0;
		while(i < ranges.length) {
			setStyle(style, ranges[i++], ranges[i++]);
		}
		updateStyleState(style, UNDERLINE);
		updateStyleState(style, STRIKEOUT);
		styledText.notifyListeners(SWT.Modify, new Event());
	}

	void setStyle(int style, int start, int length) {

		if(length == 0)
			return;

		/* Create new style range */
		StyleRange newRange = new StyleRange();
		if((style & FONT) != 0) {
			newRange.font = textFont;
		}
		if((style & FONT_STYLE) != 0) {
			newRange.fontStyle = style & FONT_STYLE;
		}
		if((style & STRIKEOUT) != 0) {
			newRange.strikeout = true;
		}
		if((style & UNDERLINE) != 0) {
			newRange.underline = true;
			newRange.underlineStyle = SWT.UNDERLINE_SINGLE;
		}

		int newRangeStart = start;
		int newRangeLength = length;
		int[] ranges = styledText.getRanges(start, length);
		StyleRange[] styles = styledText.getStyleRanges(start, length, false);
		int maxCount = ranges.length * 2 + 2;
		int[] newRanges = new int[maxCount];
		StyleRange[] newStyles = new StyleRange[maxCount / 2];
		int count = 0;
		for(int i = 0; i < ranges.length; i += 2) {
			int rangeStart = ranges[i];
			int rangeLength = ranges[i + 1];
			StyleRange range = styles[i / 2];
			if(rangeStart > newRangeStart) {
				newRangeLength = rangeStart - newRangeStart;
				newRanges[count] = newRangeStart;
				newRanges[count + 1] = newRangeLength;
				newStyles[count / 2] = newRange;
				count += 2;
			}
			newRangeStart = rangeStart + rangeLength;
			newRangeLength = (start + length) - newRangeStart;

			/* Create merged style range */
			StyleRange mergedRange = new StyleRange(range);
			// Note: fontStyle is not copied by the constructor
			mergedRange.fontStyle = range.fontStyle;
			if((style & FONT) != 0) {
				mergedRange.font = newRange.font;
			}
			if((style & FONT_STYLE) != 0) {
				mergedRange.fontStyle = range.fontStyle ^ newRange.fontStyle;
			}
			if(mergedRange.font != null && ((style & FONT) != 0 || (style & FONT_STYLE) != 0)) {
				boolean change = false;
				FontData[] fds = mergedRange.font.getFontData();
				for(FontData fd : fds) {
					if(fd.getStyle() != mergedRange.fontStyle) {
						fd.setStyle(mergedRange.fontStyle);
						change = true;
					}
				}
				if(change) {
					mergedRange.font = new Font(display, fds);
				}
			}
			if((style & STRIKEOUT) != 0) {
				mergedRange.strikeout = !range.strikeout;
			}
			if((style & UNDERLINE) != 0) {
				mergedRange.underline = !range.underline || range.underlineStyle != newRange.underlineStyle;
				mergedRange.underlineStyle = mergedRange.underline ? newRange.underlineStyle : SWT.NONE;
				mergedRange.data = mergedRange.underline ? newRange.data : null;
			}

			newRanges[count] = rangeStart;
			newRanges[count + 1] = rangeLength;
			newStyles[count / 2] = mergedRange;
			count += 2;
		}
		if(newRangeLength > 0) {
			newRanges[count] = newRangeStart;
			newRanges[count + 1] = newRangeLength;
			newStyles[count / 2] = newRange;
			count += 2;
		}
		if(0 < count && count < maxCount) {
			int[] tmpRanges = new int[count];
			StyleRange[] tmpStyles = new StyleRange[count / 2];
			System.arraycopy(newRanges, 0, tmpRanges, 0, count);
			System.arraycopy(newStyles, 0, tmpStyles, 0, count / 2);
			newRanges = tmpRanges;
			newStyles = tmpStyles;
		}
		styledText.setStyleRanges(start, length, newRanges, newStyles);
		disposeRanges(styles);
	}

	void showError(String title, String message) {

		MessageBox messageBox = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.CLOSE);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}

	void updateStyleState(int style, int changingStyle) {

		if((style & changingStyle) != 0) {
			if((style & changingStyle) == (styleState & changingStyle)) {
				styleState &= ~changingStyle;
			} else {
				styleState &= ~changingStyle;
				styleState |= style;
			}
		}
	}

	void updateToolBar() {
		if (readOnly) return;
		styleState = 0;
		link = null;
		boolean bold = false, italic = false;
		Font font = null;

		int offset = styledText.getCaretOffset();
		StyleRange range = offset > 0 ? styledText.getStyleRangeAtOffset(offset - 1) : null;
		if(range != null) {
			if(range.font != null) {
				font = range.font;
				FontData[] fds = font.getFontData();
				for(FontData fd : fds) {
					int fontStyle = fd.getStyle();
					if(!bold && (fontStyle & SWT.BOLD) != 0)
						bold = true;
					if(!italic && (fontStyle & SWT.ITALIC) != 0)
						italic = true;
				}
			} else {
				bold = (range.fontStyle & SWT.BOLD) != 0;
				italic = (range.fontStyle & SWT.ITALIC) != 0;
			}
			if(range.underline) {
				styleState |= UNDERLINE;
			}
			if(range.strikeout) {
				styleState |= STRIKEOUT;
			}
		}

		boldControl.setSelection(bold);
		italicControl.setSelection(italic);

		textFont = font;
	}

	public void addModifyListener(ModifyListener listener) {

		styledText.addModifyListener(listener);

	}

}