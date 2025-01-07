/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.io.IMassSpectraWriter;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverter;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverterSupport;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.DataProcessing;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.MsRun;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.MzXML;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.ParentFile;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.Peaks;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.Scan;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.Software;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.io.MassSpectrumWriter;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Version;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class MassSpectrumWriterVersion22 implements IMassSpectraWriter {

	public static final String VERSION = "mzXML_2.2";
	//
	private static final Logger logger = Logger.getLogger(MassSpectrumWriter.class);

	@Override
	public void write(File file, IScanMSD massSpectrum, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		writeMassSpectrum(file, massSpectrum);
	}

	@Override
	public void write(File file, IMassSpectra massSpectra, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		writeMassSpectra(file, massSpectra, monitor);
	}

	private void writeMassSpectra(File file, IMassSpectra massSpectra, IProgressMonitor monitor) throws IOException {

		for(int i = 1; i <= massSpectra.size(); i++) {
			IScanMSD massSpectrum = massSpectra.getMassSpectrum(i);
			if(massSpectrum != null && massSpectrum.getNumberOfIons() > 0) {
				writeMassSpectrum(file, massSpectrum);
			}
		}
	}

	private void writeMassSpectrum(File file, IScanMSD scanMSD) {

		try {
			writeMzXML(file, scanMSD);
		} catch(JAXBException e) {
			logger.warn(e);
		}
	}

	private void writeMzXML(File file, IScanMSD scanMSD) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		MzXML mzXML = new MzXML();
		mzXML.setMsRun(createMsRun(file, scanMSD));
		marshaller.marshal(mzXML, file);
	}

	private MsRun createMsRun(File file, IScanMSD scanMSD) {

		MsRun msRun = new MsRun();
		if(scanMSD instanceof IRegularMassSpectrum regularMassSpectrum) {
			msRun.getDataProcessing().add(createDataProcessing(regularMassSpectrum));
		}
		msRun.setScanCount(BigInteger.valueOf(1));
		msRun.getParentFile().add(createParentFile(file));
		msRun.getScan().add(createScan(scanMSD));
		return msRun;
	}

	private DataProcessing createDataProcessing(IRegularMassSpectrum regularMassSpectrum) {

		DataProcessing dataProcessing = new DataProcessing();
		dataProcessing.setCentroided(regularMassSpectrum.getMassSpectrumType() == MassSpectrumType.CENTROID);
		dataProcessing.setSoftware(createSoftware());
		return dataProcessing;
	}

	private Software createSoftware() {

		Software software = new Software();
		IProduct product = Platform.getProduct();
		if(product != null) {
			software.setName(product.getName());
			Version version = product.getDefiningBundle().getVersion();
			software.setVersion(version.getMajor() + "." + version.getMinor() + "." + version.getMicro());
		}
		return software;
	}

	private ParentFile createParentFile(File file) {

		ParentFile parentFile = new ParentFile();
		parentFile.setFileName(file.getName());
		MassSpectrumConverterSupport converterSupport = MassSpectrumConverter.getMassSpectrumConverterSupport();
		try {
			List<String> availableConverterIds = converterSupport.getAvailableConverterIds(file);
			if(!availableConverterIds.isEmpty()) {
				parentFile.setFileType(availableConverterIds.get(0));
			}
		} catch(NoConverterAvailableException e) {
			logger.warn(e);
		}
		try {
			parentFile.setFileSha1(new DigestUtils(DigestUtils.getSha1Digest()).digestAsHex(file));
		} catch(IOException e) {
			logger.warn(e);
		}
		return parentFile;
	}

	private Scan createScan(IScanMSD scanMSD) {

		Scan scan = new Scan();
		scan.setNum(BigInteger.valueOf(1));
		if(scanMSD instanceof IRegularMassSpectrum regularMassSpectrum) {
			scan.setMsLevel(BigInteger.valueOf(regularMassSpectrum.getMassSpectrometer()));
			scan.setCentroided(regularMassSpectrum.getMassSpectrumType() == MassSpectrumType.CENTROID);
		}
		scan.setPeaksCount(BigInteger.valueOf(scanMSD.getNumberOfIons()));
		scan.setPeaks(createPeaks(scanMSD));
		return scan;
	}

	private Peaks createPeaks(IScanMSD scanMSD) {

		Peaks peaks = new Peaks();
		peaks.setPrecision(BigInteger.valueOf(Float.BYTES));
		peaks.setByteOrder("network");
		peaks.setValue(encodeFloatArray(createValues(scanMSD)));
		return peaks;
	}

	private static float[] createValues(IScanMSD scanMSD) {

		float[] peaksArray = new float[scanMSD.getNumberOfIons() * 2];
		int i = 0;
		for(IIon ion : scanMSD.getIons()) {
			peaksArray[i] = (float)ion.getIon();
			i++;
			peaksArray[i] = ion.getAbundance();
			i++;
		}
		return peaksArray;
	}

	private static byte[] encodeFloatArray(float[] array) {

		FloatBuffer doubleBuffer = FloatBuffer.wrap(array);
		ByteBuffer byteBuffer = ByteBuffer.allocate(doubleBuffer.capacity() * Float.BYTES);
		byteBuffer.order(ByteOrder.BIG_ENDIAN);
		byteBuffer.asFloatBuffer().put(doubleBuffer);
		return byteBuffer.array();
	}
}
