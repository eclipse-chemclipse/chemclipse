/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.core;

import java.util.Set;

public interface IRegularLibraryMassSpectrum extends IRegularMassSpectrum, ILibraryMassSpectrum {

	public static final String PROPERTY_COLLISION_ENERGY = "Collision Energy"; // 30
	public static final String PROPERTY_INSTRUMENT_NAME = "Instrument Name"; // Agilent QTOF 6530
	public static final String PROPERTY_INSTRUMENT_TYPE = "Instrument Type"; // LC-ESI-QTOF
	public static final String PROPERTY_IONIZATION_MODE = "Ionization Mode"; // APCI
	public static final String PROPERTY_FRAGMENTATION_METHOD = "Fragmentation Method"; // CID

	String getPrecursorType();

	void setPrecursorType(String precursorType);

	double getNeutralMass();

	void setNeutralMass(double neutralMass);

	Set<String> getPropertyKeySet();

	String getProperty(String property);

	void putProperty(String key, String value);
}