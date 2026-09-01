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
 * Matthias Mailänder - adapted for MALDI
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Stream;

import org.eclipse.chemclipse.model.identifier.IComparisonMetric;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.support.DatabaseResolver;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.ux.extension.ui.provider.IdentificationTargetSupport;
import org.eclipse.chemclipse.ux.extension.ui.provider.TargetColumn;
import org.eclipse.chemclipse.ux.extension.ui.provider.TargetsColumns;
import org.eclipse.swt.graphics.Image;

public class TargetsLabelProvider extends AbstractChemClipseLabelProvider {

	private static final Stream<TargetColumn> TARGET_COLUMNS = Stream.of(TargetsColumns.BIOLOGICAL_IDENTIFIERS);
	public static final Collection<TargetColumn> TRAILING_COLUMNS = TARGET_COLUMNS.toList();

	public static final String[] TITLES = TargetsColumns.create(null, TRAILING_COLUMNS).getTitles();
	public static final int[] BOUNDS = TargetsColumns.create(null, TRAILING_COLUMNS).getBounds();

	private TargetsColumns targetsColumns = TargetsColumns.create(null, TRAILING_COLUMNS);
	private final Map<String, DecimalFormat> metricFormats = new HashMap<>();

	public void setColumns(TargetsColumns targetsColumns) {

		this.targetsColumns = targetsColumns;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(element instanceof IIdentificationTarget identificationTarget) {
			TargetColumn column = targetsColumns.getColumn(columnIndex);
			if(column == null) {
				return null;
			}

			switch(column) {
				case VERIFIED:
					/*
					 * CheckBox
					 */
					if(identificationTarget.isVerified()) {
						return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SELECTED, IApplicationImageProvider.SIZE_16x16);
					} else {
						return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DESELECTED, IApplicationImageProvider.SIZE_16x16);
					}
				case RATING:
					return IdentificationTargetSupport.getRatingSymbol(identificationTarget);
				case NAME:
					return getImage(element);
				default:
					return null;
			}
		}

		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		if(element instanceof IIdentificationTarget identificationTarget) {
			ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
			IComparisonResult comparisonResult = identificationTarget.getComparisonResult();

			IComparisonMetric metric = targetsColumns.getMetric(columnIndex);
			if(metric != null) {
				return getMetricText(comparisonResult, metric);
			}

			TargetColumn column = targetsColumns.getColumn(columnIndex);
			if(column == null) {
				return "n.v.";
			}

			switch(column) {
				case VERIFIED:
					text = "";
					break;
				case RATING:
					text = "";
					break;
				case NAME:
					text = libraryInformation.getName();
					break;
				case ADVICE:
					text = comparisonResult.getRatingSupplier().getAdvise();
					break;
				case IDENTIFIER:
					text = identificationTarget.getIdentifier();
					break;
				case MISCELLANEOUS:
					text = libraryInformation.getMiscellaneous();
					break;
				case COMMENTS:
					text = libraryInformation.getComments();
					break;
				case DATABASE:
					text = DatabaseResolver.getDatabaseName(libraryInformation.getDatabase());
					break;
				case DATABASE_INDEX:
					text = Integer.toString(libraryInformation.getDatabaseIndex());
					break;
				case CONTRIBUTOR:
					text = libraryInformation.getContributor();
					break;
				case REFERENCE_ID:
					text = libraryInformation.getReferenceIdentifier();
					break;
				case NCBI_TAXONOMY:
					text = Integer.toString(libraryInformation.getTaxonomyIdentifierNCBI());
					break;
				case GENBANK_ACCESSION:
					text = libraryInformation.getGenBankAccesion();
					break;
				default:
					text = "n.v.";
			}
		}
		return text;
	}

	private String getMetricText(IComparisonResult comparisonResult, IComparisonMetric metric) {

		OptionalDouble value = comparisonResult.getMetric(metric.getId());
		if(value.isEmpty()) {
			return "";
		}

		DecimalFormat decimalFormat = metricFormats.computeIfAbsent(metric.getFormat(), ValueFormat::getDecimalFormatEnglish);
		return decimalFormat.format(value.getAsDouble());
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_TARGETS, IApplicationImageProvider.SIZE_16x16);
	}
}