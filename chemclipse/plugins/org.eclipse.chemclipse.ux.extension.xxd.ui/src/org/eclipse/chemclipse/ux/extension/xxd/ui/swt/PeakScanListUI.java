/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 * Christoph Läubrich - add support for name editing, improve classifier support
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.ITargetSupplier;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.targets.TargetSupport;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.PeakScanListEditingSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.PeakScanListFilter;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.PeakScanListLabelProvider;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.PeakScanListTableComparator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ChromatogramDataSupport;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.Composite;

public class PeakScanListUI extends ExtendedTableViewer {

	private static final String[] titles = PeakScanListLabelProvider.TITLES;
	private static final int[] bounds = PeakScanListLabelProvider.BOUNDS;

	private final PeakScanListLabelProvider labelProvider = new PeakScanListLabelProvider();
	private final PeakScanListTableComparator tableComparator = new PeakScanListTableComparator();
	private final PeakScanListFilter listFilter = new PeakScanListFilter();

	private List<ITargetSupplier> targetSuppliers = new ArrayList<>();

	public PeakScanListUI(Composite parent, int style) {

		super(parent, style);
		createColumns();
	}

	public void setInput(IChromatogramSelection chromatogramSelection, boolean showPeaks, boolean showPeaksInRange, boolean showScans, boolean showScansInRange) {

		if(chromatogramSelection != null) {
			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			double chromatogramPeakArea = chromatogram.getPeakIntegratedArea();
			labelProvider.setChromatogramPeakArea(chromatogramPeakArea);
			tableComparator.setChromatogramPeakArea(chromatogramPeakArea);

			targetSuppliers.clear();
			/*
			 * Peaks
			 */
			if(showPeaks) {
				targetSuppliers.addAll(ChromatogramDataSupport.getPeaks(chromatogramSelection, showPeaksInRange));
			}
			/*
			 * Scans
			 */
			if(showScans) {
				targetSuppliers.addAll(ChromatogramDataSupport.getIdentifiedScans(chromatogramSelection, showScansInRange));
			}

			super.setInput(targetSuppliers);
		} else {
			clear();
		}
	}

	public void setInput(IPeaksMSD peaks) {

		labelProvider.setChromatogramPeakArea(0);
		tableComparator.setChromatogramPeakArea(0);
		super.setInput(peaks.getPeaks());
	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		listFilter.setSearchText(searchText, caseSensitive);
		refresh();
	}

	public void clear() {

		super.setInput(null);
	}

	private void createColumns() {

		createColumns(titles, bounds);
		setLabelProvider(labelProvider);
		setContentProvider(new ListContentProvider());
		setComparator(tableComparator);
		setFilters(listFilter);
		setEditingSupport();
		setColorProviderDuplicateTargets();
	}

	private void setEditingSupport() {

		List<TableViewerColumn> tableViewerColumns = getTableViewerColumns();
		for(int i = 0; i < tableViewerColumns.size(); i++) {
			TableViewerColumn tableViewerColumn = tableViewerColumns.get(i);
			String label = tableViewerColumn.getColumn().getText();
			if(label.equals(PeakScanListLabelProvider.ACTIVE_FOR_ANALYSIS)) {
				tableViewerColumn.setEditingSupport(new PeakScanListEditingSupport(this, label));
			}
		}
	}

	private void setColorProviderDuplicateTargets() {

		List<TableViewerColumn> tableViewerColumns = getTableViewerColumns();
		TableViewerColumn tableViewerColumn = tableViewerColumns.get(PeakScanListLabelProvider.INDEX_BEST_TARGET);
		if(tableViewerColumn != null) {

			tableViewerColumn.setLabelProvider(new StyledCellLabelProvider() {

				@Override
				public void update(ViewerCell cell) {

					super.update(cell);
					if(cell != null) {
						Object element = cell.getElement();
						if(element instanceof IPeak peak) {
							cell.setText(TargetSupport.getBestTargetLibraryField(peak));
							if(hasDuplicateTarget(peak)) {
								cell.setBackground(Colors.LIGHT_YELLOW);
								cell.setForeground(Colors.BLACK);
							}
						} else if(element instanceof IScan scan) {
							cell.setText(TargetSupport.getBestTargetLibraryField(scan));
							if(hasDuplicateTarget(scan)) {
								cell.setBackground(Colors.LIGHT_YELLOW);
								cell.setForeground(Colors.BLACK);
							}
						}
					}
				}

			});
		}
	}

	private boolean hasDuplicateTarget(ITargetSupplier comparisonTargetSupplier) {

		IIdentificationTarget comparisonTarget = TargetSupport.getBestIdentificationTarget(comparisonTargetSupplier);
		if(comparisonTarget == null || comparisonTarget.getLibraryInformation().getName().isEmpty()) {
			return false;
		}

		for(ITargetSupplier targetSupplier : targetSuppliers) {
			if(comparisonTargetSupplier == targetSupplier) {
				continue;
			}

			IIdentificationTarget identificationTarget = TargetSupport.getBestIdentificationTarget(targetSupplier);
			if(identificationTarget == null) {
				continue;
			}

			if(identificationTarget.getLibraryInformation().getName().equals(comparisonTarget.getLibraryInformation().getName())) {
				return true;
			}
		}
		return false;
	}
}
