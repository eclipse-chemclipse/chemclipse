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
import org.eclipse.chemclipse.dsd.model.core.Nucleobase;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.ux.extension.dsd.ui.support.NucleotideSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class ExtendedNucleotideSequenceUI extends Composite implements IExtendedPartUI {

	private AtomicReference<StyledText> textControl = new AtomicReference<>();

	public ExtendedNucleotideSequenceUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	private void createControl() {

		setLayout(new FillLayout());
		createNucleotideText(this);
	}

	private void createNucleotideText(Composite parent) {

		StyledText text = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP | SWT.READ_ONLY);
		textControl.set(text);
	}

	private void setText(String sequence) {

		StyledText text = textControl.get();
		text.setText(sequence);

		for(int index = 0; index < sequence.length(); index++) {
			Nucleobase nucleobase = Nucleobase.of(sequence.charAt(index));
			Color color = nucleobase != null ? NucleotideSupport.getColor(nucleobase) : new Color(255, 0, 255);
			text.setStyleRange(new StyleRange(index, 1, color, null));
		}
	}

	public void updateInput(IChromatogramSelection chromatogramSelection) {

		if(textControl.get() != null && chromatogramSelection != null) {
			if(chromatogramSelection.getChromatogram() instanceof IChromatogramDSD chromatogram) {
				setText(chromatogram.getNucleotideSequence().toString());
			}
		}
	}
}