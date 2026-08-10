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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.wizards;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;

public class DatabaseCleanUpWizard extends Wizard {

	private final List<DatabaseEntryClean> entries;
	private DatabaseCleanUpWizardPage page;

	public DatabaseCleanUpWizard(List<DatabaseEntryClean> entries) {

		super();
		this.entries = entries;
		setWindowTitle("Clean Up Mass Spectrum Library");
	}

	@Override
	public void addPages() {

		page = new DatabaseCleanUpWizardPage(entries);
		addPage(page);
	}

	@Override
	public boolean performFinish() {

		return true;
	}

	public List<DatabaseEntryClean> getEntriesToDelete() {

		return page.getEntries();
	}
}