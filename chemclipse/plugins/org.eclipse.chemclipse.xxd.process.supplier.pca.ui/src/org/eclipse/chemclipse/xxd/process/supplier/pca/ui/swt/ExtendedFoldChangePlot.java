/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.TTest;
import org.apache.commons.math3.util.FastMath;
import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IAnalysisSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IEvaluation;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResult;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ISamplesPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.VariableDelta;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d.FoldChangePlot;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.help.HelpContext;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePage;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePageFoldChangePlot;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.AlphanumericStringComparator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;

public class ExtendedFoldChangePlot extends Composite implements IExtendedPartUI {

	private static final String FOLD_CHANGE_GROUP_NONE = "--";
	private AtomicReference<FoldChangePlot> plotControl = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboViewerGroup1 = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboViewerGroup2 = new AtomicReference<>();
	private IEvaluation<IVariable, ISample, IResult> evaluation = null;
	private Composite control;
	private ISamplesPCA<IVariable, ISample> samples = null;
	private ArrayList<String> groups = new ArrayList<>();

	public ExtendedFoldChangePlot(Composite parent, int style) {

		super(parent, style);
		createControl();
		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		dataUpdateSupport.add((topic, objects) -> {

			if(evaluation != null) {
				if(DataUpdateSupport.isVisible(control)) {
					if(IChemClipseEvents.TOPIC_PCA_UPDATE_FEATURES.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_FOLDCHANGE_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LIST_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_STATLIST_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGBAR_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SAMPLE.equals(topic) || //
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
							evaluation.setHighlightedVariables(selectedVariables);
							setInput(evaluation);
						}
					}
				}
			}
		});
	}

	public void setInput(IEvaluation<IVariable, ISample, IResult> evaluation) {

		this.evaluation = evaluation;
		if(this.evaluation != null) {
			this.samples = evaluation.getSamples();
		}
		updateFoldChangeGroups();
		updateInput();
	}

	public void updateInput() {

		if(samples != null) {
			IAnalysisSettings analysisSettings = samples.getAnalysisSettings();
			updateWidgets(analysisSettings);
		}
		applySettings();
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));

		createToolbarMain(this);
		createPlot(this);
		initialize();
		control = this;
	}

	private void initialize() {

		groups.add(FOLD_CHANGE_GROUP_NONE);
		comboViewerGroup1.get().setInput(groups);
		comboViewerGroup2.get().setInput(groups);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(6, false));

		createLabel(composite, "Group 1:");
		createComboViewerGroup1(composite);
		createLabel(composite, "Group 2:");
		createComboViewerGroup2(composite);
		createSettingsButton(composite);
		createButtonHelp(composite, HelpContext.FOLD_CHANGE_PLOT);
	}

	private Label createLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		return label;
	}

	private void createComboViewerGroup1(Composite parent) {

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
		combo.setToolTipText("Group one for comparison");
		GridData gridData = new GridData();
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(samples != null) {
					IAnalysisSettings analysisSettings = samples.getAnalysisSettings();
					if(analysisSettings != null) {
						analysisSettings.setComparisonGroup1(comboViewer.getStructuredSelection().getFirstElement().toString());
						UpdateNotifierUI.update(e.display, IChemClipseEvents.TOPIC_PCA_UPDATE_GROUPS, comboViewer.getStructuredSelection().getFirstElement().toString());
					}
				}
			}
		});
		comboViewerGroup1.set(comboViewer);
	}

	private void createComboViewerGroup2(Composite parent) {

		ComboViewer comboViewer = new ComboViewer(parent, SWT.BORDER | SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
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
		combo.setToolTipText("Group two for comparison");
		GridData gridData = new GridData();
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(samples != null) {
					IAnalysisSettings analysisSettings = samples.getAnalysisSettings();
					if(analysisSettings != null) {
						analysisSettings.setComparisonGroup2(comboViewer.getStructuredSelection().getFirstElement().toString());
						UpdateNotifierUI.update(e.display, IChemClipseEvents.TOPIC_PCA_UPDATE_GROUPS, comboViewer.getStructuredSelection().getFirstElement().toString());
					}
				}
			}
		});
		comboViewerGroup2.set(comboViewer);
	}

	private void createPlot(Composite parent) {

		FoldChangePlot plot = new FoldChangePlot(parent, SWT.BORDER);
		plot.setLayoutData(new GridData(GridData.FILL_BOTH));

		IChartSettings chartSettings = plot.getChartSettings();
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
					 * Extract the selected Groups.
					 */
					List<ISample> group1 = new ArrayList<>();
					List<ISample> group2 = new ArrayList<>();

					for(ISample sample : evaluation.getSamples().getSamples()) {
						if(sample.getGroupName().equals(comboViewerGroup1.get().getStructuredSelection().getFirstElement().toString())) {
							group1.add(sample);
						}
						if(sample.getGroupName().equals(comboViewerGroup2.get().getStructuredSelection().getFirstElement().toString())) {
							group2.add(sample);
						}
					}

					/*
					 * Extract selected variables and calculate pValue / fold Change
					 */
					List<VariableDelta> variableDeltas = new ArrayList<>();
					List<IVariable> selectedVariableList = new ArrayList<>();
					List<Double> pValueList = new ArrayList<>();
					List<Double> foldChangeList = new ArrayList<>();
					for(int i = 0; i < evaluation.getSamples().getVariables().size(); i++) {
						if(evaluation.getSamples().getVariables().get(i).isSelected()) {
							DescriptiveStatistics stats1 = new DescriptiveStatistics();
							DescriptiveStatistics stats2 = new DescriptiveStatistics();
							for(ISample sample : group1) {
								stats1.addValue(sample.getSampleData().get(i).getData());
							}
							for(ISample sample : group2) {
								stats2.addValue(sample.getSampleData().get(i).getData());
							}
							TTest test = new TTest();
							double pValue = test.tTest(stats1.getValues(), stats2.getValues());
							double mean1 = stats1.getMean();
							double mean2 = stats2.getMean();
							double foldChange = mean1 / mean2;
							double minLog10pValue = -FastMath.log10(pValue);
							double log2FoldChange = FastMath.log(foldChange) / FastMath.log(2);
							selectedVariableList.add(evaluation.getSamples().getVariables().get(i));
							pValueList.add(minLog10pValue);
							foldChangeList.add(log2FoldChange);
						}
					}

					/*
					 * Prepare a result object with loading vectors per variable
					 */
					for(int i = 0; i < selectedVariableList.size(); i++) {
						IPoint pointResult = new Point(foldChangeList.get(i), pValueList.get(i));

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
							variableDeltas.add(new VariableDelta(selectedVariableList.get(i), deltaX, deltaY));
						}
					}
					/*
					 * Get the closest result.
					 */
					if(!variableDeltas.isEmpty()) {
						Collections.sort(variableDeltas, Comparator.comparing(VariableDelta::getDistance));
						VariableDelta variableDelta = variableDeltas.get(0);
						List<IVariable> variableList = new ArrayList<>();
						variableList.add(variableDelta.getVariable());
						UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_FOLDCHANGE_VARIABLE, variableList.toArray());
					}
				}
			}
		});
		plot.applySettings(chartSettings);
		plotControl.set(plot);
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePage.class, PreferencePageFoldChangePlot.class), _ -> applySettings());
	}

	private void applySettings() {

		String group1 = comboViewerGroup1.get().getStructuredSelection().getFirstElement().toString();
		String group2 = comboViewerGroup2.get().getStructuredSelection().getFirstElement().toString();
		if(group1 != null && group1 != null) {
			updatePlot(group1, group2);
		}

	}

	private void updateWidgets(IAnalysisSettings analysisSettings) {

		updateFoldChangeGroups();
		updateComboViewerGroups(analysisSettings);
	}

	private void updatePlot(String group1, String group2) {

		FoldChangePlot plot = plotControl.get();
		plot.deleteSeries();
		if(evaluation != null) {
			plot.setInput(evaluation, group1, group2);
		} else {
			plot.setInput(null, group1, group2);
		}
	}

	private void updateComboViewerGroups(IAnalysisSettings analysisSettings) {

		groups.sort(new AlphanumericStringComparator());
		comboViewerGroup1.get().setInput(groups);
		comboViewerGroup2.get().setInput(groups);
		String selection1 = FOLD_CHANGE_GROUP_NONE;
		String selection2 = FOLD_CHANGE_GROUP_NONE;
		if(analysisSettings != null) {
			String groupName1 = analysisSettings.getComparisonGroup1();
			if(groups.contains(groupName1)) {
				selection1 = groupName1;
			}
			String groupName2 = analysisSettings.getComparisonGroup2();
			if(groups.contains(groupName2)) {
				selection2 = groupName2;
			}
		}
		comboViewerGroup1.get().setSelection(new StructuredSelection(selection1));
		comboViewerGroup2.get().setSelection(new StructuredSelection(selection2));
	}

	private void updateFoldChangeGroups() {

		groups.clear();
		groups.add(FOLD_CHANGE_GROUP_NONE);
		if(samples != null) {
			for(ISample sample : samples.getSamples()) {
				if(sample.getGroupName() == null) {
					logger.warn("Fix GroupName (must not be null): " + sample);
					sample.setGroupName("");
				}
			}
			/*
			 * Map Group Names
			 */
			groups.addAll(samples.getSamples().stream().map(x -> x.getGroupName()).distinct().toList());
		}
	}

}
