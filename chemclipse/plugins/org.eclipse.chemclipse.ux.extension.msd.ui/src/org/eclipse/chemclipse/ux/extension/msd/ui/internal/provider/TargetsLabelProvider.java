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

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.rcp.ui.icons.core.RatingImageSupport;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.swt.graphics.Image;

public class TargetsLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String VERIFIED = "Verified";
	public static final String NAME = "Name";
	public static final String MATCH_FACTOR = "Match Factor";
	public static final String REVERSE_MATCH_FACTOR = "Reverse Match Factor";
	public static final String MATCH_FACTOR_DIRECT = "Match Factor Direct";
	public static final String REVERSE_MATCH_FACTOR_DIRECT = "Reverse Match Factor Direct";
	public static final String PROBABILITY = "Probability";
	public static final String ADVICE = "Advice";
	public static final String IDENTIFIER = "Identifier";
	public static final String MISCELLANEOUS = "Miscellaneous";
	public static final String DATABASE = "Database";
	public static final String DATABASE_INDEX = "Database Index";
	public static final String RATING = "Rating";
	public static final String COMMENTS = "Comments";
	public static final String CONTRIBUTOR = "Contributor";
	public static final String REFERENCE_ID = "Reference ID";

	public static final int INDEX_RATING = 1;
	public static final int INDEX_NAME = 2;

	public static final String[] TITLES = { //
			VERIFIED, //
			RATING, //
			NAME, //
			MATCH_FACTOR, //
			REVERSE_MATCH_FACTOR, //
			MATCH_FACTOR_DIRECT, //
			REVERSE_MATCH_FACTOR_DIRECT, //
			PROBABILITY, //
			ADVICE, //
			IDENTIFIER, //
			MISCELLANEOUS, //
			COMMENTS, //
			DATABASE, //
			DATABASE_INDEX, //
			CONTRIBUTOR, //
			REFERENCE_ID, //
	};

	public static final int[] BOUNDS = { //
			30, //
			30, //
			200, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			/*
			 * CheckBox
			 */
			if(element instanceof IIdentificationTarget identificationTarget) {
				if(identificationTarget.isVerified()) {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SELECTED, IApplicationImageProvider.SIZE_16x16);
				} else {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DESELECTED, IApplicationImageProvider.SIZE_16x16);
				}
			}
		} else if(columnIndex == INDEX_RATING) {
			/*
			 * Rating
			 */
			if(element instanceof IIdentificationTarget identificationTarget) {
				IComparisonResult comparisonResult = identificationTarget.getComparisonResult();
				String fileName = RatingImageSupport.getImageName(comparisonResult.getRatingSupplier().getScore());
				if(!fileName.isEmpty()) {
					return ApplicationImageFactory.getInstance().getImage(fileName, IApplicationImageProvider.SIZE_16x16);
				}
			}
		} else if(columnIndex == INDEX_NAME) {
			/*
			 * Entry
			 */
			return getImage(element);
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

			switch(columnIndex) {
				case 0: // Verified
					text = "";
					break;
				case 1: // Rating
					text = "";
					break;
				case 2: // Name
					text = libraryInformation.getName();
					break;
				case 3: // MQ
					text = decimalFormat.format(comparisonResult.getMatchFactor());
					break;
				case 4: // RMQ
					text = decimalFormat.format(comparisonResult.getReverseMatchFactor());
					break;
				case 5: // MQD
					text = decimalFormat.format(comparisonResult.getMatchFactorDirect());
					break;
				case 6: // RMQD
					text = decimalFormat.format(comparisonResult.getReverseMatchFactorDirect());
					break;
				case 7: // RMQD
					text = decimalFormat.format(comparisonResult.getProbability());
					break;
				case 8: // Advise
					text = comparisonResult.getRatingSupplier().getAdvise();
					break;
				case 9: // Identifier
					text = identificationTarget.getIdentifier();
					break;
				case 10: // Miscellaneous
					text = libraryInformation.getMiscellaneous();
					break;
				case 11: // Comments
					text = libraryInformation.getComments();
					break;
				case 12:
					text = libraryInformation.getDatabase();
					break;
				case 13:
					text = Integer.toString(libraryInformation.getDatabaseIndex());
					break;
				case 14:
					text = libraryInformation.getContributor();
					break;
				case 15:
					text = libraryInformation.getReferenceIdentifier();
					break;
				default:
					text = "n.v.";
			}
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_TARGETS, IApplicationImageProvider.SIZE_16x16);
	}
}