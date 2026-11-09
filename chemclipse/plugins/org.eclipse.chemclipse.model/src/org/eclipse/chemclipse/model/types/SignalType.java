/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.types;

import org.eclipse.chemclipse.model.l10n.ModelMessages;
import org.eclipse.chemclipse.support.text.ILabel;

public enum SignalType implements ILabel {

	AUTO_DETECT(ModelMessages.autoDetect), //
	CENTROID(ModelMessages.centroid), //
	PROFILE(ModelMessages.profile);

	private String label = ""; //$NON-NLS-1$

	private SignalType(String label) {

		this.label = label;
	}

	@Override
	public String label() {

		return label;
	}
}
