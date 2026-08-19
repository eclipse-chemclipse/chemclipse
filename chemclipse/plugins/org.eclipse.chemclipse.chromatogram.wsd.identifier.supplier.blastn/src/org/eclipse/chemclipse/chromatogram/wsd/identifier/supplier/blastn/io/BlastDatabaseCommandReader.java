/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io;

import java.util.ArrayList;
import java.util.List;

public final class BlastDatabaseCommandReader {

	public record BlastDatabaseInfo(long totalBases, List<String> volumes) {
	}

	public static BlastDatabaseInfo parse(String text) {

		long totalBases = -1L;
		List<String> volumes = new ArrayList<>();
		boolean inVolumes = false;

		for(String line : text.lines().toList()) {
			String trimmed = line.trim();

			if(trimmed.contains("total bases")) {
				totalBases = parseTotalBases(trimmed);
				continue;
			}

			if(trimmed.equals("Volumes:")) {
				inVolumes = true;
				continue;
			}

			if(inVolumes && !trimmed.isEmpty()) {
				volumes.add(trimmed);
			}
		}

		return new BlastDatabaseInfo(totalBases, volumes);
	}

	private static long parseTotalBases(String line) {

		String[] parts = line.split(";");
		if(parts.length < 2) {
			throw new IllegalArgumentException("Could not find total bases in line: " + line);
		}

		String number = parts[1].replace("total bases", "").replace(",", "").trim();
		return Long.parseLong(number);
	}
}