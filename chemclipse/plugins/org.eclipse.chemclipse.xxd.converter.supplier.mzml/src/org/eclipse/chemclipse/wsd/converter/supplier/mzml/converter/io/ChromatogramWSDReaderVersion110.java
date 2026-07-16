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
package org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.io;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramReader;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.wsd.converter.io.IChromatogramWSDReader;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.IVendorChromatogramWSD;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.IVendorScanSignal;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.IVendorScanWSD;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.VendorChromatogramWSD;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.VendorScanSignal;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.VendorScanWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.BinaryReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.MetadataReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.BinaryDataArrayType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.CVParamType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ChromatogramType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.MzMLType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.RunType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SpectrumType;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramWSDReaderVersion110 extends AbstractChromatogramReader implements IChromatogramWSDReader {

	private static final Logger logger = Logger.getLogger(ChromatogramWSDReaderVersion110.class);

	private MzMLType mzML;

	public ChromatogramWSDReaderVersion110(MzMLType mzML) {

		this.mzML = mzML;
	}

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogramWSD chromatogram = null;
		chromatogram = new VendorChromatogramWSD();
		chromatogram.setFile(file);
		chromatogram = (IVendorChromatogramWSD)MetadataReader110.readMetadata(mzML, chromatogram);
		RunType run = mzML.getRun();
		readSingleWavelengthSignal(run, chromatogram);
		return chromatogram;
	}

	@Override
	public IChromatogramWSD read(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogramWSD chromatogram = null;
		chromatogram = new VendorChromatogramWSD();
		chromatogram.setFile(file);
		chromatogram = (IVendorChromatogramWSD)MetadataReader110.readMetadata(mzML, chromatogram);
		RunType run = mzML.getRun();
		readSingleWavelengthSignal(run, chromatogram);
		try {
			readFullSpectrum(run, chromatogram);
		} catch(DataFormatException e) {
			logger.error(e);
		}
		return chromatogram;
	}

	private void readFullSpectrum(RunType run, IVendorChromatogramWSD chromatogram) throws DataFormatException {

		int i = 1;
		for(SpectrumType spectrum : run.getSpectrumList().getSpectrum()) {
			if(spectrum.getCvParam().stream().noneMatch(n -> //
			("MS:1000804".equals(n.getAccession()) && "electromagnetic radiation spectrum".equals(n.getName())) || //
					("MS:1000806".equals(n.getAccession()) && "absorption spectrum".equals(n.getName())))) {
				continue;
			}
			double[] wavelengths = new double[0];
			double[] intensities = new double[0];
			for(BinaryDataArrayType binaryDataArrayType : spectrum.getBinaryDataArrayList().getBinaryDataArray()) {
				Pair<String, double[]> binaryData = BinaryReader110.parseBinaryData(binaryDataArrayType);
				if(binaryData.getKey().equals("wavelength")) {
					wavelengths = binaryData.getValue();
				} else if(binaryData.getKey().equals("intensity")) {
					intensities = binaryData.getValue();
				}
			}
			if(!chromatogram.getScans().isEmpty()) {
				IVendorScanWSD scan = (IVendorScanWSD)chromatogram.getScan(i);
				if(scan != null) {
					scan.deleteScanSignals(); // otherwise the total signal is added upon
					addSpectrum(wavelengths, intensities, scan);
				}
			}
			i++;
		}
	}

	private void readSingleWavelengthSignal(RunType run, IVendorChromatogramWSD chromatogram) {

		double[] retentionTimes = new double[0];
		double[] intensities = new double[0];
		float lowestWavelength = 0f;
		float highestWavelength = 0f;
		try {
			for(ChromatogramType chromatogramType : run.getChromatogramList().getChromatogram()) {
				for(CVParamType cvParam : chromatogramType.getCvParam()) {
					if(cvParam.getAccession().equals("MS:1000618") && cvParam.getName().equals("highest observed wavelength")) {
						highestWavelength = Float.parseFloat(cvParam.getValue());
					} else if(cvParam.getAccession().equals("MS:1000619") && cvParam.getName().equals("lowest observed wavelength")) {
						lowestWavelength = Float.parseFloat(cvParam.getValue());
					} else if(cvParam.getAccession().equals("MS:1000812") && cvParam.getName().equals("absorption chromatogram")) {
						for(BinaryDataArrayType binaryDataArrayType : chromatogramType.getBinaryDataArrayList().getBinaryDataArray()) {
							Pair<String, double[]> binaryData = BinaryReader110.parseBinaryData(binaryDataArrayType);
							if(binaryData.getKey().equals("time")) {
								retentionTimes = binaryData.getValue();
							} else if(binaryData.getKey().equals("intensity")) {
								intensities = binaryData.getValue();
							}
						}
					}
				}
			}
			if(lowestWavelength != highestWavelength) {
				logger.warn("Not a single wavelength chromatogram.");
			}
			float wavelength = Math.max(lowestWavelength, highestWavelength);
			addScans(wavelength, intensities, retentionTimes, chromatogram);
		} catch(DataFormatException e) {
			logger.warn(e);
		}
	}

	private void addScans(float wavelength, double[] intensities, double[] retentionTimes, IVendorChromatogramWSD chromatogram) {

		int rt = Math.min(retentionTimes.length, intensities.length);
		for(int i = 0; i < rt; i++) {
			IVendorScanWSD scan = new VendorScanWSD();
			int retentionTime = (int)(retentionTimes[i]);
			scan.setRetentionTime(retentionTime);
			float intensity = (float)intensities[i];
			IVendorScanSignal signal = new VendorScanSignal();
			signal.setAbsorbance(intensity);
			signal.setWavelength(wavelength);
			scan.addScanSignal(signal);
			chromatogram.addScan(scan);
		}
	}

	private void addSpectrum(double[] wavelengths, double[] intensities, IVendorScanWSD scan) {

		int max = Math.min(wavelengths.length, intensities.length);
		for(int i = 0; i < max; i++) {
			IVendorScanSignal signal = new VendorScanSignal();
			signal.setAbsorbance((float)intensities[i]);
			signal.setWavelength((float)wavelengths[i]);
			scan.addScanSignal(signal);
		}
	}
}
