/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 * Philip Wenig - refactoring IExtendedPartUI
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.support.ui.workbench.PreferencesSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.EditorUpdateSupport;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.nebula.visualization.widgets.datadefinition.ColorMap;
import org.eclipse.nebula.visualization.widgets.figures.IntensityGraphFigure;
import org.eclipse.nebula.visualization.xygraph.linearscale.Range;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class MassSpectrumPseudoGelUI extends Composite implements IExtendedPartUI {

	private LightweightSystem lightweightSystem;
	private IntensityGraphFigure intensityGraphFigure;

	private EditorUpdateSupport editorUpdateSupport = new EditorUpdateSupport();

	public MassSpectrumPseudoGelUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	public void update() {

		List<IScanMSD> scanSelections = editorUpdateSupport.getMassSpectrumSelections();
		updatePseudoGel(scanSelections);
	}

	private void updatePseudoGel(List<IScanMSD> scanSelections) {

		if(scanSelections != null) {
			intensityGraphFigure = createIntensityGraphFigure(false);
			lightweightSystem.setContents(intensityGraphFigure);
			setPseudoGel(scanSelections);
		} else {
			intensityGraphFigure.erase();
		}
	}

	private void setPseudoGel(List<IScanMSD> scanList) {

		if(scanList.isEmpty()) {
			return;
		}

		int dataHeight = scanList.size();
		double lowestIon = Double.MAX_VALUE;
		double highestIon = 0;
		for(IScanMSD scan : scanList) {
			if(lowestIon > scan.getLowestIon().getIon()) {
				lowestIon = scan.getLowestIon().getIon();
			}
			if(highestIon < scan.getHighestIon().getIon()) {
				highestIon = scan.getHighestIon().getIon();
			}
		}

		int dataWidth = (int)Math.ceil(highestIon - lowestIon) + 1;
		float[] data = new float[dataWidth * dataHeight];
		double highestAbundance = 0;

		int i = 0;
		for(IScanMSD scan : scanList) {
			if(highestAbundance < scan.getBasePeak()) {
				highestAbundance = scan.getBasePeak();
			}
			for(IIon ion : scan.getIons()) {
				int x = (int)Math.round(ion.getIon() - lowestIon);
				if(x >= 0 && x < dataWidth) {
					data[i * dataWidth + x] = ion.getAbundance();
				}
			}
			i++;
		}

		intensityGraphFigure.getXAxis().setRange(new Range(lowestIon, highestIon));
		intensityGraphFigure.getYAxis().setRange(new Range(0, dataHeight));

		intensityGraphFigure.setMin(0);
		intensityGraphFigure.setMax(highestAbundance);

		intensityGraphFigure.setDataWidth(dataWidth);
		intensityGraphFigure.setDataHeight(dataHeight);

		ColorMap reversedGrayScale = new ColorMap();
		reversedGrayScale.setColorMap(getReversedGrayScaleMap());
		intensityGraphFigure.setColorMap(reversedGrayScale);
		intensityGraphFigure.setDataArray(data);
		intensityGraphFigure.repaint();
	}

	private LinkedHashMap<Double, RGB> getReversedGrayScaleMap() {

		double[] values = new double[]{0, 1};
		RGB[] colors;
		if(PreferencesSupport.isDarkTheme()) {
			colors = new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)};
		} else {
			colors = new RGB[]{new RGB(255, 255, 255), new RGB(0, 0, 0)};
		}
		LinkedHashMap<Double, RGB> map = new LinkedHashMap<>();
		for(int i = 0; i < values.length; i++) {
			map.put(values[i], colors[i]);
		}
		return map;
	}

	private void createControl() {

		setLayout(new FillLayout());

		Composite composite = new Composite(this, SWT.FILL);
		composite.setLayout(new GridLayout(1, true));

		createCanvas(composite);
	}

	private Canvas createCanvas(Composite parent) {

		Canvas canvas = new Canvas(parent, SWT.FILL | SWT.BORDER);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		canvas.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		lightweightSystem = createLightweightSystem(canvas);

		return canvas;
	}

	private LightweightSystem createLightweightSystem(Canvas canvas) {

		LightweightSystem lightweightSystem = new LightweightSystem(canvas);
		lightweightSystem.getRootFigure().setBackgroundColor(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		return lightweightSystem;
	}

	private IntensityGraphFigure createIntensityGraphFigure(boolean zoom) {

		IntensityGraphFigure intensityGraphFigure = new IntensityGraphFigure(zoom);
		intensityGraphFigure.setForegroundColor(getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		intensityGraphFigure.getXAxis().setTitle("m/z");
		intensityGraphFigure.getYAxis().setTitle("");

		return intensityGraphFigure;
	}

	@Override
	public void dispose() {

		intensityGraphFigure.dispose();
	}
}
