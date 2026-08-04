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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.cas.CasSupport;
import org.eclipse.chemclipse.model.exceptions.ReferenceMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.msd.model.core.DuplicateDetection;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.MassSpectra;
import org.eclipse.chemclipse.msd.model.implementation.RegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.support.LibrarySupport;
import org.eclipse.chemclipse.msd.swt.ui.components.massspectrum.MassSpectrumListUI;
import org.eclipse.chemclipse.msd.swt.ui.preferences.PreferencePage;
import org.eclipse.chemclipse.msd.swt.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.swt.ui.preferences.PreferencePageSystem;
import org.eclipse.chemclipse.ux.extension.msd.ui.dialogs.LibraryEntryEditDialog;
import org.eclipse.chemclipse.ux.extension.msd.ui.dialogs.MassSpectrumMergeDialog;
import org.eclipse.chemclipse.ux.extension.msd.ui.help.HelpContext;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.runnables.LibraryImportRunnable;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
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
import org.eclipse.swt.widgets.Combo;
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
	private AtomicReference<Button> buttonCleanUp = new AtomicReference<>();
	private AtomicReference<Button> buttonDeleteEntries = new AtomicReference<>();
	private AtomicReference<Button> buttonSelectionMergeEntries = new AtomicReference<>();
	private AtomicReference<Button> buttonAutoMergeEntries = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboDuplicateDetection = new AtomicReference<>();
	private AtomicReference<Button> buttonShowDuplicatesOnly = new AtomicReference<>();
	private AtomicReference<MassSpectrumListUI> massSpectrumListControl = new AtomicReference<>();

	private IMassSpectra massSpectra = null;
	private DuplicateDetection duplicateDetection = DuplicateDetection.NONE;
	private Map<String, List<IScanMSD>> duplicateGroupMap = new HashMap<>();

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
		comboDuplicateDetection.get().setSelection(new StructuredSelection(duplicateDetection));
		updateWidgets();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridDataStatus = new GridData(GridData.FILL_HORIZONTAL);
		gridDataStatus.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridDataStatus);
		composite.setLayout(new GridLayout(12, false));

		createButtonToggleToolbarInfo(composite);
		createButtonToggleToolbarSearch(composite);
		createButtonLibraryImport(composite);
		createButtonAddEntry(composite);
		createComboDuplicateDetection(composite);
		createButtonShowDuplicatesOnly(composite);
		createButtonSelectionMergeEntries(composite);
		createButtonAutoMergeEntries(composite);
		createButtonCleanUp(composite);
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
		button.setToolTipText("Import a library");
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
						updateDuplicates();
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
							updateDuplicates();
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

	private void createComboDuplicateDetection(Composite parent) {

		ComboViewer comboViewer = new ComboViewer(parent, SWT.READ_ONLY);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof DuplicateDetection detection) {
					return detection.label();
				}
				return null;
			}
		});

		Combo combo = comboViewer.getCombo();
		combo.setToolTipText("Select duplicate detection mode.");
		comboViewer.setInput(DuplicateDetection.values());
		comboViewer.addSelectionChangedListener(event -> {

			if(comboViewer.getStructuredSelection().getFirstElement() instanceof DuplicateDetection selection) {
				duplicateDetection = selection;
				updateDuplicates();
				setInput();
			}

		});

		comboDuplicateDetection.set(comboViewer);
	}

	private void createButtonShowDuplicatesOnly(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setToolTipText("Show duplicates only.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_BOOKMARK, IApplicationImageProvider.SIZE_16x16));
		button.setSelection(false);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				setInput();
			}
		});

		buttonShowDuplicatesOnly.set(button);
	}

	private void updateDuplicates() {

		/*
		 * Clear the current map.
		 * Validate that mass spectra exist.
		 */
		duplicateGroupMap.clear();
		if(massSpectra == null) {
			return;
		}
		/*
		 * Validate that duplicates shall be detected.
		 */
		if(duplicateDetection == DuplicateDetection.NONE) {
			return;
		}
		/*
		 * Run calculation
		 */
		for(IScanMSD scan : massSpectra.getList()) {
			ILibraryInformation libraryInformation = getLibraryInformation(scan);
			if(libraryInformation != null) {
				String key;
				switch(duplicateDetection) {
					case NAME:
						key = libraryInformation.getName();
						break;
					case CAS:
						key = libraryInformation.getCasNumber();
						break;
					default:
						key = null;
						break;
				}
				/*
				 * Check
				 */
				if(key != null && !key.isBlank()) {
					duplicateGroupMap.computeIfAbsent(key, k -> new ArrayList<>()).add(scan);
				}
			}
		}
		/*
		 * Clean and update list control.
		 */
		duplicateGroupMap.entrySet().removeIf(e -> e.getValue().size() < 2);
		massSpectrumListControl.get().updateDuplicateHints(duplicateDetection, duplicateGroupMap.keySet());
	}

	private ILibraryInformation getLibraryInformation(IScanMSD scan) {

		if(scan instanceof IRegularLibraryMassSpectrum libraryMassSpectrum) {
			return libraryMassSpectrum.getLibraryInformation();
		}

		return IIdentificationTarget.getLibraryInformation(scan);
	}

	private void createButtonAutoMergeEntries(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Auto-merge duplicate library entries.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXECUTE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(massSpectra != null) {
					if(!duplicateGroupMap.isEmpty()) {
						List<List<IScanMSD>> duplicateGroups = new ArrayList<>(duplicateGroupMap.values());
						MassSpectrumMergeDialog dialog = new MassSpectrumMergeDialog(getShell(), duplicateGroups, true);
						if(dialog.open() == Window.OK) {
							List<List<IScanMSD>> toMergeGroups = dialog.getGroupsToMerge();
							for(List<IScanMSD> toMerge : toMergeGroups) {
								if(toMerge.size() >= 2) {
									IRegularLibraryMassSpectrum massSpectrumMerged = LibrarySupport.merge(toMerge);
									for(IScanMSD scan : toMerge) {
										massSpectra.removeMassSpectrum(scan);
									}
									massSpectra.addMassSpectrum(massSpectrumMerged);
								}
							}
							if(!toMergeGroups.isEmpty()) {
								setMassSpectraDirty();
								updateDuplicates();
								setInput();
								resetSearch();
							}
						}
					} else {
						MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
						messageBox.setText("No Duplicates");
						messageBox.setMessage("No duplicate library entries found.");
						messageBox.open();
					}
				}
			}
		});

		buttonAutoMergeEntries.set(button);
	}

	private void createButtonSelectionMergeEntries(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
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
									IRegularLibraryMassSpectrum merged = LibrarySupport.merge(toMerge);
									for(IScanMSD scan : toMerge) {
										massSpectra.removeMassSpectrum(scan);
									}
									massSpectra.addMassSpectrum(merged);
									setMassSpectraDirty();
									updateDuplicates();
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
		buttonSelectionMergeEntries.set(button);
	}

	private void createButtonCleanUp(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Clean up the mass spectrum library.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CALCULATE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(massSpectra != null) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					messageBox.setText("Clean up library?");
					messageBox.setMessage("Would you like to clean up the mass spectrum library?");
					if(messageBox.open() == SWT.YES) {
						/*
						 * Clean Up Library
						 */
						int removed = 0;
						for(IScanMSD scan : massSpectra.getList()) {
							ILibraryInformation libraryInformation = getLibraryInformation(scan);
							if(libraryInformation != null) {
								List<String> invalid = new ArrayList<>();
								for(String cas : libraryInformation.getCasNumbers()) {
									if(!CasSupport.isValid(cas)) {
										invalid.add(cas);
									}
								}
								for(String cas : invalid) {
									libraryInformation.deleteCasNumber(cas);
								}
								removed += invalid.size();
							}
						}
						setMassSpectraDirty();
						setInput();
						/*
						 * Show details of clean up operation.
						 */
						MessageBox resultBox = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
						resultBox.setText("Clean up library");
						resultBox.setMessage("Removed CAS numbers: " + removed);
						resultBox.open();
					}
				}
			}
		});

		buttonCleanUp.set(button);
	}

	private void createButtonDeleteEntries(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
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
									updateDuplicates();
								}
							}
							setInput();
							resetSearch();
						}
					}
				}
			}
		});

		buttonDeleteEntries.set(button);
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
			updateWidgets();
		});
		/*
		 * Edit Dialog
		 */
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
								updateDuplicates();
							}
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

	private void updateWidgets() {

		int selection = massSpectrumListControl.get().getTable().getSelectionCount();
		buttonSelectionMergeEntries.get().setEnabled(selection >= 2);
		buttonDeleteEntries.get().setEnabled(selection >= 1);
		buttonAutoMergeEntries.get().setEnabled(!duplicateGroupMap.isEmpty());
		buttonShowDuplicatesOnly.get().setEnabled(!duplicateGroupMap.isEmpty());
	}

	private void setMassSpectraDirty() {

		massSpectra.setDirty(true);
		if(dirtyListener != null) {
			dirtyListener.run();
		}
	}

	private void setInput() {

		IMassSpectra input;
		Button button = buttonShowDuplicatesOnly.get();
		if(button != null && button.getSelection() && !duplicateGroupMap.isEmpty()) {
			MassSpectra filtered = new MassSpectra();
			if(massSpectra != null) {
				Set<IScanMSD> duplicateSet = duplicateGroupMap.values().stream().flatMap(List::stream).collect(Collectors.toSet());
				for(IScanMSD scan : massSpectra.getList()) {
					if(duplicateSet.contains(scan)) {
						filtered.addMassSpectrum(scan);
					}
				}
			}
			input = filtered;
		} else {
			input = massSpectra;
		}
		massSpectrumListControl.get().setInput(input);
		massSpectrumListControl.get().updateDuplicateHints(duplicateDetection, duplicateGroupMap.keySet());
		updateWidgets();
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