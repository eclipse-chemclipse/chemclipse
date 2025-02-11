/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.tsd.model.core.TraceRange;
import org.eclipse.chemclipse.tsd.model.support.TraceRangeSupport;
import org.eclipse.swt.graphics.Image;

public class TraceRangeLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String RETENTION_TIME_COLUMN1_START = "Retention Time Start (Column1) [min]";
	public static final String RETENTION_TIME_COLUMN1_STOP = "Retention Time Stop (Column1) [min]";
	public static final String RETENTION_TIME_COLUMN2_START = "Retention Time Start (Column2) [s]";
	public static final String RETENTION_TIME_COLUMN2_STOP = "Retention Time Stop (Column2) [s]";
	public static final String SCAN_INDICES_COLUMN2 = "Scan Indices (Column2)";
	public static final String NAME = "Name";
	public static final String TRACES = "Traces";
	public static final String SECOND_DIMENSION_HINT = "Second Dimension Hint";
	//
	public static final String[] TITLES = { //
			RETENTION_TIME_COLUMN1_START, //
			RETENTION_TIME_COLUMN1_STOP, //
			RETENTION_TIME_COLUMN2_START, //
			RETENTION_TIME_COLUMN2_STOP, //
			SCAN_INDICES_COLUMN2, //
			NAME, //
			TRACES, //
			SECOND_DIMENSION_HINT //
	};
	public static final int[] BOUNDS = { //
			80, //
			80, //
			80, //
			80, //
			100, //
			100, //
			200, //
			100 //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return getImage(element);
		} else {
			return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		if(element instanceof TraceRange traceRange) {
			switch(columnIndex) {
				case 0:
					text = TraceRangeSupport.DF_COLUMN_1_MINUTES.format(traceRange.getRetentionTimeColumn1Start() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
					break;
				case 1:
					text = TraceRangeSupport.DF_COLUMN_1_MINUTES.format(traceRange.getRetentionTimeColumn1Stop() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
					break;
				case 2:
					text = TraceRangeSupport.DF_COLUMN_2_SECONDS.format(traceRange.getRetentionTimeColumn2Start() / IChromatogramOverview.SECOND_CORRELATION_FACTOR);
					break;
				case 3:
					text = TraceRangeSupport.DF_COLUMN_2_SECONDS.format(traceRange.getRetentionTimeColumn2Stop() / IChromatogramOverview.SECOND_CORRELATION_FACTOR);
					break;
				case 4:
					text = traceRange.getScanIndicesColumn2();
					break;
				case 5:
					text = traceRange.getName();
					break;
				case 6:
					text = traceRange.getTraces();
					break;
				case 7:
					text = traceRange.getSecondDimensionHint().label();
					break;
				default:
					text = "n.a.";
			}
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CHROM_2D_STACK_1D, IApplicationImageProvider.SIZE_16x16);
	}
}