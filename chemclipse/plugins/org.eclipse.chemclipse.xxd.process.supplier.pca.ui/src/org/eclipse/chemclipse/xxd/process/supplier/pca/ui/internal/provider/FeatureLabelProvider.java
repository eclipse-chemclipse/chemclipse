/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - add Feature Mode
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider;

import java.text.DecimalFormat;
import java.util.List;

import org.eclipse.chemclipse.model.statistics.ISampleData;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.FeatureMode;
import org.eclipse.swt.graphics.Image;

public class FeatureLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String VARIABLE = "Variable"; // e.g. Retention Time //$NON-NLS-1$
	public static final String USE = "Use"; //$NON-NLS-1$
	public static final String CLASSIFICATION = "Classification"; //$NON-NLS-1$
	public static final String DESCRIPTION = "Description"; //$NON-NLS-1$

	public static final int BOUND_SAMPLE = 100;
	private DecimalFormat decimalFormat = getDecimalFormat();
	private DecimalFormat scientificFormat = getScientificDecimalFormatInstance();
	private FeatureMode featureMode = FeatureMode.ORIGINAL;

	public static final String[] TITLES = {//
			VARIABLE, //
			USE, //
			CLASSIFICATION, //
			DESCRIPTION //
	};

	public static final int[] BOUNDS = {//
			100, //
			30, //
			150, //
			200 //
	};

	public FeatureLabelProvider(String pattern, FeatureMode featureMode) {

		super(pattern);
		this.featureMode = featureMode;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return getImage(element);
		} else if(columnIndex == 1) {
			if(element instanceof Feature feature) {
				IVariable variable = feature.getVariable();
				if(variable.isSelected()) {
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

		String text = ""; //$NON-NLS-1$
		if(element instanceof Feature feature) {
			IVariable variable = feature.getVariable();

			switch(columnIndex) {
				case 0:
					text = variable.getValue();
					break;
				case 1:
					text = ""; // Icon //$NON-NLS-1$
					break;
				case 2:
					text = variable.getClassification();
					break;
				case 3:
					text = variable.getDescription();
					break;
				default:
					int index = columnIndex - 4;
					List<ISampleData<?>> sampleData = feature.getSampleData();
					if(sampleData.size() > index) {
						double value;
						if(featureMode.equals(FeatureMode.ORIGINAL)) {
							value = sampleData.get(index).getData();
						} else {
							value = sampleData.get(index).getModifiedData();
						}
						text = Double.isNaN(value) ? "NaN" : format(value); //$NON-NLS-1$
					} else {
						text = "--"; //$NON-NLS-1$
					}
					break;
			}
		}
		return text;
	}

	/**
	 * Original values can be small (e.g. concentrations) - show them in scientific notation instead of
	 * letting the default pattern round them to 0.
	 */
	private String format(double value) {

		if(featureMode.equals(FeatureMode.ORIGINAL) && value != 0 && Math.abs(value) < 0.01) {
			return scientificFormat.format(value);
		}
		return decimalFormat.format(value);
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SAMPLE, IApplicationImageProvider.SIZE_16x16);
	}
}
