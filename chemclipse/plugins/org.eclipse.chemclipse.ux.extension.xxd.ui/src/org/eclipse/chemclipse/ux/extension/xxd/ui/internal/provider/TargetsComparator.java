/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - sort by the metrics of the identification algorithm
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import java.util.OptionalDouble;

import org.eclipse.chemclipse.model.identifier.IComparisonMetric;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class TargetsComparator extends AbstractRecordTableComparator {

	private TargetsColumns targetsColumns = TargetsColumns.create(null, TargetsLabelProvider.TRAILING_COLUMNS);

	public void setColumns(TargetsColumns targetsColumns) {

		this.targetsColumns = targetsColumns;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IIdentificationTarget entry1 && e2 instanceof IIdentificationTarget entry2) {

			ILibraryInformation libraryInformation1 = entry1.getLibraryInformation();
			IComparisonResult comparisonResult1 = entry1.getComparisonResult();
			ILibraryInformation libraryInformation2 = entry2.getLibraryInformation();
			IComparisonResult comparisonResult2 = entry2.getComparisonResult();
			int columnIndex = getPropertyIndex();

			IComparisonMetric metric = targetsColumns.getMetric(columnIndex);
			TargetColumn column = targetsColumns.getColumn(columnIndex);
			if(metric != null) {
				sortOrder = compareMetric(metric, comparisonResult1, comparisonResult2);
			} else if(column != null) {
				switch(column) {
					case VERIFIED:
						sortOrder = Boolean.compare(entry2.isVerified(), entry1.isVerified());
						if(sortOrder == 0) {
							sortOrder = getAdditionalSortOrder(comparisonResult1, comparisonResult2);
						}
						break;
					case RATING:
						sortOrder = Float.compare(IComparisonResult.getScore(comparisonResult2), IComparisonResult.getScore(comparisonResult1));
						if(sortOrder == 0) {
							sortOrder = getAdditionalSortOrder(comparisonResult1, comparisonResult2);
						}
						break;
					case NAME:
						sortOrder = libraryInformation2.getName().compareTo(libraryInformation1.getName());
						break;
					case CAS:
						sortOrder = libraryInformation2.getCasNumber().compareTo(libraryInformation1.getCasNumber());
						break;
					case FORMULA:
						sortOrder = libraryInformation2.getFormula().compareTo(libraryInformation1.getFormula());
						break;
					case SMILES:
						sortOrder = libraryInformation2.getSmiles().compareTo(libraryInformation1.getSmiles());
						break;
					case INCHI:
						sortOrder = libraryInformation2.getInChI().compareTo(libraryInformation1.getInChI());
						break;
					case INCHI_KEY:
						sortOrder = libraryInformation2.getInChIKey().compareTo(libraryInformation1.getInChIKey());
						break;
					case MOL_WEIGHT:
						sortOrder = Double.compare(libraryInformation2.getMolWeight(), libraryInformation1.getMolWeight());
						break;
					case EXACT_MASS:
						sortOrder = Double.compare(libraryInformation2.getExactMass(), libraryInformation1.getExactMass());
						break;
					case ADVICE:
						String advise2 = comparisonResult2.getRatingSupplier().getAdvise();
						String advise1 = comparisonResult1.getRatingSupplier().getAdvise();
						if(advise2 != null && advise1 != null) {
							sortOrder = advise2.compareTo(advise1);
						}
						break;
					case IDENTIFIER:
						sortOrder = entry2.getIdentifier().compareTo(entry1.getIdentifier());
						break;
					case MISCELLANEOUS:
						sortOrder = libraryInformation2.getMiscellaneous().compareTo(libraryInformation1.getMiscellaneous());
						break;
					case COMMENTS:
						sortOrder = libraryInformation2.getComments().compareTo(libraryInformation1.getComments());
						break;
					case DATABASE:
						sortOrder = libraryInformation2.getDatabase().compareTo(libraryInformation1.getDatabase());
						break;
					case DATABASE_INDEX:
						sortOrder = Integer.compare(libraryInformation2.getDatabaseIndex(), libraryInformation1.getDatabaseIndex());
						break;
					case CONTRIBUTOR:
						sortOrder = libraryInformation2.getContributor().compareTo(libraryInformation1.getContributor());
						break;
					case REFERENCE_ID:
						sortOrder = libraryInformation2.getReferenceIdentifier().compareTo(libraryInformation1.getReferenceIdentifier());
						break;
					case RETENTION_TIME:
						sortOrder = Integer.compare(libraryInformation2.getRetentionTime(), libraryInformation1.getRetentionTime());
						break;
					case RETENTION_INDEX:
						sortOrder = Float.compare(libraryInformation2.getRetentionIndex(), libraryInformation1.getRetentionIndex());
						break;
					case NCBI_TAXONOMY:
						sortOrder = Integer.compare(libraryInformation2.getTaxonomyIdentifierNCBI(), libraryInformation1.getTaxonomyIdentifierNCBI());
						break;
					case GENBANK_ACCESSION:
						sortOrder = libraryInformation2.getGenBankAccesion().compareTo(libraryInformation1.getGenBankAccesion());
						break;
					default:
						sortOrder = 0;
				}
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}

	private int getAdditionalSortOrder(IComparisonResult comparisonResult1, IComparisonResult comparisonResult2) {

		for(IComparisonMetric metric : comparisonResult1.getMetrics()) {
			int sortOrder = compareMetric(metric, comparisonResult1, comparisonResult2);
			if(sortOrder != 0) {
				return sortOrder;
			}
		}

		return 0;
	}

	private static int compareMetric(IComparisonMetric metric, IComparisonResult comparisonResult1, IComparisonResult comparisonResult2) {

		OptionalDouble value1 = comparisonResult1.getMetric(metric.getId());
		OptionalDouble value2 = comparisonResult2.getMetric(metric.getId());

		if(value1.isEmpty()) {
			return value2.isEmpty() ? 0 : 1;
		} else if(value2.isEmpty()) {
			return -1;
		}

		return metric.getComparator().compare(value1.getAsDouble(), value2.getAsDouble());
	}
}
