/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.filter.supplier.centroiding.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.model.core.MassSpectrumPeak;
import org.eclipse.chemclipse.msd.filter.supplier.centroiding.settings.MassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.IonMSn;
import org.eclipse.chemclipse.msd.model.implementation.IonTransition;
import org.eclipse.chemclipse.msd.model.implementation.StandaloneMassSpectrum;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.Test;

public class MassSpectrumFilter_1_Test {

	private static final double PRECURSOR_START = 500.0d;
	private static final double PRECURSOR_STOP = 501.0d;
	private static final double COLLISION_ENERGY = 30.0d;

	@Test
	public void testTandemMS() {

		IStandaloneMassSpectrum massSpectrum = createProfileMassSpectrum(true);
		assertTrue(massSpectrum.isTandemMS());
		applyFilter(massSpectrum);

		assertEquals(MassSpectrumType.CENTROID, massSpectrum.getMassSpectrumType());
		assertEquals(2, massSpectrum.getNumberOfIons());
		assertTrue(massSpectrum.isTandemMS());
		for(IIon ion : massSpectrum.getIons()) {
			IIonTransition ionTransition = assertInstanceOf(IIonMSn.class, ion).getIonTransition();
			assertEquals(PRECURSOR_START, ionTransition.getQ1StartIon());
			assertEquals(PRECURSOR_STOP, ionTransition.getQ1StopIon());
			assertEquals(ion.getIon(), ionTransition.getQ3Ion());
			assertEquals(COLLISION_ENERGY, ionTransition.getCollisionEnergy());
		}
	}

	@Test
	public void testSingleMS() {

		IStandaloneMassSpectrum massSpectrum = createProfileMassSpectrum(false);
		applyFilter(massSpectrum);

		assertEquals(MassSpectrumType.CENTROID, massSpectrum.getMassSpectrumType());
		assertEquals(2, massSpectrum.getNumberOfIons());
		assertFalse(massSpectrum.isTandemMS());
		for(IIon ion : massSpectrum.getIons()) {
			assertFalse(ion instanceof IIonMSn);
		}
	}

	private static void applyFilter(IStandaloneMassSpectrum massSpectrum) {

		List<IScanMSD> massSpectra = List.of(massSpectrum);
		new MassSpectrumFilter().applyFilter(massSpectra, new MassSpectrumFilterSettings(), new NullProgressMonitor());
	}

	private static IStandaloneMassSpectrum createProfileMassSpectrum(boolean tandemMS) {

		IStandaloneMassSpectrum massSpectrum = new StandaloneMassSpectrum();
		massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
		double[] mzs = {99.9d, 100.0d, 100.1d, 199.9d, 200.0d, 200.1d};
		float[] intensities = {10f, 50f, 10f, 20f, 80f, 20f};
		for(int i = 0; i < mzs.length; i++) {
			if(tandemMS) {
				IIonTransition ionTransition = new IonTransition(PRECURSOR_START, PRECURSOR_STOP, mzs[i], mzs[i], COLLISION_ENERGY, 1, 1, 0);
				massSpectrum.addIon(new IonMSn(mzs[i], intensities[i], ionTransition), false);
			} else {
				massSpectrum.addIon(new Ion(mzs[i], intensities[i]), false);
			}
		}
		massSpectrum.getPeaks().add(createPeak(100.0d, 70.0d));
		massSpectrum.getPeaks().add(createPeak(200.0d, 120.0d));
		return massSpectrum;
	}

	private static IMassSpectrumPeak createPeak(double mz, double abundance) {

		IMassSpectrumPeak massSpectrumPeak = new MassSpectrumPeak();
		massSpectrumPeak.setIon(mz);
		massSpectrumPeak.setAbundance(abundance);
		return massSpectrumPeak;
	}
}
