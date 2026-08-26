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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.support.settings.ComboSettingsProperty.ComboSupplier;

public class LocalDatabaseComboSupplier implements ComboSupplier<String> {

	@Override
	public Collection<String> items() {

		File databaseFolder = new File(PreferenceSupplier.getDatabaseFolder());
		if(!databaseFolder.exists()) {
			return null;
		}

		List<String> installedDatabases = new ArrayList<>();
		File[] ndbFiles = databaseFolder.listFiles((_, name) -> name.endsWith(".ndb"));
		if(ndbFiles != null) {
			for(File file : ndbFiles) {
				String filename = file.getName();
				String nameWithoutExtension = filename.substring(0, filename.length() - 4);
				installedDatabases.add(nameWithoutExtension);
			}
		}
		return installedDatabases;
	}

	@Override
	public String fromString(String string) {

		return string;
	}

	@Override
	public String asString(String item) {

		return item;
	}
}
