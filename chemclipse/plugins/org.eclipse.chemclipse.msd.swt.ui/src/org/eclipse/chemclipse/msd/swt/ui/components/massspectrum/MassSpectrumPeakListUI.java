/*******************************************************************************
 * Copyright (c) 2015, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - increase performance for large spectra
 *******************************************************************************/
package org.eclipse.chemclipse.msd.swt.ui.components.massspectrum;

import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.swt.ui.internal.provider.MassSpectrumPeakLabelProvider;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.swt.widgets.Composite;

public class MassSpectrumPeakListUI extends ExtendedTableViewer {

	private String[] titles = {"m/z", "abundance", "s/n"};
	private int[] bounds = {150, 100, 10};

	public MassSpectrumPeakListUI(Composite parent) {

		super(parent);
		createColumns();
	}

	public MassSpectrumPeakListUI(Composite parent, int style) {

		super(parent, style);
		createColumns();
	}

	public void update(IRegularMassSpectrum regularMassSpectrum) {

		if(regularMassSpectrum != null) {
			setContentProviders();
			if(regularMassSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
				super.setInput(standaloneMassSpectrum.getPeaks());
				setItemCount(standaloneMassSpectrum.getPeaks().size());
			} else if(regularMassSpectrum.getMassSpectrumType() == MassSpectrumType.CENTROID) {
				super.setInput(regularMassSpectrum.getIons());
				setItemCount(regularMassSpectrum.getIons().size());
			}
		} else {
			super.setInput(null);
		}
	}

	public void clear() {

		setInput(null);
	}

	private void createColumns() {

		createColumns(titles, bounds);
		setLabelProvider();
		setContentProviders();
	}

	private void setLabelProvider() {

		setLabelProvider(new MassSpectrumPeakLabelProvider());
	}

	private void setContentProviders() {

		setContentProvider(new ListContentProvider());
	}
}
