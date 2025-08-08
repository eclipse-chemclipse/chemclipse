/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.peak.detector.supplier.firstderivative.core;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.chemclipse.chromatogram.msd.peak.detector.core.IPeakDetectorMSD;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.IRawPeak;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.RawPeak;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.core.BasePeakDetector;
import org.junit.Test;

/**
 * peakDetectorSettings.getThreshold() is MEDIUM > threshold = 0.05d;
 * WindowSize.SCANS_5
 */
public class FirstDerivativePeakDetector_3_Test {

	private IPeakDetectorMSD firstDerivativePeakDetector = new PeakDetectorMSD();;
	private Class<?> firstDerivativePeakDetectorClass = BasePeakDetector.class;;

	@Test
	public void testIsValidRawPeak_1() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		RawPeak rawPeak = new RawPeak(25, 26, 27);
		Boolean result;
		Method method = firstDerivativePeakDetectorClass.getDeclaredMethod("isValidRawPeak", new Class[]{IRawPeak.class});
		method.setAccessible(true);
		result = (Boolean)method.invoke(firstDerivativePeakDetector, new Object[]{rawPeak});
		assertEquals("detectPeakStart", Boolean.valueOf(true), result);
	}

	@Test
	public void testIsValidRawPeak_2() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		RawPeak rawPeak = new RawPeak(25, 25, 26);
		Boolean result;
		Method method = firstDerivativePeakDetectorClass.getDeclaredMethod("isValidRawPeak", new Class[]{IRawPeak.class});
		method.setAccessible(true);
		result = (Boolean)method.invoke(firstDerivativePeakDetector, new Object[]{rawPeak});
		assertEquals("detectPeakStart", Boolean.valueOf(false), result);
	}
}
