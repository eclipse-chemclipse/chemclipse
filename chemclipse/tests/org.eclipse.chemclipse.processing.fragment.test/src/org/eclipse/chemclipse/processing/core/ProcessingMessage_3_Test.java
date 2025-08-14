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

import org.junit.Test;

public class ProcessingMessage_3_Test {

	@Test
	public void testGetMessageType_1() {

		IProcessingMessage processingMessage = new ProcessingMessage(MessageType.ERROR, "Load Peaks", "Peak x2 couldn't be loaded.");
		assertEquals(MessageType.ERROR, processingMessage.getMessageType());
	}

	@Test
	public void testGetMessageType_2() {

		IProcessingMessage processingMessage = new ProcessingMessage(MessageType.WARN, "Load Peaks", "Peak x2 was parsed incompletely.");
		assertEquals(MessageType.WARN, processingMessage.getMessageType());
	}

	@Test
	public void testGetMessageType_3() {

		IProcessingMessage processingMessage = new ProcessingMessage(MessageType.INFO, "Load Peaks", "Peak x2 was loaded successfully.");
		assertEquals(MessageType.INFO, processingMessage.getMessageType());
	}
}
