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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.rcp.ui.icons.core;

/**
 * Selects the rating symbol for a normalized score between 0 and 100.
 * Which score an identification algorithm reports is its own decision, see
 * IRatingSupplier#getScore(). How the score is displayed is decided here.
 */
public class RatingImageSupport {

	public static final float RATING_VERY_GOOD = 90.0f;
	public static final float RATING_GOOD = 80.0f;
	public static final float RATING_AVERAGE = 70.0f;
	public static final float RATING_BAD = 60.0f;

	private RatingImageSupport() {

	}

	/**
	 * Returns the image name for the given score or an empty string if the
	 * score is not a number, which means that no rating is available.
	 *
	 * @param score
	 * @return String
	 */
	public static String getImageName(float score) {

		if(Float.isNaN(score)) {
			return "";
		}

		if(score >= RATING_VERY_GOOD) {
			return IApplicationImage.IMAGE_RATING_VERY_GOOD;
		} else if(score >= RATING_GOOD) {
			return IApplicationImage.IMAGE_RATING_GOOD;
		} else if(score >= RATING_AVERAGE) {
			return IApplicationImage.IMAGE_RATING_AVERAGE;
		} else if(score >= RATING_BAD) {
			return IApplicationImage.IMAGE_RATING_BAD;
		}

		return IApplicationImage.IMAGE_RATING_VERY_BAD;
	}
}
