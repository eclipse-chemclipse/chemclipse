/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.exceptions.ReferenceMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.swt.ui.components.massspectrum.MassSpectrumListUI;
import org.eclipse.chemclipse.msd.swt.ui.preferences.PreferencePage;
import org.eclipse.chemclipse.msd.swt.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.swt.ui.preferences.PreferencePageSystem;
import org.eclipse.chemclipse.ux.extension.msd.ui.dialogs.LibraryEntryEditDialog;
import org.eclipse.chemclipse.ux.extension.msd.ui.help.HelpContext;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.runnables.LibraryImportRunnable;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class MassSpectrumLibraryUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(MassSpectrumLibraryUI.class);

	private AtomicReference<Button> buttonToolbarInfo = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfo = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarSearch = new AtomicReference<>();
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private AtomicReference<MassSpectrumListUI> massSpectrumListControl = new AtomicReference<>();

	private IMassSpectra massSpectra;

	public MassSpectrumLibraryUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void update(File massSpectrumFile, IMassSpectra massSpectra) {

		this.massSpectra = massSpectra;
		setInput();
		updateLabel();
	}

	public MassSpectrumListUI getMassSpectrumListUI() {

		return massSpectrumListControl.get();
	}

	private void createControl() {

		setLayout(new FillLayout());

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createToolbarMain(composite);
		createToolbarInfo(composite);
		createToolbarSearch(composite);
		createLibraryTable(composite);

		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarInfo, buttonToolbarInfo.get(), IApplicationImage.IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarSearch, buttonToolbarSearch.get(), IMAGE_SEARCH, TOOLTIP_SEARCH, false);
		massSpectrumListControl.get().setEditEnabled(false);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridDataStatus = new GridData(GridData.FILL_HORIZONTAL);
		gridDataStatus.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridDataStatus);
		composite.setLayout(new GridLayout(6, false));

		createButtonToggleToolbarInfo(composite);
		createButtonToggleToolbarSearch(composite);
		createButtonLibraryImport(composite);
		createButtonDeleteEntries(composite);
		createButtonHelp(composite, HelpContext.MASS_SPECTRUM_SEARCH);
		createButtonSettings(composite);
	}

	private void createButtonToggleToolbarInfo(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarInfo, IMAGE_INFO, TOOLTIP_INFO);
		buttonToolbarInfo.set(button);
	}

	private void createButtonToggleToolbarSearch(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH);
		buttonToolbarSearch.set(button);
	}

	private void createButtonSettings(Composite parent) {

		createSettingsButton(parent, Arrays.asList( //
				PreferencePageSystem.class, //
				PreferencePage.class //
		), display -> applySettings());
	}

	private void createToolbarInfo(Composite parent) {

		InformationUI informationUI = new InformationUI(parent, SWT.NONE);
		informationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		toolbarInfo.set(informationUI);
	}

	private void createToolbarSearch(Composite parent) {

		SearchSupportUI searchSupportUI = new SearchSupportUI(parent, SWT.NONE);
		searchSupportUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		searchSupportUI.setSearchListener((searchText, caseSensitive) -> {

			massSpectrumListControl.get().setSearchText(searchText, caseSensitive);
			updateLabel();
		});

		toolbarSearch.set(searchSupportUI);
	}

	private void createButtonLibraryImport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Select a library");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IMPORT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(getShell(), SWT.READ_ONLY);
				fileDialog.setText("Select Library");
				fileDialog.setFilterExtensions(new String[]{"*.msl", "*.*"});
				fileDialog.setFilterNames(new String[]{"AMDIS (*.msl)", "All Files"});
				fileDialog.setFilterPath(PreferenceSupplier.getPathMassSpectrumLibraries());
				String pathname = fileDialog.open();
				if(pathname != null) {
					/*
					 * Convert
					 */
					PreferenceSupplier.setPathMassSpectrumLibraries(fileDialog.getFilterPath());
					File file = new File(pathname);
					LibraryImportRunnable runnable = new LibraryImportRunnable(file);
					ProgressMonitorDialog monitor = new ProgressMonitorDialog(getShell());
					try {
						/*
						 * Use true, true ... instead of false, true ... if the progress bar
						 * should be shown in action.
						 */
						monitor.run(true, true, runnable);
					} catch(InvocationTargetException ex) {
						logger.warn(ex);
						logger.warn(ex.getCause());
					} catch(InterruptedException ex) {
						logger.warn(ex);
						Thread.currentThread().interrupt();
					}
					/*
					 * Merge
					 */
					IMassSpectra massSpectraImport = runnable.getMassSpectra();
					if(massSpectraImport != null) {
						massSpectra.addMassSpectra(massSpectraImport.getList());
						massSpectra.setDirty(true);
						setInput();
						resetSearch();
					}
				}
			}
		});
	}

	private void createButtonDeleteEntries(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setEnabled(false);
		button.setToolTipText("Delete the library entry.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(massSpectra != null) {
					Table table = massSpectrumListControl.get().getTable();
					int index = table.getSelectionIndex();
					if(index >= 0) {
						MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
						messageBox.setText("Delete library entries?");
						messageBox.setMessage("Would you like to delete the library entries?");
						if(messageBox.open() == SWT.OK) {
							TableItem[] tableItems = table.getSelection();
							for(TableItem tableItem : tableItems) {
								Object object = tableItem.getData();
								if(object instanceof IScanMSD massSpectrum) {
									massSpectra.removeMassSpectrum(massSpectrum);
									massSpectra.setDirty(true);
								}
							}
							setInput();
							resetSearch();
						}
					}
				}
			}
		});
	}

	private void createLibraryTable(Composite parent) {

		MassSpectrumListUI massSpectrumListUI = new MassSpectrumListUI(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL);
		massSpectrumListUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		massSpectrumListUI.addSelectionChangedListener(event -> {

			if(event.getStructuredSelection().getFirstElement() instanceof IScanMSD massSpectrum) {
				/*
				 * Fire an update if an identified scan has been selected.
				 */
				IIdentificationTarget identificationTarget = getIdentificationTarget(massSpectrum);
				massSpectrum.getTargets().add(identificationTarget);
				UpdateNotifier.update(massSpectrum);
				UpdateNotifier.update(identificationTarget);
				/*
				 * It's important to set the focus here.
				 * Other views are activated and the focus is set there. But when trying to press "DEL",
				 * the focus would be on the other views. Hence, it needs to be set back to this list.
				 */
				massSpectrumListUI.getTable().setFocus();
			}
		});
		massSpectrumListUI.getTable().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				Table table = massSpectrumListUI.getTable();
				int index = table.getSelectionIndex();
				if(index >= 0) {
					Object data = table.getItem(index).getData();
					if(data instanceof IRegularLibraryMassSpectrum libraryMassSpectrum) {
						LibraryEntryEditDialog dialog = new LibraryEntryEditDialog(getShell(), libraryMassSpectrum);
						if(dialog.open() == Window.OK) {
							if(massSpectra != null) {
								massSpectra.setDirty(true);
							}
							massSpectrumListUI.updateDuplicates();
						}
					}
				}
			}
		});

		massSpectrumListControl.set(massSpectrumListUI);
	}

	private IIdentificationTarget getIdentificationTarget(IScanMSD scanMSD) {

		IIdentificationTarget identificationTarget = null;

		ILibraryInformation libraryInformation = null;
		if(scanMSD instanceof IRegularLibraryMassSpectrum libraryMassSpectrum) {
			libraryInformation = libraryMassSpectrum.getLibraryInformation();
			if(libraryInformation != null) {
				try {
					identificationTarget = new IdentificationTarget(libraryInformation, ComparisonResult.COMPARISON_RESULT_BEST_MATCH);
				} catch(ReferenceMustNotBeNullException e) {
					logger.warn(e);
				}
			}
		}

		return identificationTarget;
	}

	private void setInput() {

		massSpectrumListControl.get().setInput(massSpectra);
	}

	private void resetSearch() {

		massSpectrumListControl.get().setSearchText("", true);
		updateLabel();
	}

	private void updateLabel() {

		String filterInformation = "[" + toolbarSearch.get().getSearchText() + "]";
		toolbarInfo.get().setText("Mass Spectra: " + (massSpectra != null ? massSpectra.size() : 0) + " " + filterInformation);
	}

	private void applySettings() {

		updateLabel();
	}
}