/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - adjust tile definition
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.quickstart;

import org.eclipse.chemclipse.ux.extension.ui.definitions.ITileDefinition;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.wizards.IInputWizard;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.wizards.PeaksInputWizard;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.osgi.service.component.annotations.Component;

@Component(service = ITileDefinition.class)
public class PeakTileDefinition extends WizardTile {

	@Override
	public String getTitle() {

		return StatisticsMessages.peaks;
	}

	@Override
	protected IInputWizard createWizard() {

		return new PeaksInputWizard();
	}
}