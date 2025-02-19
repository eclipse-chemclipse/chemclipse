/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - prediction
 *******************************************************************************/
package org.eclipse.chemclipse.model.statistics;

import java.util.List;

public interface ISample {

	String getSampleName();

	void setSampleName(String sampleName);

	String getGroupName();

	void setGroupName(String groupName);

	String getClassification();

	void setClassification(String classification);

	String getDescription();

	void setDescription(String description);

	List<? extends ISampleData<?>> getSampleData();

	boolean isSelected();

	void setSelected(boolean selected);

	boolean isPredicted();

	void setPredicted(boolean predict);

	/**
	 * For example red: 255,0,0
	 * The color will be mapped to an SWT color in the UI.
	 * This is the model bundle, hence no UI related code is allowed.
	 * 
	 * @return String
	 */
	String getRGB();

	/**
	 * For example red: 255,0,0
	 * 
	 * @param rgb
	 */
	void setRGB(String rgb);
}
