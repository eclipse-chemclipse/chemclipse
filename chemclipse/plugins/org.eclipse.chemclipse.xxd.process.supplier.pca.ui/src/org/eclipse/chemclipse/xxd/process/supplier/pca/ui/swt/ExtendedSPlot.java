/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Algorithm;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.FeatureDelta;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IAnalysisSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d.SPlot;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.help.HelpContext;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePage;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePageLoadingPlot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.core.UserSelection;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;
import org.eclipse.ui.PlatformUI;

public class ExtendedSPlot extends Composite implements IExtendedPartUI {

	private AtomicReference<SPlot> plotControl = new AtomicReference<>();

	private EvaluationPCA evaluationPCA = null;

	private UserSelection userSelection = new UserSelection();

	private Composite control;

	private boolean highlightClick = false;

	public ExtendedSPlot(Composite parent, int style) {

		super(parent, style);
		createControl();
		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		dataUpdateSupport.add((topic, objects) -> {

			if(evaluationPCA != null) {
				if(DataUpdateSupport.isVisible(control)) {
					if(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LIST_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_STATLIST_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_FOLDCHANGE_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGBAR_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGLIST_VARIABLE.equals(topic)) {
						if(objects.size() == 1) {
							Object object = objects.get(0);
							ArrayList<IVariable> selectedVariables = new ArrayList<>();
							if(object instanceof Object[] values) {
								for(int i = 0; i < values.length; i++) {
									if(values[i] instanceof Feature feature) {
										selectedVariables.add(feature.getVariable());
									} else if(values[i] instanceof IVariable variable) {
										selectedVariables.add(variable);
									}
								}
							}
							evaluationPCA.setHighlightedVariables(selectedVariables);
							highlightClick = true;
							setInput(evaluationPCA);
						}
					}
				}
			}
		});
	}

	@Override
	public boolean setFocus() {

		updateOnFocus();
		return true;
	}

	public void setInput(EvaluationPCA evaluationPCA) {

		this.evaluationPCA = evaluationPCA;
		updateInput();
	}

