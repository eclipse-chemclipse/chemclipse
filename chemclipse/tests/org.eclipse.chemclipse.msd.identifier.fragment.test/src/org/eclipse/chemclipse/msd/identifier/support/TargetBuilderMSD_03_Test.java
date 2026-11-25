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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.targets.TargetUnknownSettings;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class TargetBuilderMSD_03_Test {

	private TargetBuilderMSD targetBuilderMSD = new TargetBuilderMSD();
	private IScanMSD scanMSD = new ScanMSD();

	@BeforeAll
	public void setUp() throws Exception {

		scanMSD.setRetentionTime(60000);
		scanMSD.setRetentionIndex(765.2f);
		scanMSD.addIon(new Ion(18, 284.4f));
		scanMSD.addIon(new Ion(28, 876.1f));
		scanMSD.addIon(new Ion(32, 543.9f));
		scanMSD.addIon(new Ion(55, 1000.0f));
		scanMSD.addIon(new Ion(75, 120.3f));
	}

	@Test
	public void test01() {

		scanMSD.getTargets().clear();
		TargetUnknownSettings targetUnknownSettings = new TargetUnknownSettings();
		targetUnknownSettings.setIgnoreTraces("");
		targetUnknownSettings.setIncludeIntensityPercent(true);
		targetBuilderMSD.setMassSpectrumTargetUnknown(scanMSD, "Test", targetUnknownSettings);
		IIdentificationTarget identificationTarget = scanMSD.getTargets().iterator().next();
		assertEquals("Unknown [55|100,28|88,32|54,18|28,75|12]", identificationTarget.getLibraryInformation().getName());
	}

	@Test
	public void test02() {

		scanMSD.getTargets().clear();
		TargetUnknownSettings targetUnknownSettings = new TargetUnknownSettings();
		targetUnknownSettings.setIgnoreTraces("28 32");
		targetUnknownSettings.setIncludeIntensityPercent(true);
		targetBuilderMSD.setMassSpectrumTargetUnknown(scanMSD, "Test", targetUnknownSettings);
		IIdentificationTarget identificationTarget = scanMSD.getTargets().iterator().next();
		assertEquals("Unknown [55|100,18|28,75|12]", identificationTarget.getLibraryInformation().getName());
	}

	@Test
	public void test03() {

		scanMSD.getTargets().clear();
		TargetUnknownSettings targetUnknownSettings = new TargetUnknownSettings();
		targetUnknownSettings.setIgnoreTraces("26 - 33");
		targetUnknownSettings.setIncludeIntensityPercent(true);
		targetBuilderMSD.setMassSpectrumTargetUnknown(scanMSD, "Test", targetUnknownSettings);
		IIdentificationTarget identificationTarget = scanMSD.getTargets().iterator().next();
		assertEquals("Unknown [55|100,18|28,75|12]", identificationTarget.getLibraryInformation().getName());
	}

	@Test
	public void test04() {

		scanMSD.getTargets().clear();
		TargetUnknownSettings targetUnknownSettings = new TargetUnknownSettings();
		targetUnknownSettings.setIgnoreTraces("18 28 32");
		targetUnknownSettings.setIncludeIntensityPercent(true);
		targetBuilderMSD.setMassSpectrumTargetUnknown(scanMSD, "Test", targetUnknownSettings);
		IIdentificationTarget identificationTarget = scanMSD.getTargets().iterator().next();
		assertEquals("Unknown [55|100,75|12]", identificationTarget.getLibraryInformation().getName());
	}

	@Test
	public void test05() {

		scanMSD.getTargets().clear();
		TargetUnknownSettings targetUnknownSettings = new TargetUnknownSettings();
		targetUnknownSettings.setIgnoreTraces("18 28 32 55 75");
		targetUnknownSettings.setIncludeIntensityPercent(true);
		targetBuilderMSD.setMassSpectrumTargetUnknown(scanMSD, "Test", targetUnknownSettings);
		IIdentificationTarget identificationTarget = scanMSD.getTargets().iterator().next();
		assertEquals("Unknown []", identificationTarget.getLibraryInformation().getName());
	}
}