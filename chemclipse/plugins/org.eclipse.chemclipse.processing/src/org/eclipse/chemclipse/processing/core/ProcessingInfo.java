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
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.processing.core;

public class ProcessingInfo<T> extends AbstractProcessingInfo<T> {

	public ProcessingInfo() {
		super();
	}

	public ProcessingInfo(IProcessingInfo<T> processingInfo) {
		super(processingInfo);
	}

	public ProcessingInfo(T initialResult) {
		super();
		setProcessingResult(initialResult);
	}

	@Override
	public String toString() {

		return "Result: " + getProcessingResult() + ", Messages: " + getMessages();
	}
}
