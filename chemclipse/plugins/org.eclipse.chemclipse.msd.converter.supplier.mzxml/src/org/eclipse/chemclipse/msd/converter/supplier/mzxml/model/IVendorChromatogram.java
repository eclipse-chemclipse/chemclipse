/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.model;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;

public interface IVendorChromatogram extends IChromatogramMSD {

	@Override
	String getInstrument();

	@Override
	void setInstrument(String instrument);

	String getIonisation();

	void setIonisation(String ionisation);

	String getMassAnalyzer();

	void setMassAnalyzer(String massAnalyzer);

	String getMassDetector();

	void setMassDetector(String massDetector);

	String getSoftware();

	void setSoftware(String software);
}
