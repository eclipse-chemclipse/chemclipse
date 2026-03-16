/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.support.runtime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public abstract class AbstractWindowsSupport extends AbstractRuntimeSupport {

	/**
	 * Set e.g.: C:\Programs\NIST\MSSEARCH\NISTMS$.EXE
	 */
	protected AbstractWindowsSupport(String application, List<String> parameters) throws FileNotFoundException {

		super(application, parameters);
	}

	@Override
	public Process executeRunCommand() throws IOException {

		return getRunCommand().start();
	}

	private ProcessBuilder getRunCommand() {

		/*
		 * Returns e.g.: "C:\Programs\NIST\MSSEARCH\nistms$.exe /INSTRUMENT /PAR=2
		 */
		return new ProcessBuilder().command(getCommand());
	}
}
