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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.exceptions.ReferenceMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.IFlavorMarker;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.RegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.support.CombinedNominalMassSpectrumCalculator;
import org.eclipse.chemclipse.msd.model.support.ICombinedMassSpectrumCalculator;
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
import org.eclipse.chemclipse.ux.extension.msd.ui.dialogs.MassSpectrumMergeDialog;
import org.eclipse.chemclipse.ux.extension.msd.ui.help.HelpContext;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.runnables.LibraryImportRunnable;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.StructuredSelection;
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
	private AtomicReference<Button> buttonAddEntry = new AtomicReference<>();
	private AtomicReference<Button> buttonMergeEntries = new AtomicReference<>();
	private AtomicReference<MassSpectrumListUI> massSpectrumListControl = new AtomicReference<>();

	private IMassSpectra massSpectra;
	private Runnable dirtyListener = null;

	public MassSpectrumLibraryUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void update(File massSpectrumFile, IMassSpectra massSpectra) {

		this.massSpectra = massSpectra;
		setInput();
		updateLabel();
	}

	public void setDirtyListener(Runnable listener) {

		this.dirtyListener = listener;
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
		composite.setLayout(new GridLayout(8, false));

		createButtonToggleToolbarInfo(composite);
		createButtonToggleToolbarSearch(composite);
		createButtonLibraryImport(composite);
		createButtonAddEntry(composite);
		createButtonMergeEntries(composite);
		createButtonDeleteEntries(composite);
		createButtonHelp(composite, HelpContext.MASS_SPECTRUM_SEARCH);
		createButtonSettings(composite);
	}

	private void createButtonToggleToolbarInfo(Composite parent) {

		buttonToolbarInfo.set(createButtonToggleToolbar(parent, toolbarInfo, IMAGE_INFO, TOOLTIP_INFO));
	}

	private void createButtonToggleToolbarSearch(Composite parent) {

		buttonToolbarSearch.set(createButtonToggleToolbar(parent, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH));
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
						setMassSpectraDirty();
						setInput();
						resetSearch();
					}
				}
			}
		});
	}

	private void createButtonAddEntry(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Add a new library entry.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(massSpectra != null) {
					RegularLibraryMassSpectrum libraryMassSpectrum = new RegularLibraryMassSpectrum();
					LibraryEntryEditDialog dialog = new LibraryEntryEditDialog(getShell(), libraryMassSpectrum);
					if(dialog.open() == Window.OK) {
						if(!libraryMassSpectrum.getLibraryInformation().getName().isBlank()) {
							massSpectra.addMassSpectrum(libraryMassSpectrum);
							setMassSpectraDirty();
							setInput();
							resetSearch();
							massSpectrumListControl.get().setSelection(new StructuredSelection(libraryMassSpectrum), true);
						}
					}
				}
			}
		});
		buttonAddEntry.set(button);
	}

	private void createButtonMergeEntries(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setEnabled(false);
		button.setToolTipText("Merge the selected library entries.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_MERGE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(massSpectra != null) {
					Table table = massSpectrumListControl.get().getTable();
					TableItem[] tableItems = table.getSelection();
					if(tableItems.length >= 2) {
						List<IScanMSD> selected = new ArrayList<>();
						for(TableItem tableItem : tableItems) {
							Object object = tableItem.getData();
							if(object instanceof IScanMSD scan) {
								selected.add(scan);
							}
						}
						if(selected.size() >= 2) {
							MassSpectrumMergeDialog dialog = new MassSpectrumMergeDialog(getShell(), selected);
							if(dialog.open() == Window.OK) {
								List<IScanMSD> toMerge = dialog.getCheckedMassSpectra();
								if(toMerge.size() >= 2) {
									IRegularLibraryMassSpectrum merged = mergeSelectedMassSpectra(toMerge);
									for(IScanMSD scan : toMerge) {
										massSpectra.removeMassSpectrum(scan);
									}
									massSpectra.addMassSpectrum(merged);
									setMassSpectraDirty();
									setInput();
									resetSearch();
									massSpectrumListControl.get().setSelection(new StructuredSelection(merged), true);
								}
							}
						}
					}
				}
			}
		});
		buttonMergeEntries.set(button);
	}

	private IRegularLibraryMassSpectrum mergeSelectedMassSpectra(List<IScanMSD> selected) {

		ICombinedMassSpectrumCalculator calculator = new CombinedNominalMassSpectrumCalculator();
		for(IScanMSD scan : selected) {
			for(IIon ion : scan.getIons()) {
				calculator.addIon(ion.getIon(), ion.getAbundance());
			}
		}
		ICombinedMassSpectrum combined = calculator.createMassSpectrum(CalculationType.SUM);
		RegularLibraryMassSpectrum merged = new RegularLibraryMassSpectrum();
		merged.addIons(combined.getIons(), false);
		merged.setRetentionTime(selected.get(0).getRetentionTime());
		mergeLibraryInformation(merged.getLibraryInformation(), selected);
		return merged;
	}

	private void mergeLibraryInformation(ILibraryInformation target, List<IScanMSD> selected) {

		IScanMSD first = selected.get(0);
		if(first instanceof IRegularLibraryMassSpectrum firstLibrary) {
			ILibraryInformation source = firstLibrary.getLibraryInformation();
			target.setName(source.getName());
			target.setFormula(source.getFormula());
			target.setSmiles(source.getSmiles());
			target.setInChI(source.getInChI());
			target.setInChIKey(source.getInChIKey());
			target.setMolWeight(source.getMolWeight());
			target.setExactMass(source.getExactMass());
			target.setComments(source.getComments());
			target.setMiscellaneous(source.getMiscellaneous());
			target.setReferenceIdentifier(source.getReferenceIdentifier());
			target.setDatabase(source.getDatabase());
			target.setContributor(source.getContributor());
			target.setCompoundClass(source.getCompoundClass());
			target.setMoleculeStructure(source.getMoleculeStructure());
			for(String cas : source.getCasNumbers()) {
				target.addCasNumber(cas);
			}
			target.getSynonyms().addAll(source.getSynonyms());
			for(IColumnIndexMarker marker : source.getColumnIndexMarkers()) {
				target.add(marker);
			}
			for(IFlavorMarker marker : source.getFlavorMarkers()) {
				target.add(marker);
			}
		}
		for(int i = 1; i < selected.size(); i++) {
			IScanMSD scan = selected.get(i);
			if(scan instanceof IRegularLibraryMassSpectrum library) {
				ILibraryInformation source = library.getLibraryInformation();
				String name = source.getName();
				if(!name.isEmpty() && !name.equals(target.getName())) {
					target.getSynonyms().add(name);
				}
				target.getSynonyms().addAll(source.getSynonyms());
				for(String cas : source.getCasNumbers()) {
					if(!target.getCasNumbers().contains(cas)) {
						target.addCasNumber(cas);
					}
				}
				if(target.getFormula().isEmpty()) {
					target.setFormula(source.getFormula());
				}
				if(target.getSmiles().isEmpty()) {
					target.setSmiles(source.getSmiles());
				}
				if(target.getInChI().isEmpty()) {
					target.setInChI(source.getInChI());
				}
				if(target.getInChIKey().isEmpty()) {
					target.setInChIKey(source.getInChIKey());
				}
				if(target.getMolWeight() == 0) {
					target.setMolWeight(source.getMolWeight());
				}
				if(target.getExactMass() == 0) {
					target.setExactMass(source.getExactMass());
				}
				if(target.getDatabase().isEmpty()) {
					target.setDatabase(source.getDatabase());
				}
				if(target.getContributor().isEmpty()) {
					target.setContributor(source.getContributor());
				}
				if(target.getCompoundClass().isEmpty()) {
					target.setCompoundClass(source.getCompoundClass());
				}
				if(target.getMoleculeStructure().isEmpty()) {
					target.setMoleculeStructure(source.getMoleculeStructure());
				}
				mergeTextField(target.getComments(), source.getComments(), target::setComments);
				mergeTextField(target.getMiscellaneous(), source.getMiscellaneous(), target::setMiscellaneous);
				mergeTextField(target.getReferenceIdentifier(), source.getReferenceIdentifier(), target::setReferenceIdentifier);
				for(IColumnIndexMarker marker : source.getColumnIndexMarkers()) {
					target.add(marker);
				}
				for(IFlavorMarker marker : source.getFlavorMarkers()) {
					target.add(marker);
				}
			}
		}
	}

	private void mergeTextField(String existing, String addition, Consumer<String> setter) {

		if(!addition.isEmpty()) {
			if(existing.isEmpty()) {
				setter.accept(addition);
			} else if(!existing.contains(addition)) {
				setter.accept(existing + "; " + addition);
			}
		}
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
									setMassSpectraDirty();
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
			Button mergeButton = buttonMergeEntries.get();
			if(mergeButton != null) {
				mergeButton.setEnabled(massSpectrumListUI.getTable().getSelectionCount() >= 2);
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
								setMassSpectraDirty();
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

	private void setMassSpectraDirty() {

		massSpectra.setDirty(true);
		if(dirtyListener != null) {
			dirtyListener.run();
		}
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