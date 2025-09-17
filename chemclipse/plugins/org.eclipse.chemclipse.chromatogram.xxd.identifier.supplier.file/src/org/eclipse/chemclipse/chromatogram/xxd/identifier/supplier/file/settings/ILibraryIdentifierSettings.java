/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.settings;

import org.eclipse.chemclipse.msd.identifier.settings.IMassSpectrumComparatorSettings;
import org.eclipse.chemclipse.msd.identifier.settings.IMassSpectrumIdentifierSettings;

public interface ILibraryIdentifierSettings extends IMassSpectrumIdentifierSettings, IFileIdentifierSettings, IMassSpectrumComparatorSettings {
}
