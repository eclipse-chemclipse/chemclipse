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
package org.eclipse.chemclipse.model.instruments;

import org.eclipse.chemclipse.support.util.InstrumentListUtil;

public class Instrument {

	/*
	 * Use an empty string as the default id.
	 */
	private String identifier = ""; //$NON-NLS-1$
	private String name = ""; //$NON-NLS-1$
	private String description = ""; //$NON-NLS-1$

	public Instrument() {

		this("", "Default", "This is the default instrument."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public Instrument(String identifier, String name, String description) {

		setIdentifier(identifier);
		setName(name);
		setDescription(description);
	}

	public String getIdentifier() {

		return identifier;
	}

	/**
	 * Only alphanumeric values are allowed.
	 * Non-allowed characters will be removed.
	 */
	public void setIdentifier(String identifier) {

		identifier = removeNonAlphaNumericTokens(identifier);
		identifier = removeSeparatorTokens(identifier);
		this.identifier = identifier;
	}

	public String getName() {

		return name;
	}

	/**
	 * Non-allowed characters will be removed.
	 */
	public void setName(String name) {

		this.name = removeSeparatorTokens(name);
	}

	public String getDescription() {

		return description;
	}

	/**
	 * Non-allowed characters will be removed.
	 */
	public void setDescription(String description) {

		this.description = removeSeparatorTokens(description);
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Instrument other = (Instrument)obj;
		if(identifier == null) {
			if(other.identifier != null)
				return false;
		} else if(!identifier.equals(other.identifier))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "Instrument [identifier=" + identifier + // //$NON-NLS-1$
				", name=" + name + // //$NON-NLS-1$
				", description=" + description + // //$NON-NLS-1$
				"]"; //$NON-NLS-1$
	}

	private String removeNonAlphaNumericTokens(String value) {

		return value.replaceAll("\\P{Alnum}", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private String removeSeparatorTokens(String value) {

		return value.replaceAll("\\" + InstrumentListUtil.SEPARATOR_ENTRY, "") // //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll(InstrumentListUtil.SEPARATOR_TOKEN, ""); //$NON-NLS-1$
	}
}
