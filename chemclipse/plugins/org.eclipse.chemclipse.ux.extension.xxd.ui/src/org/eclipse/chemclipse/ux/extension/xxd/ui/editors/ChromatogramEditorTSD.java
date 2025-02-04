/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.editors;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IPerspectiveAndViewIds;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.support.ui.workbench.EditorSupport;
import org.eclipse.chemclipse.tsd.model.core.IChromatogramTSD;
import org.eclipse.chemclipse.ux.extension.ui.editors.IChemClipseEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.ux.extension.xxd.ui.runnables.ImportRunnableTSD;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors.ChromatogramHeatmapUI;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class ChromatogramEditorTSD implements IChemClipseEditor {

	private static final Logger logger = Logger.getLogger(ChromatogramEditorTSD.class);
	//
	public static final String ID = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.chromatogramEditorTSD";
	public static final String CONTRIBUTION_URI = "bundleclass://org.eclipse.chemclipse.ux.extension.xxd.ui/org.eclipse.chemclipse.ux.extension.xxd.ui.editors.ChromatogramEditorTSD";
	public static final String ICON_URI = ApplicationImageFactory.getInstance().getURI(IApplicationImage.IMAGE_CHROMATOGRAM_TSD, IApplicationImageProvider.SIZE_16x16);
	public static final String TOOLTIP = ExtensionMessages.chromatogramEditorTSD;
	//
	private final MPart part;
	private final MDirtyable dirtyable;
	private final EModelService modelService;
	private final MApplication application;
	//
	private File chromatogramFile;
	private ChromatogramHeatmapUI chromatogramHeatmapUI;
	//
	private final Shell shell;

	@Inject
	public ChromatogramEditorTSD(Composite parent, MPart part, MDirtyable dirtyable, EModelService modelService, MApplication application, Shell shell) {

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

		chromatogramHeatmapUI.setFocus();
	}

	@PreDestroy
	protected void preDestroy() {

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
		 * TODO
		 */
		System.out.println("TODO Save/Export File: " + chromatogramFile);
	}

	@Override
	public boolean saveAs() {

		/*
		 * TODO
		 */
		dirtyable.setDirty(true);
		return false;
	}

	private void initialize(Composite parent) {

		createEditorPages(parent);
		IChromatogramTSD chromatogramTSD = loadChromatogram();
		chromatogramHeatmapUI.setInput(chromatogramTSD);
	}

	private synchronized IChromatogramTSD loadChromatogram() {

		IChromatogramTSD chromatogram = null;
		Object object = part.getObject();
		if(object instanceof Map<?, ?> map) {
			File file = new File((String)map.get(EditorSupport.MAP_FILE));
			boolean batch = (boolean)map.get(EditorSupport.MAP_BATCH);
			chromatogram = loadChromatogram(file, batch);
		}
		//
		return chromatogram;
	}

	private IChromatogramTSD loadChromatogram(File file, boolean batch) {

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		ImportRunnableTSD runnable = new ImportRunnableTSD(file);
		try {
			/*
			 * No fork, otherwise it might crash when loading the data takes too long.
			 */
			boolean fork = !batch;
			dialog.run(fork, false, runnable);
		} catch(InvocationTargetException e) {
			logger.warn(e);
		} catch(InterruptedException e) {
			logger.warn(e);
			Thread.currentThread().interrupt();
		}
		//
		chromatogramFile = file;
		return runnable.getChromatogram();
	}

	private void createEditorPages(Composite parent) {

		chromatogramHeatmapUI = new ChromatogramHeatmapUI(parent, SWT.NONE);
	}
}
