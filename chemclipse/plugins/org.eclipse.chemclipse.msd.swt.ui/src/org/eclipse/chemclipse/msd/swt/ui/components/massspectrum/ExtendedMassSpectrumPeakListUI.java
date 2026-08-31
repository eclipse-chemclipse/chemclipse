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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.swt.ui.components.massspectrum;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.operations.DeleteMassSpectrumPeaksOperation;
import org.eclipse.chemclipse.rcp.app.undo.UndoContextFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class ExtendedMassSpectrumPeakListUI extends Composite {

	private static final Logger logger = Logger.getLogger(ExtendedMassSpectrumPeakListUI.class);

	private MassSpectrumPeakListUI massSpectrumPeaksListUI;

	private IRegularMassSpectrum regularMassSpectrum;

	public ExtendedMassSpectrumPeakListUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void update(IRegularMassSpectrum regularMassSpectrum) {

		this.regularMassSpectrum = regularMassSpectrum;
		massSpectrumPeaksListUI.update(regularMassSpectrum);
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		createToolbarMain(this);
		massSpectrumPeaksListUI = createPeakTable(this);
	}

	private MassSpectrumPeakListUI createPeakTable(Composite parent) {

		MassSpectrumPeakListUI massSpectrumPeakListUI = new MassSpectrumPeakListUI(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		Table table = massSpectrumPeakListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		return massSpectrumPeakListUI;
	}

	private Composite createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));

		createButtonDelete(composite);
		createButtonDeleteAll(composite);

		return composite;
	}

	private Button createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Delete the selected peaks.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(regularMassSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
					List<IMassSpectrumPeak> peaksToDelete = new ArrayList<>();
					for(Object object : massSpectrumPeaksListUI.getStructuredSelection().toList()) {
						if(object instanceof IMassSpectrumPeak peak) {
							peaksToDelete.add(peak);
						}
					}
					deletePeaks(peaksToDelete, standaloneMassSpectrum);
				}
			}
		});

		return button;
	}

	private Button createButtonDeleteAll(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Delete all peaks.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE_ALL, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(regularMassSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
					deletePeaks(new ArrayList<>(standaloneMassSpectrum.getPeaks()), standaloneMassSpectrum);
				}
			}
		});

		return button;
	}

	private void deletePeaks(List<IMassSpectrumPeak> peaksToDelete, IStandaloneMassSpectrum standaloneMassSpectrum) {

		DeleteMassSpectrumPeaksOperation deletePeaks = new DeleteMassSpectrumPeaksOperation(standaloneMassSpectrum, peaksToDelete);
		deletePeaks.addContext(UndoContextFactory.getUndoContext());
		try {
			OperationHistoryFactory.getOperationHistory().execute(deletePeaks, null, null);
		} catch(ExecutionException e) {
			logger.warn(e);
		}
	}
}
