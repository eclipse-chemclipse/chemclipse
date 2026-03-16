/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.process.ui.toolbar;

import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class ProcessorTableComparator extends AbstractRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof Processor processor1 && e2 instanceof Processor processor2) {

			IProcessSupplier<?> processSupplier1 = processor1.getProcessSupplier();
			IProcessSupplier<?> processSupplier2 = processor2.getProcessSupplier();

			switch(getPropertyIndex()) {
				case 0:
					sortOrder = processSupplier2.getName().compareTo(processSupplier1.getName());
					break;
				case 1:
					sortOrder = ProcessorLabelProvider.getDataTypes(processSupplier2.getSupportedDataTypes()).compareTo(ProcessorLabelProvider.getDataTypes(processSupplier1.getSupportedDataTypes()));
					break;
				case 2:
					sortOrder = processSupplier2.getCategory().compareTo(processSupplier1.getCategory());
					break;
				case 3:
					sortOrder = processSupplier2.getDescription().compareTo(processSupplier1.getDescription());
					break;
				case 4:
					sortOrder = processSupplier2.getId().compareTo(processSupplier1.getId());
					break;
				default:
					sortOrder = 0;
			}
		}

		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}

		return sortOrder;
	}
}
