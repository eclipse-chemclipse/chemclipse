/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipFile;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.columns.SeparationColumnFactory;
import org.eclipse.chemclipse.model.exceptions.ReferenceMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.io.IReaderProxy;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.IVendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.IVendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.IVendorScanProxy;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.VendorIon;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IIonTransitionSettings;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support.Format;

/**
 * Methods are copied to ensure that file formats are kept readable even if they contain errors.
 * This is suitable but I know, it's not the best way to achieve long term support for older formats.
 */
public class ReaderProxy_1301 extends AbstractZipReader implements IReaderProxy {

	private static final Logger logger = Logger.getLogger(ReaderProxy_1301.class);

	@Override
	public void readMassSpectrum(File file, int offset, IVendorScanProxy massSpectrum, IIonTransitionSettings ionTransitionSettings) throws IOException {

		ZipFile zipFile = new ZipFile(file);
		try {
			DataInputStream dataInputStream = getDataInputStream(zipFile, Format.FILE_SCANS_MSD);
			dataInputStream.skipBytes(offset);
			readMassSpectrum(massSpectrum, dataInputStream, ionTransitionSettings);
			dataInputStream.close();
		} finally {
			zipFile.close();
		}
	}

	@Override
	public void readMassSpectrum(IVendorScan massSpectrum, DataInputStream dataInputStream, IIonTransitionSettings ionTransitionSettings) throws IOException {

		short massSpectrometer = dataInputStream.readShort(); // Mass Spectrometer
		short massSpectrumType = dataInputStream.readShort(); // Mass Spectrum Type
		double precursorIon = dataInputStream.readDouble(); // Precursor Ion (0 if MS1 or none has been selected)
		massSpectrum.setMassSpectrometer(massSpectrometer);
		massSpectrum.setMassSpectrumType(getMassSpectrumType(massSpectrumType));
		massSpectrum.setPrecursorIon(precursorIon);
		readNormalMassSpectrum(massSpectrum, dataInputStream, ionTransitionSettings);
		/*
		 * Optimized Mass Spectrum
		 */
		boolean readOptimizedMassSpectrum = dataInputStream.readBoolean();
		if(readOptimizedMassSpectrum) {
			IScanMSD optimizedMassSpectrum = new ScanMSD();
			readNormalMassSpectrum(optimizedMassSpectrum, dataInputStream, ionTransitionSettings);
			massSpectrum.setOptimizedMassSpectrum(optimizedMassSpectrum);
		}
	}

	private void readNormalMassSpectrum(IScanMSD massSpectrum, DataInputStream dataInputStream, IIonTransitionSettings ionTransitionSettings) throws IOException {

		int retentionTime = dataInputStream.readInt();
		int relativeRetentionTime = dataInputStream.readInt();
		int retentionTimeColumn1 = dataInputStream.readInt();
		int retentionTimeColumn2 = dataInputStream.readInt();
		float retentionIndex = dataInputStream.readFloat(); // Retention Index
		if(dataInputStream.readBoolean()) {
			int size = dataInputStream.readInt();
			for(int i = 0; i < size; i++) {
				SeparationColumnType separationColumnType = SeparationColumnFactory.getSeparationColumnType(readString(dataInputStream));
				float retentionIndexAdditional = dataInputStream.readFloat();
				massSpectrum.setRetentionIndex(separationColumnType, retentionIndexAdditional);
			}
		}
		int timeSegmentId = dataInputStream.readInt(); // Time Segment Id
		int cycleNumber = dataInputStream.readInt(); // Cycle Number
		massSpectrum.setRetentionTime(retentionTime);
		massSpectrum.setRelativeRetentionTime(relativeRetentionTime);
		massSpectrum.setRetentionTimeColumn1(retentionTimeColumn1);
		massSpectrum.setRetentionTimeColumn2(retentionTimeColumn2);
		massSpectrum.setRetentionIndex(retentionIndex);
		massSpectrum.setTimeSegmentId(timeSegmentId);
		massSpectrum.setCycleNumber(cycleNumber);
		//
		int numberOfIons = dataInputStream.readInt(); // Number of ions
		for(int i = 1; i <= numberOfIons; i++) {
			/*
			 * Read Ions
			 */
			IVendorIon ion = readIon(dataInputStream, ionTransitionSettings);
			massSpectrum.addIon(ion);
		}
		/*
		 * Identification Results
		 */
		readScanIdentificationTargets(dataInputStream, massSpectrum);
	}

