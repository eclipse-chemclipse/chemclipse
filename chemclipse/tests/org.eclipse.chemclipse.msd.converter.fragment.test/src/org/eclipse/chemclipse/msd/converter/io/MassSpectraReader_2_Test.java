/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.junit.Test;

public class MassSpectraReader_2_Test {

	private IMassSpectraReader massSpectraReader = new MassSpectraReader_Test_Impl();
	private IRegularLibraryMassSpectrum massSpectrum = new RegularLibraryMassSpectrum_Test_Impl();

	@Test
	public void test1() {

		String value = null;
		String delimiter = null;

		massSpectraReader.extractRetentionIndices(massSpectrum, value, delimiter);
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertFalse(massSpectrum.hasAdditionalRetentionIndices());
	}

	@Test
	public void test2() {

		String value = "";
		String delimiter = null;

		massSpectraReader.extractRetentionIndices(massSpectrum, value, delimiter);
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertFalse(massSpectrum.hasAdditionalRetentionIndices());
	}

	@Test
	public void test3() {

		String value = null;
		String delimiter = "";

		massSpectraReader.extractRetentionIndices(massSpectrum, value, delimiter);
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertFalse(massSpectrum.hasAdditionalRetentionIndices());
	}

	@Test
	public void test4() {

		String value = "";
		String delimiter = "";

		massSpectraReader.extractRetentionIndices(massSpectrum, value, delimiter);
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertFalse(massSpectrum.hasAdditionalRetentionIndices());
	}

	@Test
	public void test5() {

		String value = "1160, 1632";
		String delimiter = " ";

		massSpectraReader.extractRetentionIndices(massSpectrum, value, delimiter);
		assertEquals(1160.0f, massSpectrum.getRetentionIndex(), 0);
		assertTrue(massSpectrum.hasAdditionalRetentionIndices());
		assertEquals(1160.0f, massSpectrum.getRetentionIndex(SeparationColumnType.NON_POLAR), 0);
	}

	@Test
	public void test6() {

		String value = "1160, 1632";
		String delimiter = ", ";

		massSpectraReader.extractRetentionIndices(massSpectrum, value, delimiter);
		assertEquals(1160.0f, massSpectrum.getRetentionIndex(), 0);
		assertTrue(massSpectrum.hasAdditionalRetentionIndices());
		assertEquals(1160.0f, massSpectrum.getRetentionIndex(SeparationColumnType.NON_POLAR), 0);
		assertEquals(1632.0f, massSpectrum.getRetentionIndex(SeparationColumnType.POLAR), 0);
	}

	@Test
	public void test7() {

		String value = "1160, ";
		String delimiter = ", ";

		massSpectraReader.extractRetentionIndices(massSpectrum, value, delimiter);
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertFalse(massSpectrum.hasAdditionalRetentionIndices());
	}
}