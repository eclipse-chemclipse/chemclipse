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
import java.util.stream.IntStream;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IAnalysisSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ISamplesPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d.ScoreBarChart;
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

public class ExtendedScoreBarChart extends Composite implements IExtendedPartUI {

	private AtomicReference<ScoreBarChart> chartControl = new AtomicReference<>();
	private Combo comboPrincipalComponent;
	private EvaluationPCA evaluationPCA = null;
	private int currentPC = 0;
	private Composite control;
	private UserSelection userSelection = new UserSelection();
	private ISamplesPCA<IVariable, ISample> samples = null;
	private boolean initialDraw = true;

	public ExtendedScoreBarChart(Composite parent, int style) {

		super(parent, style);
		createControl();
		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		dataUpdateSupport.add((topic, objects) -> {

			if(evaluationPCA != null) {
				if(DataUpdateSupport.isVisible(control)) {
					if(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREBAR_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREPLOT_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCORELIST_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_VARIABLELINE_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_ERRORRESIDUALS_SAMPLE.equals(topic)) {
						if(objects.size() == 1) {
							Object object = objects.get(0);
							ArrayList<ISample> samples = new ArrayList<>();
							if(object instanceof Object[] values) {
								for(int i = 0; i < values.length; i++) {
									if(values[i] instanceof ISample) {
										samples.add((ISample)values[i]);
									}
								}
							}
							evaluationPCA.setHighlightedSamples(samples);
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

		ScoreBarChart chart = new ScoreBarChart(parent, SWT.BORDER);
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
					List<SampleScore> sampleScores = getSampleScoreList();
					int barIndex = (int)Math.round(xValue);
					if(barIndex >= 0 && barIndex < sampleScores.size()) {
						String sampleName = sampleScores.get(barIndex).getSampleName();
						String tooltip = "Sample: " + sampleName + "\nValue: " + String.format("%.2f", sampleScores.get(barIndex).getScore());
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
					baseChart.getPlotArea().redraw();
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
					List<SampleScore> sampleScores = getSampleScoreList();
					if(pXStart < 0) {
						pXStart = 0;
					} else if(pXStop < 0) {
						pXStop = 0;
					} else if(pXStop > sampleScores.size() - 1) {
						pXStop = sampleScores.size() - 1;
					}
					List<ISample> samplesHighlighted = evaluationPCA.getHighlightedSamples();
					for(int i = pXStart; i <= pXStop; i++) {
						String currentSampleName = sampleScores.get(i).getSampleName();
						int index = IntStream.range(0, samplesHighlighted.size()).filter(x -> samplesHighlighted.get(x).getSampleName().equals(currentSampleName)).findFirst().orElse(-1);
						if(index == -1) {
							samplesHighlighted.add(samples.getSamples().stream().filter(x -> x.getSampleName().equals(currentSampleName)).findFirst().get());
						}
					}

					if(!samplesHighlighted.isEmpty()) {
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREBAR_SAMPLE, samplesHighlighted.toArray());
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

					List<SampleScore> sampleScores = getSampleScoreList();
					List<ISample> highlightedSamples = new ArrayList<>();
					List<ISample> samplesHighlighted = evaluationPCA.getHighlightedSamples();

					String currentSampleName = sampleScores.get(clickIndex).getSampleName();
					int sampleIndex = IntStream.range(0, samplesHighlighted.size()).filter(x -> samplesHighlighted.get(x).getSampleName().equals(currentSampleName)).findFirst().orElse(-1);
					if(sampleIndex == -1) {
						highlightedSamples.add(samples.getSamples().stream().filter(x -> x.getSampleName().equals(currentSampleName)).findFirst().get());
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREBAR_SAMPLE, highlightedSamples.toArray());
					} else {
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREBAR_SAMPLE, highlightedSamples.toArray());
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

					List<SampleScore> sampleScores = getSampleScoreList();
					List<ISample> samplesHighlighted = evaluationPCA.getHighlightedSamples();

					String currentSampleName = sampleScores.get(clickIndex).getSampleName();
					int sampleIndex = IntStream.range(0, samplesHighlighted.size()).filter(x -> samplesHighlighted.get(x).getSampleName().equals(currentSampleName)).findFirst().orElse(-1);
					if(sampleIndex == -1) {
						samplesHighlighted.add(samples.getSamples().stream().filter(x -> x.getSampleName().equals(currentSampleName)).findFirst().get());
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREBAR_SAMPLE, samplesHighlighted.toArray());
					} else {
						samplesHighlighted.remove(sampleIndex);
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREBAR_SAMPLE, samplesHighlighted.toArray());
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
			double[] explainedVariances = evaluationPCA.getResults().getExplainedVariances();

			int maxPCs = Math.min(numberOfPCs, explainedVariances.length);

			for(int i = 0; i < maxPCs; i++) {
				String label = String.format("PC%d (%.1f%%)", i + 1, explainedVariances[i] * 100);
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

		ScoreBarChart chart = chartControl.get();
		if(chart != null) {
			if(evaluationPCA != null) {

				BaseChart baseChart = chart.getBaseChart();
				double xRangeStart = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange().lower;
				double xRangeEnd = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange().upper;
				double yRangeStart = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange().lower;
				double yRangeEnd = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange().upper;

				if(initialDraw) {
					xRangeEnd = evaluationPCA.getResults().getPcaResultList().size();
					yRangeStart = getSampleScoreList().stream().mapToDouble(x -> x.getScore()).max().getAsDouble();
					yRangeEnd = getSampleScoreList().stream().mapToDouble(x -> x.getScore()).min().getAsDouble();
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

	private static class SampleScore {

		private final String sampleName;
		private final double score;

		public SampleScore(String sampleName, double score) {

			this.sampleName = sampleName;
			this.score = score;
		}

		public String getSampleName() {

			return sampleName;
		}

		public double getScore() {

			return score;
		}
	}

	private boolean isBoxSelection() {

		if(userSelection.getStartX() != 0 && userSelection.getStartY() != 0 && userSelection.getStopX() != 0 && userSelection.getStopY() != 0) {
			return true;
		}
		return false;
	}

	private List<SampleScore> getSampleScoreList() {

		IResultsMVA results = evaluationPCA.getResults();
		List<IResultMVA> resultList = results.getPcaResultList();
		List<SampleScore> sampleScores = new ArrayList<>();
		for(IResultMVA result : resultList) {
			ISample sample = result.getSample();
			double[] scoreMatrix = result.getScoreVector();

			if(currentPC < scoreMatrix.length) {
				double score = scoreMatrix[currentPC];
				String sampleName = sample.getSampleName();
				sampleScores.add(new SampleScore(sampleName, score));
			}
		}

		sampleScores.sort(Comparator.comparingDouble(SampleScore::getScore).reversed());
		return sampleScores;
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