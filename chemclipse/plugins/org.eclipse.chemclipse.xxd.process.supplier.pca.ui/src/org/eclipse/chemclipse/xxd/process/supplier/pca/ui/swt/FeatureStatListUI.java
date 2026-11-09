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

import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.FeatureDataMatrix;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider.FeatureLabelProvider;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider.FeatureListFilter;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider.FeatureStatComparator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider.FeatureStatLabelProvider;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.provider.VisualSelectionFilter;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

public class FeatureStatListUI extends org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer {

	private static final String[] TITLES = FeatureStatLabelProvider.TITLES;
	private static final int[] BOUNDS = FeatureStatLabelProvider.BOUNDS;
	private final ViewerComparator comparator = new FeatureStatComparator();
	private final FeatureListFilter listFilter = new FeatureListFilter();
	private final VisualSelectionFilter selectFilter = new VisualSelectionFilter();
	private final FeatureStatLabelProvider labelProvider = new FeatureStatLabelProvider("#,##0.####"); //$NON-NLS-1$

	private IUpdateListener updateListener = null;

	public FeatureStatListUI(Composite parent, int style) {

		super(parent, style);
		setContentProviders();
		createColumnsDefault();
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public void updateContent() {

		if(updateListener != null) {
			updateListener.update();
		}
	}

	public void clear() {

		setInput(null);
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

	private void createColumnsDefault() {

		createColumns(TITLES, BOUNDS, labelProvider, comparator);
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

		String[] titles = titleList.toArray(new String[titleList.size()]);
		int size = boundList.size();
		int[] bounds = new int[size];
		for(int i = 0; i < size; i++) {
			bounds[i] = boundList.get(i);
		}

		super.setInput(null);
		createColumns(titles, bounds, labelProvider, comparator);
	}

	private void createColumns(String[] titles, int[] bounds, ITableLabelProvider labelProvider, ViewerComparator comparator) {

		createColumns(titles, bounds);
		setColumnStyle();
		setLabelProvider(labelProvider);
		setContentProvider(new ListContentProvider());
		setComparator(comparator);
		setFilters(listFilter, selectFilter);
	}

	private void setColumnStyle() {

		TableColumn[] columns = getTable().getColumns();
		for(TableColumn column : columns) {
			column.setAlignment(SWT.RIGHT);
		}
	}
}
