/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - add support for MALDI
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.part.support;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.csd.converter.chromatogram.ChromatogramConverterCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IMeasurementInfo;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverter;
import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.vsd.converter.core.ScanConverterVSD;
import org.eclipse.chemclipse.wsd.converter.chromatogram.ChromatogramConverterWSD;
import org.eclipse.core.runtime.NullProgressMonitor;

public class OverviewSupport {

	private String lastDisplayedFile = "";
	private Set<String> topics = new HashSet<>();
	private IOverviewListener overviewListener;

	public OverviewSupport() {

		initializeTopics();
	}

	public boolean isUpdateTopic(String topic) {

		return topics.contains(topic);
	}

	public boolean process(List<Object> objects, String topic) {

		/*
		 * 0 => because only one property was used to register the event.
		 */
		if(!objects.isEmpty()) {
			Object object = objects.get(0);
			if(object instanceof IChromatogramOverview chromatogramOverview) {
				return fireUpdate(chromatogramOverview);
			} else if(object instanceof IChromatogramSelection) {
				IChromatogramSelection chromatogramSelection = (IChromatogramSelection)object;
				return updateChromatogramSelection(chromatogramSelection);
			} else if(object instanceof File file) {
				return updateFile(file, topic);
			} else {
				if(topic.equals(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_NONE)) {
					return fireUpdate(null);
				}
			}
		}
		//
		return false;
	}

	public void setOverviewListener(IOverviewListener overviewListener) {

		this.overviewListener = overviewListener;
	}

	private boolean updateChromatogramSelection(IChromatogramSelection chromatogramSelection) {

		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		if(chromatogram != null) {
			return fireUpdate(chromatogram);
		}
		//
		return false;
	}

	private boolean updateFile(File file, String topic) {

		if(!file.getAbsolutePath().equals(lastDisplayedFile)) {
			/*
			 * Only load the overview if it is a new file.
			 */
			lastDisplayedFile = file.getAbsolutePath();
			//
			if(topic.equals(IChemClipseEvents.TOPIC_SCAN_NMR_UPDATE_RAWFILE)) {
				/*
				 * NMR
				 */
				IProcessingInfo<?> processingInfo = ScanConverterNMR.convert(file, new NullProgressMonitor());
				Object data = processingInfo.getProcessingResult();
				if(data instanceof List<?> list) {
					if(!list.isEmpty()) {
						/*
						 * IComplexSignalMeasurement<?>
						 */
						return fireUpdate(list.get(0));
					}
				}
			} else if(topic.equals(IChemClipseEvents.TOPIC_SCAN_VSD_UPDATE_RAWFILE)) {
				/*
				 * Vibrational Spectroscopy (IR, Raman)
				 */
				IProcessingInfo<?> processingInfo = ScanConverterVSD.convert(file, new NullProgressMonitor());
				Object data = processingInfo.getProcessingResult();
				if(data instanceof IMeasurementInfo) {
					return fireUpdate(data);
				}
			} else if(topic.equals(IChemClipseEvents.TOPIC_MASS_SPECTRUM_UPDATE_RAWFILE)) {
				/*
				 * MALDI
				 */
				IProcessingInfo<?> processingInfo = MassSpectrumConverter.convert(file, new NullProgressMonitor());
				Object data = processingInfo.getProcessingResult();
				if(data instanceof IMeasurementInfo) {
					return fireUpdate(data);
				}
			} else {
				/*
				 * MSD, CSD, WSD
				 */
				IChromatogramOverview chromatogramOverview = getChromatogramOverview(file, topic);
				if(chromatogramOverview != null) {
					return fireUpdate(chromatogramOverview);
				}
			}
		}
		//
		return false;
	}

	private IChromatogramOverview getChromatogramOverview(File file, String topic) {

		IChromatogramOverview chromatogramOverview = null;
		IProcessingInfo<IChromatogramOverview> processingInfo = null;
		switch(topic) {
			case IChemClipseEvents.TOPIC_CHROMATOGRAM_MSD_UPDATE_RAWFILE:
				processingInfo = ChromatogramConverterMSD.getInstance().convertOverview(file, new NullProgressMonitor());
				break;
			case IChemClipseEvents.TOPIC_CHROMATOGRAM_CSD_UPDATE_RAWFILE:
				processingInfo = ChromatogramConverterCSD.getInstance().convertOverview(file, new NullProgressMonitor());
				break;
			case IChemClipseEvents.TOPIC_CHROMATOGRAM_WSD_UPDATE_RAWFILE:
				processingInfo = ChromatogramConverterWSD.getInstance().convertOverview(file, new NullProgressMonitor());
				break;
		}
		//
		if(processingInfo != null) {
			chromatogramOverview = processingInfo.getProcessingResult();
		}
		//
		return chromatogramOverview;
	}

	private boolean fireUpdate(Object object) {

		if(overviewListener != null) {
			overviewListener.update(object);
			return true;
		}
		//
		return false;
	}

	private void initializeTopics() {

		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION);
		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_MSD_UPDATE_RAWFILE);
		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_CSD_UPDATE_RAWFILE);
		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_WSD_UPDATE_RAWFILE);
		topics.add(IChemClipseEvents.TOPIC_SCAN_NMR_UPDATE_RAWFILE);
		topics.add(IChemClipseEvents.TOPIC_SCAN_VSD_UPDATE_RAWFILE);
		topics.add(IChemClipseEvents.TOPIC_MASS_SPECTRUM_UPDATE_RAWFILE);
		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_MSD_UPDATE_OVERVIEW);
		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_CSD_UPDATE_OVERVIEW);
		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_WSD_UPDATE_OVERVIEW);
		topics.add(IChemClipseEvents.TOPIC_SCAN_NMR_UPDATE_OVERVIEW);
		topics.add(IChemClipseEvents.TOPIC_SCAN_VSD_UPDATE_OVERVIEW);
		topics.add(IChemClipseEvents.TOPIC_MASS_SPECTRUM_UPDATE_OVERVIEW);
		topics.add(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_NONE);
	}
}
