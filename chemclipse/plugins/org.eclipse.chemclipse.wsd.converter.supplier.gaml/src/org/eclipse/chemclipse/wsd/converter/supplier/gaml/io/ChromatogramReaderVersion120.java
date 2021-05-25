/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.gaml.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.converter.exceptions.FileIsEmptyException;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotReadableException;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramReader;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.wsd.converter.io.IChromatogramWSDReader;
import org.eclipse.chemclipse.wsd.converter.supplier.gaml.model.IVendorChromatogram;
import org.eclipse.chemclipse.wsd.converter.supplier.gaml.model.IVendorScanSignalWSD;
import org.eclipse.chemclipse.wsd.converter.supplier.gaml.model.VendorChromatogram;
import org.eclipse.chemclipse.wsd.converter.supplier.gaml.model.VendorScan;
import org.eclipse.chemclipse.wsd.converter.supplier.gaml.model.VendorScanSignalWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.io.IConstants;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Coordinates;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Experiment;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.GAML;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Link;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Parameter;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Technique;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Trace;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Units;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Xdata;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.v120.model.Ydata;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.io.Reader120;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ChromatogramReaderVersion120 extends AbstractChromatogramReader implements IChromatogramWSDReader {

	private static final Logger logger = Logger.getLogger(ChromatogramReaderVersion120.class);
	private String contextPath;

	public ChromatogramReaderVersion120(String contextPath) {

		this.contextPath = contextPath;
	}

	@Override
	public IChromatogramWSD read(File file, IProgressMonitor monitor) throws FileNotFoundException, FileIsNotReadableException, FileIsEmptyException, IOException {

		List<VendorChromatogram> chromatograms = getChromatograms(file);
		if(chromatograms.isEmpty())
			return null;
		VendorChromatogram chromatogram = chromatograms.get(0);
		chromatograms.stream().skip(1).forEach(chromatogram::addReferencedChromatogram);
		return chromatogram;
	}

	List<VendorChromatogram> getChromatograms(File file) {

		List<VendorChromatogram> chromatograms = new ArrayList<VendorChromatogram>();
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList nodeList = document.getElementsByTagName(IConstants.NODE_GAML);
			JAXBContext jaxbContext = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			GAML gaml = (GAML)unmarshaller.unmarshal(nodeList.item(0));
			for(Experiment experiment : gaml.getExperiment()) {
				VendorChromatogram chromatogram = new VendorChromatogram();
				chromatogram.setDataName(experiment.getName());
				XMLGregorianCalendar collectDate = experiment.getCollectdate();
				if(collectDate != null)
					chromatogram.setDate(collectDate.toGregorianCalendar().getTime());
				chromatogram.setConverterId("");
				chromatogram.setFile(file);
				for(Parameter parameter : experiment.getParameter()) {
					if(parameter.getName().equals("limsID"))
						chromatogram.setBarcode(parameter.getValue());
				}
				Map<Object, List<double[]>> linkedAbsorbancesPerWaveLength = new LinkedHashMap<Object, List<double[]>>();
				Map<Object, double[]> linkedWaveLengths = new HashMap<Object, double[]>();
				Map<Object, double[]> linkedRetentionTimes = new HashMap<Object, double[]>();
				Map<Object, Units> linkedUnits = new HashMap<Object, Units>();
				for(Trace trace : experiment.getTrace()) {
					List<double[]> absorbancesPerWaveLength = new ArrayList<double[]>();
					if(trace.getTechnique() == Technique.CHROM) {
						for(Xdata xdata : trace.getXdata()) {
							Units unit = xdata.getUnits();
							double[] retentionTimes = Reader120.parseValues(xdata.getValues());
							for(Link link : xdata.getLink()) {
								linkedRetentionTimes.put(link.getLinkref(), retentionTimes);
								linkedUnits.put(link.getLinkref(), unit);
							}
						}
					}
					if(trace.getTechnique() == Technique.PDA) {
						double[] wavelengths = null;
						for(Xdata xdata : trace.getXdata()) {
							Units unit = xdata.getUnits();
							if(unit == Units.NANOMETERS) {
								wavelengths = Reader120.parseValues(xdata.getValues());
								for(Coordinates coordinate : trace.getCoordinates()) {
									linkedWaveLengths.put(coordinate, wavelengths);
								}
							}
							for(Ydata ydata : xdata.getYdata()) {
								absorbancesPerWaveLength.add(Reader120.parseValues(ydata.getValues()));
							}
							for(Coordinates coordinate : trace.getCoordinates()) {
								linkedAbsorbancesPerWaveLength.put(coordinate, absorbancesPerWaveLength);
							}
						}
					}
				}
				Object link = linkedAbsorbancesPerWaveLength.keySet().iterator().next();
				List<double[]> absorbancesPerWaveLength = linkedAbsorbancesPerWaveLength.get(link);
				double[] retentionTimes = linkedRetentionTimes.get(link);
				double[] waveLengths = linkedWaveLengths.get(link);
				Units unit = linkedUnits.get(link);
				int rt = 0;
				for(double[] absorbances : absorbancesPerWaveLength) {
					VendorScan scan = new VendorScan();
					scan.setRetentionTime(Reader120.convertToMiliSeconds(retentionTimes[rt], unit));
					for(int a = 0; a < absorbances.length; a++) {
						IVendorScanSignalWSD scanSignal = new VendorScanSignalWSD();
						scanSignal.setAbundance((float)absorbances[a]);
						scanSignal.setWavelength(waveLengths[a]);
						scan.addScanSignal(scanSignal);
					}
					rt++;
					chromatogram.addScan(scan);
				}
				chromatograms.add(chromatogram);
			}
		} catch(SAXException e) {
			logger.warn(e);
		} catch(JAXBException e) {
			logger.warn(e);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}
		return chromatograms;
	}

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws FileNotFoundException, FileIsNotReadableException, FileIsEmptyException, IOException {

		IVendorChromatogram chromatogram = null;
		//
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList nodeList = document.getElementsByTagName(IConstants.NODE_GAML);
			//
			JAXBContext jaxbContext = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			GAML gaml = (GAML)unmarshaller.unmarshal(nodeList.item(0));
			//
			chromatogram = new VendorChromatogram();
			//
			Experiment experiment = gaml.getExperiment().get(0);
			chromatogram.setDataName(experiment.getName());
			chromatogram.setDate(experiment.getCollectdate().toGregorianCalendar().getTime());
		} catch(SAXException e) {
			logger.warn(e);
		} catch(JAXBException e) {
			logger.warn(e);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		}
		return chromatogram;
	}
}
