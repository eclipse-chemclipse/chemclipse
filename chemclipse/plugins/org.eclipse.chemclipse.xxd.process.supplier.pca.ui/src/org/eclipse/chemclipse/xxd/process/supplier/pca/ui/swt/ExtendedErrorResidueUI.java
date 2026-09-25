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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d.ErrorResidueChart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.core.UserSelection;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;

public class ExtendedErrorResidueUI extends Composite implements IExtendedPartUI {

	private AtomicReference<ErrorResidueChart> chartControl = new AtomicReference<>();
	private EvaluationPCA evaluationPCA = null;
	private Composite control;
	private UserSelection userSelection = new UserSelection();

	public ExtendedErrorResidueUI(Composite parent, int style) {

		super(parent, style);
		createControl();
		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		dataUpdateSupport.add((topic, objects) -> {

			if(evaluationPCA != null) {
				if(DataUpdateSupport.isVisible(control)) {
					if(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_ERRORRESIDUALS_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREBAR_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREPLOT_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCORELIST_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_VARIABLELINE_SAMPLE.equals(topic)) {
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
		updatePlot();
	}

	public void updatePlot() {

		chartControl.get().setInput(evaluationPCA);
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		createPlot(this);
		control = this;

	}

	private void createPlot(Composite parent) {

		ErrorResidueChart chart = new ErrorResidueChart(parent, SWT.BORDER);
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
					List<IResultMVA> resultPerSample = evaluationPCA.getResults().getPcaResultList();
					int barIndex = (int)Math.round(xValue);
					if(barIndex >= 0 && barIndex < resultPerSample.size()) {
						String sampleName = resultPerSample.get(barIndex).getSample().getSampleName();
						String tooltip = "Sample: " + sampleName + "\nValue: " + String.format("%.2f", resultPerSample.get(barIndex).getErrorMetric());
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
					List<IResultMVA> resultsPerSample = evaluationPCA.getResults().getPcaResultList();
					if(pXStart < 0) {
						pXStart = 0;
					} else if(pXStop < 0) {
						pXStop = 0;
					} else if(pXStop > resultsPerSample.size() - 1) {
						pXStop = resultsPerSample.size() - 1;
					}
					List<ISample> samplesHighlighted = evaluationPCA.getHighlightedSamples();
					for(int i = pXStart; i <= pXStop; i++) {
						String currentSampleName = resultsPerSample.get(i).getSample().getSampleName();
						int index = IntStream.range(0, samplesHighlighted.size()).filter(x -> samplesHighlighted.get(x).getSampleName().equals(currentSampleName)).findFirst().orElse(-1);
						if(index == -1) {
							samplesHighlighted.add(evaluationPCA.getSamples().getSamples().stream().filter(x -> x.getSampleName().equals(currentSampleName)).findFirst().get());
						}
					}

					if(!samplesHighlighted.isEmpty()) {
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_ERRORRESIDUALS_SAMPLE, samplesHighlighted.toArray());
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

					List<IResultMVA> resultsPerSample = evaluationPCA.getResults().getPcaResultList();
					List<ISample> highlightedSamples = new ArrayList<>();
					List<ISample> samplesHighlighted = evaluationPCA.getHighlightedSamples();

					String currentSampleName = resultsPerSample.get(clickIndex).getSample().getSampleName();
					int sampleIndex = IntStream.range(0, samplesHighlighted.size()).filter(x -> samplesHighlighted.get(x).getSampleName().equals(currentSampleName)).findFirst().orElse(-1);
					if(sampleIndex == -1) {
						highlightedSamples.add(evaluationPCA.getSamples().getSamples().stream().filter(x -> x.getSampleName().equals(currentSampleName)).findFirst().get());
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_ERRORRESIDUALS_SAMPLE, highlightedSamples.toArray());
					} else {
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_ERRORRESIDUALS_SAMPLE, highlightedSamples.toArray());
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

					List<IResultMVA> resultsPerSample = evaluationPCA.getResults().getPcaResultList();
					List<ISample> samplesHighlighted = evaluationPCA.getHighlightedSamples();

					String currentSampleName = resultsPerSample.get(clickIndex).getSample().getSampleName();
					int sampleIndex = IntStream.range(0, samplesHighlighted.size()).filter(x -> samplesHighlighted.get(x).getSampleName().equals(currentSampleName)).findFirst().orElse(-1);
					if(sampleIndex == -1) {
						samplesHighlighted.add(evaluationPCA.getSamples().getSamples().stream().filter(x -> x.getSampleName().equals(currentSampleName)).findFirst().get());
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_ERRORRESIDUALS_SAMPLE, samplesHighlighted.toArray());
					} else {
						samplesHighlighted.remove(sampleIndex);
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_ERRORRESIDUALS_SAMPLE, samplesHighlighted.toArray());
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
