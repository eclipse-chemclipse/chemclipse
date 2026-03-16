/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.wizards;

import java.util.List;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ISamplesPCA;
import org.eclipse.jface.wizard.Wizard;

public class GroupNamingWizard extends Wizard {

	private GroupNamingWizardPage groupNamingWizardPage;
	private ISamplesPCA<IVariable, ISample> samples;

	public GroupNamingWizard(ISamplesPCA<IVariable, ISample> samples) {

		this.groupNamingWizardPage = new GroupNamingWizardPage(samples);
		this.samples = samples;
	}

	@Override
	public void addPages() {

		addPage(groupNamingWizardPage);
	}

	@Override
	public boolean performFinish() {

		List<ISample> groupSamples = this.groupNamingWizardPage.getSamples();
		for(int i = 0; i < groupSamples.size(); i++) {
			samples.getSamples().get(i).setGroupName(groupSamples.get(i).getGroupName());
		}

		return true;
	}
}