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
package org.eclipse.chemclipse.processing.core;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ProcessingMessage_2_Test {

	private IProcessingMessage processingMessage;

	@Before
	public void setUp() {

		processingMessage = new ProcessingMessage(null, null, null);
	}

	@Test
	public void testGetValues_1() {

		assertEquals(MessageType.ERROR, processingMessage.getMessageType());
	}

	@Test
	public void testGetValues_2() {

		assertEquals("Description was null.", processingMessage.getDescription());
	}

	@Test
	public void testGetValues_3() {

		assertEquals("Message was null.", processingMessage.getMessage());
	}
}
