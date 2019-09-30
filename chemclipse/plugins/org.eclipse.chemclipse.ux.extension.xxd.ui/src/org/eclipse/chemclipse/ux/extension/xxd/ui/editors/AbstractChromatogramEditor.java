/*******************************************************************************
 * Copyright (c) 2018, 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.editors;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.chemclipse.converter.exceptions.FileIsEmptyException;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotReadableException;
import org.eclipse.chemclipse.converter.exceptions.NoChromatogramConverterAvailableException;
import org.eclipse.chemclipse.csd.converter.chromatogram.ChromatogramConverterCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.selection.ChromatogramSelectionCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.methods.IProcessMethod;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.supplier.IChromatogramSelectionProcessSupplier;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.ProcessorFactory;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.settings.UserManagement;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.support.ui.workbench.EditorSupport;
import org.eclipse.chemclipse.support.ui.workbench.PartSupport;
import org.eclipse.chemclipse.ux.extension.ui.editors.IChromatogramEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.editors.ChromatogramFileSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.runnables.ChromatogramImportRunnable;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.AbstractDataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.IDataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.ObjectChangedListener;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.ProcessMethodNotifications;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceConstants;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ChromatogramDataSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors.ExtendedChromatogramUI;
import org.eclipse.chemclipse.wsd.converter.chromatogram.ChromatogramConverterWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.selection.ChromatogramSelectionWSD;
import org.eclipse.chemclipse.xxd.process.support.ProcessTypeSupport;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("rawtypes")
public abstract class AbstractChromatogramEditor extends AbstractDataUpdateSupport implements IChromatogramEditor, IDataUpdateSupport {

	public static final String ICON_URI = "platform:/plugin/org.eclipse.chemclipse.rcp.ui.icons/icons/16x16/chromatogram.gif";
	public static final String TOOLTIP = "Chromatogram Editor";
	//
	private static final Logger logger = Logger.getLogger(AbstractChromatogramEditor.class);
	//
	private DataType dataType;
	private MPart part;
	private MDirtyable dirtyable;
	//
	private IEventBroker eventBroker;
	//
	private File chromatogramFile = null;
	private ExtendedChromatogramUI extendedChromatogramUI;
	//
	private IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	private Shell shell;
	private ObjectChangedListener<Object> updateMenuListener = new ObjectChangedListener<Object>() {

		@Override
		public void objectChanged(ChangeType type, Object newObject, Object oldObject) {

			if(extendedChromatogramUI != null) {
				extendedChromatogramUI.updateMenu();
			}
		}
	};

	public AbstractChromatogramEditor(DataType dataType, Composite parent, MPart part, MDirtyable dirtyable, ProcessorFactory filterFactory, Shell shell) {
		super(part);
		//
		this.dataType = dataType;
		this.part = part;
		this.dirtyable = dirtyable;
		this.eventBroker = Activator.getDefault().getEventBroker();
		this.shell = shell;
		//
		initialize(parent);
	}

	@Override
	public void registerEvents() {

		registerEvent(IChemClipseEvents.TOPIC_CHROMATOGRAM_MSD_UPDATE_CHROMATOGRAM_SELECTION, IChemClipseEvents.PROPERTY_CHROMATOGRAM_SELECTION);
		registerEvent(IChemClipseEvents.TOPIC_CHROMATOGRAM_CSD_UPDATE_CHROMATOGRAM_SELECTION, IChemClipseEvents.PROPERTY_CHROMATOGRAM_SELECTION);
		registerEvent(IChemClipseEvents.TOPIC_CHROMATOGRAM_WSD_UPDATE_CHROMATOGRAM_SELECTION, IChemClipseEvents.PROPERTY_CHROMATOGRAM_SELECTION);
		registerEvent(IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION, IChemClipseEvents.PROPERTY_SELECTED_SCAN);
		registerEvent(IChemClipseEvents.TOPIC_SCAN_XXD_UNLOAD_SELECTION, IChemClipseEvents.PROPERTY_SELECTED_SCAN);
		registerEvent(IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION, IChemClipseEvents.PROPERTY_SELECTED_PEAK);
		registerEvent(IChemClipseEvents.TOPIC_PEAK_XXD_UNLOAD_SELECTION, IChemClipseEvents.PROPERTY_SELECTED_PEAK);
	}

	@Focus
	public void onFocus() {

		if(shell != null) {
			extendedChromatogramUI.fireUpdate(shell.getDisplay());
		}
	}

	@Override
	public void updateObjects(List<Object> objects, String topic) {

		/*
		 * 0 => because only one property was used to register the event.
		 */
		if(objects.size() == 1) {
			Object object = objects.get(0);
			if(object instanceof IChromatogramSelection) {
				IChromatogramSelection chromatogramSelection = (IChromatogramSelection)object;
				if(extendedChromatogramUI.isActiveChromatogramSelection(chromatogramSelection)) {
					extendedChromatogramUI.update();
				}
			} else if(object instanceof IScan) {
				extendedChromatogramUI.updateSelectedScan();
			} else if(object instanceof IPeak) {
				extendedChromatogramUI.updateSelectedPeak();
			}
		}
	}

	@PostConstruct
	private void postConstruct(ProcessMethodNotifications notification) {

		notification.addObjectChangedListener(updateMenuListener);
	}

	@PreDestroy
	private void preDestroy(ProcessMethodNotifications notifications, PartSupport partSupport) {

		notifications.removeObjectChangedListener(updateMenuListener);
		if(eventBroker != null) {
			DisplayUtils.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {

					eventBroker.send(IChemClipseEvents.TOPIC_SCAN_XXD_UNLOAD_SELECTION, null);
				}
			});
			//
			DisplayUtils.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {

					eventBroker.send(IChemClipseEvents.TOPIC_PEAK_XXD_UNLOAD_SELECTION, null);
				}
			});
			//
			DisplayUtils.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {

					eventBroker.send(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UNLOAD_SELECTION, null);
				}
			});
		}
		partSupport.closePart(part);
	}

	@Persist
	public void save() {

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		IRunnableWithProgress runnable = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

				try {
					monitor.beginTask("Save Chromatogram", IProgressMonitor.UNKNOWN);
					try {
						saveChromatogram(monitor);
					} catch(NoChromatogramConverterAvailableException e) {
						throw new InvocationTargetException(e);
					}
				} finally {
					monitor.done();
				}
			}
		};
		/*
		 * Run the export
		 */
		try {
			dialog.run(true, false, runnable);
		} catch(InvocationTargetException e) {
			saveAs();
		} catch(InterruptedException e) {
			logger.warn(e);
		}
	}

	@Override
	public boolean saveAs() {

		boolean saveSuccessful = false;
		IChromatogramSelection chromatogramSelection = extendedChromatogramUI.getChromatogramSelection();
		if(chromatogramSelection != null) {
			try {
				/*
				 * Get the path of the loaded data file.
				 */
				String filterPath = getFilterPath();
				saveSuccessful = ChromatogramFileSupport.saveChromatogram(shell, chromatogramSelection.getChromatogram(), dataType, filterPath);
				dirtyable.setDirty(!saveSuccessful);
			} catch(Exception e) {
				logger.warn(e);
			}
		}
		return saveSuccessful;
	}

	@Override
	public IChromatogramSelection getChromatogramSelection() {

		return extendedChromatogramUI.getChromatogramSelection();
	}

	private String getFilterPath() {

		String filterPath = preferenceStore.getString(PreferenceConstants.P_CHROMATOGRAM_SAVE_AS_FOLDER);
		if("".equals(filterPath)) {
			if(chromatogramFile != null) {
				if(chromatogramFile.isDirectory()) {
					filterPath = chromatogramFile.getAbsolutePath();
				} else {
					filterPath = chromatogramFile.getParentFile().getAbsolutePath();
				}
			} else {
				filterPath = UserManagement.getUserHome();
			}
		}
		return filterPath;
	}

	private synchronized void initialize(Composite parent) {

		IChromatogramSelection chromatogramSelection = loadChromatogram();
		createEditorPages(parent);
		extendedChromatogramUI.updateChromatogramSelection(chromatogramSelection);
		processChromatogram(chromatogramSelection);
		//
		if(chromatogramSelection != null) {
			part.setLabel(ChromatogramDataSupport.getChromatogramEditorLabel(chromatogramSelection));
			dirtyable.setDirty(true);
			chromatogramSelection.update(true);
		}
	}

	private void processChromatogram(IChromatogramSelection chromatogramSelection) {

		File file = new File(preferenceStore.getString(PreferenceConstants.P_CHROMATOGRAM_LOAD_PROCESS_METHOD));
		if(chromatogramSelection != null && file != null) {
			try {
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
				dialog.run(false, false, new IRunnableWithProgress() {

					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

						IProcessMethod processMethod = Adapters.adapt(file, IProcessMethod.class);
						if(processMethod != null) {
							ProcessTypeSupport processTypeSupport = new ProcessTypeSupport();
							IChromatogramSelectionProcessSupplier.applyProcessMethod(chromatogramSelection, processMethod, ProcessExecutionContext.create(processTypeSupport, new ProcessingInfo<>(), monitor));
						}
					}
				});
			} catch(InvocationTargetException e) {
				logger.warn(e);
			} catch(InterruptedException e) {
				logger.warn(e);
			}
		}
	}

	private synchronized IChromatogramSelection loadChromatogram() {

		IChromatogramSelection chromatogramSelection = null;
		try {
			Object object = part.getObject();
			if(object instanceof Map) {
				/*
				 * Map
				 */
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>)object;
				File file = new File((String)map.get(EditorSupport.MAP_FILE));
				boolean batch = (boolean)map.get(EditorSupport.MAP_BATCH);
				chromatogramSelection = loadChromatogramSelection(file, batch);
			} else {
				/*
				 * Already available.
				 */
				if(object instanceof IChromatogramMSD) {
					IChromatogramMSD chromatogram = (IChromatogramMSD)object;
					chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
				} else if(object instanceof IChromatogramCSD) {
					IChromatogramCSD chromatogram = (IChromatogramCSD)object;
					chromatogramSelection = new ChromatogramSelectionCSD(chromatogram);
				} else if(object instanceof IChromatogramWSD) {
					IChromatogramWSD chromatogram = (IChromatogramWSD)object;
					chromatogramSelection = new ChromatogramSelectionWSD(chromatogram);
				}
				chromatogramFile = null;
			}
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		//
		return chromatogramSelection;
	}

	private synchronized IChromatogramSelection loadChromatogramSelection(File file, boolean batch) throws FileNotFoundException, NoChromatogramConverterAvailableException, FileIsNotReadableException, FileIsEmptyException, ChromatogramIsNullException {

		IChromatogramSelection chromatogramSelection = null;
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		ChromatogramImportRunnable runnable = new ChromatogramImportRunnable(file, dataType);
		try {
			/*
			 * No fork, otherwise it might crash when loading a chromatogram takes too long.
			 */
			boolean fork = batch ? false : true;
			dialog.run(fork, false, runnable);
			chromatogramSelection = runnable.getChromatogramSelection();
			chromatogramFile = file;
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		//
		return chromatogramSelection;
	}

	private void saveChromatogram(IProgressMonitor monitor) throws NoChromatogramConverterAvailableException {

		IChromatogramSelection chromatogramSelection = extendedChromatogramUI.getChromatogramSelection();
		if(chromatogramSelection != null && shell != null) {
			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			String converterId = chromatogram.getConverterId();
			if(converterId != null && !converterId.equals("") && chromatogramFile != null) {
				monitor.subTask("Save Chromatogram");
				//
				IProcessingInfo processingInfo = null;
				if(chromatogram instanceof IChromatogramMSD) {
					IChromatogramMSD chromatogramMSD = (IChromatogramMSD)chromatogram;
					processingInfo = ChromatogramConverterMSD.getInstance().convert(chromatogramFile, chromatogramMSD, converterId, monitor);
				} else if(chromatogram instanceof IChromatogramCSD) {
					IChromatogramCSD chromatogramCSD = (IChromatogramCSD)chromatogram;
					processingInfo = ChromatogramConverterCSD.getInstance().convert(chromatogramFile, chromatogramCSD, converterId, monitor);
				} else if(chromatogram instanceof IChromatogramWSD) {
					IChromatogramWSD chromatogramWSD = (IChromatogramWSD)chromatogram;
					processingInfo = ChromatogramConverterWSD.getInstance().convert(chromatogramFile, chromatogramWSD, converterId, monitor);
				}
				//
				if(processingInfo != null) {
					Object object = processingInfo.getProcessingResult();
					if(object instanceof File) {
						dirtyable.setDirty(false);
					}
				} else {
					throw new NoChromatogramConverterAvailableException();
				}
			} else {
				throw new NoChromatogramConverterAvailableException();
			}
		}
	}

	private void createEditorPages(Composite parent) {

		createChromatogramPage(parent);
	}

	private void createChromatogramPage(Composite parent) {

		extendedChromatogramUI = new ExtendedChromatogramUI(parent, SWT.BORDER, Activator.getDefault().getPreferenceStore(), new ProcessTypeSupport(), eventBroker);
	}
}
