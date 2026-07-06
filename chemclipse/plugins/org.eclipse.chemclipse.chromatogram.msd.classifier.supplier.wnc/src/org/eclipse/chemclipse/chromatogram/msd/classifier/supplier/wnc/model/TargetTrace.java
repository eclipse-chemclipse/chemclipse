/*******************************************************************************
 * Copyright (c) 2011, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.model;

public class TargetTrace {

	private int ion;
	private String name;
	private double percentageMaxIntensity;
	private double percentageSumIntensity;

	public TargetTrace(int ion, String name) {

		this.ion = ion;
		/*
		 * Some characters are not allowed.
		 * They are used to persist the entries.
		 */
		name = name.trim();
		name = name.replace(TargetTraces.VALUE_DELIMITER, "");
		name = name.replace(TargetTraces.ENTRY_DELIMITER, "");
		this.name = name;
	}

	public int getIon() {

		return ion;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public double getPercentageMaxIntensity() {

		return percentageMaxIntensity;
	}

	public void setPercentageMaxIntensity(double percentageMaxIntensity) {

		this.percentageMaxIntensity = percentageMaxIntensity;
	}

	public double getPercentageSumIntensity() {

		return percentageSumIntensity;
	}

	public void setPercentageSumIntensity(double percentageSumIntensity) {

		this.percentageSumIntensity = percentageSumIntensity;
	}

	public void copyFrom(TargetTrace targetTrace) {

		this.ion = targetTrace.getIon();
		this.name = targetTrace.getName();
	}

	@Override
	public boolean equals(Object otherObject) {

		if(this == otherObject) {
			return true;
		}
		if(otherObject == null) {
			return false;
		}
		if(getClass() != otherObject.getClass()) {
			return false;
		}
		TargetTrace other = (TargetTrace)otherObject;
		return ion == other.getIon() && name.equals(other.getName());
	}

	@Override
	public int hashCode() {

		return 7 * Integer.hashCode(ion) + 11 * name.hashCode();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("ion=" + ion);
		builder.append(",");
		builder.append("name=" + name);
		builder.append("]");
		return builder.toString();
	}
}