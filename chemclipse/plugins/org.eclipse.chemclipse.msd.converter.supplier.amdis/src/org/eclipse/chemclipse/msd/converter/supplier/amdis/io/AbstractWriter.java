/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.io;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.exceptions.ReferenceMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.quantitation.IInternalStandard;
import org.eclipse.chemclipse.model.quantitation.IQuantitationEntry;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.model.IVendorLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.ILibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.RegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.support.text.ValueFormat;

public abstract class AbstractWriter {

	public static final String CRLF = "\r\n";
	public static final String TAB = "\t";
	public static final float NORMALIZATION_BASE = 1000.0f;
	//
	private static final Logger logger = Logger.getLogger(AbstractWriter.class);
	//
	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish();
	//
	private static final String RT = "RT: ";
	private static final String RRT = "RRT: ";
	private static final String RI = "RI: ";
	private static final String COLUMNINDEX = "COLUMNINDEX: ";
	private static final String NAME = "NAME: ";
	private static final String CASNO = "CASNO: ";
	private static final String SMILES = "SMILES: ";
	private static final String INCHI = "INCHI: ";
	private static final String INCHIKEY = "INCHIKEY: ";
	private static final String COMMENTS = "COMMENTS: ";
	private static final String SOURCE = "SOURCE: ";
	private static final String NUM_PEAKS = "NUM PEAKS: ";
	private static final String FORMULA = "FORMULA: ";
	private static final String MW = "MW: ";
	private static final String EXACT_MASS = "EXACTMASS: ";
	private static final String DB = "DB: ";
	private static final String CONTRIBUTOR = "CONTRIBUTOR: ";
	private static final String REFID = "REFID: ";
	private static final String SYNONYM = "Synon:";
	private static final String ISTD = "ISTD: ";
	private static final String QUANT = "QUANT: ";
	private static final String AREA = "AREA: ";
	//
	private static final String NO_IDENTIFIER = "NO IDENTIFIER AVAILABLE";

	/**
	 * Makes a deep copy of the mass spectrum, normalizes it and removes too low abundances.
	 * 
	 * @param massSpectrum
	 * @return {@link IScanMSD}
	 */
	protected IScanMSD getOptimizedMassSpectrum(IScanMSD massSpectrum) {

		IScanMSD optimizedMassSpectrum = getUnitOrHighMassResolutionCopy(massSpectrum);
		normalizeMassSpectrumOnDemand(optimizedMassSpectrum);
		removeLowIntensityIonsOnDemand(optimizedMassSpectrum);
		//
		return optimizedMassSpectrum;
	}

	/**
	 * Removes the ions below the given minimum abundance.
	 * 
	 * @param normalizedMassSpectrum
	 * @param minimumAbundance
	 */
	protected void removeIonsWithAnTooLowAbundance(IScanMSD normalizedMassSpectrum, float minimumAbundance) {

		List<IIon> ionsToRemove = new ArrayList<>();
		for(IIon ion : normalizedMassSpectrum.getIons()) {
			if(ion.getAbundance() < minimumAbundance) {
				ionsToRemove.add(ion);
			}
		}
		// Remove the selected ions.
		for(IIon ion : ionsToRemove) {
			normalizedMassSpectrum.removeIon(ion);
		}
	}

	protected String getSynonyms(IScanMSD massSpectrum) {

		StringBuilder builder = new StringBuilder();
		if(massSpectrum instanceof ILibraryMassSpectrum libraryMassSpectrum) {
			Set<String> synonyms = libraryMassSpectrum.getLibraryInformation().getSynonyms();
			if(!synonyms.isEmpty()) {
				for(String synonym : synonyms) {
					/*
					 * Set the synonym.
					 */
					if(!synonym.equals("")) {
						builder.append(SYNONYM);
						builder.append(synonym);
						builder.append(CRLF);
					}
				}
			}
		}
		//
		return builder.toString();
	}

