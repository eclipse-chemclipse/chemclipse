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
 * Jan Holy - implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;
import org.eclipse.chemclipse.support.literature.LiteratureReference;

public abstract class AbstractSupplier<S extends IIdentifierSettings> implements ISupplierSetter {

	private String description = ""; //$NON-NLS-1$
	private String id = ""; //$NON-NLS-1$
	private String identifierName = ""; //$NON-NLS-1$
	private Class<? extends S> identifierSettingsClass;
	private List<LiteratureReference> literatureReference = new ArrayList<>();

	@Override
	public boolean equals(final Object otherObject) {

		if(this == otherObject) {
			return true;
		}
		if(otherObject == null) {
			return false;
		}
		if(getClass() != otherObject.getClass()) {
			return false;
		}
		ISupplier other = (ISupplier)otherObject;
		return id.equals(other.getId()) && description.equals(other.getDescription()) && identifierName.equals(other.getIdentifierName());
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public String getId() {

		return id;
	}

	@Override
	public String getIdentifierName() {

		return identifierName;
	}

	protected Class<? extends S> getSpecificIdentifierSettingsClass() {

		return this.identifierSettingsClass;
	}

	@Override
	public int hashCode() {

		return id.hashCode() + description.hashCode() + identifierName.hashCode();
	}

	@Override
	public void setDescription(final String description) {

		if(description != null) {
			this.description = description;
		}
	}

	@Override
	public void setId(final String id) {

		if(id != null) {
			this.id = id;
		}
	}

	@Override
	public void setIdentifierName(final String identifierName) {

		if(identifierName != null) {
			this.identifierName = identifierName;
		}
	}

	public void setIdentifierSettingsClass(Class<? extends S> identifierSettingsClass) {

		this.identifierSettingsClass = identifierSettingsClass;
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		return literatureReference;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("["); //$NON-NLS-1$
		builder.append("id=" + this.id); //$NON-NLS-1$
		builder.append(","); //$NON-NLS-1$
		builder.append("description=" + this.description); //$NON-NLS-1$
		builder.append(","); //$NON-NLS-1$
		builder.append("identifierName=" + this.identifierName); //$NON-NLS-1$
		builder.append("]"); //$NON-NLS-1$
		return builder.toString();
	}
}
