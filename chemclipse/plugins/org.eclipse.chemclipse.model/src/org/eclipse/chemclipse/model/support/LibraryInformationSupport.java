/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.support;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.model.identifier.IFlavorMarker;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;

public class LibraryInformationSupport {

	public static List<ILibraryInformation> filterByRetentionIndexDelta(List<ILibraryInformation> libraryInformations, float retentionIndex, int maxResults) {

		/*
		 * Filter the best results if too many entries have been extracted.
		 * The lowest retention index delta shall be preferred.
		 */
		if(libraryInformations.size() > maxResults) {
			Collections.sort(libraryInformations, (l1, l2) -> Float.compare(Math.abs(l1.getRetentionIndex() - retentionIndex), Math.abs(l2.getRetentionIndex() - retentionIndex)));
			return libraryInformations.subList(0, maxResults);
		} else {
			return libraryInformations;
		}
	}

	/**
	 * 
	 * @param searchText
	 *            supports "prefix:query" syntax
	 */
	public static boolean containsSearchText(ILibraryInformation libraryInformation, String searchText, boolean caseSensitive) {

		if(libraryInformation == null || searchText == null) {
			return false;
		}

		int colon = searchText.indexOf(':');
		if(colon > 0) {
			String possiblePrefix = searchText.substring(0, colon).trim().toLowerCase();
			String remainder = searchText.substring(colon + 1);
			SearchField mapped = mapPrefixToField(possiblePrefix);
			if(mapped != null) {
				return containsSearchText(libraryInformation, remainder, caseSensitive, EnumSet.of(mapped));
			}
		}

		return containsSearchText(libraryInformation, searchText, caseSensitive, EnumSet.of(SearchField.ALL));
	}

	public static boolean containsSearchText(ILibraryInformation libraryInformation, String searchText, boolean caseSensitive, Set<SearchField> fieldsToSearch) {

		if(libraryInformation == null || searchText == null || fieldsToSearch == null) {
			return false;
		}

		String query = caseSensitive ? searchText : searchText.toLowerCase();
		boolean searchAll = fieldsToSearch.contains(SearchField.ALL);

		if(searchAll || fieldsToSearch.contains(SearchField.NAME)) {
			if(matches(libraryInformation.getName(), query, caseSensitive)) {
				return true;
			}
		}

		if(searchAll || fieldsToSearch.contains(SearchField.REFERENCE_IDENTIFIER)) {
			if(matches(libraryInformation.getReferenceIdentifier(), query, caseSensitive)) {
				return true;
			}
		}

		if(searchAll || fieldsToSearch.contains(SearchField.FORMULA)) {
			if(matches(libraryInformation.getFormula(), query, caseSensitive)) {
				return true;
			}
		}

		if(searchAll || fieldsToSearch.contains(SearchField.SMILES)) {
			if(matches(libraryInformation.getSmiles(), query, caseSensitive)) {
				return true;
			}
		}

		if(searchAll || fieldsToSearch.contains(SearchField.INCHI)) {
			if(matches(libraryInformation.getInChI(), query, caseSensitive)) {
				return true;
			}
		}

		if(searchAll || fieldsToSearch.contains(SearchField.INCHI_KEY)) {
			if(matches(libraryInformation.getInChIKey(), query, caseSensitive)) {
				return true;
			}
		}

		if(searchAll || fieldsToSearch.contains(SearchField.CAS)) {
			List<String> casNumbers = libraryInformation.getCasNumbers();
			if(casNumbers != null) {
				for(String cas : casNumbers) {
					if(matches(cas, query, caseSensitive)) {
						return true;
					}
				}
			}
		}

		if(searchAll || fieldsToSearch.contains(SearchField.COMMENTS)) {
			if(matches(libraryInformation.getComments(), query, caseSensitive)) {
				return true;
			}
		}

		if(searchAll || fieldsToSearch.contains(SearchField.SYNONYMS)) {
			Set<String> synonyms = libraryInformation.getSynonyms();
			if(synonyms != null) {
				for(String synonym : synonyms) {
					if(matches(synonym, query, caseSensitive)) {
						return true;
					}
				}
			}
		}

		if(searchAll || fieldsToSearch.contains(SearchField.DATABASE)) {
			if(matches(libraryInformation.getDatabase(), query, caseSensitive)) {
				return true;
			}
		}

		if(searchAll || fieldsToSearch.contains(SearchField.FLAVOR_ODOR) || fieldsToSearch.contains(SearchField.FLAVOR_MATRIX) || fieldsToSearch.contains(SearchField.FLAVOR_SOLVENT)) {
			for(IFlavorMarker flavorMarker : libraryInformation.getFlavorMarkers()) {
				if(flavorMarker == null) {
					continue;
				}
				if((searchAll || fieldsToSearch.contains(SearchField.FLAVOR_ODOR)) && matches(flavorMarker.getOdor(), query, caseSensitive)) {
					return true;
				}
				if((searchAll || fieldsToSearch.contains(SearchField.FLAVOR_MATRIX)) && matches(flavorMarker.getMatrix(), query, caseSensitive)) {
					return true;
				}
				if((searchAll || fieldsToSearch.contains(SearchField.FLAVOR_SOLVENT)) && matches(flavorMarker.getSolvent(), query, caseSensitive)) {
					return true;
				}
			}
		}

		return false;
	}

	public void extractNameAndReferenceIdentifier(String name, ILibraryInformation libraryInformation, String referenceIdentifierMarker, String referenceIdentifierPrefix) {

		if(name != null && libraryInformation != null) {
			boolean setNameTraditionally = true;
			if(referenceIdentifierMarker != null && !referenceIdentifierMarker.equals("")) { //$NON-NLS-1$
				if(name.contains(referenceIdentifierMarker)) {
					String[] values = name.split(referenceIdentifierMarker);
					if(values.length >= 2) {
						/*
						 * Extract the reference identifier.
						 */
						setNameTraditionally = false;
						libraryInformation.setName(values[0].trim());

						StringBuilder builder = new StringBuilder();
						if(referenceIdentifierPrefix != null) {
							builder.append(referenceIdentifierPrefix);
						}
						int size = values.length;
						for(int i = 1; i < size; i++) {
							builder.append(values[i]);
							if(i < size - 1) {
								builder.append(" "); //$NON-NLS-1$
							}
						}
						libraryInformation.setReferenceIdentifier(builder.toString().trim());
					}
				}
			}

			if(setNameTraditionally) {
				libraryInformation.setName(name);
			}
		}
	}

	private static SearchField mapPrefixToField(String prefix) {

		if(prefix == null || prefix.isEmpty()) {
			return SearchField.ALL;
		}

		for(SearchField field : SearchField.values()) {
			for(String keyword : field.keywords()) {
				if(keyword.equalsIgnoreCase(prefix)) {
					return field;
				}
			}
		}

		return SearchField.ALL;
	}

	private static boolean matches(String candidate, String query, boolean caseSensitive) {

		if(candidate == null || query == null) {
			return false;
		}
		if(!caseSensitive) {
			candidate = candidate.toLowerCase();
		}
		return candidate.contains(query);
	}
}