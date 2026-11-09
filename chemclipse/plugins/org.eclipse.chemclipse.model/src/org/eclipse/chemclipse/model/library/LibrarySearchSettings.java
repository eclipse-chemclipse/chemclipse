/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.library;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;

public class LibrarySearchSettings {

	private String searchColumn = ""; //$NON-NLS-1$
	private int retentionTime = 0;
	private int retentionIndex = 0;
	private List<IIdentificationTarget> identificationTargets = new ArrayList<>();

	public String getSearchColumn() {

		return searchColumn;
	}

	public void setSearchColumn(String searchColumn) {

		this.searchColumn = searchColumn;
	}

	public int getRetentionTime() {

		return retentionTime;
	}

	public void setRetentionTime(int retentionTime) {

		this.retentionTime = retentionTime;
	}

	public float getRetentionIndex() {

		return retentionIndex;
	}

	public void setRetentionIndex(int retentionIndex) {

		this.retentionIndex = retentionIndex;
	}

	public List<IIdentificationTarget> getIdentificationTargets() {

		return identificationTargets;
	}
}