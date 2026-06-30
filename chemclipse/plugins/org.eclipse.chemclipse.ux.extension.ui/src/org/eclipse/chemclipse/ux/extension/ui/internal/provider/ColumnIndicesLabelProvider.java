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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.internal.provider;

import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.swt.graphics.Image;

public class ColumnIndicesLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String RETENTION_INDEX = "Retention Index";
	public static final String NAME = "Name";
	public static final String COLUMN_TYPE = "Column Type";
	public static final String COLUMN_PACKAGING = "Column Packaging";
	public static final String CALCULATION_TYPE = "Calculation Type";
	public static final String LENGTH = "Length";
	public static final String DIAMETER = "Diameter";
	public static final String PHASE = "Phase";
	public static final String THICKNESS = "Thickness";

	public static final String[] TITLES = { //
			RETENTION_INDEX, //
			NAME, //
			COLUMN_TYPE, //
			COLUMN_PACKAGING, //
			CALCULATION_TYPE, //
			LENGTH, //
			DIAMETER, //
			PHASE, //
			THICKNESS //
	};

	public static final int[] BOUNDS = { //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100, //
			100 //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return getImage(element);
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		if(element instanceof IColumnIndexMarker columnIndexMarker) {
			ISeparationColumn separationColumn = columnIndexMarker.getSeparationColumn();
			switch(columnIndex) {
				case 0:
					text = Float.toString(columnIndexMarker.getRetentionIndex());
					break;
				case 1:
					text = separationColumn.getName();
					break;
				case 2:
					text = separationColumn.getSeparationColumnType().label();
					break;
				case 3:
					text = separationColumn.getSeparationColumnPackaging().label();
					break;
				case 4:
					text = separationColumn.getCalculationType();
					break;
				case 5:
					text = separationColumn.getLength();
					break;
				case 6:
					text = separationColumn.getDiameter();
					break;
				case 7:
					text = separationColumn.getPhase();
					break;
				case 8:
					text = separationColumn.getThickness();
					break;
				default:
					text = "n.v.";
			}
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RETENION_INDEX, IApplicationImageProvider.SIZE_16x16);
	}
}