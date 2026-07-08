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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.database.DatabaseConverter;
import org.eclipse.chemclipse.msd.converter.database.DatabaseConverterSupport;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.msd.model.serializer.ScanDeserializerMSD;
import org.eclipse.chemclipse.msd.model.serializer.ScanSerializerMSD;
import org.eclipse.chemclipse.msd.model.support.MassSpectrumIO;
import org.eclipse.chemclipse.msd.swt.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.ux.extension.msd.ui.dialogs.MassSpectrumEditDialog;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.runnables.LibraryImportRunnable;
import org.eclipse.chemclipse.ux.extension.ui.methods.IChangeListener;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

public class MassSpectrumEditor extends Composite implements IChangeListener {

	private static final Logger logger = Logger.getLogger(MassSpectrumEditor.class);

	private AtomicReference<Button> buttonAdd = new AtomicReference<>();
	private AtomicReference<Button> buttonEdit = new AtomicReference<>();
	private AtomicReference<Button> buttonDelete = new AtomicReference<>();
	private AtomicReference<Button> buttonClear = new AtomicReference<>();
	private AtomicReference<MassSpectrumIonListUI> listControl = new AtomicReference<>();
	private AtomicReference<MassSpectrumChartCentroid> chartControl = new AtomicReference<>();

	private List<Button> buttons = new ArrayList<>();
	private List<Listener> listeners = new ArrayList<>();

	private IScanMSD massSpectrum = new ScanMSD();

	public MassSpectrumEditor(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	public IScanMSD getMassSpectrum() {

		return massSpectrum;
	}

	public void load(String text) {

		ScanMSD scanMSD = new ScanMSD();
		ScanDeserializerMSD.load(scanMSD, text);
		update(scanMSD);
	}

	public String save() {

		if(massSpectrum != null) {
			return ScanSerializerMSD.save(massSpectrum);
		} else {
			return "";
		}
	}

	public void update(IScanMSD massSpectrum) {

		this.massSpectrum = massSpectrum;
		chartControl.get().update(massSpectrum);
		if(massSpectrum != null) {
			listControl.get().setInput(massSpectrum.getIons());
		} else {
			listControl.get().clear();
		}
		/*
		 * Updates
		 */
		updateButtons();
		for(Listener listener : listeners) {
			listener.handleEvent(new Event());
		}
	}

	@Override
	public void addChangeListener(Listener listener) {

		for(Button button : buttons) {
			button.addListener(SWT.Selection, listener);
			button.addListener(SWT.KeyUp, listener);
			button.addListener(SWT.MouseUp, listener);
			button.addListener(SWT.MouseDoubleClick, listener);
		}
	}

	private void initialize() {

		setLayout(new GridLayout(1, true));
		setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

		createToolbarMain(this);
		createDataSection(this);
	}

	private void createDataSection(Composite parent) {

		SashForm sashForm = new SashForm(this, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		createChart(sashForm);
		createTable(sashForm);

		sashForm.setWeights(60, 40);
	}

	private void createChart(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new FillLayout());
		chartControl.set(new MassSpectrumChartCentroid(composite, SWT.NONE));
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(6, false));

		createButtonClipboard(composite);
		createButtonImport(composite);
		createButtonAdd(composite);
		createButtonEdit(composite);
		createButtonDelete(composite);
		createButtonClear(composite);
	}

