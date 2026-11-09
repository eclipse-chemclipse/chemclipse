/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.ui.preferences;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.ui.l10n.ExtensionMessages;
import org.eclipse.jface.dialogs.IInputValidator;

public class IonInputValidator implements IInputValidator {

	@Override
	public String isValid(String newText) {

		/*
		 * Test if the input is an integer value.<br/> If yes, return null,
		 * otherwise throw a failure description.
		 */
		try {
			String.valueOf(Integer.parseInt(newText));
		} catch(NumberFormatException e) {
			return ExtensionMessages.inputMustBeInteger;
		}
		return null;
	}
}
