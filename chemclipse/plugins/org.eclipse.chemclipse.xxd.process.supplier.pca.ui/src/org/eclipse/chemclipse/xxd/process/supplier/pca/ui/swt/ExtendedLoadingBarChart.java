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
 * Lorenz Gerber- initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IAnalysisSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ISamplesPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d.LoadingBarChart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.core.UserSelection;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;

public class ExtendedLoadingBarChart extends Composite implements IExtendedPartUI {

	private AtomicReference<LoadingBarChart> chartControl = new AtomicReference<>();
	private Combo comboPrincipalComponent;
	private EvaluationPCA evaluationPCA = null;
	private int currentPC = 0;
	private Composite control;
	private UserSelection userSelection = new UserSelection();
	private ISamplesPCA<IVariable, ISample> samples = null;
	private boolean initialDraw = true;

	public ExtendedLoadingBarChart(Composite parent, int style) {

		super(parent, style);
		createControl();
		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		dataUpdateSupport.add((topic, objects) -> {

			if(evaluationPCA != null) {
				if(DataUpdateSupport.isVisible(control)) {
					if(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGBAR_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LIST_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_STATLIST_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_FOLDCHANGE_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGLIST_VARIABLE.equals(topic)) {
						if(objects.size() == 1) {
							Object object = objects.get(0);
							ArrayList<IVariable> selectedVariables = new ArrayList<>();
							if(object instanceof Object[] values) {
								for(int i = 0; i < values.length; i++) {
									if(values[i] instanceof Feature) {
										Feature feature = (Feature)values[i];
										selectedVariables.add(feature.getVariable());
									} else if(values[i] instanceof IVariable) {
										IVariable variable = (IVariable)values[i];
										selectedVariables.add(variable);
									}
								}
							}
							evaluationPCA.setHighlightedVariables(selectedVariables);
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
		if(this.evaluationPCA != null) {
			samples = evaluationPCA.getSamples();
		}
		updatePlot();
	}

	public void updatePlot() {

		updateWidgets();
		applySettings();
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		createToolbarMain(this);
		createChart(this);
		control = this;
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		gridData.widthHint = 300;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));

		createPCSelector(composite);
	}

	private void createPCSelector(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("Principal Component:");

		comboPrincipalComponent = new Combo(parent, SWT.READ_ONLY);
		comboPrincipalComponent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comboPrincipalComponent.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				currentPC = comboPrincipalComponent.getSelectionIndex();
				updateChart();
			}
		});
	}

	private void createChart(Composite parent) {

		LoadingBarChart chart = new LoadingBarChart(parent, SWT.BORDER);
		chart.setLayoutData(new GridData(GridData.FILL_BOTH));

		IChartSettings chartSettings = chart.getChartSettings();

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

				return SWT.NONE;
			}

			@Override
			public void handleEvent(BaseChart baseChart, Event event) {

				if(evaluationPCA != null) {

					double xValue = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getDataCoordinate(event.x);
					List<VariableLoading> variableLoadings = getVariableLoadingList();
					int barIndex = (int)Math.round(xValue);
					if(barIndex >= 0 && barIndex < variableLoadings.size()) {
						String variableName = variableLoadings.get(barIndex).getVariableName();
						String tooltip = "Variable: " + variableName + "\nValue: " + String.format("%.4f", variableLoadings.get(barIndex).getLoading());
						baseChart.getPlotArea().setToolTipText(tooltip);
					} else {
						baseChart.getPlotArea().setToolTipText(null);
					}
				}
			}

		});
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
					baseChart.getPlotArea().getControl().redraw();
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
					int pXStart = (int)baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getDataCoordinate(userSelection.getStartX());
					int pXStop = (int)baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getDataCoordinate(userSelection.getStopX());

					if(pXStart > pXStop) {
						int flip = pXStart;
						pXStart = pXStop;
						pXStop = flip;
					}
					List<VariableLoading> variableLoadings = getVariableLoadingList();
					if(pXStart < 0) {
						pXStart = 0;
					} else if(pXStop < 0) {
						pXStop = 0;
					} else if(pXStop > variableLoadings.size() - 1) {
						pXStop = variableLoadings.size() - 1;
					}
					List<IVariable> variablesHighlighted = evaluationPCA.getHighlightedVariables();
					for(int i = pXStart; i <= pXStop; i++) {
						String currentVariableName = variableLoadings.get(i).getVariableName();
						int index = IntStream.range(0, variablesHighlighted.size()).filter(x -> variablesHighlighted.get(x).getValue().equals(currentVariableName)).findFirst().orElse(-1);
						if(index == -1) {
							variablesHighlighted.add(samples.getVariables().stream().filter(x -> x.getValue().equals(currentVariableName)).findFirst().get());
						}
					}

					if(!variablesHighlighted.isEmpty()) {
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGBAR_VARIABLE, variablesHighlighted.toArray());
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

				if(evaluationPCA != null) {
					int clickIndex = (int)baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getDataCoordinate(event.x);

					List<VariableLoading> variableLoadings = getVariableLoadingList();
					List<IVariable> highlightedVariables = new ArrayList<>();
					List<IVariable> variablesHighlighted = evaluationPCA.getHighlightedVariables();

					String currentVariableName = variableLoadings.get(clickIndex).getVariableName();
					int variableIndex = IntStream.range(0, variablesHighlighted.size()).filter(x -> variablesHighlighted.get(x).getValue().equals(currentVariableName)).findFirst().orElse(-1);
					if(variableIndex == -1) {
						highlightedVariables.add(samples.getVariables().stream().filter(x -> x.getValue().equals(currentVariableName)).findFirst().get());
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGBAR_VARIABLE, highlightedVariables.toArray());
					} else {
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGBAR_VARIABLE, highlightedVariables.toArray());
					}
				}
				userSelection.reset();
				userSelection.setSingleClick(false);
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
					int clickIndex = (int)baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getDataCoordinate(event.x);

					List<VariableLoading> variableLoadings = getVariableLoadingList();
					List<IVariable> variablesHighlighted = evaluationPCA.getHighlightedVariables();

					String currentVariableName = variableLoadings.get(clickIndex).getVariableName();
					int variableIndex = IntStream.range(0, variablesHighlighted.size()).filter(x -> variablesHighlighted.get(x).getValue().equals(currentVariableName)).findFirst().orElse(-1);
					if(variableIndex == -1) {
						variablesHighlighted.add(samples.getVariables().stream().filter(x -> x.getValue().equals(currentVariableName)).findFirst().get());
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGBAR_VARIABLE, variablesHighlighted.toArray());
					} else {
						variablesHighlighted.remove(variableIndex);
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGBAR_VARIABLE, variablesHighlighted.toArray());
					}
				}
				userSelection.reset();
				userSelection.setSingleClick(false);

			}
		});

		chart.applySettings(chartSettings);

		chart.getBaseChart().getPlotArea().addCustomPaintListener(e -> {

			if(userSelection.isActive()) {
				int xMin = Math.min(userSelection.getStartX(), userSelection.getStopX());
				int xMax = Math.max(userSelection.getStartX(), userSelection.getStopX());
				int y = Math.min(userSelection.getStartY(), userSelection.getStopY());
				BaseChart baseChart = chartControl.get().getBaseChart();
				IPlotArea plotArea = baseChart.getPlotArea();
				Point rectangle = plotArea instanceof Scrollable scrollable ? scrollable.getSize() : plotArea.getSize();

				GC gc = e.gc;
				gc.setBackground(Colors.RED);
				gc.setForeground(Colors.DARK_RED);
				gc.setAlpha(45);
				gc.setLineStyle(SWT.LINE_DASH);
				gc.setLineWidth(2);
				gc.drawLine(xMin, 0, xMin, rectangle.y);
				gc.drawLine(xMax, 0, xMax, rectangle.y);
				gc.drawLine(xMin, y, xMax, y);
			}
		});

		chartControl.set(chart);
	}

	private void updateWidgets() {

		if(evaluationPCA != null) {
			IAnalysisSettings analysisSettings = evaluationPCA.getSamples().getAnalysisSettings();
			updatePCCombo(analysisSettings);
		} else {
			comboPrincipalComponent.removeAll();
		}
	}

	private void updatePCCombo(IAnalysisSettings analysisSettings) {

		comboPrincipalComponent.removeAll();

		if(evaluationPCA != null && analysisSettings != null) {
			int numberOfPCs = analysisSettings.getNumberOfPrincipalComponents();
			int numberOfLoadingVectors = evaluationPCA.getResults().getLoadingVectors().size();

			int maxPCs = Math.min(numberOfPCs, numberOfLoadingVectors);

			for(int i = 0; i < maxPCs; i++) {
				String label = String.format("PC%d", i + 1);
				comboPrincipalComponent.add(label);
			}

			if(maxPCs > 0) {
				int selectedIndex = Math.min(currentPC, maxPCs - 1);
				comboPrincipalComponent.select(selectedIndex);
				currentPC = selectedIndex;
			}
		}
	}

	private void updateChart() {

		LoadingBarChart chart = chartControl.get();
		if(chart != null) {
			if(evaluationPCA != null) {

				BaseChart baseChart = chart.getBaseChart();
				double xRangeStart = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange().lower;
				double xRangeEnd = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange().upper;
				double yRangeStart = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange().lower;
				double yRangeEnd = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange().upper;

				if(initialDraw) {
					xRangeEnd = evaluationPCA.getResults().getExtractedVariables().size();
					yRangeStart = DoubleStream.of(evaluationPCA.getResults().getLoadingVectors().get(currentPC)).max().getAsDouble();
					yRangeEnd = DoubleStream.of(evaluationPCA.getResults().getLoadingVectors().get(currentPC)).min().getAsDouble();
					initialDraw = false;
				}
				chart.setInput(evaluationPCA, comboPrincipalComponent.getSelectionIndex());

				baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).setRange(new Range(xRangeStart, xRangeEnd));
				baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).setRange(new Range(yRangeStart, yRangeEnd));
				baseChart.redraw();

			} else {
				chart.setInput(null, 0);
			}
		}
	}

	private void applySettings() {

		updateChart();
	}

	@Override
	public void update() {

		updatePlot();
	}

	private static class VariableLoading {

		private final String variableName;
		private final double loading;

		public VariableLoading(String variableName, double loading) {

			this.variableName = variableName;
			this.loading = loading;
		}

		public String getVariableName() {

			return variableName;
		}

		public double getLoading() {

			return loading;
		}
	}

	private boolean isBoxSelection() {

		if(userSelection.getStartX() != 0 && userSelection.getStartY() != 0 && userSelection.getStopX() != 0 && userSelection.getStopY() != 0) {
			return true;
		}
		return false;
	}

	private List<VariableLoading> getVariableLoadingList() {

		IResultsMVA results = evaluationPCA.getResults();
		List<IVariable> variables = results.getExtractedVariables();
		List<double[]> loadings = results.getLoadingVectors();
		List<VariableLoading> variableLoadings = new ArrayList<>();
		for(int i = 0; i < variables.size(); i++) {
			IVariable variable = variables.get(i);
			if(currentPC < loadings.size()) {
				double loading = loadings.get(currentPC)[i];
				String variableName = variable.getValue();
				variableLoadings.add(new VariableLoading(variableName, loading));
			}

		}

		variableLoadings.sort(Comparator.comparingDouble(VariableLoading::getLoading).reversed());
		return variableLoadings;
	}

	private void updateOnFocus() {

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		List<Object> objects = dataUpdateSupport.getUpdates(getLastTopic(dataUpdateSupport.getTopics()));

		if(!objects.isEmpty()) {
			Object object = objects.get(0);
			if(object instanceof EvaluationPCA evaluation) {
				setInput(evaluation);
				updatePlot();
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
