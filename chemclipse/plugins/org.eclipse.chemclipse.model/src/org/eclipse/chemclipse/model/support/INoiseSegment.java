/*******************************************************************************
 * Copyright (c) 2019, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - corrected the name scheme
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

import java.io.Serializable;

public interface INoiseSegment extends IAnalysisSegment, Serializable {

	double getNoiseFactor();

	void setNoiseFactor(double noiseFactor);

	String getTraces();

	boolean isUse();

	void setUse(boolean use);

	boolean isUserSelection();

	void setUserSelection(boolean userSelection);
}