/*******************************************************************************
 * Copyright (c) 2011, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.model;

public interface IDataInputEntry {

	/**
	 * Return the name of input file with extension
	 *
	 * @return
	 */
	String getFileName();

	String getGroupName();

	/**
	 * Returns the path to the input file.
	 *
	 * @return String
	 */
	String getInputFile();

	/**
	 * Returns the name of the input file without extension.
	 *
	 * @return String
	 */
	String getSampleName();

	void setGroupName(String groupName);
}