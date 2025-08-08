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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.settings.RetentionIndexImporterSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.junit.Test;

public class RetentionIndexImporter_1_ITest extends RetentionIndexImporterTestCase {

	@Test
	public void test1a() {

		RetentionIndexImporterSettings settings = getRetentionIndexImporterSettings();

		IChromatogram chromatogram = getChromatogram(CHROMATOGRAM_1);
		apply(chromatogram, settings);

		assertEquals(5, chromatogram.getSeparationColumnIndices().size());
		assertEquals(5, chromatogram.getReferencedChromatograms().get(0).getSeparationColumnIndices().size());
	}

	@Test
	public void test1b() {

		RetentionIndexImporterSettings settings = getRetentionIndexImporterSettings();
		settings.setCaseSensitive(false);

		IChromatogram chromatogram = getChromatogram(CHROMATOGRAM_1);
		apply(chromatogram, settings);

		assertEquals(5, chromatogram.getSeparationColumnIndices().size());
		assertEquals(5, chromatogram.getReferencedChromatograms().get(0).getSeparationColumnIndices().size());
	}

	@Test
	public void test1c() {

		RetentionIndexImporterSettings settings = getRetentionIndexImporterSettings();
		settings.setFileNamePattern("");

		IChromatogram chromatogram = getChromatogram(CHROMATOGRAM_1);
		apply(chromatogram, settings);

		assertEquals(5, chromatogram.getSeparationColumnIndices().size());
		assertEquals(5, chromatogram.getReferencedChromatograms().get(0).getSeparationColumnIndices().size());
	}

	@Test
	public void test1d() {

		RetentionIndexImporterSettings settings = getRetentionIndexImporterSettings();
		settings.setFileNamePattern("{chromatogram}");

		IChromatogram chromatogram = getChromatogram(CHROMATOGRAM_1);
		apply(chromatogram, settings);

		assertEquals(10, chromatogram.getSeparationColumnIndices().size());
		assertEquals(10, chromatogram.getReferencedChromatograms().get(0).getSeparationColumnIndices().size());
	}

	@Test
	public void test1e() {

		RetentionIndexImporterSettings settings = getRetentionIndexImporterSettings();
		settings.setFileNamePattern("heptane C7 - tridecane C13");

		IChromatogram chromatogram = getChromatogram(CHROMATOGRAM_1);
		apply(chromatogram, settings);

		assertEquals(7, chromatogram.getSeparationColumnIndices().size());
		assertEquals(7, chromatogram.getReferencedChromatograms().get(0).getSeparationColumnIndices().size());
	}

	@Test
	public void test1f() {

		RetentionIndexImporterSettings settings = getRetentionIndexImporterSettings();
		settings.setFileNamePattern("(.*)(C7)(.*)(C13)");

		IChromatogram chromatogram = getChromatogram(CHROMATOGRAM_1);
		apply(chromatogram, settings);

		assertEquals(7, chromatogram.getSeparationColumnIndices().size());
		assertEquals(7, chromatogram.getReferencedChromatograms().get(0).getSeparationColumnIndices().size());
	}

	@Test
	public void test2a() {

		RetentionIndexImporterSettings settings = getRetentionIndexImporterSettings();
		settings.setProcessReferenceChromatograms(false);

		IChromatogram chromatogram = getChromatogram(CHROMATOGRAM_1);
		apply(chromatogram, settings);

		assertEquals(5, chromatogram.getSeparationColumnIndices().size());
		assertEquals(0, chromatogram.getReferencedChromatograms().get(0).getSeparationColumnIndices().size());
	}

	@Test
	public void test2d() {

		RetentionIndexImporterSettings settings = getRetentionIndexImporterSettings();
		settings.setProcessReferenceChromatograms(false);
		settings.setFileNamePattern("{chromatogram}");

		IChromatogram chromatogram = getChromatogram(CHROMATOGRAM_1);
		apply(chromatogram, settings);

		assertEquals(10, chromatogram.getSeparationColumnIndices().size());
		assertEquals(0, chromatogram.getReferencedChromatograms().get(0).getSeparationColumnIndices().size());
	}
}
