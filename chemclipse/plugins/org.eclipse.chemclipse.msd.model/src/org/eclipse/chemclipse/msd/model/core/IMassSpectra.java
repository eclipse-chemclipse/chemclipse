/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - implementation
 * Matthias Mailänder - add dirty handling
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.support.updates.IUpdateListener;

/**
 * This class stores a list of mass spectra.
 */
public interface IMassSpectra extends IUpdateListener {

	/**
	 * Adds the mass spectrum to the end of the list.
	 */
	void addMassSpectrum(IScanMSD massSpectrum);

	void addMassSpectra(Collection<? extends IScanMSD> massSpectra);

	/**
	 * Removes the mass spectrum from the list.
	 */
	void removeMassSpectrum(IScanMSD massSpectrum);

	/**
	 * Returns the mass spectrum with the given number.<br/>
	 * Be aware, the index is 1 based and not 0 based like in a normal list.<br/>
	 * If no mass spectrum is available, null will be returned.
	 */
	IScanMSD getMassSpectrum(int i);

	/**
	 * Returns a list of stored mass spectra.
	 */
	List<IScanMSD> getList();

	/**
	 * Returns true if the list is empty.
	 */
	boolean isEmpty();

	/**
	 * Returns the number of stored mass spectra.
	 */
	int size();

	/**
	 * Returns the converter id.
	 */
	String getConverterId();

	/**
	 * Sets the converter id.
	 */
	void setConverterId(String converterId);

	/**
	 * Returns the name.
	 */
	String getName();

	/**
	 * Sets the name.
	 */
	void setName(String name);

	/**
	 * Use the update listener to react to updates.
	 */
	void addUpdateListener(IUpdateListener updateListener);

	/**
	 * Remove the specified update listener.
	 */
	void removeUpdateListener(IUpdateListener updateListener);

	/**
	 * This flag marks if a this list of mass spectra has been edited.
	 */
	boolean isDirty();

	/**
	 * This flag marks if this list of mass spectra has been edited.<br/>
	 * It will only be saved if it is dirty. It should save a little bit of
	 * process time.
	 */
	void setDirty(boolean isDirty);
}
