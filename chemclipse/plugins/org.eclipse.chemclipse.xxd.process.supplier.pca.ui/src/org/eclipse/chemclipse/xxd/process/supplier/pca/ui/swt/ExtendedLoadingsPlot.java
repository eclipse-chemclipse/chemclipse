/*******************************************************************************
 * Copyright (c) 2020, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - update feature table selection from loading plot
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
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.IDataUpdateListener;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ISettingsHandler;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.FeatureDelta;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IAnalysisSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d.LoadingsPlot;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.help.HelpContext;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePage;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePageLoadingPlot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.ICustomPaintListener;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.core.UserSelection;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;
import org.eclipse.ui.PlatformUI;

public class ExtendedLoadingsPlot extends Composite implements IExtendedPartUI {

	private AtomicReference<LoadingsPlot> plotControl = new AtomicReference<>();
	private AtomicReference<PrincipalComponentUI> principalComponentControl = new AtomicReference<>();
	//
	private EvaluationPCA evaluationPCA = null;
	//
	private UserSelection userSelection = new UserSelection();
	//
	private Composite control;
	//
	private boolean highlightClick = false;

	public ExtendedLoadingsPlot(Composite parent, int style) {

		super(parent, style);
		createControl();
		DataUpdateSupport dataUpdateSupport = new DataUpdateSupport(Activator.getDefault().getEventBroker());
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_PCA_UPDATE_RESULT, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LIST_VARIABLE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.add(new IDataUpdateListener() {

			@Override
			public void update(String topic, List<Object> objects) {

				if(evaluationPCA != null) {
					if(DataUpdateSupport.isVisible(control)) {
						if(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LIST_VARIABLE.equals(topic) || IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE.equals(topic)) {
							if(objects.size() == 1) {
								Object object = objects.get(0);
								ArrayList<IVariable> selectedVariables = new ArrayList<>();
								if(object instanceof Object[] values) {
									for(int i = 0; i < values.length; i++) {
										if(values[i] instanceof Feature) {
											Feature feature = (Feature)values[i];
											selectedVariables.add(feature.getVariable());
										}
									}
								}
								evaluationPCA.setHighlightedVariables(selectedVariables);
								setInput(evaluationPCA);
							}
						}
					}
				}
			}
		});
	}

	public void setInput(EvaluationPCA evaluationPCA) {

		this.evaluationPCA = evaluationPCA;
		updateInput();
	}

	public void updateInput() {

		updateWidgets();
		applySettings();
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		createPlot(this);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, HelpContext.LOADINGS_PLOT);
		control = this;
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(3, false));
		//
		createPrincipalComponentUI(composite);
		createSettingsButton(composite);
		createButtonHelp(composite);
	}

	private void createPlot(Composite parent) {

		LoadingsPlot plot = new LoadingsPlot(parent, SWT.BORDER);
		plot.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
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

				if(evaluationPCA != null) {
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

				if(evaluationPCA != null && isBoxSelection()) {
					/*
					 * Prepare Data viewport
					 */
					Rectangle rectangle = baseChart.getPlotArea().getBounds();
					int width = rectangle.width;
					int height = rectangle.height;
					Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
					Range rangeY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();
					/*
					 * Determine x|y coordinates Start/Stop.
					 */
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
					/*
					 * Calculate selected points.
					 */
					double pXStart = rangeX.lower + (rangeX.upper - rangeX.lower) * ((1.0d / width) * startX);
					double pYStart = rangeY.lower + (rangeY.upper - rangeY.lower) * ((1.0d / height) * (height - startY));
					double pXStop = rangeX.lower + (rangeX.upper - rangeX.lower) * ((1.0d / width) * stopX);
					double pYStop = rangeY.lower + (rangeY.upper - rangeY.lower) * ((1.0d / height) * (height - stopY));
					/*
					 * Map the result deltas.
					 */
					PrincipalComponentUI principalComponentUI = principalComponentControl.get();
					int pcX = principalComponentUI.getPCX();
					int pcY = principalComponentUI.getPCY();
					IResultsPCA<? extends IResultPCA, ? extends IVariable> resultsPCA = evaluationPCA.getResults();
					List<Feature> featuresSelected = evaluationPCA.getFeatureDataMatrix().getFeatures().stream().filter(x -> x.getVariable().isSelected()).toList();
					/*
					 * Get all currently selected features
					 */
					List<Feature> highlighted = new ArrayList<>();
					for(Feature feature : evaluationPCA.getFeatureDataMatrix().getFeatures()) {
						if(evaluationPCA.getHighlightedVariables().contains(feature.getVariable())) {
							highlighted.add(feature);
						}
					}
					/*
					 * Prepare a result object with loading vectors per variable
					 */
					for(int i = 0; i < resultsPCA.getExtractedVariables().size(); i++) {
						double[] variableLoading = getVariableLoading(resultsPCA, i);
						IPoint pointResult = getPoint(variableLoading, pcX, pcY, i);
						if(pointResult.getX() > pXStart && pointResult.getX() < pXStop && pointResult.getY() < pYStart && pointResult.getY() > pYStop) {
							if(highlighted.contains(featuresSelected.get(i))) {
								int index = highlighted.indexOf(featuresSelected.get(i));
								highlighted.remove(index);
							} else {
								highlighted.add(featuresSelected.get(i));
							}
						}
					}
					/*
					 * Get the closest result.
					 */
					if(!highlighted.isEmpty()) {
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE, highlighted.toArray());
					}
					/*
					 * Finish User Selection Process
					 */
					userSelection.reset();
					userSelection.setSingleClick(false);
					highlightClick = true;
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

				if(evaluationPCA != null) {
					/*
					 * Determine the x|y coordinates.
					 */
					Rectangle rectangle = baseChart.getPlotArea().getBounds();
					double x = event.x;
					double y = event.y;
					double width = rectangle.width;
					double height = rectangle.height;
					/*
					 * Calculate the selected point.
					 */
					Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
					Range rangeY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();
					/*
					 * Map the result deltas.
					 */
					PrincipalComponentUI principalComponentUI = principalComponentControl.get();
					int pcX = principalComponentUI.getPCX();
					int pcY = principalComponentUI.getPCY();
					IResultsPCA<? extends IResultPCA, ? extends IVariable> resultsPCA = evaluationPCA.getResults();
					List<FeatureDelta> featureDeltas = new ArrayList<>();
					List<Feature> selectedFeatures = evaluationPCA.getFeatureDataMatrix().getFeatures().stream().filter(f -> f.getVariable().isSelected()).toList();
					/*
					 * Prepare a result object with loading vectors per variable
					 */
					for(int i = 0; i < resultsPCA.getExtractedVariables().size(); i++) {
						double[] variableLoading = getVariableLoading(resultsPCA, i);
						IPoint pointResult = getPoint(variableLoading, pcX, pcY, i);
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
					/*
					 * Get the closest result.
					 */
					if(!featureDeltas.isEmpty()) {
						Collections.sort(featureDeltas, Comparator.comparing(FeatureDelta::getDistance));
						FeatureDelta featureDelta = featureDeltas.get(0);
						List<Feature> featureList = new ArrayList<>();
						featureList.add(featureDelta.getFeature());
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE, featureList.toArray());
						highlightClick = true;
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

				if(evaluationPCA != null) {
					/*
					 * Determine the x|y coordinates.
					 */
					Rectangle rectangle = baseChart.getPlotArea().getBounds();
					double x = event.x;
					double y = event.y;
					double width = rectangle.width;
					double height = rectangle.height;
					/*
					 * Calculate the selected point.
					 */
					Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
					Range rangeY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();
					/*
					 * Map the result deltas.
					 */
					PrincipalComponentUI principalComponentUI = principalComponentControl.get();
					int pcX = principalComponentUI.getPCX();
					int pcY = principalComponentUI.getPCY();
					IResultsPCA<? extends IResultPCA, ? extends IVariable> resultsPCA = evaluationPCA.getResults();
					List<FeatureDelta> featureDeltas = new ArrayList<>();
					List<Feature> selectedFeatures = evaluationPCA.getFeatureDataMatrix().getFeatures().stream().filter(f -> f.getVariable().isSelected()).toList();
					/*
					 * Prepare a result object with loading vectors per variable
					 */
					for(int i = 0; i < resultsPCA.getExtractedVariables().size(); i++) {
						double[] variableLoading = getVariableLoading(resultsPCA, i);
						IPoint pointResult = getPoint(variableLoading, pcX, pcY, i);
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
					/*
					 * Get all currently selected features
					 */
					List<Feature> highlighted = new ArrayList<>();
					for(Feature feature : evaluationPCA.getFeatureDataMatrix().getFeatures()) {
						if(evaluationPCA.getHighlightedVariables().contains(feature.getVariable())) {
							highlighted.add(feature);
						}
					}
					/*
					 * Get the closest result.
					 */
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
						highlightClick = true;
					}
					userSelection.reset();
				}
			}
		});
		plot.applySettings(chartSettings);
		/*
		 * Paint Listener
		 */
		plot.getBaseChart().getPlotArea().addCustomPaintListener(new ICustomPaintListener() {

			@Override
			public void paintControl(PaintEvent e) {

				if(userSelection.isActive()) {
					int x = Math.min(userSelection.getStartX(), userSelection.getStopX());
					int y = Math.min(userSelection.getStartY(), userSelection.getStopY());
					int width = Math.abs(userSelection.getStopX() - userSelection.getStartX());
					int height = Math.abs(userSelection.getStopY() - userSelection.getStartY());
					//
					GC gc = e.gc;
					gc.setBackground(Colors.RED);
					gc.setForeground(Colors.DARK_RED);
					gc.setAlpha(45);
					gc.fillRectangle(x, y, width, height);
					gc.setLineStyle(SWT.LINE_DASH);
					gc.setLineWidth(2);
					gc.drawRectangle(x, y, width, height);
				}
			}
		});
		//
		plotControl.set(plot);
	}

	private double[] getVariableLoading(IResultsPCA<? extends IResultPCA, ? extends IVariable> results, int number) {

		double[] variableLoading = new double[results.getLoadingVectors().size()];
		for(int i = 0; i < results.getLoadingVectors().size(); i++) {
			variableLoading[i] = results.getLoadingVectors().get(i)[number];
		}
		return variableLoading;
	}

	private IPoint getPoint(double[] variableLoading, int pcX, int pcY, int i) {

		double rX = 0;
		if(pcX != 0) {
			rX = variableLoading[pcX - 1]; // e.g. 0 = PC1
		} else {
			rX = i;
		}
		double rY = variableLoading[pcY - 1]; // e.g. 1 = PC2
		//
		return new Point(rX, rY);
	}

	private void createPrincipalComponentUI(Composite parent) {

		PrincipalComponentUI principalComponentUI = new PrincipalComponentUI(parent, SWT.NONE, PrincipalComponentUI.SPINNER_X | PrincipalComponentUI.SPINNER_Y);
		principalComponentUI.setSelectionListener(new ISelectionListenerPCs() {

			@Override
			public void update(int pcX, int pcY, int pcZ) {

				updatePlot(pcX, pcY);
			}
		});
		//
		principalComponentControl.set(principalComponentUI);
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePage.class, PreferencePageLoadingPlot.class), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				applySettings();
			}
		});
	}

	private void applySettings() {

		PrincipalComponentUI principalComponentUI = principalComponentControl.get();
		int pcX = principalComponentUI.getPCX();
		int pcY = principalComponentUI.getPCY();
		BaseChart baseChart = plotControl.get().getBaseChart();
		Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
		Range rangeY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();
		updatePlot(pcX, pcY);
		/*
		 * Prevent Zoom reset on Double Click
		 */
		if(highlightClick) {
			plotControl.get().updateRange(rangeX, rangeY);
		}
	}

	private void updateWidgets() {

		PrincipalComponentUI principalComponentUI = principalComponentControl.get();
		if(evaluationPCA != null) {
			IAnalysisSettings analysisSettings = evaluationPCA.getSamples().getAnalysisSettings();
			principalComponentUI.setInput(analysisSettings);
		} else {
			principalComponentUI.setInput(null);
		}
	}

	private void updatePlot(int pcX, int pcY) {

		LoadingsPlot plot = plotControl.get();
		plot.deleteSeries();
		//
		if(evaluationPCA != null) {
			plot.setInput(evaluationPCA, pcX, pcY);
		} else {
			plot.setInput(null, pcX, pcY);
		}
	}

	private boolean isBoxSelection() {

		if(userSelection.getStartX() != 0 && userSelection.getStartY() != 0 && userSelection.getStopX() != 0 && userSelection.getStopY() != 0) {
			return true;
		}
		return false;
	}
}