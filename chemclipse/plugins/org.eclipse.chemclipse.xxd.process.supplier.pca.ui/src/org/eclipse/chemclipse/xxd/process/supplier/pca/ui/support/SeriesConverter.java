/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - getting rid of JavaFX
 * Lorenz Gerber - fix sample selection
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.LabelOptionPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesData;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.ScatterSeriesData;

public class SeriesConverter {

	public static List<IScatterSeriesData> basisVectorsToSeries(IResultsPCA<? extends IResultPCA, ? extends IVariable> pcaResults, int pcX, int pcY) {

		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		List<IScatterSeriesData> scatterSeriesDataList = new ArrayList<>();
		List<IScatterSeriesData> scatterSeriesDataListSelected = new ArrayList<>();
		List<? extends IVariable> variables = pcaResults.getExtractedVariables();
		//
		for(int i = 0; i < variables.size(); i++) {
			IVariable variable = variables.get(i);
			String name = variables.get(i).getValue();
			//
			double x = 0;
			if(pcX != 0) {
				x = pcaResults.getLoadingVectors().get(pcX - 1)[i];
			} else {
				x = i;
			}
			double y = pcaResults.getLoadingVectors().get(pcY - 1)[i];
			ISeriesData seriesData = new SeriesData(new double[]{x}, new double[]{y}, name);
			IScatterSeriesData scatterSeriesData = new ScatterSeriesData(seriesData);
			IScatterSeriesSettings scatterSeriesSettings = scatterSeriesData.getSettings();
			scatterSeriesSettings.setSymbolColor(variable.isSelected() ? Colors.GRAY : Colors.RED);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.valueOf(preferenceStore.getString(PreferenceSupplier.P_LOADING_PLOT_2D_SYMBOL_TYPE)));
			int baseSymbolSize = preferenceStore.getInt(PreferenceSupplier.P_LOADING_PLOT_2D_SYMBOL_SIZE);
			scatterSeriesSettings.setSymbolSize(variable.isSelected() ? baseSymbolSize + 3 : baseSymbolSize);
			if(variable.isSelected()) {
				scatterSeriesDataListSelected.add(scatterSeriesData);
			} else {
				scatterSeriesDataList.add(scatterSeriesData);
			}
		}
		scatterSeriesDataList.addAll(scatterSeriesDataListSelected);
		return scatterSeriesDataList;
	}

	public static List<IScatterSeriesData> basisVectorsToSeriesDescription(IResultsPCA<? extends IResultPCA, ? extends IVariable> pcaResults, int pcX, int pcY) {

		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		List<IScatterSeriesData> scatterSeriesDataList = new ArrayList<>();
		List<? extends IVariable> variables = pcaResults.getExtractedVariables();
		//
		for(int i = 0; i < variables.size(); i++) {
			IVariable variable = variables.get(i);
			String description = variable.getDescription();
			String name = null;
			if(description == null || description.isEmpty()) {
				name = variables.get(i).getValue();
			} else {
				name = description;
			}
			//
			double x = 0;
			if(pcX != 0) {
				x = pcaResults.getLoadingVectors().get(pcX - 1)[i];
			} else {
				x = i;
			}
			double y = pcaResults.getLoadingVectors().get(pcY - 1)[i];
			ISeriesData seriesData = new SeriesData(new double[]{x}, new double[]{y}, name);
			IScatterSeriesData scatterSeriesData = new ScatterSeriesData(seriesData);
			IScatterSeriesSettings scatterSeriesSettings = scatterSeriesData.getSettings();
			//
			if(variable.isSelected()) {
				scatterSeriesSettings.setSymbolColor(Colors.RED);
			} else {
				scatterSeriesSettings.setSymbolColor(Colors.GRAY);
			}
			scatterSeriesSettings.setSymbolType(PlotSymbolType.valueOf(preferenceStore.getString(PreferenceSupplier.P_LOADING_PLOT_2D_SYMBOL_TYPE)));
			scatterSeriesSettings.setSymbolSize(preferenceStore.getInt(PreferenceSupplier.P_LOADING_PLOT_2D_SYMBOL_SIZE));
			scatterSeriesDataList.add(scatterSeriesData);
		}
		return scatterSeriesDataList;
	}

	public static List<IScatterSeriesData> sampleToSeries(IResultsPCA<? extends IResultPCA, ?> resultsPCA, int pcX, int pcY, Map<String, IResultPCA> extractedPcaResults) {

		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		List<IScatterSeriesData> scatterSeriesDataList = new ArrayList<>();
		extractedPcaResults.clear();
		/*
		 * Group Colors
		 */
		List<? extends IResultPCA> resultList = resultsPCA.getPcaResultList();
		LabelOptionPCA labelOptionPCA = resultsPCA.getPcaSettings().getLabelOptionPCA();
		//
		for(int i = 0; i < resultList.size(); i++) {
			IResultPCA pcaResult = resultList.get(i);
			/*
			 * Create the series.
			 */
			String sampleName = pcaResult.getSample().getSampleName();
			String description;
			switch(labelOptionPCA) {
				case GROUP_NAME:
					description = pcaResult.getSample().getGroupName();
					break;
				case CLASSIFICATION:
					description = pcaResult.getSample().getClassification();
					break;
				case DESCRIPTION:
					description = pcaResult.getSample().getDescription();
					break;
				default:
					description = sampleName;
					break;
			}
			//
			extractedPcaResults.put(sampleName, pcaResult);
			if(!pcaResult.isDisplayed()) {
				continue;
			}
			double[] eigenSpace = pcaResult.getScoreVector();
			double x = 0;
			if(pcX != 0) {
				x = eigenSpace[pcX - 1]; // e.g. 0 = PC1
			} else {
				x = i;
			}
			double y = eigenSpace[pcY - 1]; // e.g. 1 = PC2
			ISeriesData seriesData = new SeriesData(new double[]{x}, new double[]{y}, sampleName);
			/*
			 * Set the color.
			 */
			IScatterSeriesData scatterSeriesData = new ScatterSeriesData(seriesData);
			IScatterSeriesSettings scatterSeriesSettings = scatterSeriesData.getSettings();
			scatterSeriesSettings.setDescription(description);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.valueOf(preferenceStore.getString(PreferenceSupplier.P_SCORE_PLOT_2D_SYMBOL_TYPE)));
			scatterSeriesSettings.setSymbolSize(preferenceStore.getInt(PreferenceSupplier.P_SCORE_PLOT_2D_SYMBOL_SIZE));
			// If a sample is selected, use grey
			Color color = Colors.getColor(pcaResult.getSample().getRGB());
			scatterSeriesSettings.setSymbolColor(pcaResult.isSelected() ? Colors.GRAY : color);
			scatterSeriesDataList.add(scatterSeriesData);
		}
		return scatterSeriesDataList;
	}
}
