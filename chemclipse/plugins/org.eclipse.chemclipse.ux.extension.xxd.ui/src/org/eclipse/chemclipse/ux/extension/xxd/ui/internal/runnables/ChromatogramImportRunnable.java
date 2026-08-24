/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics, Logging
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.runnables;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.chemclipse.csd.converter.chromatogram.ChromatogramConverterCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.dsd.converter.chromatogram.ChromatogramConverterDSD;
import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.chemclipse.csd.model.core.selection.ChromatogramSelectionCSD;
import org.eclipse.chemclipse.fsd.converter.chromatogram.ChromatogramConverterFSD;
import org.eclipse.chemclipse.fsd.model.core.IChromatogramFSD;
import org.eclipse.chemclipse.fsd.model.core.selection.ChromatogramSelectionFSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoPartSupport;
import org.eclipse.chemclipse.vsd.converter.chromatogram.ChromatogramConverterVSD;
import org.eclipse.chemclipse.vsd.model.core.IChromatogramVSD;
import org.eclipse.chemclipse.vsd.model.core.selection.ChromatogramSelectionVSD;
import org.eclipse.chemclipse.wsd.converter.chromatogram.ChromatogramConverterWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.selection.ChromatogramSelectionWSD;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class ChromatogramImportRunnable implements IRunnableWithProgress {

	private static final Logger logger = Logger.getLogger(ChromatogramImportRunnable.class);

	private List<File> files = new ArrayList<>();
	private DataType dataType;
	private String supplierId;
	private List<IChromatogramSelection> chromatogramSelections = new ArrayList<>();

	public ChromatogramImportRunnable(File file, DataType dataType) {

		this(file, dataType, null);
	}

	public ChromatogramImportRunnable(File file, DataType dataType, String supplierId) {

		this.files.addAll(Arrays.asList(file));
		this.dataType = dataType;
		this.supplierId = supplierId;
	}

	public ChromatogramImportRunnable(List<File> files, DataType dataType) {

		this.files.addAll(files);
		this.dataType = dataType;
	}

	public IChromatogramSelection getChromatogramSelection() {

		if(!chromatogramSelections.isEmpty()) {
			return chromatogramSelections.get(0);
		} else {
			return null;
		}
	}

	public List<IChromatogramSelection> getChromatogramSelections() {

		return chromatogramSelections;
	}

	@Override
	public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		try {
			/*
			 * Don't fire an update.
			 */
			monitor.beginTask("Import Chromatograms", IProgressMonitor.UNKNOWN);
			for(File file : files) {
				boolean fireUpdate = false;
				switch(dataType) {
					case MSD_NOMINAL:
					case MSD_TANDEM:
					case MSD_HIGHRES:
					case MSD:
						IProcessingInfo<IChromatogramMSD> processingInfoMSD;
						if(supplierId != null) {
							processingInfoMSD = ChromatogramConverterMSD.getInstance().convert(file, supplierId, monitor);
						} else {
							processingInfoMSD = ChromatogramConverterMSD.getInstance().convert(file, monitor);
						}
						ProcessingInfoPartSupport.getInstance().update(processingInfoMSD);
						IChromatogramMSD chromatogramMSD = processingInfoMSD.getProcessingResult();
						chromatogramSelections.add(new ChromatogramSelectionMSD(chromatogramMSD, fireUpdate));
						break;
					case CSD:
						IProcessingInfo<IChromatogramCSD> processingInfoCSD;
						if(supplierId != null) {
							processingInfoCSD = ChromatogramConverterCSD.getInstance().convert(file, supplierId, monitor);
						} else {
							processingInfoCSD = ChromatogramConverterCSD.getInstance().convert(file, monitor);
						}
						ProcessingInfoPartSupport.getInstance().update(processingInfoCSD);
						IChromatogramCSD chromatogramCSD = processingInfoCSD.getProcessingResult();
						chromatogramSelections.add(new ChromatogramSelectionCSD(chromatogramCSD, fireUpdate));
						break;
					case WSD:
						IProcessingInfo<IChromatogramWSD> processingInfoWSD;
						if(supplierId != null) {
							processingInfoWSD = ChromatogramConverterWSD.getInstance().convert(file, supplierId, monitor);
						} else {
							processingInfoWSD = ChromatogramConverterWSD.getInstance().convert(file, monitor);
						}
						ProcessingInfoPartSupport.getInstance().update(processingInfoWSD);
						IChromatogramWSD chromatogramWSD = processingInfoWSD.getProcessingResult();
						chromatogramSelections.add(new ChromatogramSelectionWSD(chromatogramWSD, fireUpdate));
						break;
					case DSD:
						IProcessingInfo<IChromatogramDSD> processingInfoDSD;
						if(supplierId != null) {
							processingInfoDSD = ChromatogramConverterDSD.getInstance().convert(file, supplierId, monitor);
						} else {
							processingInfoDSD = ChromatogramConverterDSD.getInstance().convert(file, monitor);
						}
						ProcessingInfoPartSupport.getInstance().update(processingInfoDSD);
						IChromatogramDSD chromatogramDSD = processingInfoDSD.getProcessingResult();
						chromatogramSelections.add(new ChromatogramSelectionWSD(chromatogramDSD, fireUpdate));
						break;
					case VSD:
						IProcessingInfo<IChromatogramVSD> processingInfoVSD;
						if(supplierId != null) {
							processingInfoVSD = ChromatogramConverterVSD.getInstance().convert(file, supplierId, monitor);
						} else {
							processingInfoVSD = ChromatogramConverterVSD.getInstance().convert(file, monitor);
						}
						ProcessingInfoPartSupport.getInstance().update(processingInfoVSD);
						IChromatogramVSD chromatogramVSD = processingInfoVSD.getProcessingResult();
						chromatogramSelections.add(new ChromatogramSelectionVSD(chromatogramVSD, fireUpdate));
						break;
					case FSD:
						IProcessingInfo<IChromatogramFSD> processingInfoFSD;
						if(supplierId != null) {
							processingInfoFSD = ChromatogramConverterFSD.getInstance().convert(file, supplierId, monitor);
						} else {
							processingInfoFSD = ChromatogramConverterFSD.getInstance().convert(file, monitor);
						}
						ProcessingInfoPartSupport.getInstance().update(processingInfoFSD);
						IChromatogramFSD chromatogramFSD = processingInfoFSD.getProcessingResult();
						chromatogramSelections.add(new ChromatogramSelectionFSD(chromatogramFSD, fireUpdate));
						break;
					default:
						// No action
				}
			}
		} catch(Exception e) {
			logger.error(e);
		} finally {
			monitor.done();
		}
	}
}
