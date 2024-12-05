/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import java.text.DecimalFormat;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.swt.graphics.Image;

public class NoiseSegmentLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String RETENTION_TIME_START = ExtensionMessages.startRetentionTime;
	public static final String RETENTION_TIME_STOP = ExtensionMessages.stopRetentionTime;
	public static final String USER_SELECTION = "User Selection";
	public static final String SCAN_START = "Start Scan";
	public static final String SCAN_STOP = "Stop Scan";
	public static final String SCAN_WIDTH = "Width Scan";
	public static final String USE = "Use";
	public static final String NOISE_FACTOR = "Noise Factor";
	//
	public static final int INDEX_USE = 6;
	//
	public static final String[] TITLES = {//
			RETENTION_TIME_START, //
			RETENTION_TIME_STOP, //
			USER_SELECTION, //
			SCAN_START, //
			SCAN_STOP, //
			SCAN_WIDTH, //
			USE, //
			NOISE_FACTOR //
	};
	//
	public static final int BOUNDS[] = { //
			100, //
			100, //
			30, //
			80, //
			80, //
			80, //
			30, //
			100 //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return getImage(element);
		} else if(columnIndex == INDEX_USE) {
			if(element instanceof INoiseSegment noiseSegment) {
				String fileName = noiseSegment.isUse() ? IApplicationImage.IMAGE_SELECTED : IApplicationImage.IMAGE_DESELECTED;
				return ApplicationImageFactory.getInstance().getImage(fileName, IApplicationImageProvider.SIZE_16x16);
			}
		}
		//
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.0000");
		String text = "";
		if(element instanceof INoiseSegment noiseSegment) {
			switch(columnIndex) {
				case 0:
					text = decimalFormat.format(noiseSegment.getStartRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
					break;
				case 1:
					text = decimalFormat.format(noiseSegment.getStopRetentionTime() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
					break;
				case 2:
					text = noiseSegment.isUserSelection() ? "M" : "A";
					break;
				case 3:
					text = Integer.toString(noiseSegment.getStartScan());
					break;
				case 4:
					text = Integer.toString(noiseSegment.getStopScan());
					break;
				case 5:
					text = Integer.toString(noiseSegment.getWidth());
					break;
				case 6:
					text = "";
					break;
				case 7:
					text = Double.toString(noiseSegment.getNoiseFactor());
					break;
				default:
					text = "n.v.";
			}
		}
		//
		return text;
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_PREFERENCES, IApplicationImageProvider.SIZE_16x16);
	}
}