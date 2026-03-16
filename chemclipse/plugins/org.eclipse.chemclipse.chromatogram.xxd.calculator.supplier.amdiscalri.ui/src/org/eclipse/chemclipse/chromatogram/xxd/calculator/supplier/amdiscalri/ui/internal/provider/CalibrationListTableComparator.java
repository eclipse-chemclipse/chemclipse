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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.internal.provider;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.CalibrationFile;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class CalibrationListTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof CalibrationFile calibrationFile1 && e2 instanceof CalibrationFile calibrationFile2) {

			String column1 = calibrationFile1.getSeparationColumnIndices().getSeparationColumn().getName();
			String column2 = calibrationFile2.getSeparationColumnIndices().getSeparationColumn().getName();
			String type1 = calibrationFile1.getSeparationColumnIndices().getSeparationColumn().getSeparationColumnType().label();
			String type2 = calibrationFile2.getSeparationColumnIndices().getSeparationColumn().getSeparationColumnType().label();

			switch(getPropertyIndex()) {
				case 0:
					sortOrder = column2.compareTo(column1);
					break;
				case 1:
					sortOrder = type2.compareTo(type1);
					break;
				case 2:
					sortOrder = calibrationFile2.getFile().getName().compareTo(calibrationFile1.getFile().getName());
					break;
				case 3:
					sortOrder = calibrationFile2.getFile().getAbsolutePath().compareTo(calibrationFile1.getFile().getAbsolutePath());
					break;
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}
