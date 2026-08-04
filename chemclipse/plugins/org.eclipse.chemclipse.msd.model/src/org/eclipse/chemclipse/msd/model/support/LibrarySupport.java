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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.support;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.SeparationColumnFactory;
import org.eclipse.chemclipse.model.identifier.ColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.IFlavorMarker;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.RegularLibraryMassSpectrum;
import org.eclipse.chemclipse.support.model.SeparationColumnType;

public class LibrarySupport {

	public static IRegularLibraryMassSpectrum merge(List<IScanMSD> libraryEntriesSource) {

		RegularLibraryMassSpectrum libraryMassSpectrumMerged = null;
		if(libraryEntriesSource != null && !libraryEntriesSource.isEmpty()) {
			ICombinedMassSpectrum combinedMassSpectrum = getCombinedMassSpectrum(libraryEntriesSource);
			libraryMassSpectrumMerged = new RegularLibraryMassSpectrum();
			libraryMassSpectrumMerged.addIons(combinedMassSpectrum.getIons(), false);
			libraryMassSpectrumMerged.setRetentionTime(libraryEntriesSource.get(0).getRetentionTime());
			libraryMassSpectrumMerged.setRetentionIndex(libraryEntriesSource.get(0).getRetentionIndex());
			mergeLibraryInformation(libraryMassSpectrumMerged.getLibraryInformation(), libraryEntriesSource);
		}

		return libraryMassSpectrumMerged;
	}

	private static ICombinedMassSpectrum getCombinedMassSpectrum(List<IScanMSD> scans) {

		ICombinedMassSpectrumCalculator calculator = new CombinedNominalMassSpectrumCalculator();
		for(IScanMSD scan : scans) {
			for(IIon ion : scan.getIons()) {
				calculator.addIon(ion.getIon(), ion.getAbundance());
			}
		}

		return calculator.createMassSpectrum(CalculationType.SUM);
	}

	private static void mergeLibraryInformation(ILibraryInformation libraryInformationTarget, List<IScanMSD> libraryEntriesSource) {

		boolean initialize = true;
		Iterator<IScanMSD> iterator = libraryEntriesSource.iterator();
		while(iterator.hasNext()) {
			if(iterator.next() instanceof IRegularLibraryMassSpectrum libraryMassSpectrumSource) {
				if(initialize) {
					initializeLibraryData(libraryMassSpectrumSource, libraryInformationTarget);
					initialize = false;
				} else {
					mergeLibraryData(libraryMassSpectrumSource, libraryInformationTarget);
				}
			}
		}
	}

	private static void initializeLibraryData(IRegularLibraryMassSpectrum libraryMassSpectrumSource, ILibraryInformation libraryInformationTarget) {

		ILibraryInformation libraryInformationSource = libraryMassSpectrumSource.getLibraryInformation();
		libraryInformationTarget.setName(libraryInformationSource.getName());
		libraryInformationTarget.setFormula(libraryInformationSource.getFormula());
		libraryInformationTarget.setSmiles(libraryInformationSource.getSmiles());
		libraryInformationTarget.setInChI(libraryInformationSource.getInChI());
		libraryInformationTarget.setInChIKey(libraryInformationSource.getInChIKey());
		libraryInformationTarget.setMolWeight(libraryInformationSource.getMolWeight());
		libraryInformationTarget.setExactMass(libraryInformationSource.getExactMass());
		libraryInformationTarget.setComments(libraryInformationSource.getComments());
		libraryInformationTarget.setMiscellaneous(libraryInformationSource.getMiscellaneous());
		libraryInformationTarget.setReferenceIdentifier(libraryInformationSource.getReferenceIdentifier());
		libraryInformationTarget.setRetentionTime(libraryInformationSource.getRetentionTime());
		libraryInformationTarget.setDatabase(libraryInformationSource.getDatabase());
		libraryInformationTarget.setContributor(libraryInformationSource.getContributor());
		libraryInformationTarget.setCompoundClass(libraryInformationSource.getCompoundClass());
		libraryInformationTarget.setMoleculeStructure(libraryInformationSource.getMoleculeStructure());
		/*
		 * Retention Index
		 */
		float retentionIndex = libraryInformationSource.getRetentionIndex();
		if(libraryInformationTarget.getRetentionIndex() == 0) {
			if(retentionIndex > 0) {
				libraryInformationTarget.setRetentionIndex(retentionIndex);
			}
		} else {
			if(retentionIndex > 0) {
				ISeparationColumn defaultColumn = SeparationColumnFactory.getSeparationColumn(SeparationColumnType.DEFAULT);
				libraryInformationTarget.add(new ColumnIndexMarker(defaultColumn, retentionIndex));
			}
		}
		/*
		 * CAS#
		 */
		for(String cas : libraryInformationSource.getCasNumbers()) {
			libraryInformationTarget.addCasNumber(cas);
		}
		/*
		 * Synonyms
		 */
		String targetName = libraryInformationTarget.getName();
		libraryInformationSource.getSynonyms().stream().filter(s -> !s.isEmpty() && !s.equals(targetName)).forEach(libraryInformationTarget.getSynonyms()::add);
		/*
		 * Column Indices
		 */
		for(IColumnIndexMarker marker : libraryInformationSource.getColumnIndexMarkers()) {
			libraryInformationTarget.add(marker);
		}
		/*
		 * Flavor Marker
		 */
		for(IFlavorMarker marker : libraryInformationSource.getFlavorMarkers()) {
			libraryInformationTarget.add(marker);
		}
	}

