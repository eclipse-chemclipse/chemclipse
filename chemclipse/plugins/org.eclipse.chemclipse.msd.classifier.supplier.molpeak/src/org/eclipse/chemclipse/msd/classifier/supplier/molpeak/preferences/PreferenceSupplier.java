/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Dr. Lorenz Gerber - initial API and implementation
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.classifier.supplier.molpeak.preferences;

import org.eclipse.chemclipse.msd.classifier.supplier.molpeak.settings.ClassifierSettings;
import org.eclipse.chemclipse.msd.classifier.supplier.molpeak.settings.IBasePeakSettings;
import org.eclipse.chemclipse.msd.classifier.supplier.molpeak.settings.MassSpectrumIdentifierSettings;
import org.eclipse.chemclipse.msd.classifier.supplier.molpeak.settings.PeakIdentifierSettings;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final float MIN_FACTOR = 0.0f;
	public static final float MAX_FACTOR = 100.0f;

	public static final String P_LIMIT_MATCH_FACTOR = "limitMatchFactor";
	public static final float DEF_LIMIT_MATCH_FACTOR = 80.0f;
	public static final String P_MATCH_QUALITY = "matchQuality";
	public static final float DEF_MATCH_QUALITY = 80.0f;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_LIMIT_MATCH_FACTOR, Float.toString(DEF_LIMIT_MATCH_FACTOR));
		putDefault(P_MATCH_QUALITY, Float.toString(DEF_MATCH_QUALITY));
	}

	public static MassSpectrumIdentifierSettings getMassSpectrumIdentifierSettings() {

		MassSpectrumIdentifierSettings settings = new MassSpectrumIdentifierSettings();
		assignDefaultValues(settings);

		return settings;
	}

	public static PeakIdentifierSettings getPeakIdentifierSettings() {

		PeakIdentifierSettings settings = new PeakIdentifierSettings();
		assignDefaultValues(settings);

		return settings;
	}

	public static ClassifierSettings getChromatogramClassifierSettings() {

		return new ClassifierSettings();
	}

	private static void assignDefaultValues(IBasePeakSettings settings) {

		settings.setLimitMatchFactor(INSTANCE().getFloat(P_LIMIT_MATCH_FACTOR, DEF_LIMIT_MATCH_FACTOR));
		settings.setMatchQuality(INSTANCE().getFloat(P_MATCH_QUALITY, DEF_MATCH_QUALITY));
	}
}