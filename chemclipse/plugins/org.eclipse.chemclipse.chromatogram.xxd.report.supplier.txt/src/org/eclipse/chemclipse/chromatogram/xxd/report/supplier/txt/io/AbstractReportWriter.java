/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.report.supplier.txt.io;

import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;

public class AbstractReportWriter {

	public String getChromatogramType(IChromatogram chromatogram) {

		if(chromatogram instanceof IChromatogramCSD) {
			return "CSD";
		} else if(chromatogram instanceof IChromatogramMSD) {
			return "MSD";
		} else if(chromatogram instanceof IChromatogramWSD) {
			return "WSD";
		} else {
			return "???";
		}
	}
}
