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

public class ProcessingMessage_1_Test {

	private IProcessingMessage processingMessage;

	@Before
	public void setUp() {

		processingMessage = new ProcessingMessage(MessageType.ERROR, "Load Peak", "The peak X35P couldn't be loaded, cause it seems to have no values.");
	}

	@Test
	public void testGetValues_1() {

		assertEquals(MessageType.ERROR, processingMessage.getMessageType());
	}

	@Test
	public void testGetValues_2() {

		assertEquals("Load Peak", processingMessage.getDescription());
	}

	@Test
	public void testGetValues_3() {

		assertEquals("The peak X35P couldn't be loaded, cause it seems to have no values.", processingMessage.getMessage());
	}
}