	private void createButtonClipboard(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Paste mass spectrum from clipboard.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_COPY_CLIPBOARD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				pasteFromClipboard();
			}
		});

		buttons.add(button);
	}

	private void pasteFromClipboard() {

		Clipboard clipboard = new Clipboard(getDisplay());
		try {
			String text = (String)clipboard.getContents(TextTransfer.getInstance());
			if(text != null && !text.isEmpty()) {
				IScanMSD scanMSDImport = MassSpectrumIO.getMassSpectrum(text);
				if(scanMSDImport != null) {
					massSpectrum.removeAllIons();
					massSpectrum.addIons(scanMSDImport.getIons(), true);
					updateTableAndChart();
				}
			}
		} finally {
			clipboard.dispose();
		}
	}

	private void createButtonImport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Import a mass spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IMPORT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				importMassSpectrum();
			}
		});

		buttons.add(button);
	}

	private void importMassSpectrum() {

		FileDialog fileDialog = new FileDialog(getShell(), SWT.READ_ONLY);
		fileDialog.setText("Select Mass Spectrum");
		try {
			DatabaseConverterSupport converterSupport = DatabaseConverter.getDatabaseConverterSupport();
			fileDialog.setFilterExtensions(converterSupport.getFilterExtensions());
			fileDialog.setFilterNames(converterSupport.getFilterNames());
		} catch(NoConverterAvailableException e) {
			fileDialog.setFilterExtensions(new String[]{"*.*"});
			fileDialog.setFilterNames(new String[]{"All Files"});
		}

		fileDialog.setFilterPath(PreferenceSupplier.getPathMassSpectrumLibraries());
		String pathname = fileDialog.open();
		if(pathname != null) {
			PreferenceSupplier.setPathMassSpectrumLibraries(fileDialog.getFilterPath());
			File file = new File(pathname);
			LibraryImportRunnable runnable = new LibraryImportRunnable(file);
			ProgressMonitorDialog monitor = new ProgressMonitorDialog(getShell());
			try {
				monitor.run(true, true, runnable);
			} catch(InvocationTargetException ex) {
				logger.warn(ex);
				logger.warn(ex.getCause());
			} catch(InterruptedException ex) {
				logger.warn(ex);
				Thread.currentThread().interrupt();
			}
			/*
			 * Import
			 */
			IMassSpectra massSpectraImport = runnable.getMassSpectra();
			if(massSpectraImport != null) {
				if(massSpectrum == null) {
					massSpectrum = new ScanMSD();
				}
				for(IScanMSD scanMSDImport : massSpectraImport.getList()) {
					massSpectrum.addIons(scanMSDImport.getIons(), true);
				}
				updateTableAndChart();
			}
		}
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				addIon();
			}
		});

		buttons.add(button);
		buttonAdd.set(button);
	}

	private void createButtonEdit(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setEnabled(false);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				editIon();
			}
		});

		buttons.add(button);
		buttonEdit.set(button);
	}

	private void createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setEnabled(false);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteIons();
			}
		});

		buttons.add(button);
		buttonDelete.set(button);
	}

	private void createButtonClear(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setEnabled(false);
		button.setToolTipText("Remove all ions.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CLEAR, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				clearIons();
			}
		});

		buttons.add(button);
		buttonClear.set(button);
	}

	private void createTable(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new FillLayout());

		MassSpectrumIonListUI listUI = new MassSpectrumIonListUI(composite, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		Table table = listUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateButtons();
			}
		});

		listControl.set(listUI);
	}

	private void addIon() {

		if(massSpectrum != null) {
			MassSpectrumEditDialog dialog = new MassSpectrumEditDialog(getShell(), null);
			if(dialog.open() == Window.OK) {
				IIon ion = dialog.getIon();
				if(ion != null) {
					massSpectrum.addIon(ion);
					updateTableAndChart();
				}
			}
		}
	}

	private void editIon() {

		if(massSpectrum != null) {
			IStructuredSelection selection = listControl.get().getStructuredSelection();
			if(selection.size() == 1 && selection.getFirstElement() instanceof IIon existing) {
				MassSpectrumEditDialog dialog = new MassSpectrumEditDialog(getShell(), existing);
				if(dialog.open() == Window.OK) {
					IIon updated = dialog.getIon();
					if(updated != null) {
						massSpectrum.removeIon(existing);
						massSpectrum.addIon(updated);
						updateTableAndChart();
					}
				}
			}
		}
	}

	private void deleteIons() {

		if(massSpectrum != null) {
			IStructuredSelection selection = listControl.get().getStructuredSelection();
			for(Object object : selection.toList()) {
				if(object instanceof IIon ion) {
					massSpectrum.removeIon(ion);
				}
			}
			updateTableAndChart();
		}
	}

	private void clearIons() {

		if(massSpectrum != null) {
			massSpectrum.removeAllIons();
			updateTableAndChart();
		}
	}

	private void updateTableAndChart() {

		listControl.get().setInput(massSpectrum.getIons());
		chartControl.get().update(massSpectrum);
		updateButtons();
	}

	private void updateButtons() {

		int selectionCount = listControl.get().getTable().getSelectionCount();
		int itemCount = listControl.get().getTable().getItemCount();
		buttonEdit.get().setEnabled(selectionCount == 1);
		buttonDelete.get().setEnabled(selectionCount > 0);
		buttonClear.get().setEnabled(itemCount > 0);
	}
}