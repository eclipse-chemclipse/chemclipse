/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.processing.core;

import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.processing.core.exceptions.TypeCastException;
import org.junit.Before;
import org.junit.Test;

public class ProcessingInfo_7_Test {

	private IProcessingInfo<Object> processingInfo;

	@Before
	public void setUp() {

		processingInfo = new ProcessingInfo<>();
		processingInfo.setProcessingResult(null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testProcessingInfo_1() {

		assertThrows(TypeCastException.class, () -> processingInfo.getProcessingResult(String.class));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testProcessingInfo_2() {

		assertThrows(TypeCastException.class, () -> processingInfo.getProcessingResult(Integer.class));
	}
}
