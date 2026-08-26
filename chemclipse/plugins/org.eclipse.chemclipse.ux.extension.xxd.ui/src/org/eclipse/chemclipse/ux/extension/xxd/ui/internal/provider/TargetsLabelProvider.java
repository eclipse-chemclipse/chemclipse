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
 * Matthias Mailänder - display the metrics of the identification algorithm
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Stream;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
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
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;

public class TargetsLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String VERIFIED = ExtensionMessages.verified;
	public static final String NAME = ExtensionMessages.name;
	public static final String MATCH_FACTOR = ExtensionMessages.matchFactor;
	public static final String REVERSE_MATCH_FACTOR = ExtensionMessages.reverseMatchFactor;
	public static final String MATCH_FACTOR_DIRECT = ExtensionMessages.matchFactorDirect;
	public static final String REVERSE_MATCH_FACTOR_DIRECT = ExtensionMessages.reverseMatchFactorDirect;
	public static final String PROBABILITY = ExtensionMessages.probability;
	public static final String MOL_WEIGHT = ExtensionMessages.molWeight;
	public static final String EXACT_MASS = ExtensionMessages.excactMass;
	public static final String ADVICE = ExtensionMessages.advice;
	public static final String IDENTIFIER = ExtensionMessages.identifier;
	public static final String MISCELLANEOUS = ExtensionMessages.miscellaneous;
	public static final String DATABASE = ExtensionMessages.database;
	public static final String DATABASE_INDEX = ExtensionMessages.databaseIndex;
	public static final String RATING = ExtensionMessages.rating;
	public static final String CAS = "CAS";
	public static final String COMMENTS = ExtensionMessages.comments;
	public static final String FORMULA = ExtensionMessages.formula;
	public static final String SMILES = "SMILES";
	public static final String INCHI = "InChI";
	public static final String INCHI_KEY = "InChI Key";
	public static final String CONTRIBUTOR = ExtensionMessages.contributor;
	public static final String RETENTION_TIME = ExtensionMessages.retentionTime;
	public static final String RETENTION_INDEX = ExtensionMessages.retentionIndex;
	public static final String REFERENCE_ID = ExtensionMessages.referenceID;
	public static final String INLIB_FACTOR = ExtensionMessages.inLibFactor;
	public static final String NCBI_TAXONOMY = "NCBI Taxonomy ID";
	public static final String GENBANK_ACCESSION = "GenBank Accession";

	private static final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

	private static final Stream<TargetColumn[]> TARGET_COLUMNS = Stream.of(TargetsColumns.CHEMICAL_IDENTIFIERS, TargetsColumns.CHROMATOGRAPHY_RELATED, TargetsColumns.BIOLOGICAL_IDENTIFIERS);
	public static final Collection<TargetColumn> TRAILING_COLUMNS = TARGET_COLUMNS.flatMap(Arrays::stream).toList();

	public static final String[] TITLES = TargetsColumns.create(null, TRAILING_COLUMNS).getTitles();
	public static final int[] BOUNDS = TargetsColumns.create(null, TRAILING_COLUMNS).getBounds();

	private TargetsColumns targetsColumns = TargetsColumns.create(null, TRAILING_COLUMNS);
	private final Map<String, DecimalFormat> metricFormats = new HashMap<>();

	public void setColumns(TargetsColumns targetsColumns) {

		this.targetsColumns = targetsColumns;
	}

	public static String getRetentionTimeText(ILibraryInformation libraryInformation, Integer retentionTime) {

		DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.000");
		String deltaRetentionTime = "";
		if(retentionTime != null) {
			if(preferenceStore.getBoolean(PreferenceSupplier.P_TARGETS_TABLE_SHOW_DEVIATION_RT)) {
				int delta = libraryInformation.getRetentionTime() - retentionTime;
				deltaRetentionTime = " [" + decimalFormat.format(delta / IChromatogramOverview.MINUTE_CORRELATION_FACTOR) + "]";
			}
		}
		/*
		 * Label
		 */
		String libraryRetentionTime = decimalFormat.format(libraryInformation.getRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
		return libraryRetentionTime + deltaRetentionTime;
	}

	public static String getRetentionIndexText(ILibraryInformation libraryInformation, Float retentionIndex) {

		DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.000");
		DecimalFormat decimalFormatInteger = ValueFormat.getDecimalFormatEnglish("0");
		boolean showRetentionIndexWithoutDecimals = org.eclipse.chemclipse.model.preferences.PreferenceSupplier.showRetentionIndexWithoutDecimals();

		String deltaRetentionIndex = "";
		if(retentionIndex != null) {
			if(preferenceStore.getBoolean(PreferenceSupplier.P_TARGETS_TABLE_SHOW_DEVIATION_RI)) {
				float delta = libraryInformation.getRetentionIndex() - retentionIndex;
				if(showRetentionIndexWithoutDecimals) {
					deltaRetentionIndex = " [" + decimalFormatInteger.format(delta) + "]";
				} else {
					deltaRetentionIndex = " [" + decimalFormat.format(delta) + "]";
				}
			}
		}
		/*
		 * Label
		 */
		String libraryRetentionIndex = "";
		if(showRetentionIndexWithoutDecimals) {
			libraryRetentionIndex = decimalFormatInteger.format(libraryInformation.getRetentionIndex());
		} else {
			libraryRetentionIndex = decimalFormat.format(libraryInformation.getRetentionIndex());
		}

		return libraryRetentionIndex + deltaRetentionIndex;
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

		DecimalFormat decimalFormat = getDecimalFormat();

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
				case CAS:
					text = libraryInformation.getCasNumber();
					break;
				case FORMULA:
					text = libraryInformation.getFormula();
					break;
				case SMILES:
					text = libraryInformation.getSmiles();
					break;
				case INCHI:
					text = libraryInformation.getInChI();
					break;
				case INCHI_KEY:
					text = libraryInformation.getInChIKey();
					break;
				case MOL_WEIGHT:
					text = decimalFormat.format(libraryInformation.getMolWeight());
					break;
				case EXACT_MASS:
					text = decimalFormat.format(libraryInformation.getExactMass());
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
					/*
					 * UUID or resolved name.
					 */
					if(PreferenceSupplier.isResolveDatabaseUUID()) {
						text = DatabaseResolver.getDatabaseName(libraryInformation.getDatabase());
					} else {
						text = libraryInformation.getDatabase();
					}
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
				case RETENTION_TIME:
					text = getRetentionTimeText(libraryInformation, null);
					break;
				case RETENTION_INDEX:
					text = getRetentionIndexText(libraryInformation, null);
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