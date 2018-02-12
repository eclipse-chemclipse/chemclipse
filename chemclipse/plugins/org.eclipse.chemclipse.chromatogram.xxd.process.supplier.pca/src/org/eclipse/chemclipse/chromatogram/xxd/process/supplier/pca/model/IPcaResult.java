/*******************************************************************************
 * Copyright (c) 2015, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.model;

import javafx.beans.Observable;
import javafx.util.Callback;

public interface IPcaResult {

	static Callback<IPcaResult, Observable[]> extractor() {

		return (IPcaResult r) -> new Observable[]{r.getSample().selectedProperty()};
	}

	double[] getScoreVector();

	double getErrorMemberShip();

	String getGroupName();

	String getName();

	ISample<? extends ISampleData> getSample();

	double[] getSampleData();

	boolean isDisplayed();

	void setDisplayed(boolean displayed);

	void setScoreVector(double[] eigenSpace);

	void setErrorMemberShip(double errorMemberShip);

	void setGroupName(String groupName);

	void setName(String name);

	void setSampleData(double[] sampleData);
}