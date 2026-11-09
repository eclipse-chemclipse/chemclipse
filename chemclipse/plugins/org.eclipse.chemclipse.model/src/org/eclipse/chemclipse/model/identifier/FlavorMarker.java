/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FlavorMarker implements IFlavorMarker {

	private static final long serialVersionUID = -2057882168172746294L;

	private boolean manuallyVerified = false;
	private String odor = ""; //$NON-NLS-1$
	private String matrix = ""; //$NON-NLS-1$
	private String solvent = ""; //$NON-NLS-1$
	private String samplePreparation = ""; //$NON-NLS-1$
	private String literatureReference = ""; // if possible, DOI //$NON-NLS-1$
	private Set<IOdorThreshold> odorThresholds = new HashSet<>();

	public FlavorMarker(String odor, String matrix, String solvent) {

		this.odor = odor;
		this.matrix = matrix;
		this.solvent = solvent;
	}

	@Override
	public void clear() {

		manuallyVerified = false;
		odor = ""; //$NON-NLS-1$
		matrix = ""; //$NON-NLS-1$
		solvent = ""; //$NON-NLS-1$
		samplePreparation = ""; //$NON-NLS-1$
		literatureReference = ""; //$NON-NLS-1$
		odorThresholds.clear();
	}

	@Override
	public boolean isManuallyVerified() {

		return manuallyVerified;
	}

	@Override
	public void setManuallyVerified(boolean manuallyVerified) {

		this.manuallyVerified = manuallyVerified;
	}

	@Override
	public String getOdor() {

		return odor;
	}

	@Override
	public String getMatrix() {

		return matrix;
	}

	@Override
	public String getSolvent() {

		return solvent;
	}

	@Override
	public String getSamplePreparation() {

		return samplePreparation;
	}

	@Override
	public void setSamplePreparation(String samplePreparation) {

		this.samplePreparation = samplePreparation;
	}

	@Override
	public String getLiteratureReference() {

		return literatureReference;
	}

	@Override
	public void setLiteratureReference(String literatureReference) {

		this.literatureReference = literatureReference;
	}

	@Override
	public void add(IOdorThreshold flavorThreshold) {

		odorThresholds.add(flavorThreshold);
	}

	@Override
	public void remove(IOdorThreshold flavorThreshold) {

		odorThresholds.remove(flavorThreshold);
	}

	@Override
	public void clearOdorThresholds() {

		odorThresholds.clear();
	}

	@Override
	public Set<IOdorThreshold> getOdorThresholds() {

		return Collections.unmodifiableSet(odorThresholds);
	}

	@Override
	public int hashCode() {

		return Objects.hash(matrix, odor, solvent);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		FlavorMarker other = (FlavorMarker)obj;
		return Objects.equals(matrix, other.matrix) && Objects.equals(odor, other.odor) && Objects.equals(solvent, other.solvent);
	}

	@Override
	public String toString() {

		return "FlavorMarker [odorQuality=" + odor + ", matrix=" + matrix + ", solvent=" + solvent + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}