/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - update data explorer handling
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.swt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.eclipse.chemclipse.converter.methods.MethodConverter;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.locations.UserLocation;
import org.eclipse.chemclipse.model.locations.UserLocations;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.processing.converter.ISupplierFileIdentifier;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.swt.ui.preferences.PreferencePageSystem;
import org.eclipse.chemclipse.ux.extension.ui.l10n.Messages;
import org.eclipse.chemclipse.ux.extension.ui.model.DataExplorerTreeSettings;
import org.eclipse.chemclipse.ux.extension.ui.preferences.PreferencePage;
import org.eclipse.chemclipse.ux.extension.ui.preferences.PreferencePageUserLocations;
import org.eclipse.chemclipse.ux.extension.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.ui.provider.DataExplorerContentProvider;
import org.eclipse.chemclipse.ux.extension.ui.provider.ISupplierFileEditorSupport;
import org.eclipse.chemclipse.ux.extension.ui.provider.LazyFileExplorerContentProvider;
import org.eclipse.chemclipse.xxd.process.files.SupplierFileIdentifierCache;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class MultiDataExplorerTreeUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(MultiDataExplorerTreeUI.class);
	private static final String TAB_KEY_SUFFIX = "selectedTab"; //$NON-NLS-1$
	//
	private AtomicReference<TabFolder> tabFolderControl = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboViewerUserLocations = new AtomicReference<>();
	private DataExplorerTreeUI[] dataExplorerTreeUIs;
	//
	private DataExplorerTreeSettings dataExplorerTreeSettings;
	private UserLocations userLocations = new UserLocations();
	private final SupplierFileIdentifierCache supplierFileIdentifierCache = new SupplierFileIdentifierCache(LazyFileExplorerContentProvider.MAX_CACHE_SIZE);

	public MultiDataExplorerTreeUI(Composite parent, int style, DataExplorerTreeSettings dataExplorerTreeSettings) {

		super(parent, style);
		this.dataExplorerTreeSettings = dataExplorerTreeSettings;
		createControl();
	}

	public DataExplorerTreeSettings getDataExplorerTreeSettings() {

		return dataExplorerTreeSettings;
	}

	@Override
	public boolean setFocus() {

		TabFolder tabFolder = tabFolderControl.get();
		tabFolder.setFocus();
		for(TabItem item : tabFolder.getSelection()) {
			item.getControl().setFocus();
		}
		//
		return true;
	}

	public Control getControl() {

		return tabFolderControl.get();
	}

	public void setSupplierFileIdentifier(Collection<? extends ISupplierFileIdentifier> supplierFileEditorSupportList) {

		supplierFileIdentifierCache.setIdentifier(supplierFileEditorSupportList);
		for(DataExplorerTreeUI dataExplorerTreeUI : dataExplorerTreeUIs) {
			dataExplorerTreeUI.getTreeViewer().refresh();
		}
	}

	public void expandLastDirectoryPath() {

		IPreferenceStore preferenceStore = dataExplorerTreeSettings.getPreferenceStore();
		for(DataExplorerTreeUI dataExplorerTreeUI : dataExplorerTreeUIs) {
			String preferenceKey = getPreferenceKey(dataExplorerTreeUI.getRoot());
			dataExplorerTreeUI.expandLastDirectoryPath(preferenceStore, preferenceKey);
		}
	}

	public void saveLastDirectoryPath() {

		IPreferenceStore preferenceStore = dataExplorerTreeSettings.getPreferenceStore();
		for(DataExplorerTreeUI dataExplorerTreeUI : dataExplorerTreeUIs) {
			dataExplorerTreeUI.saveLastDirectoryPath(preferenceStore, getPreferenceKey(dataExplorerTreeUI.getRoot()));
		}
		//
		int index = tabFolderControl.get().getSelectionIndex();
		preferenceStore.setValue(getSelectedTabPreferenceKey(), index);
		if(preferenceStore.needsSaving()) {
			if(preferenceStore instanceof IPersistentPreferenceStore persistentPreferenceStore) {
				try {
					persistentPreferenceStore.save();
				} catch(IOException e) {
					logger.warn(Messages.storingPreferencesFailed);
				}
			}
		}
	}

	protected Function<File, Map<ISupplierFileIdentifier, Collection<ISupplier>>> getIdentifierSupplier() {

		return supplierFileIdentifierCache;
	}

	protected void handleDoubleClick(File file) {

		openEditor(file);
	}

	protected void handleSelection(File[] files, DataExplorerTreeUI treeUI) {

		if(files.length > 0) {
			openOverview(files[0], treeUI);
		}
	}

	protected String getSelectedTabPreferenceKey() {

		return getPreferenceKey(DataExplorerTreeRoot.USER_LOCATION) + TAB_KEY_SUFFIX;
	}

	protected String getUserLocationPreferenceKey() {

		return PreferenceSupplier.P_USER_LOCATION_PATH;
	}

	protected String getPreferenceKey(DataExplorerTreeRoot root) {

		return root.getPreferenceKeyDefaultPath();
	}

	protected void setSupplierFileEditorSupport() {

	}

	protected List<Class<? extends IPreferencePage>> addPreferencePages() {

		return Collections.emptyList();
	}

	private void createControl() {

		setLayout(new FillLayout());
		createDataExplorerTabFolder(this);
		initialize();
	}

	private void initialize() {

		updateUserLocations();
	}

	private void createDataExplorerTabFolder(Composite parent) {

		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		//
		DataExplorerTreeRoot[] dataExplorerTreeRoots = dataExplorerTreeSettings.getDataExplorerTreeRoots();
		dataExplorerTreeUIs = new DataExplorerTreeUI[dataExplorerTreeRoots.length];
		for(int i = 0; i < dataExplorerTreeRoots.length; i++) {
			dataExplorerTreeUIs[i] = createDataExplorerTreeUI(tabFolder, dataExplorerTreeRoots[i]);
		}
		//
		tabFolderControl.set(tabFolder);
	}

	private DataExplorerTreeUI createDataExplorerTreeUI(TabFolder tabFolder, DataExplorerTreeRoot dataExplorerTreeRoot) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(dataExplorerTreeRoot.toString());
		//
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(1, true));
		//
		createToolbarMain(composite, dataExplorerTreeRoot);
		DataExplorerTreeUI dataExplorerTreeUI = createDataExplorerTreeUI(composite, tabFolder, tabItem, dataExplorerTreeRoot);
		createButtonLocation(composite, dataExplorerTreeUI);
		createButtonBatchOpen(composite, dataExplorerTreeUI);
		//
		tabItem.setControl(composite);
		return dataExplorerTreeUI;
	}

	private void createToolbarMain(Composite parent, DataExplorerTreeRoot dataExplorerTreeRoot) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(4, false));
		//
		if(dataExplorerTreeRoot == DataExplorerTreeRoot.USER_LOCATION) {
			createComboViewerLocations(composite);
			createButtonDeleteLocation(composite);
		} else {
			createLocationPlaceholder(composite, 2);
		}
		/*
		 * Add custom elements.
		 */
		createResetButton(composite);
		createSettingsButton(composite);
	}

	private void createLocationPlaceholder(Composite parent, int horizontalSpan) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = horizontalSpan;
		label.setLayoutData(gridData);
	}

	private void createResetButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the data explorer.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RESET, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				setSupplierFileEditorSupport();
				expandLastDirectoryPath();
			}
		});
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, getPreferencePages(), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				updateUserLocations();
				setSupplierFileEditorSupport();
			}
		}, true);
	}

	private List<Class<? extends IPreferencePage>> getPreferencePages() {

		List<Class<? extends IPreferencePage>> preferencePages = new ArrayList<>();
		//
		preferencePages.add(PreferencePageSystem.class);
		preferencePages.add(PreferencePage.class);
		preferencePages.add(PreferencePageUserLocations.class);
		preferencePages.addAll(addPreferencePages());
		//
		return preferencePages;
	}

	private void createComboViewerLocations(Composite parent) {

		ComboViewer comboViewer = new EnhancedComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ListContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof UserLocation userLocation) {
					return userLocation.getName();
				}
				return null;
			}
		});
		//
		combo.setToolTipText("Select a user location.");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				switchUserLocation(e.display.getActiveShell());
			}
		});
		//
		comboViewerUserLocations.set(comboViewer);
	}

	private void switchUserLocation(Shell shell) {

		UserLocation userLocation = getUserLocation();
		if(userLocation != null) {
			File directory = new File(userLocation.getPath());
			if(!directory.exists()) {
				MessageDialog.openInformation(shell, "User Location", "The given location doesn't exist.");
			} else if(!directory.isDirectory()) {
				MessageDialog.openInformation(shell, "User Location", "The given location is not a directory.");
			} else {
				DataExplorerTreeUI dataExplorerTreeUI = getUserLocationTreeUI();
				if(dataExplorerTreeUI != null) {
					IPreferenceStore preferenceStore = dataExplorerTreeSettings.getPreferenceStore();
					preferenceStore.setValue(getUserLocationPreferenceKey(), directory.getAbsolutePath());
					dataExplorerTreeUI.updateDirectory(directory);
				}
			}
		}
	}

	private void createButtonDeleteLocation(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Delete the location.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				UserLocation userLocation = getUserLocation();
				if(userLocation != null) {
					if(MessageDialog.openQuestion(e.display.getActiveShell(), "User Selection", "Would you like to delete the user selection?")) {
						userLocations.remove(userLocation);
						PreferenceSupplier.setUserLocations(userLocations.save());
						IPreferenceStore preferenceStore = dataExplorerTreeSettings.getPreferenceStore();
						preferenceStore.setValue(getUserLocationPreferenceKey(), "");
						updateUserLocations();
					}
				}
			}
		});
	}

	private DataExplorerTreeUI getUserLocationTreeUI() {

		for(DataExplorerTreeUI dataExplorerTreeUI : dataExplorerTreeUIs) {
			if(dataExplorerTreeUI.getRoot() == DataExplorerTreeRoot.USER_LOCATION) {
				return dataExplorerTreeUI;
			}
		}
		//
		return null;
	}

	private void addUserLocationButton(Composite parent, DataExplorerTreeUI dataExplorerTreeUI) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText(Messages.selectUserLocation);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_FOLDER_OPENED, IApplicationImageProvider.SIZE_16x16));
		button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				DirectoryDialog directoryDialog = new DirectoryDialog(e.display.getActiveShell(), SWT.READ_ONLY);
				directoryDialog.setText(Messages.selectDirectory);
				String pathname = directoryDialog.open();
				if(pathname != null) {
					File directory = new File(pathname);
					if(directory.exists()) {
						IPreferenceStore preferenceStore = dataExplorerTreeSettings.getPreferenceStore();
						preferenceStore.setValue(getUserLocationPreferenceKey(), directory.getAbsolutePath());
						dataExplorerTreeUI.getTreeViewer().setInput(new File[]{directory});
						//
						String name = directory.getName();
						if(userLocations.get(name) == null) {
							if(MessageDialog.openQuestion(e.display.getActiveShell(), "User Selection", "Would you like to bookmark the selected user selection?")) {
								addUserLocationBookmark(e.display.getActiveShell(), name, directory);
							}
						}
					}
				}
			}
		});
	}

	private DataExplorerTreeUI createDataExplorerTreeUI(Composite parent, TabFolder tabFolder, TabItem tabItem, DataExplorerTreeRoot dataExplorerTreeRoot) {

		DataExplorerTreeUI dataExplorerTreeUI = new DataExplorerTreeUI(parent, dataExplorerTreeRoot, getIdentifierSupplier());
		TreeViewer treeViewer = dataExplorerTreeUI.getTreeViewer();
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				Object[] array = treeViewer.getStructuredSelection().toArray();
				File[] files = new File[array.length];
				for(int i = 0; i < files.length; i++) {
					files[i] = (File)array[i];
				}
				handleSelection(files, dataExplorerTreeUI);
			}
		};
		//
		treeViewer.addSelectionChangedListener(selectionChangedListener);
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				if(dataExplorerTreeUI.getTreeViewer().getStructuredSelection().getFirstElement() instanceof File file) {
					handleDoubleClick(file);
				}
			}
		});
		//
		tabFolder.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				TabItem[] selection = tabFolder.getSelection();
				for(TabItem item : selection) {
					if(item == tabItem) {
						selectionChangedListener.selectionChanged(null);
					}
				}
			}
		});
		//
		createContextMenu(dataExplorerTreeUI);
		//
		return dataExplorerTreeUI;
	}

	private void createContextMenu(DataExplorerTreeUI dataExplorerTreeUI) {

		TreeViewer treeViewer = dataExplorerTreeUI.getTreeViewer();
		MenuManager menuManager = new MenuManager("#ViewerMenu"); //$NON-NLS-1$
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager mgr) {

				/*
				 * Menu Entries
				 */
				Object[] selection = treeViewer.getStructuredSelection().toArray();
				updateFileAndFolders(dataExplorerTreeUI, menuManager);
				selectUserLocationDirectory(menuManager, selection);
				selectMethodDirectory(menuManager, selection);
				selectMethodFile(menuManager, selection);
				openFileAs(menuManager, selection);
				openFiles(dataExplorerTreeUI, menuManager, selection);
			}
		});
		//
		Menu menu = menuManager.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);
	}

	private void createButtonLocation(Composite parent, DataExplorerTreeUI dataExplorerTreeUI) {

		if(dataExplorerTreeUI.getRoot() == DataExplorerTreeRoot.USER_LOCATION) {
			/*
			 * Select a specific user location.
			 */
			addUserLocationButton(parent, dataExplorerTreeUI);
			//
			IPreferenceStore preferenceStore = dataExplorerTreeSettings.getPreferenceStore();
			File directory = new File(preferenceStore.getString(getUserLocationPreferenceKey()));
			if(directory.exists()) {
				dataExplorerTreeUI.updateDirectory(directory);
			}
		}
	}

	private void updateFileAndFolders(DataExplorerTreeUI dataExplorerTreeUI, MenuManager menuManager) {

		TreeViewer treeViewer = dataExplorerTreeUI.getTreeViewer();
		menuManager.add(new Action(Messages.scanForFilesystemUpdates, ApplicationImageFactory.getInstance().getImageDescriptor(IApplicationImage.IMAGE_REFRESH, IApplicationImageProvider.SIZE_16x16)) {

			@Override
			public void run() {

				treeViewer.refresh();
			}
		});
	}

	private void selectUserLocationDirectory(MenuManager menuManager, Object[] selection) {

		if(selection.length >= 1) {
			if(selection[0] instanceof File file) {
				if(file.isDirectory()) {
					menuManager.add(new Action("Bookmark User Location", ApplicationImageFactory.getInstance().getImageDescriptor(IApplicationImage.IMAGE_BOOKMARK, IApplicationImageProvider.SIZE_16x16)) {

						@Override
						public void run() {

							InputDialog inputDialog = new InputDialog(Display.getDefault().getActiveShell(), "User Location", "Bookmark the user location.", file.getName(), new IInputValidator() {

								@Override
								public String isValid(String name) {

									if(name.isBlank()) {
										return "Please select a name.";
									} else if(userLocations.get(name) != null) {
										return "The name exists already.";
									}
									//
									return null;
								}
							});
							/*
							 * User Location
							 */
							if(inputDialog.open() == Window.OK) {
								String name = inputDialog.getValue();
								addUserLocationBookmark(Display.getDefault().getActiveShell(), name, file);
							}
						}
					});
				}
			}
		}
	}

	private void addUserLocationBookmark(Shell shell, String name, File directory) {

		UserLocation userLocation = new UserLocation(name, directory.getAbsolutePath());
		userLocations.add(userLocation);
		PreferenceSupplier.setUserLocations(userLocations.save());
		updateUserLocations(userLocation);
		switchUserLocation(shell);
	}

	private void selectMethodDirectory(MenuManager menuManager, Object[] files) {

		if(files.length >= 1) {
			if(files[0] instanceof File file) {
				if(file.isDirectory()) {
					menuManager.add(new Action(Messages.setAsMethodDirectory, ApplicationImageFactory.getInstance().getImageDescriptor(IApplicationImage.IMAGE_METHOD, IApplicationImageProvider.SIZE_16x16)) {

						@Override
						public void run() {

							MethodConverter.setUserMethodDirectory(file);
							UpdateNotifierUI.update(Display.getDefault(), IChemClipseEvents.TOPIC_METHOD_UPDATE, null);
						}
					});
				}
			}
		}
	}

	private void selectMethodFile(MenuManager menuManager, Object[] files) {

		if(files.length >= 1) {
			if(files[0] instanceof File file) {
				if(file.isFile()) {
					if(file.getName().endsWith(MethodConverter.FILE_EXTENSION)) {
						menuManager.add(new Action(Messages.setAsActiveMethod, ApplicationImageFactory.getInstance().getImageDescriptor(IApplicationImage.IMAGE_METHOD, IApplicationImageProvider.SIZE_16x16)) {

							@Override
							public void run() {

								MethodConverter.setUserMethodFile(file);
								UpdateNotifierUI.update(Display.getDefault(), IChemClipseEvents.TOPIC_METHOD_UPDATE, null);
							}
						});
					}
				}
			}
		}
	}

	private void openFileAs(MenuManager menuManager, Object[] files) {

		Set<ISupplier> supplierSet = new TreeSet<>(new Comparator<ISupplier>() {

			@Override
			public int compare(ISupplier supplier1, ISupplier supplier2) {

				return supplier1.getId().compareTo(supplier2.getId());
			}
		});
		//
		for(Object object : files) {
			if(object instanceof File file) {
				Map<ISupplierFileIdentifier, Collection<ISupplier>> map = getIdentifierSupplier().apply(file);
				if(map == null) {
					continue;
				}
				for(ISupplierFileIdentifier supplierFileIdentifier : map.keySet()) {
					if(supplierFileIdentifier.isMatchContent(file)) {
						Collection<ISupplier> suppliers = map.get(supplierFileIdentifier);
						for(ISupplier supplier : suppliers) {
							if(supplier.isMatchMagicNumber(file) && supplier.isMatchContent(file)) {
								supplierSet.add(supplier);
							}
						}
					}
				}
			}
		}
		//
		for(ISupplier activeFileSupplier : supplierSet) {
			menuManager.add(new Action(NLS.bind(Messages.openAs, activeFileSupplier.getFilterName()), ApplicationImageFactory.getInstance().getImageDescriptor(IApplicationImage.IMAGE_FILE, IApplicationImageProvider.SIZE_16x16)) {

				@Override
				public void run() {

					for(Object object : files) {
						if(object instanceof File file) {
							Map<ISupplierFileIdentifier, Collection<ISupplier>> identifiers = getIdentifierSupplier().apply(file);
							for(Entry<ISupplierFileIdentifier, Collection<ISupplier>> entry : identifiers.entrySet()) {
								ISupplierFileIdentifier identifier = entry.getKey();
								if(identifier instanceof ISupplierFileEditorSupport supplierFileEditorSupport) {
									for(ISupplier supplier : entry.getValue()) {
										if(activeFileSupplier.getId().equals(supplier.getId())) {
											if(openEditorWithSupplier(file, supplierFileEditorSupport, supplier)) {
												return;
											}
										}
									}
								}
							}
						}
					}
				}

				@Override
				public String getToolTipText() {

					return activeFileSupplier.getDescription();
				}
			});
		}
	}

	private void openFiles(DataExplorerTreeUI dataExplorerTreeUI, MenuManager menuManager, Object[] files) {

		if(files.length == 1 && files[0] instanceof File file && file.isDirectory()) {
			menuManager.add(new Action(Messages.openAllContainedMeasurements, ApplicationImageFactory.getInstance().getImageDescriptor(IApplicationImage.IMAGE_FOLDER, IApplicationImageProvider.SIZE_16x16)) {

				@Override
				public void run() {

					openRecursively((File)files[0], dataExplorerTreeUI);
				}
			});
		}
	}

	private boolean openRecursively(File file, DataExplorerTreeUI dataExplorerTreeUI) {

		boolean opened = false;
		File[] listFiles = file.listFiles();
		if(listFiles != null) {
			for(File f : listFiles) {
				opened |= openEditor(f);
			}
			if(!opened) {
				for(File f : listFiles) {
					if(f.isDirectory()) {
						/*
						 * recurse into sub-directory...
						 */
						opened |= openRecursively(f, dataExplorerTreeUI);
					}
				}
			}
		}
		//
		return opened;
	}

	private void createButtonBatchOpen(Composite parent, DataExplorerTreeUI dataExplorerTreeUI) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText(Messages.openSelectedMeasurements);
		button.setToolTipText(Messages.tryToOpenAllSelectedFiles);
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IMPORT, IApplicationImageProvider.SIZE_16x16));
		button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IStructuredSelection structuredSelection = dataExplorerTreeUI.getTreeViewer().getStructuredSelection();
				Iterator<?> iterator = structuredSelection.iterator();
				while(iterator.hasNext()) {
					Object object = iterator.next();
					if(object instanceof File file) {
						e.display.asyncExec(new Runnable() {

							@Override
							public void run() {

								openEditor(file);
							}
						});
					}
				}
			}
		});
	}

	private void openOverview(File file, DataExplorerTreeUI dataExplorerTreeUI) {

		if(file != null) {
			DataExplorerContentProvider contentProvider = (DataExplorerContentProvider)dataExplorerTreeUI.getTreeViewer().getContentProvider();
			/*
			 * Update the directories content, until there is
			 * actual no way to monitor the file system outside
			 * of the workbench without using operating system
			 * specific function via e.g. JNI.
			 */
			if(file.isDirectory()) {
				contentProvider.refresh(file);
			}
			//
			Collection<ISupplierFileIdentifier> identifiers = getIdentifierSupplier().apply(file).keySet();
			for(ISupplierFileIdentifier identifier : identifiers) {
				if(!identifier.isMatchMagicNumber(file) || !identifier.isMatchContent(file)) {
					continue;
				}
				if(identifier instanceof ISupplierFileEditorSupport fileEditorSupport) {
					fileEditorSupport.openOverview(file);
					return;
				}
			}
			//
			UpdateNotifier.update(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_NONE, null);
		}
	}

	private boolean openEditor(File file) {

		boolean success = false;
		if(file != null) {
			boolean openFirstDataMatchOnly = PreferenceSupplier.isOpenFirstDataMatchOnly();
			Map<ISupplierFileIdentifier, Collection<ISupplier>> identifiers = getIdentifierSupplier().apply(file);
			for(Entry<ISupplierFileIdentifier, Collection<ISupplier>> entry : identifiers.entrySet()) {
				ISupplierFileIdentifier identifier = entry.getKey();
				if(identifier instanceof ISupplierFileEditorSupport supplierFileEditorSupport) {
					for(ISupplier converter : entry.getValue()) {
						if(converter.isMatchMagicNumber(file) && converter.isMatchContent(file)) {
							success = success | openEditorWithSupplier(file, supplierFileEditorSupport, converter);
							if(success && openFirstDataMatchOnly) {
								return true;
							}
						}
					}
				}
			}
		}
		return success;
	}

	private boolean openEditorWithSupplier(File file, ISupplierFileEditorSupport identifier, ISupplier converter) {

		saveLastDirectoryPath();
		return identifier.openEditor(file, converter);
	}

	private UserLocation getUserLocation() {

		Object object = comboViewerUserLocations.get().getStructuredSelection().getFirstElement();
		if(object instanceof UserLocation userLocation) {
			return userLocation;
		}
		//
		return null;
	}

	private void updateUserLocations() {

		updateUserLocations(getUserLocation());
	}

	private void updateUserLocations(UserLocation userLocation) {

		userLocations.clear();
		userLocations.load(PreferenceSupplier.getUserLocations());
		/*
		 * Update combo viewer and selection.
		 */
		ComboViewer comboViewer = comboViewerUserLocations.get();
		List<UserLocation> userLocationsSorted = new ArrayList<>(userLocations.values());
		Collections.sort(userLocationsSorted, (l1, l2) -> l1.getName().compareTo(l2.getName()));
		comboViewer.setInput(userLocationsSorted);
		if(userLocation != null && userLocationsSorted.contains(userLocation)) {
			comboViewer.setSelection(new StructuredSelection(userLocation));
		}
	}
}