	/**
	 * Returns the name information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getNameField(IScanMSD massSpectrum, IIdentificationTarget identificationTarget) {

		/*
		 * The identifier is very important when tagging files
		 * for identification processes. Hence, if an identifier
		 * is available, set it.
		 */
		String field = NAME;
		String identification = "";
		String identifier = massSpectrum.getIdentifier();
		//
		if(identifier != null && !identifier.equals("")) {
			identification = identifier;
		} else if(identificationTarget != null) {
			identification = identificationTarget.getLibraryInformation().getName();
		}
		/*
		 * Set a default name if the identifier is empty.
		 */
		if(identification.equals("")) {
			identification = NO_IDENTIFIER;
		}
		//
		return field + identification;
	}

	/**
	 * This method returns the identification target or null if there is none.
	 * 
	 * @param massSpectrum
	 * @return
	 */
	protected IIdentificationTarget getIdentificationTarget(IScanMSD massSpectrum) {

		IIdentificationTarget identificationTarget = null;
		if(massSpectrum instanceof IRegularLibraryMassSpectrum libraryMassSpectrum) {
			/*
			 * Library MS
			 */
			try {
				identificationTarget = new IdentificationTarget(libraryMassSpectrum.getLibraryInformation(), ComparisonResult.COMPARISON_RESULT_NO_MATCH);
			} catch(ReferenceMustNotBeNullException e) {
				logger.warn(e);
			}
		} else if(massSpectrum instanceof IRegularMassSpectrum) {
			/*
			 * Scan MS
			 */
			identificationTarget = IIdentificationTarget.getIdentificationTarget(massSpectrum);
		}
		//
		return identificationTarget;
	}

	protected IIdentificationTarget getPeakTarget(IPeak peak) {

		return IIdentificationTarget.getIdentificationTarget(peak);
	}

	protected void writeField(FileWriter fileWriter, String content) throws IOException {

		fileWriter.write(content + CRLF);
	}

	/**
	 * Returns the CAS number information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getCasNumberField(IIdentificationTarget identificationTarget) {

		String field = CASNO;
		if(identificationTarget != null) {
			field += identificationTarget.getLibraryInformation().getCasNumber();
		}
		//
		return field;
	}

	protected void writeCasNumberFields(FileWriter fileWriter, IIdentificationTarget identificationTarget) throws IOException {

		if(identificationTarget != null) {
			ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
			for(String casNumber : libraryInformation.getCasNumbers()) {
				writeField(fileWriter, CASNO + casNumber);
			}
		}
	}

	protected void writeSynonymsFields(FileWriter fileWriter, IIdentificationTarget identificationTarget) throws IOException {

		if(identificationTarget != null) {
			ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
			for(String synonym : libraryInformation.getSynonyms()) {
				writeField(fileWriter, SYNONYM + synonym);
			}
		}
	}

	protected void writeColumnIndicesFields(FileWriter fileWriter, IIdentificationTarget identificationTarget) throws IOException {

		if(identificationTarget != null) {
			ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
			for(IColumnIndexMarker columnIndexMarker : libraryInformation.getColumnIndexMarkers()) {
				ISeparationColumn separationColumn = columnIndexMarker.getSeparationColumn();
				StringBuilder builder = new StringBuilder();
				builder.append(COLUMNINDEX);
				builder.append(columnIndexMarker.getRetentionIndex());
				builder.append(TAB);
				builder.append(separationColumn.getName());
				builder.append(TAB);
				builder.append(separationColumn.getSeparationColumnType());
				builder.append(TAB);
				builder.append(separationColumn.getSeparationColumnPackaging());
				builder.append(TAB);
				builder.append(separationColumn.getCalculationType());
				builder.append(TAB);
				builder.append(separationColumn.getLength());
				builder.append(TAB);
				builder.append(separationColumn.getDiameter());
				builder.append(TAB);
				builder.append(separationColumn.getPhase());
				builder.append(TAB);
				builder.append(separationColumn.getThickness());
				builder.append(CRLF);
				fileWriter.write(builder.toString());
			}
		}
	}

	/**
	 * Returns the CAS number information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getSmilesField(IIdentificationTarget identificationTarget) {

		String field = SMILES;
		if(identificationTarget != null) {
			field += identificationTarget.getLibraryInformation().getSmiles();
		}
		//
		return field;
	}

	protected String getMolWeightField(IIdentificationTarget identificationTarget) {

		String field = MW;
		if(identificationTarget != null) {
			field += identificationTarget.getLibraryInformation().getMolWeight();
		}
		//
		return field;
	}

	protected String getExactMassField(IIdentificationTarget identificationTarget) {

		String field = EXACT_MASS;
		if(identificationTarget != null) {
			field += identificationTarget.getLibraryInformation().getExactMass();
		}
		//
		return field;
	}

	protected String getFormulaField(IIdentificationTarget identificationTarget) {

		String field = FORMULA;
		if(identificationTarget != null) {
			field += identificationTarget.getLibraryInformation().getFormula();
		}
		//
		return field;
	}

	protected String getInChIField(IIdentificationTarget identificationTarget) {

		String field = INCHI;
		if(identificationTarget != null) {
			field += identificationTarget.getLibraryInformation().getInChI();
		}
		//
		return field;
	}

	protected String getInChIKeyField(IIdentificationTarget identificationTarget) {

		String field = INCHIKEY;
		if(identificationTarget != null) {
			field += identificationTarget.getLibraryInformation().getInChIKey();
		}
		//
		return field;
	}

	/**
	 * Returns the comments information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getCommentsField(IScanMSD massSpectrum) {

		String field = COMMENTS;
		if(massSpectrum instanceof IRegularLibraryMassSpectrum regularMassSpectrum) {
			field += regularMassSpectrum.getLibraryInformation().getComments();
		}
		//
		return field;
	}

	/**
	 * Returns the source information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getSourceField(IScanMSD massSpectrum, IIdentificationTarget identificationTarget) {

		String field = SOURCE;
		if(massSpectrum instanceof IVendorLibraryMassSpectrum vendorMassSpectrum) {
			field += vendorMassSpectrum.getSource();
		} else {
			if(identificationTarget != null) {
				field += identificationTarget.getIdentifier();
			}
		}
		//
		return field;
	}

	/**
	 * Returns the retention time information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getRetentionTimeField(IScanMSD massSpectrum) {

		String field = RT;
		if(massSpectrum instanceof IRegularMassSpectrum regularMassSpectrum) {
			field += decimalFormat.format(regularMassSpectrum.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
		} else {
			field += decimalFormat.format(0.0d);
		}
		//
		return field;
	}

	/**
	 * Returns the retention time information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getRelativeRetentionTimeField(IScanMSD massSpectrum) {

		String field = RRT;
		if(massSpectrum instanceof IRegularMassSpectrum regularMassSpectrum) {
			field += decimalFormat.format(regularMassSpectrum.getRelativeRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
		} else {
			field += decimalFormat.format(0.0d);
		}
		//
		return field;
	}

	/**
	 * Returns the retention index information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getRetentionIndexField(IScanMSD massSpectrum) {

		String field = RI;
		if(massSpectrum instanceof IRegularMassSpectrum regularMassSpectrum) {
			field += decimalFormat.format(regularMassSpectrum.getRetentionIndex());
		} else {
			field += decimalFormat.format(0.0d);
		}
		//
		return field;
	}

	/**
	 * Returns the name information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getNumberOfPeaks(IScanMSD massSpectrum) {

		String field = NUM_PEAKS;
		field += massSpectrum.getNumberOfIons();
		//
		return field;
	}

	/**
	 * Returns the formula information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getFormulaField(IScanMSD massSpectrum) {

		String field = FORMULA;
		if(massSpectrum instanceof IRegularLibraryMassSpectrum regularMassSpectrum) {
			field += regularMassSpectrum.getLibraryInformation().getFormula();
		}
		//
		return field;
	}

	/**
	 * Returns the MW information from the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getMWField(IScanMSD massSpectrum) {

		String field = MW;
		if(massSpectrum instanceof IRegularLibraryMassSpectrum regularMassSpectrum) {
			field += regularMassSpectrum.getLibraryInformation().getMolWeight();
		}
		//
		return field;
	}

	protected String getDatabaseField(IIdentificationTarget identificationTarget) {

		String field = DB;
		if(identificationTarget != null) {
			field += identificationTarget.getLibraryInformation().getDatabase();
		}
		//
		return field;
	}

	protected String getContributorField(IIdentificationTarget identificationTarget) {

		String field = CONTRIBUTOR;
		if(identificationTarget != null) {
			field += identificationTarget.getLibraryInformation().getContributor();
		}
		//
		return field;
	}

	protected String getReferenceIdentifierField(IIdentificationTarget identificationTarget) {

		String field = REFID;
		if(identificationTarget != null) {
			field += identificationTarget.getLibraryInformation().getReferenceIdentifier();
		}
		//
		return field;
	}

	private IScanMSD getUnitOrHighMassResolutionCopy(IScanMSD massSpectrum) {

		IScanMSD optimizedMassSpectrum;
		if(PreferenceSupplier.isUseUnitMassResolution()) {
			/*
			 * Unit Mass Resolution
			 */
			IExtractedIonSignal extractedIonSignal = massSpectrum.getExtractedIonSignal();
			optimizedMassSpectrum = getMassSpectrumCopy(massSpectrum, false);
			int startIon = extractedIonSignal.getStartIon();
			int stopIon = extractedIonSignal.getStopIon();
			for(int ion = startIon; ion <= stopIon; ion++) {
				optimizedMassSpectrum.addIon(new Ion(ion, extractedIonSignal.getAbundance(ion)));
			}
		} else {
			/*
			 * High Mass Resolution
			 */
			optimizedMassSpectrum = getMassSpectrumCopy(massSpectrum, true);
		}
		//
		return optimizedMassSpectrum;
	}

	private void normalizeMassSpectrumOnDemand(IScanMSD massSpectrum) {

		if(PreferenceSupplier.isNormalizeIntensities()) {
			massSpectrum.normalize(NORMALIZATION_BASE);
		}
	}

	private void removeLowIntensityIonsOnDemand(IScanMSD massSpectrum) {

		if(PreferenceSupplier.isRemoveIntensitiesLowerThanOne()) {
			removeIonsWithAnTooLowAbundance(massSpectrum, 1.0f);
		}
	}

	private IScanMSD getMassSpectrumCopy(IScanMSD massSpectrum, boolean copyIons) {

		IRegularLibraryMassSpectrum massSpectrumCopy = new RegularLibraryMassSpectrum();
		massSpectrumCopy.setRetentionTime(massSpectrum.getRetentionTime());
		massSpectrumCopy.setRelativeRetentionTime(massSpectrum.getRelativeRetentionTime());
		massSpectrumCopy.setRetentionIndex(massSpectrum.getRetentionIndex());
		//
		massSpectrumCopy.getTargets().addAll(massSpectrum.getTargets());
		if(massSpectrum instanceof IRegularLibraryMassSpectrum regularMassSpectrum) {
			/*
			 * Transfer the library information.
			 */
			massSpectrumCopy.setLibraryInformation(regularMassSpectrum.getLibraryInformation());
		} else {
			/*
			 * Set the library information.
			 */
			ILibraryInformation libraryInformation = IIdentificationTarget.getLibraryInformation(massSpectrum);
			massSpectrumCopy.setLibraryInformation(libraryInformation);
		}
		//
		if(copyIons) {
			for(IIon ion : massSpectrum.getIons()) {
				massSpectrumCopy.addIon(new Ion(ion.getIon(), ion.getAbundance()));
			}
		}
		//
		return massSpectrumCopy;
	}

	/**
	 * Returns the mass spectra in the convenient AMDIS format.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getIonsFormatMSL(IScanMSD massSpectrum) {

		int blockSize = 5;
		int actualPosition = 1;
		boolean exportIntensityAsInteger = PreferenceSupplier.isExportIntensitiesAsInteger();
		//
		StringBuilder builder = new StringBuilder();
		List<IIon> ions = massSpectrum.getIons();
		for(IIon ion : ions) {
			/*
			 * Insert a carriage return / line feed after each block size.
			 */
			if(actualPosition > blockSize) {
				builder.append(CRLF);
				actualPosition = 1;
			}
			/*
			 * Add each ion.
			 */
			builder.append("(");
			builder.append(ion.getIon());
			builder.append(" ");
			if(exportIntensityAsInteger) {
				builder.append(AbstractIon.getAbundance(ion.getAbundance()));
			} else {
				builder.append(ion.getAbundance());
			}
			builder.append(")");
			/*
			 * The last element in the row do not need to have a whitespace at
			 * its end.
			 */
			if(actualPosition < blockSize) {
				builder.append(" ");
			}
			/*
			 * Increase the actual position.
			 */
			actualPosition++;
		}
		//
		return builder.toString();
	}

	/**
	 * Returns the mass spectra in the convenient AMDIS format.
	 * 
	 * @param massSpectrum
	 * @return String
	 */
	protected String getIonsFormatMSP(IScanMSD massSpectrum) {

		boolean exportIntensityAsInteger = PreferenceSupplier.isExportIntensitiesAsInteger();
		StringBuilder builder = new StringBuilder();
		List<IIon> ions = massSpectrum.getIons();
		for(IIon ion : ions) {
			/*
			 * Add each ion.
			 */
			builder.append(ion.getIon());
			builder.append(" ");
			if(exportIntensityAsInteger) {
				builder.append(AbstractIon.getAbundance(ion.getAbundance()));
			} else {
				builder.append(ion.getAbundance());
			}
			builder.append(";");
			builder.append(CRLF);
		}
		//
		return builder.toString();
	}

	protected String getInternalStandards(IPeakMSD peak) {

		StringBuilder builder = new StringBuilder();
		List<IInternalStandard> internalStandards = peak.getInternalStandards();
		if(!internalStandards.isEmpty()) {
			for(IInternalStandard internalStandard : internalStandards) {
				/*
				 * Set the synonym.
				 */
				builder.append(ISTD);
				builder.append(internalStandard.getName());
				builder.append(" ");
				builder.append(internalStandard.getConcentration());
				builder.append(" ");
				builder.append(internalStandard.getConcentrationUnit());
				builder.append(" ");
				builder.append(internalStandard.getCompensationFactor());
				builder.append(CRLF);
			}
		}
		//
		return builder.toString();
	}

	protected String getQuantitations(IPeakMSD peak) {

		StringBuilder builder = new StringBuilder();
		List<IQuantitationEntry> quanitationEntries = peak.getQuantitationEntries();
		if(!quanitationEntries.isEmpty()) {
			for(IQuantitationEntry quantitationEntry : quanitationEntries) {
				/*
				 * Set the synonym.
				 */
				builder.append(QUANT);
				builder.append(quantitationEntry.getName());
				builder.append(" ");
				builder.append(quantitationEntry.getConcentration());
				builder.append(" ");
				builder.append(quantitationEntry.getConcentrationUnit());
				builder.append(CRLF);
			}
		}
		//
		return builder.toString();
	}

	protected String getArea(IPeakMSD peak) {

		String field = AREA;
		field += decimalFormat.format(peak.getIntegratedArea());
		//
		return field;
	}
}
