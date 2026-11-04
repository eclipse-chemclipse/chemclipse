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
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - add Feature Mode
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.FeatureDataMatrix;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider.FeatureComparator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider.FeatureEditingSupport;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider.FeatureLabelProvider;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider.FeatureListFilter;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider.VisualSelectionFilter;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.FeatureColumnLabels;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.FeatureMode;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

public class FeatureListUI extends ExtendedTableViewer {

	private static final String[] TITLES = FeatureLabelProvider.TITLES;
	private static final int[] BOUNDS = FeatureLabelProvider.BOUNDS;

	private final FeatureLabelProvider labelProviderOriginal = new FeatureLabelProvider("#,##0.##", FeatureMode.ORIGINAL);
	private final FeatureLabelProvider labelProviderPreprocessed = new FeatureLabelProvider("0.00##", FeatureMode.PREPROCESSED);
	private final ViewerComparator comparator = new FeatureComparator();
	private final FeatureListFilter listFilter = new FeatureListFilter();
	private final VisualSelectionFilter selectFilter = new VisualSelectionFilter();

	private IUpdateListener updateListener = null;
	private FeatureMode featureMode = FeatureMode.ORIGINAL;
	private FeatureColumnLabels columnLabels = FeatureColumnLabels.SAMPLENAMES;

	public FeatureListUI(Composite parent, int style) {

		super(parent, style);
		setContentProviders();
		createColumnsDefault();
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	/**
	 * Fires and update if the listener is != null.
	 */
	public void updateContent() {

		if(updateListener != null) {
			updateListener.update();
		}
	}

	public void clear() {

		setInput(null);
	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		listFilter.setSearchText(searchText, caseSensitive);
		refresh();
	}

	public void enableVisualSelection(boolean enable) {

		selectFilter.setMode(enable);
		refresh();
	}

	public void setHighlightedVariables(List<IVariable> highlightedVariables) {

		selectFilter.setHighlightedVariables(highlightedVariables);
		refresh();
	}

	public void setInput(FeatureDataMatrix featureDataMatrix) {

		if(featureDataMatrix != null) {
			createColumnsSpecific(featureDataMatrix);
			super.setInput(featureDataMatrix.getFeatures());
		} else {
			super.setInput(Collections.emptyList());
			createColumnsDefault();
		}
	}

	public void setFeatureMode(FeatureMode featureMode) {

		this.featureMode = featureMode;
	}

	public void setColumnLabels(FeatureColumnLabels labels) {

		this.columnLabels = labels;
	}

	private void createColumnsDefault() {

		if(featureMode.equals(FeatureMode.ORIGINAL)) {
			createColumns(TITLES, BOUNDS, labelProviderOriginal, comparator);
		} else {
			createColumns(TITLES, BOUNDS, labelProviderPreprocessed, comparator);
		}
	}

	private void setContentProviders() {

		setContentProvider(new ListContentProvider());
		setUseHashlookup(true);
		setComparator(null);
	}

	private void createColumnsSpecific(FeatureDataMatrix featureDataMatrix) {

		/*
		 * Labels and bounds
		 */
		String variableName = featureDataMatrix.getVariableName();
		List<String> columnNames;
		if(columnLabels.equals(FeatureColumnLabels.SAMPLENAMES)) {
			columnNames = featureDataMatrix.getSampleNames();
		} else {
			columnNames = featureDataMatrix.getGroupNames();
		}

		List<String> titleList = new ArrayList<>();
		List<Integer> boundList = new ArrayList<>();
		/*
		 * Standards Labels
		 */
		for(int i = 0; i < TITLES.length; i++) {
			/*
			 * Replace the variable name.
			 */
			String title = TITLES[i];
			if(FeatureLabelProvider.VARIABLE.equals(title)) {
				title = variableName;
			}

			titleList.add(title);
			boundList.add(BOUNDS[i]);
		}
		/*
		 * Sample labels
		 */
		for(String sampleName : columnNames) {
			titleList.add(sampleName);
			boundList.add(FeatureLabelProvider.BOUND_SAMPLE);
		}

		String[] titles = titleList.toArray(new String[titleList.size()]);
		int size = boundList.size();
		int[] bounds = new int[size];
		for(int i = 0; i < size; i++) {
			bounds[i] = boundList.get(i);
		}

		super.setInput(null);
		if(featureMode.equals(FeatureMode.ORIGINAL)) {
			createColumns(titles, bounds, labelProviderOriginal, comparator);
		} else {
			createColumns(titles, bounds, labelProviderPreprocessed, comparator);
		}
		/*
		 * Tell what the values are (e.g. "Concentration [mg/L]").
		 */
		String valueLabel = featureDataMatrix.getValueLabel();
		if(!valueLabel.isBlank()) {
			TableColumn[] columns = getTable().getColumns();
			for(int i = TITLES.length; i < columns.length; i++) {
				columns[i].setToolTipText(columns[i].getText() + ": " + valueLabel);
			}
		}
	}

	private void createColumns(String[] titles, int[] bounds, ITableLabelProvider labelProvider, ViewerComparator comparator) {

		createColumns(titles, bounds);
		setColumnStyle();
		setLabelProvider(labelProvider);
		setContentProvider(new ListContentProvider());
		setComparator(comparator);
		setFilters(listFilter, selectFilter);
		setEditingSupport();
	}

	private void setEditingSupport() {

		List<TableViewerColumn> tableViewerColumns = getTableViewerColumns();
		for(int i = 0; i < tableViewerColumns.size(); i++) {
			TableViewerColumn tableViewerColumn = tableViewerColumns.get(i);
			String label = tableViewerColumn.getColumn().getText();
			if(isEditable(label)) {
				tableViewerColumn.setEditingSupport(new FeatureEditingSupport(this, label));
			}
		}
	}

	private boolean isEditable(String label) {

		if(FeatureLabelProvider.USE.equals(label)) {
			return true;
		} else if(FeatureLabelProvider.CLASSIFICATION.equals(label)) {
			return true;
		} else if(FeatureLabelProvider.DESCRIPTION.equals(label)) {
			return true;
		}
		return false;
	}

	private void setColumnStyle() {

		TableColumn[] columns = getTable().getColumns();
		for(TableColumn column : columns) {
			column.setAlignment(getStyle(column.getText()));
		}
	}

	private int getStyle(String label) {

		if(FeatureLabelProvider.CLASSIFICATION.equals(label)) {
			return SWT.LEFT;
		} else if(FeatureLabelProvider.DESCRIPTION.equals(label)) {
			return SWT.LEFT;
		}
		return SWT.RIGHT;
	}
}
