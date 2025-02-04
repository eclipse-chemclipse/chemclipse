/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - remember save location
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui.editors;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.pcr.model.core.IPlate;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.events.IPerspectiveAndViewIds;
import org.eclipse.chemclipse.support.settings.UserManagement;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.support.ui.workbench.EditorSupport;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.pcr.ui.Activator;
import org.eclipse.chemclipse.ux.extension.pcr.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.pcr.ui.runnables.PCRImportRunnable;
import org.eclipse.chemclipse.ux.extension.pcr.ui.support.PCRFileSupport;
import org.eclipse.chemclipse.ux.extension.ui.editors.IChemClipseEditor;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class PlateEditorPCR implements IChemClipseEditor {

	private static final Logger logger = Logger.getLogger(PlateEditorPCR.class);
	private final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	//
	public static final String ID = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.plateEditorPCR";
	public static final String CONTRIBUTION_URI = "bundleclass://org.eclipse.chemclipse.ux.extension.pcr.ui/org.eclipse.chemclipse.ux.extension.pcr.ui.editors.PlateEditorPCR";
	public static final String ICON_URI = ApplicationImageFactory.getInstance().getURI(IApplicationImage.IMAGE_PLATE_PCR, IApplicationImageProvider.SIZE_16x16);
	public static final String TOOLTIP = "PCR Editor";
	//
	private final MPart part;
	private final MDirtyable dirtyable;
	private final EModelService modelService;
	private final MApplication application;
	//
	private File plateFile;
	private IPlate plate = null;
	private ExtendedPCRPlateUI extendedPCRPlateUI;
	//
	private final Shell shell;

	@Inject
	public PlateEditorPCR(Composite parent, MPart part, MDirtyable dirtyable, EModelService modelService, MApplication application, Shell shell) {

		this.part = part;
		this.dirtyable = dirtyable;
		this.modelService = modelService;
		this.application = application;
		this.shell = shell;
		//
		initialize(parent);
	}

	@Focus
	public void setFocus() {

		updatePlate();
	}

	@PreDestroy
	protected void preDestroy() {

		unloadPlate();
		//
		if(modelService != null && application != null) {
			MPartStack partStack = (MPartStack)modelService.find(IPerspectiveAndViewIds.EDITOR_PART_STACK_ID, application);
			part.setToBeRendered(false);
			part.setVisible(false);
			DisplayUtils.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {

					partStack.getChildren().remove(part);
				}
			});
		}
	}

	@Persist
	public void save() {

		/*
		 * Save the data is not supported yet.
		 * dirtyable.setDirty(false);
		 */
		System.out.println("Plate File: " + plateFile);
		saveAs();
	}

	private String getFilterPath() {

		String filterPath = preferenceStore.getString(PreferenceSupplier.P_PCR_SAVE_AS_FOLDER);
		if(filterPath.isEmpty()) {
			return UserManagement.getUserHome();
		}
		return filterPath;
	}

	@Override
	public boolean saveAs() {

		boolean saveSuccessful = false;
		if(plate != null) {
			try {
				String path = getFilterPath();
				File file = PCRFileSupport.savePlate(shell, plate, path);
				saveSuccessful = file != null;
				if(saveSuccessful) {
					preferenceStore.setValue(PreferenceSupplier.P_PCR_SAVE_AS_FOLDER, file.getParent());
				}
				dirtyable.setDirty(!saveSuccessful);
			} catch(Exception e) {
				logger.warn(e);
			}
		}
		return saveSuccessful;
	}

	private void initialize(Composite parent) {

		createEditorPages(parent);
		plate = loadPlate();
		extendedPCRPlateUI.update(plate);
	}

	private synchronized IPlate loadPlate() {

		IPlate plate = null;
		Object object = part.getObject();
		if(object instanceof Map<?, ?> map) {
			/*
			 * Map
			 */
			File file = new File((String)map.get(EditorSupport.MAP_FILE));
			boolean batch = (boolean)map.get(EditorSupport.MAP_BATCH);
			plate = loadPlate(file, batch);
		}
		//
		return plate;
	}

	private IPlate loadPlate(File file, boolean batch) {

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		PCRImportRunnable runnable = new PCRImportRunnable(file);
		try {
			/*
			 * No fork, otherwise it might crash when loading the data takes too long.
			 */
			boolean fork = !(batch);
			dialog.run(fork, false, runnable);
		} catch(InvocationTargetException e) {
			logger.warn(e);
		} catch(InterruptedException e) {
			logger.warn(e);
			Thread.currentThread().interrupt();
		}
		//
		plateFile = file;
		return runnable.getPlate();
	}

	private void createEditorPages(Composite parent) {

		createScanPage(parent);
	}

	private void createScanPage(Composite parent) {

		extendedPCRPlateUI = new ExtendedPCRPlateUI(parent, SWT.NONE);
	}

	private void updatePlate() {

		extendedPCRPlateUI.update(plate);
		UpdateNotifierUI.update(Display.getDefault(), IChemClipseEvents.TOPIC_PLATE_PCR_UPDATE_SELECTION, plate);
	}

	private void unloadPlate() {

		List<String> clearTopics = Arrays.asList(IChemClipseEvents.TOPIC_PLATE_PCR_UPDATE_SELECTION, IChemClipseEvents.TOPIC_WELL_PCR_UPDATE_SELECTION);
		UpdateNotifierUI.update(Display.getDefault(), IChemClipseEvents.TOPIC_EDITOR_PCR_CLOSE, clearTopics);
	}
}