	public void updateInput() {

		applySettings();
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));

		createToolbarMain(this);
		createPlot(this);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, HelpContext.S_PLOT);
		control = this;
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));

		createSettingsToolbarButton(composite);
		createButtonHelp(composite, HelpContext.S_PLOT);
	}

	private void createPlot(Composite parent) {

		SPlot plot = new SPlot(parent, SWT.BORDER);
		plot.setLayoutData(new GridData(GridData.FILL_BOTH));

		IChartSettings chartSettings = plot.getChartSettings();
		chartSettings.addHandledEventProcessor(new IHandledEventProcessor() {

			@Override
			public int getEvent() {

				return IMouseSupport.EVENT_MOUSE_DOWN;
			}

			@Override
			public int getButton() {

				return IMouseSupport.MOUSE_BUTTON_LEFT;
			}

			@Override
			public int getStateMask() {

				return SWT.MOD1;
			}

			@Override
			public void handleEvent(BaseChart baseChart, Event event) {

				if(isValidSPlot()) {
					if(event.count == 1) {
						userSelection.setSingleClick(true);
						userSelection.setStartCoordinate(event.x, event.y);
					}
				}
			}
		});
		chartSettings.addHandledEventProcessor(new IHandledEventProcessor() {

			@Override
			public int getEvent() {

				return IMouseSupport.EVENT_MOUSE_MOVE;
			}

			@Override
			public int getButton() {

				return IMouseSupport.MOUSE_BUTTON_NONE;
			}

			@Override
			public int getStateMask() {

				return SWT.MOD1;
			}

			@Override
			public void handleEvent(BaseChart baseChart, Event event) {

				if(userSelection.getStartX() > 0 && userSelection.getStartY() > 0) {
					userSelection.setStopCoordinate(event.x, event.y);
				}
			}
		});
		chartSettings.addHandledEventProcessor(new IHandledEventProcessor() {

			@Override
			public int getEvent() {

				return IMouseSupport.EVENT_MOUSE_UP;
			}

			@Override
			public int getButton() {

				return IMouseSupport.MOUSE_BUTTON_LEFT;
			}

			@Override
			public int getStateMask() {

				return SWT.MOD1;
			}

			@Override
			public void handleEvent(BaseChart baseChart, Event event) {

				if(isValidSPlot() && isBoxSelection()) {
					Rectangle rectangle = baseChart.getPlotArea().getBounds();
					int width = rectangle.width;
					int height = rectangle.height;
					Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
					Range rangeY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();

					int startX = userSelection.getStartX();
					int startY = userSelection.getStartY();
					int stopX = userSelection.getStopX();
					int stopY = userSelection.getStopY();
					if(startX > stopX) {
						int flip = startX;
						startX = stopX;
						stopX = flip;
					}
					if(startY > stopY) {
						int flip = startY;
						startY = stopY;
						stopY = flip;
					}

					double pXStart = rangeX.lower + (rangeX.upper - rangeX.lower) * ((1.0d / width) * startX);
					double pYStart = rangeY.lower + (rangeY.upper - rangeY.lower) * ((1.0d / height) * (height - startY));
					double pXStop = rangeX.lower + (rangeX.upper - rangeX.lower) * ((1.0d / width) * stopX);
					double pYStop = rangeY.lower + (rangeY.upper - rangeY.lower) * ((1.0d / height) * (height - stopY));

					IResultsMVA results = evaluationPCA.getResults();
					List<Feature> featuresSelected = evaluationPCA.getFeatureDataMatrix().getFeatures().stream().filter(x -> x.getVariable().isSelected()).toList();

					List<Feature> highlighted = new ArrayList<>();
					for(Feature feature : evaluationPCA.getFeatureDataMatrix().getFeatures()) {
						List<IVariable> highlightedVariables = evaluationPCA.getHighlightedVariables();
						if(highlightedVariables != null && highlightedVariables.contains(feature.getVariable())) {
							highlighted.add(feature);
						}
					}

					for(int i = 0; i < featuresSelected.size(); i++) {
						IPoint pointResult = getPoint(results, i);
						if(pointResult.getX() > pXStart && pointResult.getX() < pXStop && pointResult.getY() < pYStart && pointResult.getY() > pYStop) {
							if(!highlighted.contains(featuresSelected.get(i))) {
								highlighted.add(featuresSelected.get(i));
							}
						}
					}

					if(!highlighted.isEmpty()) {
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE, highlighted.toArray());
					}

					userSelection.reset();
					userSelection.setSingleClick(false);
				}
			}
		});
		chartSettings.addHandledEventProcessor(new IHandledEventProcessor() {

			@Override
			public int getEvent() {

				return IMouseSupport.EVENT_MOUSE_DOUBLE_CLICK;
			}

			@Override
			public int getButton() {

				return IMouseSupport.MOUSE_BUTTON_LEFT;
			}

			@Override
			public int getStateMask() {

				return SWT.NONE;
			}

			@Override
			public void handleEvent(BaseChart baseChart, Event event) {

				if(isValidSPlot()) {
					Rectangle rectangle = baseChart.getPlotArea().getBounds();
					double x = event.x;
					double y = event.y;
					double width = rectangle.width;
					double height = rectangle.height;

					Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
					Range rangeY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();

					IResultsMVA results = evaluationPCA.getResults();
					List<FeatureDelta> featureDeltas = new ArrayList<>();
					List<Feature> selectedFeatures = evaluationPCA.getFeatureDataMatrix().getFeatures().stream().filter(f -> f.getVariable().isSelected()).toList();

					for(int i = 0; i < selectedFeatures.size(); i++) {
						IPoint pointResult = getPoint(results, i);
						if(pointResult.getX() > rangeX.lower && pointResult.getX() < rangeX.upper && pointResult.getY() > rangeY.lower && pointResult.getY() < rangeY.upper) {
							double deltaX = 0;
							double deltaY = 0;
							if(rangeX.upper < 0 || rangeX.lower > 0) {
								deltaX = Math.abs(1.00 / Math.abs((Math.abs(rangeX.upper) - Math.abs(rangeX.lower))) * (pointResult.getX() - rangeX.lower) * width - x);
							} else {
								deltaX = Math.abs(1.00 / (rangeX.upper - rangeX.lower) * (pointResult.getX() - rangeX.lower) * width - x);
							}
							if(rangeY.upper < 0 || rangeY.lower > 0) {
								deltaY = Math.abs(1.00 / Math.abs((Math.abs(rangeY.upper) - Math.abs(rangeY.lower))) * (pointResult.getY() - rangeY.lower) * height - (height - y));
							} else {
								deltaY = Math.abs(1.00 / (rangeY.upper - rangeY.lower) * (pointResult.getY() - rangeY.lower) * height - (height - y));
							}
							featureDeltas.add(new FeatureDelta(selectedFeatures.get(i), deltaX, deltaY));
						}
					}

					if(!featureDeltas.isEmpty()) {
						Collections.sort(featureDeltas, Comparator.comparing(FeatureDelta::getDistance));
						FeatureDelta featureDelta = featureDeltas.get(0);
						List<Feature> featureList = new ArrayList<>();
						featureList.add(featureDelta.getFeature());
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE, featureList.toArray());
					}
				}
			}
		});
		chartSettings.addHandledEventProcessor(new IHandledEventProcessor() {

			@Override
			public int getEvent() {

				return IMouseSupport.EVENT_MOUSE_DOUBLE_CLICK;
			}

			@Override
			public int getButton() {

				return IMouseSupport.MOUSE_BUTTON_LEFT;
			}

			@Override
			public int getStateMask() {

				return SWT.MOD1;
			}

			@Override
			public void handleEvent(BaseChart baseChart, Event event) {

				if(isValidSPlot()) {
					Rectangle rectangle = baseChart.getPlotArea().getBounds();
					double x = event.x;
					double y = event.y;
					double width = rectangle.width;
					double height = rectangle.height;

					Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
					Range rangeY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();

					IResultsMVA results = evaluationPCA.getResults();
					List<FeatureDelta> featureDeltas = new ArrayList<>();
					List<Feature> selectedFeatures = evaluationPCA.getFeatureDataMatrix().getFeatures().stream().filter(f -> f.getVariable().isSelected()).toList();

					for(int i = 0; i < selectedFeatures.size(); i++) {
						IPoint pointResult = getPoint(results, i);
						if(pointResult.getX() > rangeX.lower && pointResult.getX() < rangeX.upper && pointResult.getY() > rangeY.lower && pointResult.getY() < rangeY.upper) {
							double deltaX = 0;
							double deltaY = 0;
							if(rangeX.upper < 0 || rangeX.lower > 0) {
								deltaX = Math.abs(1.00 / Math.abs((Math.abs(rangeX.upper) - Math.abs(rangeX.lower))) * (pointResult.getX() - rangeX.lower) * width - x);
							} else {
								deltaX = Math.abs(1.00 / (rangeX.upper - rangeX.lower) * (pointResult.getX() - rangeX.lower) * width - x);
							}
							if(rangeY.upper < 0 || rangeY.lower > 0) {
								deltaY = Math.abs(1.00 / Math.abs((Math.abs(rangeY.upper) - Math.abs(rangeY.lower))) * (pointResult.getY() - rangeY.lower) * height - (height - y));
							} else {
								deltaY = Math.abs(1.00 / (rangeY.upper - rangeY.lower) * (pointResult.getY() - rangeY.lower) * height - (height - y));
							}
							featureDeltas.add(new FeatureDelta(selectedFeatures.get(i), deltaX, deltaY));
						}
					}

					List<Feature> highlighted = new ArrayList<>();
					for(Feature feature : evaluationPCA.getFeatureDataMatrix().getFeatures()) {
						List<IVariable> highlightedVariables = evaluationPCA.getHighlightedVariables();
						if(highlightedVariables != null && highlightedVariables.contains(feature.getVariable())) {
							highlighted.add(feature);
						}
					}

					if(!featureDeltas.isEmpty()) {
						Collections.sort(featureDeltas, Comparator.comparing(FeatureDelta::getDistance));
						FeatureDelta featureDelta = featureDeltas.get(0);
						if(highlighted.contains(featureDelta.getFeature())) {
							int index = highlighted.indexOf(featureDelta.getFeature());
							highlighted.remove(index);
						} else {
							highlighted.add(featureDelta.getFeature());
						}
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE, highlighted.toArray());
					}
					userSelection.reset();
				}
			}
		});
		plot.applySettings(chartSettings);

		plot.getBaseChart().getPlotArea().addCustomPaintListener(e -> {

			if(userSelection.isActive()) {
				int x = Math.min(userSelection.getStartX(), userSelection.getStopX());
				int y = Math.min(userSelection.getStartY(), userSelection.getStopY());
				int width = Math.abs(userSelection.getStopX() - userSelection.getStartX());
				int height = Math.abs(userSelection.getStopY() - userSelection.getStartY());

				GC gc = e.gc;
				gc.setBackground(Colors.RED);
				gc.setForeground(Colors.DARK_RED);
				gc.setAlpha(45);
				gc.fillRectangle(x, y, width, height);
				gc.setLineStyle(SWT.LINE_DASH);
				gc.setLineWidth(2);
				gc.drawRectangle(x, y, width, height);
			}
		});

		plotControl.set(plot);
	}

	private IPoint getPoint(IResultsMVA results, int index) {

		double[] pCovarianceValues = results.getPCovarianceValues();
		double[] pCorrValues = results.getPCorrValues();
		if(pCovarianceValues == null || pCorrValues == null || index >= pCovarianceValues.length || index >= pCorrValues.length) {
			return new Point(0, 0);
		}
		return new Point(pCovarianceValues[index], pCorrValues[index]);
	}

	private void createSettingsToolbarButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePage.class, PreferencePageLoadingPlot.class), display -> applySettings());
	}

	private void applySettings() {

		BaseChart baseChart = plotControl.get().getBaseChart();
		Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
		Range rangeY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();
		updatePlot();
		if(highlightClick) {
			plotControl.get().updateRange(rangeX, rangeY);
			highlightClick = false;
		}
	}

	private void updatePlot() {

		SPlot plot = plotControl.get();
		plot.deleteSeries();

		if(isValidSPlot()) {
			plot.setInput(evaluationPCA);
		} else {
			plot.setInput(null);
		}
	}

	private boolean isValidSPlot() {

		if(evaluationPCA == null) {
			return false;
		}
		IAnalysisSettings analysisSettings = evaluationPCA.getSamples().getAnalysisSettings();
		if(analysisSettings == null || analysisSettings.getAlgorithm() != Algorithm.OPLS) {
			return false;
		}
		IResultsMVA results = evaluationPCA.getResults();
		return results != null && results.getPCovarianceValues() != null && results.getPCorrValues() != null;
	}

	private boolean isBoxSelection() {

		if(userSelection.getStartX() != 0 && userSelection.getStartY() != 0 && userSelection.getStopX() != 0 && userSelection.getStopY() != 0) {
			return true;
		}
		return false;
	}

	private void updateOnFocus() {

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		List<Object> objects = dataUpdateSupport.getUpdates(getLastTopic(dataUpdateSupport.getTopics()));

		if(!objects.isEmpty()) {
			Object object = objects.get(0);
			if(object instanceof EvaluationPCA evaluation) {
				setInput(evaluation);
				updateInput();
			}
		}
	}

	private String getLastTopic(List<String> topics) {

		Collections.reverse(topics);
		for(String topic : topics) {
			if(topic.equals(IChemClipseEvents.TOPIC_PCA_UPDATE_RESULT)) {
				return topic;
			}
			if(topic.equals(IChemClipseEvents.TOPIC_PCA_UPDATE_SELECTION)) {
				return topic;
			}
		}

		return "";
	}
}