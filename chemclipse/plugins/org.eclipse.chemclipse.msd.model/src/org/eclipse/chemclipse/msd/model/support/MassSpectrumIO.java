/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;

public class MassSpectrumIO {

	private static final String DELIMITER_ION_ABUNDANCE = ":";
	private static final String DELIMITER_IONS = ";";

	public static String getMassSpectrum(IScanMSD massSpectrum) {

		StringBuilder builder = new StringBuilder();
		if(massSpectrum != null) {
			for(IIon ion : massSpectrum.getIons()) {
				builder.append(ion.getIon());
				builder.append(DELIMITER_ION_ABUNDANCE);
				builder.append(ion.getAbundance());
				builder.append(DELIMITER_IONS);
			}
		}
		return builder.toString();
	}

	public static IScanMSD getMassSpectrum(String value) {

		if(value != null && !value.isEmpty()) {
			ScanMSD scanMSD = new ScanMSD();
			String[] ions = value.split(DELIMITER_IONS);
			for(String ion : ions) {
				String[] fragment = ion.split(DELIMITER_ION_ABUNDANCE);
				if(fragment.length == 2) {
					/*
					 * Add the mass fragment
					 */
					double mz = Double.parseDouble(fragment[0]);
					float abundance = Float.parseFloat(fragment[1]);
					IIon subtractIon = new Ion(mz, abundance);
					scanMSD.addIon(subtractIon);
				}
			}
			return scanMSD;
		}
		return null;
	}
}