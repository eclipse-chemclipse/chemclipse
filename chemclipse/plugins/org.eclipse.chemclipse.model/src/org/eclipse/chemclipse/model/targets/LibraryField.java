/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.targets;

import java.util.function.Function;

import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.l10n.ModelMessages;
import org.eclipse.chemclipse.support.text.ILabel;

public enum LibraryField implements ILabel {

	NAME(ModelMessages.name, getLibraryExtractor(ILibraryInformation::getName)), //
	CAS("CAS", getLibraryExtractor(ILibraryInformation::getCasNumber)), // //$NON-NLS-1$
	NAME_CAS(ModelMessages.nameCas, getLibraryExtractor(ILibraryInformation::getName, ILibraryInformation::getCasNumber)), //
	FORMULA(ModelMessages.formula, getLibraryExtractor(ILibraryInformation::getFormula)), //
	SMILES("SMILES", getLibraryExtractor(ILibraryInformation::getSmiles)), // //$NON-NLS-1$
	SYNONYMS(ModelMessages.synonyms, getLibraryExtractor(ILibraryInformation::getSynonyms).andThen(elements -> elements != null ? String.join("; ", elements) : null)), // //$NON-NLS-1$
	INCHI("InChI", getLibraryExtractor(ILibraryInformation::getInChI)), // //$NON-NLS-1$
	INCHIKEY("InChIKey", getLibraryExtractor(ILibraryInformation::getInChIKey)), // //$NON-NLS-1$
	MOL_WEIGHT(ModelMessages.molWeight, getLibraryExtractorDouble(ILibraryInformation::getMolWeight)), //
	EXACT_MASS(ModelMessages.exactMass, getLibraryExtractorDouble(ILibraryInformation::getExactMass)), //
	REFID("RefID", getLibraryExtractor(ILibraryInformation::getReferenceIdentifier)), // //$NON-NLS-1$
	NAME_REFID(ModelMessages.nameRefId, getLibraryExtractor(ILibraryInformation::getName, ILibraryInformation::getReferenceIdentifier)); //

	private final Function<IIdentificationTarget, String> transformer;
	private final String label;

	private LibraryField(String label, Function<IIdentificationTarget, String> transformer) {

		this.label = label;
		this.transformer = transformer;
	}

	@Override
	public String label() {

		return label;
	}

	/**
	 * @return a transformer that can transform the given target into an ordinary String
	 */
	public Function<IIdentificationTarget, String> getTransformer() {

		return transformer;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}

	private static <T> Function<IIdentificationTarget, T> getLibraryExtractor(Function<ILibraryInformation, T> extractor) {

		return target -> {
			/*
			 * No target.
			 */
			if(target == null) {
				return null;
			}
			/*
			 * No library information.
			 */
			ILibraryInformation libraryInformation = target.getLibraryInformation();
			if(libraryInformation == null) {
				return null;
			}

			return extractor.apply(libraryInformation);
		};
	}

	private static <T> Function<IIdentificationTarget, String> getLibraryExtractorDouble(Function<ILibraryInformation, Double> extractor) {

		return target -> {
			/*
			 * No target.
			 */
			if(target == null) {
				return null;
			}
			/*
			 * No library information.
			 */
			ILibraryInformation libraryInformation = target.getLibraryInformation();
			if(libraryInformation == null) {
				return null;
			}

			return Double.toString(extractor.apply(libraryInformation));
		};
	}

	private static <T> Function<IIdentificationTarget, String> getLibraryExtractor(Function<ILibraryInformation, T> extractor1, Function<ILibraryInformation, T> extractor2) {

		return target -> {
			/*
			 * No target.
			 */
			if(target == null) {
				return null;
			}
			/*
			 * No library information.
			 */
			ILibraryInformation libraryInformation = target.getLibraryInformation();
			if(libraryInformation == null) {
				return null;
			}

			String result = extractor1.apply(libraryInformation) + " (" + extractor2.apply(libraryInformation) + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			return result.trim();
		};
	}
}