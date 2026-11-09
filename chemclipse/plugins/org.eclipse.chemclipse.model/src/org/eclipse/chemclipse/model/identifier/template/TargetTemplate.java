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
package org.eclipse.chemclipse.model.identifier.template;

public class TargetTemplate {

	private String name = ""; //$NON-NLS-1$
	private String casNumber = ""; //$NON-NLS-1$
	private String comments = ""; //$NON-NLS-1$
	private String contributor = ""; //$NON-NLS-1$
	private String referenceId = ""; //$NON-NLS-1$

	public TargetTemplate() {

		this("", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public TargetTemplate(String name, String casNumber) {

		this.name = name;
		this.casNumber = casNumber;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getCasNumber() {

		return casNumber;
	}

	public void setCasNumber(String casNumber) {

		this.casNumber = casNumber;
	}

	public String getComments() {

		return comments;
	}

	public void setComments(String comments) {

		this.comments = comments;
	}

	public String getContributor() {

		return contributor;
	}

	public void setContributor(String contributor) {

		this.contributor = contributor;
	}

	public String getReferenceId() {

		return referenceId;
	}

	public void setReferenceId(String referenceId) {

		this.referenceId = referenceId;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		TargetTemplate other = (TargetTemplate)obj;
		if(name == null) {
			if(other.name != null)
				return false;
		} else if(!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "TargetTemplate [name=" + name + ", casNumber=" + casNumber + ", comments=" + comments + ", contributor=" + contributor + ", referenceId=" + referenceId + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	}
}
