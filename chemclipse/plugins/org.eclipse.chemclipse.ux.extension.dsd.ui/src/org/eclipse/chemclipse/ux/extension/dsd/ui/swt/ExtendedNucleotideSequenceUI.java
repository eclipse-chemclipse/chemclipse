/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.dsd.ui.swt;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ExtendedNucleotideSequenceUI extends Composite implements IExtendedPartUI {

	private AtomicReference<Text> textControl = new AtomicReference<>();

	public ExtendedNucleotideSequenceUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	private void createControl() {

		setLayout(new FillLayout());
		createText(this);
	}

	private void createText(Composite parent) {

		Text text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP | SWT.READ_ONLY);
		textControl.set(text);
	}

	public void updateInput(IChromatogramSelection chromatogramSelection) {

		if(textControl.get() != null && chromatogramSelection != null) {
			if(chromatogramSelection.getChromatogram() instanceof IChromatogramDSD chromatogram) {
				textControl.get().setText(chromatogram.getNucleotideSequence().toString());
			}
		}
	}
}