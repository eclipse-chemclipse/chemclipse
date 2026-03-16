/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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

public class ProcessingMessage extends AbstractProcessingMessage {

	public ProcessingMessage(MessageType messageType, String description, String message) {

		super(messageType, description, message);
	}

	public ProcessingMessage(MessageType messageType, String description, String message, String solution) {

		super(messageType, description, message, solution);
	}
}