	private static void mergeLibraryData(IRegularLibraryMassSpectrum libraryMassSpectrumSource, ILibraryInformation libraryInformationTarget) {

		ILibraryInformation libraryInformationSource = libraryMassSpectrumSource.getLibraryInformation();
		String targetName = libraryInformationTarget.getName();
		String sourceName = libraryInformationSource.getName();
		/*
		 * Synonyms
		 */
		if(!sourceName.isEmpty() && !sourceName.equals(targetName)) {
			libraryInformationTarget.getSynonyms().add(sourceName);
		}
		libraryInformationSource.getSynonyms().stream().filter(s -> !s.isEmpty() && !s.equals(targetName)).forEach(libraryInformationTarget.getSynonyms()::add);
		/*
		 * CAS#
		 */
		for(String cas : libraryInformationSource.getCasNumbers()) {
			if(!libraryInformationTarget.getCasNumbers().contains(cas)) {
				libraryInformationTarget.addCasNumber(cas);
			}
		}
		if(libraryInformationTarget.getFormula().isEmpty()) {
			libraryInformationTarget.setFormula(libraryInformationSource.getFormula());
		}
		if(libraryInformationTarget.getSmiles().isEmpty()) {
			libraryInformationTarget.setSmiles(libraryInformationSource.getSmiles());
		}
		if(libraryInformationTarget.getInChI().isEmpty()) {
			libraryInformationTarget.setInChI(libraryInformationSource.getInChI());
		}
		if(libraryInformationTarget.getInChIKey().isEmpty()) {
			libraryInformationTarget.setInChIKey(libraryInformationSource.getInChIKey());
		}
		if(libraryInformationTarget.getMolWeight() == 0) {
			libraryInformationTarget.setMolWeight(libraryInformationSource.getMolWeight());
		}
		if(libraryInformationTarget.getExactMass() == 0) {
			libraryInformationTarget.setExactMass(libraryInformationSource.getExactMass());
		}
		if(libraryInformationTarget.getDatabase().isEmpty()) {
			libraryInformationTarget.setDatabase(libraryInformationSource.getDatabase());
		}
		if(libraryInformationTarget.getContributor().isEmpty()) {
			libraryInformationTarget.setContributor(libraryInformationSource.getContributor());
		}
		if(libraryInformationTarget.getCompoundClass().isEmpty()) {
			libraryInformationTarget.setCompoundClass(libraryInformationSource.getCompoundClass());
		}
		if(libraryInformationTarget.getMoleculeStructure().isEmpty()) {
			libraryInformationTarget.setMoleculeStructure(libraryInformationSource.getMoleculeStructure());
		}
		if(libraryInformationTarget.getRetentionTime() == 0) {
			libraryInformationTarget.setRetentionTime(libraryInformationSource.getRetentionTime());
		}
		/*
		 * Retention Index
		 */
		if(libraryInformationTarget.getRetentionIndex() == 0) {
			libraryInformationTarget.setRetentionIndex(libraryInformationSource.getRetentionIndex());
		} else {
			float retentionIndex = libraryInformationSource.getRetentionIndex();
			if(retentionIndex > 0) {
				ISeparationColumn defaultColumn = SeparationColumnFactory.getSeparationColumn(SeparationColumnType.DEFAULT);
				libraryInformationTarget.add(new ColumnIndexMarker(defaultColumn, retentionIndex));
			}
		}
		mergeTextField(libraryInformationTarget.getComments(), libraryInformationSource.getComments(), libraryInformationTarget::setComments);
		mergeTextField(libraryInformationTarget.getMiscellaneous(), libraryInformationSource.getMiscellaneous(), libraryInformationTarget::setMiscellaneous);
		mergeTextField(libraryInformationTarget.getReferenceIdentifier(), libraryInformationSource.getReferenceIdentifier(), libraryInformationTarget::setReferenceIdentifier);
		/*
		 * Column Indices
		 */
		for(IColumnIndexMarker marker : libraryInformationSource.getColumnIndexMarkers()) {
			libraryInformationTarget.add(marker);
		}
		/*
		 * Flavor Marker
		 */
		for(IFlavorMarker marker : libraryInformationSource.getFlavorMarkers()) {
			libraryInformationTarget.add(marker);
		}

	}

	private static void mergeTextField(String existing, String addition, Consumer<String> setter) {

		if(!addition.isEmpty()) {
			if(existing.isEmpty()) {
				setter.accept(addition);
			} else if(!Arrays.asList(existing.split(", ")).contains(addition)) {
				setter.accept(existing + ", " + addition);
			}
		}
	}
}