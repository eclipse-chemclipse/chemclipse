/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.targets;

import org.eclipse.chemclipse.model.l10n.ModelMessages;
import org.eclipse.chemclipse.support.text.ILabel;

public enum DisplayOption implements ILabel {

	STANDARD(ModelMessages.standard), //
	NUMBERS(ModelMessages.numbers), //
	NUMBERS_STANDARD(ModelMessages.numbersDisplayField), //
	RETENTION_TIME(ModelMessages.retentionTime), //
	RETENTION_TIME_STANDARD(ModelMessages.retentionTimeDisplayField), //
	RETENTION_INDEX(ModelMessages.retentionIndex), //
	RETENTION_INDEX_STANDARD(ModelMessages.retentionIndexDisplayField), //
	RETENTION_INDEX_AREA_PERCENT(ModelMessages.retentionIndexAreaPercent), //
	AREA_PERCENT(ModelMessages.areaPercent), //
	AREA_PERCENT_STANDARD(ModelMessages.areaPercentDisplayField); //

	private String label = ""; //$NON-NLS-1$

	private DisplayOption(String label) {

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