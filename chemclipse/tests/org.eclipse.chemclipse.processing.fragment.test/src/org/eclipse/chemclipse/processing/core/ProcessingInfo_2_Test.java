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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ProcessingInfo_2_Test {

	private IProcessingInfo<String> processingInfo;
	private IProcessingInfo<String> processingInfo2;
	private IProcessingMessage processingMessage;
	private String processingResult;
	private String processingResult2;

	@Before
	public void setUp() {

		processingInfo = new ProcessingInfo<>();
		processingResult = "Hello World!";
		processingInfo.setProcessingResult(processingResult);

		processingMessage = new ProcessingMessage(MessageType.ERROR, "Load Peak", "The peak X35P couldn't be loaded, cause it seems to have no values.");
		processingInfo.addMessage(processingMessage);

		processingMessage = new ProcessingMessage(MessageType.WARN, "Load Peak", "The peak X35P couldn't be loaded completely.");
		processingInfo.addMessage(processingMessage);

		processingMessage = new ProcessingMessage(MessageType.INFO, "Calculate Abundance", "The abundance was calculated correctly.");
		processingInfo.addMessage(processingMessage);
		/*
		 * ProcessingInfo2
		 */
		processingInfo2 = new ProcessingInfo<>();
		processingResult2 = "Hello World 2!";
		processingInfo2.setProcessingResult(processingResult2);
		processingInfo2.addMessages(processingInfo);
	}

	@Test
	public void testProcessingInfo_1() {

		assertNotNull(processingInfo.getProcessingResult());
	}

	@Test
	public void testProcessingInfo_2() {

		Object result = processingInfo2.getProcessingResult();
		assertTrue(result instanceof String);
		assertEquals("Hello World 2!", (String)result);
	}

	@Test
	public void testProcessingInfo_3() {

		List<IProcessingMessage> messages = processingInfo2.getMessages();
		assertEquals(3, messages.size());
	}

	@Test
	public void testProcessingInfo_4() {

		List<IProcessingMessage> messages = processingInfo2.getMessages();
		IProcessingMessage message = messages.get(0);
		assertEquals(MessageType.ERROR, message.getMessageType());
		assertEquals("Load Peak", message.getDescription());
		assertEquals("The peak X35P couldn't be loaded, cause it seems to have no values.", message.getMessage());
	}

	@Test
	public void testProcessingInfo_5() {

		List<IProcessingMessage> messages = processingInfo2.getMessages();
		IProcessingMessage message = messages.get(1);
		assertEquals(MessageType.WARN, message.getMessageType());
		assertEquals("Load Peak", message.getDescription());
		assertEquals("The peak X35P couldn't be loaded completely.", message.getMessage());
	}

	@Test
	public void testProcessingInfo_6() {

		List<IProcessingMessage> messages = processingInfo2.getMessages();
		IProcessingMessage message = messages.get(2);
		assertEquals(MessageType.INFO, message.getMessageType());
		assertEquals("Calculate Abundance", message.getDescription());
		assertEquals("The abundance was calculated correctly.", message.getMessage());
	}
}
