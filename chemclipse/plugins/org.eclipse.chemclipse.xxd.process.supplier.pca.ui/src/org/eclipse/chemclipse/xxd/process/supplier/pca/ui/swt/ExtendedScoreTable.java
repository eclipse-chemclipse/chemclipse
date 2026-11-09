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

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Sample;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d.ScoreTable;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.ScoreRow;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class ExtendedScoreTable extends Composite implements IExtendedPartUI {

	private AtomicReference<ScoreTable> listControl = new AtomicReference<>();
	private EvaluationPCA evaluationPCA;
	private Composite control;

	public ExtendedScoreTable(Composite parent, int style) {

		super(parent, style);
		createControl();

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		dataUpdateSupport.add((topic, objects) -> {

			if(evaluationPCA != null) {
				if(DataUpdateSupport.isVisible(control)) {
					if(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCORELIST_SAMPLE.equals(topic) || //
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
									if(values[i] instanceof ISample sample) {
										samples.add(sample);
									}
								}
								if(!samples.isEmpty()) {
									ArrayList<ScoreRow> scoreRows = new ArrayList<>();
									for(ISample sample : samples) {
										@SuppressWarnings("unchecked")
										ScoreRow row = ((ArrayList<ScoreRow>)listControl.get().getTableViewer().getInput()).stream().filter(x -> x.getSampleName() == sample.getSampleName()).findFirst().get();
										scoreRows.add(row);
									}
									if(!scoreRows.isEmpty()) {
										listControl.get().getTableViewer().setSelection(new StructuredSelection(scoreRows));
									}
									evaluationPCA.setHighlightedSamples(samples);
								}
							}
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
		updateTable();
	}

	public void updateTable() {

		if(listControl.get() != null && !listControl.get().isDisposed()) {
			listControl.get().setInput(evaluationPCA);
		}
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		createScoreTable(this);
		control = this;
		layout(true, true);
	}

	private void createScoreTable(Composite parent) {

		ScoreTable scoreTable = new ScoreTable(parent, SWT.NONE);
		Table table = scoreTable.getTableViewer().getTable();
		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int[] selectedIndices = table.getSelectionIndices();
				ArrayList<Object> selectedElements = new ArrayList<>();
				for(int index : selectedIndices) {
					String currentSampleName = ((ScoreRow)scoreTable.getTableViewer().getElementAt(index)).getSampleName();
					ISample highlightedSample = evaluationPCA.getSamples().getSamples().stream().filter(x -> x.getSampleName() == currentSampleName).findFirst().get();

					selectedElements.add(highlightedSample);
				}
				handleRowSelection(selectedElements);
			}
		});
		scoreTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		listControl.set(scoreTable);
	}

	@Override
	public void update() {

		updateTable();
	}

	private void handleRowSelection(List<Object> selectedElements) {

		if(selectedElements.isEmpty()) {
			UpdateNotifierUI.update(getDisplay(), IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCORELIST_SAMPLE, selectedElements.toArray());
		} else if(Sample.class.isInstance(selectedElements.get(0))) {
			ArrayList<Sample> samples = new ArrayList<>();
			for(Object element : selectedElements) {
				if(Sample.class.isInstance(element)) {
					samples.add((Sample)element);
				}
			}
		}
		UpdateNotifierUI.update(getDisplay(), IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_SCORELIST_SAMPLE, selectedElements.toArray());
	}

	private void updateOnFocus() {

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		List<Object> objects = dataUpdateSupport.getUpdates(getLastTopic(dataUpdateSupport.getTopics()));

		if(!objects.isEmpty()) {
			Object object = objects.get(0);
			if(object instanceof EvaluationPCA evaluation) {
				setInput(evaluation);
				updateSelection();
			}
		}
	}

	private void updateSelection() {

		List<ISample> highlighted = evaluationPCA.getHighlightedSamples();
		if(!highlighted.isEmpty()) {
			ArrayList<ScoreRow> scoreRows = new ArrayList<>();
			for(ISample sample : highlighted) {
				@SuppressWarnings("unchecked")
				ScoreRow row = ((ArrayList<ScoreRow>)listControl.get().getTableViewer().getInput()).stream().filter(x -> x.getSampleName() == sample.getSampleName()).findFirst().get();
				scoreRows.add(row);
			}
			if(!scoreRows.isEmpty()) {
				listControl.get().getTableViewer().setSelection(new StructuredSelection(scoreRows));
			}
		}

	}

	private String getLastTopic(List<String> topics) {

		Collections.reverse(topics);
		for(String topic : topics) {
			if(topic.equals(IChemClipseEvents.TOPIC_PCA_UPDATE_SELECTION) || topic.equals(IChemClipseEvents.TOPIC_PCA_UPDATE_RESULT)) {
				return topic;
			}
		}

		return ""; //$NON-NLS-1$
	}
}
