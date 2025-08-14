/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
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

import org.junit.Test;

public class ProcessingMessage_4_Test {

	@Test
	public void testGetMessageType_1() {

		IProcessingMessage processingMessage = new ProcessingMessage(MessageType.INFO, "Test", "Everything is ok.", "");
		assertEquals(MessageType.INFO, processingMessage.getMessageType());
		assertEquals("", processingMessage.getProposedSolution());
	}

	@Test
	public void testGetMessageType_2() {

		IProcessingMessage processingMessage = new ProcessingMessage(MessageType.WARN, "Test", "The area is out of range.", "Please adjust the area.");
		assertEquals(MessageType.WARN, processingMessage.getMessageType());
		assertEquals("Please adjust the area.", processingMessage.getProposedSolution());
	}

	@Test
	public void testGetMessageType_3() {

		IProcessingMessage processingMessage = new ProcessingMessage(MessageType.ERROR, "Test", "The area is 0.", "Please re-evaluate the data.");
		assertEquals(MessageType.ERROR, processingMessage.getMessageType());
		assertEquals("Please re-evaluate the data.", processingMessage.getProposedSolution());
	}
}
