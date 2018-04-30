/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.classifier.supplier.durbinwatson.ui.internal.provider;

import javax.naming.directory.InvalidAttributesException;

import org.eclipse.chemclipse.ux.extension.ui.support.AbstractMeasurementResultTitles;
import org.eclipse.chemclipse.ux.extension.ui.support.IMeasurementResultTitles;

public class MeasurementResultTitles extends AbstractMeasurementResultTitles implements IMeasurementResultTitles {

	private static final String[] titles = {"Rating", "Derivative", "Order", "Width"};
	private static final int[] bounds = {100, 100, 100, 100};

	public MeasurementResultTitles() throws InvalidAttributesException {
		super(titles, bounds);
	}
}
