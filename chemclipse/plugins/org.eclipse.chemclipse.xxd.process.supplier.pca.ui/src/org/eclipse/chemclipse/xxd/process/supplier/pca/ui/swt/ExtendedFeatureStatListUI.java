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
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.FeatureDataMatrix;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IEvaluation;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResult;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

public class ExtendedFeatureStatListUI extends Composite implements IExtendedPartUI {

	private AtomicReference<FeatureStatListUI> listControl = new AtomicReference<>();

	private IEvaluation<IVariable, ISample, IResult> evaluation = null;
	private FeatureDataMatrix featureDataMatrix = null;

	private Composite control;

	public ExtendedFeatureStatListUI(Composite parent, int style) {

		super(parent, style);
		createControl();

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		dataUpdateSupport.add((topic, objects) -> {

			if(evaluation != null) {
				if(DataUpdateSupport.isVisible(control)) {
					if(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LIST_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_FOLDCHANGE_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGBAR_VARIABLE.equals(topic) || //
							IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LOADINGLIST_VARIABLE.equals(topic)) {
						if(objects.size() == 1) {
							Object object = objects.get(0);
							ArrayList<Feature> features = new ArrayList<>();
							if(object instanceof Object[] values) {
								int length = values.length;
								for(int i = 0; i < length; i++) {
									if(values[i] instanceof Feature feature) {
										features.add(feature);
									} else if(values[i] instanceof IVariable) {
										IVariable variable = (IVariable)values[i];
										for(Feature feature : evaluation.getFeatureDataMatrix().getFeatures()) {
											if(feature.getVariable().equals(variable)) {
												features.add(feature);
											}
										}
									}
								}
								if(features.size() >= 0) {
									listControl.get().setSelection(new StructuredSelection(features));
									if(!features.isEmpty()) {
										listControl.get().reveal(features.get(0));
									}
								}
							}
						}
					}
				}
			}
		});
	}

	public void setInput(IEvaluation<IVariable, ISample, IResult> evaluation) {

		if(doUpdate(evaluation)) {
			this.evaluation = evaluation;
			updateInput(true);
		}
	}

	public void updateSelection() {

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		List<Object> objects = dataUpdateSupport.getUpdates(getLastTopic(dataUpdateSupport.getTopics()));
		if(!objects.isEmpty()) {
			Object object = objects.get(0);
			ArrayList<Feature> features = new ArrayList<>();
			if(object instanceof Object[] values) {
				int length = values.length;
				for(int i = 0; i < length; i++) {
					if(values[i] instanceof Feature feature) {
						features.add(feature);
					} else if(values[i] instanceof IVariable) {
						IVariable variable = (IVariable)values[i];
						for(Feature feature : evaluation.getFeatureDataMatrix().getFeatures()) {
							if(feature.getVariable().equals(variable)) {
								features.add(feature);
							}
						}
					}
				}
			}
			UpdateNotifierUI.update(Display.getDefault(), IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE, features.toArray());
		}
	}

	private String getLastTopic(List<String> topics) {

		Collections.reverse(topics);
		for(String topic : topics) {
			if(topic.equals(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_PLOT_VARIABLE) || //
					topic.equals(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_FOLDCHANGE_VARIABLE) || //
					topic.equals(IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_STATLIST_VARIABLE)) {
				return topic;
			}
		}
		return ""; //$NON-NLS-1$
	}

	private boolean doUpdate(IEvaluation<IVariable, ISample, IResult> evaluation) {

		return this.evaluation != evaluation;
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		createList(this);
		control = this;
	}

	private void createList(Composite parent) {

		FeatureStatListUI featureStatListUI = new FeatureStatListUI(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		Table table = featureStatListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int[] selectedIndices = table.getSelectionIndices();
				ArrayList<Object> selectedElements = new ArrayList<>();
				for(int index : selectedIndices) {
					selectedElements.add(featureStatListUI.getElementAt(index));
				}
				handleRowSelection(selectedElements);
			}
		});

		featureStatListUI.setUpdateListener(() -> UpdateNotifierUI.update(Display.getDefault(), IChemClipseEvents.TOPIC_PCA_UPDATE_FEATURES, evaluation));

		listControl.set(featureStatListUI);
	}

	private void handleRowSelection(List<Object> selectedElements) {

		if(selectedElements.isEmpty()) {
			UpdateNotifierUI.update(getDisplay(), IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_STATLIST_VARIABLE, selectedElements.toArray());
		} else if(Feature.class.isInstance(selectedElements.get(0))) {
			ArrayList<Feature> features = new ArrayList<>();
			for(Object element : selectedElements) {
				if(Feature.class.isInstance(element)) {
					features.add((Feature)element);
				}
			}
			UpdateNotifierUI.update(getDisplay(), IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_STATLIST_VARIABLE, selectedElements.toArray());
		}
	}

	private void updateInput(boolean updateFeatures) {

		if(updateFeatures) {
			featureDataMatrix = evaluation != null ? evaluation.getFeatureDataMatrix() : null;
		}
		updateInput();
	}

	private void updateInput() {

		listControl.get().clear();

		getDisplay().asyncExec(() -> {
			updateWidgets();
		});
	}

	private void updateWidgets() {

		FeatureStatListUI featureStatListUI = listControl.get();
		featureStatListUI.clearColumns();
		featureStatListUI.setInput(featureDataMatrix);
	}
}