	private IVendorIon readIon(DataInputStream dataInputStream, IIonTransitionSettings ionTransitionSettings) throws IOException {

		IVendorIon ion;
		//
		double mz = dataInputStream.readDouble(); // m/z
		float abundance = dataInputStream.readFloat(); // Abundance
		/*
		 * Ion Transition
		 */
		int transition = dataInputStream.readInt();
		if(transition == 0) {
			ion = new VendorIon(mz, abundance);
		} else {
			/*
			 * parent m/z start, ...
			 */
			String compoundName = readString(dataInputStream); // compound name
			double filter1FirstIon = dataInputStream.readDouble(); // parent m/z start
			double filter1LastIon = dataInputStream.readDouble(); // parent m/z stop
			double filter3FirstIon = dataInputStream.readDouble(); // daughter m/z start
			double filter3LastIon = dataInputStream.readDouble(); // daughter m/z stop
			double collisionEnergy = dataInputStream.readDouble(); // collision energy
			double filter1Resolution = dataInputStream.readDouble(); // q1 resolution
			double filter3Resolution = dataInputStream.readDouble(); // q3 resolution
			int transitionGroup = dataInputStream.readInt(); // transition group
			int dwell = dataInputStream.readInt(); // dwell
			//
			IIonTransition ionTransition = ionTransitionSettings.getIonTransition(compoundName, filter1FirstIon, filter1LastIon, filter3FirstIon, filter3LastIon, collisionEnergy, filter1Resolution, filter3Resolution, transitionGroup);
			ionTransition.setDwell(dwell);
			ion = new VendorIon(mz, abundance, ionTransition);
		}
		return ion;
	}

	private void readScanIdentificationTargets(DataInputStream dataInputStream, IScanMSD scanMSD) throws IOException {

		int numberOfTargets = dataInputStream.readInt();
		for(int i = 1; i <= numberOfTargets; i++) {
			//
			String identifier = readString(dataInputStream); // Identifier
			boolean manuallyVerified = dataInputStream.readBoolean();
			//
			int retentionTime = dataInputStream.readInt();
			float retentionIndex = dataInputStream.readFloat();
			String casNumber = readString(dataInputStream); // CAS-Number
			String comments = readString(dataInputStream); // Comments
			String referenceIdentifier = readString(dataInputStream);
			String miscellaneous = readString(dataInputStream); // Miscellaneous
			String database = readString(dataInputStream);
			String contributor = readString(dataInputStream);
			String name = readString(dataInputStream); // Name
			Set<String> synonyms = new HashSet<>(); // Synonyms
			int numberOfSynonyms = dataInputStream.readInt();
			for(int j = 0; j < numberOfSynonyms; j++) {
				synonyms.add(readString(dataInputStream));
			}
			String formula = readString(dataInputStream); // Formula
			String smiles = readString(dataInputStream); // SMILES
			String inChI = readString(dataInputStream); // InChI
			double molWeight = dataInputStream.readDouble(); // Mol Weight
			float matchFactor = dataInputStream.readFloat(); // Match Factor
			float matchFactorDirect = dataInputStream.readFloat(); // Match Factor Direct
			float reverseMatchFactor = dataInputStream.readFloat(); // Reverse Match Factor
			float reverseMatchFactorDirect = dataInputStream.readFloat(); // Reverse Match Factor Direct
			float probability = dataInputStream.readFloat(); // Probability
			boolean isMatch = dataInputStream.readBoolean();
			//
			ILibraryInformation libraryInformation = new LibraryInformation();
			libraryInformation.setRetentionTime(retentionTime);
			libraryInformation.setRetentionIndex(retentionIndex);
			libraryInformation.setCasNumber(casNumber);
			libraryInformation.setComments(comments);
			libraryInformation.setReferenceIdentifier(referenceIdentifier);
			libraryInformation.setMiscellaneous(miscellaneous);
			libraryInformation.setDatabase(database);
			libraryInformation.setContributor(contributor);
			libraryInformation.setName(name);
			libraryInformation.setSynonyms(synonyms);
			libraryInformation.setFormula(formula);
			libraryInformation.setSmiles(smiles);
			libraryInformation.setInChI(inChI);
			libraryInformation.setMolWeight(molWeight);
			//
			IComparisonResult comparisonResult = new ComparisonResult(matchFactor, reverseMatchFactor, matchFactorDirect, reverseMatchFactorDirect, probability);
			comparisonResult.setMatch(isMatch);
			//
			try {
				IIdentificationTarget identificationEntry = new IdentificationTarget(libraryInformation, comparisonResult);
				identificationEntry.setIdentifier(identifier);
				identificationEntry.setVerified(manuallyVerified);
				scanMSD.getTargets().add(identificationEntry);
			} catch(ReferenceMustNotBeNullException e) {
				logger.warn(e);
			}
		}
	}

	private MassSpectrumType getMassSpectrumType(short massSpectrumType) {

		MassSpectrumType type = MassSpectrumType.CENTROID;
		if(massSpectrumType == 1) {
			type = MassSpectrumType.PROFILE;
		}
		return type;
	}
}
