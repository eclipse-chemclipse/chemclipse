/*******************************************************************************
 * Copyright (c) 2014, 2016 Dr. Philip Wenig.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.identifier.supplier.file.settings;

import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.AbstractMassSpectrumIdentifierSettings;

public class FileMassSpectrumIdentifierSettings extends AbstractMassSpectrumIdentifierSettings implements IFileMassSpectrumIdentifierSettings {

	private String massSpectraFile;
	private int numberOfTargets;
	private float minMatchFactor;
	private float minReverseMatchFactor;
	private boolean addUnknownMzListTarget;

	@Override
	public String getMassSpectraFile() {

		return massSpectraFile;
	}

	@Override
	public void setMassSpectraFile(String massSpectraFile) {

		this.massSpectraFile = massSpectraFile;
	}

	@Override
	public int getNumberOfTargets() {

		return numberOfTargets;
	}

	@Override
	public void setNumberOfTargets(int numberOfTargets) {

		this.numberOfTargets = numberOfTargets;
	}

	@Override
	public float getMinMatchFactor() {

		return minMatchFactor;
	}

	@Override
	public void setMinMatchFactor(float minMatchFactor) {

		this.minMatchFactor = minMatchFactor;
	}

	@Override
	public float getMinReverseMatchFactor() {

		return minReverseMatchFactor;
	}

	@Override
	public void setMinReverseMatchFactor(float minReverseMatchFactor) {

		this.minReverseMatchFactor = minReverseMatchFactor;
	}

	@Override
	public boolean isAddUnknownMzListTarget() {

		return addUnknownMzListTarget;
	}

	@Override
	public void setAddUnknownMzListTarget(boolean addUnknownMzListTarget) {

		this.addUnknownMzListTarget = addUnknownMzListTarget;
	}
}
