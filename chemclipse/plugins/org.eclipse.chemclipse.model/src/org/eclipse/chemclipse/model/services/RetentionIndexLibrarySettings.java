/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.services;

import java.util.ArrayList;
import java.util.List;

public class RetentionIndexLibrarySettings {

	private String searchColumn = "DB5";
	private boolean caseSensitive = false;
	private boolean removeWhiteSpace = false;
	private int retentionIndexDelta = 10;
	private String specificDatabase = ""; // If empty, take all.
	//
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