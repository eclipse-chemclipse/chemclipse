/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.logging.core;

public class Logger extends Category {
	
	private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

	protected Logger(Class<?> clazz) {

		super(clazz);
	}

	/**
	 * Returns the logger.
	 */
	public static Logger getLogger(Class<?> clazz) {

		return new Logger(clazz);
	}
	
	/**
	 * Returns the logger for the caller class.
	 */
	public static Logger getLogger() {

		return new Logger(STACK_WALKER.getCallerClass());
	}
}
