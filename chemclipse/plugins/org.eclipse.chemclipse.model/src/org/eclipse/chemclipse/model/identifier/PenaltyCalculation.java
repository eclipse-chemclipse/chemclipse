/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.identifier;

import org.eclipse.chemclipse.model.l10n.ModelMessages;
import org.eclipse.chemclipse.support.text.ILabel;

public enum PenaltyCalculation implements ILabel {
	NONE(ModelMessages.none), //
	RETENTION_TIME_MS(ModelMessages.retentionTimeMs), //
	RETENTION_TIME_MIN(ModelMessages.retentionTimeMin), //
	RETENTION_INDEX(ModelMessages.retentionIndex); //

	private String label = ""; //$NON-NLS-1$

	private PenaltyCalculation(String label) {

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