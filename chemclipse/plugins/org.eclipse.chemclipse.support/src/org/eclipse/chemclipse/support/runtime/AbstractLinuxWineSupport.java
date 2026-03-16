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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractLinuxWineSupport extends AbstractWineRuntimeSupport {

	/**
	 * /home/chemclipse/.wine/dosdevices/c:/Programme/NIST/MSSEARCH/nistms$.exe<br/>
	 * /home/chemclipse/.wine/drive_c/Programme/NIST/MSSEARCH/nistms$.exe<br/>
	 *
	 * @param application
	 * @param parameter
	 */
	protected AbstractLinuxWineSupport(String application, List<String> parameter) throws FileNotFoundException {

		super(application, parameter);
	}

	@Override
	public Process executeRunCommand() throws IOException {

		return getRunCommand().start();
	}

	private ProcessBuilder getRunCommand() {

		/*
		 * "env WINEPREFIX=/home/chemclipse/.wine wine start C:\\programme\\nist\\MSSEARCH\\nistms$.exe /INSTRUMENT /PAR=2"
		 */
		List<String> commands = new ArrayList<>();
		commands.add("wine");
		commands.add("start");
		commands.add(getWineApplication());
		commands.addAll(getParameters());
		ProcessBuilder processBuilder = new ProcessBuilder(commands);
		Map<String, String> environment = processBuilder.environment();
		environment.put("WINEPREFIX", getWineEnvironment());
		return processBuilder;
	}
}
