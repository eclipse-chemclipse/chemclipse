/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.identifier.comparison;

public class MassSpectrumComparisonSupplier implements IMassSpectrumComparisonSupplier {

	private String id = "";
	private String description = "";
	private String comparatorName = "";
	private boolean supportsNominalMS = false;
	private boolean supportsTandemMS = false;
	private boolean supportsHighResolutionMS = false;

	/**
	 * Sets the supplier id like
	 * "org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.xyz".
	 * 
	 * @param id
	 */
	public void setId(String id) {

		if(id != null) {
			this.id = id;
		}
	}

	@Override
	public String getId() {

		return id;
	}

	/**
	 * Sets the description of the mass spectrum comparison supplier.
	 * 
	 * @param description
	 */
	public void setDescription(final String description) {

		if(description != null) {
			this.description = description;
		}
	}

	@Override
	public String getDescription() {

		return description;
	}

	/**
	 * Sets the comparator name of the mass spectrum comparison supplier.
	 * 
	 * @param comparatorName
	 */
	public void setComparatorName(String comparatorName) {

		if(comparatorName != null) {
			this.comparatorName = comparatorName;
		}
	}

	@Override
	public String getComparatorName() {

		return comparatorName;
	}

	@Override
	public boolean supportsNominalMS() {

		return supportsNominalMS;
	}

	protected void setSupportsNominalMS(boolean supportsNominalMS) {

		this.supportsNominalMS = supportsNominalMS;
	}

	@Override
	public boolean supportsTandemMS() {

		return supportsTandemMS;
	}

	protected void setSupportsTandemMS(boolean supportsTandemMS) {

		this.supportsTandemMS = supportsTandemMS;
	}

	@Override
	public boolean supportsHighResolutionMS() {

		return supportsHighResolutionMS;
	}

	protected void setSupportsHighResolutionMS(boolean supportsHighResolutionMS) {

		this.supportsHighResolutionMS = supportsHighResolutionMS;
	}

	// ------------------------------------hashCode, equals, toString
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
		MassSpectrumComparisonSupplier otherSupplier = (MassSpectrumComparisonSupplier)other;
		return id.equals(otherSupplier.id) && description.equals(otherSupplier.description) && comparatorName.equals(otherSupplier.comparatorName);
	}

	@Override
	public int hashCode() {

		return 7 * id.hashCode() + 11 * description.hashCode() + 13 * comparatorName.hashCode();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("id=" + id);
		builder.append(",");
		builder.append("description=" + description);
		builder.append(",");
		builder.append("comparatorName=" + comparatorName);
		builder.append(",");
		builder.append("supportsNominalMS=" + supportsNominalMS);
		builder.append(",");
		builder.append("supportsTandemMS=" + supportsTandemMS);
		builder.append(",");
		builder.append("supportsHighResolutionMS=" + supportsHighResolutionMS);
		builder.append("]");
		return builder.toString();
	}
}
