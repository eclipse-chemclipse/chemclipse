/*******************************************************************************
 * Copyright (c) 2013, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Jan Holy - implementation
 * Lorenz Gerber - add select
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.model;

import org.eclipse.chemclipse.model.statistics.ISample;

public class ResultPCA implements IResultPCA {

	private ISample sample;
	private boolean isDisplayed;
	private double[] scoreVector;
	private double errorMemberShip;
	private double[] sampleData;
	private boolean isSelected;

	public ResultPCA(ISample sample) {

		this.isDisplayed = true;
		this.sample = sample;
		this.isSelected = false;
	}

	@Override
	public double[] getScoreVector() {

		return scoreVector;
	}

	@Override
	public double getErrorMemberShip() {

		return errorMemberShip;
	}

	@Override
	public ISample getSample() {

		return sample;
	}

	@Override
	public double[] getSampleData() {

		return sampleData;
	}

	@Override
	public boolean isDisplayed() {

		return isDisplayed;
	}

	@Override
	public void setDisplayed(boolean displayed) {

		this.isDisplayed = displayed;
	}

	@Override
	public void setScoreVector(double[] scoreVector) {

		this.scoreVector = scoreVector;
	}

	@Override
	public void setErrorMemberShip(double errorMemberShip) {

		this.errorMemberShip = errorMemberShip;
	}

	@Override
	public void setSampleData(double[] sampleData) {

		this.sampleData = sampleData;
	}

	@Override
	public void toggleSelected() {

		if(this.isSelected) {
			this.isSelected = false;
		} else {
			this.isSelected = true;
		}
	}

	@Override
	public boolean isSelected() {

		return this.isSelected;
	}
}