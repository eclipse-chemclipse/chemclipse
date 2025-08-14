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
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.processing.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.processing.core.exceptions.TypeCastException;
import org.junit.Before;
import org.junit.Test;

public class ProcessingInfo_8_Test {

	private interface MyInterface {

		String getData();
	}

	private class MyClass implements MyInterface {

		private String data = "";

		public MyClass(String data) {

			this.data = data;
		}

		@Override
		public String getData() {

			return data;
		}
	}

	private IProcessingInfo<Object> processingInfo;

	@Before
	public void setUp() {

		processingInfo = new ProcessingInfo<>();
		MyClass myClass = new MyClass("Test");
		processingInfo.setProcessingResult(myClass);
		// processingInfo.setProcessingResult(null);
	}

	@Test
	public void testProcessingInfo_1() {

		MyInterface result = (MyInterface)processingInfo.getProcessingResult();
		assertNotNull(result);
		assertEquals("Test", result.getData());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testProcessingInfo_2() {

		assertThrows(TypeCastException.class, () -> processingInfo.getProcessingResult(Integer.class));
	}
}
