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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.editors.SystemEditor;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.swt.ui.components.ISearchListener;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.IDataUpdateListener;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ISettingsHandler;
import org.eclipse.chemclipse.xxd.process.supplier.pca.core.ProcessorPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.io.FeatureDataMatrixIO;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.FeatureDataMatrix;
import org.eclipse.chemclipse.xxd.process.supplier.pca.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences.PreferencePage;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;

public class ExtendedFeatureListUI extends Composite implements IExtendedPartUI {

	private Button buttonToolbarSearch;
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private AtomicReference<FeatureListUI> listControl = new AtomicReference<>();
	private Button buttonToolbarInfo;
	private AtomicReference<InformationUI> toolbarInfo = new AtomicReference<>();
	//
	private EvaluationPCA evaluationPCA = null;
	private FeatureDataMatrix featureDataMatrix = null;
	//
	private Composite control;

	public ExtendedFeatureListUI(Composite parent, int style) {

		super(parent, style);
		createControl();
		//
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
								ArrayList<Feature> features = new ArrayList<>();
								if(object instanceof Object[] values) {
									int length = values.length;
									for(int i = 0; i < length; i++) {
										if(values[i] instanceof Feature feature) {
											features.add(feature);
										}
									}
									if(features.size() >= 0) {
										listControl.get().setSelection(new StructuredSelection(features));
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

	public void setInput(EvaluationPCA evaluationPCA) {

		if(doUpdate(evaluationPCA)) {
			this.evaluationPCA = evaluationPCA;
			updateInput(true);
		}
	}

	private boolean doUpdate(EvaluationPCA evaluationPCA) {

		return this.evaluationPCA != evaluationPCA;
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		createToolbarSearch(this);
		createList(this);
		createToolbarInfo(this);
		//
		initialize();
		control = this;
	}

	private void initialize() {

		enableToolbar(toolbarSearch, buttonToolbarSearch, IMAGE_SEARCH, TOOLTIP_EDIT, false);
		enableToolbar(toolbarInfo, buttonToolbarInfo, IMAGE_INFO, TOOLTIP_INFO, true);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(6, false));
		//
		buttonToolbarInfo = createButtonToggleToolbar(composite, toolbarInfo, IMAGE_INFO, TOOLTIP_INFO);
		buttonToolbarSearch = createButtonToggleToolbar(composite, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH);
		createButtonCleanVariables(composite);
		createButtonReset(composite);
		createButtonExport(composite);
		createSettingsButton(composite);
	}

	private void createToolbarSearch(Composite parent) {

		SearchSupportUI searchSupportUI = new SearchSupportUI(parent, SWT.NONE);
		searchSupportUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		searchSupportUI.setSearchListener(new ISearchListener() {

			@Override
			public void performSearch(String searchText, boolean caseSensitive) {

				listControl.get().setSearchText(searchText, caseSensitive);
				updateInfoLabel();
			}
		});
		//
		toolbarSearch.set(searchSupportUI);
	}

	private void createList(Composite parent) {

		FeatureListUI featureListUI = new FeatureListUI(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.VIRTUAL);
		Table table = featureListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		ITableSettings tableSettings = featureListUI.getTableSettings();
		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return "Toggle Use";
			}

			@Override
			public String getCategory() {

				return "Features";
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				Iterator<?> iterator = featureListUI.getStructuredSelection().iterator();
				while(iterator.hasNext()) {
					if(iterator.next() instanceof Feature feature) {
						IVariable variable = feature.getVariable();
						variable.setSelected(!variable.isSelected());
					}
				}
				featureListUI.refresh();
			}
		});
		featureListUI.applySettings(tableSettings);
		//
		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int[] selectedIndices = table.getSelectionIndices();
				ArrayList<Object> selectedElements = new ArrayList<>();
				for(int index : selectedIndices) {
					selectedElements.add(featureListUI.getElementAt(index));
				}
				handleRowSelection(selectedElements);
			}
		});
		//
		featureListUI.setUpdateListener(new IUpdateListener() {

			@Override
			public void update() {

				UpdateNotifierUI.update(Display.getDefault(), IChemClipseEvents.TOPIC_PCA_UPDATE_FEATURES, evaluationPCA);
			}
		});
		//
		listControl.set(featureListUI);
	}

	private void handleRowSelection(List<Object> selectedElements) {

		if(selectedElements.isEmpty()) {
			UpdateNotifierUI.update(getDisplay(), IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LIST_VARIABLE, selectedElements.toArray());
		} else if(Feature.class.isInstance(selectedElements.get(0))) {
			ArrayList<Feature> features = new ArrayList<>();
			for(Object element : selectedElements) {
				if(Feature.class.isInstance(element)) {
					features.add((Feature)element);
				}
			}
			UpdateNotifierUI.update(getDisplay(), IChemClipseEvents.TOPIC_PCA_UPDATE_HIGHLIGHT_LIST_VARIABLE, selectedElements.toArray());
		}
	}

	private void createToolbarInfo(Composite parent) {

		InformationUI informationUI = new InformationUI(parent, SWT.NONE);
		informationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		toolbarInfo.set(informationUI);
	}

	private Button createButtonCleanVariables(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Remove unused variables.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CLEAR, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openConfirm(e.display.getActiveShell(), "Variables", "Remove all unused variables?")) {
					ProcessorPCA processorPCA = new ProcessorPCA();
					processorPCA.cleanUnusedVariables(evaluationPCA, new NullProgressMonitor());
					updateInput(true);
				}
			}
		});
		//
		return button;
	}

	private Button createButtonReset(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Reset the feature table.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RESET, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateInput(false);
			}
		});
		//
		return button;
	}

	private Button createButtonExport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Export the feature table.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXPORT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(featureDataMatrix != null) {
					FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
					fileDialog.setOverwrite(true);
					fileDialog.setText("Export");
					fileDialog.setFilterExtensions(new String[]{FeatureDataMatrixIO.FILTER_EXTENSION});
					fileDialog.setFilterNames(new String[]{FeatureDataMatrixIO.FILTER_NAME});
					fileDialog.setFileName(FeatureDataMatrixIO.FILE_NAME);
					fileDialog.setFilterPath(PreferenceSupplier.getPathExportFile());
					String path = fileDialog.open();
					if(path != null) {
						try {
							PreferenceSupplier.setPathExportFile(fileDialog.getFilterPath());
							File file = new File(path);
							FeatureDataMatrixIO.write(file, featureDataMatrix);
							SystemEditor.open(file);
						} catch(FileNotFoundException e1) {
							MessageDialog.openWarning(e.display.getActiveShell(), "Export", "The feature data matrix file couldn't be found.");
						}
					}
				}
			}
		});
		//
		return button;
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePage.class), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				applySettings();
			}
		});
	}

	private void applySettings() {

		updateInput(false);
	}

	private void updateInput(boolean updateFeatures) {

		if(updateFeatures) {
			featureDataMatrix = evaluationPCA != null ? evaluationPCA.getFeatureDataMatrix() : null;
		}
		updateInput();
	}

	private void updateInput() {

		listControl.get().clear();
		toolbarInfo.get().setText("Loading...");
		//
		getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {

				updateWidgets();
				updateInfoLabel();
			}
		});
	}

	private void updateWidgets() {

		FeatureListUI featureListUI = listControl.get();
		featureListUI.clearColumns();
		featureListUI.setInput(featureDataMatrix);
	}

	private void updateInfoLabel() {

		String searchText = toolbarSearch.get().getSearchText();
		int count = listControl.get().getTable().getItemCount();
		String marker = "".equals(searchText) ? "" : "*";
		String search = "".equals(searchText) ? "" : " (" + searchText + ")";
		toolbarInfo.get().setText("Features" + marker + ": " + count + search);
	}
}
