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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.support.updates.IUpdateListener;

public abstract class AbstractMassSpectra implements IMassSpectra {

	private final List<IScanMSD> massSpectra;
	private String converterId = "";
	private String name = "";
	private boolean dirty;
	private final List<IUpdateListener> updateListeners;

	protected AbstractMassSpectra(List<IScanMSD> massSpectra) {

		this.massSpectra = massSpectra;
		updateListeners = new ArrayList<>();
	}

	/**
	 * Initialize mass spectra and create a new internal mass spectra list.
	 */
	protected AbstractMassSpectra() {

		this(new ArrayList<>());
	}

	@Override
	public void addMassSpectrum(IScanMSD massSpectrum) {

		if(massSpectrum != null) {
			massSpectra.add(massSpectrum);
		}
	}

	@Override
	public void addMassSpectra(Collection<? extends IScanMSD> massSpectra) {

		for(IScanMSD massSpectrum : massSpectra) {
			addMassSpectrum(massSpectrum);
		}
	}

	@Override
	public void removeMassSpectrum(IScanMSD massSpectrum) {

		if(massSpectrum != null) {
			massSpectra.remove(massSpectrum);
		}
	}

	@Override
	public IScanMSD getMassSpectrum(int i) {

		IScanMSD massSpectrum = null;
		if(i > 0 && i <= massSpectra.size()) {
			massSpectrum = massSpectra.get(--i);
		}
		return massSpectrum;
	}

	@Override
	public int size() {

		return massSpectra.size();
	}

	@Override
	public boolean isEmpty() {

		return massSpectra.isEmpty();
	}

	@Override
	public List<IScanMSD> getList() {

		return massSpectra;
	}

	@Override
	public String getConverterId() {

		return converterId;
	}

	@Override
	public void setConverterId(String converterId) {

		this.converterId = converterId;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public void setName(String name) {

		this.name = name;
	}

	@Override
	public void update() {

		for(IUpdateListener updateListener : updateListeners) {
			updateListener.update();
		}
	}

	@Override
	public void addUpdateListener(IUpdateListener updateListener) {

		updateListeners.add(updateListener);
	}

	@Override
	public void removeUpdateListener(IUpdateListener updateListener) {

		updateListeners.remove(updateListener);
	}

	@Override
	public boolean isDirty() {

		return dirty;
	}

	@Override
	public void setDirty(boolean dirty) {

		this.dirty = dirty;
	}
}