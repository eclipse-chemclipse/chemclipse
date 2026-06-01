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
package org.eclipse.chemclipse.model.quantitation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.support.text.ILabel;

public enum CalibrationMethod implements ILabel {

	/*
	 * ISTD is used for internal standards only.
	 * All other are used for external calibration.
	 */
	LINEAR("Linear"), //
	QUADRATIC("Quadratic (Classic)"), // Keep name for backward compatibility
	QUADRATIC_CHEMSTATION("Quadratic (ChemStation - experimental)"), //
	AVERAGE("Average"), //
	ISTD("Internal Standard");

	private String label = "";

	private CalibrationMethod(String label) {

		this.label = label;
	}

	@Override
	public String label() {

		return label;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}

	public static final CalibrationMethod[] getInternalCalibrationOptions() {

		return new CalibrationMethod[]{ //
				ISTD //
		};
	}

	public static final String[] getInternalCalibrationOptionsArray() {

		return getOptionsArray(getInternalCalibrationOptions());
	}

	public static final CalibrationMethod[] getExternalCalibrationOptions() {

		return new CalibrationMethod[]{ //
				AVERAGE, //
				LINEAR, //
				QUADRATIC, //
				QUADRATIC_CHEMSTATION //
		};
	}

	public static final String[] getExternalCalibrationOptionsArray() {

		return getOptionsArray(getExternalCalibrationOptions());
	}

	public static boolean isQuadraticMethod(CalibrationMethod calibrationMethod) {

		return CalibrationMethod.QUADRATIC.equals(calibrationMethod) || CalibrationMethod.QUADRATIC_CHEMSTATION.equals(calibrationMethod);
	}

	private static final String[] getOptionsArray(CalibrationMethod[] calibrationMethods) {

		List<String> options = new ArrayList<>();
		for(CalibrationMethod calibrationMethod : calibrationMethods) {
			options.add(calibrationMethod.name());
		}

		return options.toArray(new String[options.size()]);
	}
}