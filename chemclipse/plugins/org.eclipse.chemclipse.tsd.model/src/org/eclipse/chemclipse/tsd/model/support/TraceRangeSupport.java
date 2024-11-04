/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.model.support;

import java.text.DecimalFormat;

import org.eclipse.chemclipse.support.text.ValueFormat;

public class TraceRangeSupport {

	public static final DecimalFormat DF_COLUMN_1_MINUTES = ValueFormat.getDecimalFormatEnglish("0.00##");
	public static final DecimalFormat DF_COLUMN_2_SECONDS = ValueFormat.getDecimalFormatEnglish("0");
}