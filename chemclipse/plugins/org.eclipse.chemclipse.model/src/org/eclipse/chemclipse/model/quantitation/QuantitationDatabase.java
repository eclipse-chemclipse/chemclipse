/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.quantitation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class QuantitationDatabase extends HashSet<IQuantitationCompound> implements IQuantitationDatabase {

	private static final long serialVersionUID = 2742894549648464728L;
	/*
	 * Transient
	 * File and ConverterId are set by the DB converter.
	 */
	private File file = null;
	private String converterId = ""; //$NON-NLS-1$

	private String operator = ""; //$NON-NLS-1$
	private String description = ""; //$NON-NLS-1$

	@Override
	public File getFile() {

		return file;
	}

	@Override
	public void setFile(File file) {

		this.file = file;
	}

	@Override
	public String getConverterId() {

		return converterId;
	}

	@Override
	public void setConverterId(String converterId) {

		this.converterId = converterId;
	}

	@Override
	public String getOperator() {

		return operator;
	}

	@Override
	public void setOperator(String operator) {

		this.operator = operator;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public void setDescription(String description) {

		this.description = description;
	}

	@Override
	public List<String> getCompoundNames() {

		List<String> compoundNames = new ArrayList<>();
		for(IQuantitationCompound quantitationCompound : this) {
			compoundNames.add(quantitationCompound.getName());
		}
		return compoundNames;
	}

	@Override
	public IQuantitationCompound getQuantitationCompound(String name) {

		for(IQuantitationCompound quantitationCompound : this) {
			if(quantitationCompound.getName().equals(name)) {
				return quantitationCompound;
			}
		}
		return null;
	}

	@Override
	public boolean containsQuantitationCompund(String name) {

		return (getQuantitationCompound(name) != null) ? true : false;
	}
}
