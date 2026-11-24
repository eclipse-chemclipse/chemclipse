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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.xxd.process.ui.l10n.ProcessMessages;
import org.eclipse.swt.graphics.Image;

public class ProcessorLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String[] TITLES = { //
			ProcessMessages.labelName, //
			ProcessMessages.labelDataType, //
			ProcessMessages.labelCategory, //
			ProcessMessages.labelDescription //
	};

	public static final int[] BOUNDS = { //
			200, //
			100, //
			100, //
			100 //
	};

	public static String getDataTypes(Set<DataCategory> dataTypes) {

		List<String> types = new ArrayList<>();
		for(DataCategory dataType : dataTypes) {
			types.add(dataType.name());
		}

		Collections.sort(types);
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = types.iterator();
		while(iterator.hasNext()) {
			builder.append(iterator.next());
			if(iterator.hasNext()) {
				builder.append(", "); //$NON-NLS-1$
			}
		}

		return builder.toString();
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			if(element instanceof Processor processor) {
				return processor.getMenuIcon();
			}
		}

		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = ""; //$NON-NLS-1$
		if(element instanceof Processor processor) {
			IProcessSupplier<?> processSupplier = processor.getProcessSupplier();
			switch(columnIndex) {
				case 0:
					text = processSupplier.getName();
					break;
				case 1:
					text = getDataTypes(processSupplier.getSupportedDataTypes());
					break;
				case 2:
					text = processSupplier.getCategory();
					break;
				case 3:
					text = processSupplier.getDescription();
					break;
				default:
					text = ProcessMessages.labelNA;
			}
		}
		return text;
	}
}