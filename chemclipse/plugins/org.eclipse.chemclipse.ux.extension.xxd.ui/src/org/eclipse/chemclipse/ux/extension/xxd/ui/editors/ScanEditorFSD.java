/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.editors;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.fsd.converter.ui.swt.FluorescenceSpectrumFileSupport;
import org.eclipse.chemclipse.fsd.model.core.ISpectrumFSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.events.IPerspectiveAndViewIds;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.support.ui.workbench.EditorSupport;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.ui.editors.IScanEditorFSD;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.runnables.ScanFSDImportRunnable;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors.ExtendedFSDScanUI;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class ScanEditorFSD implements IScanEditorFSD {

	private static final Logger logger = Logger.getLogger(ScanEditorFSD.class);
	//
	public static final String ID = "org.eclipse.chemclipse.ux.extension.xxd.ui.part.scanEditorFSD";
	public static final String CONTRIBUTION_URI = "bundleclass://org.eclipse.chemclipse.ux.extension.xxd.ui/org.eclipse.chemclipse.ux.extension.xxd.ui.editors.ScanEditorFSD";
	public static final String ICON_URI = ApplicationImageFactory.getInstance().getURI(IApplicationImage.IMAGE_SCAN_FSD, IApplicationImageProvider.SIZE_16x16);
	public static final String TOOLTIP = ExtensionMessages.editorFSD;
	//
	private final MPart part;
	private final MDirtyable dirtyable;
	private final EModelService modelService;
	private final MApplication application;
	//
	private File scanFile;
	private ExtendedFSDScanUI extendedFSDScanUI;
	//
	private ISpectrumFSD spectrumFSD = null;
	//
	private final Shell shell;

	@Inject
	public ScanEditorFSD(Composite parent, MPart part, MDirtyable dirtyable, EModelService modelService, MApplication application, Shell shell) {

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

		extendedFSDScanUI.setFocus();
	}

	@PreDestroy
	protected void preDestroy() {

		List<String> clearTopics = Arrays.asList();
		UpdateNotifierUI.update(Display.getDefault(), IChemClipseEvents.TOPIC_EDITOR_FSD_CLOSE, clearTopics);
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
		/*
		 * Run the garbage collector.
		 */
		System.gc();
	}

	@Persist
	public void save() {

		// TODO
		System.out.println(scanFile);
		dirtyable.setDirty(false);
	}

	@Override
	public boolean saveAs() {

		try {
			FluorescenceSpectrumFileSupport.saveSpectrum(spectrumFSD);
		} catch(NoConverterAvailableException e) {
			return false;
		}
		dirtyable.setDirty(false);
		return true;
	}

	@Override
	public ISpectrumFSD getScanSelection() {

		return spectrumFSD;
	}

	private void initialize(Composite parent) {

		spectrumFSD = loadScan();
		createEditorPages(parent);
		extendedFSDScanUI.update(spectrumFSD);
	}

	private synchronized ISpectrumFSD loadScan() {

		ISpectrumFSD spectrumFSD = null;
		//
		try {
			Object object = part.getObject();
			if(object instanceof Map<?, ?> map) {
				/*
				 * String
				 */
				File file = new File((String)map.get(EditorSupport.MAP_FILE));
				boolean batch = (boolean)map.get(EditorSupport.MAP_BATCH);
				spectrumFSD = loadSpectrum(file, batch);
			}
		} catch(Exception e) {
			logger.error(e);
		}
		//
		return spectrumFSD;
	}

	private ISpectrumFSD loadSpectrum(File file, boolean batch) {

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		ScanFSDImportRunnable runnable = new ScanFSDImportRunnable(file);
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
		scanFile = file;
		return runnable.getSpectrumFSD();
	}

	private void createEditorPages(Composite parent) {

		createScanPage(parent);
	}

	private void createScanPage(Composite parent) {

		extendedFSDScanUI = new ExtendedFSDScanUI(parent, SWT.NONE);
	}
}
