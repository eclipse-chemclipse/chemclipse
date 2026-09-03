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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.support.history.ISupplierEditHistory;

/**
 * An interface for single MALDI-TOF MS spectra which contain additional metadata.
 * 
 * @author Matthias Mailänder
 */
public interface IStandaloneMassSpectrum extends IRegularMassSpectrum, ISupplierEditHistory {

	/**
	 * Returns the file, see setFile().
	 * May return null.
	 * 
	 * @return File
	 */
	File getFile();

	/**
	 * Set the file of the mass spectrum, e.g. if it is a MALDI-MS record.
	 * If it's a GC/MS run, file is not needed cause the chromatogram holds the scans.
	 * 
	 * @param file
	 */
	void setFile(File file);

	/**
	 * Returns the name of the mass spectrum, if it's e.g. a MALDI-MS record.
	 * 
	 * @return String
	 */
	String getName();

	String getSampleName();

	void setSampleName(String name);

	/**
	 * @return ID of the MALDI target
	 */
	String getPlate();

	void setPlate(String plate);

	/**
	 * @return location (coordinate) on the plate
	 */
	String getPosition();

	void setPosition(String plateLocation);

	String getDescription();

	void setDescription(String description);

	String getOperator();

	void setOperator(String operator);

	Date getDate();

	void setDate(Date date);

	String getInstrument();

	void setInstrument(String instrument);

	List<IMassSpectrumPeak> getPeaks();

	List<Double> getNoise();

	List<Double> getBaseline();

	void setBaseline(List<Double> baseline);
}
