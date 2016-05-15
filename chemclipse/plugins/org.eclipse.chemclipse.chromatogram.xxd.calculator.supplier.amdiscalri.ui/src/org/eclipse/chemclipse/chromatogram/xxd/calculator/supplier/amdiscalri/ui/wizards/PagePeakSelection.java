/*******************************************************************************
 * Copyright (c) 2016 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.wizards;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.internal.provider.PeakLabelProvider;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.swt.ui.components.chromatogram.SelectedPeakChromatogramUI;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.wizards.AbstractExtendedWizardPage;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class PagePeakSelection extends AbstractExtendedWizardPage {

	private static final Logger logger = Logger.getLogger(PagePeakSelection.class);
	private IRetentionIndexWizardElements wizardElements;
	private String[] titles = {"RT", "S/N", "Peak Area"};
	private int[] bounds = {200, 150, 150};

	public PagePeakSelection(IRetentionIndexWizardElements wizardElements) {
		//
		super(PagePeakSelection.class.getName());
		setTitle("Peak Selection");
		setDescription("Please select the peaks that shall be used.");
		this.wizardElements = wizardElements;
	}

	@Override
	public boolean canFinish() {

		return true;
	}

	@Override
	public void setDefaultValues() {

	}

	@Override
	public void setVisible(boolean visible) {

		super.setVisible(visible);
		if(visible) {
			System.out.println(wizardElements.getFileName());
			validateSelection();
		}
	}

	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		//
		createChromatogramField(composite);
		createPeakTableField(composite);
		//
		validateSelection();
		setControl(composite);
	}

	private void createChromatogramField(Composite composite) {

		Composite parent = new Composite(composite, SWT.NONE);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		parent.setLayout(new FillLayout());
		SelectedPeakChromatogramUI selectedPeakChromatogramUI = new SelectedPeakChromatogramUI(parent, SWT.NONE);
	}

	private void createPeakTableField(Composite composite) {

		ExtendedTableViewer chromatogramTableViewer = new ExtendedTableViewer(composite, SWT.BORDER);
		chromatogramTableViewer.createColumns(titles, bounds);
		Table table = chromatogramTableViewer.getTable();
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 100;
		table.setLayoutData(gridData);
		chromatogramTableViewer.setLabelProvider(new PeakLabelProvider());
		chromatogramTableViewer.setContentProvider(new ListContentProvider());
		chromatogramTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				int index = chromatogramTableViewer.getTable().getSelectionIndex();
				logger.info("TODO index");
			}
		});
	}

	private void validateSelection() {

		String message = null;
		/*
		 * Updates the status
		 */
		updateStatus(message);
	}
}
