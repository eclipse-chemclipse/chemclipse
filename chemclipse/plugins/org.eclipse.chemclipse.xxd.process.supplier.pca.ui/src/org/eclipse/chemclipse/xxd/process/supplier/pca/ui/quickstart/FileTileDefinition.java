/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.quickstart;

import org.eclipse.chemclipse.ux.extension.ui.definitions.ITileDefinition;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.wizards.FilesInputWizard;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.wizards.IInputWizard;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.osgi.service.component.annotations.Component;

@Component(service = ITileDefinition.class)
public class FileTileDefinition extends WizardTile {

	@Override
	public String getTitle() {

		return StatisticsMessages.files;
	}

	@Override
	protected IInputWizard createWizard() {

		return new FilesInputWizard();
	}
}