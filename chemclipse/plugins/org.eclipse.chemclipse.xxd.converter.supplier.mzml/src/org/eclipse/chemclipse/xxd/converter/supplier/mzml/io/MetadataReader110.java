/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.mzml.io;

import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.support.history.EditInformation;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.CVParamType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.DataProcessingType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.InstrumentConfigurationType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.MzMLType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ParamGroupType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ProcessingMethodType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ReferenceableParamGroupRefType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SampleListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SampleType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SoftwareType;

public class MetadataReader110 {

	public static IChromatogram readMetadata(MzMLType mzML, IChromatogram chromatogram) {

		for(ParamGroupType contact : mzML.getFileDescription().getContact()) {
			for(CVParamType cvParam : contact.getCvParam()) {
				if(chromatogram.getOperator().isEmpty()) {
					chromatogram.setOperator(cvParam.getValue());
				} else {
					chromatogram.setOperator(String.join(", ", chromatogram.getOperator(), cvParam.getValue()));
				}
			}
		}

		SampleListType sampleList = mzML.getSampleList();
		if(sampleList != null) {
			for(SampleType sample : sampleList.getSample()) {
				String sampleName = getSampleName(sample);
				if(sampleName != null) {
					chromatogram.setSampleName(sampleName);
				}
			}
		}

		for(InstrumentConfigurationType instrument : mzML.getInstrumentConfigurationList().getInstrumentConfiguration()) {

			List<ReferenceableParamGroupRefType> referenceableParamGroupRef = instrument.getReferenceableParamGroupRef();
			if(referenceableParamGroupRef != null) {
				for(ReferenceableParamGroupRefType referenceableParamGroupRefType : referenceableParamGroupRef) {
					readInstrument(referenceableParamGroupRefType.getRef().getCvParam(), chromatogram);
				}
			}

			readInstrument(instrument.getCvParam(), chromatogram);
		}

		for(DataProcessingType dataProcessing : mzML.getDataProcessingList().getDataProcessing()) {
			for(ProcessingMethodType processingMethod : dataProcessing.getProcessingMethod()) {
				SoftwareType software = (SoftwareType)processingMethod.getSoftwareRef();
				for(CVParamType cvParam : processingMethod.getCvParam()) {
					String operation = cvParam.getName();
					String editor = software.getId() + " " + software.getVersion();
					chromatogram.getEditHistory().add(new EditInformation(operation, editor));
				}
			}
		}

		return chromatogram;
	}

	private static void readInstrument(List<CVParamType> cvParams, IChromatogram chromatogram) {

		for(CVParamType cvParam : cvParams) {
			if(cvParam.getAccession().equals("MS:1000529") && cvParam.getName().equals("instrument serial number")) {
				continue;
			} else {
				chromatogram.setInstrument(cvParam.getName());
			}
		}
	}

	private static String getSampleName(SampleType sample) {

		for(CVParamType cvParam : sample.getCvParam()) {
			if("MS:1000002".equals(cvParam.getAccession()) && cvParam.getValue() != null && !cvParam.getValue().isBlank()) {
				return cvParam.getValue();
			}
		}
		if(sample.getName() != null && !sample.getName().isBlank()) {
			return sample.getName();
		}
		if(sample.getId() != null && !sample.getId().isBlank()) {
			return sample.getId(); // might be distorted with XML escaping
		}
		return null;
	}
}
