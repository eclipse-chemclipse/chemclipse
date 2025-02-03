/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - new API and reimplementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui.l10n;

import org.eclipse.osgi.util.NLS;

public class ExtensionMessages extends NLS {

	public static String wells;
	public static String value;
	public static String subsetSelection;
	public static String selectColor;
	public static String resetPlate;
	public static String removeSelectedColorCodes;
	public static String remove;
	public static String referenceLabel;
	public static String reallyDeleteSelectedCode;
	public static String plateChart;
	public static String pcrExport;
	public static String nameMustNotContain;
	public static String nameMustBeSpecified;
	public static String name;
	public static String importPlate;
	public static String fluorescence;
	public static String fieldForColorCodeName;
	public static String exportPlate;
	public static String exportFailed;
	public static String editSelectedColorCode;
	public static String edit;
	public static String defaultLineColor;
	public static String cycle;
	public static String colorIsUsed;
	public static String colorCodes;
	public static String color;
	public static String channelSpecification;
	public static String addRemoveColorCodes;
	public static String addColorCode;
	public static String add;

	static {
		NLS.initializeMessages("org.eclipse.chemclipse.ux.extension.pcr.ui.l10n.messages", ExtensionMessages.class); //$NON-NLS-1$
	}

	private ExtensionMessages() {

	}
}
