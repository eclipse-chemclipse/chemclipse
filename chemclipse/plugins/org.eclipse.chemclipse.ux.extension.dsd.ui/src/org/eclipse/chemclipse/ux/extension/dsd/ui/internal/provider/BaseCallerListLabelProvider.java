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
package org.eclipse.chemclipse.ux.extension.dsd.ui.internal.provider;

import org.eclipse.chemclipse.dsd.model.core.Nucleobase;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.ITargetSupplier;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.targets.TargetSupport;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.ux.extension.ui.provider.IdentificationTargetSupport;
import org.eclipse.swt.graphics.Image;

public class BaseCallerListLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String BASE_NUMBER = "Number";
	public static final String BEST_TARGET = "Nucleotide";
	public static final String MATCH_QUALITY = "Rating";

	private static final String BLANK = "";

	public static final String[] TITLES = { //
			BASE_NUMBER, //
			MATCH_QUALITY, //
			BEST_TARGET, //
	};

	public static final int[] BOUNDS = { //
			80, //
			100, //
			200, //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 1) {
			if(element instanceof ITargetSupplier targetSupplier) {
				IIdentificationTarget target = TargetSupport.getBestIdentificationTarget(targetSupplier);
				return IdentificationTargetSupport.getRatingSymbol(target);
			}
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = BLANK;
		if(element instanceof IScan scan) {
			text = getScanText(scan, columnIndex);
		}
		return text;
	}

	private String getScanText(IScan scan, int columnIndex) {

		String text = BLANK;

		switch(columnIndex) {
			case 0:
				text = String.valueOf(scan.getCycleNumber());
				break;
			case 1:
				IIdentificationTarget identificationTarget = TargetSupport.getBestIdentificationTarget(scan);
				if(!identificationTarget.getComparisonResult().getMetricValues().isEmpty()) {
					text = String.valueOf(identificationTarget.getComparisonResult().getMetricValues().values().iterator().next().intValue());
				}
				break;
			case 2:
				String targetLibraryField = TargetSupport.getBestTargetLibraryField(scan);
				Nucleobase nucleobase = Nucleobase.of(targetLibraryField.charAt(0));
				text = nucleobase.label();
				break;
		}

		return text;
	}
}