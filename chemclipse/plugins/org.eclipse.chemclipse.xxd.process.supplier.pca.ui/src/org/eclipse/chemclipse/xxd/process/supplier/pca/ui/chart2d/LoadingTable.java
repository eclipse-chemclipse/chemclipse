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
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support.LoadingRow;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class LoadingTable extends Composite {

	private TableViewer tableViewer;
	private EvaluationPCA evaluationPCA;
	private int numberOfComponents = 0;
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.000000"); //$NON-NLS-1$

	public LoadingTable(Composite parent, int style) {

		super(parent, style);
		setLayout(new GridLayout(1, true));
		createTable();
	}

	public void setInput(EvaluationPCA evaluationPCA) {

		this.evaluationPCA = evaluationPCA;
		updateTable();
	}

	public TableViewer getTableViewer() {

		return tableViewer;
	}

	private void createTable() {

		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewer.setContentProvider(new ArrayContentProvider());
	}

	private void updateTable() {

		// Clear existing columns
		Table table = tableViewer.getTable();
		while(table.getColumnCount() > 0) {
			table.getColumn(0).dispose();
		}

		if(evaluationPCA == null) {
			tableViewer.setInput(null);
			return;
		}

		IResultsMVA results = evaluationPCA.getResults();
		if(results == null) {
			tableViewer.setInput(null);
			return;
		}

		numberOfComponents = results.getExplainedVariances().length;

		createVariableNameColumn();

		for(int i = 0; i < numberOfComponents; i++) {
			createPCColumn(i);
		}

		List<LoadingRow> rows = createLoadingRows();
		tableViewer.setInput(rows);

		for(int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}
	}

	private void createVariableNameColumn() {

		TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(StatisticsMessages.variable);
		column.setWidth(200);
		column.setResizable(true);
		column.setMoveable(false);

		viewerColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof LoadingRow) {
					return ((LoadingRow)element).getVariableName();
				}
				return ""; //$NON-NLS-1$
			}
		});

		// Make sortable alphabetically
		column.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				tableViewer.setComparator(new ViewerComparator() {

					@Override
					public int compare(org.eclipse.jface.viewers.Viewer viewer, Object e1, Object e2) {

						LoadingRow r1 = (LoadingRow)e1;
						LoadingRow r2 = (LoadingRow)e2;

						int direction = getSortDirection(column);
						return direction * r1.getVariableName().compareTo(r2.getVariableName());
					}
				});
				updateSortDirection(column);
			}
		});
	}

	private void createPCColumn(final int pcIndex) {

		TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();

		double explainedVariance = 0.0;
		if(evaluationPCA != null && evaluationPCA.getResults() != null) {
			double[] variances = evaluationPCA.getResults().getExplainedVariances();
			if(pcIndex < variances.length) {
				explainedVariance = variances[pcIndex] * 100;
			}
		}

		column.setText(String.format("PC%d (%.1f%%)", pcIndex + 1, explainedVariance)); //$NON-NLS-1$
		column.setWidth(120);
		column.setResizable(true);
		column.setMoveable(true);

		viewerColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof LoadingRow) {
					double[] loadings = ((LoadingRow)element).getLoadings();
					if(pcIndex < loadings.length) {
						return DECIMAL_FORMAT.format(loadings[pcIndex]);
					}
				}
				return ""; //$NON-NLS-1$
			}
		});

		// Make sortable numerically
		column.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				tableViewer.setComparator(new ViewerComparator() {

					@Override
					public int compare(org.eclipse.jface.viewers.Viewer viewer, Object e1, Object e2) {

						LoadingRow r1 = (LoadingRow)e1;
						LoadingRow r2 = (LoadingRow)e2;
						double[] loadings1 = r1.getLoadings();
						double[] loadings2 = r2.getLoadings();

						if(pcIndex >= loadings1.length || pcIndex >= loadings2.length) {
							return 0;
						}

						int direction = getSortDirection(column);
						return direction * Double.compare(loadings1[pcIndex], loadings2[pcIndex]);
					}
				});
				updateSortDirection(column);
			}
		});
	}

	private List<LoadingRow> createLoadingRows() {

		List<LoadingRow> rows = new ArrayList<>();

		if(evaluationPCA == null) {
			return rows;
		}

		IResultsMVA results = evaluationPCA.getResults();
		if(results == null) {
			return rows;
		}

		List<IVariable> variables = results.getExtractedVariables();
		List<double[]> loadingVectors = results.getLoadingVectors();

		if(variables == null || loadingVectors == null) {
			return rows;
		}

		for(int i = 0; i < variables.size(); i++) {
			IVariable variable = variables.get(i);
			String variableName = variable.getValue();

			// Extract loadings for this variable across all PCs
			double[] loadingsForVariable = new double[loadingVectors.size()];
			for(int pc = 0; pc < loadingVectors.size(); pc++) {
				double[] loadingVector = loadingVectors.get(pc);
				if(i < loadingVector.length) {
					loadingsForVariable[pc] = loadingVector[i];
				}
			}

			rows.add(new LoadingRow(variableName, loadingsForVariable));
		}

		return rows;
	}

	private int getSortDirection(TableColumn column) {

		Table table = tableViewer.getTable();
		if(column == table.getSortColumn()) {
			return table.getSortDirection() == SWT.UP ? -1 : 1;
		}
		return 1;
	}

	private void updateSortDirection(TableColumn column) {

		Table table = tableViewer.getTable();
		if(column == table.getSortColumn()) {
			int direction = table.getSortDirection() == SWT.UP ? SWT.DOWN : SWT.UP;
			table.setSortDirection(direction);
		} else {
			table.setSortColumn(column);
			table.setSortDirection(SWT.UP);
		}
		tableViewer.refresh();
	}
}
