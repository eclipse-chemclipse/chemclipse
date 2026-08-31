/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.swt.ui.components.peak;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.swt.ui.internal.provider.PeakCheckBoxEditingSupport;
import org.eclipse.chemclipse.msd.swt.ui.internal.provider.PeakListContentProvider;
import org.eclipse.chemclipse.msd.swt.ui.internal.provider.PeakListLabelProvider;
import org.eclipse.chemclipse.msd.swt.ui.internal.provider.PeakListTableComparator;
import org.eclipse.chemclipse.msd.swt.ui.support.DatabaseFileSupport;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class PeakListUI {

	private static final Logger logger = Logger.getLogger(PeakListUI.class);

	private IChromatogramSelection chromatogramSelection;

	private ExtendedTableViewer tableViewer;
	private Label labelSelectedPeak;
	private Label labelPeaks;

	private static final String PEAK_IS_ACTIVE_FOR_ANALYSIS = "Active for Analysis";
	private final String[] titles = {PEAK_IS_ACTIVE_FOR_ANALYSIS, "RT (min)", "RI", "Area", "Start RT", "Stop RT", "Width", "Scan# at Peak Maximum", "S/N", "Leading", "Tailing", "Model Description", "Suggested Components", "Name"};
	private final int[] bounds = {30, 100, 60, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};

	public PeakListUI(Composite parent) {

		initialize(parent);
	}

	public ExtendedTableViewer getTableViewer() {

		return tableViewer;
	}

	private void initialize(Composite parent) {

		parent.setLayout(new FillLayout());

		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		layout.makeColumnsEqualWidth = true;
		layout.numColumns = 1;
		composite.setLayout(layout);

		createButtons(composite);
		createTable(composite);
		createInfos(composite);
	}

	private void createButtons(Composite composite) {

		Composite compositeButtons = new Composite(composite, SWT.NONE);
		GridData gridDataComposite = new GridData(GridData.FILL_HORIZONTAL);
		gridDataComposite.horizontalAlignment = SWT.END;
		compositeButtons.setLayoutData(gridDataComposite);
		compositeButtons.setLayout(new GridLayout(3, false));

		createUncheckAllButton(compositeButtons);
		createCheckAllButton(compositeButtons);
		createSaveButton(compositeButtons);
	}

	private void createUncheckAllButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Uncheck the selected peaks.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_UNCHECK_ALL, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				setActiveForAnalysis(false);
			}
		});
	}

	private void createCheckAllButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Check the selected peaks.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CHECK_ALL, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				setActiveForAnalysis(true);
			}
		});
	}

	private void createSaveButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Save the peaks");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SAVE_AS, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					List<IPeakMSD> peaks = getPeakList();
					DatabaseFileSupport.saveMassSpectra(peaks);
				} catch(NoConverterAvailableException e1) {
					logger.warn(e1);
				}
			}
		});
	}

	private void createTable(Composite composite) {

		// SWT.VIRTUAL | SWT.FULL_SELECTION
		tableViewer = new ExtendedTableViewer(composite, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewer.createColumns(titles, bounds);
		tableViewer.setContentProvider(new PeakListContentProvider());
		tableViewer.setLabelProvider(new PeakListLabelProvider());
		/*
		 * Sorting the table.
		 */
		PeakListTableComparator peakListTableComparator = new PeakListTableComparator();
		tableViewer.setComparator(peakListTableComparator);
		setEditingSupport();
	}

	private void createInfos(Composite composite) {

		labelSelectedPeak = new Label(composite, SWT.NONE);
		labelSelectedPeak.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		labelPeaks = new Label(composite, SWT.NONE);
		labelPeaks.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private void setActiveForAnalysis(boolean activeForAnalysis) {

		List<IPeakMSD> peaks = getPeakList();
		for(IPeakMSD peak : peaks) {
			peak.setActiveForAnalysis(activeForAnalysis);
		}
		tableViewer.refresh();
		chromatogramSelection.update(true);
	}

	private List<IPeakMSD> getPeakList() {

		List<IPeakMSD> peakList = new ArrayList<>();
		Table table = tableViewer.getTable();
		for(TableItem tableItem : table.getItems()) {
			Object object = tableItem.getData();
			if(object instanceof IPeakMSD peak) {
				peakList.add(peak);
			}
		}
		return peakList;
	}

	private void setEditingSupport() {

		TableViewer tableViewer = getTableViewer();
		List<TableViewerColumn> tableViewerColumns = this.tableViewer.getTableViewerColumns();
		for(int i = 0; i < tableViewerColumns.size(); i++) {
			TableViewerColumn tableViewerColumn = tableViewerColumns.get(i);
			String label = tableViewerColumn.getColumn().getText();
			if(label.equals(PEAK_IS_ACTIVE_FOR_ANALYSIS)) {
				tableViewerColumn.setEditingSupport(new PeakCheckBoxEditingSupport(tableViewer));
			}
		}
	}
}
