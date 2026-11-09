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
 * Philip Wenig - get rid of JavaFX
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.wizards;

import org.eclipse.chemclipse.xxd.process.supplier.pca.core.IPreprocessingSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.swt.PreprocessingSettingsUI;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class PreprocessingWizardPage extends WizardPage {

	private PreprocessingSettingsUI preprocessingSettingsUI;

	public PreprocessingWizardPage() {

		super("Preprocess"); //$NON-NLS-1$
		setTitle(StatisticsMessages.preprocessData);
		setDescription(StatisticsMessages.dataCanBePreprocessedLater);
	}

	@Override
	public void createControl(Composite parent) {

		preprocessingSettingsUI = new PreprocessingSettingsUI(parent, SWT.NONE);
		setControl(preprocessingSettingsUI);
	}

	public IPreprocessingSettings getPreprocessingSettings() {

		return preprocessingSettingsUI.getPreprocessingSettings();
	}
}
