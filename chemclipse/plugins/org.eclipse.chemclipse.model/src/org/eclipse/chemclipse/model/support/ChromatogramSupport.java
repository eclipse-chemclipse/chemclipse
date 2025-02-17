/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

import org.eclipse.chemclipse.model.core.IChromatogram;

public class ChromatogramSupport {

	public static void calculateScanIntervalAndDelay(IChromatogram chromatogram) {

		int startRetentionTime = chromatogram.getStartRetentionTime();
		int stopRetentionTime = chromatogram.getStopRetentionTime();
		float deltaRetentionTime = stopRetentionTime - startRetentionTime;
		int numberOfScans = chromatogram.getNumberOfScans();
		/*
		 * Delay
		 */
		int scanDelay = 0;
		if(startRetentionTime > 0) {
			scanDelay = startRetentionTime;
		}
		/*
		 * Interval
		 */
		int scanInterval = 100;
		if(numberOfScans >= 1 && deltaRetentionTime > 0) {
			float calculation = deltaRetentionTime / (numberOfScans - 1) / 10.0f;
			scanInterval = Math.round(calculation) * 10;
		}
		/*
		 * Adjust the retention times.
		 */
		chromatogram.setScanDelay(scanDelay);
		chromatogram.setScanInterval(scanInterval);
	}
}