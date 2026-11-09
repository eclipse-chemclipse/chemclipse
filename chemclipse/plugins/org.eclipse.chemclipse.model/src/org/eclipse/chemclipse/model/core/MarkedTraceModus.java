/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - add mode to interface
 * Matthias Mailänder - add label
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import org.eclipse.chemclipse.model.l10n.ModelMessages;
import org.eclipse.chemclipse.support.text.ILabel;

public enum MarkedTraceModus implements ILabel {
	EXCLUDE(ModelMessages.exclude), //
	INCLUDE(ModelMessages.include);

	private String label = ""; //$NON-NLS-1$

	private MarkedTraceModus(String label) {

		this.label = label;
	}

	@Override
	public String label() {

		return label;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}