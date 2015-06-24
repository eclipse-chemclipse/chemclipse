/*******************************************************************************
 * Copyright (c) 2013, 2015 Dr. Philip Wenig.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.quantitation.supplier.chemclipse.ui.internal.wizards;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;

import org.eclipse.chemclipse.model.targets.IPeakTarget;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.quantitation.IQuantitationCompoundMSD;
import org.eclipse.chemclipse.msd.model.core.quantitation.IQuantitationPeakMSD;
import org.eclipse.chemclipse.msd.model.implementation.QuantitationCompoundMSD;
import org.eclipse.chemclipse.msd.model.implementation.QuantitationPeakMSD;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.supplier.chemclipse.database.IQuantDatabase;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.supplier.chemclipse.database.documents.IQuantitationCompoundDocument;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.supplier.chemclipse.exceptions.QuantitationCompoundAlreadyExistsException;
import org.eclipse.chemclipse.logging.core.Logger;

public class AddPeakToQuantitationTableWizard extends Wizard {

	private static final Logger logger = Logger.getLogger(AddPeakToQuantitationTableWizard.class);
	private SelectDocumentPage documentPage;
	private IQuantDatabase database;
	private IChromatogramPeakMSD chromatogramPeakMSD;
	private NumberFormat numberFormat;

	public AddPeakToQuantitationTableWizard(IQuantDatabase database, IChromatogramPeakMSD chromatogramPeakMSD) {

		setNeedsProgressMonitor(true);
		setWindowTitle("Add Peak to Quantitation Table");
		this.database = database;
		this.chromatogramPeakMSD = chromatogramPeakMSD;
		this.numberFormat = NumberFormat.getInstance();
	}

	@Override
	public void addPages() {

		List<String> peakTargetNames = getPeakTargetNames(chromatogramPeakMSD);
		documentPage = new SelectDocumentPage("CreateDoc", peakTargetNames, database);
		addPage(documentPage);
	}

	@Override
	public boolean performFinish() {

		removeErrorMessage();
		if(database == null) {
			showErrorMessage("Please select a valid database.");
			return false;
		} else {
			/*
			 * The database must be not null.
			 */
			if(documentPage.buttonMerge.getSelection()) {
				return checkAndMergeQuantitationDocument();
			} else {
				return checkAndCreateNewQuantitationDocument();
			}
		}
	}

	private boolean checkAndMergeQuantitationDocument() {

		/*
		 * Merge the peak with the existing quantitation document.
		 */
		String name = documentPage.comboQuantitationCompoundNames.getText().trim();
		IQuantitationCompoundDocument quantitationCompoundDocument = database.getQuantitationCompoundDocument(name);
		if(quantitationCompoundDocument != null) {
			try {
				double concentration = numberFormat.parse(documentPage.textConcentrationMerge.getText().trim()).doubleValue();
				if(concentration > 0) {
					String concentrationUnit = quantitationCompoundDocument.getConcentrationUnit();
					long quantitationCompoundId = quantitationCompoundDocument.getDocumentId();
					IQuantitationPeakMSD quantitationPeakMSD = new QuantitationPeakMSD(chromatogramPeakMSD, concentration, concentrationUnit);
					database.createQuantitationPeakDocument(quantitationPeakMSD, quantitationCompoundId);
					/*
					 * Return true, cause all checks are valid.
					 */
					return true;
					//
				} else {
					showErrorMessage("Please select a valid concentration.");
				}
			} catch(ParseException e) {
				logger.warn(e);
				showErrorMessage("Please type in a valid concentration.");
			}
		} else {
			showErrorMessage("An existing quantitation compound must be selected.");
		}
		return false;
	}

	private boolean checkAndCreateNewQuantitationDocument() {

		/*
		 * Create a new quantitation document.
		 */
		String name = documentPage.comboPeakTargetNames.getText().trim();
		if(name == null || name.equals("")) {
			showErrorMessage("Please select a quantitation compound name.");
		} else {
			/*
			 * Selected document
			 */
			boolean documentStillExists = database.isQuantitationCompoundAlreadyAvailable(name);
			if(documentStillExists) {
				showErrorMessage("The quantitation compound still exists in the database.");
			} else {
				/*
				 * Concentration Unit
				 */
				String concentrationUnit = documentPage.textConcentrationUnitCreate.getText().trim();
				if(concentrationUnit == null || concentrationUnit.equals("")) {
					showErrorMessage("Please select a concentration unit.");
				} else {
					/*
					 * Concentration
					 */
					try {
						double concentration = numberFormat.parse(documentPage.textConcentrationCreate.getText().trim()).doubleValue();
						if(concentration > 0) {
							int retentionTime = chromatogramPeakMSD.getPeakModel().getRetentionTimeAtPeakMaximum();
							String chemicalClass = documentPage.textChemicalClassCreate.getText().trim();
							//
							IQuantitationCompoundMSD quantitationCompoundMSD = new QuantitationCompoundMSD(name, concentrationUnit, retentionTime);
							quantitationCompoundMSD.setChemicalClass(chemicalClass);
							quantitationCompoundMSD.getRetentionTimeWindow().setAllowedNegativeDeviation(1500);
							quantitationCompoundMSD.getRetentionIndexWindow().setAllowedPositiveDeviation(1500);
							quantitationCompoundMSD.setUseTIC(true);
							try {
								long quantitationCompoundId = database.createQuantitationCompoundDocument(quantitationCompoundMSD);
								//
								IQuantitationPeakMSD quantitationPeakMSD = new QuantitationPeakMSD(chromatogramPeakMSD, concentration, concentrationUnit);
								database.createQuantitationPeakDocument(quantitationPeakMSD, quantitationCompoundId);
								/*
								 * Return true, cause all checks are valid.
								 */
								return true;
								//
							} catch(QuantitationCompoundAlreadyExistsException e) {
								logger.warn(e);
							}
						} else {
							showErrorMessage("Please select a valid concentration.");
						}
					} catch(ParseException e1) {
						logger.warn(e1);
						showErrorMessage("Please type in a valid concentration.");
					}
				}
			}
		}
		return false;
	}

	private void showErrorMessage(String message) {

		documentPage.setErrorMessage(message);
	}

	private void removeErrorMessage() {

		documentPage.setErrorMessage(null);
	}

	/**
	 * Returns the peak target names.
	 * 
	 * @param chromatogramPeakMSD
	 * @return List<String>
	 */
	private List<String> getPeakTargetNames(IChromatogramPeakMSD chromatogramPeakMSD) {

		List<String> peakTargetNames = new ArrayList<String>();
		if(chromatogramPeakMSD != null) {
			//
			List<IPeakTarget> peakTargets = chromatogramPeakMSD.getTargets();
			if(peakTargets.size() > 0) {
				/*
				 * Get the name of the stored identification entry.
				 */
				for(IPeakTarget peakTarget : peakTargets) {
					if(peakTarget instanceof IPeakTarget) {
						IPeakTarget peakIdentificationEntry = (IPeakTarget)peakTarget;
						String name = peakIdentificationEntry.getLibraryInformation().getName();
						peakTargetNames.add(name);
					}
				}
			}
		}
		return peakTargetNames;
	}
}
