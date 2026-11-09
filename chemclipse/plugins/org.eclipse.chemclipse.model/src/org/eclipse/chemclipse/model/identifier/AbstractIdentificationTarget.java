/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.exceptions.ReferenceMustNotBeNullException;
import org.eclipse.chemclipse.model.targets.AbstractTarget;

public abstract class AbstractIdentificationTarget extends AbstractTarget implements IIdentificationTarget {

	private static final long serialVersionUID = 8447935680974437678L;

	private ILibraryInformation libraryInformation;
	private IComparisonResult comparisonResult;
	private String identifier = ""; //$NON-NLS-1$
	private boolean verified = false;

	/**
	 * Set the libraryInformation and comparisonResult.
	 * 
	 * @param libraryInformation
	 * @param comparisonResult
	 * @throws ReferenceMustNotBeNullException
	 */
	public AbstractIdentificationTarget(ILibraryInformation libraryInformation, IComparisonResult comparisonResult) throws ReferenceMustNotBeNullException {

		if(libraryInformation == null) {
			throw new ReferenceMustNotBeNullException("The reference libraryInformation must be not null."); //$NON-NLS-1$
		}
		if(comparisonResult == null) {
			throw new ReferenceMustNotBeNullException("The reference comparisonResult must be not null."); //$NON-NLS-1$
		}
		this.libraryInformation = libraryInformation;
		this.comparisonResult = comparisonResult;
	}

	@Override
	public ILibraryInformation getLibraryInformation() {

		return libraryInformation;
	}

	@Override
	public IComparisonResult getComparisonResult() {

		return comparisonResult;
	}

	@Override
	public String getIdentifier() {

		return identifier;
	}

	@Override
	public void setIdentifier(String identifier) {

		if(identifier != null) {
			this.identifier = identifier;
		}
	}

	@Override
	public boolean isVerified() {

		return verified;
	}

	@Override
	public void setVerified(boolean verified) {

		this.verified = verified;
	}

	@Override
	public boolean equals(Object other) {

		if(other == null) {
			return false;
		}
		if(this == other) {
			return true;
		}
		if(this.getClass() != other.getClass()) {
			return false;
		}
		IIdentificationTarget otherEntry = (IIdentificationTarget)other;
		return getIdentifier().equals(otherEntry.getIdentifier()) && //
				getLibraryInformation().getName().equals(otherEntry.getLibraryInformation().getName()) && //
				getLibraryInformation().getCasNumber().equals(otherEntry.getLibraryInformation().getCasNumber()) && //
				getComparisonResult().getMatchFactor() == otherEntry.getComparisonResult().getMatchFactor() && //
				getComparisonResult().getReverseMatchFactor() == otherEntry.getComparisonResult().getReverseMatchFactor();
	}

	@Override
	public int hashCode() {

		return 7 * identifier.hashCode() + //
				11 * getLibraryInformation().getName().hashCode() + //
				13 * getLibraryInformation().getCasNumber().hashCode() + //
				11 * Float.valueOf(getComparisonResult().getMatchFactor()).hashCode() + //
				7 * Float.valueOf(getComparisonResult().getReverseMatchFactor()).hashCode();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("["); //$NON-NLS-1$
		builder.append("identifier=" + identifier); //$NON-NLS-1$
		builder.append(","); //$NON-NLS-1$
		builder.append("name=" + getLibraryInformation().getName()); //$NON-NLS-1$
		builder.append(","); //$NON-NLS-1$
		builder.append("cas=" + getLibraryInformation().getCasNumber()); //$NON-NLS-1$
		builder.append(","); //$NON-NLS-1$
		builder.append(getComparisonResult().toString());
		builder.append("]"); //$NON-NLS-1$
		return builder.toString();
	}
}