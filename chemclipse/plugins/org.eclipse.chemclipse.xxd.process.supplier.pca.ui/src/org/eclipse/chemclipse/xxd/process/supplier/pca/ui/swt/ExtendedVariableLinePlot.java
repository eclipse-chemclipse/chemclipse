/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IAnalysisSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IEvaluation;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResult;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ISamplesPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d.VariableLinePlot;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.help.HelpContext;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePage;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePageFoldChangePlot;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.AlphanumericStringComparator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.FeatureColumnLabels;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.core.UserSelection;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;

public class ExtendedVariableLinePlot extends Composite implements IExtendedPartUI {

	private static final String VARIABLE_LINE_PLOT_SELECT_NONE = "--";
	private AtomicReference<VariableLinePlot> plotControl = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboViewerVariables = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboViewerCategoryLabelType = new AtomicReference<>();
	private AtomicReference<Spinner> spinnerControlFontSize = new AtomicReference<>();
	private IEvaluation<IVariable, ISample, IResult> evaluation = null;
	private Composite control;
	private ISamplesPCA<IVariable, ISample> samples = null;
	private ArrayList<String> variables = new ArrayList<>();
	private String lastVariableSelection = VARIABLE_LINE_PLOT_SELECT_NONE;
	private UserSelection userSelection = new UserSelection();
	private ArrayList<Integer> sortedSampleIndices = new ArrayList<>();

