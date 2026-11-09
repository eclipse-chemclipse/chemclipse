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

import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d.LoadingTable;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.LoadingRow;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class ExtendedLoadingTable extends Composite implements IExtendedPartUI {

	private AtomicReference<LoadingTable> listControl = new AtomicReference<>();
	private EvaluationPCA evaluationPCA;
	private Composite control;

	public ExtendedLoadingTable(Composite parent, int style) {

		super(parent, style);
		createControl();

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		dataUpdateSupport.add((topic, objects) -> {

			if(evaluationPCA != null) {
				if(DataUpdateSupport.isVisible(control)) {
					if(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGLIST_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGBAR_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LIST_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_STATLIST_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_FOLDCHANGE_VARIABLE.equals(topic)) {
						if(objects.size() == 1) {
							Object object = objects.get(0);
							ArrayList<IVariable> variables = new ArrayList<>();
							if(object instanceof Object[] values) {
								for(int i = 0; i < values.length; i++) {
									if(values[i] instanceof IVariable variable) {
										variables.add(variable);
									}
								}
								if(!variables.isEmpty()) {
									ArrayList<LoadingRow> loadingRows = new ArrayList<>();
									for(IVariable variable : variables) {
										@SuppressWarnings("unchecked")
										LoadingRow row = ((ArrayList<LoadingRow>)listControl.get().getTableViewer().getInput()).stream().filter(x -> x.getVariableName().equals(variable.getValue())).findFirst().orElse(null);
										if(row != null) {
											loadingRows.add(row);
										}
									}
									if(!loadingRows.isEmpty()) {
										listControl.get().getTableViewer().setSelection(new StructuredSelection(loadingRows));
									}
									evaluationPCA.setHighlightedVariables(variables);
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
		createLoadingTable(this);
		control = this;
		layout(true, true);
	}

	private void createLoadingTable(Composite parent) {

		LoadingTable loadingTable = new LoadingTable(parent, SWT.NONE);
		Table table = loadingTable.getTableViewer().getTable();
		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int[] selectedIndices = table.getSelectionIndices();
				ArrayList<Object> selectedElements = new ArrayList<>();
				for(int index : selectedIndices) {
					String currentVariableName = ((LoadingRow)loadingTable.getTableViewer().getElementAt(index)).getVariableName();
					IVariable highlightedVariable = evaluationPCA.getResults().getExtractedVariables().stream().filter(x -> x.getValue().equals(currentVariableName)).findFirst().orElse(null);

					if(highlightedVariable != null) {
						selectedElements.add(highlightedVariable);
					}
				}
				handleRowSelection(selectedElements);
			}
		});
		loadingTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		listControl.set(loadingTable);
	}

	@Override
	public void update() {

		updateTable();
	}

	private void handleRowSelection(List<Object> selectedElements) {

		if(selectedElements.isEmpty()) {
			UpdateNotifierUI.update(getDisplay(), IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGLIST_VARIABLE, selectedElements.toArray());
		} else if(IVariable.class.isInstance(selectedElements.get(0))) {
			ArrayList<IVariable> variables = new ArrayList<>();
			for(Object element : selectedElements) {
				if(IVariable.class.isInstance(element)) {
					variables.add((IVariable)element);
				}
			}
		}
		UpdateNotifierUI.update(getDisplay(), IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGLIST_VARIABLE, selectedElements.toArray());
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

		if(evaluationPCA != null && listControl.get() != null) {
			List<IVariable> highlightedVariables = evaluationPCA.getHighlightedVariables();
			if(highlightedVariables != null && !highlightedVariables.isEmpty()) {
				ArrayList<LoadingRow> loadingRows = new ArrayList<>();
				for(IVariable variable : highlightedVariables) {
					@SuppressWarnings("unchecked")
					LoadingRow row = ((ArrayList<LoadingRow>)listControl.get().getTableViewer().getInput()).stream().filter(x -> x.getVariableName().equals(variable.getValue())).findFirst().orElse(null);
					if(row != null) {
						loadingRows.add(row);
					}
				}
				if(!loadingRows.isEmpty()) {
					listControl.get().getTableViewer().setSelection(new StructuredSelection(loadingRows));
				}
			}
		}
	}

	private String getLastTopic(List<String> topics) {

		Collections.reverse(topics);
		for(String topic : topics) {
			if(topic.equals(IChemClipseEvents.TOPIC_PCA_UPDATE_SELECTION) || //
					topic.equals(IChemClipseEvents.TOPIC_PCA_UPDATE_RESULT)) {
				return topic;
			}
		}

		return ""; //$NON-NLS-1$
	}
}
