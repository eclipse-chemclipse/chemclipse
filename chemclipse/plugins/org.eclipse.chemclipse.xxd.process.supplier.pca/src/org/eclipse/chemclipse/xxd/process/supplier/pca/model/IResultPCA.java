/*******************************************************************************
 * Copyright (c) 2015, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - add select
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.model;

import org.eclipse.chemclipse.model.statistics.ISample;

public interface IResultPCA {

	ISample getSample();

	double[] getSampleData();

	boolean isDisplayed();

	void setDisplayed(boolean displayed);

	double[] getScoreVector();

	void setScoreVector(double[] eigenSpace);

	double getErrorMemberShip();

	void setErrorMemberShip(double errorMemberShip);

	void setSampleData(double[] sampleData);

	void toggleSelected();

	boolean isSelected();
}