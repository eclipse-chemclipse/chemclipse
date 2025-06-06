/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.support;

import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.widgets.Display;

public class ChromatogramUpdateSupport {

	public static void fireUpdateChromatogramSelection(Display display, IScan scan) {

		display.asyncExec(() -> {

			IEventBroker eventBroker = Activator.getDefault().getEventBroker();
			if(eventBroker != null) {
				DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
				List<Object> objects = dataUpdateSupport.getUpdates(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION);
				if(objects != null && !objects.isEmpty()) {
					Object object = objects.get(0);
					if(object instanceof IChromatogramSelection chromatogramSelection) {
						if(scan != null) {
							/*
							 * We assume that the subtraction takes place in the same
							 * chromatogram. It could happen, that one scan is selected
							 * and set to edit modus and afterwards another chromatogram
							 * is selected. This could lead to misleading behavior.
							 * But it's unclear how to solve it hear. This is currently
							 * the best way to prevent unwanted behavior. The scan chart shows
							 * data from scans and peaks.
							 */
							IChromatogram chromatogram = chromatogramSelection.getChromatogram();
							int retentionTime = scan.getRetentionTime();
							int scanNumber = chromatogram.getScanNumber(retentionTime);
							IScan scanReference = chromatogram.getScan(scanNumber);
							if(scan == scanReference) {
								chromatogramSelection.setSelectedScan(scan);
							}
							chromatogramSelection.getChromatogram().setDirty(true);
							chromatogramSelection.update(false);
						}
					}
				}
			}
		});
	}
}
