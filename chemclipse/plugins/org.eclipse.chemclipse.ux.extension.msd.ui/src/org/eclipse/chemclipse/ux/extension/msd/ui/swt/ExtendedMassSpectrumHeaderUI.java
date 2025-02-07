/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ExtendedMassSpectrumHeaderUI extends Composite {

	private Text text;

	public ExtendedMassSpectrumHeaderUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void updateMassSpectrum(IRegularMassSpectrum massSpectrum) {

		StringBuilder builder = new StringBuilder();
		if(massSpectrum != null) {
			addHeaderLine(builder, "Data", massSpectrum.getMassSpectrumType().label());
			addHeaderLine(builder, "Technique", "MS" + massSpectrum.getMassSpectrometer());
			addHeaderLine(builder, "Ions", Integer.toString(massSpectrum.getNumberOfIons()));
			if(massSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
				addHeaderLine(builder, "Name", standaloneMassSpectrum.getName());
				addHeaderLine(builder, "File", standaloneMassSpectrum.getFile().getName());
				addHeaderLine(builder, "Sample", standaloneMassSpectrum.getSampleName());
				addHeaderLine(builder, "Instrument", standaloneMassSpectrum.getInstrument());
				addHeaderLine(builder, "Operator", standaloneMassSpectrum.getOperator());
				if(standaloneMassSpectrum.getDate() != null) {
					addHeaderLine(builder, "Date", ValueFormat.getDateFormatEnglish().format(standaloneMassSpectrum.getDate()));
				}
			}
		}

		text.setText(builder.toString());
	}

	private void addHeaderLine(StringBuilder builder, String key, String value) {

		builder.append(key);
		builder.append(": ");
		builder.append(value);
		builder.append("\n");
	}

	private void createControl() {

		setLayout(new FillLayout());
		text = new Text(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	}
}
