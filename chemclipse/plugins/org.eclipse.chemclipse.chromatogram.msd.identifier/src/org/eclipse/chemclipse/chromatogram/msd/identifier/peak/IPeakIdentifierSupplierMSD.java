/*******************************************************************************
 * Copyright (c) 2008, 2024 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Jan Holy - implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.identifier.peak;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IPeakIdentifierSettingsMSD;
import org.eclipse.chemclipse.model.identifier.core.ISupplier;
import org.eclipse.chemclipse.support.literature.LiteratureReference;

public interface IPeakIdentifierSupplierMSD extends ISupplier {

	@Override
	Class<? extends IPeakIdentifierSettingsMSD> getSettingsClass();

	List<LiteratureReference> getLiteratureReferences();
}
