/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - prediction
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.swt.graphics.Image;

public class SamplesLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String SAMPLE_NAME = "Sample Name";
	public static final String USE = "Use";
	public static final String PREDICT = "Predict";
	public static final String COLOR = "Color";
	public static final String GROUP_NAME = "Group Name";
	public static final String CLASSIFICATION = "Classification";
	public static final String DESCRIPTION = "Description";
	//
	public static final int INDEX_COLOR = 3;
	//
	public static String[] TITLES = {//
			SAMPLE_NAME, //
			USE, //
			PREDICT, //
			COLOR, //
			GROUP_NAME, //
			CLASSIFICATION, //
			DESCRIPTION //
	};
	//
	public static int[] BOUNDS = {//
			300, //
			30, //
			30, //
			30, //
			100, //
			100, //
			300 //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return getImage(element);
		} else if(columnIndex == 1) {
			if(element instanceof ISample sample) {
				if(sample.isSelected()) {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SELECTED, IApplicationImageProvider.SIZE_16x16);
				} else {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DESELECTED, IApplicationImageProvider.SIZE_16x16);
				}
			}
		} else if(columnIndex == 2) {
			if(element instanceof ISample sample) {
				if(sample.isPredicted()) {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SELECTED, IApplicationImageProvider.SIZE_16x16);
				} else {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DESELECTED, IApplicationImageProvider.SIZE_16x16);
				}
			}
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		if(element instanceof ISample sample) {
			//
			switch(columnIndex) {
				case 0:
					text = sample.getSampleName() != null ? sample.getSampleName() : "";
					break;
				case 1:
					text = ""; // Checkbox
					break;
				case 2:
					text = ""; // Checkbox
					break;
				case 3:
					text = ""; // Color
					break;
				case 4:
					text = sample.getGroupName() != null ? sample.getGroupName() : "";
					break;
				case 5:
					text = sample.getClassification() != null ? sample.getClassification() : "";
					break;
				case 6:
					text = sample.getDescription() != null ? sample.getDescription() : "";
					break;
			}
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SAMPLE, IApplicationImageProvider.SIZE_16x16);
	}
}
