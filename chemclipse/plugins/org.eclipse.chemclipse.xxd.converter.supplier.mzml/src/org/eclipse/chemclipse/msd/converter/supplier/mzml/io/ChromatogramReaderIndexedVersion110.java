/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.mzml.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramReader;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.IVendorChromatogramMSD;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.IVendorScanProxy;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorChromatogramMSD;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorScanProxy;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.idx.model.v110.IndexType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.idx.model.v110.IndexedMzML;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.idx.model.v110.OffsetType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.MetadataReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlHelper;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ChromatogramType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.MzMLType;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import jakarta.xml.bind.JAXBException;

public class ChromatogramReaderIndexedVersion110 extends AbstractChromatogramReader implements IChromatogramMSDReader {

	private static final Logger logger = Logger.getLogger(ChromatogramReaderIndexedVersion110.class);

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogramMSD chromatogram = null;
		try {
			chromatogram = new VendorChromatogramMSD();
			MzMLType mzML = XmlHelper.parseFiltered(file, MzMLType.class, "mzML", "run");
			MetadataReader110.readMetadata(mzML, chromatogram);

			IndexedMzML indexedMzML = XmlHelper.parseFiltered(file, IndexedMzML.class, null, "mzML");
			for(IndexType index : indexedMzML.getIndexList().getIndex()) {
				if(index.getName().equals("chromatogram")) {
					readChromatograms(file, index.getOffset(), chromatogram);
				}
			}
		} catch(JAXBException e) {
			logger.warn(e);
		} catch(XMLStreamException e) {
			logger.warn(e);
		}
		return chromatogram;
	}

	@Override
	public IChromatogramMSD read(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogramMSD chromatogram = null;
		try {
			chromatogram = new VendorChromatogramMSD();
			chromatogram.setFile(file);
			MzMLType mzML = XmlHelper.parseFiltered(file, MzMLType.class, "mzML", "run");
			MetadataReader110.readMetadata(mzML, chromatogram);

			IndexedMzML indexedMzML = XmlHelper.parseFiltered(file, IndexedMzML.class, null, "mzML");
			int indexSize = indexedMzML.getIndexList().getCount().intValue();
			SubMonitor subMonitor = SubMonitor.convert(monitor, ConverterMessages.importChromatogramOverview, indexSize * 2);
			for(IndexType index : indexedMzML.getIndexList().getIndex()) {
				if(index.getName().equals("chromatogram")) {
					readChromatograms(file, index.getOffset(), chromatogram);
				}
				subMonitor.worked(1);
			}
			for(IndexType index : indexedMzML.getIndexList().getIndex()) {
				if(index.getName().equals("spectrum")) {
					readSpectra(index.getOffset(), file, subMonitor.newChild(1), chromatogram);
				} else {
					subMonitor.worked(1);
				}
			}
		} catch(JAXBException e) {
			logger.warn(e);
		} catch(XMLStreamException e) {
			logger.warn(e);
		}
		return chromatogram;
	}

	private void readChromatograms(File file, List<OffsetType> offsets, IVendorChromatogramMSD chromatogram) {

		for(OffsetType offset : offsets) {
			if(offset.getIdRef().equals("tic") || offset.getIdRef().equals("TIC")) {
				readTIC(file, offset.getValue(), chromatogram);
			}
		}
	}

	private void readTIC(File file, long offset, IVendorChromatogramMSD chromatogram) {

		try {
			ChromatogramType mzMLchromatogram = XmlHelper.unmarshalElementAtOffset(file, offset, ChromatogramType.class);
			chromatogram.setDataName(mzMLchromatogram.getId());
			Pair<double[], double[]> binaryData = ChromatogramMSDReaderVersion110.readBinaryData(mzMLchromatogram);
			XmlChromatogramReader.addTotalSignals(binaryData.getValue(), binaryData.getKey(), chromatogram);
		} catch(FileNotFoundException e) {
			logger.error(e);
		} catch(IOException e) {
			logger.error(e);
		} catch(JAXBException e) {
			logger.error(e);
		} catch(FactoryConfigurationError e) {
			logger.error(e);
		} catch(XMLStreamException e) {
			logger.error(e);
		}
	}

	// Replace TIC with scan proxies
	private void readSpectra(List<OffsetType> offsets, File file, IProgressMonitor monitor, IVendorChromatogramMSD chromatogram) {

		int i = 1;
		List<IVendorScanProxy> scanProxies = new ArrayList<>();
		SubMonitor subMonitor = SubMonitor.convert(monitor, ConverterMessages.importScan, offsets.size());
		for(OffsetType offset : offsets) {
			IScanMSD scan = chromatogram.getScan(i);
			if(scan == null) {
				// TODO: handle DAD spectra
				continue;
			}
			IVendorScanProxy scanProxy = new VendorScanProxy(file, offset.getValue(), chromatogram, monitor);
			scanProxy.setTotalSignal(scan.getTotalSignal());
			scanProxy.setRetentionTime(scan.getRetentionTime());
			scanProxy.setIdentifier(offset.getIdRef());
			scanProxies.add(scanProxy);
			subMonitor.worked(1);
			i++;
		}
		chromatogram.getScans().clear();
		for(IVendorScanProxy scanProxy : scanProxies) {
			chromatogram.addScan(scanProxy);
		}
	}
}
