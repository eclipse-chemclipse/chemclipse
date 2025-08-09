/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.targets;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.junit.Test;

public class UnknownTargetBuilder_1_Test {

	@Test
	public void test1() {

		TargetUnknownSettings settings = new TargetUnknownSettings();
		settings.setTargetName("Toluene");

		IScan scan = new Scan(42);
		scan.setRetentionTime(5000);
		scan.setRetentionIndex(100);
		ILibraryInformation libraryInformation = UnknownTargetBuilder.getLibraryInformationUnknown(scan, settings, "");

		assertEquals("Toluene []", libraryInformation.getName());
	}

	@Test
	public void test2() {

		TargetUnknownSettings settings = new TargetUnknownSettings();
		settings.setIncludeIntensityPercent(false);
		settings.setIncludeRetentionIndex(false);
		settings.setIncludeRetentionTime(false);
		settings.setMarkerStart("");
		settings.setMarkerStop("");
		settings.setMatchQuality(85.0f);
		settings.setTargetName("Toluene");

		IScan scan = new Scan(42);
		scan.setRetentionTime(5000);
		scan.setRetentionIndex(100);
		ILibraryInformation libraryInformation = UnknownTargetBuilder.getLibraryInformationUnknown(scan, settings, "");

		assertEquals("Toluene", libraryInformation.getName());
	}
}