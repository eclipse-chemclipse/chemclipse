/*******************************************************************************
 * Copyright (c) 2012, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.classifier.core.ChromatogramClassifier;
import org.eclipse.chemclipse.chromatogram.msd.classifier.core.IChromatogramClassifierSupplier;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.xxd.process.support.IProcessTypeSupplier;
import org.eclipse.core.runtime.IProgressMonitor;

public class ClassifierTypeSupplier extends AbstractProcessTypeSupplier implements IProcessTypeSupplier {

	public static final String CATEGORY = "Classifier";

	@Override
	public String getCategory() {

		return CATEGORY;
	}

	@Override
	public String getSupportedDataTypes() {

		return DataType.MSD.toString();
	}

	@Override
	public String getProcessorName(String processorId) throws Exception {

		IChromatogramClassifierSupplier classifierSupplier = ChromatogramClassifier.getChromatogramClassifierSupport().getClassifierSupplier(processorId);
		return classifierSupplier.getClassifierName();
	}

	@Override
	public String getProcessorDescription(String processorId) throws Exception {

		IChromatogramClassifierSupplier classifierSupplier = ChromatogramClassifier.getChromatogramClassifierSupport().getClassifierSupplier(processorId);
		return classifierSupplier.getDescription();
	}

	@Override
	public List<String> getPluginIds() throws Exception {

		return ChromatogramClassifier.getChromatogramClassifierSupport().getAvailableClassifierIds();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IProcessingInfo applyProcessor(IChromatogramSelection chromatogramSelection, String processorId, IProgressMonitor monitor) {

		IProcessingInfo processingInfo;
		if(chromatogramSelection instanceof IChromatogramSelectionMSD) {
			IChromatogramSelectionMSD chromatogramSelectionMSD = (IChromatogramSelectionMSD)chromatogramSelection;
			processingInfo = ChromatogramClassifier.applyClassifier(chromatogramSelectionMSD, processorId, monitor);
		} else {
			processingInfo = new ProcessingInfo();
			processingInfo.addErrorMessage(processorId, "The data is not supported by the processor.");
		}
		return processingInfo;
	}
}
