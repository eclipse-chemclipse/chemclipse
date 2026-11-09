/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 * Christoph Läubrich - change to new Wizard API
 * Philip Wenig - get rid of JavaFX
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.ux.extension.xxd.ui.wizards.InputEntriesWizard;
import org.eclipse.chemclipse.ux.extension.xxd.ui.wizards.InputWizardSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.DataInputEntry;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IDataInputEntry;
import org.eclipse.chemclipse.xxd.process.supplier.pca.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;

public class ScanFilesWizardPage extends DataInputPageWizard {

	public ScanFilesWizardPage() {

		super("DataInputFiles"); //$NON-NLS-1$
		setTitle(StatisticsMessages.chromatogramMsdInputFiles);
		setDescription(StatisticsMessages.selectChromatogramMsdInputFiles);
	}

	@Override
	protected void addFiles() {

		InputWizardSettings inputWizardSettings = InputWizardSettings.create(Activator.getDefault().getPreferenceStore(), PreferenceSupplier.N_INPUT_FILE, DataType.MSD);
		inputWizardSettings.setTitle(StatisticsMessages.chromatogramMsdInputFiles);
		inputWizardSettings.setDescription(StatisticsMessages.selectSeveralChromatogramMsdInputFiles);

		List<IDataInputEntry> dataInputEntries = new ArrayList<>();
		for(File file : InputEntriesWizard.openWizard(getShell(), inputWizardSettings).keySet()) {
			dataInputEntries.add(new DataInputEntry(file.getAbsolutePath()));
		}

		addInputFiles(dataInputEntries);
		update();
	}
}
