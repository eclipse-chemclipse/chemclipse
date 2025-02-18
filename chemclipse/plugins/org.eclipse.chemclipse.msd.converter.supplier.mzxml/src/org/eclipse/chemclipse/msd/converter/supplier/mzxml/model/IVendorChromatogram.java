/*******************************************************************************
 * Copyright (c) 2008, 2023 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.model;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;

public interface IVendorChromatogram extends IChromatogramMSD {

	String getInstrument();

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
