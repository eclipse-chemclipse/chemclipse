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
package org.eclipse.chemclipse.model.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.support.model.SeparationColumnType;

public class RetentionIndexLibrarySettings {

	private String searchColumn = "DB5"; //$NON-NLS-1$
	private boolean caseSensitive = false;
	private boolean removeWhiteSpace = false;
	private boolean matchPartly = false;
	private SeparationColumnType separationColumnTypeFallback = SeparationColumnType.DEFAULT;
	private int retentionIndexDelta = 10;
	private String specificDatabase = ""; // If empty, take all. //$NON-NLS-1$

	private List<IRetentionIndexLibraryService> retentionIndexLibraryServices = new ArrayList<>();

	public String getSearchColumn() {

		return searchColumn;
	}

	public void setSearchColumn(String searchColumn) {

		this.searchColumn = searchColumn;
	}

	public boolean isCaseSensitive() {

		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {

		this.caseSensitive = caseSensitive;
	}

	public boolean isRemoveWhiteSpace() {

		return removeWhiteSpace;
	}

	public void setRemoveWhiteSpace(boolean removeWhiteSpace) {

		this.removeWhiteSpace = removeWhiteSpace;
	}

	public boolean isMatchPartly() {

		return matchPartly;
	}

	public void setMatchPartly(boolean matchPartly) {

		this.matchPartly = matchPartly;
	}

	public SeparationColumnType getSeparationColumnTypeFallback() {

		return separationColumnTypeFallback;
	}

	public void setSeparationColumnTypeFallback(SeparationColumnType separationColumnTypeFallback) {

		this.separationColumnTypeFallback = separationColumnTypeFallback;
	}

	public int getRetentionIndexDelta() {

		return retentionIndexDelta;
	}

	public void setRetentionIndexDelta(int retentionIndexDelta) {

		this.retentionIndexDelta = retentionIndexDelta;
	}

	public String getSpecificDatabase() {

		return specificDatabase;
	}

	public void setSpecificDatabase(String specificDatabase) {

		this.specificDatabase = specificDatabase;
	}

	public List<IRetentionIndexLibraryService> getRetentionIndexLibraryServices() {

		return retentionIndexLibraryServices;
	}
}