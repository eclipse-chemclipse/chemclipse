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

	private static final String USERS_CHOICE = "Users Choice";
	private static final String HYDROCARBONS = "Hydrocarbons";
	private static final String FATTY_ACIDS = "Fatty Acids";
	private static final String FAME = "FAME";
	private static final String SOLVENT_TAILING = "Solvent Tailing";
	private static final String COLUMN_BLEED = "Column Bleed";
	private static final String TRADITIONAL = "Traditional Wavelengths";
	private static final String MERCURY_LAMP = "Low Pressure Mercury Lamp";
	private static final String ZINC_LAMP = "Zinc Lamp";

	public static final String getDefaultTraces() {

		NamedTraces namedTraces = new NamedTraces();

		namedTraces.add(new NamedTrace(USERS_CHOICE, "18 28 32 84 207"));
		namedTraces.add(new NamedTrace(HYDROCARBONS, "57 71 85"));
		namedTraces.add(new NamedTrace(FATTY_ACIDS, "74 84"));
		namedTraces.add(new NamedTrace(FAME, "79 81"));
		namedTraces.add(new NamedTrace(SOLVENT_TAILING, "84"));
		namedTraces.add(new NamedTrace(COLUMN_BLEED, "207"));

		return namedTraces.save();
	}

	public static final String getDefaultWavelengths() {

		NamedTraces namedTraces = new NamedTraces();

		namedTraces.add(new NamedTrace(TRADITIONAL, "214 254 280 365"));
		namedTraces.add(new NamedTrace(MERCURY_LAMP, "254")); // 253.7 nm
		namedTraces.add(new NamedTrace(ZINC_LAMP, "214"));

		return namedTraces.save();
	}
}