	public ExtendedVariableLinePlot(Composite parent, int style) {

		super(parent, style);
		createControl();
		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		dataUpdateSupport.add((topic, objects) -> {

			if(evaluation != null) {
				if(DataUpdateSupport.isVisible(control)) {
					if(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREPLOT_SAMPLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCOREBAR_SAMPLE.equals(topic) || //
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
							evaluation.setHighlightedSamples(samples);
							setInput(evaluation);
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

	public void setInput(IEvaluation<IVariable, ISample, IResult> evaluation) {

		this.evaluation = evaluation;
		if(this.evaluation != null) {
			this.samples = evaluation.getSamples();
		}
		updateInput();
	}

	public void updateInput() {

		if(samples != null) {
			IAnalysisSettings analysisSettings = samples.getAnalysisSettings();
			updateSortedIndices();
			updateWidgets(analysisSettings);
		}
		applySettings();
	}

	public void updateSortedIndices() {

		sortedSampleIndices.clear();
		ArrayList<ISample> sortedSamples = new ArrayList<>();
		for(int i = 0; i < samples.getSamples().size(); i++) {
			sortedSamples.add(samples.getSamples().get(i));
		}
		if(comboViewerCategoryLabelType.get().getStructuredSelection().getFirstElement().toString().equals(FeatureColumnLabels.GROUPNAMES.toString())) {
			sortedSamples.sort(new GroupComparator());
		} else {
			sortedSamples.sort(new SampleNameComparator());
		}
		for(ISample sample : sortedSamples) {
			sortedSampleIndices.add(IntStream.range(0, samples.getSamples().size()).filter(x -> samples.getSamples().get(x).equals(sample)).findFirst().getAsInt());
		}
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));

		createToolbarMain(this);
		createPlot(this);
		initialize();
		control = this;
	}

	private void initialize() {

		variables.add(VARIABLE_LINE_PLOT_SELECT_NONE);
		comboViewerVariables.get().setInput(variables);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(7, false));

		createComboViewerCategoryLabelType(composite);
		createLabel(composite, "Font Size");
		createSpinnerFontSize(composite);
		createLabel(composite, "Variable:");
		createComboViewerVariable(composite);
		createSettingsButton(composite);
		createButtonHelp(composite, HelpContext.FOLD_CHANGE_PLOT);
	}

	private Label createLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		return label;
	}

	private void createComboViewerCategoryLabelType(Composite parent) {

		ComboViewer comboViewer = new ComboViewer(parent, SWT.READ_ONLY);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof FeatureColumnLabels featureColumnLabel) {
					return featureColumnLabel.label();
				}
				return null;
			}
		});
		Combo combo = comboViewer.getCombo();
		combo.setToolTipText("Select Column Label");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = comboViewer.getStructuredSelection().getFirstElement();
				if(object instanceof FeatureColumnLabels featureColumnLabel) {
					if(evaluation != null) {
						if(featureColumnLabel.equals(FeatureColumnLabels.SAMPLENAMES)) {
							plotControl.get().setCategoryLabelType(FeatureColumnLabels.SAMPLENAMES);
							updateInput();
						} else {
							plotControl.get().setCategoryLabelType(FeatureColumnLabels.GROUPNAMES);
							updateInput();
						}
					}
				}
			}
		});

		comboViewer.setInput(FeatureColumnLabels.values());
		comboViewer.setSelection(new StructuredSelection(FeatureColumnLabels.SAMPLENAMES));

		comboViewerCategoryLabelType.set(comboViewer);
	}

	private void createSpinnerFontSize(Composite parent) {

		Spinner spinner = new Spinner(parent, SWT.BORDER);
		spinner.setToolTipText("Font Size X-Axis");
		spinner.setMinimum(PreferenceSupplier.MIN_VARIABLE_LINE_PLOT_XAXIS_FONT_SIZE);
		spinner.setIncrement(1);
		spinner.setSelection(PreferenceSupplier.DEF_VARIABLE_LINE_PLOT_XAXIS_FONT_SIZE);
		spinner.setMaximum(PreferenceSupplier.MAX_VARIABLE_LINE_PLOT_XAXIS_FONT_SIZE);
		spinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(samples != null) {
					IAnalysisSettings analysisSettings = samples.getAnalysisSettings();
					if(analysisSettings != null) {
						analysisSettings.setVariableLinePlotFontSize(spinner.getSelection());
						updateInput();
					}
				}
			}
		});
		spinnerControlFontSize.set(spinner);
	}

	private void createComboViewerVariable(Composite parent) {

		ComboViewer comboViewer = new ComboViewer(parent, SWT.BORDER | SWT.READ_ONLY);

		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof String value) {
					return value;
				} else {
					return "";
				}
			}
		});
		Combo combo = comboViewer.getCombo();
		combo.setToolTipText("Variable to plot");
		GridData gridData = new GridData();
		gridData.widthHint = 350;
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(samples != null) {
					IAnalysisSettings analysisSettings = samples.getAnalysisSettings();
					if(analysisSettings != null) {
						analysisSettings.setVariableLinePlotVariable(comboViewer.getStructuredSelection().getFirstElement().toString());
						UpdateNotifierUI.update(e.display, IChemClipseEvents.TOPIC_PCA_UPDATE_GROUPS, comboViewer.getStructuredSelection().getFirstElement().toString());
					}
				}
			}
		});
		comboViewerVariables.set(comboViewer);
	}

	private void createPlot(Composite parent) {

		VariableLinePlot plot = new VariableLinePlot(parent, SWT.BORDER);
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

				if(evaluation != null) {
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

				if(evaluation != null && isBoxSelection()) {
					/*
					 * Prepare Data viewport
					 */
					Rectangle rectangle = baseChart.getPlotArea().getBounds();
					int width = rectangle.width;
					Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
					/*
					 * Determine x|y coordinates Start/Stop.
					 */
					int startX = userSelection.getStartX();
					int stopX = userSelection.getStopX();
					if(startX > stopX) {
						int flip = startX;
						startX = stopX;
						stopX = flip;
					}
					/*
					 * Calculate selected points.
					 */
					double pXStart = rangeX.lower + (rangeX.upper - rangeX.lower) * ((1.0d / width) * startX);
					double pXStop = rangeX.lower + (rangeX.upper - rangeX.lower) * ((1.0d / width) * stopX);
					/*
					 * Map the result deltas.
					 */
					List<ISample> samplesHighlighted = evaluation.getHighlightedSamples();
					/*
					 * get samples within box selection
					 */
					for(int i = 0; i < evaluation.getSamples().getSamples().size(); i++) {
						if(i > pXStart && i < pXStop) {
							if(!samplesHighlighted.contains(getSampleForPlotIndex(i))) {
								samplesHighlighted.add(getSampleForPlotIndex(i));
							}
						}
					}
					/*
					 * Send Update event.
					 */
					if(!samplesHighlighted.isEmpty()) {
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_VARIABLELINE_SAMPLE, samplesHighlighted.toArray());
					}
					/*
					 * Finish User Selection Process
					 */
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

				if(evaluation != null) {
					/*
					 * Determine the x coordinate.
					 */
					Rectangle rectangle = baseChart.getPlotArea().getBounds();
					double x = event.x;
					double width = rectangle.width;
					/*
					 * Calculate the sample index from screen coordinate.
					 */
					Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();

					int index = (int)Math.round(rangeX.lower + (rangeX.upper - rangeX.lower) * ((1.0d / width) * x));
					/*
					 * Apply highlighting logic.
					 */
					if(index < evaluation.getSamples().getSamples().size() || index > -1) {
						List<ISample> samplesHighlighted = evaluation.getHighlightedSamples();
						List<ISample> highlightedSamples = new ArrayList<>();
						if(samplesHighlighted.isEmpty()) {
							highlightedSamples.add(getSampleForPlotIndex(index));
							UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_VARIABLELINE_SAMPLE, highlightedSamples.toArray());
						} else {
							if(samplesHighlighted.contains(getSampleForPlotIndex(index))) {
								UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_VARIABLELINE_SAMPLE, highlightedSamples.toArray());
							} else {
								highlightedSamples.add(getSampleForPlotIndex(index));
								UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_VARIABLELINE_SAMPLE, highlightedSamples.toArray());
							}

						}
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

				if(evaluation != null) {
					/*
					 * Determine the x coordinate.
					 */
					Rectangle rectangle = baseChart.getPlotArea().getBounds();
					double x = event.x;
					double width = rectangle.width;
					/*
					 * Calculate the sample index from screen coordinates.
					 */
					Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();

					int index = (int)Math.round(rangeX.lower + (rangeX.upper - rangeX.lower) * ((1.0d / width) * x));
					/*
					 * Apply highlighting logic.
					 */
					if(index < evaluation.getSamples().getSamples().size() || index > -1) {
						List<ISample> samplesHighlighted = evaluation.getHighlightedSamples();
						if(samplesHighlighted.isEmpty()) {
							samplesHighlighted.add(getSampleForPlotIndex(index));
							UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_VARIABLELINE_SAMPLE, samplesHighlighted.toArray());
						} else {
							if(samplesHighlighted.contains(getSampleForPlotIndex(index))) {
								samplesHighlighted.remove(getSampleForPlotIndex(index));
								UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_VARIABLELINE_SAMPLE, samplesHighlighted.toArray());
							} else {
								samplesHighlighted.add(getSampleForPlotIndex(index));
								UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_VARIABLELINE_SAMPLE, samplesHighlighted.toArray());
							}

						}
					}
				}
				userSelection.reset();
			}
		});
		plot.applySettings(chartSettings);

		/*
		 * Paint Listener
		 */
		plot.getBaseChart().getPlotArea().addCustomPaintListener(e -> {

			if(userSelection.isActive()) {
				int xMin = Math.min(userSelection.getStartX(), userSelection.getStopX());
				int xMax = Math.max(userSelection.getStartX(), userSelection.getStopX());
				int y = Math.min(userSelection.getStartY(), userSelection.getStopY());
				BaseChart baseChart = plotControl.get().getBaseChart();
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
		plotControl.set(plot);

	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePage.class, PreferencePageFoldChangePlot.class), _ -> applySettings());
	}

	private void applySettings() {

		String variable = comboViewerVariables.get().getStructuredSelection().getFirstElement().toString();
		if(variable != null) {
			updatePlot(variable);
		}

	}

	private void updateWidgets(IAnalysisSettings analysisSettings) {

		updateVariables();
		updateComboViewerVariables(analysisSettings);

	}

	private void updateComboViewerVariables(IAnalysisSettings analysisSettings) {

		variables.sort(new AlphanumericStringComparator());
		comboViewerVariables.get().setInput(variables);
		String selection = VARIABLE_LINE_PLOT_SELECT_NONE;
		if(analysisSettings != null) {
			String variableName = analysisSettings.getVariableLinePlotVariable();
			if(variables.contains(variableName)) {
				selection = variableName;
			}
		}
		comboViewerVariables.get().setSelection(new StructuredSelection(selection));
	}

	private void updateVariables() {

		variables.clear();
		variables.add(VARIABLE_LINE_PLOT_SELECT_NONE);
		if(samples != null) {
			for(IVariable variable : samples.getVariables()) {
				if(variable.getDescription() == null) {
					variable.setDescription("");
				}
			}

			variables.addAll(samples.getVariables().stream().map(x -> x.getValue() + " " + x.getDescription()).toList());
		}
	}

	private void updatePlot(String variable) {

		boolean keepRange = true;
		if(lastVariableSelection.equals(VARIABLE_LINE_PLOT_SELECT_NONE) || !lastVariableSelection.equals(variable)) {
			keepRange = false;
		}
		lastVariableSelection = variable;
		VariableLinePlot plot = plotControl.get();
		IAxis xAxis = plot.getBaseChart().getAxisSet().getXAxis(0);
		Range range = new Range(xAxis.getRange().lower, xAxis.getRange().upper);
		plot.deleteSeries();
		if(evaluation != null) {
			plot.setInput(evaluation, variable, sortedSampleIndices);
		} else {
			plot.setInput(null, variable, sortedSampleIndices);
		}
		if(keepRange) {
			xAxis.setRange(range);
		}

	}

	private boolean isBoxSelection() {

		if(userSelection.getStartX() != 0 && userSelection.getStartY() != 0 && userSelection.getStopX() != 0 && userSelection.getStopY() != 0) {
			return true;
		}
		return false;
	}

	private ISample getSampleForPlotIndex(int plotIndex) {

		List<ISample> sortedActiveSamples;

		if(comboViewerCategoryLabelType.get().getStructuredSelection().getFirstElement().equals(FeatureColumnLabels.SAMPLENAMES)) {
			sortedActiveSamples = samples.getSamples().stream().filter(x -> x.isSelected()).sorted(new SampleNameComparator()).toList();
		} else {
			sortedActiveSamples = samples.getSamples().stream().filter(x -> x.isSelected()).sorted(new GroupComparator()).toList();
		}
		List<ISample> sample = IntStream.range(0, samples.getSamples().size() - 1).filter(x -> samples.getSamples().get(x).equals(sortedActiveSamples.get(plotIndex))).mapToObj(x -> samples.getSamples().get(x)).toList();
		if(sample.isEmpty()) {
			return null;
		} else {
			return sample.getFirst();
		}
	}

	private class GroupComparator implements Comparator<ISample> {

		@Override
		public int compare(ISample o1, ISample o2) {

			AlphanumericStringComparator stringComp = new AlphanumericStringComparator();
			String str1 = o1.getGroupName();
			String str2 = o2.getGroupName();

			return stringComp.compare(str1, str2);
		}

	}

	private class SampleNameComparator implements Comparator<ISample> {

		@Override
		public int compare(ISample o1, ISample o2) {

			AlphanumericStringComparator stringComp = new AlphanumericStringComparator();
			String str1 = o1.getSampleName();
			String str2 = o2.getSampleName();

			return stringComp.compare(str1, str2);
		}

	}

	private void updateOnFocus() {

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		List<Object> objects = dataUpdateSupport.getUpdates(getLastTopic(dataUpdateSupport.getTopics()));

		if(!objects.isEmpty()) {
			Object object = objects.get(0);
			if(object instanceof EvaluationPCA evaluation) {
				setInput(evaluation);
				updatePlot(lastVariableSelection);
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
