/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.traces;

public class NamedTraceUtil {

	private static final String USERS_CHOICE = "Users Choice"; //$NON-NLS-1$
	private static final String HYDROCARBONS = "Hydrocarbons"; //$NON-NLS-1$
	private static final String FATTY_ACIDS = "Fatty Acids"; //$NON-NLS-1$
	private static final String FAME = "FAME"; //$NON-NLS-1$
	private static final String SOLVENT_TAILING = "Solvent Tailing"; //$NON-NLS-1$
	private static final String COLUMN_BLEED = "Column Bleed"; //$NON-NLS-1$
	private static final String TRADITIONAL = "Traditional Wavelengths"; //$NON-NLS-1$
	private static final String MERCURY_LAMP = "Low Pressure Mercury Lamp"; //$NON-NLS-1$
	private static final String ZINC_LAMP = "Zinc Lamp"; //$NON-NLS-1$

	public static final String getDefaultTraces() {

		NamedTraces namedTraces = new NamedTraces();

		namedTraces.add(new NamedTrace(USERS_CHOICE, "18 28 32 84 207")); //$NON-NLS-1$
		namedTraces.add(new NamedTrace(HYDROCARBONS, "57 71 85")); //$NON-NLS-1$
		namedTraces.add(new NamedTrace(FATTY_ACIDS, "74 84")); //$NON-NLS-1$
		namedTraces.add(new NamedTrace(FAME, "79 81")); //$NON-NLS-1$
		namedTraces.add(new NamedTrace(SOLVENT_TAILING, "84")); //$NON-NLS-1$
		namedTraces.add(new NamedTrace(COLUMN_BLEED, "207")); //$NON-NLS-1$

		return namedTraces.save();
	}

	public static final String getDefaultWavelengths() {

		NamedTraces namedTraces = new NamedTraces();

		namedTraces.add(new NamedTrace(TRADITIONAL, "214 254 280 365")); //$NON-NLS-1$
		namedTraces.add(new NamedTrace(MERCURY_LAMP, "254")); // 253.7 nm //$NON-NLS-1$
		namedTraces.add(new NamedTrace(ZINC_LAMP, "214")); //$NON-NLS-1$

		return namedTraces.save();
	}
}
