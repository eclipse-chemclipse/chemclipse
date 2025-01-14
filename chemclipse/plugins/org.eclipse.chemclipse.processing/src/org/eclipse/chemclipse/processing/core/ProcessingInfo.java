/*******************************************************************************
 * Copyright (c) 2012, 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.processing.core;

public class ProcessingInfo<T> extends AbstractProcessingInfo<T> implements IProcessingInfo<T> {

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
