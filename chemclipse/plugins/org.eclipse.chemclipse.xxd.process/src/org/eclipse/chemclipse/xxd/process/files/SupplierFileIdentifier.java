/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.files;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.converter.methods.MethodConverter;
import org.eclipse.chemclipse.converter.quantitation.QuantDBConverter;
import org.eclipse.chemclipse.converter.sequence.SequenceConverter;
import org.eclipse.chemclipse.csd.converter.chromatogram.ChromatogramConverterCSD;
import org.eclipse.chemclipse.fsd.converter.chromatogram.ChromatogramConverterFSD;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;
import org.eclipse.chemclipse.pcr.converter.core.PlateConverterPCR;
import org.eclipse.chemclipse.processing.converter.AbstractSupplierFileIdentifier;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.vsd.converter.chromatogram.ChromatogramConverterVSD;
import org.eclipse.chemclipse.vsd.converter.core.ScanConverterVSD;
import org.eclipse.chemclipse.wsd.converter.chromatogram.ChromatogramConverterWSD;
import org.eclipse.chemclipse.wsd.converter.core.ScanConverterWSD;

public class SupplierFileIdentifier extends AbstractSupplierFileIdentifier {

	private String type = "";

	public SupplierFileIdentifier(DataType dataType) {

		super(getSupplier(dataType));
		initialize(dataType);
	}

	private static List<ISupplier> getSupplier(DataType dataType) {

		List<ISupplier> supplier = switch(dataType) {
			case MSD_NOMINAL, MSD_TANDEM, MSD_HIGHRES, MSD -> ChromatogramConverterMSD.getInstance().getChromatogramConverterSupport().getSupplier();
			case CSD -> ChromatogramConverterCSD.getInstance().getChromatogramConverterSupport().getSupplier();
			case WSD -> ChromatogramConverterWSD.getInstance().getChromatogramConverterSupport().getSupplier();
			case VSD -> ChromatogramConverterVSD.getInstance().getChromatogramConverterSupport().getSupplier();
			case FSD -> ChromatogramConverterFSD.getInstance().getChromatogramConverterSupport().getSupplier();
			case SCAN_VSD -> ScanConverterVSD.getScanConverterSupport().getSupplier();
			case SCAN_WSD -> ScanConverterWSD.getScanConverterSupport().getSupplier();
			case NMR -> ScanConverterNMR.getScanConverterSupport().getSupplier();
			case PCR -> PlateConverterPCR.getPlateConverterSupport().getSupplier();
			case SEQ -> SequenceConverter.getSequenceConverterSupport().getSupplier();
			case MTH -> MethodConverter.getMethodConverterSupport().getSupplier();
			case QDB -> QuantDBConverter.getQuantDBConverterSupport().getSupplier();
			default -> new ArrayList<>();
		};
		return supplier;
	}

	private void initialize(DataType dataType) {

		type = switch(dataType) {
			case MSD_NOMINAL, MSD_TANDEM, MSD_HIGHRES, MSD -> TYPE_MSD;
			case CSD -> TYPE_CSD;
			case FSD -> TYPE_FSD;
			case WSD -> TYPE_WSD;
			case SCAN_WSD -> TYPE_SCAN_WSD;
			case VSD -> TYPE_VSD;
			case SCAN_VSD -> TYPE_SCAN_VSD;
			case NMR -> TYPE_NMR;
			case PCR -> TYPE_PCR;
			case SEQ -> TYPE_SEQ;
			case MTH -> TYPE_MTH;
			case QDB -> TYPE_QDB;
			default -> "";
		};
	}

	@Override
	public String getType() {

		return type;
	}
}
