/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.fsd.model.core.implementation;

import java.util.TreeSet;

import org.eclipse.chemclipse.fsd.model.core.ISignalFSD;
import org.eclipse.chemclipse.fsd.model.core.ISpectrumFSD;
import org.eclipse.chemclipse.model.core.AbstractMeasurementInfo;

public class SpectrumFSD extends AbstractMeasurementInfo implements ISpectrumFSD {

	private static final long serialVersionUID = -8354507225461817214L;
	//
	private TreeSet<ISignalFSD> emission = new TreeSet<>();
	private TreeSet<ISignalFSD> excitation = new TreeSet<>();

	@Override
	public TreeSet<ISignalFSD> getExcitation() {

		return emission;
	}

	@Override
	public TreeSet<ISignalFSD> getEmission() {

		return excitation;
	}
}