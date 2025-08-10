/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.implementation.MeasurementResult;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.junit.Test;

public class ChromatogramResult_1_Test {

	private IChromatogramMSD chromatogram = new ChromatogramMSD();

	@Test
	public void testChromatogramResult_1() {

		assertEquals(0, chromatogram.getMeasurementResults().size());
	}

	@Test
	public void testChromatogramResult_2() {

		IMeasurementResult<?> chromatogramResult = new MeasurementResult("name", "result1.id", "Description 1", "TestObject 1");
		chromatogram.addMeasurementResult(chromatogramResult);
		assertEquals(1, chromatogram.getMeasurementResults().size());
	}

	@Test
	public void testChromatogramResult_3() {

		IMeasurementResult<?> chromatogramResult = new MeasurementResult("name", "result1.id", "Description 1", "TestObject 1");
		chromatogram.addMeasurementResult(chromatogramResult);
		chromatogramResult = new MeasurementResult("name", "result2.id", "Description 2", "TestObject 2");
		chromatogram.addMeasurementResult(chromatogramResult);
		assertEquals(2, chromatogram.getMeasurementResults().size());
	}

	@Test
	public void testChromatogramResult_4() {

		IMeasurementResult<?> chromatogramResult = new MeasurementResult("name", "result1.id", "Description 1", "TestObject 1");
		chromatogram.addMeasurementResult(chromatogramResult);
		chromatogramResult = new MeasurementResult("name", "result1.id", "Description 2", "TestObject 2");
		chromatogram.addMeasurementResult(chromatogramResult);
		assertEquals(1, chromatogram.getMeasurementResults().size());
		chromatogramResult = chromatogram.getMeasurementResult("result1.id");
		assertEquals("Description 2", chromatogramResult.getDescription());
		assertEquals("TestObject 2", chromatogramResult.getResult());
	}

	@Test
	public void testChromatogramResult_5() {

		IMeasurementResult<?> chromatogramResult = new MeasurementResult("name", "result1.id", "Description 1", "TestObject 1");
		chromatogram.addMeasurementResult(chromatogramResult);
		chromatogramResult = new MeasurementResult("name", "result2.id", "Description 2", "TestObject 2");
		chromatogram.addMeasurementResult(chromatogramResult);
		assertEquals(2, chromatogram.getMeasurementResults().size());
		chromatogram.deleteMeasurementResult("result2.id");
		assertEquals(1, chromatogram.getMeasurementResults().size());
		chromatogramResult = chromatogram.getMeasurementResult("result1.id");
		assertEquals("Description 1", chromatogramResult.getDescription());
		assertEquals("TestObject 1", chromatogramResult.getResult());
	}

	@Test
	public void testChromatogramResult_6() {

		IMeasurementResult<?> chromatogramResult = new MeasurementResult("name", "result1.id", "Description 1", "TestObject 1");
		chromatogram.addMeasurementResult(chromatogramResult);
		chromatogramResult = new MeasurementResult("name", "result2.id", "Description 2", "TestObject 2");
		chromatogram.addMeasurementResult(chromatogramResult);
		assertEquals(2, chromatogram.getMeasurementResults().size());
		chromatogram.removeAllMeasurementResults();
		assertEquals(0, chromatogram.getMeasurementResults().size());
	}

	@Test
	public void testChromatogramResult_7() {

		chromatogram.addMeasurementResult(null);
		assertEquals(0, chromatogram.getMeasurementResults().size());
	}

	@Test
	public void testChromatogramResult_8() {

		IMeasurementResult<?> chromatogramResult = new MeasurementResult("name", "result1.id", "Description 1", "TestObject 1");
		chromatogram.addMeasurementResult(chromatogramResult);
		chromatogramResult = chromatogram.getMeasurementResult((String)null);
		assertNull(chromatogramResult);
	}

	@Test
	public void testChromatogramResult_9() {

		IMeasurementResult<?> chromatogramResult = new MeasurementResult("name", "result1.id", "Description 1", "TestObject 1");
		chromatogram.addMeasurementResult(chromatogramResult);
		chromatogram.deleteMeasurementResult(null);
		assertEquals(1, chromatogram.getMeasurementResults().size());
	}
}
