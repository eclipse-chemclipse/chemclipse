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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.ocx.io;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1003;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1004;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1005;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1006;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1007;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1100;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1300;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1301;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1400;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1500;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1501;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io.ReaderProxy_1502;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.IVendorScanProxy;
import org.eclipse.chemclipse.msd.model.core.IIonTransitionSettings;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.settings.Format;
import org.eclipse.core.runtime.IProgressMonitor;

public class ProxyReaderMSD {

	public void readMassSpectrum(File file, int offset, String version, IVendorScanProxy massSpectrum, IIonTransitionSettings ionTransitionSettings, IProgressMonitor monitor) throws IOException {

		IReaderProxy scanReaderProxy = null;
		if(version.equals(Format.CHROMATOGRAM_VERSION_1003)) {
			scanReaderProxy = new ReaderProxy_1003();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1004)) {
			scanReaderProxy = new ReaderProxy_1004();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1005)) {
			scanReaderProxy = new ReaderProxy_1005();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1006)) {
			scanReaderProxy = new ReaderProxy_1006();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1007)) {
			scanReaderProxy = new ReaderProxy_1007();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1100)) {
			scanReaderProxy = new ReaderProxy_1100();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1300)) {
			scanReaderProxy = new ReaderProxy_1300();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1301)) {
			scanReaderProxy = new ReaderProxy_1301();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1400)) {
			scanReaderProxy = new ReaderProxy_1400();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1500)) {
			scanReaderProxy = new ReaderProxy_1500();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1501)) {
			scanReaderProxy = new ReaderProxy_1501();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1502)) {
			scanReaderProxy = new ReaderProxy_1502();
		}
		//
		if(scanReaderProxy != null) {
			scanReaderProxy.readMassSpectrum(file, offset, massSpectrum, ionTransitionSettings);
		}
	}
}